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
package org.l2s.gameserver.network.clientpackets;

import java.util.OptionalInt;

import org.l2s.commons.network.PacketReader;
import org.l2s.gameserver.instancemanager.ClanEntryManager;
import org.l2s.gameserver.model.actor.instance.PlayerInstance;
import org.l2s.gameserver.network.GameClient;
import org.l2s.gameserver.network.serverpackets.ExPledgeWaitingListApplied;

/**
 * @author Sdw
 */
public class RequestPledgeWaitingApplied implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || (player.getClan() != null))
		{
			return;
		}
		
		final OptionalInt clanId = ClanEntryManager.getInstance().getClanIdForPlayerApplication(player.getObjectId());
		if (clanId.isPresent())
		{
			player.sendPacket(new ExPledgeWaitingListApplied(clanId.getAsInt(), player.getObjectId()));
		}
	}
}
