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
package org.l2s.gameserver.model.events.impl.creature.player;

import org.l2s.gameserver.model.clan.Clan;
import org.l2s.gameserver.model.clan.ClanMember;
import org.l2s.gameserver.model.events.EventType;
import org.l2s.gameserver.model.events.impl.IBaseEvent;

/**
 * @author UnAfraid
 */
public class OnPlayerClanDestroy implements IBaseEvent
{
	private final ClanMember _clanMember;
	private final Clan _clan;
	
	public OnPlayerClanDestroy(ClanMember clanMember, Clan clan)
	{
		_clanMember = clanMember;
		_clan = clan;
	}
	
	public ClanMember getClanMember()
	{
		return _clanMember;
	}
	
	public Clan getClan()
	{
		return _clan;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_CLAN_DESTROY;
	}
}
