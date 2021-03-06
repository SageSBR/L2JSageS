/*
 * This file is part of the L2J SageS project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import org.l2s.Config;
import org.l2s.gameserver.enums.ShotType;
import org.l2s.gameserver.model.StatsSet;
import org.l2s.gameserver.model.actor.Creature;
import org.l2s.gameserver.model.effects.AbstractEffect;
import org.l2s.gameserver.model.effects.EffectFlag;
import org.l2s.gameserver.model.effects.EffectType;
import org.l2s.gameserver.model.items.instance.ItemInstance;
import org.l2s.gameserver.model.items.type.CrystalType;
import org.l2s.gameserver.model.skills.Skill;
import org.l2s.gameserver.model.stats.Formulas;
import org.l2s.gameserver.model.stats.Stats;
import org.l2s.gameserver.network.SystemMessageId;
import org.l2s.gameserver.network.serverpackets.ExMagicAttackInfo;
import org.l2s.gameserver.network.serverpackets.SystemMessage;

/**
 * HpCpHeal effect implementation.
 * @author Sdw
 */
public class HpCpHeal extends AbstractEffect
{
	private final double _power;
	
	public HpCpHeal(StatsSet params)
	{
		_power = params.getDouble("power", 0);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.HEAL;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effected.isDead() || effected.isDoor() || effected.isHpBlocked())
		{
			return;
		}
		
		if ((effected != effector) && effected.isAffected(EffectFlag.FACEOFF))
		{
			return;
		}
		
		double amount = _power;
		double staticShotBonus = 0;
		double mAtkMul = 1;
		final boolean sps = skill.isMagic() && effector.isChargedShot(ShotType.SPIRITSHOTS);
		final boolean bss = skill.isMagic() && effector.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		final double shotsBonus = effector.getStat().getValue(Stats.SHOTS_BONUS);
		
		if (((sps || bss) && (effector.isPlayer() && effector.getActingPlayer().isMageClass())) || effector.isSummon())
		{
			staticShotBonus = skill.getMpConsume(); // static bonus for spiritshots
			mAtkMul = bss ? 4 * shotsBonus : 2 * shotsBonus;
			staticShotBonus *= bss ? 2.4 : 1.0;
		}
		else if ((sps || bss) && effector.isNpc())
		{
			staticShotBonus = 2.4 * skill.getMpConsume(); // always blessed spiritshots
			mAtkMul = 4 * shotsBonus;
		}
		else
		{
			// no static bonus
			// grade dynamic bonus
			final ItemInstance weaponInst = effector.getActiveWeaponInstance();
			if (weaponInst != null)
			{
				mAtkMul = weaponInst.getItem().getCrystalTypePlus() == CrystalType.R ? 4 : weaponInst.getItem().getCrystalTypePlus() == CrystalType.S ? 2 : 1;
			}
			// shot dynamic bonus
			mAtkMul = bss ? mAtkMul * 4 : mAtkMul + 1;
		}
		
		if (!skill.isStatic())
		{
			amount += staticShotBonus + Math.sqrt(mAtkMul * effector.getMAtk());
			amount *= effected.getStat().getValue(Stats.HEAL_EFFECT, 1);
			amount += effected.getStat().getValue(Stats.HEAL_EFFECT_ADD, 0);
			amount *= (item == null) && effector.isPlayable() ? Config.PLAYER_HEALING_SKILL_MULTIPLIERS.getOrDefault(effector.getActingPlayer().getClassId(), 1f) : 1;
			// Heal critic, since CT2.3 Gracia Final
			if (skill.isMagic() && (Formulas.calcCrit(skill.getMagicCriticalRate(), effector, effected, skill) || effector.isAffected(EffectFlag.HPCPHEAL_CRITICAL)))
			{
				amount *= 3;
				effector.sendPacket(SystemMessageId.M_CRITICAL);
				effector.sendPacket(new ExMagicAttackInfo(effector.getObjectId(), effected.getObjectId(), ExMagicAttackInfo.CRITICAL_HEAL));
				if (effected.isPlayer() && (effected != effector))
				{
					effected.sendPacket(new ExMagicAttackInfo(effector.getObjectId(), effected.getObjectId(), ExMagicAttackInfo.CRITICAL_HEAL));
				}
			}
		}
		
		// Prevents overheal and negative amount
		final double healAmount = Math.max(Math.min(amount, effected.getMaxRecoverableHp() - effected.getCurrentHp()), 0);
		if (healAmount != 0)
		{
			final double newHp = healAmount + effected.getCurrentHp();
			effected.setCurrentHp(newHp, false);
		}
		
		if (effected.isPlayer())
		{
			if (effector.isPlayer() && (effector != effected))
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.S2_HP_HAS_BEEN_RESTORED_BY_C1);
				sm.addString(effector.getName());
				sm.addInt((int) healAmount);
				effected.sendPacket(sm);
			}
			else
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.S1_HP_HAS_BEEN_RESTORED);
				sm.addInt((int) healAmount);
				effected.sendPacket(sm);
			}
			
			amount = Math.max(Math.min(amount - healAmount, effected.getMaxRecoverableCp() - effected.getCurrentCp()), 0);
			if (amount != 0)
			{
				final double newCp = amount + effected.getCurrentCp();
				effected.setCurrentCp(newCp, false);
			}
			
			if (effector.isPlayer() && (effector != effected))
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.S2_CP_HAS_BEEN_RESTORED_BY_C1);
				sm.addString(effector.getName());
				sm.addInt((int) amount);
				effected.sendPacket(sm);
			}
			else
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.S1_CP_HAS_BEEN_RESTORED);
				sm.addInt((int) amount);
				effected.sendPacket(sm);
			}
		}
		effected.broadcastStatusUpdate(effector);
	}
}
