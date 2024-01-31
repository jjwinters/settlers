package com.gmail.wintersj7.settlers;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

// This class is a wrapper for a Bukkit Team that includes a few extra counters for the Settlers game.

public class SettlersTeam {

	private Team team;
	private TeamSet teamSet;
	private int score;
	private int roadsBuilt, settlementsBuilt, citiesBuilt;
	private int developmentCards;
	private ChatColor chatColor;
	private boolean canPlayCard;
	private boolean threePort, breadPort, woolPort, woodPort, brickPort, ironPort;
	private int knightAmount;
	private int longRoad;
	private boolean hasLargestArmy, hasLongestRoad;
	private boolean cardBlocked;
	
	
	public SettlersTeam (Team team, TeamSet teamSet){
		this.setTeam(team);
		this.setScore(0);
		this.setRoadsBuilt(0);
		this.setSettlementsBuilt(0);
		this.setCitiesBuilt(0);
		this.setDevelopmentCards(0);
		this.setCanPlayCard(true);
		this.setChatColor(ChatColor.WHITE);
		this.setTeamSet(teamSet);
		this.setLongRoad(0);
		this.setKnightAmount(0);
		this.setHasLargestArmy(false);
		this.setHasLongestRoad(false);
		this.setCardBlocked(false);
		
		this.setThreePort(false);
		this.setBreadPort(false);
		this.setWoodPort(false);
		this.setWoolPort(false);
		this.setIronPort(false);
		this.setBrickPort(false);
		
		String name = team.getName();
		switch (name){
		case "blue":
			this.setChatColor(ChatColor.BLUE);
			break;
		case "red":
			this.setChatColor(ChatColor.RED);
			break;
		case "brown":
			this.setChatColor(ChatColor.GOLD);
			break;
		case "white":
			this.setChatColor(ChatColor.WHITE);
			break;
		}
		
		
	}


	public Team getTeam() {
		return team;
	}


	public String toString() {
		return this.getTeam().getDisplayName();
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public int getRoadsBuilt() {
		return roadsBuilt;
	}


	public void setRoadsBuilt(int roadsBuilt) {
		this.roadsBuilt = roadsBuilt;
	}


	public int getSettlementsBuilt() {
		return settlementsBuilt;
	}


	public void setSettlementsBuilt(int settlementsBuilt) {
		this.settlementsBuilt = settlementsBuilt;
	}

	public int getCitiesBuilt() {
		return citiesBuilt;
	}

	public void setCitiesBuilt(int citiesBuilt) {
		this.citiesBuilt = citiesBuilt;
	}

	public int getDevelopmentCards() {
		return developmentCards;
	}

	public void setDevelopmentCards(int developmentCards) {
		this.developmentCards = developmentCards;
	}

	public boolean isCanPlayCard() {
		return canPlayCard;
	}

	public void setCanPlayCard(boolean canPlayCard) {
		this.canPlayCard = canPlayCard;
	}

	public ChatColor getChatColor() {
		return chatColor;
	}

	public void setChatColor(ChatColor chatColor) {
		this.chatColor = chatColor;
	}

	public boolean hasThreePort() {
		return threePort;
	}

	public void setThreePort(boolean threePort) {
		this.threePort = threePort;
	}

	public boolean hasBreadPort() {
		return breadPort;
	}

	public void setBreadPort(boolean breadPort) {
		this.breadPort = breadPort;
	}

	public boolean hasWoolPort() {
		return woolPort;
	}

	public void setWoolPort(boolean woolPort) {
		this.woolPort = woolPort;
	}

	public boolean hasWoodPort() {
		return woodPort;
	}

	public void setWoodPort(boolean woodPort) {
		this.woodPort = woodPort;
	}

	public boolean hasBrickPort() {
		return brickPort;
	}

	public void setBrickPort(boolean brickPort) {
		this.brickPort = brickPort;
	}


	public boolean hasIronPort() {
		return ironPort;
	}

	public void setIronPort(boolean ironPort) {
		this.ironPort = ironPort;
	}


	public TeamSet getTeamSet() {
		return teamSet;
	}


	public void setTeamSet(TeamSet teamSet) {
		this.teamSet = teamSet;
	}


	public int getKnightAmount() {
		return knightAmount;
	}


	public void setKnightAmount(int knightAmount) {
		this.knightAmount = knightAmount;
	}


	public boolean hasLargestArmy() {
		return hasLargestArmy;
	}


	public void setHasLargestArmy(boolean hasLargestArmy) {
		this.hasLargestArmy = hasLargestArmy;
	}


	public int getLongRoad() {
		return longRoad;
	}


	public void setLongRoad(int longRoad) {
		this.longRoad = longRoad;
	}


	public boolean hasLongestRoad() {
		return hasLongestRoad;
	}


	public void setHasLongestRoad(boolean hasLongestRoad) {
		this.hasLongestRoad = hasLongestRoad;
	}


	public boolean isCardBlocked() {
		return cardBlocked;
	}


	public void setCardBlocked(boolean cardBlocked) {
		this.cardBlocked = cardBlocked;
	}
	
}
