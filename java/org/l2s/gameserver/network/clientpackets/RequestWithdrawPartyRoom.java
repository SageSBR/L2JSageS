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

import org.l2s.commons.network.PacketReader;
import org.l2s.gameserver.enums.MatchingRoomType;
import org.l2s.gameserver.model.actor.instance.PlayerInstance;
import org.l2s.gameserver.model.matching.MatchingRoom;
import org.l2s.gameserver.network.GameClient;

/**
 * @author Gnacik
 */
public class RequestWithdrawPartyRoom implements IClientIncomingPacket
{
	private int _roomId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_roomId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final MatchingRoom room = player.getMatchingRoom();
		if (room == null)
		{
			return;
		}
		
		if ((room.getId() != _roomId) || (room.getRoomType() != MatchingRoomType.PARTY))
		{
			return;
		}
		
		room.deleteMember(player, false);
	}
}
