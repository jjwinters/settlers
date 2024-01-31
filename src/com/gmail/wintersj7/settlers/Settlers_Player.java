package com.gmail.wintersj7.settlers;

import org.bukkit.entity.Player;

// A wrapper class for a player.
//
public class Settlers_Player {
	private Player player;
	private TeamSet teamSet;
	private SettlersTeam team;
	
	public Settlers_Player (Player player, TeamSet teamSet, SettlersTeam team) {
		this.setPlayer(player);
		this.setTeamSet(teamSet);
		this.setTeam(team);
	}
	
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public TeamSet getTeamSet() {
		return teamSet;
	}
	public void setTeamSet(TeamSet teamSet) {
		this.teamSet = teamSet;
	}
	public SettlersTeam getTeam() {
		return team;
	}
	public void setTeam(SettlersTeam team) {
		this.team = team;
	}
	
}
