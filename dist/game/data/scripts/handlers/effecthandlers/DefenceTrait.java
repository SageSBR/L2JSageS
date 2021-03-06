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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.l2s.gameserver.model.StatsSet;
import org.l2s.gameserver.model.actor.Creature;
import org.l2s.gameserver.model.effects.AbstractEffect;
import org.l2s.gameserver.model.items.instance.ItemInstance;
import org.l2s.gameserver.model.skills.Skill;
import org.l2s.gameserver.model.stats.TraitType;

/**
 * Defence Trait effect implementation.
 * @author NosBit
 */
public class DefenceTrait extends AbstractEffect
{
	private final Map<TraitType, Float> _defenceTraits = new HashMap<>();
	
	public DefenceTrait(StatsSet params)
	{
		if (params.isEmpty())
		{
			LOGGER.warning(getClass().getSimpleName() + ": must have parameters.");
			return;
		}
		
		for (Entry<String, Object> param : params.getSet().entrySet())
		{
			_defenceTraits.put(TraitType.valueOf(param.getKey()), Float.parseFloat((String) param.getValue()) / 100);
		}
	}
	
	@Override
	public void onStart(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		for (Entry<TraitType, Float> trait : _defenceTraits.entrySet())
		{
			if (trait.getValue() < 1.0f)
			{
				effected.getStat().mergeDefenceTrait(trait.getKey(), trait.getValue());
			}
			else
			{
				effected.getStat().mergeInvulnerableTrait(trait.getKey());
			}
		}
	}
	
	@Override
	public void onExit(Creature effector, Creature effected, Skill skill)
	{
		for (Entry<TraitType, Float> trait : _defenceTraits.entrySet())
		{
			if (trait.getValue() < 1.0f)
			{
				effected.getStat().removeDefenceTrait(trait.getKey(), trait.getValue());
			}
			else
			{
				effected.getStat().removeInvulnerableTrait(trait.getKey());
			}
		}
	}
}
