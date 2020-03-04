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
import org.l2s.gameserver.handler.IUserCommandHandler;
import org.l2s.gameserver.handler.UserCommandHandler;
import org.l2s.gameserver.model.actor.instance.PlayerInstance;
import org.l2s.gameserver.network.GameClient;

/**
 * @version $Revision: 1.1.2.1.2.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class BypassUserCmd implements IClientIncomingPacket
{
	private int _command;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_command = packet.readD();
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
		
		final IUserCommandHandler handler = UserCommandHandler.getInstance().getHandler(_command);
		if (handler == null)
		{
			if (player.isGM())
			{
				player.sendMessage("User commandID " + _command + " not implemented yet.");
			}
		}
		else
		{
			handler.useUserCommand(_command, client.getPlayer());
		}
	}
}