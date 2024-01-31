package com.gmail.wintersj7.settlers;

import java.io.File;
import java.io.IOException;
//import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.map.MapView;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.apache.commons.io.FileUtils;


public class Settlers extends JavaPlugin {
	
	public static final int ROAD_WIDTH = 5;
	private final int BASE_ELEVATION = 63;
	private static int NUMBER_HEIGHT;
	private final int NUMBER_THICKNESS = 8;
	private final Material NUMBER_MATERIAL = Material.SNOW;
	private final Material NUMBER_MATERIAL_OFF = Material.SNOW;
	private final Material SKY_SKULL_MATERIAL = Material.STONE;
	private final int SETTLEMENT_DIMS = 20;
	private final int CITY_DIMS = 10;
	private final int SKULL_DIMS = 30;
	private static int MAX_WORLD_ENTITIES = 350;
	private final int SPRINT_DURATION = 20;
	
	private final int LIGHT_BLUE_WOOL = 3;
	private final int WHITE_WOOL = 0;
	private final int BROWN_WOOL = 12;
	private final int RED_WOOL = 14;
	
	private final int ROLL_CEILING = 100;
	private int TREE_DIFFICULTY; // in config
	private int CLAY_DIFFICULTY;
	private int WHAT_DIFFICULTY;
	private int ORE_DIFFICULTY;
	private int WOOL_DIFFICULTY;
	private int REFINE_AMOUNT;
	
	private float TOOL_CHANCE;
	private int MAX_RESOURCES_HELD;
	
	private int RESOURCE_TIMER;
	private int COUNTDOWN = 600; // 2100
	private int MIN_QUEUE_SIZE;
	
	private final int MAX_SETTLEMENTS = 5;
	private final int MAX_CITIES = 4;
	private final int MAX_ROADS = 15;
	
	private static int MAX_WORLDS; // in config
	private static int POINTS_TO_WIN; // in config
	private static int CARD_COOLDOWN; 
	private int HEX_RADIUS;
	private boolean SKY_NUMBERS_ON;
	private ArrayList<String> BLOCKS_BREAKABLE;
	private float SPRINT_SPEED_1;
	private float SPRINT_SPEED_2;
	
	static final int ENDGAME_DELAY = 100; // ticks; 20 = 1sec
	
	private int BLUE_X;
	private int BLUE_Y;
	private int BLUE_Z;
	private int RED_X;
	private int RED_Y;
	private int RED_Z;
	private int BROWN_X;
	private int BROWN_Y;
	private int BROWN_Z;
	private int WHITE_X;
	private int WHITE_Y;
	private int WHITE_Z;
	
	//private NormalBoard normalBoard;
	
	private static ItemStack settlementItem, roadItem, cityItem, fleeceItem; // oreItem, logItem, wheatItem, clayItem;
	private static ItemStack starterSettlement, starterRoad;
	private static ItemStack rupeeItem, developmentCardItem;
	private static ItemStack woodShears, stoneShears, ironShears;
	
	
	private ShapelessRecipe settlementRecipe, roadRecipe, cityRecipe, developmentCardRecipe;
	private ShapelessRecipe woolRecipe, brickRecipe, woodRecipe, breadRecipe, ingotRecipe;
	private ShapelessRecipe rupRecIngot, rupRecBrick, rupRecPlank, rupRecWool, rupRecBread;
	private ShapelessRecipe rupRecIngot3, rupRecBrick3, rupRecPlank3, rupRecWool3, rupRecBread3;
	private ShapelessRecipe rupRecIngot2, rupRecBrick2, rupRecPlank2, rupRecWool2, rupRecBread2;
	private ShapedRecipe urrIngot, urrBrick, urrPlank, urrWool, urrBread;
	//private ShapedRecipe uurOre, uurClay, uurWood, uurFleece, uurWheat;
	
	// Use a collection of TeamSet, each specific to a world.
	private ArrayList<TeamSet> teamSets;
	
	private boolean genLock = false;
	
	
	// One queue for the lobby
	private HashSet<Player> queue;
	
	private BukkitTask resourceTicker;
	
	
	private Settlers getSettlers(){
		return this;
	}
	
	public static ItemStack getRoadItem(){
		return roadItem;
	}
	
	public static ItemStack getRupeeItem(){
		return rupeeItem;
	}
	
	private boolean isGenLocked(){
		return this.genLock;
	}
	
	private void setGenLock(boolean lock){
		this.genLock = lock;
	}
	
	@Override
	public void onEnable() {
		new SettlersListener(this);
		
		// Configs ***********************************
		this.saveDefaultConfig();
		POINTS_TO_WIN = this.getConfig().getInt("points_to_win");
		SKY_NUMBERS_ON = this.getConfig().getBoolean("generate_tile_numbers");
		BLOCKS_BREAKABLE = (ArrayList<String>) this.getConfig().getStringList("blocks_breakable");
		
		MAX_WORLDS = this.getConfig().getInt("max_worlds");
		
		TREE_DIFFICULTY = this.getConfig().getInt("tree_difficulty");
		CLAY_DIFFICULTY = this.getConfig().getInt("clay_difficulty");
		WHAT_DIFFICULTY = this.getConfig().getInt("wheat_difficulty");
		ORE_DIFFICULTY = this.getConfig().getInt("ore_difficulty");
		WOOL_DIFFICULTY = this.getConfig().getInt("fleece_difficulty");
		
		TOOL_CHANCE = (float) this.getConfig().getDouble("tool_drop_chance");
		MAX_RESOURCES_HELD = this.getConfig().getInt("max_resources_held");
		
		REFINE_AMOUNT = this.getConfig().getInt("refine_amount");
		RESOURCE_TIMER = this.getConfig().getInt("resource_timer") * 20;
		CARD_COOLDOWN = this.getConfig().getInt("development_card_cooldown") * 20;
		
		HEX_RADIUS = this.getConfig().getInt("hex_radius");
		NUMBER_HEIGHT = this.getConfig().getInt("number_height");
		
		MIN_QUEUE_SIZE = this.getConfig().getInt("players_to_start");
		
		SPRINT_SPEED_1 = (float)this.getConfig().getDouble("sprint_speed_1");
		SPRINT_SPEED_2 = (float)this.getConfig().getDouble("sprint_speed_2");
		
		if ((20 * this.getConfig().getInt("game_start_countdown")) > COUNTDOWN) {
			COUNTDOWN = 20 * this.getConfig().getInt("game_start_countdown");
		}
		
		 BLUE_X = this.getConfig().getInt("blueX");
		 BLUE_Y = this.getConfig().getInt("blueY");
		 BLUE_Z = this.getConfig().getInt("blueZ");
		 RED_X = this.getConfig().getInt("redX");
		 RED_Y = this.getConfig().getInt("redY");
		 RED_Z = this.getConfig().getInt("redZ");
		 BROWN_X = this.getConfig().getInt("brownX");
		 BROWN_Y = this.getConfig().getInt("brownY");
		 BROWN_Z = this.getConfig().getInt("brownZ");
		 WHITE_X = this.getConfig().getInt("whiteX");
		 WHITE_Y = this.getConfig().getInt("whiteY");
		 WHITE_Z = this.getConfig().getInt("whiteZ");
		
		
		// Teams *************************************
	
		 queue = new HashSet<Player>();
		 teamSets = new ArrayList<TeamSet>();
		 	
		// Items *************************************
		settlementItem = new ItemStack(Material.FURNACE);
		ItemMeta im = settlementItem.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Place on a torquoise block.");
		im.setDisplayName("Settlement");
		im.setLore(lore);
		settlementItem.setItemMeta(im);
		
		starterSettlement = new ItemStack(Material.FURNACE);
		im = starterSettlement.getItemMeta();
		lore = new ArrayList<String>();
		lore.add("Place on a torquoise block.");
		lore.add("No connecting road required.");
		im.setDisplayName("STARTER Settlement");
		im.setLore(lore);
		starterSettlement.setItemMeta(im);
		
		roadItem = new ItemStack(Material.BRICK);
		im = roadItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add("Place on an emerald block.");
		im.setDisplayName("Road");
		im.setLore(lore);
		roadItem.setItemMeta(im);
		
		starterRoad = new ItemStack(Material.BRICK);
		im = starterRoad.getItemMeta();
		lore = new ArrayList<String>();
		lore.add("Place on an emerald block.");
		lore.add("Must connect directly to settlement.");
		im.setDisplayName("STARTER Road");
		im.setLore(lore);
		starterRoad.setItemMeta(im);
		
		cityItem = new ItemStack(Material.ENDER_PORTAL_FRAME);
		im = cityItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add("SHIT+RIGHT_CLICK to place.");
		lore.add("Place on a workbench in a settlement.");
		im.setDisplayName("City Upgrade");
		im.setLore(lore);
		cityItem.setItemMeta(im);
		
		developmentCardItem = new ItemStack(Material.ITEM_FRAME);
		im = developmentCardItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add("Unknown Card...");
		im.setDisplayName("Development Card");
		im.setLore(lore);
		developmentCardItem.setItemMeta(im);
		
		fleeceItem = new ItemStack(Material.CARPET);
		lore = new ArrayList<String>(); 
		lore.add("Combine " + REFINE_AMOUNT + " for WOOL.");
		im.setDisplayName("Fleece");
		im.setLore(lore);
		fleeceItem.setItemMeta(im);
		
		woodShears = new ItemStack(Material.SHEARS);
		lore = new ArrayList<String>(); 
		lore.add("Might as well be made of wood...");
		im.setDisplayName("Dull Shears");
		im.setLore(lore);
		woodShears.setItemMeta(im);
		
		stoneShears = new ItemStack(Material.SHEARS);
		lore = new ArrayList<String>(); 
		lore.add("Stone-sharpened!");
		im.setDisplayName("Sharp Shears");
		im.setLore(lore);
		stoneShears.setItemMeta(im);
		
		ironShears = new ItemStack(Material.SHEARS);
		lore = new ArrayList<String>(); 
		lore.add("The sharpest shears!");
		im.setDisplayName("Iron Shears");
		im.setLore(lore);
		ironShears.setItemMeta(im);
		
	/*	oreItem = new ItemStack(Material.IRON_ORE);
		im = oreItem.getItemMeta();
		lore = new ArrayList<String>();
		lore.add("Combine " + REFINE_AMOUNT + " for an INGOT.");
		im.setDisplayName("Iron Ore");
		im.setLore(lore);
		oreItem.setItemMeta(im);*/
		
		rupeeItem = new ItemStack(Material.EMERALD, 2);
		lore = new ArrayList<String>(); 
		lore.add(ChatColor.YELLOW + "Combine 2 for any resource.");
		lore.add("2(BrK) | 2(Wd) | 2(IrN)");
		lore.add("  ___   |  ___  | 2(BrD)");
		lore.add("1 here |  ___  | 2(wL)");
		im.setDisplayName("Resource Rupee");
		im.setLore(lore);
		rupeeItem.setItemMeta(im);
		
		// Recipes ***********************************
		getServer().clearRecipes();
		
		settlementRecipe = new ShapelessRecipe(settlementItem);
		settlementRecipe.addIngredient(1, Material.WOOD);
		settlementRecipe.addIngredient(1, Material.CLAY_BRICK);
		settlementRecipe.addIngredient(1, Material.BREAD);
		settlementRecipe.addIngredient(1, Material.WOOL);
		
		cityRecipe = new ShapelessRecipe(cityItem);
		cityRecipe.addIngredient(2, Material.BREAD);
		cityRecipe.addIngredient(3, Material.IRON_INGOT);
		
		roadRecipe = new ShapelessRecipe(roadItem);
		roadRecipe.addIngredient(1, Material.WOOD);
		roadRecipe.addIngredient(1, Material.CLAY_BRICK);
		
		developmentCardRecipe = new ShapelessRecipe(developmentCardItem);
		developmentCardRecipe.addIngredient(1, Material.IRON_INGOT);
		developmentCardRecipe.addIngredient(1, Material.WOOL);
		developmentCardRecipe.addIngredient(1, Material.BREAD);
		
		woolRecipe = new ShapelessRecipe (new ItemStack(Material.WOOL));
		woolRecipe.addIngredient(REFINE_AMOUNT, Material.CARPET);
		
		woodRecipe = new ShapelessRecipe (new ItemStack(Material.WOOD));
		woodRecipe.addIngredient(REFINE_AMOUNT, Material.LOG);
		
		ingotRecipe = new ShapelessRecipe (new ItemStack(Material.IRON_INGOT));
		ingotRecipe.addIngredient(REFINE_AMOUNT, Material.IRON_ORE);
		
		brickRecipe = new ShapelessRecipe (new ItemStack(Material.CLAY_BRICK));
		brickRecipe.addIngredient(REFINE_AMOUNT, Material.CLAY_BALL);
		
		breadRecipe = new ShapelessRecipe (new ItemStack(Material.BREAD));
		breadRecipe.addIngredient(REFINE_AMOUNT, Material.WHEAT);
		
		
		// 4-Trades
		rupRecIngot = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecIngot.addIngredient(4, Material.IRON_INGOT);
		
		rupRecBrick = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecBrick.addIngredient(4, Material.CLAY_BRICK);
		
		rupRecBread = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecBread.addIngredient(4, Material.BREAD);
		
		rupRecWool = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecWool.addIngredient(4, Material.WOOL);
		
		rupRecPlank = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecPlank.addIngredient(4, Material.WOOD);
		
		// 3-Trades
		rupRecIngot3 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecIngot3.addIngredient(3, Material.IRON_INGOT);
		
		rupRecBrick3 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecBrick3.addIngredient(3, Material.CLAY_BRICK);
		
		rupRecBread3 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecBread3.addIngredient(3, Material.BREAD);
		
		rupRecWool3 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecWool3.addIngredient(3, Material.WOOL);
		
		rupRecPlank3 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecPlank3.addIngredient(3, Material.WOOD);
		
		// 2-Trades
		rupRecIngot2 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecIngot2.addIngredient(2, Material.IRON_INGOT);
		
		rupRecBrick2 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecBrick2.addIngredient(2, Material.CLAY_BRICK);
		
		rupRecBread2 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecBread2.addIngredient(2, Material.BREAD);
		
		rupRecWool2 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecWool2.addIngredient(2, Material.WOOL);
		
		rupRecPlank2 = new ShapelessRecipe (new ItemStack(rupeeItem));
		rupRecPlank2.addIngredient(2, Material.WOOD);
		
		urrIngot = new ShapedRecipe(new ItemStack(Material.IRON_INGOT));
        urrIngot.shape(new String[]{"XXA", "XXX", "AXX"});
        urrIngot.setIngredient('A', Material.EMERALD);
  
		urrBrick = new ShapedRecipe(new ItemStack(Material.CLAY_BRICK));
        urrBrick.shape(new String[]{"BXX", "XXX", "BXX"});
        urrBrick.setIngredient('B', Material.EMERALD);
        
        urrPlank = new ShapedRecipe(new ItemStack(Material.WOOD));
        urrPlank.shape(new String[]{"XCX", "XXX", "CXX"});
        urrPlank.setIngredient('C', Material.EMERALD);
        
        urrBread = new ShapedRecipe(new ItemStack(Material.BREAD));
        urrBread.shape(new String[]{"XXX", "XXD", "DXX"});
        urrBread.setIngredient('D', Material.EMERALD);
        
        urrWool = new ShapedRecipe(new ItemStack(Material.WOOL));
        urrWool.shape(new String[]{"XXX", "XXX", "EXE"});
        urrWool.setIngredient('E', Material.EMERALD);
        
        getServer().addRecipe(urrIngot);
        getServer().addRecipe(urrBrick);
        getServer().addRecipe(urrBread);
        getServer().addRecipe(urrWool);
        getServer().addRecipe(urrPlank);
        
        getServer().addRecipe(rupRecIngot);
        getServer().addRecipe(rupRecBrick);
        getServer().addRecipe(rupRecBread);
        getServer().addRecipe(rupRecPlank);
        getServer().addRecipe(rupRecWool);
        getServer().addRecipe(rupRecIngot3);
        getServer().addRecipe(rupRecBrick3);
        getServer().addRecipe(rupRecBread3);
        getServer().addRecipe(rupRecPlank3);
        getServer().addRecipe(rupRecWool3);
        getServer().addRecipe(rupRecIngot2);
        getServer().addRecipe(rupRecBrick2);
        getServer().addRecipe(rupRecBread2);
        getServer().addRecipe(rupRecPlank2);
        getServer().addRecipe(rupRecWool2);
		
		getServer().addRecipe(woodRecipe);
		getServer().addRecipe(woolRecipe);
		getServer().addRecipe(brickRecipe);
		getServer().addRecipe(breadRecipe);
		getServer().addRecipe(ingotRecipe);
		getServer().addRecipe(settlementRecipe);
		getServer().addRecipe(roadRecipe);
		getServer().addRecipe(cityRecipe);
		getServer().addRecipe(developmentCardRecipe);
		
		
		resourceTicker = new ResourceTickerAsyncTask(this).runTaskTimerAsynchronously(this,0,RESOURCE_TIMER);
	}
	
	@Override
	public void onDisable() {
		
		Iterator<TeamSet> it = teamSets.iterator();
		
		while (it.hasNext()){
			TeamSet temp = it.next();
			Iterator<Settlers_Player> it2 = temp.getPlayers().iterator();
			
			while (it2.hasNext()){
				temp.toTeam(it2.next(), "none");
			}
		}
		
		// Stop all async tasks.
		resourceTicker.cancel();
	    getServer().getScheduler().cancelTasks(this);
	}
	
	class ResourceTickerAsyncTask extends BukkitRunnable {
		// Remains in the background, firing off synchronous ResourceCollectionTasks
		
	    private final Settlers settlers;
		 
	    public ResourceTickerAsyncTask(Settlers settlers) {
	        this.settlers = settlers;
	    }
		    
	    public void run() {   	
	    	new ResourceCollectionTask().runTask(settlers);    		
	    }	 
	}
	
	
	class UnlockCardsTask extends BukkitRunnable {
		private SettlersTeam settlersTeam;
		
	    public UnlockCardsTask(SettlersTeam settlersTeam) {
	        this.settlersTeam = settlersTeam;
	    }
		    
	    public void run() {   	
	    	settlersTeam.setCardBlocked(false);
	    	this.cancel();
	    }	 
	}
	

	// TODO: Finish resource collection (7s)
	class ResourceCollectionTask extends BukkitRunnable {

		@Override
		public void run() {
				
			int roll;

			if (SKY_NUMBERS_ON){
			
				// Roll dice between 1 and 6
				int roll1 = (int)(Math.random() * 6) + 1;
				while (roll1 < 1 || roll1 > 6) {
					roll1 = (int)(Math.random() * 6) + 1;
				}
			
				// Roll dice between 1 and 6
				int roll2 = (int)(Math.random() * 6) + 1;
				while (roll2 < 1 || roll2 > 6) {
				roll2 = (int)(Math.random() * 6) + 1;
				}
				
				roll = roll1 + roll2;
			}
			else {
				roll = (int)(Math.random() * 11) + 2;
				while (roll < 2 || roll > 12) {
					roll = (int)(Math.random() * 11) + 2;
				}
			}
			
			
			// If 7:
			if (roll == 7){
				// Need to iterate thru all TeamSets and do everything for all of them.
				// This could get ugly...
				Settlers settlers = getSettlers();
				int teamSetCounter = 0;
				Iterator<TeamSet> iterator = settlers.teamSets.iterator();				
				
				while (iterator.hasNext()){
					teamSetCounter = teamSetCounter + 10;
					TeamSet teamSet = iterator.next();
					NormalBoard normalBoard = teamSet.getNormalBoard();
						
					// players lose half of their refined resources if > max_resources_held
					World world = teamSet.getWorld();
					List<Player> players = world.getPlayers();
					
					Iterator<Player> it2 = players.iterator();
					while (it2.hasNext()){											
						new HalfPlayerResources(it2.next()).runTaskLater(settlers, teamSetCounter);			
					}
					
					// all chests lose half their resources, regardless of amount.
					new HalfChestResources(normalBoard).runTaskLater(settlers, teamSetCounter + 5);
					
					// spawn a mob near all settlements (part of halfchesttask^)
					
					// move robber randomly.	
					
				    //if (normalBoard.getBlockedTileId() > -1) {
				    	Tile oldTile = normalBoard.getBlockedTile();
				    	skySkull(oldTile.getBoardLocation(), Material.AIR);
				   // }
				    normalBoard.unblockTiles();
				    	
				    int newTileIndex = (int)(Math.random() * normalBoard.getTileTotal());
				    Tile tile = normalBoard.getTiles()[newTileIndex];
				    
				    while (oldTile.getId() == tile.getId()) {
				    	newTileIndex = (int)(Math.random() * normalBoard.getTileTotal());
					    tile = normalBoard.getTiles()[newTileIndex];
				    }
				    
				    tile.setBlocked(true);
				    normalBoard.setBlockedTileId(tile.getId());
				    normalBoard.setBlockedTile(tile);
				    skySkull(tile.getBoardLocation(), SKY_SKULL_MATERIAL);	
						
						
				    // Broadcast roll
				    worldBroadcast(teamSet.getWorld(), ChatColor.GREEN + "The dice rolled 7!" + ChatColor.BOLD + " All settlements have been robbed!");			
					
				}						

			}
			else {
			// If not 7:
				
				// get all Tiles for all TeamSets that have the value rolled.
				// from that get all points on each tile that are built,
				// add 1 or 2 resources to the chest (settlement vs city)
				for (int i = 0; i < teamSets.size(); i++) {
					Tile[] tiles = teamSets.get(i).getNormalBoard().getTiles();
					worldBroadcast(teamSets.get(i).getWorld(), ChatColor.GREEN + "The dice rolled " + roll + "!");
					
					for (int j = 0; j < tiles.length; j++){
						if (tiles[j].getResource_value() == roll && !tiles[j].isBlocked()){
							Point[] points = tiles[j].getPoints();
							
							// Types: field, mountain, pasture, quarry, forest, desert
							Material m = Material.SAND;
							if (tiles[j].getType().equals("field"))
								m = Material.BREAD;
							else if (tiles[j].getType().equals("mountain"))
								m = Material.IRON_INGOT;
							else if (tiles[j].getType().equals("pasture"))
								m = Material.WOOL;
							else if (tiles[j].getType().equals("quarry"))
								m = Material.CLAY_BRICK;
							else if (tiles[j].getType().equals("forest"))
								m = Material.WOOD;
							else {
								System.err.println("Error determining resource type for roll " + roll);
							}
							
							for (int k = 0; k < points.length; k++) {
								if (points[k].getStructure() > 0){
									Location l = points[k].getBoardPoint().clone(); 
									l.setX(l.getX() + 1);
									l.setZ(l.getZ() - 3);
									Block b = l.getBlock();
									l.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
									Chest chest = (Chest) b.getState();
									Inventory inv = chest.getInventory();
									ItemStack is = new ItemStack(m, points[k].getStructure());
									inv.addItem(is);
								}
							}
						}
						
					} // end tile actions forloop
					
					// Also spawn mobs on the blocked tile - watch max mobs.
					if (teamSets.get(i).getWorld().getEntities().size() < MAX_WORLD_ENTITIES) {
						NormalBoard nb = teamSets.get(i).getNormalBoard();
						Tile blockedTile = nb.getBlockedTile();
						
					
						if (blockedTile != null) {
							World w = blockedTile.getBoardLocation().getWorld();
							Location l = blockedTile.getBoardLocation().clone();
							
							if (w.getTime() < 12000){
							// day time
								l.setX(l.getX() + HEX_RADIUS / 2);
								l.setY(w.getHighestBlockYAt(l));
								w.spawnEntity(l, EntityType.SPIDER);
								l.setX(l.getX() - HEX_RADIUS);
								l.setY(w.getHighestBlockYAt(l));
								w.spawnEntity(l, EntityType.SLIME);
								l.setX(l.getX() + HEX_RADIUS / 2);
								l.setZ(l.getZ() + HEX_RADIUS / 2);
								l.setY(w.getHighestBlockYAt(l));
								w.spawnEntity(l, EntityType.SPIDER);
								l.setZ(l.getZ() - HEX_RADIUS);
								l.setY(w.getHighestBlockYAt(l));
								w.spawnEntity(l, EntityType.SLIME);
							}
							else {
							// night time	
								l.setX(l.getX() + HEX_RADIUS / 2);
								l.setY(w.getHighestBlockYAt(l));
								w.spawnEntity(l, EntityType.ZOMBIE);
								l.setX(l.getX() - HEX_RADIUS);
								l.setY(w.getHighestBlockYAt(l));
								Skeleton e = (Skeleton)w.spawnEntity(l, EntityType.SKELETON);
								e.getEquipment().setItemInHand(new ItemStack(Material.BOW,1));
								l.setX(l.getX() + HEX_RADIUS / 2);
								l.setZ(l.getZ() + HEX_RADIUS / 2);
								l.setY(w.getHighestBlockYAt(l));
								Skeleton e2 = (Skeleton)w.spawnEntity(l, EntityType.SKELETON);
								e2.getEquipment().setItemInHand(new ItemStack(Material.BOW,1));
								l.setZ(l.getZ() - HEX_RADIUS);
								l.setY(w.getHighestBlockYAt(l));
								w.spawnEntity(l, EntityType.SLIME);
							}
						}
					}
					else{
						// System.out.println("Too many mobs to spawn more.");
					}
						

				} // end teamset actions
			}
			
			this.cancel();
		}
	}
	
	
	
	class PlaceDiamondsTask extends BukkitRunnable {
		 
	    private final Settlers settlers;
	    private NormalBoard normalBoard;
	    private World world;
	    
	    public PlaceDiamondsTask(Settlers settlers, NormalBoard normalBoard, World world) {
	        this.settlers = settlers;
	        this.normalBoard = normalBoard;
	        this.world = world;
	    }

		@Override
		public void run() {
			Point[] diamondPoints = normalBoard.allPoints();
			for (int i = 0; i < diamondPoints.length; i++){
				Block b = world.getBlockAt(diamondPoints[i].getBoardPoint());
				
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY(), b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY(), b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY(), b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY(), b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY(), b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY(), b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY(), b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY(), b.getZ() - 1)).setType(Material.AIR);
				
				world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY() + 1, b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY() + 1, b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY() + 1, b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY() + 1, b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY() + 1, b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY() + 1, b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ() - 1)).setType(Material.AIR);
				
				b.setType(Material.DIAMOND_BLOCK);
				b.setMetadata("id", new FixedMetadataValue(settlers, diamondPoints[i].getId()));
					
			}
			
			System.out.println("Placed blocks on vertecies.");
			this.cancel();
		}
	}
	
	class PlaceGoldTask extends BukkitRunnable {
		 
	    private final Settlers settlers;
	    private NormalBoard normalBoard;
	    private World world;
	    
	    public PlaceGoldTask(Settlers settlers, NormalBoard normalBoard, World world) {
	        this.settlers = settlers;
	        this.normalBoard = normalBoard;
	        this.world = world;
	    }

		@Override
		public void run() {
			Tile[] goldPoints = normalBoard.getTiles();
			for (int i = 0; i < goldPoints.length; i++){
				Block b = world.getHighestBlockAt(goldPoints[i].getBoardLocation());
				Block b2 = b.getRelative(BlockFace.DOWN);
				
				while (b2.getType().equals(Material.LEAVES) || b2.getType().equals(Material.LOG) || b2.getType().equals(Material.AIR)) {
					b = b2;
					b2 = b2.getRelative(BlockFace.DOWN);
				}
				
				b.setType(Material.GOLD_BLOCK);
				b.setMetadata("id", new FixedMetadataValue(settlers, goldPoints[i].getId()));
					
			}
			
			System.out.println("Placed gold blocks on tile centers.");
			this.cancel();
		}
	}
	
	class PlacePortsTask extends BukkitRunnable {
		private NormalBoard normalBoard;
		
		public PlacePortsTask(NormalBoard normalBoard) {
		   this.normalBoard = normalBoard;
		}

		@Override
		public void run() {
			// randomize 2:1 port locations
			
			// port settings from Point class:
			// port 0 = none
			// 1 = wood
			// 2 = wheat
			// 3 = wool
			// 4 = brick
			// 5 = ore
			// 6 = 3:1
			
			int[] portTypes = new int[] {1,2,3,4,5};
			NormalBoard.shuffleArray(portTypes);
			
			// 2:1 point id pairs:
			// (2,9)  (52,35)  (48,47)  (42,41)  (18,17)
			
			normalBoard.addPort(2,portTypes[0]);
			normalBoard.addPort(9,portTypes[0]);
			
			normalBoard.addPort(52,portTypes[1]);
			normalBoard.addPort(35,portTypes[1]);
			
			normalBoard.addPort(48,portTypes[2]);
			normalBoard.addPort(47,portTypes[2]);
			
			normalBoard.addPort(42,portTypes[3]);
			normalBoard.addPort(41,portTypes[3]);
			
			normalBoard.addPort(18,portTypes[4]);
			normalBoard.addPort(17,portTypes[4]);
			
			// set 3:1 ports- fixed locations between 2:1 ports.
			// 3:1 point id pairs:
			// (13,12)  (33,36)  (43,46)  (53,20)  (5,4)
			
			normalBoard.addPort(13,6);
			normalBoard.addPort(12,6);
			
			normalBoard.addPort(33,6);
			normalBoard.addPort(36,6);
			
			normalBoard.addPort(43,6);
			normalBoard.addPort(46,6);
			
			normalBoard.addPort(53,6);
			normalBoard.addPort(20,6);
			
			normalBoard.addPort(5,6);
			normalBoard.addPort(4,6);
			
			this.cancel();
		}
		
	}
	
	
	class EndGamePlayer extends BukkitRunnable {
	//	private Settlers settlers;
	    private Player player;
	    
	    public EndGamePlayer(Player player) {
	    	//this.settlers = settlers;
	        this.player = player;
	    }

		@Override
		public void run() {
			toLobby(player);
			//toTeam(player, "none");
			player.getInventory().clear();
			this.cancel();
		}
	}
	
	class TimerMessagePlayer extends BukkitRunnable {
	    private Player player;
	    private int time;
	    
	    public TimerMessagePlayer(Player player, int time) {
	        this.player = player;
	        this.time = time;
	    }

		@Override
		public void run() {
			player.sendMessage("Game Timer: " + time);
			this.cancel();
		}
	}
	
	
	class StartGameTask extends BukkitRunnable {
	    private TeamSet teamSet;
	    
	    public StartGameTask(TeamSet teamSet) {
	    	this.teamSet = teamSet;
	    }

		@Override
		public void run() {
			startGame(teamSet);
			this.cancel();
		}
	}
	
	class OneBlockTask extends BukkitRunnable {
	    private Location location;
	    private Material newMaterial;
	    
	    public OneBlockTask(Location l, Material m) {
	    	this.location = l.clone();
	    	this.newMaterial = m;
	    }

		@Override
		public void run() {
			location.getBlock().setType(newMaterial);
			this.cancel();
		}
	}
	
	
	class HexGenerateTask extends BukkitRunnable {
	    private BigHexagon hex;
	    private World world;
	    
	    public HexGenerateTask(BigHexagon hex, World world) {
	    	this.hex = hex;
	    	this.world = world;
	    }

		@Override
		public void run() {
			hex.generate(world);
			this.cancel();
		}
	}
	
	class DeconstructGameTask extends BukkitRunnable {
		private TeamSet teamSet;
		
		public DeconstructGameTask(TeamSet teamSet){
			this.teamSet = teamSet;
		}

		@Override
		public void run() {
			teamSet.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			teamSet.getObjective().unregister();
			teamSets.remove(teamSets.indexOf(teamSet));
				
			// unload world
			Bukkit.getServer().unloadWorld(teamSet.getWorld(), true);
				
			// delete world
			deleteWorld(teamSet.getWorld().getName());
			this.cancel();
		}
	}
	
	class PlaceEmeraldsTask extends BukkitRunnable {
		 
	    private final Settlers settlers;
	    private NormalBoard normalBoard;
	    private World world;
	    
	    public PlaceEmeraldsTask(Settlers settlers, NormalBoard normalBoard, World world) {
	        this.settlers = settlers;
	        this.normalBoard = normalBoard;
	        this.world = world;
	    }

		@Override
		public void run() {
			Edge[] emeraldPoints = normalBoard.allEdges();
			for (int i = 0; i < emeraldPoints.length; i++){
				Block b = world.getBlockAt(emeraldPoints[i].getBoardPoint());
				
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY(), b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY(), b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY(), b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY(), b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY(), b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY(), b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY(), b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY(), b.getZ() - 1)).setType(Material.AIR);
				
				world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY() + 1, b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY() + 1, b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() - 1, b.getY() + 1, b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY() + 1, b.getZ())).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY() + 1, b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX() + 1, b.getY() + 1, b.getZ() - 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ() + 1)).setType(Material.AIR);
				world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ() - 1)).setType(Material.AIR);
				
				b.setType(Material.EMERALD_BLOCK);
				b.setMetadata("id", new FixedMetadataValue(settlers, emeraldPoints[i].getId()));		
			
			}
			
			System.out.println("Placed blocks on edges.");
			this.cancel();
		}
	}
	
	class PlaceNumbersTask extends BukkitRunnable {
		private Settlers settlers;
	    private NormalBoard normalBoard;
	    private World world;
	    
	    public PlaceNumbersTask(Settlers settlers, NormalBoard normalBoard, World world) {
	    	this.settlers = settlers;
	        this.normalBoard = normalBoard;
	        this.world = world;
	    }

		@Override
		public void run() {
			Tile[] tiles = normalBoard.getTiles();
			//int delay = NUMBER_THICKNESS * 15;
			int delay = NUMBER_THICKNESS;
			
			for (int i = 0; i < tiles.length; i++){
				new PlaceOneNumberTask(settlers, tiles[i], world).runTaskLater(settlers, i*delay);
			}
			
			System.out.println("Scheduled number placement.");
			this.cancel();
		}
	}
	
	class SlowPlayerTask extends BukkitRunnable {
		private Player player;
		private Settlers settlers;
	    
	    public SlowPlayerTask(Player player, Settlers settlers) {
	    	this.player = player;
	    	this.settlers = settlers;
	    }

		@Override
		public void run() {
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.WOOL)){
				int data = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getData();
				
				if (player.hasMetadata("settlers_team")) {
					String team = player.getMetadata("settlers_team").get(0).asString();
					
					if ((team.equals("blue") && data == LIGHT_BLUE_WOOL) ||
							(team.equals("red") && data == RED_WOOL ||
							(team.equals("brown") && data == BROWN_WOOL) ||
							(team.equals("white") && data == WHITE_WOOL)))
					{
						// play effect
						player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
						new SlowPlayerTask(player, settlers).runTaskLater(settlers, SPRINT_DURATION);
					}	
					else
						player.setWalkSpeed((float) 0.2);
				}
				else 
					player.setWalkSpeed((float) 0.2);
			}
			else {
				player.setWalkSpeed((float) 0.2);	
			}
			this.cancel();
		}
	}
	
	class SpeedPlayer1Task extends BukkitRunnable {
		private Player player;
		private Settlers settlers;
	    
	    public SpeedPlayer1Task(Player player, Settlers settlers) {
	    	this.player = player;
	    	this.settlers = settlers;
	    }

		@Override
		public void run() {
			player.setWalkSpeed((float) SPRINT_SPEED_1);
			player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
			new SlowPlayerTask(player, settlers).runTaskLater(settlers, SPRINT_DURATION);
			this.cancel();
		}
	}
	
	class SpeedPlayer2Task extends BukkitRunnable {
		private Player player;
		private Settlers settlers;
	    
	    public SpeedPlayer2Task(Player player, Settlers settlers) {
	    	this.player = player;
	    	this.settlers = settlers;
	    }

		@Override
		public void run() {
			player.setWalkSpeed((float) SPRINT_SPEED_2);
			player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 0);
			new SlowPlayerTask(player, settlers).runTaskLater(settlers, SPRINT_DURATION);
			this.cancel();
		}
	}
	
	class HalfPlayerResources extends BukkitRunnable {
		private Player player;
	    
	    public HalfPlayerResources(Player player) {
	    	this.player = player;
	    }

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			Inventory inv = player.getInventory();
			
			int counter = 0;
			ArrayList<Integer> chopBlock = new ArrayList<Integer>();
			ItemStack[] items = inv.getContents();
			
			for(int i = 0; i < items.length; i++){
				ItemStack tempItem = items[i];
				if (tempItem != null) {
					if (tempItem.getType().equals(Material.WOOD) ||
							tempItem.getType().equals(Material.WOOL) ||
							tempItem.getType().equals(Material.CLAY_BRICK) ||
							tempItem.getType().equals(Material.IRON_INGOT) ||
							tempItem.getType().equals(Material.BREAD) 
					){
						//System.out.println("in player itemcheck - found");
						if (counter % 2 > 0){
							chopBlock.add((Integer)i);
						}
						
						counter++;
					}
				}
			}
			
			if (counter > MAX_RESOURCES_HELD) {
				Iterator<Integer> chopIt = chopBlock.iterator();
				
				while (chopIt.hasNext()){
					//System.out.println("in player chopit");
					inv.setItem((int)chopIt.next(), null);
					//items[(int)chopIt.next()].setAmount(0);
					player.updateInventory();
				}
				
				player.sendMessage(ChatColor.GREEN + "You had too many resources, and lost half of them!");
			}
			this.cancel();
		}
	}
	
	class HalfChestResources extends BukkitRunnable {
		private NormalBoard normalBoard;
	    
	    public HalfChestResources(NormalBoard normalBoard) {
	    	this.normalBoard = normalBoard;
	    }

		@Override
		public void run() {
			Point[] points = normalBoard.allPoints();
			for (int i = 0; i < points.length; i++){
				
				if (points[i].getOwner() == 0){
					
					
				}
				else {
				
				  // Spawn mobs behind settlements
				  Location lMob = points[i].getBoardPoint().clone();
				
				  lMob.setX(lMob.getX() + 10);
				  lMob.setY(lMob.getWorld().getHighestBlockYAt(lMob));
				  lMob.getWorld().spawnEntity(lMob, EntityType.SLIME);
					
				  // Half chest's inventory
				  Chest chest = points[i].getChest();
				  Inventory inv = chest.getInventory();
				
				  int coin = 0;

				  ItemStack[] items = inv.getContents();
				  for (int j = 0; j < items.length; j++){
					ItemStack tempItem = items[j];
					
					if (tempItem != null) {
						if (tempItem.getType().equals(Material.WOOD) ||
								tempItem.getType().equals(Material.WOOL) ||
								tempItem.getType().equals(Material.CLAY_BRICK) ||
								tempItem.getType().equals(Material.IRON_INGOT) ||
								tempItem.getType().equals(Material.BREAD) 
						){
							if (tempItem.getAmount() == 1){
								if (coin > 1) {
									inv.setItem(j, null);
									coin++;
								}
								else {
									coin = 0;
								}
							}
							else {
								tempItem.setAmount(tempItem.getAmount() / 2);
							}
						
						}
					}
				  }
				  
				  
				}
			}
			this.cancel();
	
		}
	}

	
	
	class PlaceOneNumberTask extends BukkitRunnable {
		
	    private Tile tile;
	    private World world;
	    
	    public PlaceOneNumberTask(Settlers settlers, Tile tile, World world) {
	        this.tile = tile;
	        this.world = world;
	    }

		@Override
		public void run() {
			
			BigNumber bn;		
			
			Location l = tile.getBoardLocation();
			Material m = NUMBER_MATERIAL;
			String type = tile.getType();				
				
			if (type.equals("mountain")){
				m = NUMBER_MATERIAL_OFF;
			}
				
			if (tile.getResource_value() > 9) {
				bn = new BigNumber(1, NUMBER_THICKNESS);
				//bn.place(world, new Location (world,l.getX() - NUMBER_THICKNESS * 2, l.getY() + NUMBER_HEIGHT, l.getZ()), m);
				bn.place_grounded(world, new Location (world,l.getX() - NUMBER_THICKNESS * 2, l.getY() + NUMBER_HEIGHT, l.getZ()), m);
				
				if (tile.getResource_value() == 10) {
					bn = new BigNumber(0, NUMBER_THICKNESS);
					//bn.place(world, new Location (world,l.getX() + NUMBER_THICKNESS * 2, l.getY() + NUMBER_HEIGHT, l.getZ()), m);
					bn.place_grounded(world, new Location (world,l.getX() + NUMBER_THICKNESS * 2, l.getY() + NUMBER_HEIGHT, l.getZ()), m);
				} else if (tile.getResource_value() == 11) {
					bn = new BigNumber(1, NUMBER_THICKNESS);
					//bn.place(world, new Location (world,l.getX(), l.getY() + NUMBER_HEIGHT, l.getZ()), m);
					bn.place_grounded(world, new Location (world,l.getX(), l.getY() + NUMBER_HEIGHT, l.getZ()), m);
				} else if (tile.getResource_value() == 12) {
					bn = new BigNumber(2, NUMBER_THICKNESS);
					//bn.place(world, new Location (world,l.getX() + NUMBER_THICKNESS * 2, l.getY() + NUMBER_HEIGHT, l.getZ()), m);
					bn.place_grounded(world, new Location (world,l.getX() + NUMBER_THICKNESS * 2, l.getY() + NUMBER_HEIGHT, l.getZ()), m);
				}
			} else if (tile.getResource_value() < 1){
				// do nothing
			}
			else {
				bn = new BigNumber(tile.getResource_value(), NUMBER_THICKNESS);
				//bn.place(world, new Location (world,l.getX(),l.getY() + NUMBER_HEIGHT, l.getZ()), m);
				bn.place_grounded(world, new Location (world,l.getX(),l.getY() + NUMBER_HEIGHT, l.getZ()), m);
			}
			
			this.cancel();
			
		}
	}
	
	private void stoneToolChance(Player player, Location l) {
		int roll = (int)(Math.random() * 100);
		int invertedToolChance = 100 - (int) TOOL_CHANCE;
		if (roll > invertedToolChance) {		
			ItemStack item = new ItemStack(Material.STONE_HOE);
			double slice = TOOL_CHANCE / 6.0;
			String itemName = "stone hoe";
			
			if (roll > invertedToolChance + (5*slice)){
				item = new ItemStack(Material.STONE_AXE);
				itemName = "a stone axe";
			}
			else if (roll > invertedToolChance + (4*slice)){
				item = new ItemStack(Material.STONE_PICKAXE);
				itemName = "a stone pickaxe";
			}
			else if (roll > invertedToolChance + (3*slice)){
				item = new ItemStack(Material.STONE_SPADE);
				itemName = "a stone shovel";
			}
			else if (roll > invertedToolChance + (2*slice)){
				item = new ItemStack(Material.STONE_SWORD);
				itemName = "a stone sword";
			}
			else if (roll > invertedToolChance + (1*slice)){
				item = new ItemStack(stoneShears);
				itemName = "sharp shears";
			}
		
			player.sendMessage(ChatColor.BOLD + "You found " + itemName + "!");
			player.getWorld().dropItemNaturally(l, item);
			l.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
		}
	}
	
	class SettlersListener implements Listener {
		Settlers settlers;
		
	    public SettlersListener (Settlers settlers) {
	    	this.settlers = settlers;
	        settlers.getServer().getPluginManager().registerEvents(this, settlers);
	    }
	    
	    // Sneak Event
	   @EventHandler
	    public void onSneak(PlayerToggleSneakEvent evt) {
	    	/*Player player = evt.getPlayer();
	    	World w = player.getWorld();
	    	Location l = player.getLocation();
	    	Location l2 = new Location(w, l.getX(), l.getY() - 1, l.getZ());
	    	Block b = w.getBlockAt(l2);

	    	if (b.getType().equals(Material.DIAMOND_BLOCK)){
	    		//player.sendMessage("It's diamond.");
	    		if (!b.getMetadata("id").isEmpty()){
	    			int id = b.getMetadata("id").get(0).asInt();
	    			//ArrayList<Edge> edges = normalBoard.getPointById(id).getEdges();
	    			
	    			player.sendMessage("point id: " + id);
	    			
	    			//for (int i = 0; i < edges.size(); i++) {
	    			//	player.sendMessage(edges.get(i).toString());
	    			//}
	    		} 
	    		else {
	    			
	    		}
	    	
	    	} 
	    	else if (b.getType().equals(Material.EMERALD_BLOCK)){
	    		if (!b.getMetadata("id").isEmpty()){
	    			int id = b.getMetadata("id").get(0).asInt();

	    			player.sendMessage("" + id);

	    		} 
	    		else {
	    			
	    		}
	    	
	    	} 
	    	else {
	    		
	    	}*/
	    		
	    }
	    
	 // Block Flow Event
	    @EventHandler
	    public void onFlow(BlockFromToEvent evt) {
	    	if (evt.getBlock().getType().equals(Material.LAVA) || evt.getBlock().getType().equals(Material.STATIONARY_LAVA)){
	    		evt.setCancelled(true);
	    	}
	    }
	    
	    // Block Break Event
	    @EventHandler
	    public void onBlockBreak(BlockBreakEvent evt) {
	    	Player player = evt.getPlayer();
	    	World w = player.getWorld();
	    	Block b = evt.getBlock();
	    	
	    	if (w.getName().equals("settlers_lobby") || w.getName().equals("settlers_template")){
	    		
	    		if (!player.hasPermission("settlers.block_manipulation")) {
	    			evt.setCancelled(true);
	    			player.sendMessage("You cannot break blocks here.");
	    		}
	    	}
	    	else {
	    		int roll;
	    		double factor = 1;
	    		
	    		if (player.getItemInHand().getType().equals(Material.WOOD_SPADE)) {
	    			
	    			factor = 1.2;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.WOOD_HOE)) {
	    			factor = 1.02;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.STONE_AXE) ||
	    				player.getItemInHand().getType().equals(Material.WOOD_PICKAXE) ||
	    				player.getItemInHand().getType().equals(Material.WOOD_AXE) ||
	    				player.getItemInHand().getType().equals(Material.STONE_PICKAXE) ||
	    				player.getItemInHand().getType().equals(Material.STONE_SPADE)) {
	    			
	    			factor = 1.4;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.STONE_HOE)) {
	    			factor = 1.05;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.IRON_AXE) ||
	    				player.getItemInHand().getType().equals(Material.IRON_PICKAXE) ||
	    				player.getItemInHand().getType().equals(Material.IRON_SPADE)) {
	    			
	    			factor = 1.6;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.IRON_HOE)) {
	    			factor = 1.08;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.DIAMOND_AXE) ||
	    				player.getItemInHand().getType().equals(Material.DIAMOND_PICKAXE) ||
	    				player.getItemInHand().getType().equals(Material.DIAMOND_SPADE)) {
	    			
	    			factor = 2;
	    		}
	    		else if (player.getItemInHand().getType().equals(Material.DIAMOND_HOE)) {
	    			factor = 1.1;
	    		}
	    		
	    		if (b.getType().equals(Material.CROPS)){
	    			roll = (int)(Math.random() * ROLL_CEILING);
	    			if (roll * factor > WHAT_DIFFICULTY) {
	    				//player.sendMessage("You picked some wheat.");
	    				evt.setCancelled(true);
	    				evt.getBlock().setType(Material.AIR);
	    				w.dropItemNaturally(evt.getBlock().getLocation(), new ItemStack(Material.WHEAT,1));
	    				stoneToolChance(player,evt.getBlock().getLocation());
	    			}
	    			else {
	    				evt.setCancelled(true);
	    			}
	    			
	    			
	    			if ( !player.getItemInHand().getType().equals(Material.IRON_HOE) &&
	    				 !player.getItemInHand().getType().equals(Material.WOOD_HOE) &&
	    				 !player.getItemInHand().getType().equals(Material.STONE_HOE) &&
	    				 !player.getItemInHand().getType().equals(Material.DIAMOND_HOE)) {
	    				
	    				player.sendMessage("Grass cut! Use a hoe!");
	    				
	    				if (player.getHealth() > 1)
	    					player.damage(1);
	    			}
	    			
	    		}
	    		else if (b.getType().equals(Material.LOG)){
	    			roll = (int)(Math.random() * 100);
	    			if (roll * factor > TREE_DIFFICULTY) {
	    				player.sendMessage("You jacked some lumber.");
	    				b.setType(Material.AIR);
	    				w.dropItemNaturally(b.getLocation(), new ItemStack(Material.LOG));
	    				treeFall(b);
	    				stoneToolChance(player,evt.getBlock().getLocation());
	    				evt.setCancelled(true);
	    			}
	    			else {
	    				evt.setCancelled(true);
	    				player.sendMessage("The tree wobbles.");
	    			}
	    			
	    			if ( !player.getItemInHand().getType().equals(Material.IRON_AXE) &&
	    				 !player.getItemInHand().getType().equals(Material.WOOD_AXE) &&
	    				 !player.getItemInHand().getType().equals(Material.STONE_AXE) &&
	    				 !player.getItemInHand().getType().equals(Material.DIAMOND_AXE)) {
	    				
	    				player.sendMessage("Ouch! An axe would be easier!");
	    				
	    				if (player.getHealth() > 4)
	    					player.damage(4);	
	    				
	    			}

	    		}
	    		else if (b.getType().equals(Material.CLAY)){
	    			roll = (int)(Math.random() * 100);
	    			if (roll * factor > CLAY_DIFFICULTY) {
	    				player.sendMessage("You dug up some clay.");
	    				evt.setCancelled(true);
	    				evt.getBlock().setType(Material.AIR);
	    				w.dropItemNaturally(evt.getBlock().getLocation(), new ItemStack(Material.CLAY_BALL,1));
	    				stoneToolChance(player,evt.getBlock().getLocation());
	    			}
	    			else {
	    				evt.setCancelled(true);
	    				player.sendMessage("The clay loosens.");
	    			}
	    			
	    			
	    			if ( !player.getItemInHand().getType().equals(Material.IRON_SPADE) &&
	    				 !player.getItemInHand().getType().equals(Material.WOOD_SPADE) &&
	    				 !player.getItemInHand().getType().equals(Material.STONE_SPADE) &&
	    				 !player.getItemInHand().getType().equals(Material.DIAMOND_SPADE)) {
	    				
	    				player.sendMessage("Ouch! A spade would be easier!");
	    				if (player.getHealth() > 4)
	    					player.damage(4);
	    			}
	    			
	    		}
	    		else if (b.getType().equals(Material.IRON_ORE)){
	    			roll = (int)(Math.random() * 100);
	    			if (roll * factor > ORE_DIFFICULTY) {
	    				player.sendMessage("You manage to obtain some iron ore.");
	    				evt.setCancelled(true);
	    				evt.getBlock().setType(Material.AIR);
	    				w.dropItemNaturally(evt.getBlock().getLocation(), new ItemStack(Material.IRON_ORE,1));
	    				stoneToolChance(player,evt.getBlock().getLocation());
	    			}
	    			else {
	    				evt.setCancelled(true);
	    				player.sendMessage("You slip, and fail to hit the rock.");
	    			}
	    			
	    			if ( !player.getItemInHand().getType().equals(Material.IRON_PICKAXE) &&
	    				 !player.getItemInHand().getType().equals(Material.WOOD_PICKAXE) &&
	    				 !player.getItemInHand().getType().equals(Material.STONE_PICKAXE) &&
	    				 !player.getItemInHand().getType().equals(Material.DIAMOND_PICKAXE)) {
	    				
	    				player.sendMessage("Ahhh, it's ROCK! You PUNCHED it!!! Use a pickaxe!");
	    				if (player.getHealth() > 8)
	    					player.damage(8);
	    			}
	  
	    		}
	    		/*else if (
	    					b.getType().equals(Material.COBBLESTONE) ||
	    					b.getType().equals(Material.MOSSY_COBBLESTONE) ||
	    					b.getType().equals(Material.DIRT) ||
	    					b.getType().equals(Material.GRASS) ||
	    					b.getType().equals(Material.SAND) ||
	    					b.getType().equals(Material.LEAVES) ||
	    					b.getType().equals(Material.TORCH) ||
	    					b.getType().equals(Material.LADDER)
	    															){*/
	    		
	    		else if (BLOCKS_BREAKABLE.contains(b.getType().toString())) {
	    			// do nothing; allow the configured blocks to be broken by players
	    			
	    		}
	    		else {
	    			evt.setCancelled(true);
		    		player.sendMessage("It won't budge!");
	    			
	    		}
	    		
	    	}
	    }
	    
	   /* @EventHandler
	    public void closeInventory(InventoryCloseEvent evt) {
	    	
	    }*
	    
	    /*@EventHandler
	    public void unstacker(InventoryClickEvent evt) {
	    	Inventory inv = evt.getWhoClicked().getInventory();
    		
	    	
    		Player p = (Player)evt.getWhoClicked();
    		p.sendMessage(evt.getCurrentItem().toString() + " " + evt.getCurrentItem().getAmount());
    		ItemStack is = null;
    		int amount = 0;
    		
    		if (inv.getItem(evt.getSlot()) == null){	
    			p.sendMessage("ItemSlot null");
    			return;
    		}
    		else {
    			p.sendMessage(evt.getSlot() + " " + inv.getItem(evt.getSlot()).toString());
    			is = inv.getItem(evt.getSlot());
    			amount = is.getAmount();
    		}
    	
	    	
	    	if (evt.getAction().equals(InventoryAction.PLACE_ALL)){
	    		
	    		for (int i = 0; i < amount - 1; i++){
	    			if (inv.firstEmpty() > 0){
	    				ItemStack temp = new ItemStack(is);
	    				temp.setAmount(1);
	    				inv.addItem(temp);
	    				is.setAmount(is.getAmount() - 1);
	    			}
	    			else {
	    				evt.getWhoClicked().getWorld().dropItem(evt.getWhoClicked().getEyeLocation(), is);
	    				is.setAmount(0);
	    				
	    				if (evt.getWhoClicked() instanceof Player){
	    					((Player)evt.getWhoClicked()).sendMessage("Your inventory is full!");
	    				}
	    			}
	    		}
	    		
	    	}
	    }*/
	    
	    
	    // Block Fade Event
	    @EventHandler
	    public void onBlockFade(BlockFadeEvent evt) {
	    	Block b = evt.getBlock();
	    	
	    	if (b.getType().equals(Material.ICE) || b.getType().equals(Material.SNOW_BLOCK)){
	    		evt.setCancelled(true);
	    		
	    	}
	    	
	    }
	    
	    
	    // Join Event
	    @EventHandler
	    public void onJoin(PlayerJoinEvent evt) {
	    	Player player = evt.getPlayer();
	    	World w = player.getWorld();
	    	
	    	player.setFoodLevel(20);
	    	player.getInventory().setMaxStackSize(1);
	    	player.setWalkSpeed((float) 0.2);
	    	
	    	if (w.getName().equals("settlers_lobby")){
	    		//player.teleport(w.getSpawnLocation());
	    		toLobby(player);
	    		player.sendMessage("Welcome to Settlers - Lobby World");
	    	}
	    	else {
	    		// check team via metadata
	    		// Warn player of team and game status
	    		// Teleport to lobby and remove from team if game is over.
	    		
	    	/*
	    	  if (player.getMetadata("settlers_team").get(0).equals("blue")){
	    		player.sendMessage("You are currently on the BLUE team.");
    		  }
    		  else if (redTeam.getTeam().getPlayers().contains((OfflinePlayer)player)){
    			player.sendMessage("You are currently on the RED team.");
    		  }
    		  else if (brownTeam.getTeam().getPlayers().contains((OfflinePlayer)player)){
    			player.sendMessage("You are currently on the BROWN team.");
    		  }
    		  else if (whiteTeam.getTeam().getPlayers().contains((OfflinePlayer)player)){
    			player.sendMessage("You are currently on the WHITE team.");
    		  }
    		  else {
    			player.sendMessage("You are not on a team.");
    			player.getEquipment().setLeggings(null);
    			player.getEquipment().setChestplate(null);
    		  } */
	    	}
	    }
	    
	    
	    // Hanging Protector
	    @EventHandler
	    public void onHangingItemBreak(HangingBreakByEntityEvent evt) {
	    	Player player;
	    	
	    	if (evt.getRemover() instanceof Player){
	    		player = (Player) evt.getRemover();
	
	    		if (!player.hasPermission("settlers.block_manipulation")) {
	    			evt.setCancelled(true);
	    		}
	    	}
	    }
	    
	    @EventHandler
	    public void onPlayerItemBreak(PlayerItemBreakEvent evt) {    	
	    	evt.getBrokenItem().setDurability((short) 100);
	    }
	    
	    // Stop wool drops from sheep
	    @EventHandler
	    public void onShear(PlayerShearEntityEvent evt) {
	    	if (evt.getEntity() instanceof Sheep) {
	    		double factor = 1.0;
	    		int roll = (int)(Math.random() * ROLL_CEILING);
	    		Player player = evt.getPlayer();
	    		
	    		if (player.getItemInHand().hasItemMeta()){
	    		  if (player.getItemInHand().getItemMeta().getDisplayName().equals("Dull Shears")){
	    			factor = 1.07;
	    		  }
	    		  else if (player.getItemInHand().getItemMeta().getDisplayName().equals("Sharp Shears")){
	    			factor = 1.22;
	    		  }
	    		  else if (player.getItemInHand().getItemMeta().getDisplayName().equals("Iron Shears")){
	    			factor = 1.45;
	    		  }
	    		}
	    	
	    		if (roll * factor > WOOL_DIFFICULTY) {
	    			player.sendMessage("You shear some fleece off the sheep.");
	    			evt.setCancelled(true);
		    		Sheep sheep = (Sheep) evt.getEntity();
		    		sheep.setSheared(true);
		    		sheep.getWorld().dropItemNaturally(sheep.getLocation(), fleeceItem);
		    		stoneToolChance(player,sheep.getLocation());

	    		}
	    		else {
	    			evt.setCancelled(true);
	    			
	    			if (roll * factor < 15)
	    				player.sendMessage("The sheep wriggles free!");
	    			else if (roll * factor < 20)
	    				player.sendMessage("Baaa Ramm Eeewwe!");
	    			else if (roll * factor < 35)
	    				player.sendMessage("The sheep won't stand still!");
	    			else{
	    				//player.sendMessage("You fail to shear a decent fleece.");
	    			}
	    		}
	    	}
	    }
	    
	    // Entity Death
	    @EventHandler
	    public void onEntityDeath(EntityDeathEvent evt) {
	    	// Prevent sheep from dropping wool
	    	// Extension- prevent mobs from dropping anything or exp.
	    	//if (evt.getEntity() instanceof Sheep) {
	    	if (!(evt.getEntity() instanceof Player)) {
	    		evt.setDroppedExp(0);
	    		evt.getDrops().clear();
	    	}	    	
	    	 	
	    }
	    
	    @EventHandler
	    public void onEntityDamage(EntityDamageEvent evt) {
	    	if (evt.getEntity() instanceof Player) {
	    		Player player = (Player)evt.getEntity();
	    		World w = player.getWorld();
	    		
	    		if (player.getHealth() - evt.getDamage() <= 0) {
	    			evt.setCancelled(true);
	    			Inventory inv = player.getInventory();
	    			EntityEquipment equipment = player.getEquipment();
	    			ItemStack[] items = inv.getContents();
	    			ItemStack tempItem;
	    			int count = 0;
	    			
	    			for (int i = 0; i < items.length; i++){
	    				if (items[i] != null){
	    				 if ( items[i].getType().equals(Material.CARPET) ||
	    					  items[i].getType().equals(Material.LOG) ||
	    					  items[i].getType().equals(Material.IRON_ORE) ||
	    					  items[i].getType().equals(Material.CLAY_BALL) ||
	    					  items[i].getType().equals(Material.WHEAT)  ){
	    					
	    					tempItem = items[i].clone();
	    					/*tempItem.setAmount(tempItem.getAmount() / 2);
	    					items[i].setAmount(items[i].getAmount() / 2);
	    					w.dropItemNaturally(player.getLocation(), tempItem);*/	
	    					 
	    					if (count % 2 == 0) {
	    						w.dropItemNaturally(player.getLocation(), tempItem);
	    						items[i].setAmount(0);
	    					}
	    					
	    					count++; 
	    				 }
	    				}
	    			}
	    			
	    			ItemStack[] equipmentItems = equipment.getArmorContents().clone();
	    			equipment.setLeggings(null);
	    			equipment.setChestplate(null);
	    			equipment.setHelmet(null);
	    			equipment.setBoots(null);
	    			equipment.clear();
	    			
	    			ItemStack[] items2 = items.clone();
	    			inv.clear();
	    			
	    			player.setHealth(0);
	    			equipment.setArmorContents(equipmentItems);
	    			inv.setContents(items2);
	    			
	    			    			
	    			
	    		}
	    			
	    	}
	    }
	    
	   

		// Entity Respawn
	    @EventHandler
	    public void onRespawn(PlayerRespawnEvent evt) {
	    	Player player = evt.getPlayer();
			World world = player.getWorld();
			
			if (world.getName() == "settlers_lobby"){
				return;
			}
			
			Iterator<TeamSet> iterator = getSettlers().teamSets.iterator();
			TeamSet teamSet = null;
			
			while (iterator.hasNext()){
				TeamSet temp = iterator.next();
				if (temp.getWorld().getName().equals(world.getName())){
					teamSet = temp;
					break;
				}
			}
			
			if (teamSet == null) {
				System.out.println(player.getDisplayName() + " died outside the lobby while not on a team.");
				return;
			}
			
	    	if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			evt.setRespawnLocation(teamSet.getBlueSpawn());
    		}
    		else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			evt.setRespawnLocation(teamSet.getRedSpawn());
    		}
    		else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			evt.setRespawnLocation(teamSet.getBrownSpawn());
    		}
    		else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			evt.setRespawnLocation(teamSet.getWhiteSpawn());
    		}
		
    		player.teleport(evt.getRespawnLocation());
    		player.sendMessage("Respawning at your shipwreck.");
	    }
	    
	    
	 // Inventory Click - stop people from taking off coloured leather
	    @EventHandler
	    public void onInventoryClick(InventoryClickEvent evt) {
	    	if (evt.getSlot() == 37) {
	    		evt.setCancelled(true);
	    		Player p = (Player)evt.getWhoClicked();
	    		p.sendMessage("You can't change your team-issued pants or tunic.");
	    	}
	    	
	    	if (evt.getSlot() == 38) {
	    		evt.setCancelled(true);
	    		Player p = (Player)evt.getWhoClicked();
	    		p.sendMessage("You can't change your team-issued pants or tunic.");
	    	}
	    	
	    }
	    
	    
	    @EventHandler
	    public void abandonChecker(PlayerQuitEvent evt) {
	    	World world = evt.getPlayer().getWorld();
	    	Player player = evt.getPlayer();
	    //	System.out.println("Player left " + world.getName());
	    	
	    	if (world.getName().equals("settlers_lobby")){
	    		return;
	    	}
	    	
	    	if (world.getName().equals("settlers_template")){
	    		return;
	    	}
	    	
	    	if (!world.getName().contains("settlers")){
	    		System.out.println("Player left " + world.getName() + " that wasn't a settlers world.");
	    		return;
	    	}
	    	
	    	boolean worldEmpty = true;
	    	Iterator<Player> it3 = world.getPlayers().iterator();
	    	while(it3.hasNext()){
	    		Player p = it3.next();
	    		if (p.isOnline() && !player.equals(p)){
	    		//	System.out.println(p.getName() + " is online in " + world.getName());
	    			worldEmpty = false;
	    			return;
	    		}
	    	}
	    	
	    	if (worldEmpty){
	    		System.out.println(world.getName() + " has been abandoned; cleaning up. World will unload but not be deleted.");
	    		
	    		Iterator<Player> it2 = world.getPlayers().iterator();
		    	while(it2.hasNext()){
		    		Player p = it2.next();
		    		toLobby(p);
		    		p.sendMessage("Your game was abandoned so you were sent to the lobby and reset.");
		    	}
	    		
	    		Iterator<TeamSet> it = teamSets.iterator();
	    		while (it.hasNext()){
	    			TeamSet teamSet = it.next();
	    			if (teamSet.getWorld().getName().equals(world.getName())){
	    				// Deconstruct TeamSet
	    				teamSet.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
	    				teamSet.getObjective().unregister();
	    				teamSets.remove(teamSets.indexOf(teamSet));
	    					
	    				// unload world
	    				Bukkit.getServer().unloadWorld(teamSet.getWorld(), true);
	    				return;
	    			}
	    		}
	    		
	    		System.out.println("Error cleaning up abandoned world; couldn't find associated TeamSet.");
	    	}
	    }
	    
	    //Block Place
	    @SuppressWarnings("deprecation")
		@EventHandler
	    public void onBlockPlace(BlockPlaceEvent evt) {
	    	TeamSet teamSet = null;
	    	Player player = evt.getPlayer();
	    	
	    	
	    	// Cancel event if a resource was placed.
	    	if (	evt.getBlockPlaced().getType().equals(Material.WOOD) ||
    				evt.getBlockPlaced().getType().equals(Material.WOOL) ||
    				evt.getBlockPlaced().getType().equals(Material.CARPET) ||
    				evt.getBlockPlaced().getType().equals(Material.LOG) ||
    				evt.getBlockPlaced().getType().equals(Material.IRON_ORE)
	    	){
	    		if (	player.getWorld().getName().contains("settlers") && 
	    				!player.getWorld().getName().equals("settlers_lobby") && 
	    				!player.getWorld().getName().equals("settlers_template")
	    		){
	    			evt.getPlayer().sendMessage("You can't place resources.");
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			return;
	    		}
	    	}
	    	
	    	
	    	// Get teamSet if block placed was a relevant block
	    	if ( evt.getBlockPlaced().getType().equals(settlementItem.getType()) ||
	    		 evt.getBlockPlaced().getType().equals(cityItem.getType()) ||
	    		 evt.getBlockPlaced().getType().equals(roadItem.getType()) ||
	    		 evt.getBlockPlaced().getType().equals(DevelopmentCard.monopolyMat) ||
	    		 evt.getBlockPlaced().getType().equals(DevelopmentCard.knightMat) ||
	    		 evt.getBlockPlaced().getType().equals(DevelopmentCard.yearOfPlentyMat) ||
	    		 evt.getBlockPlaced().getType().equals(DevelopmentCard.roadBuildingMat) ||
	    		 evt.getBlockPlaced().getType().equals(DevelopmentCard.victoryPointMat)
	    	){
	    		Iterator<TeamSet> iterator = getSettlers().teamSets.iterator();
			
				while (iterator.hasNext()){
					TeamSet temp = iterator.next();
					if (temp.getWorld().getName().equals(evt.getPlayer().getWorld().getName())){
						teamSet = temp;
						break;
					}
				}
	    	} 
	    	else {
	    		return;
	    	}
	    	
	    	
	    	// Prevent settlements from being placed anywhere
	    	if (evt.getBlockPlaced().getType().equals(settlementItem.getType())){
	    		//Player player = evt.getPlayer();
	    		Location l = evt.getBlockPlaced().getLocation();
	    		World w = evt.getBlockPlaced().getWorld();
	    		Block b = w.getBlockAt(new Location(w, l.getX(), l.getY() - 1, l.getZ()));
	    		
	    		if (b.getType().equals(Material.DIAMOND_BLOCK)){
	    			
		    		int pointId = b.getMetadata("id").get(0).asInt();
		    		Point p = teamSet.getNormalBoard().getPointById(pointId);
		    		
		    		if (p.isBuildable()) {
		    			int newOwner = 0;   			
		    			//int tempScore = 0;
		    			int sb = 0;
		    			
		    			if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				// increase score by 1 for each of these
		    				newOwner = 1;
		    				//tempScore = blueTeam.getScore();
		    				sb = teamSet.getBlueTeam().getSettlementsBuilt();
		    			}
		    			else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				newOwner = 2;
		    				//tempScore = redTeam.getScore();
		    				sb = teamSet.getRedTeam().getSettlementsBuilt();
		    			}
		    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				newOwner = 3;
		    				//tempScore = brownTeam.getScore();
		    				sb = teamSet.getBrownTeam().getSettlementsBuilt();
		    			}
		    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				newOwner = 4;
		    				//tempScore = whiteTeam.getScore();
		    				sb = teamSet.getWhiteTeam().getSettlementsBuilt();
		    			}
		    			
		    			
		    			boolean allowed = false;
		    			
		    			// Check if we're connected to a road
		    			ArrayList<Edge> e = p.getEdges();
		    			for (int j = 0; j < e.size(); j++){
		    				if (e.get(j).getOwner() == newOwner || sb < 2) 
		    					allowed = true;
		    				
		    			}
		    			
		    			SettlersTeam tempTeam = teamSet.getBlueTeam();
		    			switch (newOwner){
		    			case 2:
		    				tempTeam = teamSet.getRedTeam();
		    				break;
		    			case 3:
		    				tempTeam = teamSet.getBrownTeam();
		    				break;
		    			case 4:
		    				tempTeam = teamSet.getWhiteTeam();
		    				break;
		    			}
		    			
		    			if (tempTeam.getSettlementsBuilt() >= MAX_SETTLEMENTS)
		    				allowed = false;
		    			
		    			
		    			if (allowed){
		    				p.setOwner(newOwner);
			    			p.setStructure(1);
			    			
			    			if (p.getPort() > 0){
			    				switch (p.getPort()){
			    				case 1:
			    					tempTeam.setWoodPort(true);
			    					worldBroadcast(w, tempTeam.getChatColor() + tempTeam.getTeam().getDisplayName() + " has taken the 2:1 WOOD PORT!");
			    					break;
			    				case 2:
			    					tempTeam.setBreadPort(true);
			    					worldBroadcast(w, tempTeam.getChatColor() + tempTeam.getTeam().getDisplayName() + " has taken the 2:1 BREAD PORT!");
			    					break;
			    				case 3:
			    					tempTeam.setWoolPort(true);
			    					worldBroadcast(w, tempTeam.getChatColor() + tempTeam.getTeam().getDisplayName() + " has taken the 2:1 WOOL PORT!");
			    					break;
			    				case 4:
			    					tempTeam.setBrickPort(true);
			    					worldBroadcast(w, tempTeam.getChatColor() + tempTeam.getTeam().getDisplayName() + " has taken the 2:1 BRICK PORT!");
			    					break;
			    				case 5:
			    					tempTeam.setIronPort(true);
			    					worldBroadcast(w, tempTeam.getChatColor() + tempTeam.getTeam().getDisplayName() + " has taken the 2:1 IRON PORT!");
			    					break;
			    				case 6:
			    					tempTeam.setThreePort(true);
			    					worldBroadcast(w, tempTeam.getChatColor() + tempTeam.getTeam().getDisplayName() + " has taken a 3:1 PORT!");
			    					break;		
			    				}	
			    			}
			    			
			    			// settlement deploy method
			    			deploySettlement(evt.getPlayer(), pointId, teamSet);
			    			evt.getBlockPlaced().setType(Material.AIR);
			    			player.teleport(evt.getBlockPlaced().getLocation());

			    			// Make neighboring points unbuildable.
			    			// Remove diamond blocks of unbuildable points.
			    			ArrayList<Edge> edges = p.getEdges();
			    			for (int i = 0; i < edges.size(); i++){
			    				edges.get(i).getPoints()[0].setBuildable(false);
			    				if (edges.get(i).getPoints()[0].getBoardPoint().getBlock().getType().equals(Material.DIAMOND_BLOCK))
			    					edges.get(i).getPoints()[0].getBoardPoint().getBlock().setType(Material.AIR);
			    				
			    				edges.get(i).getPoints()[1].setBuildable(false);
			    				if (edges.get(i).getPoints()[1].getBoardPoint().getBlock().getType().equals(Material.DIAMOND_BLOCK))
			    					edges.get(i).getPoints()[1].getBoardPoint().getBlock().setType(Material.AIR);
			    			}
			    			
			    			
			    			// Increase team score
			    			Objective objective = teamSet.getObjective();
			    			Score score;
			    			
			    			switch (newOwner) {
			    			case 1:
			    				//blueScore++;
			    				score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "BLUE"));
				    			tempTeam.setScore(tempTeam.getScore() + 1);
			    				score.setScore(tempTeam.getScore());
			    				tempTeam.setSettlementsBuilt(tempTeam.getSettlementsBuilt() + 1);
			    				checkScore(tempTeam, teamSet);
			    				break;
			    			case 2:
			    				//redScore++;
			    				score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "RED"));
			    				tempTeam.setScore(tempTeam.getScore() + 1);
				    			score.setScore(tempTeam.getScore());
				    			tempTeam.setSettlementsBuilt(tempTeam.getSettlementsBuilt() + 1);
				    			checkScore(tempTeam, teamSet);
			    				break;
			    			case 3:
			    				//brownScore++;
			    				score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "BROWN"));
			    				tempTeam.setScore(tempTeam.getScore() + 1);
				    			score.setScore(tempTeam.getScore());
				    			tempTeam.setSettlementsBuilt(tempTeam.getSettlementsBuilt() + 1);
				    			checkScore(tempTeam, teamSet);
			    				break;
			    			case 4:
			    				//whiteScore++;
			    				score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE + "WHITE"));
			    				tempTeam.setScore(tempTeam.getScore() + 1);
				    			score.setScore(tempTeam.getScore());
				    			tempTeam.setSettlementsBuilt(tempTeam.getSettlementsBuilt() + 1);
				    			checkScore(tempTeam, teamSet);
			    				break;
			    			default:
			    				System.err.println("Can't add to score; someone placed a settlement while not on a team.");
			    				
			    			}    			
			    			
		    			}
		    			else {
		    				evt.setCancelled(true);
		    				player.updateInventory();
		    				if (tempTeam.getSettlementsBuilt() >= MAX_SETTLEMENTS)
		    					evt.getPlayer().sendMessage("Your team can only build " + MAX_SETTLEMENTS + " settlements!");
		    				else
		    					evt.getPlayer().sendMessage("You must build settlements on your own roads.");
		    			}
		    			
		    		} 
		    		else {
		    			evt.setCancelled(true);
		    			player.updateInventory();
		    			evt.getPlayer().sendMessage("That point is blocked forever and should not be visible.");
		    		}
	    			
	    		} 
	    		
	    		else {
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			evt.getPlayer().sendMessage("Settlements must be placed on torqouise blocks at tile intersections.");
	    		}
	    	} 
	    	
	    	// City placement
	    	else if (evt.getBlockPlaced().getType().equals(cityItem.getType())) {
	    	//	Player player = evt.getPlayer();
	    		Location l = evt.getBlockPlaced().getLocation();
	    		World w = evt.getBlockPlaced().getWorld();
	    		Block b = w.getBlockAt(new Location(w, l.getX(), l.getY() - 1, l.getZ()));
	    		
		    	if (b.getType().equals(Material.WORKBENCH)){
		    		int id = b.getMetadata("id").get(0).asInt();
		    		Point p = teamSet.getNormalBoard().getPointById(id);
		    		int existingCities = 5;
		    		
		    		switch (p.getOwner()){
		    		case 1:
		    			existingCities = teamSet.getBlueTeam().getCitiesBuilt();
		    			break;
		    		case 2:
		    			existingCities = teamSet.getRedTeam().getCitiesBuilt();
		    			break;
		    		case 3:
		    			existingCities = teamSet.getBrownTeam().getCitiesBuilt();
		    			break;
		    		case 4:
		    			existingCities = teamSet.getWhiteTeam().getCitiesBuilt();
		    			break;
		    		
		    		default:
		    			
		    		}
		    		
		    		if (existingCities < MAX_CITIES) {
		    			deployCity(player, id, teamSet);
		    			b.removeMetadata("id", settlers);
		    			evt.getBlockPlaced().setType(Material.AIR);
		    			
		    		}
		    		else {
		    			evt.setCancelled(true);
		    			player.updateInventory();
		    			player.sendMessage("You have too many cities; you should not have been able to craft a 5th.");
		    		}
		    			
		    	}
		    	else {
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			evt.getPlayer().sendMessage("Cities must be placed on a workbench inside a settlement.");
	    		}
		    	
		    	
		    	
	    	}
	    	
	    	// Road placement
	    	else if (evt.getBlockPlaced().getType().equals(roadItem.getType())) {
	    		//Player player = evt.getPlayer();
	    		Location l = evt.getBlockPlaced().getLocation();
	    		World w = evt.getBlockPlaced().getWorld();
	    		Block b = w.getBlockAt(new Location(w, l.getX(), l.getY() - 1, l.getZ()));
	    		ChatColor col = ChatColor.YELLOW;
	    		
		    	if (b.getType().equals(Material.EMERALD_BLOCK)){
	    			
		    		int pointId = b.getMetadata("id").get(0).asInt();
		    		Edge e = teamSet.getNormalBoard().getEdgeById(pointId);
		    		String teamName = "unknown";
		    		
		    		if (e.getOwner() == 0) {
		    			int c = 15; // black wool
		    			int newOwner = 0;
		    			int existingRoads = 0;
		    			
		    			if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				// increase score by 1 for each of these
		    				c = LIGHT_BLUE_WOOL;
		    				teamName = "BLUE";
		    				newOwner = 1;
		    				existingRoads = teamSet.getBlueTeam().getRoadsBuilt();
		    				col = ChatColor.BLUE;
		    			}
		    			else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				c = RED_WOOL;
		    				teamName = "RED";
		    				newOwner = 2;
		    				existingRoads = teamSet.getRedTeam().getRoadsBuilt();
		    				col = ChatColor.RED;
		    			}
		    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				c = BROWN_WOOL;
		    				teamName = "BROWN";
		    				newOwner = 3;
		    				existingRoads = teamSet.getBrownTeam().getRoadsBuilt();
		    				col = ChatColor.GOLD;
		    			}
		    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    				c = WHITE_WOOL;
		    				teamName = "WHITE";
		    				newOwner = 4;
		    				existingRoads = teamSet.getWhiteTeam().getRoadsBuilt();
		    				col = ChatColor.WHITE;
		    			}
		    			
		    			boolean allowed = false;
		    			boolean notifyerRoads = false;
		    			// Check if we're connected to a road or settlement
		    			
		    			Point[] ep = e.getPoints();
		    			for (int i = 0; i < 2; i++){	
		    				
		    				if (ep[i].getOwner() == newOwner)
		    					allowed = true;
		    				
		    				// if roads built < 2, road must come off settlement.
		    				if (existingRoads < 2){
		    					// don't allow
		    					notifyerRoads = true;
		    				}
		    				else {
		    					ArrayList<Edge> otherEdges = ep[i].getEdges();
			    				for (int j = 0; j < otherEdges.size(); j++){
			    					if (otherEdges.get(j).getOwner() == newOwner) {
			    						allowed = true;
			    						
			    						Point tp = e.sharedPoint(otherEdges.get(j));
			    						if (tp.getOwner() != newOwner && tp.getOwner() != 0)
			    							allowed = false;
			    					}
			    				}
		    				}
		    				
		    				
		    				
		    			}
		    			
		    			SettlersTeam tempTeam = teamSet.getBlueTeam();
		    			switch (newOwner){
		    			case 2:
		    				tempTeam = teamSet.getRedTeam();
		    				break;
		    			case 3:
		    				tempTeam = teamSet.getBrownTeam();
		    				break;
		    			case 4:
		    				tempTeam = teamSet.getWhiteTeam();
		    				break;
		    			}
		    			
		    			if (tempTeam.getSettlementsBuilt() >= MAX_ROADS)
		    				allowed = false;
		    			
		    			
		    			if (allowed) {
		    				e.setOwner(newOwner);
		    			
		    				// road deploy method
		    				deployRoad(c, e);
		    			
		    				// remove road block
		    				evt.getBlockPlaced().setType(Material.AIR);
		    				
		    				// remove emerald block 
		    				Location temploc = evt.getBlockPlaced().getLocation().clone();
		    				temploc.setY(temploc.getY() - 1);
		    				w.getBlockAt(temploc).setType(Material.AIR);
		    			
		    				// Server message
		    				worldBroadcast(w, col + player.getDisplayName() + " has built a road for team " + teamName + "!");
		    				
		    				tempTeam.setRoadsBuilt(tempTeam.getRoadsBuilt() + 1);
		    				
		    				if (newOwner > 4 && newOwner < 1) 
			    				System.err.println("Someone placed a road while not on a team.");
			    				
		    				ArrayList<Point> allPointsList = new ArrayList<Point>(); 
		    				Point[] allPointsArray = teamSet.getNormalBoard().allPoints();
		    				for (int i = 0; i < allPointsArray.length; i++){
		    					allPointsList.add(allPointsArray[i]);
		    				}
		    				
		    				Edge[] allEdgesArray = teamSet.getNormalBoard().allEdges();
		    				ArrayList<Edge> ownerEdges = new ArrayList<Edge>();
		    				for (int i = 0; i < allEdgesArray.length; i++){
		    					if (allEdgesArray[i].getOwner() == newOwner) {
		    						ownerEdges.add(allEdgesArray[i]);
		    					}
		    				}
		    				
		    				ArrayList<Point> ownerPoints = new ArrayList<Point>();
		    				Iterator<Edge> it = ownerEdges.iterator();
		    				while (it.hasNext()){
		    					Edge tempOwnerEdge = it.next();
		    					ownerPoints.add(tempOwnerEdge.getPoints()[0]);
		    					ownerPoints.add(tempOwnerEdge.getPoints()[1]);
		    				}
		    				
		    				// for each point in ownerPoints, check the longest path from that point
		    				// Need to loop through that here:
		    				double longestPath = 0;
		    				double oldPath;
		    				
		    				Iterator<Point> it2 = ownerPoints.iterator(); 
		    				
		    				while (it2.hasNext()){
		    					oldPath = longestPath;
		    					longestPath = initLongestPath(allPointsList, it2.next(), newOwner);
		    					if (oldPath > longestPath)
		    						longestPath = oldPath;
		    					
		    				}
		    				
		    				player.sendMessage("Your team's longest road: " + longestPath);
		    				tempTeam.setLongRoad((int)longestPath);
		    				
		    				// Check for longest road
				    		if (teamSet.longestRoadCalc(tempTeam)) {
				    			worldBroadcast(w, tempTeam.getChatColor() + "LONGEST ROAD was claimed by team " + tempTeam.getTeam().getName() + "!");
				    			
				    			SettlersTeam[] teams = {teamSet.getBlueTeam(), teamSet.getRedTeam(), teamSet.getBrownTeam(), teamSet.getWhiteTeam()};
				    			
				    			Objective objective = teamSet.getObjective();
				    			Score score = null;
				    			
				    			for (int i = 0; i < teams.length; i++){
				    				if (teams[i].hasLongestRoad()){
				    					teams[i].setHasLongestRoad(false);
				    					teams[i].setScore(teams[i].getScore() - 2);
				    					
				    					switch(teams[i].getTeam().getName()){
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
						    				System.err.println("Error finding team losing longest road.");
				    					}
				    					
				    					score.setScore(teams[i].getScore());
				    				}
				    			}
				    			
				    				
				    			tempTeam.setScore(tempTeam.getScore() + 2);
				    			tempTeam.setHasLongestRoad(true);
				    			
				    			String name = tempTeam.getTeam().getName();
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
				    				System.err.println("Error finding team gaining largest army.");
				    				
				    			}    			
				    			
				    			score.setScore(tempTeam.getScore());		
				    					
				    		}
				    			
				    		// Check Score
				    		checkScore(tempTeam, teamSet);
		    				
		    				
		    				
		    				
		    			}
		    			else {
		    				if (tempTeam.getSettlementsBuilt() >= MAX_ROADS)
		    					player.sendMessage("Your team can only build " + MAX_ROADS + " roads.");
		    				else if (notifyerRoads)
		    					player.sendMessage("You must first place your starter roads, connecting DIRECTLY to settlements.");
		    				else
		    					player.sendMessage("Roads must connect to your existing roads or settlements.");
		    				
		    				evt.setCancelled(true);
		    				player.updateInventory();
		    			}
		    				
		    			
		    		} 
		    		else {
		    			evt.setCancelled(true);
		    			player.updateInventory();
		    			evt.getPlayer().sendMessage("That road is blocked forever and should not be visible.");
		    		}
	    			
	    		} 
		    	else {
		    		evt.setCancelled(true);
		    		player.updateInventory();
	    			player.sendMessage("Roads must be placed on emerald blocks.");
		    	}
	    	}
	    	else if ( 	evt.getBlockPlaced().getType().equals(DevelopmentCard.roadBuildingMat)){
	    		// Determine SettlersTeam
	    		//Player player = evt.getPlayer();
	    		SettlersTeam tempTeam = teamSet.getBlueTeam();
	    		if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
    			}
    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getBrownTeam();
    			}
    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getWhiteTeam();
    			}
    			
	    		if (tempTeam.isCardBlocked()){
	    			player.sendMessage("Development Cards have a " + (CARD_COOLDOWN/20) + " second cooldown, shared by your team.");
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			return;
	    		}

	    		// remove the block
	    		evt.getBlock().setType(Material.AIR);
	    		evt.getBlock().getWorld().playEffect(evt.getBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
	    		
	    		DevelopmentCard.roadBuildingEffect(tempTeam, player);
	    		tempTeam.setCardBlocked(true);
	    		new UnlockCardsTask(tempTeam).runTaskLater(settlers, CARD_COOLDOWN);
	    	}
	    	
	    	else if (evt.getBlockPlaced().getType().equals(DevelopmentCard.yearOfPlentyMat)) {
	    		// Determine SettlersTeam
	    		//Player player = evt.getPlayer();
	    		SettlersTeam tempTeam = teamSet.getBlueTeam();
	    		if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
    			}
    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getBrownTeam();
    			}
    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getWhiteTeam();
    			}
    			    		
	    		if (tempTeam.isCardBlocked()){
	    			player.sendMessage("Development Cards have a " + (CARD_COOLDOWN/20) + " second cooldown, shared by your team.");
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			return;
	    		}
	    		
	    		// remove the block
	    		evt.getBlock().setType(Material.AIR);
	    		evt.getBlock().getWorld().playEffect(evt.getBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
	    		
	    		DevelopmentCard.yearOfPlentyEffect(tempTeam, player);
	    		tempTeam.setCardBlocked(true);
	    		new UnlockCardsTask(tempTeam).runTaskLater(settlers, CARD_COOLDOWN);
	    	}
	    	
	    	else if (evt.getBlockPlaced().getType().equals(DevelopmentCard.knightMat)){
	    		// Determine SettlersTeam
	    		//Player player = evt.getPlayer();
	    		Location l = evt.getBlockPlaced().getLocation();
	    		World w = evt.getBlockPlaced().getWorld();
	    		Block b = w.getBlockAt(new Location(w, l.getX(), l.getY() - 1, l.getZ()));
	    		NormalBoard normalBoard = teamSet.getNormalBoard();
	    		
	    		if (b.getType().equals(Material.GOLD_BLOCK)){
	    			
		    		int pointId = b.getMetadata("id").get(0).asInt();
		    		Tile tile = normalBoard.getTileById(pointId);
		    		
		    		SettlersTeam tempTeam = teamSet.getBlueTeam();
		    		if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
		    			tempTeam = teamSet.getRedTeam();
	    			}
	    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    				 tempTeam = teamSet.getBrownTeam();
	    			}
	    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    				 tempTeam = teamSet.getWhiteTeam();
	    			}
		    		
		    		if (tempTeam.isCardBlocked()){
		    			player.sendMessage("Development Cards have a " + (CARD_COOLDOWN/20) + " second cooldown, shared by your team.");
		    			evt.setCancelled(true);
		    			player.updateInventory();
		    			return;
		    		}
		    		
		    		tempTeam.setKnightAmount(tempTeam.getKnightAmount() + 1);
    		   		
		    		
		    		if (normalBoard.getBlockedTileId() > -1) {
		    			Tile oldTile = normalBoard.getBlockedTile();
		    			skySkull(oldTile.getBoardLocation(), Material.AIR);
		    		}
		    		
		    		normalBoard.unblockTiles();
		    		tile.setBlocked(true);
		    		normalBoard.setBlockedTile(tile);
		    		normalBoard.setBlockedTileId(tile.getId());
		    		skySkull(tile.getBoardLocation(), SKY_SKULL_MATERIAL);
		    		
		    		// Start 'haunt tile' async task - this is part of dice roll async task.

		    		
		    		// remove the knight block (not the gold block)
		    		evt.getBlock().setType(Material.AIR);
		    		evt.getBlock().getWorld().playEffect(evt.getBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
		    		DevelopmentCard.knightEffect(tempTeam, player);
		    		tempTeam.setCardBlocked(true);
		    		new UnlockCardsTask(tempTeam).runTaskLater(settlers, CARD_COOLDOWN);
		    		
		    		
		    		// Check for largest army
		    		if (teamSet.largestArmyCalc(tempTeam)) {
		    			worldBroadcast(w, tempTeam.getChatColor() + "LARGEST ARMY was claimed by team " + tempTeam.getTeam().getName() + "!");
		    			
		    			SettlersTeam[] teams = {teamSet.getBlueTeam(), teamSet.getRedTeam(), teamSet.getBrownTeam(), teamSet.getWhiteTeam()};
		    			
		    			Objective objective = teamSet.getObjective();
		    			Score score = null;
		    			
		    			for (int i = 0; i < teams.length; i++){
		    				if (teams[i].hasLargestArmy()){
		    					teams[i].setHasLargestArmy(false);
		    					teams[i].setScore(teams[i].getScore() - 2);
		    					
		    					switch(teams[i].getTeam().getName()){
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
				    				System.err.println("Error finding team losing largest army.");
		    					}
		    					
		    					score.setScore(teams[i].getScore());
		    				}
		    			}
		    			
		    				
		    			tempTeam.setScore(tempTeam.getScore() + 2);
		    			tempTeam.setHasLargestArmy(true);
		    			
		    			String name = tempTeam.getTeam().getName();
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
		    				System.err.println("Error finding team gaining largest army.");
		    				
		    			}    			
		    			
		    			score.setScore(tempTeam.getScore());		
		    					
		    		}
		    			
		    		// Check Score
		    		checkScore(tempTeam, teamSet);
		    		
		    		
	    		}
	    		else {
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			player.sendMessage("You must place knights on gold blocks.");
	    			return;
	    		}
		    		
		    	
	    		
	    	}
	    	
	    	else if (evt.getBlockPlaced().getType().equals(DevelopmentCard.victoryPointMat) ){
	    		// Determine SettlersTeam
	    		//Player player = evt.getPlayer();
	    		SettlersTeam tempTeam = teamSet.getBlueTeam();
	    		if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
    			}
    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getBrownTeam();
    			}
    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getWhiteTeam();
    			}
    			    		
	    		// remove the block
	    		evt.getBlock().setType(Material.AIR);
	    		evt.getBlock().getWorld().playEffect(evt.getBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
	    		
	    		DevelopmentCard.victoryPointEffect(tempTeam, player);
	    		
	    		// check the SCORE
	    		checkScore(tempTeam, teamSet);
	    	}
	    	
	    	else if ( evt.getBlockPlaced().getType().equals(DevelopmentCard.monopolyMat) ){
	    		// Determine SettlersTeam
	    		//Player player = evt.getPlayer();
	    		SettlersTeam tempTeam = teamSet.getBlueTeam();
	    		if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
    			}
    			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getBrownTeam();
    			}
    			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    				 tempTeam = teamSet.getWhiteTeam();
    			}
    			    		
	    		if (tempTeam.isCardBlocked()){
	    			player.sendMessage("Development Cards have a " + (CARD_COOLDOWN/20) + " second cooldown, shared by your team.");
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			return;
	    		}
	    		
	    		// remove the block
	    		evt.getBlock().setType(Material.AIR);
	    		evt.getBlock().getWorld().playEffect(evt.getBlock().getLocation(), Effect.ENDER_SIGNAL, 0);
	    		
	    		// Get target type
	    		Block b = evt.getBlockPlaced().getRelative(BlockFace.DOWN);
	    		
	    		ItemStack target = null;
	    		
	    		
	    		if (b.getType().equals(Material.GRASS)){
	    			target = new ItemStack(Material.WOOL);
	    			ItemMeta im = target.getItemMeta();
	    			im.setDisplayName("WOOL");
	    			target.setItemMeta(im);
	    		}
	    		else if (b.getType().equals(Material.IRON_ORE)){
	    			target = new ItemStack(Material.IRON_INGOT);
	    			ItemMeta im = target.getItemMeta();
	    			im.setDisplayName("IRON INGOTS");
	    			target.setItemMeta(im);
	    		}
	    		else if (b.getType().equals(Material.PUMPKIN)){
	    			target = new ItemStack(Material.BREAD);
	    			ItemMeta im = target.getItemMeta();
	    			im.setDisplayName("BREAD");
	    			target.setItemMeta(im);
	    		}
	    		else if (b.getType().equals(Material.LOG)){
	    			target = new ItemStack(Material.WOOD);
	    			ItemMeta im = target.getItemMeta();
	    			im.setDisplayName("WOOD PLANKS");
	    			target.setItemMeta(im);
	    		}
	    		else if (b.getType().equals(Material.CLAY)){
	    			target = new ItemStack(Material.BRICK);
	    			ItemMeta im = target.getItemMeta();
	    			im.setDisplayName("BRICKS");
	    			target.setItemMeta(im);
	    		}
	    		else {
	    			evt.setCancelled(true);
	    			player.updateInventory();
	    			player.sendMessage("You must place that on grass, clay, ore, a pumpkin, or a log.");
	    			return;
	    		}
	    		
	    		// execute the card
	    		DevelopmentCard.monopolyEffect(tempTeam, player, target);
	    		tempTeam.setCardBlocked(true);
	    		new UnlockCardsTask(tempTeam).runTaskLater(settlers, CARD_COOLDOWN);
    		
	    	}
	       
	    }
	    
	    
	    // Craft Item
	    @EventHandler
	    public void onCraftItem(CraftItemEvent evt) {
	    	Player player = (Player)evt.getWhoClicked();
	    	TeamSet teamSet = null;
	    
	    	Iterator<TeamSet> iterator = teamSets.iterator();
	    	
	    	if (!iterator.hasNext()) {
	    		return;
	    	}
			
			while (iterator.hasNext()){
				TeamSet temp = iterator.next();
				if (temp.getWorld().getName().equals(player.getWorld().getName())){
					teamSet = temp;
					break;
				}
			}
	    	
	    	if (teamSet == null){
	    		return;
	    	}
	    	
	    	ItemStack item = evt.getCurrentItem();
	    	SettlersTeam tempTeam = teamSet.getBlueTeam();
	    	
	    	if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			tempTeam = teamSet.getBlueTeam();
    		} else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			tempTeam = teamSet.getRedTeam();
    		} else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			tempTeam = teamSet.getBrownTeam();
    		} else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
    			tempTeam = teamSet.getWhiteTeam();
    		} 
    		else {
    			player.sendMessage("You can't do that while not on a team.");
    			System.out.println(player.getDisplayName() + " is crafting in a game, teamless!");
    			return;
    		}
	    
	    	
	    	if (item.hasItemMeta()) {
	    	
	    	if (item.getItemMeta().getDisplayName().equals("City Upgrade")) {
	    		/*if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getBlueTeam();
	    		} else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
	    		} else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getBrownTeam();
	    		} else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getWhiteTeam();
	    		} 
	    		
	    		else {
	    			evt.setCancelled(true);
	    			System.err.println(player.getDisplayName() + " crafted a city while not on a team. That shouldn't be possible.");
	    		}*/
	    		
	    		
	    		if (tempTeam.getCitiesBuilt() >= MAX_CITIES) {
    				evt.setCancelled(true);
    				player.sendMessage("You already have " + MAX_CITIES + " placed cities!");
    			}
	    		return;
	    	}
	    	
	    	else if (item.getItemMeta().getDisplayName().equals("Settlement")) {
	    		/*if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getBlueTeam();
	    		} else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
	    		} else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getBrownTeam();
	    		} else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getWhiteTeam();
	    		} 
	    		else {
	    			evt.setCancelled(true);
	    			System.err.println(player.getDisplayName() + " crafted a settlement while not on a team.");
	    		}*/
	    		
	    		
	    		if (tempTeam.getSettlementsBuilt() >= MAX_SETTLEMENTS) {
    				evt.setCancelled(true);
    				player.sendMessage("You already have " + MAX_SETTLEMENTS + " placed settlements!");
    			}
	    		return;
	    	}
	    	
	    	else if (item.getItemMeta().getDisplayName().equals("Road")) {
	    		/*if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getBlueTeam();
	    		} else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getRedTeam();
	    		} else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getBrownTeam();
	    		} else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
	    			tempTeam = teamSet.getWhiteTeam();
	    		}
	    		
	    		else {
	    			evt.setCancelled(true);
	    			System.err.println(player.getDisplayName() + " built a road while not on a team.");
	    		}*/
	    		
	    		
	    		if (tempTeam.getSettlementsBuilt() >= MAX_ROADS) {
    				evt.setCancelled(true);
    				player.sendMessage("You already have " + MAX_ROADS + " placed roads!");
    			}
	    		return;
	    	}
	    	
	    	else if (item.getItemMeta().getDisplayName().equals("Development Card")){
	    		Inventory inv = ((Player)evt.getWhoClicked()).getInventory();
	    		
	    		if (inv.firstEmpty() < 0){
	    			player.sendMessage("You can't create a development card with a full inventory. ");
	    			evt.setCancelled(true);
	    		}
	    		else {
	    			// Development Cards will default to knights
	    			ItemStack newCard = DevelopmentCard.getKnightItem();
	    			DevelopmentCard cardDrawn;
	    			
	    			if (teamSet.getDevelopmentCards().isEmpty()){
	    				player.sendMessage("There are no more development cards!");
	    				evt.setCancelled(true);
	    				return;
	    			}
	    			else {
	    				cardDrawn = teamSet.drawCard();
	    				if (teamSet.getDevelopmentCards().isEmpty())
	    					worldBroadcast(player.getWorld(), ChatColor.BOLD + player.getDisplayName() + " bought the LAST development card!");
	    			}
	    			
	    			if (cardDrawn.getName().equals("roadBuilding")){
	    				newCard = DevelopmentCard.getRoadBuildingItem();
	    			}
	    			else if (cardDrawn.getName().equals("yearOfPlenty")){
	    				newCard = DevelopmentCard.getYearOfPlentyItem();
	    			}
	    			else if (cardDrawn.getName().equals("monopoly")){
	    				newCard = DevelopmentCard.getMonopolyItem();
	    			}
	    			else if (cardDrawn.getName().equals("victoryPoint")){
	    				newCard = DevelopmentCard.getVictoryPointItem();
	    			}
	    			

	    			// TODO: find out how to immediately update the inventory - without deprecated method?

	    			item.setAmount(newCard.getAmount());
	    			item.setItemMeta(newCard.getItemMeta());
	    			item.setType(newCard.getType());
	    			
	    			player.sendMessage("You created a " + newCard.getItemMeta().getDisplayName() + " card!");
	    			worldBroadcast(player.getWorld(), tempTeam.getChatColor() + player.getDisplayName() + " created a development card!");
	    		}
	    	}
	    	}
	    	
	    	Material m = evt.getRecipe().getResult().getType();
	    	World world = player.getWorld();
	    	
	    	if (m.equals(Material.IRON_INGOT)){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " refined 1 IRON_INGOT.");
	    	}
	    	else if (m.equals(Material.CLAY_BRICK)){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " refined 1 BRICK.");
	    	}
	    	else if (m.equals(Material.WOOD)){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " refined 1 WOOD PLANK.");
	    	}
	    	else if (m.equals(Material.BREAD)){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " refined 1 BREAD.");
	    	}
	    	else if (m.equals(Material.WOOL)){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " refined 1 WOOL.");
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecBread.getIngredientList())){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 4 BREAD for 2 RUPEES.");
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecBread3.getIngredientList())){
	    		if (tempTeam.hasThreePort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 3 BREAD for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have a 3:1 port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecBread2.getIngredientList())){
	    		if (tempTeam.hasBreadPort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 2 BREAD for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have the bread port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecWool.getIngredientList())){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 4 WOOL for 2 RUPEES.");
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecWool3.getIngredientList())){
	    		if (tempTeam.hasThreePort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 3 WOOL for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have a 3:1 port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecWool2.getIngredientList())){
	    		if (tempTeam.hasWoolPort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 2 WOOL for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have the wool port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecIngot.getIngredientList())){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 4 IRON INGOTS for 2 RUPEES.");
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecIngot3.getIngredientList())){
	    		if (tempTeam.hasThreePort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 3 IRON INGOTS for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have a 3:1 port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecIngot2.getIngredientList())){
	    		if (tempTeam.hasIronPort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 2 IRON INGOTS for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have the iron port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecPlank.getIngredientList())){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 4 WOOD PLANKS for 2 RUPEES.");
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecPlank3.getIngredientList())){
	    		if (tempTeam.hasThreePort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 3 WOOD PLANKS for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have a 3:1 port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecPlank2.getIngredientList())){
	    		if (tempTeam.hasWoodPort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 2 WOOD PLANKS for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have the wood port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecBrick.getIngredientList())){
	    		worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 4 BRICKS for 2 RUPEES.");
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecBrick3.getIngredientList())){
	    		if (tempTeam.hasThreePort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 3 BRICKS for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have a 3:1 port!");
	    		}
	    	}
	    	else if (((ShapelessRecipe)evt.getRecipe()).getIngredientList().equals(rupRecBrick2.getIngredientList())){
	    		if (tempTeam.hasBrickPort())
	    			worldBroadcast(world, tempTeam.getChatColor() + player.getDisplayName() + ChatColor.YELLOW + " traded 2 BRICK for 2 RUPEES.");
	    		else {
	    			evt.setCancelled(true);
	    			player.sendMessage("You don't have the brick port!");
	    		}
	    	}

	    	
	    	
	    }
	    
	    // Player interact event
	    @EventHandler
	    public void onInteract(PlayerInteractEvent event) {
	    	// wheat saver
	        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL){
	        	event.setCancelled(true);
	        	return;
	        }
	        
	        if (event.getClickedBlock() == null)
	        	return;
	        
	        if (event.getClickedBlock().getType().equals(Material.IRON_DOOR_BLOCK)){
	        	TeamSet teamSet = null;
    	    	Iterator<TeamSet> iterator = getSettlers().teamSets.iterator();
    	    	int data = 0;
    	    	Player player = event.getPlayer();
    	    	Location l = player.getLocation();
    			
    			while (iterator.hasNext()){
    				TeamSet temp = iterator.next();
    				if (temp.getWorld().getName().equals(player.getWorld().getName())){
    					teamSet = temp;
    					break;
    				}
    			}
    	    	
    	    	if (teamSet == null){
    	    		player.sendMessage("You can't enter settlements while not on a team.");
    	    		return;
    	    	}
    	    	
    			if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
					data = LIGHT_BLUE_WOOL;
				}
				else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
					data = RED_WOOL ;
				}
				else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
					data = BROWN_WOOL;
				}
				else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
					data = WHITE_WOOL;
				}	
    	
	    		if ((int)l.getBlock().getData() != data && (int)l.getBlock().getRelative(BlockFace.DOWN).getData() != data) {
	    	    	/*Vector v = player.getLocation().getDirection().clone();
	    			int deltaX, deltaZ;
	    				    	
	    			
	    			if (Math.abs(v.getX()) > Math.abs(v.getZ())) {
	    				if (v.getX() > 0){
		    				deltaX = -4;
		    				
		    			} else
		    				deltaX = 4;
	    				deltaZ = 0;
	    			}
	    			else {
	    				if (v.getZ() > 0) {
		    				deltaZ = -4;
		    			} else
		    				deltaZ = 4;
	    				deltaX = 0;
	    			}
	    			
	    			
	    			Location oldLoc = player.getLocation().clone();
	    			player.sendMessage("You can't enter other teams' settlements!");
	    			player.teleport(new Location(l.getWorld(), l.getX() + deltaX, l.getY() + 1, l.getZ() + deltaZ, oldLoc.getYaw(), oldLoc.getPitch()));

	    			player.damage(4);*/

	    			player.sendMessage("You shall not pass!");
	    			event.setCancelled(true);
	    		}
	    		else{
	    		//	Vector v = player.getLocation().getDirection().clone();
	    			double doorZ = event.getClickedBlock().getLocation().getZ();
	    			double doorX = event.getClickedBlock().getLocation().getX();
	    			
	    			int deltaX=0, deltaZ=0;
	    				    	
	    			
	    			/*if (Math.abs(v.getX()) > Math.abs(v.getZ())) {
	    				if (v.getX() > 0){
		    				deltaX = 2;
		    				
		    			} else
		    				deltaX = -2;
	    				
	    			}
	    			else {
	    				if (v.getZ() > 0) {
		    				deltaZ = 2;
		    			} else
		    				deltaZ = -2;
	    				
	    			}*/
	    			
	    			if (event.getBlockFace().equals(BlockFace.EAST))
	    				deltaX = -2;
	    			else if (event.getBlockFace().equals(BlockFace.WEST))
	    				deltaX = 2;
	    			else if (event.getBlockFace().equals(BlockFace.NORTH))
	    				deltaZ = 2;
	    			else if (event.getBlockFace().equals(BlockFace.SOUTH))
	    				deltaZ = -2;
	    			else 
	    				return;
	   
	    			
	    			Location oldLoc = player.getLocation().clone();
	    			player.teleport(new Location(l.getWorld(), doorX + deltaX, l.getY(), doorZ + deltaZ, oldLoc.getYaw(), oldLoc.getPitch()));
	    			event.setCancelled(true);
	    		}
	        }
	    }
	    
	    // No starving
	    @EventHandler
	    public void noStarving(FoodLevelChangeEvent event){
	    	event.setCancelled(true);
	    }
	    
	    @EventHandler
	    public void craftClicking(InventoryClickEvent event){
	    	if (event.getInventory().getType().equals(InventoryType.WORKBENCH)){

	    		/*if (event.getCurrentItem().getType().equals(Material.EMERALD)){
	    			int slot = event.getSlot();
	    			((Player)event.getWhoClicked()).sendMessage(slot + "");
	    			
	    			Inventory inv = event.getInventory();
	    			for (int i = 1; i < 10; i++){
	    				if (i != slot && inv.getItem(slot) != null && !inv.getItem(slot).getType().equals(Material.EMERALD)) {
	    					inv.setItem(i, new ItemStack(Material.GHAST_TEAR));
	    				}
	    			}
	    			
	    		}
	    		else if (event.getCurrentItem().getType().equals(Material.GHAST_TEAR)){
	    			event.setCancelled(true);
	    			((Player)event.getWhoClicked()).sendMessage("Don't touch that!");
	    		}	    */		
	    	}	
	    }
	    
	    // Sprinting
		@EventHandler
		public void sprinting(PlayerToggleSprintEvent evt){
			Player player = evt.getPlayer();
			Location l = player.getLocation();
			Block b = l.getBlock().getRelative(BlockFace.DOWN);
			int data = 0;
			
			// Might be best to use metadata here.
			if (b.getType().equals(Material.WOOL)) {
				data = b.getData();
			}
			else 
				return;
			
			if (player.hasMetadata("settlers_team")) {
				String team = player.getMetadata("settlers_team").get(0).asString();
				
				if ((team.equals("blue") && data == LIGHT_BLUE_WOOL) ||
						(team.equals("red") && data == RED_WOOL ||
						(team.equals("brown") && data == BROWN_WOOL) ||
						(team.equals("white") && data == WHITE_WOOL)))
				{
					
					if (player.hasPermission("settlers.speed2"))
						new SpeedPlayer2Task(player, settlers).runTask(settlers);
					else if (player.hasPermission("settlers.speed1"))
						new SpeedPlayer1Task(player, settlers).runTask(settlers);
					
				}
					

				
			} 
		
		}
		
	    
	} // end Listener
	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("set.givemap")) { 
			Player target; 
			
			try {
				target = (Bukkit.getServer().getPlayer(args[0]));
			}
			catch (Exception e){
				if (sender instanceof Player){
					if (!sender.hasPermission("settlers.givemap_self ")){
						sender.sendMessage("You don't have admin command permissions.");
						return false;
					} else {
						target = (Player)sender;
						giveMap(target,target.getWorld());
						sender.sendMessage("Successfully got yourself a map.");
					}
				} 
				else {
					sender.sendMessage("You must specify a player.");
					return false;
				}
			}
					
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
				return false;
		    } 
			else if (sender instanceof Player && target == (Player)sender){}
			else {
				if (!sender.hasPermission("settlers.admin_commands")){
					sender.sendMessage("You don't have admin command permissions.");
					return false;
				}
				else {
					giveMap(target,target.getWorld());
					sender.sendMessage("Successfully gave a map to " + target.getDisplayName());
				}
			}
		
			return true;
		} // end givemap command
		
		if (cmd.getName().equalsIgnoreCase("set.makehex")) { 
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return false;
			}
			Player target = (Bukkit.getServer().getPlayer(args[0]));
			
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
				return false;
		    } 
			else {
				String type = args[1];
				int radius = Integer.parseInt(args[2]);
				World w = target.getWorld();
				Location l = target.getLocation();
				l.setY((int)l.getY() - 1);
				
				// makeHexagon(type, l, w, radius);

				BigHexagon hex = new BigHexagon(type, l, radius);
				hex.generate(w);
				
				sender.sendMessage("Successfully made a hexagon of radius " + radius);

			}
		
			// set.makehex waffles87 forest 50
			
			return true;
		} // end makeHex command
		
		
		
		if (cmd.getName().equalsIgnoreCase("set.bignum")) { 
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return true;
			}
			Player target = (Bukkit.getServer().getPlayer(args[0]));
			
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
				return true;
		    } 
			else
			{
				int value = Integer.parseInt(args[1]);
				String type = args[2];
				int thickness = Integer.parseInt(args[3]);
				World w = target.getWorld();
				Location l = target.getLocation();
				l.setY(l.getY() + 100);
				Material m = Material.getMaterial(type);
				
				BigNumber bn = new BigNumber(value,thickness);
				bn.place(w, l, m);
				
				w.getBlockAt(l).setType(m);
				sender.sendMessage("Placed big number " + value + " of thickness " + thickness);

			}
		
			return true;
		} // end makeBigNumber command
		
		
		
		if (cmd.getName().equalsIgnoreCase("set.copy")) { 
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return true;
			}
			// args
			String n1 = args[0];
			String n2 = args[1];
			
			// Create new world for this board
			copyWorld(n1, n2);
			System.out.println("World copied.");
			//WorldCreator wc = new WorldCreator (name);
			//Bukkit.getServer().createWorld(wc);
			//World world = Bukkit.getServer().getWorld(name);
			//world.setSpawnLocation(0, 64, 0);
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("all")) { 
			if (args.length < 1)
				return false;
			
			// args
			String msg = args[0];
			for (int i = 1; i<args.length; i++){
				msg = msg + " " + args[i];
			}
			
			if (sender instanceof Player) {
				Player player = (Player)sender;
				ChatColor cy = ChatColor.YELLOW;
				
				if (player.hasMetadata("settlers_team")) {
					String team = player.getMetadata("settlers_team").get(0).asString();
					ChatColor c = ChatColor.YELLOW;
					ChatColor cw = ChatColor.WHITE;
					
					switch(team){
					case "blue":
						c = ChatColor.BLUE;
						break;
					case "red":
						c = ChatColor.RED;
						break;
					case "brown":
						c = ChatColor.GOLD;
						break;
					case "white":
						c = ChatColor.BOLD;
						break;
					
					}
					
					worldBroadcast(player.getWorld(), cy + "(All) " + cw + c + player.getDisplayName() + ": " + cy + msg);
				}
				else {
					worldBroadcast(player.getWorld(), cy + "(All) " + player.getDisplayName() + ": " + msg);
				}
			}
			
			return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("team")) { 
			if (args.length < 1)
				return false;
			
			// args
			String msg = args[0];
			for (int i = 1; i<args.length; i++){
				msg = msg + " " + args[i];
			}
			
			if (sender instanceof Player) {
				Player player = (Player)sender;
				
				if (player.hasMetadata("settlers_team")) {
					String team = player.getMetadata("settlers_team").get(0).asString();
					ChatColor c = ChatColor.WHITE;
					
					switch(team){
					case "blue":
						c = ChatColor.BLUE;
						break;
					case "red":
						c = ChatColor.RED;
						break;
					case "brown":
						c = ChatColor.GOLD;
						break;
					case "white":
						c = ChatColor.BOLD;
						break;
					
					}
					
					Iterator<Player> it = player.getWorld().getPlayers().iterator();
					while (it.hasNext()){
						Player rec = it.next();
						if (rec.hasMetadata("settlers_team")){
							if (rec.getMetadata("settlers_team").get(0).asString().equals(team)){
								rec.sendMessage(c + "(Team) " + player.getDisplayName() + ": " + msg);
							}
						}
					}
					
				}
				else {
					sender.sendMessage("You are not on a team.");
					return true;
				}
				
			}
			
			return true;
		}
		
		
		/*if (cmd.getName().equalsIgnoreCase("set.toteam")) { 
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return false;
			}
			
			if (args.length < 2) {
				return false;
			}
			
			// args
			Player p = Bukkit.getServer().getPlayer(args[0]);
			
			if (p == null) {
				sender.sendMessage(args[0] + " is not online!");
				return false;
			}
			
			String teamName = args[1];
			
			toTeam(p, teamName);
			
			return true;
		}*/
		
		if (cmd.getName().equalsIgnoreCase("set.makeboard")) { 
			
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return true;
			}
			
			if (args.length < 1) {
				return false;
			}
					
			
		
			return true;
		} // end makeboard command
		
		
		if (cmd.getName().equalsIgnoreCase("set.enqueue") || cmd.getName().equalsIgnoreCase("set.q")) { 
			if (! (sender instanceof Player)) {
				if (args.length < 1) {
					return false;
				}
				
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(args[0] + " is not online!");
					return true;
				}
				if (!player.getWorld().getName().equals("settlers_lobby")){
					sender.sendMessage(args[0] + " is not in the lobby!");
					return true;
				}
				enqueue(player);
				return true;
			}
	
			if (args.length < 1) {
				if (sender.hasPermission("settlers.queue_self")) {
					Player player = (Player) sender;
					if (!player.getWorld().getName().equals("settlers_lobby")){
						sender.sendMessage("You are not in the lobby!");
						return true;
					}
					
					
					enqueue((Player) sender);
					return true;
				}
				else {
					sender.sendMessage("You don't have admin command permissions.");
					return false;
				}
			}
			else {
				Player player = Bukkit.getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage(args[0] + " is not online!");
					return false;
				}
				if (!player.getWorld().getName().equals("settlers_lobby")){
					sender.sendMessage(args[0] + " is not in the lobby!");
					return true;
				}
				if (player.equals((Player)sender)){
					enqueue(player);
					return true;
				}
					
				
				if (!sender.hasPermission("settlers.admin_commands")){
					sender.sendMessage("You don't have admin command permissions.");
					return false;
				}
				else {
					
					enqueue(player);
				}
			}
			
			return true;
		} 
		
		if (cmd.getName().equalsIgnoreCase("set.dequeue") || cmd.getName().equalsIgnoreCase("set.dq")) { 
			if (! (sender instanceof Player)) {
				if (args.length < 1) {
					return false;
				}
				
				Player player = Bukkit.getPlayer(args[0]);
				dequeue(player);
				return true;
			}
	
			if (args.length < 1) {
				if (sender.hasPermission("settlers.queue_self")) {
					dequeue((Player) sender);
					return true;
				}
				else {
					sender.sendMessage("You don't have admin command permissions.");
					return false;
				}
			}
			else {
				Player player = Bukkit.getPlayer(args[0]);
				
				if (player.equals((Player)sender)){
					enqueue(player);
					return true;
				}
				
				if (!sender.hasPermission("settlers.admin_commands")){
					sender.sendMessage("You don't have admin command permissions.");
					return false;
				}
				else {
					
					dequeue(player);
				}
			}
			
			return true;
		} 
		
		
		
	/*	if (cmd.getName().equalsIgnoreCase("set.water")) { 
			
			// args
	
			int radius = 50;
			String name = args[0];
			
			// Create new world for this board
			//copyWorld("settlers_template", name);
			WorldCreator wc = new WorldCreator (name);
			Bukkit.getServer().createWorld(wc);
			World world = Bukkit.getServer().getWorld(name);
			world.setSpawnLocation(0, 64, 0);
			
			world.setDifficulty(Difficulty.PEACEFUL);
			//world.setMonsterSpawnLimit(0);
			//world.setAnimalSpawnLimit(0);
			
			
			// Generate virtual map and numbers

			
			// set location to spawn point
			Location baseLoc = world.getSpawnLocation();
			baseLoc.setY(BASE_ELEVATION);
			Location l = baseLoc.clone();
			
			// Make all the hexagons on board
			
			//  at left
			//BigHexagon hex = new BigHexagon("forest", l, radius);
			l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
			l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
			l.setX(l.getX() - ((radius * 2) + (ROAD_WIDTH * 4)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() + radius + ROAD_WIDTH);
			l.setZ(l.getZ() - ((radius * 1.5) + (ROAD_WIDTH * 2)));
			waterHex(l,  radius + ROAD_WIDTH);
			
			l.setX(l.getX() + radius + ROAD_WIDTH);
			l.setZ(l.getZ() - ((radius * 1.5) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() + radius + ROAD_WIDTH);
			l.setZ(l.getZ() - ((radius * 1.5) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);

			
			// top
			
			l.setX(l.getX() + ((radius * 2) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() + ((radius * 2) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() + ((radius * 2) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);

			
			// top right
			
			l.setX(l.getX() + radius + ROAD_WIDTH);
			l.setZ(l.getZ() + ((radius * 1.5) + (ROAD_WIDTH * 2)));	
			waterHex(l, radius + ROAD_WIDTH);
	
			l.setX(l.getX() + radius + ROAD_WIDTH);
			l.setZ(l.getZ() + ((radius * 1.5) + (ROAD_WIDTH * 2)));			
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() + radius + ROAD_WIDTH);
			l.setZ(l.getZ() + ((radius * 1.5) + (ROAD_WIDTH * 2)));			
			waterHex(l, radius + ROAD_WIDTH);
			
			
			// bottom right
			
			l.setX(l.getX() - (radius + ROAD_WIDTH));
			l.setZ(l.getZ() + ((radius * 1.5) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() - (radius + ROAD_WIDTH));
			l.setZ(l.getZ() + ((radius * 1.5) + (ROAD_WIDTH * 2)));			
			waterHex(l, radius + ROAD_WIDTH);

			l.setX(l.getX() - (radius + ROAD_WIDTH));
			l.setZ(l.getZ() + ((radius * 1.5) + (ROAD_WIDTH * 2)));			
			waterHex(l, radius + ROAD_WIDTH);
			
			
			// bottom
			
			l.setX(l.getX() - ((radius * 2) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() - ((radius * 2) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);
			
			l.setX(l.getX() - ((radius * 2) + (ROAD_WIDTH * 2)));
			waterHex(l, radius + ROAD_WIDTH);

			
			// bottom left
			
			l.setX(l.getX() - (radius + ROAD_WIDTH));
			l.setZ(l.getZ() - ((radius * 1.5) + ROAD_WIDTH));
			waterHex(l, radius + ROAD_WIDTH);

			l.setX(l.getX() - (radius + ROAD_WIDTH));
			l.setZ(l.getZ() - ((radius * 1.5) + ROAD_WIDTH));	
			waterHex(l, radius + ROAD_WIDTH);

			System.out.println("Done generating water.");
			
			return true;
		}

		*/
		
		
		if (cmd.getName().equalsIgnoreCase("set.start")) { 
			// Just have this force a start with whoever is queued.
			
			
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return true;
			}
				
			if (queue.isEmpty()){
				sender.sendMessage("Cannot start a game with 0 players queued.");
				return true;
			}
				
			
			sender.sendMessage("Forcing a start without a full queue.");
			
			this.setGenLock(true);
			Iterator<Player> it = queue.iterator();
			
			while (it.hasNext()) {
				Player p = it.next();
				p.sendMessage("New game will begin with " + queue.size() + " players in about " + (COUNTDOWN / 20) + " seconds.");
				p.sendMessage("Standby for new world generation.");
				
				if (COUNTDOWN >= 1800)
					new TimerMessagePlayer(p,90).runTaskLater(this, COUNTDOWN - 1800);
				if (COUNTDOWN >= 1200)
					new TimerMessagePlayer(p,60).runTaskLater(this, COUNTDOWN - 1200);
				if (COUNTDOWN >= 600)
					new TimerMessagePlayer(p,30).runTaskLater(this, COUNTDOWN - 600);
				if (COUNTDOWN >= 200)
					new TimerMessagePlayer(p,10).runTaskLater(this, COUNTDOWN - 200);
				if (COUNTDOWN >= 100)
					new TimerMessagePlayer(p,5).runTaskLater(this, COUNTDOWN - 100);
			}
			
			TeamSet newTeamSet = preStartGame();
			new StartGameTask(newTeamSet).runTaskLater(this, COUNTDOWN);
			
			return true;
		}
		
		
		
		if (cmd.getName().equalsIgnoreCase("set.tolobby")) { 
			
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return false;
			}
			
			Player target; 
			
			try {
				target = (Bukkit.getServer().getPlayer(args[0]));
			}
			catch (Exception e){
				if (sender instanceof Player){
					target = (Player)sender;
				} 
				else {
					sender.sendMessage("You must specify a player.");
					return false;
				}
			}
			
			
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
				return false;
		    } 
			else {
				toLobby(target);
				target.sendMessage("You were sent to lobby by admin.");
				sender.sendMessage("Ported " + target.getDisplayName() + " to lobby.");
			}
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("set.setspawn")) { 
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return false;
			}
			Player target = (Bukkit.getServer().getPlayer(args[0]));
			
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
				return false;
		    } 
			else
			{
				World world = target.getWorld();
				
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int z = Integer.parseInt(args[3]);
				
				world.setSpawnLocation(x, y, z);
				
				target.sendMessage("Set spawn.");
				sender.sendMessage("Set spawn");
			}
			
			return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("set.toworld")) { 
			/*Player target = (Bukkit.getServer().getPlayer(args[0]));
			World world = null;
			
			Bukkit.getServer().createWorld(new WorldCreator(args[1]));
			world = Bukkit.getServer().getWorld(args[1]);
			world.setDifficulty(Difficulty.PEACEFUL);
			
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
				return false;
		    } 
			else
			{
				Location dest = world.getSpawnLocation();
				target.teleport(dest);
				target.sendMessage("You were sent to world " + world.getName() + " by admin.");
				sender.sendMessage("Ported " + target.getDisplayName() + " to world " + world.getName());
			}
			*/
			if (!sender.hasPermission("settlers.admin_commands")){
				sender.sendMessage("You don't have admin command permissions.");
				return true;
			}
			
			if (args.length < 2) {
				
				return false;
			}
			
			Player p = Bukkit.getServer().getPlayer(args[0]);
			World world = Bukkit.getServer().createWorld(new WorldCreator(args[1]));
			
			if (world.getName() == "settlers_lobby"){
				toLobby(p);
				return true;
			}
			
			Iterator<TeamSet> iterator = getSettlers().teamSets.iterator();
			TeamSet teamSet = null;
			
			while (iterator.hasNext()){
				TeamSet temp = iterator.next();
				if (temp.getWorld().getName().equals(world.getName())){
					teamSet = temp;
					break;
				}
			}
			
			if (teamSet == null){
				sender.sendMessage("No Settlers world exists by that name.");
				return false;
			}
			
			toWorld(p, teamSet);
			p.sendMessage("You were sent to world " + world.getName() + " by admin.");
					
			return true;
		}
		
		return false;
	} // end command method
	
	
	private World makeWorld(String name) {
		// Create new world for this board
		copyWorld("settlers_template", name);
		WorldCreator wc = new WorldCreator (name);
		Bukkit.getServer().createWorld(wc);
		World world = Bukkit.getServer().getWorld(name);
		world.setSpawnLocation(0, 64, 0);
		
		world.setDifficulty(Difficulty.NORMAL);
		world.setMonsterSpawnLimit(0);
		world.setAnimalSpawnLimit(0);
		world.setWaterAnimalSpawnLimit(0);
		return world;
	}
	
	
	private NormalBoard makeBoard(World world){
		Bukkit.getServer().broadcastMessage("New gameboard generation in progress.");
	
		String gameType = "normal";
		NormalBoard normalBoard;

		int radius = HEX_RADIUS;
	
					
		// Generate virtual map and numbers
		
		if (gameType.equals("cities")){
			gameType = "normal";
			System.out.println("Game type invalid; using normal game type.");
			normalBoard = new NormalBoard(true);
		}
		else if (gameType.equals("normal")){
			gameType = "normal";
			System.out.println("Using normal game type.");
			normalBoard = new NormalBoard(true);
			System.out.println("Gameboard initialized.");
		}
		else {
			gameType = "normal";
			System.out.println("Game type invalid; using normal game type.");
			normalBoard = new NormalBoard(true);
			System.out.println("Gameboard initialized.");
		}
			

		
		// set location to spawn point
		Location baseLoc = world.getSpawnLocation();
		baseLoc.setY(BASE_ELEVATION);
		Location l = baseLoc.clone();
		
		int incr = 20;
		int time = 80;
		
		// Make all the hexagons on board, with numbers
		BigHexagon hex;
		
		// *** CAUTION: This order is messed up; it's not the same as for NormalBoard!
		
		// middle 5 hexes: 7 - 11 (out of order!)
		hex = new BigHexagon(normalBoard.getTiles()[9].getType(), l, radius);
		normalBoard.getTiles()[9].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[9].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[8].getType(), l, radius);
		normalBoard.getTiles()[8].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[8].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[7].getType(), l, radius);
		normalBoard.getTiles()[7].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[7].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l = baseLoc.clone();
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[10].getType(), l, radius);
		normalBoard.getTiles()[10].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[10].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[11].getType(), l, radius);
		normalBoard.getTiles()[11].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[11].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		// top 3 hexes: 0 - 2 (backwards!)
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		l.setZ(l.getZ() - ((radius * 3) + ROAD_WIDTH * 2));
		hex = new BigHexagon(normalBoard.getTiles()[2].getType(), l, radius);
		normalBoard.getTiles()[2].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[2].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
				
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[1].getType(), l, radius);
		normalBoard.getTiles()[1].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[1].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[0].getType(), l, radius);
		normalBoard.getTiles()[0].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[0].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
				
		
		// bottom 3 hexes: 16 - 18 (forwards!)
		l.setZ(l.getZ() + ((radius * 6) + ROAD_WIDTH * 4));
		hex = new BigHexagon(normalBoard.getTiles()[16].getType(), l, radius);
		normalBoard.getTiles()[16].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[16].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[17].getType(), l, radius);
		normalBoard.getTiles()[17].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[17].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[18].getType(), l, radius);
		normalBoard.getTiles()[18].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[18].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		
		// bottom middle 4: 12 - 15 (backwards!)
		l.setX(l.getX() + radius + ROAD_WIDTH);
		l.setZ(l.getZ() - ((radius * 1.5) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[15].getType(), l, radius);
		normalBoard.getTiles()[15].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[15].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[14].getType(), l, radius);
		normalBoard.getTiles()[14].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[14].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[13].getType(), l, radius);
		normalBoard.getTiles()[13].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[13].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() - ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[12].getType(), l, radius);
		normalBoard.getTiles()[12].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[12].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		
		// top middle 4 hexes: 3 - 6
		l.setZ(l.getZ() - ((radius * 3) + ROAD_WIDTH * 2));
		hex = new BigHexagon(normalBoard.getTiles()[3].getType(), l, radius);
		normalBoard.getTiles()[3].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[3].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
			
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[4].getType(), l, radius);
		normalBoard.getTiles()[4].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[4].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[5].getType(), l, radius);
		normalBoard.getTiles()[5].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[5].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		l.setX(l.getX() + ((radius * 2) + ROAD_WIDTH));
		hex = new BigHexagon(normalBoard.getTiles()[6].getType(), l, radius);
		normalBoard.getTiles()[6].setBoardPoints(hex.getPoints());
		normalBoard.getTiles()[6].setBoardLocation(l.clone());
		new HexGenerateTask(hex, world).runTaskLater(this, time);
		time = time + incr;
		
		
		// Add diamond blocks to boardLocations of all Points
		new PlaceDiamondsTask(this, normalBoard, world).runTaskLater(this, time);
		time = time + (incr * 19);
		
		// Add emerald blocks to boardLocations of all Edges
		new PlaceEmeraldsTask(this, normalBoard, world).runTaskLater(this, time);
		time = time + 2;
		
		// Add gold blocks to boardLocations of all tiles
		new PlaceGoldTask(this, normalBoard, world).runTaskLater(this, time);
		time = time + 2;
		
		// Add numbers
		if (SKY_NUMBERS_ON)
			new PlaceNumbersTask(this, normalBoard, world).runTaskLater(this, time);
		//else
			//sender.sendMessage("Sky numbers disabled; not generating.");
		
		time = time + 2 ;
		new PlacePortsTask(normalBoard).runTaskLater(this, time);
		
		
		//if (!SKY_NUMBERS_ON)
		//	Bukkit.getServer().broadcastMessage("New gameboard generation complete, no tile numbers");
		//else
			//Bukkit.getServer().broadcastMessage("New gameboard generation complete with tile numbers.");
		
		//sender.sendMessage("Successfully generated " + name + "; a " + gameType + " board with hex radius " + radius);
		System.out.println("Successfully generated " + world.getName() + "; a " + gameType + " board with hex radius " + radius);
		//lastBoard = name;
		return normalBoard;
	}
	
	
	
	public void toWorld(Player player, TeamSet teamSet){

		World world = teamSet.getWorld();
		
		// Moved to TeamSet constructor
		// Still do this anyway because it just returns the world if already loaded?
		/*Bukkit.getServer().createWorld(new WorldCreator(world.getName()));
		world.setDifficulty(Difficulty.NORMAL);
		world.setMonsterSpawnLimit(0);
		world.setAnimalSpawnLimit(40);*/
		
		
		if (player == null) {
			System.out.println("Specified player is not online!");
	    } 
		else {
			
			Location dest = world.getSpawnLocation();
			ItemStack tunic = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lam_tunic = (LeatherArmorMeta)tunic.getItemMeta();
			ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta lam_pants = (LeatherArmorMeta)pants.getItemMeta();

			SettlersTeam team = null;
			
			if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
				dest = teamSet.getBlueSpawn();
				team = teamSet.getBlueTeam();
				lam_tunic.setColor(Color.fromRGB(70,70,240));
				lam_pants.setColor(Color.fromRGB(70,70,240));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"blue"));
			}
			else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
				dest = teamSet.getRedSpawn();
				team = teamSet.getRedTeam();
				lam_tunic.setColor(Color.fromRGB(150,1,1));
				lam_pants.setColor(Color.fromRGB(150,1,1));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"red"));
			}
			else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
				dest = teamSet.getBrownSpawn();
				team = teamSet.getBrownTeam();
				lam_tunic.setColor(Color.fromRGB(120,75,1));
				lam_pants.setColor(Color.fromRGB(120,75,1));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"brown"));
			}
			else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
				dest = teamSet.getWhiteSpawn();
				team = teamSet.getWhiteTeam();
				lam_tunic.setColor(Color.fromRGB(255,255,255));
				lam_pants.setColor(Color.fromRGB(255,255,255));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"white"));
			}
			
			
			if (player.isOnline() && team != null) {
				player.teleport(dest);
				System.out.println("Ported " + player.getDisplayName() + " to world " + world.getName() + ".");
				
				
				teamSet.getBlueTeam().getTeam().removePlayer((OfflinePlayer) player);
				teamSet.getRedTeam().getTeam().removePlayer((OfflinePlayer) player);
				teamSet.getBrownTeam().getTeam().removePlayer((OfflinePlayer) player);
				teamSet.getWhiteTeam().getTeam().removePlayer((OfflinePlayer) player);
				
				tunic.setItemMeta(lam_tunic);
				pants.setItemMeta(lam_pants);
				
				player.getEquipment().setLeggings(pants);
				player.getEquipment().setChestplate(tunic);
				
				team.getTeam().addPlayer((OfflinePlayer) player);
				player.setScoreboard(teamSet.getScoreboard());
			}
			else {
				if (!player.isOnline())
					System.out.println("Did not port " + player + " to " + world + " because they are not online.");
				if (team == null){
					System.out.println("Can't move people (yet!) to worlds if they aren't already on a team from that world.");
					// TODO: add team option to toWorld command
				}
			}
			
			
			
			
		}
	}
	
	public void toLobby(Player player) {
		World world = Bukkit.getServer().getWorld("settlers_lobby");
		Location dest = world.getSpawnLocation();
		player.teleport(dest);
		player.getInventory().clear();
		player.getEquipment().setLeggings(null);
		player.getEquipment().setChestplate(null);
		player.getScoreboard().resetScores(player);
	}
	
	public void giveMap(Player player, World w) {
		MapView map = Bukkit.createMap(w);
		map.setScale(MapView.Scale.NORMAL);
		int spawnX = (int)map.getWorld().getSpawnLocation().getX();
		int spawnZ = (int)map.getWorld().getSpawnLocation().getZ();
		map.setCenterX(spawnX);
		map.setCenterZ(spawnZ);
		
		player.sendMap(map);
		
		ItemStack newItem = new ItemStack(Material.MAP, 1, map.getId());
		player.getInventory().addItem(newItem);
	}
	
	public void enqueue(Player player){
		// If max games are in session, do not allow more to queue
		if (teamSets.size() >= MAX_WORLDS){
			player.sendMessage("You can't queue now; the server is at capacity.");
			return;
		}	
		
		if (this.isGenLocked()) {
			player.sendMessage("You can't queue now; a game is generating. Try again in a few seconds.");
			return;
		}
		
		// Add player to queue
		if (!queue.contains(player)){
			queue.add(player);
			player.sendMessage("You joined the queue for the next game ("+queue.size()+"/"+MIN_QUEUE_SIZE+").");
		}
		else {
			player.sendMessage("You are already in the queue ("+queue.size()+"/"+MIN_QUEUE_SIZE+").");
			return;
		}
		
		// If queue is full, start a new game.
		if (queue.size() >= MIN_QUEUE_SIZE) {
			this.setGenLock(true);
			Iterator<Player> it = queue.iterator();
			
			while (it.hasNext()) {
				Player p = it.next();
				p.sendMessage("New game will begin with " + queue.size() + " players in about " + (COUNTDOWN / 20) + " seconds.");
				p.sendMessage("Standby for new world generation.");
				if (COUNTDOWN >= 1800)
					new TimerMessagePlayer(p,90).runTaskLater(this, COUNTDOWN - 1800);
				if (COUNTDOWN >= 1200)
					new TimerMessagePlayer(p,60).runTaskLater(this, COUNTDOWN - 1200);
				if (COUNTDOWN >= 600)
					new TimerMessagePlayer(p,30).runTaskLater(this, COUNTDOWN - 600);
				if (COUNTDOWN >= 200)
					new TimerMessagePlayer(p,10).runTaskLater(this, COUNTDOWN - 200);
				if (COUNTDOWN >= 100)
					new TimerMessagePlayer(p,5).runTaskLater(this, COUNTDOWN - 100);
			}
			
			TeamSet newTeamSet = preStartGame();
			new StartGameTask(newTeamSet).runTaskLater(this, COUNTDOWN);
		}
	}
	
	public void dequeue(Player player){
		if (queue.contains(player)){
			queue.remove(player);
			player.sendMessage("You left the queue ("+queue.size()+"/"+MIN_QUEUE_SIZE+").");
		}
		else {
			player.sendMessage("You are not in the queue.");
		}
	}
	
	public void clearQueue(){
		queue = new HashSet<Player>();
	}
	
	// just needs queue, which is global.. so no params?
	public TeamSet preStartGame() {
		
		// Generate new world for teamSet
		// Rely on better scheduling later to reduce lag?
		int n = 1;
		File container = Bukkit.getServer().getWorldContainer();
		String worldName = "settlers" + n;
		File newWorld = new File(worldName);
		
		try {
			while (FileUtils.directoryContains(container, newWorld)){
				n++;
				worldName = "settlers" + n;
				newWorld = new File(worldName);
			}
		} catch (IOException e) {
			System.err.println("Exception checking if world exists.");
			e.printStackTrace();
		}
		
		
		
		World world = makeWorld(worldName); 
		NormalBoard nb = makeBoard(world);
				
		Location[] spawns = new Location[4];
		spawns[0] = new Location(world, BLUE_X,BLUE_Y,BLUE_Z);
		spawns[1] = new Location(world, RED_X,RED_Y,RED_Z);
		spawns[2] = new Location(world, BROWN_X,BROWN_Y,BROWN_Z);
		spawns[3] = new Location(world, WHITE_X,WHITE_Y,WHITE_Z);	
		
		
				
		// Create new TeamSet
		// team randomization happens in TeamSet constructor
		TeamSet newTeamSet = new TeamSet(queue, world, spawns, this);
		newTeamSet.setNormalBoard(nb);
		if (nb == null)
			System.out.println("NULL NORMALBOARD!!!");
		
		// Add starter chests
		newTeamSet.setBlueChest(new Location(world, BLUE_X-2,BLUE_Y,BLUE_Z-2));
		newTeamSet.setRedChest(new Location(world, RED_X-2,RED_Y,RED_Z-2));
		newTeamSet.setBrownChest(new Location(world, BROWN_X-2,BROWN_Y,BROWN_Z-2));
		newTeamSet.setWhiteChest(new Location(world, WHITE_X-2,WHITE_Y,WHITE_Z-2));
		
		world.getBlockAt(newTeamSet.getBlueChest()).setType(Material.CHEST);
		world.getBlockAt(newTeamSet.getRedChest()).setType(Material.CHEST);
		world.getBlockAt(newTeamSet.getBrownChest()).setType(Material.CHEST);
		world.getBlockAt(newTeamSet.getWhiteChest()).setType(Material.CHEST);
		
		
		teamSets.add(newTeamSet);
		clearQueue();
		return newTeamSet;
	}
	
	public void startGame(TeamSet teamSet) {
		// fill chests with starter items if teams aren't empty.
		if (!teamSet.getBlueTeam().getTeam().getPlayers().isEmpty()){
			Chest chest = (Chest) teamSet.getBlueChest().getBlock().getState();
			Inventory inv = chest.getInventory();
			ItemStack is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
		}
		
		if (!teamSet.getRedTeam().getTeam().getPlayers().isEmpty()){
			Chest chest = (Chest) teamSet.getRedChest().getBlock().getState();
			Inventory inv = chest.getInventory();
			ItemStack is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
		}
		
		if (!teamSet.getBrownTeam().getTeam().getPlayers().isEmpty()){
			Chest chest = (Chest) teamSet.getBrownChest().getBlock().getState();
			Inventory inv = chest.getInventory();
			ItemStack is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
		}
		
		if (!teamSet.getWhiteTeam().getTeam().getPlayers().isEmpty()){
			Chest chest = (Chest) teamSet.getWhiteChest().getBlock().getState();
			Inventory inv = chest.getInventory();
			ItemStack is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterSettlement);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
			is = new ItemStack(starterRoad);
			inv.addItem(is);
		}
		
		// port everyone to spawns
		World world = teamSet.getWorld();	
		Iterator<Settlers_Player> itr = teamSet.getPlayers().iterator();
		
		while (itr.hasNext()){
			Player p = itr.next().getPlayer();
			p.getInventory().clear();
			toWorld(p, teamSet);
			giveMap(p, world);
			p.sendMessage("New game started!");
			p.sendMessage("Get your team's starter items from the chest on your shipwreck!");
		}

		this.setGenLock(false);
	}
	

	public void endGame(SettlersTeam victors, TeamSet teamSet) {
		// Port all players from world to the lobby
		// Take all steps to delete TeamSet and its World
		
				
		Iterator<Settlers_Player> itr = teamSet.getPlayers().iterator();
		
		while (itr.hasNext()){
			Settlers_Player sp = itr.next();
			Player p = sp.getPlayer();
			new EndGamePlayer(p).runTaskLater(this, Settlers.ENDGAME_DELAY);
			teamSet.toTeam(sp, "none");
			p.sendMessage(ChatColor.BOLD + victors.toString() + " team wins!");
		}
		
		// Deconstruct TeamSet
		new DeconstructGameTask(teamSet).runTaskLater(this, Settlers.ENDGAME_DELAY + 60);
		
	}
	
	public boolean checkScore(SettlersTeam team, TeamSet teamSet){
		if (team.getScore() >= POINTS_TO_WIN && !teamSet.isGameOver()){
			teamSet.setGameOver(true);
			endGame(team, teamSet);
			return true;
		}
		
		return false;
	}
	
	
	public void removeAllItems(Player player){
		player.getInventory().clear();
		player.getEquipment().clear();
		player.sendMessage("Your inventory has been reset.");
	}
	
	
	/*public void toTeam(Player player, String teamName){
		World world = player.getWorld();
		Iterator<TeamSet> iterator = this.teamSets.iterator();
		TeamSet teamSet;
		SettlersTeam team;
		
		while (iterator.hasNext()){
			TeamSet temp = iterator.next();
			if (temp.getWorld().getName().equals(world.getName())){
				teamSet = temp;
				break;
			}
		}
		
		ItemStack tunic = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam_tunic = (LeatherArmorMeta)tunic.getItemMeta();
		ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta lam_pants = (LeatherArmorMeta)pants.getItemMeta();

		
		switch (teamName) {
			case "blue":
				team = teamSet.getBlueTeam();
				lam_tunic.setColor(Color.fromRGB(70,70,240));
				lam_pants.setColor(Color.fromRGB(70,70,240));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"blue"));
				break;
			case "red":
				team = teamSet.getRedTeam();
				lam_tunic.setColor(Color.fromRGB(150,1,1));
				lam_pants.setColor(Color.fromRGB(150,1,1));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"red"));
				break;
			case "brown":
				team = teamSet.getBrownTeam();
				lam_tunic.setColor(Color.fromRGB(120,75,1));
				lam_pants.setColor(Color.fromRGB(120,75,1));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"brown"));
				break;
			case "white":
				team = teamSet.getWhiteTeam();
				lam_tunic.setColor(Color.fromRGB(255,255,255));
				lam_pants.setColor(Color.fromRGB(255,255,255));
				player.setMetadata("settlers_team",  new FixedMetadataValue (this,"white"));
				break;
			case "none":
				team = null;
				teamSet.getBlueTeam().getTeam().removePlayer((OfflinePlayer) player);
				teamSet.getRedTeam().getTeam().removePlayer((OfflinePlayer) player);
				teamSet.getBrownTeam().getTeam().removePlayer((OfflinePlayer) player);
    			teamSet.getWhiteTeam().getTeam().removePlayer((OfflinePlayer) player);
    			player.getEquipment().setLeggings(null);
    			player.getEquipment().setChestplate(null);
    			player.removeMetadata("settlers_team", this);
    			break;
			default:
				System.out.println("Bad team name. <blue, red, white, or brown>");
				team = null;
		}
		
		if (player.isOnline() && team != null) {
			teamSet.getBlueTeam().getTeam().removePlayer((OfflinePlayer) player);
			teamSet.getRedTeam().getTeam().removePlayer((OfflinePlayer) player);
			teamSet.getBrownTeam().getTeam().removePlayer((OfflinePlayer) player);
			teamSet.getWhiteTeam().getTeam().removePlayer((OfflinePlayer) player);
			
			tunic.setItemMeta(lam_tunic);
			pants.setItemMeta(lam_pants);
			
			player.getEquipment().setLeggings(pants);
			player.getEquipment().setChestplate(tunic);
			
			team.getTeam().addPlayer((OfflinePlayer) player);
				
			// Display members of that team
			/*System.out.println("Team " + team.getTeam().getDisplayName() + " players: ");
			Iterator<OfflinePlayer> it = team.getTeam().getPlayers().iterator();
			
			while (it.hasNext()) {
				System.out.println(it.next().getName());
			}
		}
		
		if (player.isOnline() && team == null) {
			teamSet.getBlueTeam().getTeam().removePlayer((OfflinePlayer) player);
			teamSet.getRedTeam().getTeam().removePlayer((OfflinePlayer) player);
			teamSet.getBrownTeam().getTeam().removePlayer((OfflinePlayer) player);
			teamSet.getWhiteTeam().getTeam().removePlayer((OfflinePlayer) player);
			player.getEquipment().setLeggings(null);
			player.getEquipment().setChestplate(null);
		}
	}*/
		
	public void copyWorld(String worldName, String newName) {
		File container = Bukkit.getServer().getWorldContainer();
		File[] worlds = container.listFiles();
		File templateWorld = null;
		
		for (int i = 0; i < worlds.length; i++){
			if (worlds[i].getName().equals(worldName)) {
				templateWorld = worlds[i];
			}
		}
			
		//System.out.println("worldName=" + worldName);
		//System.out.println("newName=" + newName);
		//System.out.println("templateWorld=" + templateWorld);
		
		if (templateWorld != null) {
			File newFile = new File(templateWorld.getParent() + '/' + newName);
			//FileUtil.copy(templateWorld, newFile);
			try {
				FileUtils.copyDirectory(templateWorld, newFile);
				File dim1 = new File(templateWorld.getParent() + '/' + newName + "/DIM1");
				File uid = new File (templateWorld.getParent() + '/' + newName + "/uid.dat");
				
				if (dim1.isDirectory()){
					File region = new File(templateWorld.getParent() + '/' + newName + "/DIM1/region");
					FileUtils.moveDirectory(region, newFile);
					FileUtils.deleteDirectory(dim1);
				}
				
				uid.delete();
			} catch (IOException e) {
				System.out.println("Error copying template world.");
				e.printStackTrace();
			}
			//System.out.println("Used fileutil.copy on: " + templateWorld.getName() + " to " + newFile.getName());
			//System.out.println(templateWorld.getAbsolutePath() + " to " + newFile.getAbsolutePath());
		
		}
	} // end copyWorld
	
	public void deleteWorld(String worldName) {
		File container = Bukkit.getServer().getWorldContainer();
		File[] worlds = container.listFiles();
		File deadWorld = null;
		
		for (int i = 0; i < worlds.length; i++){
			if (worlds[i].getName().equals(worldName)) {
				deadWorld = worlds[i];
			}
		}
		
		if (deadWorld != null) {
			try {
				FileUtils.deleteDirectory(deadWorld);
				//deadWorld.delete();
				
				if (FileUtils.directoryContains(container, deadWorld)){
					System.out.println("Old world " + worldName + " still exists.");
				} 
				else {
					System.out.println("Old world " + worldName + " deleted successfully");
				}
				
				
			} catch (IOException e) {
				System.out.println("Error deleting the old world.");
				e.printStackTrace();
			}
			//System.out.println("Used fileutil.copy on: " + templateWorld.getName() + " to " + newFile.getName());
			//System.out.println(templateWorld.getAbsolutePath() + " to " + newFile.getAbsolutePath());
		
		}
	} // end copyWorld
	
	
	public void deployCity(Player player, int pointId, TeamSet teamSet){
		Point point = teamSet.getNormalBoard().getPointById(pointId);
		Location l = point.getBoardPoint().clone();
		World w = l.getWorld();
		Location sky_l = l.clone();
		sky_l.setY(sky_l.getY() + NUMBER_HEIGHT + 2);
		Material skyMaterial = Material.STONE;
		
		ChatColor col = ChatColor.YELLOW;
		String teamName = "unknown";
		SettlersTeam tempTeam = null;
		int colour = 15; // black wool
		
		point.setStructure(2);
		
		Objective objective = teamSet.getObjective();
		Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "BLUE"));
		
		if (point.getOwner() == 1){
			colour = LIGHT_BLUE_WOOL;
			teamName = "BLUE";
			//skyMaterial = Material.ICE;
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "BLUE"));
			tempTeam = teamSet.getBlueTeam();
			col = ChatColor.BLUE;
		}
		else if (point.getOwner() == 2){
			colour = RED_WOOL;
			teamName = "RED";
			//skyMaterial = Material.LAVA;
			tempTeam = teamSet.getRedTeam();
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "RED"));
			col = ChatColor.RED;
		}
		else if (point.getOwner() == 3){
			colour = BROWN_WOOL;
			teamName = "BROWN";
			//skyMaterial = Material.WOOD;
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "BROWN"));
			tempTeam = teamSet.getBrownTeam();
			col = ChatColor.GOLD;
		}
		else if (point.getOwner() == 4){
			colour = WHITE_WOOL;
			teamName = "WHITE";
			//skyMaterial = Material.SNOW_BLOCK;
			tempTeam = teamSet.getWhiteTeam();
			score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.WHITE + "WHITE"));
			col = ChatColor.WHITE;
		}
		else {
			
			System.out.println("City deployed from null team in " + teamSet.getWorld().getName() + " world.");
		}
		
		if (tempTeam == null)
			return;
		
		tempTeam.setScore(tempTeam.getScore() + 1);
		tempTeam.setCitiesBuilt(tempTeam.getCitiesBuilt() + 1);
		tempTeam.setSettlementsBuilt(tempTeam.getSettlementsBuilt() - 1);
		
		score.setScore(tempTeam.getScore());
		
		skyCity(sky_l, skyMaterial, CITY_DIMS);
		upgradeHut(l,colour);
		worldBroadcast(w, col + player.getDisplayName() + " upgraded a " + teamName + " settlement to a city!");
		
		l = point.getBoardPoint().clone(); 
		l.setX(l.getX() + 1);
		l.setZ(l.getZ() - 3);
		Block b = w.getBlockAt(l);
		w.playEffect(l, Effect.ENDER_SIGNAL, 0);
		//b.setType(Material.CHEST);
		Chest chest = (Chest) b.getState();
		Inventory inv = chest.getInventory();
		ItemStack is = new ItemStack(Material.TORCH, 3);
		inv.addItem(is);
		is = new ItemStack(Material.WOOD_SWORD, 1);
		inv.addItem(is);
		
		ArrayList<Tile> tiles = point.getTiles();
		
		for (int i = 0; i < tiles.size(); i++){
			if (tiles.get(i).getType().equals("field")) {
				is = new ItemStack(Material.IRON_HOE, 1);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("mountain")) {
				is = new ItemStack(Material.IRON_PICKAXE, 1);
				inv.addItem(is);
				is = new ItemStack(Material.LADDER, 3);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("forest")) {
				is = new ItemStack(Material.IRON_AXE, 1);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("pasture")) {
				is = new ItemStack(ironShears);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("quarry")) {
				is = new ItemStack(Material.IRON_SPADE, 1);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("desert")) {
				is = new ItemStack(Material.IRON_SWORD, 1);
				inv.addItem(is);
			}
		}
		
		checkScore(tempTeam, teamSet);
		
	}
	
	
	public void deploySettlement(Player player, int pointId, TeamSet teamSet){
		Point point = teamSet.getNormalBoard().getPointById(pointId);
		Location l = point.getBoardPoint().clone();
		World w = l.getWorld();
		Location sky_l = l.clone();
		sky_l.setY(sky_l.getY() + NUMBER_HEIGHT);
		Material skyMaterial = Material.STONE;
		ChatColor c = ChatColor.YELLOW;
		
		String team = "unknown";
		int colour = 15; // black wool
		
		if (teamSet.getBlueTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
			colour = LIGHT_BLUE_WOOL;
			team = "BLUE";
			skyMaterial = Material.ICE;
			c = ChatColor.BLUE;
		}
		else if (teamSet.getRedTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
			colour = RED_WOOL;
			team = "RED";
			skyMaterial = Material.TNT;
			c = ChatColor.RED;
		}
		else if (teamSet.getBrownTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
			colour = BROWN_WOOL;
			team = "BROWN";
			skyMaterial = Material.WOOD;
			c = ChatColor.GOLD;
		}
		else if (teamSet.getWhiteTeam().getTeam().getPlayers().contains((OfflinePlayer)player)){
			colour = WHITE_WOOL;
			team = "WHITE";
			skyMaterial = Material.SNOW_BLOCK;
			c = ChatColor.WHITE;
		}
		
		skySettlement(sky_l, skyMaterial, SETTLEMENT_DIMS);
		
		// Remove port signs and item frames if this is Point is on a port.
		if (point.getPort() > 0){
			
			// Get all entities in chunk and remove if they are item frames.
			// Does this take the items with it?
			Entity[] nearEntities = point.getBoardPoint().getChunk().getEntities();
			for (int i = 0; i < nearEntities.length; i++) {
				if (nearEntities[i].getType().equals(EntityType.ITEM_FRAME)){
					nearEntities[i].remove();
				}
			}
			
			// Remove all signs
			Location signL = point.getBoardPoint().clone();
			signL.setY(signL.getY() + 2);
			Block b = w.getBlockAt(signL);
			b.getRelative(BlockFace.NORTH).setType(Material.AIR);
			b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
			b.getRelative(BlockFace.EAST).setType(Material.AIR);
			b.getRelative(BlockFace.WEST).setType(Material.AIR);
			
		}
		
		
		l.setX(l.getX() - SETTLEMENT_DIMS / 2);
		l.setZ(l.getZ() - SETTLEMENT_DIMS / 2);
		
		for (int i = 0; i < SETTLEMENT_DIMS; i++){
			for (int j = 0; j < SETTLEMENT_DIMS; j++){
				l.setY(l.getY() + (SETTLEMENT_DIMS / 2) - 1);
				
				for (int k = 0; k < SETTLEMENT_DIMS / 2; k++){
					w.getBlockAt(l).setType(Material.AIR);
					l.setY(l.getY() - 1);
				}
				w.getBlockAt(l).setType(Material.WOOL);				
				
				//w.getBlockAt(l).setType(Material.CARPET);
				w.getBlockAt(l).setData((byte)colour);
				l.setX(l.getX() + 1);
				l.setY(l.getY() + 1);
			}
			
			l.setX(l.getX() - SETTLEMENT_DIMS);
			l.setZ(l.getZ() + 1);
		}
		
		l = point.getBoardPoint().clone();
		l.setX(l.getX() - SETTLEMENT_DIMS / 2);
		l.setZ(l.getZ() - SETTLEMENT_DIMS / 2);
		
		l.setX(l.getX() + 1);
		l.setZ(l.getZ() + 1);	
		deployLamp(l, colour);
		
		l.setX(l.getX() + (SETTLEMENT_DIMS - 3));	
		deployLamp(l, colour);
		
		l.setZ(l.getZ() + (SETTLEMENT_DIMS - 3));		
		deployLamp(l, colour);
		
		l.setX(l.getX() - (SETTLEMENT_DIMS - 3));	
		deployLamp(l, colour);

		deployHut(point.getBoardPoint(), colour);
		
		l = point.getBoardPoint().clone(); 
		w.getBlockAt(l).setType(Material.WORKBENCH);
		w.getBlockAt(l).setMetadata("id", new FixedMetadataValue(this, pointId));
		
		l.setX(l.getX() + 1);
		l.setZ(l.getZ() - 3);
		Block b = w.getBlockAt(l);
		w.playEffect(l, Effect.ENDER_SIGNAL, 0);
		b.setType(Material.CHEST);
		Chest chest = (Chest) b.getState();
		Inventory inv = chest.getInventory();
		ItemStack is = new ItemStack(Material.TORCH, 5);
		inv.addItem(is);
		
		ArrayList<Tile> tiles = point.getTiles();
		
		for (int i = 0; i < tiles.size(); i++){
			if (tiles.get(i).getType().equals("field")) {
				is = new ItemStack(Material.WOOD_HOE, 1);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("mountain")) {
				is = new ItemStack(Material.WOOD_PICKAXE, 1);
				inv.addItem(is);
				is = new ItemStack(Material.LADDER, 3);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("forest")) {
				is = new ItemStack(Material.WOOD_AXE, 1);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("pasture")) {
				is = new ItemStack(woodShears);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("quarry")) {
				is = new ItemStack(Material.WOOD_SPADE, 1);
				inv.addItem(is);
			}
			else if (tiles.get(i).getType().equals("desert")) {
				is = new ItemStack(Material.WOOD_SWORD, 1);
				inv.addItem(is);
			}
		}
		
		
		worldBroadcast(w, c + player.getDisplayName() + " has founded a settlement for team " + team + "!");
	}
	
	public void skySettlement(Location l, Material m, int dims){
		
		if (m.equals(Material.STATIONARY_LAVA) || m.equals(Material.WATER)){
			skySettlement(l, Material.GLASS, dims + 2);
			skySettlement(new Location(l.getWorld(), l.getX(), l.getY() -1, l.getZ()), Material.GLASS, dims + 2);
		}
		
		l = l.clone();
		World w = l.getWorld();
		
		l.setX(l.getX() - dims / 2);
		l.setZ(l.getZ() - dims / 2);
		
		for (int i = 0; i < dims; i++){
			for (int j = 0; j < dims; j++){
				w.getBlockAt(l).setType(m);
				l.setX(l.getX() + 1);
			}
			
			l.setX(l.getX() - dims);
			l.setZ(l.getZ() + 1);
		}
	}
	
	public void skyCity(Location l, Material m, int dims){
		
		skySettlement(l,m,dims);
	}
	
	public void skySkull(Location l, Material m){
		
		l = l.clone();
		l.setY(l.getY() + NUMBER_HEIGHT + 2);
		//World w = l.getWorld();
		int dims = SKULL_DIMS;
		
		l.setX(l.getX() - (dims / 2));
		l.setZ(l.getZ() - (dims / 2));
		int c = 0;
		
		for (int i = 0; i < dims; i++){
			for (int j = 0; j < dims; j++){
				//l.getBlock().setType(m); 
				
				if (j % 3 != 0) {
					new OneBlockTask(l, m).runTaskLater(this, c);
				}
				else {	
					c = c + 2;
				}
				l.setX(l.getX() + 1);
			}
			
			l.setX(l.getX() - dims);
			l.setZ(l.getZ() + 1);
		}
	}
	
	
	public void skyRoad(Edge edge, Material mat, int extraX, int extraY) {
		double x1, x2, z1, z2;
		double x, m, b;
		
		if (mat.equals(Material.STATIONARY_LAVA) || mat.equals(Material.WATER)){
			skyRoad(edge, Material.GLASS, 1, 1);
			//skyRoad(edge, Material.GLASS, 1, 0);
		}
		
		Location l1 = edge.getPoints()[0].getBoardPoint();
		Location l2 = edge.getPoints()[1].getBoardPoint();
		World w = l1.getWorld();
		int y = (int) (l1.getY() + NUMBER_HEIGHT - (edge.getOwner() * 3) - extraY);
		
		x1 = l1.getX();
		x2 = l2.getX();
		z1 = l1.getZ();
		z2 = l2.getZ();
		
		if (x2 - x1 == 0){
			x1 = x1 + 0.02;
		}
		
		// Yummy maths
		m = (z2 - z1) / (x2 - x1);
		b = z1 - (m * x1);
		
		int Z = Math.min((int) z1, (int) z2);
		int endZ = Math.max((int) z1, (int) z2);
		int c = 0;
		
		for (int z = Z; z < endZ; z++){					
			x = (z - b) / m;

			for (int k = -4 - extraX; k < 2 + extraX; k++) {
			  for (int j = -2 - extraX; j < 4 + extraX; j++) {				   
				//Block block = w.getBlockAt(new Location (w, x + k, y, ((int) z) +j));
				//block.setType(mat);
				  
				  if (w.getBlockAt(new Location (w, x + k, y, ((int) z) +j)).isEmpty()) {
					  new OneBlockTask(new Location (w, x + k, y, ((int) z) +j), mat).runTaskLater(this, c);
					//  if (j % 3 == 0){
						  c++;
					
					 // }
				  }
			  }
			}
			
		}
	}
	
	public void deployLamp(Location location, int colour){
		Location l = location.clone();
		World w = l.getWorld();
		
		w.getBlockAt(l).setType(Material.FENCE);
		l.setY(l.getY() + 1);
		w.getBlockAt(l).setType(Material.FENCE);
		l.setY(l.getY() + 1);
		w.getBlockAt(l).setType(Material.WOOL);
		w.getBlockAt(l).setData((byte)colour);
		l.setY(l.getY() + 1);
		w.getBlockAt(l).setType(Material.TORCH);
	}
	
	
	public void deployHut(Location location, int colour){
		Location l = location.clone();
		World w = l.getWorld();	
		
		final int WID = 6;
		final int LEN = 8;
		final int H = 4;
		
		l.setX(l.getX() - WID / 2);
		l.setZ(l.getZ() - LEN / 2);
		
		for (int i = 0; i < WID; i++){
			for (int j = 0; j < LEN; j++){
			
			  if (j == 0 || j == LEN - 1 || i == 0 || i == WID - 1){
				for (int k = 0; k < H; k++){
					if (
							(j == 0 && i == 0) ||
							(j == 0 && i == WID - 1) ||
							(j == LEN-1 && i == 0) ||
							(j == LEN-1 && i == WID - 1) ) {
						w.getBlockAt(l).setType(Material.WOOL);
						w.getBlockAt(l).setData((byte) colour);

					} else
						w.getBlockAt(l).setType(Material.WOOD);
					
					l.setY(l.getY() + 1);	
				}
				
				l.setY(l.getY() - H);
			  }
			  
			  l.setZ(l.getZ() + 1);
			}
			l.setZ(l.getZ() - LEN);
			
			
			l.setX(l.getX() + 1);
		}
		
		l.setX(l.getX() - ((WID / 2) + 2));
		l.setY(l.getY() + H);
		
		for (int i = 0; i < WID - 2; i++){
			for (int j = 0; j < LEN; j++){
				w.getBlockAt(l).setType(Material.WOOL);
				w.getBlockAt(l).setData((byte) colour);
				l.setZ(l.getZ() + 1);
			}
			l.setZ(l.getZ() - LEN);	
			l.setX(l.getX() + 1);
		}
		
		
		l.setX(l.getX() - (WID/2));
		l.setY(l.getY() + 1);
		
		for (int i = 0; i < WID - 4; i++){
			for (int j = 0; j < LEN; j++){
				w.getBlockAt(l).setType(Material.WOOL);
				w.getBlockAt(l).setData((byte) colour);
				l.setZ(l.getZ() + 1);
			}
			l.setZ(l.getZ() - LEN);	
			l.setX(l.getX() + 1);
		}
		
		//Byte data1 = (0x8); 
       // Byte data2 = (0x4);
		
		byte data1 = (byte)9;
		byte data2 = (byte)4;

        l = location.clone();
		
		l.setX(l.getX() - WID / 2);
		l.setY(l.getY() - 1);
		w.getBlockAt(l).setType(Material.WOOL);
		w.getBlockAt(l).setData((byte) colour);
		l.setY(l.getY() + 2);	
		w.getBlockAt(l).setType(Material.AIR);
		w.getBlockAt(l).setTypeIdAndData(71, data1, true);
		l.setY(l.getY() - 1);
		w.getBlockAt(l).setTypeIdAndData(71, data2, true);
		w.getBlockAt(l).setData((byte) (w.getBlockAt(l).getData() ^ 0x4));
		l.setY(l.getY() + 2);
		l.setX(l.getX() + 1);
		w.getBlockAt(l).setType(Material.TORCH);
		l.setX(l.getX() - 2);
		w.getBlockAt(l).setType(Material.TORCH);
	}
	
	
	public void upgradeHut(Location location, int colour){
		Location l = location.clone();
		World w = l.getWorld();	
		
		final int WID = 6;
		final int LEN = 8;
		final int H = 4;
		
		l.setX(l.getX() - WID / 2);
		l.setZ(l.getZ() - LEN / 2);
		
		for (int i = 0; i < WID; i++){
			for (int j = 0; j < LEN; j++){
			
			  if (j == 0 || j == LEN - 1 || i == 0 || i == WID - 1){
				for (int k = 0; k < H; k++){
					if (w.getBlockAt(l).getType().equals(Material.WOOD))
						w.getBlockAt(l).setType(Material.SMOOTH_BRICK);
					
					l.setY(l.getY() + 1);	
				}
				
				l.setY(l.getY() - H);
			  }
			  
			  l.setZ(l.getZ() + 1);
			}
			l.setZ(l.getZ() - LEN);
			
			
			l.setX(l.getX() + 1);
		}
		
		l.setX(l.getX() - ((WID / 2) + 2));
		l.setY(l.getY() + H);
	}
	
	
	public void deployRoad(int colour, Edge edge) {
		double x1, x2, z1, z2;
		double x, m, b;
		
		Location l1 = edge.getPoints()[0].getBoardPoint();
		Location l2 = edge.getPoints()[1].getBoardPoint();
		World w = l1.getWorld();
		int y = (int) (l1.getY() - 1);
		
		x1 = l1.getX();
		x2 = l2.getX();
		z1 = l1.getZ();
		z2 = l2.getZ();
		
		if (x2 - x1 == 0){
			x1 = x1 + 0.02;
		}
		
		// Yummy maths
		m = (z2 - z1) / (x2 - x1);
		b = z1 - (m * x1);
		
		int Z = Math.min((int) z1, (int) z2);
		int endZ = Math.max((int) z1, (int) z2);

		for (int z = Z; z < endZ; z++){					
			x = (z - b) / m;

			
			for (int k = -3; k < 1; k++) {
			  for (int j = -1; j < 3; j++) {
				   
				for (int i = 5; i > 0; i--) {
					Material mat = w.getBlockAt(new Location (w, x + k, y + i, ((int) z) + j)).getType();
					if ( 	!mat.equals(Material.DIAMOND_BLOCK) && 
							!mat.equals(Material.WOOD) &&
							!mat.equals(Material.CARPET) &&
							!mat.equals(Material.WOOL) &&
							!mat.equals(Material.WORKBENCH) &&
							!mat.equals(Material.CHEST) &&
							!mat.equals(Material.FENCE) &&
							!mat.equals(Material.TORCH) &&
							!mat.equals(Material.SMOOTH_BRICK) &&
							!mat.equals(Material.IRON_DOOR) &&
							!mat.equals(Material.IRON_DOOR_BLOCK) &&
							!mat.equals(Material.NETHER_BRICK) &&
							!mat.equals(Material.WALL_SIGN))
						// Do not let roads erase the above materials when being built
						
						w.getBlockAt(new Location (w, x + k, y + i, ((int) z) + j)).setType(Material.AIR);
				}
				
				
				Block block = w.getBlockAt(new Location (w, x + k, y, ((int) z) +j));
				block.setType(Material.WOOL);
				block.setData((byte) colour); 
			  }
			}
			
		}
		
		Material material = Material.STONE;
		switch (colour){
		case LIGHT_BLUE_WOOL:
			material = Material.ICE;
			break;
		case RED_WOOL:
			material = Material.TNT;
			break;
		case BROWN_WOOL:
			material = Material.WOOD;
			break;
		case WHITE_WOOL:
			material = Material.SNOW_BLOCK;
			break;
		}
		
		skyRoad(edge, material, 0, 0);
	}
	
	
	public void treeFall(Block b){
		World w = b.getWorld();
		Location l = b.getLocation().clone();
		Location l2 = b.getLocation().clone();
		int count = 0;
		
		l.setY(l.getY() + 1);
		l2.setY(l2.getY() + 1);
		l2.setX(l2.getX() + 1);
		
		//new Location (w, l.getX(), l.getY()+1, l.getZ())
		
		while ( w.getBlockAt(l).getType().equals(Material.LOG) && 
				(int) w.getBlockAt(l).getData() < 4 ){
			w.getBlockAt(l).setType(Material.AIR);
			w.getBlockAt(l2).setType(Material.AIR);
			w.getBlockAt(new Location (w, l2.getX(), l2.getY()+1, l2.getZ())).setType(Material.AIR);
			w.getBlockAt(new Location (w, l2.getX(), l2.getY()+2, l2.getZ())).setType(Material.AIR);
			
			w.spawnFallingBlock(l2, Material.LOG, (byte) 4);
			
			l.setY(l.getY() + 1);
			l2.setY(l2.getY() + 1);
			l2.setX(l2.getX() + 1);		
			count++;
		}
		
		
		// Make leaves and branches fall
		int leafRadius = 8;
		l.setX(l.getX() - leafRadius / 2 + count);
		l.setZ(l.getZ() - leafRadius / 2);
		l.setY(l.getY() - leafRadius / 2);
		
		for (int i = 0; i < leafRadius; i++){
			for (int j = 0; j < leafRadius; j++){
				for (int k = 0; k < leafRadius; k++){
					if (w.getBlockAt(l).getType().equals(Material.LEAVES) || w.getBlockAt(l).getType().equals(Material.LOG)){
						Material m = w.getBlockAt(l).getType();
						w.getBlockAt(l).setType(Material.AIR);
						w.spawnFallingBlock(l, m, (byte) 0);
					}
					l.setY(l.getY() + 1);	
				}
				
				l.setY(l.getY() - leafRadius);
				l.setZ(l.getZ() + 1);
			}
			
			l.setZ(l.getZ() - leafRadius);
			l.setX(l.getX() + 1);
		}
		
	}
	
	
	// Send a message to all players on a world.
	public static void worldBroadcast(World world, String message){
		Iterator<Player> it = world.getPlayers().iterator();
		
		while (it.hasNext()){
			it.next().sendMessage(message);
		}
	}
	
	/*
	public void waterHex (Location l, int r){
		World world = l.getWorld();
		BigHexagon hex = new BigHexagon("air", l, r + ROAD_WIDTH);
		hex.generate(world);
		
		l.setY(l.getY() - 1);
		hex = new BigHexagon("sand", l, r + ROAD_WIDTH);
		hex.generate(world);
		
		hex = new BigHexagon("ocean", l, r + (ROAD_WIDTH / 2));
		hex.generate(world);
		
		l.setY(l.getY() - 1);
		hex = new BigHexagon("ocean", l, r + (ROAD_WIDTH / 2));
		hex.generate(world);
		
		l.setY(l.getY() - 1);
		hex = new BigHexagon("sand", l, r + (ROAD_WIDTH / 2));
		hex.generate(world);
		
		l.setY(l.getY() + 3);
	}*/
	
	//public class LongestPath {
		public double initLongestPath(ArrayList<Point> points,Point source, int owner){
		    for(Point p:points){
		        p.setVisited(false);
		    }
		    return getLongestPath(source, owner);
		}
		
		public double getLongestPath(Point p, int owner){
		    double dist,max=0;
		    p.setVisited(true);
		    for(Edge e:p.getEdges()){
		        if(!e.otherPoint(p).getVisited() && e.getOwner() == owner){
		            dist=1+getLongestPath(e.otherPoint(p), owner);
		            if(dist>max)
		                max=dist;
		        }
		    }

		    p.setVisited(false);    
		    return max;
		}
//	}
	
	
} // End Settlers
