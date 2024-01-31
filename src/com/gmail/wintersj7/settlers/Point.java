package com.gmail.wintersj7.settlers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class Point {

	/*
	 * No need to have add/remove methods for edges and points because these will not change once 
	 * set in a game. Only their owner and structure values will change.
	 */
	
	// owner 0 = unoccupied
	// 1 - blue
	// 2 - red
	// 3 - brown
	// 4 - white
	private int owner;
	
	// structure 0 = unoccupied
	// 1 = settlement
	// 2 = city
	// 3 = metropolis?
	private int structure;
	
	// port 0 = none
	// 1 = wood
	// 2 = wheat
	// 3 = wool
	// 4 = brick
	// 5 = ore
	// 6 = 3:1
	private int port;
	
	private boolean buildable;
	
	private ArrayList<Edge> edges;
	private ArrayList<Tile> tiles;
	private int id;
	private Location boardPoint;
	
	private static int id_counter;
	
	private boolean visited;
	
	/*public Point(Edge[] edges, ArrayList<Tile> tiles) {
		try {
			Point.id_counter++;
		}
		catch (Exception e){
			Point.id_counter = 1;
		}
		
		this.setBuildable(true);
		this.setOwner(0);
		this.setStructure(0);
		this.tiles = tiles;
		this.edges = edges;
		this.id = id_counter;
	}*/
	
	public Point() {
		try {
			Point.id_counter++;
		}
		catch (Exception e){
			Point.id_counter = 1;
		}
		
		this.setBuildable(true);
		this.setOwner(0);
		this.setStructure(0);
		this.id = id_counter;
		this.tiles = new ArrayList<Tile>();
		this.edges = new ArrayList<Edge>();
		this.setVisited(false);
	}

	// Two are equal if they have the same ID
	public boolean equals (Object other){
		if (other.getClass() != this.getClass())
			return false;
		else if (this.getId() == ((Point) other).getId())
			return true;
		else
			return false;
	}
	
	public Chest getChest(){
		if (this.getOwner() == 0) {
			System.err.println("getChest can't be used for a point that is unbuilt");
			return null;
		}
		
		Location l = this.getBoardPoint().clone(); 
		l.setX(l.getX() + 1);
		l.setZ(l.getZ() - 3);
		Block b = l.getBlock();
		Chest chest = (Chest) b.getState();
		return chest;
	}
	
	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getStructure() {
		return structure;
	}

	public void setStructure(int structure) {
		this.structure = structure;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public int getId() {
		return id;
	}

	public int getPort() {
		return port;
	}

	public boolean isBuildable() {
		return buildable;
	}

	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}
	
	public Location getBoardPoint() {
		return boardPoint;
	}

	public void setBoardPoint(Location boardPoint) {
		this.boardPoint = boardPoint;
	}

	public boolean getVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public void setPort(int port){
		this.port = port;
	}
}
