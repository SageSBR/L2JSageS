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
package org.l2s.gameserver.model.stats.finalizers;

import java.util.OptionalDouble;

import org.l2s.Config;
import org.l2s.gameserver.model.actor.Creature;
import org.l2s.gameserver.model.items.Item;
import org.l2s.gameserver.model.stats.IStatsFunction;
import org.l2s.gameserver.model.stats.Stats;

/**
 * @author UnAfraid
 */
public class MEvasionRateFinalizer implements IStatsFunction
{
	@Override
	public double calc(Creature creature, OptionalDouble base, Stats stat)
	{
		throwIfPresent(base);
		
		double baseValue = calcWeaponPlusBaseValue(creature, stat);
		
		final int level = creature.getLevel();
		if (creature.isPlayer())
		{
			// [Square(WIT)] * 3 + lvl;
			baseValue += (Math.sqrt(creature.getWIT()) * 3) + (level * 2);
			
			// Enchanted helm bonus
			baseValue += calcEnchantBodyPart(creature, Item.SLOT_HEAD);
		}
		else
		{
			// [Square(DEX)] * 6 + lvl;
			baseValue += (Math.sqrt(creature.getWIT()) * 3) + (level * 2);
			if (level > 69)
			{
				baseValue += (level - 69) + 2;
			}
		}
		return validateValue(creature, Stats.defaultValue(creature, stat, baseValue), Double.NEGATIVE_INFINITY, Config.MAX_EVASION);
	}
	
	@Override
	public double calcEnchantBodyPartBonus(int enchantLevel, boolean isBlessed)
	{
		if (isBlessed)
		{
			return (0.3 * Math.max(enchantLevel - 3, 0)) + (0.3 * Math.max(enchantLevel - 6, 0));
		}
		
		return (0.2 * Math.max(enchantLevel - 3, 0)) + (0.2 * Math.max(enchantLevel - 6, 0));
	}
}
