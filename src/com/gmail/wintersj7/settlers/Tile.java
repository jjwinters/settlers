package com.gmail.wintersj7.settlers;

import org.bukkit.Location;

public class Tile {

	/*  Edge Indexing
	
	 		^
	 	2/	   \1		
	 			
	  3 |		| 0
	  
	   4 \	   / 5
	  	    V
	  
	 */
	
	
	
	/*	Point Indexing - just like BigHexagon
	 * 
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
	 * 					5
	 * 					*
	 */
	
	private int resource_value;
	private String type;
	private Edge[] edges;
	private Point[] points;
	private static int id_counter;
	private int id;
	private Location boardLocation;
	private boolean blocked;
	
	// Types: field, mountain, pasture, quarry, forest, desert
	
	/*public Tile(int resource_value, String type, Edge[] edges, Point[] points) {
		try {
			Tile.id_counter++;
		}
		catch (Exception e){
			Tile.id_counter = 1;
		}
		this.resource_value = resource_value;
		this.type = type;
		this.edges = edges;
		this.points = points;
		this.id = id_counter;
	}*/
	
	public Tile(Edge[] edges, Point[] points) {
		try {
			Tile.id_counter++;
		}
		catch (Exception e){
			Tile.id_counter = 1;
		}
		this.resource_value = 0;
		this.type = "unset!";
		this.edges = edges;
		this.points = points;
		this.id = id_counter;
		this.setBlocked(false);
		
		for (int i = 0; i < points.length; i++){
			points[i].getTiles().add(this);
		}
	}
	
	// Two are equal if they have the same ID
	public boolean equals (Tile other){
		if (this.getId() == other.getId())
			return true;
		else
			return false;
	}

	public void setBoardPoints(Location[] boardPoints) {
		
		// Corner (Point) boardPoints
		Point[] tempPoints = getPoints();
		for (int i = 0; i < boardPoints.length; i++) {
			//System.out.println("tempPoints ID=" + tempPoints[i].getId());
			//System.out.println("boardPoint X=" + boardPoints[i].getX());
			
			tempPoints[i].setBoardPoint(boardPoints[i].clone());
			//tempPoints[i].getBoardPoint().setY(tempPoints[i].getBoardPoint().getY() + 1);
		}
		
		int offset = Settlers.ROAD_WIDTH / 2;
		
		tempPoints[0].getBoardPoint().setX(tempPoints[0].getBoardPoint().getX() + offset);
		tempPoints[0].getBoardPoint().setZ(tempPoints[0].getBoardPoint().getZ() + offset);
		tempPoints[1].getBoardPoint().setX(tempPoints[1].getBoardPoint().getX() + offset);
		tempPoints[1].getBoardPoint().setZ(tempPoints[1].getBoardPoint().getZ() - offset);
		tempPoints[2].getBoardPoint().setZ(tempPoints[2].getBoardPoint().getZ() - offset);
		tempPoints[2].getBoardPoint().setX(tempPoints[2].getBoardPoint().getX() - offset);
		tempPoints[3].getBoardPoint().setX(tempPoints[3].getBoardPoint().getX() - offset);
		tempPoints[3].getBoardPoint().setZ(tempPoints[3].getBoardPoint().getZ() - offset);
		tempPoints[4].getBoardPoint().setX(tempPoints[4].getBoardPoint().getX() - offset);
		tempPoints[4].getBoardPoint().setZ(tempPoints[4].getBoardPoint().getZ() + offset);
		tempPoints[5].getBoardPoint().setZ(tempPoints[5].getBoardPoint().getZ() + offset);
		tempPoints[5].getBoardPoint().setX(tempPoints[5].getBoardPoint().getX() - offset);
		
		
		// Edge boardPoints
		
		for (int i = 0; i < edges.length; i++){
			double x1, x2, x3;
			double z1, z2, z3;
			Location l1 = edges[i].getPoints()[0].getBoardPoint();
			Location l2 = edges[i].getPoints()[1].getBoardPoint();
			
			x1 = l1.getX();
			x2 = l2.getX();
			z1 = l1.getZ();
			z2 = l2.getZ();
			
			x3 = (x1 + x2) / 2;
			z3 = (z1 + z2) / 2;
			
			edges[i].setBoardPoint(new Location (l1.getWorld(), x3, l1.getY(), z3));
		}
		
	}
	
	public int getResource_value() {
		return resource_value;
	}
	
	public void setResource_value(int resource_value) {
		this.resource_value = resource_value;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public Edge[] getEdges() {
		return edges;
	}

	public Point[] getPoints() {
		return points;
	}

	public int getId() {
		return id;
	}

	public Location getBoardLocation() {
		return boardLocation;
	}

	public void setBoardLocation(Location boardLocation) {
		this.boardLocation = boardLocation;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
}
