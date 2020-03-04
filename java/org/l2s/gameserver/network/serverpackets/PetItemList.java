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
package org.l2s.gameserver.network.serverpackets;

import java.util.Collection;

import org.l2s.commons.network.PacketWriter;
import org.l2s.gameserver.model.items.instance.ItemInstance;
import org.l2s.gameserver.network.OutgoingPackets;

public class PetItemList extends AbstractItemPacket
{
	private final Collection<ItemInstance> _items;
	
	public PetItemList(Collection<ItemInstance> items)
	{
		_items = items;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.PET_ITEM_LIST.writeId(packet);
		
		packet.writeH(_items.size());
		for (ItemInstance item : _items)
		{
			writeItem(packet, item);
		}
		return true;
	}
}