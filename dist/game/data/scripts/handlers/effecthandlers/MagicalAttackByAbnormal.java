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
import org.l2s.gameserver.model.effects.EffectType;
import org.l2s.gameserver.model.items.instance.ItemInstance;
import org.l2s.gameserver.model.skills.Skill;
import org.l2s.gameserver.model.stats.Formulas;

/**
 * Magical Attack By Abnormal effect implementation.
 * @author Adry_85
 */
public class MagicalAttackByAbnormal extends AbstractEffect
{
	private final double _power;
	
	public MagicalAttackByAbnormal(StatsSet params)
	{
		_power = params.getDouble("power", 0);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MAGICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (effector.isAlikeDead())
		{
			return;
		}
		
		if (effected.isPlayer() && effected.getActingPlayer().isFakeDeath() && Config.FAKE_DEATH_DAMAGE_STAND)
		{
			effected.stopFakeDeath(true);
		}
		
		final boolean sps = skill.useSpiritShot() && effector.isChargedShot(ShotType.SPIRITSHOTS);
		final boolean bss = skill.useSpiritShot() && effector.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		final boolean mcrit = Formulas.calcCrit(skill.getMagicCriticalRate(), effector, effected, skill);
		double damage = Formulas.calcMagicDam(effector, effected, skill, effector.getMAtk(), _power, effected.getMDef(), sps, bss, mcrit);
		
		// each buff increase +30%
		damage *= (((effected.getBuffCount() * 0.3) + 1.3) / 4);
		
		effector.doAttack(damage, effected, skill, false, false, mcrit, false);
	}
}