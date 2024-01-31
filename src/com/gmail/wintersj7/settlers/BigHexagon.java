package com.gmail.wintersj7.settlers;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.material.Crops;

public class BigHexagon {
	
	/*	POINTS	
	 * 					2				
	 * 					*
	 * 
	 * 		3						1
	 * 		*						*
	 * 
	 * 
	 * 
	 * 		4						0
	 * 		*						*
	 * 	
	 * 
	 * 					5
	 * 					*
	 * 
	 */
	
	
	
	private String type;
	private Location location;
	private World world;
	private int radius;
	
	private Location[] points;
	
	public BigHexagon(String type, Location location, int radius) {
	//	System.out.println("newHex type= " + type);
		int x = (int)location.getX();
		int z = (int)location.getZ();
		int y = (int)location.getY();
		
		this.world = location.getWorld();
		this.points = new Location[6];
		
		for (int i = 0; i < points.length; i++){
			points[i] = new Location(world,x,y,z);
		}
		
		points[0].setY(y + 1);
		points[0].setX(x + radius);
		points[0].setZ(z + (radius/2));
		
		points[1].setY(y + 1);
		points[1].setX(x + radius);
		points[1].setZ(z - (radius/2));
		
		points[2].setY(y + 1);
		points[2].setX(x);
		points[2].setZ(z - radius);
		
		points[3].setY(y + 1);
		points[3].setX(x - radius);
		points[3].setZ(z - (radius/2));
		
		points[4].setY(y + 1);
		points[4].setX(x - radius);
		points[4].setZ(z + (radius/2));
		
		points[5].setY(y + 1);
		points[5].setX(x);
		points[5].setZ(z + radius);
		
	//this.points = points.clone();
		this.type = type;
		this.location = location.clone();
		this.radius = radius; 
		
	}

	public void generate(World w) {
		Material baseMaterial, offMaterial, rareMaterial;
		int offFreq = 30;
		int rareFreq = 2;
		int swellFrequency = 3; 
		int swellHeight = 1;
		int swellArea = 7;
		int sinkDepth = 1;
		int sinkArea = 7;
		int sinkFrequency = 3;
		
		if (this.getType().equals("sand")) {
				baseMaterial = Material.SAND;
				offMaterial = Material.SAND;
				rareMaterial = Material.SAND;
				offFreq = 0;
				rareFreq = 0;
				swellFrequency = 0; 
				sinkFrequency = 0;
				sinkDepth = 0;
				sinkArea = 0;
		}
		else if (this.getType().equals("air")){
				baseMaterial = Material.AIR;
				offMaterial = Material.AIR;
				rareMaterial = Material.AIR;
				offFreq = 0;
				rareFreq = 0;
				swellFrequency = 0; 
				sinkFrequency = 0;
				sinkDepth = 0;
				sinkArea = 0;
		}
		else if (this.getType().equals("forest")){
				baseMaterial = Material.GRASS;
				offMaterial = Material.STONE;
				rareMaterial = Material.DIRT;
				offFreq = 10;
				rareFreq = 0;
				swellFrequency = 3; 
				sinkFrequency = 2;
				sinkDepth = 1;
				sinkArea = 8;
		}
		else if (this.getType().equals("mountain")){
				baseMaterial = Material.STONE;
				offMaterial = Material.COBBLESTONE;
				rareMaterial = Material.MOSSY_COBBLESTONE;
				offFreq = 12;
				rareFreq = 10;
				swellHeight = 5;
				swellFrequency = 3;
				swellArea = 9;
				sinkFrequency = 25;
				sinkDepth = 1;
				sinkArea = 3;
				hill(this.getLocation(), Material.STONE, radius - (radius / 3));
		}
		else if (this.getType().equals("quarry")){
				baseMaterial = Material.DIRT;
				offMaterial = Material.NETHERRACK;
				rareMaterial = Material.GRASS;
				offFreq = 50;
				rareFreq = 5;
				swellFrequency = 5;
				swellArea = 7;
				sinkFrequency = 5;
				sinkDepth = 1;
				sinkArea = 7;
				basin(this.getLocation(), Material.DIRT, radius - (radius / 2));
		}
		else if (this.getType().equals("field")){
				baseMaterial = Material.SOIL;
				offMaterial = Material.DIRT;
				rareMaterial = Material.MOSSY_COBBLESTONE;
				offFreq = 3;
				rareFreq = 2;
				swellFrequency = 1;
				swellArea = 5;
				sinkFrequency = 0;
				sinkDepth = 1;
				sinkArea = 3;
		}
		else if (this.getType().equals("pasture")){
				baseMaterial = Material.GRASS;
				offMaterial = Material.MOSSY_COBBLESTONE;
				rareMaterial = Material.DIRT;
				offFreq = 6;
				rareFreq = 1;
				swellFrequency = 1;
				swellArea = 7;
				sinkFrequency = 1;
				sinkDepth = 1;
				sinkArea = 7;
		}
		else if (this.getType().equals("desert")){
				baseMaterial = Material.SAND;
				offMaterial = Material.SANDSTONE;
				rareMaterial = Material.SAND;
				offFreq = 6;
				rareFreq = 8;
				swellFrequency = 1;
				swellArea = 7;
				sinkFrequency = 1;
				sinkDepth = 1;
				sinkArea = 12;
		}
		else if (this.getType().equals("ocean")){
				baseMaterial = Material.WATER;
				offMaterial = Material.WATER;
				rareMaterial = Material.WATER;
				offFreq = 0;
				rareFreq = 0;
				swellFrequency = 0;
				swellArea = 0;
				sinkFrequency = 0;
				sinkDepth = 0;
				sinkArea = 0;
		}	
		
		else {
				baseMaterial = Material.DIRT;
				offMaterial = Material.STONE;
				rareMaterial = Material.DIRT;
				offFreq = 0;
				rareFreq = 0;
		}
		
		
		//for (int i = 0; i < 6; i++){
		//	world.getBlockAt(this.points[i]).setType(Material.DIAMOND_BLOCK);
		//}
		
		int trim = (swellArea / 2);
		
		// Fill in square minus trim on left and right
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX() + trim; x < (int)points[1].getX() - trim; x++){
				Material m = baseMaterial;
				
				int matRoll = (int) (Math.random() * 100.0);
				if (matRoll < rareFreq) {
					m = rareMaterial;
				}
				else if (matRoll < offFreq + rareFreq){
					m = offMaterial;
				}

				if (this.getType().equals("quarry"))
					world.getHighestBlockAt(x, z).setType(m);
				else
					world.getBlockAt(x, (int)this.getLocation().getY(), z).setType(m);
				
				
				//world.getBlockAt(x, (int)this.getLocation().getY(), z).setBiome(biome);
	
				
				int swellRoll = (int) (Math.random() * 100.0);
				if (swellRoll < swellFrequency){
					landSwell(new Location(world, x, (int)this.getLocation().getY(), z), m, swellHeight, swellArea);
					//System.out.println ("Did a landSwell. roll=" + swellRoll + "height="+swellHeight + "area="+swellArea);	
				}
				
				swellRoll = (int) (Math.random() * 100.0);
				if (swellRoll < sinkFrequency){
					landSink(new Location(world, x, (int)this.getLocation().getY(), z), m, sinkDepth, sinkArea);
					//System.out.println ("Did a landSink. roll=" + swellRoll);
				}
				
				
				// go deeper with Y coordinate
			}
		}
		
		
		// Fill in left trimmed side
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[3].getX() + trim; x++){
				Material m = baseMaterial;
				
				int matRoll = (int) (Math.random() * 100.0);
				if (matRoll < rareFreq) {
					m = rareMaterial;
				}
				else if (matRoll < offFreq + rareFreq){
					m = offMaterial;
				}
				
				world.getBlockAt(x, (int)this.getLocation().getY(), z).setType(m);
				
				int swellRoll = (int) (Math.random() * 100.0);
				if (swellRoll < swellFrequency)
					landSwell(new Location(world, x, (int)this.getLocation().getY(), z), m, (swellHeight / 2) + 1, (swellArea / 2) + 1);
				else if (swellRoll < swellFrequency + sinkFrequency)
					landSink(new Location(world, x, (int)this.getLocation().getY(), z), m, (sinkDepth / 2) + 1, (sinkArea / 2) + 1);
				
			}
		}
		
		
		// Fill in right trimmed side
		for (int z = (int)points[1].getZ(); z <= (int)points[0].getZ(); z++) {
			for (int x = (int)points[1].getX() - trim; x < (int)points[1].getX(); x++){
				Material m = baseMaterial;
				
				int matRoll = (int) (Math.random() * 100.0);
				if (matRoll < rareFreq) {
					m = rareMaterial;
				}
				else if (matRoll < offFreq + rareFreq){
					m = offMaterial;
				}
				
				world.getBlockAt(x, (int)this.getLocation().getY(), z).setType(m);
				
				int swellRoll = (int) (Math.random() * 100.0);
				if (swellRoll < swellFrequency)
					landSwell(new Location(world, x, (int)this.getLocation().getY(), z), m, (swellHeight / 2) + 1, (swellArea / 2) + 1);
				else if (swellRoll < swellFrequency + sinkFrequency)
					landSink(new Location(world, x, (int)this.getLocation().getY(), z), m, (sinkDepth / 2) + 1, (sinkArea / 2) + 1);
				
			}
		}
		
		
		// Fill in top point
		int shrink = 0;
		for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
			for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
				Material m = baseMaterial;
				
				int matRoll = (int) (Math.random() * 100.0);
				if (matRoll < rareFreq) {
					m = rareMaterial;
				}
				else if (matRoll < offFreq + rareFreq){
					m = offMaterial;
				}
				
				world.getBlockAt(x, (int)this.getLocation().getY(), z).setType(m);
				
				int swellRoll = (int) (Math.random() * 100.0);
				if (swellRoll < swellFrequency)
					landSwell(new Location(world, x, (int)this.getLocation().getY(), z), m, (swellHeight / 2) + 1, (swellArea / 2) + 1);
				else if (swellRoll < swellFrequency + sinkFrequency)
					landSink(new Location(world, x, (int)this.getLocation().getY(), z), m, (sinkDepth / 2) + 1, (sinkArea / 2) + 1);
			}
			shrink = shrink + 2;
		}
		
		
		// Fill in bottom point
		shrink = 0;
		for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
			for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
				Material m = baseMaterial;
				
				int matRoll = (int) (Math.random() * 100.0);
				if (matRoll < rareFreq) {
					m = rareMaterial;
				}
				else if (matRoll < offFreq + rareFreq){
					m = offMaterial;
				}
				
				world.getBlockAt(x, (int)this.getLocation().getY(), z).setType(m);
				int swellRoll = (int) (Math.random() * 100.0);
				if (swellRoll < swellFrequency)
					landSwell(new Location(world, x, (int)this.getLocation().getY(), z), m, (swellHeight / 2) + 1, (swellArea / 2) + 1);
				else if (swellRoll < swellFrequency + sinkFrequency)
					landSink(new Location(world, x, (int)this.getLocation().getY(), z), m, (sinkDepth / 2) + 1, (sinkArea / 2) + 1);
			}
			shrink = shrink + 2;
		}
		
		
		switch (this.getType()) {
		case "forest":
			this.placeTrees(w, this.getLocation(), 120);
			break;
		case "mountain":
			//this.placeSnow(w, this.getLocation(), 100);
			this.placeOre(w, this.getLocation(), 100);
			break;
		case "quarry":
			this.placeClay(w, this.getLocation(), 3);
			this.placeTrees(w, this.getLocation(), 1);
			break;
		case "field":
			this.placePumpkins(w, this.getLocation(), 1);
			this.placeTrees(w, this.getLocation(), 15);
			this.placeWheat(w, this.getLocation(), 70);
			break;
		case "pasture":
			this.placeTrees(w, this.getLocation(), 1);
			this.placeSheep(w, this.getLocation(), 1);
			break;
		case "desert":
			this.placeCacti(w, this.getLocation(), 1);
			break;
	
		default:

	}
	
				
	} // end generate method
	
	
	public void landSwell(Location l, Material m, int swell, int hRange){
		//System.out.println ("marker1");
		World world = l.getWorld();
		int zLength = (int) (Math.random() * hRange);
		int xLength = (int) (Math.random() * hRange);
		
		if (zLength < 1)
			zLength = 1;
		
		if (xLength < 1)
			xLength = 1;
		
		int elevation = (int)l.getY();
		
		//System.out.println ("marker2");
		for (int x = (int)l.getX() - xLength; x < (int)l.getX() + xLength; x++) {
			for (int z = (int)l.getZ() - zLength; z < (int)l.getZ() + zLength; z++) {
				elevation = (int)l.getY();
				
				//System.out.println (world.getBlockAt(x, elevation, z).getType().toString());
				
				while (!world.getBlockAt(x, elevation, z).getType().equals(Material.AIR)){
					//System.out.println ("Raising elevation by 1. elevation=" + elevation);
					elevation = elevation + 1;
				}
				
				while (world.getBlockAt(x, elevation - 1, z).getType().equals(Material.AIR)){
					//System.out.println ("Lowering elevation by 1. elevation=" + elevation);
					elevation = elevation - 1;
				}
				
				for (int y = elevation; y < (int) (elevation + swell); y++) {
					world.getBlockAt(x, y, z).setType(m);
				}
			}
		}
	} // end landswell
	
	public void landSink(Location l, Material m, int swell, int hRange){
		World world = l.getWorld();
		int zLength = (int) (Math.random() * hRange);
		int xLength = (int) (Math.random() * hRange);
		
		if (zLength < 1)
			zLength = 1;
		
		if (xLength < 1)
			xLength = 1;
		
		int elevation = (int)l.getY();
		
		for (int x = (int)l.getX() - xLength; x < (int)l.getX() + xLength; x++) {
			for (int z = (int)l.getZ() - zLength; z < (int)l.getZ() + zLength; z++) {
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				while (world.getBlockAt(x, elevation, z).getType().equals(Material.AIR)){
					elevation = elevation - 1;
				}
				
				for (int y = elevation; y > (int) (elevation - swell); y--) {
					world.getBlockAt(x, y + 1, z).setType(Material.AIR);
					world.getBlockAt(x, y, z).setType(Material.AIR);
					world.getBlockAt(x, y - 1, z).setType(m);
					
					if (world.getBlockAt(x, y - 2, z).getType().equals(Material.AIR))
						world.getBlockAt(x, y - 2, z).setType(m);
				}
			}
		}
	} // end landSink
	
	
	public void hill(Location l, Material m, int r){
		//System.out.println ("marker1");
		World world = l.getWorld();
		int elevation = (int)l.getY();
		
		//System.out.println ("marker2");
		int count = 0;
		
		for (int y = elevation; y < elevation + r - 2; y++) {
		  for (int x = (int)l.getX() - r + count; x < (int)l.getX() + r - count; x++) {
			for (int z = (int)l.getZ() - r + count; z < (int)l.getZ() + r - count; z++) {

				world.getBlockAt(x, y, z).setType(m);

			} //end z
		  } // end x
		  
		  count++;
		} //end y  
	} // end hill
	
	
	public void basin(Location l, Material m, int r){
		//System.out.println ("marker1");
		World world = l.getWorld();
		int elevation = (int)l.getY();
		
		//System.out.println ("marker2");
		int count = 0;
		
		for (int y = elevation; y > elevation - r + (r / 5); y--) {
		  for (int x = (int)l.getX() - r + count; x < (int)l.getX() + r - count; x++) {
			for (int z = (int)l.getZ() - r + count; z < (int)l.getZ() + r - count; z++) {

				world.getBlockAt(x, y, z).setType(Material.AIR);
				world.getBlockAt(x, y - 1, z).setType(m);

			} //end z
		  } // end x
		  count++;
		} //end y  
	} // end basin
	
	
	public void placeTrees(World world, Location l, int freq){
		int elevation = (int)l.getY();

		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
						
				int doodadRoll = (int) (Math.random() * 1000.0);
				if (doodadRoll < (freq/4)){
					//System.out.println("marker4 - ele=" + elevation + " x=" + x + " z=" + z);
					world.generateTree(new Location(world, x, elevation + 1, z), TreeType.BIG_TREE);	
				}
				else if (doodadRoll < freq) {
					world.generateTree(new Location(world, x, elevation + 1, z), TreeType.TALL_REDWOOD);
					// apply doodad
				}
			}
		}
				
		// Fill in top point
		int shrink = 0;
		for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
			for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				int doodadRoll = (int) (Math.random() * 1000.0);
				if (doodadRoll < (freq/4)){
					//System.out.println("marker4 - ele=" + elevation + " x=" + x + " z=" + z);
					world.generateTree(new Location(world, x, elevation + 1, z), TreeType.TALL_REDWOOD);	
				}
				else if (doodadRoll < freq) {
					world.generateTree(new Location(world, x, elevation + 1, z), TreeType.BIRCH);
					// apply doodad
				}
			}
			shrink = shrink + 2;
		}
				
				
		// Fill in bottom point
		shrink = 0;
		for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
			for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
						
				int doodadRoll = (int) (Math.random() * 1000.0);
				if (doodadRoll < (freq/4)){
					//System.out.println("marker4 - ele=" + elevation + " x=" + x + " z=" + z);
					world.generateTree(new Location(world, x, elevation + 1, z), TreeType.TALL_REDWOOD);	
				}
				else if (doodadRoll < freq) {
					world.generateTree(new Location(world, x, elevation + 1, z), TreeType.REDWOOD);
					// apply doodad
				}
			}
			shrink = shrink + 2;
		}
		
	} // end placeTrees
	
	
	public void placeWheat(World world, Location l, int freq){
		int elevation = (int)l.getY();

		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
				elevation = (int)l.getY();
								
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				if (world.getBlockAt(new Location(world,x,elevation,z)).getType().equals(Material.SOIL)) {	
					int doodadRoll = (int) (Math.random() * 100.0);
					if (doodadRoll < freq) {
						Block b = world.getBlockAt(new Location(world,x,elevation + 1,z));
						b.setType(Material.CROPS);
						if (b.getType().equals(Material.CROPS)){
							Crops c = new Crops(CropState.RIPE);
							BlockState bs = b.getState();
							bs.setData(c);
							bs.update();

						}
						// apply doodad
					}
				}
				//else
				//	System.out.println(world.getHighestBlockAt(x,z).getType().toString());
			}
		}
				
		// Fill in top point
		int shrink = 0;
		for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
			for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				if (world.getBlockAt(new Location(world,x,elevation,z)).getType().equals(Material.SOIL)) {	
					int doodadRoll = (int) (Math.random() * 100.0);
					if (doodadRoll < freq) {
						Block b = world.getBlockAt(new Location(world,x,elevation + 1,z));
						b.setType(Material.CROPS);
						if (b.getType().equals(Material.CROPS)){
							Crops c = new Crops(CropState.RIPE);
							BlockState bs = b.getState();
							bs.setData(c);
							bs.update();

						}
					}
				}
			}
			shrink = shrink + 2;
		}
				
				
		// Fill in bottom point
		shrink = 0;
		for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
			for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
						
				if (world.getBlockAt(new Location(world,x,elevation,z)).getType().equals(Material.SOIL)) {	
					int doodadRoll = (int) (Math.random() * 100.0);
					if (doodadRoll < freq) {
						Block b = world.getBlockAt(new Location(world,x,elevation + 1,z));
						b.setType(Material.CROPS);
						if (b.getType().equals(Material.CROPS)){
							Crops c = new Crops(CropState.RIPE);
							BlockState bs = b.getState();
							bs.setData(c);
							bs.update();

						}
					}
				}
			}
			shrink = shrink + 2;
		}
		
		
		
	} // end placeWheat
	
	
	
	public void placeCacti(World world, Location l, int freq){
		int elevation = (int)l.getY();

		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				if (world.getBlockAt(new Location(world,x,elevation,z)).getType().equals(Material.SAND)) {	
					int doodadRoll = (int) (Math.random() * 100.0);
					if (doodadRoll < freq) {
						world.getBlockAt(new Location(world,x,elevation+1,z)).setType(Material.CACTUS);
						world.getBlockAt(new Location(world,x,elevation+2,z)).setType(Material.CACTUS);
						// apply doodad
					}
				}
			}
		}
		
		
		
		// Fill in top point
				int shrink = 0;
				for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
					for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
						elevation = (int)l.getY();
						
						while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
							elevation = elevation + 1;
						}
						
						
						if (world.getBlockAt(new Location(world,x,elevation,z)).getType().equals(Material.SAND)) {	
						int doodadRoll = (int) (Math.random() * 100.0);
						if (doodadRoll < (freq)){
							world.getBlockAt(new Location(world,x,elevation+1,z)).setType(Material.CACTUS);
							world.getBlockAt(new Location(world,x,elevation+2,z)).setType(Material.CACTUS);
						}
						}
					}
					shrink = shrink + 2;
				}
						
						
				// Fill in bottom point
				shrink = 0;
				for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
					for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
						elevation = (int)l.getY();
						
						while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
							elevation = elevation + 1;
						}
								
						if (world.getBlockAt(new Location(world,x,elevation,z)).getType().equals(Material.SAND)) {	
							int doodadRoll = (int) (Math.random() * 100.0);
							if (doodadRoll < (freq)){
								world.getBlockAt(new Location(world,x,elevation+1,z)).setType(Material.CACTUS);
								world.getBlockAt(new Location(world,x,elevation+2,z)).setType(Material.CACTUS);
							}
						}
					}
					shrink = shrink + 2;
				}
		
		
	} // end placeCacti
	
	
	public void placeSnow(World world, Location l, int freq){
		int elevation = (int)l.getY();

		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				if (elevation > 85) {	
					int doodadRoll = (int) (Math.random() * 100.0);
					if (doodadRoll < freq) {
						world.getBlockAt(new Location(world,x,elevation,z)).setType(Material.SNOW);
						// apply doodad
					}
				}
			}
		}
		
		// Fill in top point
				int shrink = 0;
				for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
					for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
						elevation = (int)l.getY();
						
						while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
							elevation = elevation + 1;
						}
						
						if (elevation > 85) {	
							int doodadRoll = (int) (Math.random() * 100.0);
							if (doodadRoll < freq) {
								world.getBlockAt(new Location(world,x,elevation,z)).setType(Material.SNOW);
								// apply doodad
							}
						}
					}
					shrink = shrink + 2;
				}
						
						
				// Fill in bottom point
				shrink = 0;
				for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
					for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
						elevation = (int)l.getY();
						
						while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
							elevation = elevation + 1;
						}
								
						if (elevation > 85) {	
							int doodadRoll = (int) (Math.random() * 100.0);
							if (doodadRoll < freq) {
								world.getBlockAt(new Location(world,x,elevation,z)).setType(Material.SNOW);
								// apply doodad
							}
						}
					}
					shrink = shrink + 2;
				}
		
		
	} // end placeSnow
	
	
	public void placeSheep(World world, Location l, int freq){
		int elevation = (int)l.getY();

		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				
				int doodadRoll = (int) (Math.random() * 120.0);
				if (doodadRoll < freq) {
					world.spawnEntity(new Location(world,x,elevation + 1,z), EntityType.SHEEP);
					// apply doodad
				}
				
			}
		}
	} // end placeCacti
	
	
	
	public void placePumpkins(World world, Location l, int freq){
		int elevation = (int)l.getY();

		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
						
				int doodadRoll = (int) (Math.random() * 100.0);
				if (doodadRoll < freq){
					world.getBlockAt(new Location(world, x, elevation + 1, z)).setType(Material.PUMPKIN);
				}
			}
		}
				
		// Fill in top point
		int shrink = 0;
		for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
			for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
				
				int doodadRoll = (int) (Math.random() * 100.0);
				if (doodadRoll < freq){
					world.getBlockAt(new Location(world, x, elevation + 1, z)).setType(Material.PUMPKIN);
				}
			}
			shrink = shrink + 2;
		}
				
				
		// Fill in bottom point
		shrink = 0;
		for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
			for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
				elevation = (int)l.getY();
				
				while (!world.getBlockAt(x, elevation + 1, z).getType().equals(Material.AIR)){
					elevation = elevation + 1;
				}
						
				int doodadRoll = (int) (Math.random() * 100.0);
				if (doodadRoll < freq){
					world.getBlockAt(new Location(world, x, elevation + 1, z)).setType(Material.PUMPKIN);
				}
			}
			shrink = shrink + 2;
		}
	} // end placePumpkins
	
	
	public void placeOre(World world, Location l, int freq){
		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x= x + 1){
				int topY = world.getHighestBlockYAt(x, z);
				int doodadRoll = (int) (Math.random() * topY);
				if (doodadRoll < l.getY())
					doodadRoll = topY - doodadRoll;
				
				if (doodadRoll > 71)	
					world.getBlockAt(new Location(world, x, doodadRoll, z)).setType(Material.IRON_ORE);
			}
		}
				
		// Fill in top point
		int shrink = 0;
		for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
			for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x = x + 3){
				
				int topY = world.getHighestBlockYAt(x, z);
				int doodadRoll = (int) (Math.random() * topY);
				if (doodadRoll < l.getY())
					doodadRoll = topY - doodadRoll;
				
				if (doodadRoll > 71)
					world.getBlockAt(new Location(world, x, doodadRoll, z)).setType(Material.IRON_ORE);
				
			}
			shrink = shrink + 2;
		}
				
				
		// Fill in bottom point
		shrink = 0;
		for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
			for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x = x + 3){
				int topY = world.getHighestBlockYAt(x, z);
				int doodadRoll = (int) (Math.random() * topY);
				if (doodadRoll < l.getY())
					doodadRoll = topY - doodadRoll;
					
				if (doodadRoll > 71)
					world.getBlockAt(new Location(world, x, doodadRoll, z)).setType(Material.IRON_ORE);
			}
			shrink = shrink + 2;
		}
	} // end placeOre
	
	
	public void placeClay(World world, Location l, int freq){
		// Fill in square
		for (int z = (int)points[3].getZ(); z <= (int)points[4].getZ(); z++) {
			for (int x = (int)points[3].getX(); x < (int)points[1].getX(); x++){
	
				int doodadRoll = (int) (Math.random() * 100.0);
				if (doodadRoll < freq){
					Block b = world.getHighestBlockAt(x, z);
					world.getBlockAt(new Location (world, x, b.getY() - 1, z)).setType(Material.CLAY);
				}
			}
		}
				
		// Fill in top point
		int shrink = 0;
		for (int z = (int)points[3].getZ(); z > (int)points[2].getZ(); z--) {
			for (int x = (int)points[3].getX() + shrink; x < (int)points[1].getX() - shrink; x++){
				int doodadRoll = (int) (Math.random() * 100.0);
				if (doodadRoll < freq){
					Block b = world.getHighestBlockAt(x, z);
					world.getBlockAt(new Location (world, x, b.getY() - 1, z)).setType(Material.CLAY);
				}
			}
			shrink = shrink + 2;
		}
				
				
		// Fill in bottom point
		shrink = 0;
		for (int z = (int)points[4].getZ(); z < (int)points[5].getZ(); z++) {
			for (int x = (int)points[4].getX() + shrink; x < (int)points[0].getX() - shrink; x++){
				int doodadRoll = (int) (Math.random() * 100.0);
				if (doodadRoll < freq){
					Block b = world.getHighestBlockAt(x, z);
					world.getBlockAt(new Location (world, x, b.getY() - 1, z)).setType(Material.CLAY);
				}
			}
			shrink = shrink + 2;
		}
	} // end placeClay
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Location[] getPoints() {
		return points;
	}

	public void setPoints(Location[] points) {
		this.points = points;
	}
	
	
}
