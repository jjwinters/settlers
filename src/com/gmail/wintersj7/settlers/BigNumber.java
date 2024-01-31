package com.gmail.wintersj7.settlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BigNumber {
	
	/*
	 * 			r1
	 * 		  _________
	 * 
	 * 		|			|
	 *    c1|		  c2|
	 *      |	        |
	 *           r2
	 *        _________
	 *      
	 *      |			|
	 *    c3|		  c4|
	 *      |			|
	 * 			 r3
	 * 		  _________
	 */
	
	
	private int[] rows;
	private int[] cols;
	
	private int width;
	private int height;
	private int thickness;
	
	private int value;
	
	public BigNumber (int value, int thickness) {
		switch(value){
		case 1:
			this.setRows(new int[]{});
			this.setCols(new int[]{2,4});
			break;
		case 2:
			this.setRows(new int[]{1,2,3});
			this.setCols(new int[]{2,3});
			break;
		case 3:
			this.setRows(new int[]{1,2,3});
			this.setCols(new int[]{2,4});
			break;
		case 4:
			this.setRows(new int[]{2});
			this.setCols(new int[]{1,2,4});
			break;
		case 5:
			this.setRows(new int[]{1,2,3});
			this.setCols(new int[]{1,4});
			break;
		case 6:
			this.setRows(new int[]{1,2,3});
			this.setCols(new int[]{1,3,4});
			break;
		case 7:
			this.setRows(new int[]{1});
			this.setCols(new int[]{2,4});
			break;
		case 8:
			this.setRows(new int[]{1,2,3});
			this.setCols(new int[]{1,2,3,4});
			break;
		case 9:
			this.setRows(new int[]{1,2,3});
			this.setCols(new int[]{1,2,4});
			break;

		case -1:
			this.setRows(new int[]{});
			this.setCols(new int[]{});
			
		default:
			value = 0;
			this.setRows(new int[]{1,3});
			this.setCols(new int[]{1,2,3,4});
		
		}
		
		if (thickness < 1 || thickness > 50) {
			this.setThickness(6);
		}
		this.setThickness(thickness);
		this.setWidth(thickness * 3);
		this.setHeight(thickness * 5);
		this.setValue(value);
		
	}
	
	class OneRowTask extends BukkitRunnable {
	    private Location location;
	    private Material newMaterial;
	    private int width;
	    
	    public OneRowTask(Location l, Material m, int width) {
	    	this.location = l.clone();
	    	this.newMaterial = m;
	    	this.width = width;
	    }

		@Override
		public void run() {
			for (int i = 0; i < width; i++){
				location.getBlock().setType(newMaterial);
				location.setX(location.getX() + 1);
			}
		}
	}
	
	class OneColumnTask extends BukkitRunnable {
	    private Location location;
	    private Material newMaterial;
	    private int height;
	    
	    public OneColumnTask(Location l, Material m, int height) {
	    	this.location = l.clone();
	    	this.newMaterial = m;
	    	this.height = height;
	    }

		@Override
		public void run() {
			for (int i = 0; i < height; i++){
				location.getBlock().setType(newMaterial);
				location.setZ(location.getZ() + 1);
			}
		}
	}
	
	public void place_grounded (World w, Location loc, Material m){
		//location.setY(location.getWorld().getHighestBlockYAt((int)location.getX(), (int)location.getZ()));		
		
			int lBound = (int)loc.getX() - (this.width / 2);
			int rBound = (int)loc.getX() + (this.width / 2);
			int upBound = (int)loc.getZ() - (this.height / 2);
			int downBound = (int)loc.getZ() + (this.height / 2);
			
			for (int r = 0; r < this.rows.length; r++){
				
				if (rows[r] == 1) {
					for (int minirow = upBound; minirow < upBound + this.thickness; minirow++){
						for (int i = 0; i < this.width; i++){
							
							if (w.getHighestBlockAt(lBound + i, minirow).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(lBound + i, minirow);
								b.setType(Material.AIR); // get rid of CROPS
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT); // get rid of SOIL
								b.setType(m); // change to m
							}
							else
								w.getHighestBlockAt(lBound + i, minirow).setType(m);
						}
					} // end thickrow
				} // end row 1 selector
				
				if (rows[r] == 2) {
					for (int minirow = upBound + (this.thickness * 2); minirow < upBound + (this.thickness * 3); minirow++){
						for (int i = 0; i < this.width; i++){
							
							if (w.getHighestBlockAt(lBound + i, minirow).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(lBound + i, minirow);
								b.setType(Material.AIR);
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
								b.setType(m);
							}
							else
								w.getHighestBlockAt(lBound + i, minirow).setType(m);
						}
					} // end thickrow
				} // end row 1 selector
				
				
				if (rows[r] == 3) {
					for (int minirow = downBound + 3; minirow > downBound - this.thickness; minirow--){
						for (int i = 0; i < this.width; i++){
							
							if (w.getHighestBlockAt(lBound + i, minirow).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(lBound + i, minirow);
								b.setType(Material.AIR);
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
								b.setType(m);	
							} 
							else
								w.getHighestBlockAt(lBound + i, minirow).setType(m);
						}
					} // end thickrow
				} // end row 1 selector
			} // end row placer
			
			
			for (int c = 0; c < this.cols.length; c++) {
				
				if (cols[c] == 1) {
					for (int minicol = lBound; minicol < lBound + this.thickness; minicol++){
						for (int i = 0; i < this.height / 2; i++){
							
							if (w.getHighestBlockAt(minicol, upBound + i).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(minicol, upBound + i);
								b.setType(Material.AIR);
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
								b.setType(m);	
							}
							else
								w.getHighestBlockAt(minicol, upBound + i).setType(m);
						}
					} // end thickrow
				} // end row 1 selector
				
				if (cols[c] == 2) {
					for (int minicol = rBound - this.thickness; minicol < rBound; minicol++){
						for (int i = 0; i < this.height / 2; i++){
							
							if (w.getHighestBlockAt(minicol, upBound + i).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(minicol, upBound + i);
								b.setType(Material.AIR);
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
								b.setType(m);	
							}
							else
								w.getHighestBlockAt(minicol, upBound + i).setType(m);
						}
					} // end thickrow
				} // end row 1 selector
				
				
				if (cols[c] == 3) {
					for (int minicol = lBound; minicol < lBound + this.thickness; minicol++){
						for (int i = 0; i < this.height / 2; i++){
							if (w.getHighestBlockAt(minicol, upBound + i + (this.height / 2)).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(minicol, upBound + i + (this.height / 2));
								b.setType(Material.AIR);
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
								b.setType(m);	
							}
							else
								w.getHighestBlockAt(minicol, upBound + i + (this.height / 2)).setType(m);
						}
					} // end thickrow
					
					
				} // end row 1 selector
				
				
				if (cols[c] == 4) {
					for (int minicol = rBound - this.thickness; minicol < rBound; minicol++){
						for (int i = 0; i < this.height / 2; i++){
							if (w.getHighestBlockAt(minicol, upBound + i + (this.height / 2)).getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)){
								Block b = w.getHighestBlockAt(minicol, upBound + i + (this.height / 2));
								b.setType(Material.AIR);
								b.getRelative(BlockFace.DOWN).setType(Material.DIRT);
								b.setType(m);	
							}
							else
								w.getHighestBlockAt(minicol, upBound + i + (this.height / 2)).setType(m);
							/*Block tempBlock = w.getHighestBlockAt(minicol, upBound + i);
							if (tempBlock.getType().equals(m)){
								tempBlock.setType(m);
							}*/

						}
						
					} // end thickrow
				} // end row 1 selector
				
			}
			
	}
	
	
	public void place(World w, Location loc, Material m) {
		
		int lBound = (int)loc.getX() - (this.width / 2);
		int rBound = (int)loc.getX() + (this.width / 2);
		int upBound = (int)loc.getZ() - (this.height / 2);
		int downBound = (int)loc.getZ() + (this.height / 2);
		int time = 0;
		int incr = 1;
		Plugin settlers = Bukkit.getPluginManager().getPlugin("Settlers");
		
		for (int r = 0; r < this.rows.length; r++){
			
			if (rows[r] == 1) {
				for (int minirow = upBound; minirow < upBound + this.thickness; minirow++){
					//for (int i = 0; i < this.width; i++){
					if (minirow % 3 != 0){
						//w.getBlockAt(lBound + i, (int) loc.getY(), minirow).setType(m);
						new OneRowTask(new Location (loc.getWorld(),lBound, (int) loc.getY(), minirow), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
					//} //end minirow
				} // end thickrow
			} // end row 1 selector
			
			if (rows[r] == 2) {
				for (int minirow = upBound + (this.thickness * 2); minirow < upBound + (this.thickness * 3); minirow++){
					//for (int i = 0; i < this.width; i++){
					//w.getBlockAt(lBound + i, (int) loc.getY(), minirow).setType(m);
					if (minirow % 3 != 0){
						new OneRowTask(new Location (loc.getWorld(),lBound, (int) loc.getY(), minirow), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
				//} //end minirow
				} // end thickrow
			} // end row 1 selector
			
			
			if (rows[r] == 3) {
				for (int minirow = downBound + 3; minirow > downBound - this.thickness; minirow--){
					//for (int i = 0; i < this.width; i++){
					//w.getBlockAt(lBound + i, (int) loc.getY(), minirow).setType(m);
					if (minirow % 3 != 0){
						new OneRowTask(new Location (loc.getWorld(),lBound, (int) loc.getY(), minirow), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
				//} //end minirow
				} // end thickrow
			} // end row 1 selector
		} // end row placer
		
		
		for (int c = 0; c < this.cols.length; c++) {
			
			if (cols[c] == 1) {
				for (int minicol = lBound; minicol < lBound + this.thickness; minicol++){
					//for (int i = 0; i < this.height / 2; i++){
					//	w.getBlockAt(minicol, (int) loc.getY(), upBound + i).setType(m);
					if (minicol % 3 != 0){
						new OneColumnTask(new Location (loc.getWorld(),minicol, (int) loc.getY(), upBound), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
					//} //end minirow
				} // end thickrow
			} // end row 1 selector
			
			if (cols[c] == 2) {
				for (int minicol = rBound - this.thickness; minicol < rBound; minicol++){
					//for (int i = 0; i < this.height / 2; i++){
					//	w.getBlockAt(minicol, (int) loc.getY(), upBound + i).setType(m);
					if (minicol % 3 != 0){
						new OneColumnTask(new Location (loc.getWorld(),minicol, (int) loc.getY(), upBound), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
					//} //end minirow
				} // end thickrow
			} // end row 1 selector
			
			
			if (cols[c] == 3) {
				for (int minicol = lBound; minicol < lBound + this.thickness; minicol++){
					//for (int i = 0; i < this.height / 2; i++){
					//	w.getBlockAt(minicol, (int) loc.getY(), upBound + i).setType(m);
					if (minicol % 3 != 0){
						new OneColumnTask(new Location (loc.getWorld(),minicol, (int) loc.getY(), upBound + (this.height / 2)), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
					//} //end minirow
				} // end thickrow
			} // end row 1 selector
			
			
			if (cols[c] == 4) {
				for (int minicol = rBound - this.thickness; minicol < rBound; minicol++){
					//for (int i = 0; i < this.height / 2; i++){
					//	w.getBlockAt(minicol, (int) loc.getY(), upBound + i).setType(m);
					if (minicol % 3 != 0){
						new OneColumnTask(new Location (loc.getWorld(),minicol, (int) loc.getY(), upBound + (this.height / 2)), m, this.width).runTaskLater(settlers, time);
						time = time + incr;
					}
					//} //end minirow
				} // end thickrow
			} // end row 1 selector
			
		}
		
		
		
		
	}
	
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int[] getCols() {
		return cols;
	}

	public void setCols(int[] cols) {
		this.cols = cols;
	}

	public int[] getRows() {
		return rows;
	}

	public void setRows(int[] rows) {
		this.rows = rows;
	}


	public int getThickness() {
		return thickness;
	}


	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	
	
	
	
}
