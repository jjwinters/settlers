package com.gmail.wintersj7.settlers;

import java.util.ArrayList;

import org.bukkit.Location;

public class Edge {

	// owner 0 = no road
	// 1 - blue
	// 2 - red
	// 3 - brown
	// 4 - white
	private int owner;
	private static int id_counter;
	private int id;
	private Point[] points;
	private Location boardPoint;
	
	
	public Edge (Point[] points) {
		try {
			Edge.id_counter++;
		}
		catch (Exception e){
			Edge.id_counter = 1;
		}

		this.setOwner(0);
		
		this.points = points.clone();
		this.id = id_counter;
		
		for (int i = 0; i < points.length; i++){
			points[i].getEdges().add(this);
		}
	}
	
	// Two are equal if they have the same ID
	public boolean equals (Edge other){
		if (this.getId() == other.getId())
			return true;
		else
			return false;
	}
	
	
	public Point sharedPoint(Edge other){
	
		Point[] thesePoints = this.getPoints();
		Point[] thosePoints = other.getPoints();
		
		for (int i = 0; i < thesePoints.length; i++){
			for (int j = 0; j <thosePoints.length; j++){
				if (thesePoints[i].equals(thosePoints[j])){
					return thesePoints[i];
				}
			}
		}
		
		return null;

	}
	
	public Point otherPoint(Point p){
		ArrayList<Point> thesePoints = new ArrayList<Point>();
		
		for(int i = 0; i < this.getPoints().length; i++){
			thesePoints.add(this.getPoints()[i]);
		}
		
		if (thesePoints.contains(p)){
			thesePoints.remove(p);
		}
		
		if (thesePoints.size() != 1){
			System.err.println("Error in longest path algorithm; otherPoint method in Edge class.");
		}
		
		return thesePoints.get(0);
	}
	
	public String toString(){
		return "(" + id + ") " + points[0].getId() + " - " + points[1].getId();
	}
	
	public Point[] getPoints() {
		return points;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public Location getBoardPoint() {
		return boardPoint;
	}

	public void setBoardPoint(Location boardPoint) {
		this.boardPoint = boardPoint;
	}

}
