package com.gmail.wintersj7.settlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;


public class DevelopmentCard {
	static final int ROAD_BUILDING = 2;
	static final int MONOPOLY = 2;
	static final int YEAR_OF_PLENTY = 2;
	static final int VICTORY_POINT = 5;
	static final int KNIGHT = 14;
	//static final int KNIGHT = 1;
	static final int TOTAL = 25;
	
	private static ItemStack roadBuildingItem, yearOfPlentyItem, monopolyItem, knightItem, victoryPointItem;
	
	static final Material roadBuildingMat = Material.BRICK_STAIRS;
	static final Material yearOfPlentyMat = Material.MELON_BLOCK;
	static final Material monopolyMat = Material.CAKE_BLOCK;
	static final Material knightMat = Material.SKULL;
	static final Material victoryPointMat = Material.SPONGE;
	
	private String name;

	
	public DevelopmentCard (String name){
		this.setName(name);

		roadBuildingItem = new ItemStack(roadBuildingMat);
		ItemMeta im = roadBuildingItem.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Development Card");
		lore.add("Place anywhere to use.");
		lore.add("Gives two placeable roads.");
		im.setDisplayName("Road Building");
		im.setLore(lore);
		roadBuildingItem.setItemMeta(im);
		
		yearOfPlentyItem = new ItemStack(yearOfPlentyMat);
		im = yearOfPlentyItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Development Card");
		lore.add("Place anywhere to use.");
		lore.add("Gives rupees for 2 resources");
		im.setDisplayName("Year of Plenty");
		im.setLore(lore);
		yearOfPlentyItem.setItemMeta(im);
		
		monopolyItem = new ItemStack(monopolyMat);
		im = monopolyItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Development Card");
		lore.add("Place on log -> steal all wood.");
		lore.add("Place on grass -> steal all wool.");
		lore.add("Place on pumpkin -> steal all bread.");
		lore.add("Place on ore -> steal all ingots.");
		lore.add("Place on clay -> steal all bricks.");
		im.setDisplayName("Monopoly");
		im.setLore(lore);
		monopolyItem.setItemMeta(im);
		
		knightItem = new ItemStack(knightMat);
		im = knightItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Development Card");
		lore.add("Place on a GOLD block to use.");
		lore.add("Gold blocks are in the center of hexagons.");
		lore.add("Moves the skull:");
		lore.add("Blocks resources and haunts the tile!");
		im.setDisplayName("Knight");
		im.setLore(lore);
		knightItem.setItemMeta(im);
		
		victoryPointItem = new ItemStack(victoryPointMat);
		im = victoryPointItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Development Card");
		lore.add("Place anywhere to use.");
		lore.add("Permanently increases your score by 1.");
		lore.add("Useable ANY time.");
		im.setDisplayName("Victory Point");
		im.setLore(lore);
		victoryPointItem.setItemMeta(im);
		

	}


	/*public void execute(SettlersTeam settlersTeam, Player player){
		if (this.getName().equals("knight")) {
			knightEffect(settlersTeam, player);
		} 
		else if (this.getName().equals("roadBuilding")) {
			roadBuildingEffect(settlersTeam, player);
		} 
		else if (this.getName().equals("yearOfPlenty")) {
			yearOfPlentyEffect(settlersTeam, player);
		} 
		else if (this.getName().equals("victoryPoint")) {
			victoryPointEffect(settlersTeam, player);
		} 
		else
			System.err.println("Wrong method signature used for development card execution - monopoly, or unkown dev card.");
		
	}
	
	public void execute(SettlersTeam settlersTeam, Settlers_Player setPlayer, ItemStack target){
		if (this.getName().equals("monopoly")) {
			monopolyEffect(settlersTeam, setPlayer.getPlayer(), target);
		} 
		else
			System.err.println("Wrong method signature used for development card execution - monopoly only, or unknown dev card.");
		
	}*/
	
	@SuppressWarnings("deprecation")
	public static void roadBuildingEffect(SettlersTeam settlersTeam, Player player) {
		player.getInventory().addItem(new ItemStack(Settlers.getRoadItem()));
		player.getInventory().addItem(new ItemStack(Settlers.getRoadItem()));
		//player.getInventory().setContents(player.getInventory().getContents());
		player.updateInventory();
		Settlers.worldBroadcast(player.getWorld(), settlersTeam.getChatColor() + player.getDisplayName() + " has obtained two roads by using a ROAD BUILDING card!");
	}
	
	@SuppressWarnings("deprecation")
	public static void yearOfPlentyEffect(SettlersTeam settlersTeam, Player player) {
		player.getInventory().addItem(new ItemStack(Settlers.getRupeeItem()));
		player.getInventory().addItem(new ItemStack(Settlers.getRupeeItem()));
		//player.getInventory().setContents(player.getInventory().getContents());
		player.updateInventory();
		Settlers.worldBroadcast(player.getWorld(), settlersTeam.getChatColor() + player.getDisplayName() + " used YEAR OF PLENTY to get two resources!");
	}
	
	@SuppressWarnings("deprecation")
	public static void monopolyEffect(SettlersTeam settlersTeam, Player player, ItemStack target) {
		World world = player.getWorld();
		List<Player> players = world.getPlayers();
		Set<OfflinePlayer> teammates = settlersTeam.getTeam().getPlayers();
		ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
		
		Iterator<OfflinePlayer> it1 = teammates.iterator();
		while (it1.hasNext()){
			Player temp = (Player) it1.next();
			players.remove(temp);
		}
		
		Iterator<Player> it2 = players.iterator();
		while (it2.hasNext()){
			Player temp = it2.next();
			Inventory inv = temp.getInventory();
			
			ListIterator<ItemStack> lit = inv.iterator();
			while (lit.hasNext()){
				ItemStack tempItem = lit.next();
				if (tempItem != null) {
					if (tempItem.getType().equals(target.getType())){
						loot.add(new ItemStack(tempItem));
						
					}
				}
			}
			
			inv.remove(target.getType());
			
		}
		
		Inventory playerInv = player.getInventory();
		Iterator<ItemStack> it3 = loot.iterator();
		
		while (it3.hasNext()){
			if (playerInv.firstEmpty() > -1){
				playerInv.addItem(it3.next());
			}
			else {
				player.getWorld().dropItem(player.getLocation(), it3.next());
				player.sendMessage("Your stolen items didn't all fit and were dropped at your feet!");
			}
		}
		
		//player.getInventory().setContents(player.getInventory().getContents());
		player.updateInventory();
		
		player.sendMessage("You stole " + loot.size() + " " + target.getItemMeta().getDisplayName() + " from everyone!");
		Settlers.worldBroadcast(player.getWorld(), settlersTeam.getChatColor() + player.getDisplayName() + " used MONOPOLY to steal everyone's " + target.getItemMeta().getDisplayName() + "!");
	}
	
	public static void knightEffect(SettlersTeam settlersTeam, Player player) {
		
		Settlers.worldBroadcast(player.getWorld(), settlersTeam.getChatColor() + player.getDisplayName() + " played a KNIGHT! (" + settlersTeam.getKnightAmount() + " total)");
	}
	
	public static void victoryPointEffect(SettlersTeam settlersTeam, Player player) {
		settlersTeam.setScore(settlersTeam.getScore() + 1);
		Objective objective = settlersTeam.getTeamSet().getObjective();
		
		Score score = null;
		
		String name = settlersTeam.getTeam().getName();
		switch (name){
		case "blue":
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "BLUE"));
			break;
		case "red":
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "RED"));
			break;
		case "brown":
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "BROWN"));
			break;
		case "white":
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE + "WHITE"));
			break;
		default:
			System.err.println("Can't add to score; someone played a victory point while not on a team.");
			
		}    			
		
		score.setScore(settlersTeam.getScore());
		
		Settlers.worldBroadcast(player.getWorld(), settlersTeam.getChatColor() + player.getDisplayName() + " revealed a VICTORY POINT for team " + settlersTeam.getTeam().getDisplayName() + "!");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static ItemStack getRoadBuildingItem() {
		return roadBuildingItem;
	}
	
	public static ItemStack getYearOfPlentyItem() {
		return yearOfPlentyItem;
	}

	public static ItemStack getMonopolyItem() {
		return monopolyItem;
	}

	public static ItemStack getKnightItem() {
		return knightItem;
	}

	public static ItemStack getVictoryPointItem() {
		return victoryPointItem;
	}
	

}
