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
package ai.areas.RuinsOfDespair;

import org.l2s.gameserver.enums.ChatType;
import org.l2s.gameserver.model.actor.Npc;
import org.l2s.gameserver.model.actor.instance.PlayerInstance;
import org.l2s.gameserver.network.NpcStringId;

import ai.AbstractNpcAI;

/**
 * Guard AI.
 * @author St3eT
 */
public class RoDGuard extends AbstractNpcAI
{
	// NPCs
	private static final int GUARD = 33432;
	
	private RoDGuard()
	{
		addSpawnId(GUARD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, PlayerInstance player)
	{
		if (event.equals("NPC_SHOUT"))
		{
			npc.broadcastSay(ChatType.NPC_GENERAL, NpcStringId.THIS_IS_THE_RUINS_OF_AGONY_WHERE_POSLOF_IS);
			startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onSpawn(Npc npc)
	{
		startQuestTimer("NPC_SHOUT", (10 + getRandom(5)) * 1000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new RoDGuard();
	}
}