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
package org.l2s.gameserver.network.serverpackets.faction;

import org.l2s.commons.network.PacketWriter;
import org.l2s.gameserver.enums.Faction;
import org.l2s.gameserver.model.actor.instance.PlayerInstance;
import org.l2s.gameserver.network.OutgoingPackets;
import org.l2s.gameserver.network.serverpackets.IClientOutgoingPacket;

/**
 * @author Mathael, Mobius
 */
public class ExFactionInfo implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private final boolean _openDialog;
	
	public ExFactionInfo(PlayerInstance player, boolean openDialog)
	{
		_player = player;
		_openDialog = openDialog;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_FACTION_INFO.writeId(packet);
		
		packet.writeD(_player.getObjectId());
		packet.writeC(_openDialog ? 1 : 0);
		packet.writeD(Faction.values().length);
		
		for (Faction faction : Faction.values())
		{
			packet.writeC(faction.getId());
			packet.writeH(_player.getFactionLevel(faction));
			packet.writeE(_player.getFactionProgress(faction));
		}
		
		return true;
	}
}
