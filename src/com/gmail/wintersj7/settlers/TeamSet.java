package com.gmail.wintersj7.settlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;


// Represents one instance of a settlers world, with all of its unique teams and scores. 
//
public class TeamSet {
	private SettlersTeam blueTeam, redTeam, whiteTeam, brownTeam;
	private Location blueSpawn, redSpawn, brownSpawn, whiteSpawn;
	private Location blueChest, redChest, brownChest, whiteChest;
	
	private Scoreboard scoreboard;
	private Objective objective;
	private World world;
	private Settlers thisPlugin;
	private HashSet<Settlers_Player> players;
	private NormalBoard normalBoard;
	private boolean gameOver;
	private ArrayList<DevelopmentCard> developmentCards;
	
	public TeamSet (HashSet<Player> queue, World world, Location[] spawns, Settlers settlers){
		this.thisPlugin = settlers;
		setGameOver(false);
		players = new HashSet<Settlers_Player>();
		
		// Just do it once when the TeamSet is instantiated.
		Bukkit.getServer().createWorld(new WorldCreator(world.getName()));
		world.setDifficulty(Difficulty.NORMAL);
		world.setMonsterSpawnLimit(0);
		world.setAnimalSpawnLimit(40);
		
		this.setWorld(world);
		
		// Spawns ************************************
		blueSpawn = spawns[0];
		redSpawn = spawns[1];
		brownSpawn = spawns[2];
		whiteSpawn = spawns[3];	
		
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective(world.getName() + "_score", "dummy");
		this.objective.setDisplayName("Visible Points");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "BLUE"));
		
		blueTeam = new SettlersTeam(scoreboard.registerNewTeam("blue"), this);
		blueTeam.getTeam().setDisplayName("BLUE");
		
		redTeam = new SettlersTeam(scoreboard.registerNewTeam("red"), this);
		redTeam.getTeam().setDisplayName("RED");
		
		brownTeam = new SettlersTeam(scoreboard.registerNewTeam("brown"), this);
		brownTeam.getTeam().setDisplayName("BROWN");
		
		whiteTeam = new SettlersTeam(scoreboard.registerNewTeam("white"), this);
		whiteTeam.getTeam().setDisplayName("WHITE");
		
		score.setScore(blueTeam.getScore());
		score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "RED"));
		score.setScore(redTeam.getScore());
		score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "BROWN"));
		score.setScore(brownTeam.getScore());
		score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE + "WHITE"));
		score.setScore(whiteTeam.getScore());
		
		Iterator<Player> it = queue.iterator();
		int selector = 0;
		
		while (it.hasNext()) {
			Settlers_Player sp = null;
			Player p = it.next();
			
			// Don't clear inventory until the timer expires and the game actually starts.
			// Do this in Settlers.startGame?
			//p.getInventory().clear();
			
			switch (selector) {
			case 0:
				sp = new Settlers_Player(p, this, this.getBlueTeam());
				toTeam(sp,"blue");
				break;
			case 1:
				sp = new Settlers_Player(p, this, this.getRedTeam());
				toTeam(sp,"red");
				break;
			case 2:
				sp = new Settlers_Player(p, this, this.getBrownTeam());
				toTeam(sp,"brown");
				break;
			case 3:
				sp = new Settlers_Player(p, this, this.getWhiteTeam());
				toTeam(sp,"white");
				break;
			}
			selector++;
			
			if (selector >= 3) {
				selector = 0;
			}
			
			if (sp != null)
				players.add(sp);
			else
				System.err.println("Error randomizing teams.");
		}
		
		
		developmentCards = new ArrayList<DevelopmentCard>();
		
		for (int i = 0; i < DevelopmentCard.KNIGHT; i++){
			DevelopmentCard newCard = new DevelopmentCard("knight");
			developmentCards.add(newCard);
		}
		
		for (int i = 0; i < DevelopmentCard.ROAD_BUILDING; i++){
			DevelopmentCard newCard = new DevelopmentCard("roadBuilding");
			developmentCards.add(newCard);
		}
		
		for (int i = 0; i < DevelopmentCard.MONOPOLY; i++){
			DevelopmentCard newCard = new DevelopmentCard("monopoly");
			developmentCards.add(newCard);
		}
		
		for (int i = 0; i < DevelopmentCard.YEAR_OF_PLENTY; i++){
			DevelopmentCard newCard = new DevelopmentCard("yearOfPlenty");
			developmentCards.add(newCard);
		}
		
		for (int i = 0; i < DevelopmentCard.VICTORY_POINT; i++){
			DevelopmentCard newCard = new DevelopmentCard("victoryPoint");
			developmentCards.add(newCard);
		}
		

	} // end constructor
	
	public DevelopmentCard drawCard (){
		int cardsLeft = this.getDevelopmentCards().size();
		int index = (int)(Math.random() * cardsLeft);
		DevelopmentCard drawnCard = this.getDevelopmentCards().get(index);	
		this.getDevelopmentCards().remove(index);
		return drawnCard;
	}
	
	public void toTeam(Settlers_Player set_player, String teamName){
		SettlersTeam team;
		
		ItemStack tunic = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam_tunic = (LeatherArmorMeta)tunic.getItemMeta();
		ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta lam_pants = (LeatherArmorMeta)pants.getItemMeta();

		Player player = set_player.getPlayer();
		
		switch (teamName) {
			case "blue":
				team = blueTeam;
				lam_tunic.setColor(Color.fromRGB(70,70,240));
				lam_pants.setColor(Color.fromRGB(70,70,240));
				player.setMetadata("settlers_team",  new FixedMetadataValue (thisPlugin,teamName));
				break;
			case "red":
				team = redTeam;
				lam_tunic.setColor(Color.fromRGB(150,1,1));
				lam_pants.setColor(Color.fromRGB(150,1,1));
				player.setMetadata("settlers_team",  new FixedMetadataValue (thisPlugin,teamName));
				break;
			case "brown":
				team = brownTeam;
				lam_tunic.setColor(Color.fromRGB(120,75,1));
				lam_pants.setColor(Color.fromRGB(120,75,1));
				player.setMetadata("settlers_team",  new FixedMetadataValue (thisPlugin,teamName));
				break;
			case "white":
				team = whiteTeam;
				lam_tunic.setColor(Color.fromRGB(255,255,255));
				lam_pants.setColor(Color.fromRGB(255,255,255));
				player.setMetadata("settlers_team",  new FixedMetadataValue (thisPlugin,teamName));
				break;
			case "none":
				team = null;
				blueTeam.getTeam().removePlayer((OfflinePlayer) player);
    			redTeam.getTeam().removePlayer((OfflinePlayer) player);
    			brownTeam.getTeam().removePlayer((OfflinePlayer) player);
    			whiteTeam.getTeam().removePlayer((OfflinePlayer) player);
    			player.getEquipment().setLeggings(null);
    			player.getEquipment().setChestplate(null);
    			player.removeMetadata("settlers_team", thisPlugin);
    			break;
			default:
				System.out.println("Bad team name. <blue, red, white, or brown>");
				team = null;
		}
		
		if (player.isOnline() && team != null) {
			blueTeam.getTeam().removePlayer((OfflinePlayer) player);
			redTeam.getTeam().removePlayer((OfflinePlayer) player);
			brownTeam.getTeam().removePlayer((OfflinePlayer) player);
			whiteTeam.getTeam().removePlayer((OfflinePlayer) player);
			
			tunic.setItemMeta(lam_tunic);
			pants.setItemMeta(lam_pants);
			
			player.getEquipment().setLeggings(pants);
			player.getEquipment().setChestplate(tunic);
			
			team.getTeam().addPlayer((OfflinePlayer) player);
						
			System.out.println("Team " + team.getTeam().getDisplayName() + " players: ");
			Iterator<OfflinePlayer> it = team.getTeam().getPlayers().iterator();
			
			while (it.hasNext()) {
				System.out.println(it.next().getName());
			}
		}
		
		if (player.isOnline() && team == null) {
			blueTeam.getTeam().removePlayer((OfflinePlayer) player);
			redTeam.getTeam().removePlayer((OfflinePlayer) player);
			brownTeam.getTeam().removePlayer((OfflinePlayer) player);
			whiteTeam.getTeam().removePlayer((OfflinePlayer) player);
			player.getEquipment().setLeggings(null);
			player.getEquipment().setChestplate(null);
		}
	}

	
	
	// Two TeamSets are equal if they have the same world.
	public boolean equals(Object other){
		if (!(other instanceof TeamSet)){
			return false;
		}
		else {		
			TeamSet otherSet = (TeamSet) other;
			
			if (otherSet.getWorld().getName().equals(this.getWorld().getName())) {
				return true;
			}
			
			return false;
		}
	}
	
	public boolean largestArmyCalc(SettlersTeam thisTeam) {
		if (thisTeam.getKnightAmount() < 3)
			return false;
		
		if (thisTeam.hasLargestArmy())
			return false;
		
		int blueK = this.getBlueTeam().getKnightAmount();
		int redK = this.getRedTeam().getKnightAmount();
		int brownK = this.getBrownTeam().getKnightAmount();
		int whiteK = this.getWhiteTeam().getKnightAmount();
		
		int t1 = Math.max(blueK, redK);
		int t2 = Math.max(t1, brownK);
		int t3 = Math.max(t2, whiteK);
		
		if (thisTeam.getKnightAmount() == t3){
			SettlersTeam[] teams = {getBlueTeam(), getRedTeam(), getBrownTeam(), getWhiteTeam()};
			
			for (int i = 0; i < teams.length; i++){
				if (teams[i].getKnightAmount() == t3){
					if (!(teams[i].getTeam().getName().equals(thisTeam.getTeam().getName()))){
						return false;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	
	public boolean longestRoadCalc(SettlersTeam thisTeam) {
		if (thisTeam.getLongRoad() < 5)
			return false;
		
		if (thisTeam.hasLongestRoad())
			return false;
		
		int blueK = this.getBlueTeam().getLongRoad();
		int redK = this.getRedTeam().getLongRoad();
		int brownK = this.getBrownTeam().getLongRoad();
		int whiteK = this.getWhiteTeam().getLongRoad();
		
		int t1 = Math.max(blueK, redK);
		int t2 = Math.max(t1, brownK);
		int t3 = Math.max(t2, whiteK);
		
		if (thisTeam.getLongRoad() == t3){
			SettlersTeam[] teams = {getBlueTeam(), getRedTeam(), getBrownTeam(), getWhiteTeam()};
			
			for (int i = 0; i < teams.length; i++){
				if (teams[i].getLongRoad() == t3){
					if (!(teams[i].getTeam().getName().equals(thisTeam.getTeam().getName()))){
						return false;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public SettlersTeam getBlueTeam() {
		return blueTeam;
	}

	public void setBlueTeam(SettlersTeam blueTeam) {
		this.blueTeam = blueTeam;
	}

	public SettlersTeam getRedTeam() {
		return redTeam;
	}

	public void setRedTeam(SettlersTeam redTeam) {
		this.redTeam = redTeam;
	}

	public SettlersTeam getWhiteTeam() {
		return whiteTeam;
	}

	public void setWhiteTeam(SettlersTeam whiteTeam) {
		this.whiteTeam = whiteTeam;
	}

	public SettlersTeam getBrownTeam() {
		return brownTeam;
	}

	public void setBrownTeam(SettlersTeam brownTeam) {
		this.brownTeam = brownTeam;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}
	
	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public Location getBlueSpawn() {
		return blueSpawn;
	}

	public void setBlueSpawn(Location blueSpawn) {
		this.blueSpawn = blueSpawn;
	}

	public Location getRedSpawn() {
		return redSpawn;
	}

	public void setRedSpawn(Location redSpawn) {
		this.redSpawn = redSpawn;
	}

	public Location getBrownSpawn() {
		return brownSpawn;
	}

	public void setBrownSpawn(Location brownSpawn) {
		this.brownSpawn = brownSpawn;
	}

	public Location getWhiteSpawn() {
		return whiteSpawn;
	}

	public void setWhiteSpawn(Location whiteSpawn) {
		this.whiteSpawn = whiteSpawn;
	}

	public Location getBlueChest() {
		return blueChest;
	}

	public void setBlueChest(Location blueChest) {
		this.blueChest = blueChest;
	}

	public Location getRedChest() {
		return redChest;
	}

	public void setRedChest(Location redChest) {
		this.redChest = redChest;
	}

	public Location getBrownChest() {
		return brownChest;
	}

	public void setBrownChest(Location brownChest) {
		this.brownChest = brownChest;
	}

	public Location getWhiteChest() {
		return whiteChest;
	}

	public void setWhiteChest(Location whiteChest) {
		this.whiteChest = whiteChest;
	}
	
	public HashSet<Settlers_Player> getPlayers(){
		return this.players;
	}

	public NormalBoard getNormalBoard() {
		return normalBoard;
	}

	public void setNormalBoard(NormalBoard normalBoard) {
		this.normalBoard = normalBoard;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public ArrayList<DevelopmentCard> getDevelopmentCards(){
		return this.developmentCards;
	}
	
}
