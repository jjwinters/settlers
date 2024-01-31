package com.gmail.wintersj7.settlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

public class NormalBoard {
	
	/*  Indexing:
	 
		{ 0 } { 1 } { 2 }
		
	 { 3 } { 4 } { 5 } { 6 } 
 
  { 7 } { 8 } { 9 } {10 } {11 }
   
     {12 } {13 } {14 } {15 }	 
	
	    {16 } {17 } {18 } 
	
	*/
	
	private final int TILE_TOTAL = 19;
	private final int WHEAT = 4;
	private final int WOOL = 4;
	private final int WOOD = 4;
	private final int BRICK = 3;
	private final int ORE = 3;
	private final int DESERT = 1;
	private Tile[] tiles;
	private int blockedTileId;
	private Tile blockedTile;
	private ArrayList<Point> allPoints;
	
	public NormalBoard(boolean reroll) {
		/*if (WHEAT_TILES + WOOL_TILES + WOOD_TILES + BRICK_TILES + ORE_TILES + DESERT_TILES != TILE_TOTAL) {
			System.out.println("Tile totals do not match up in board config.");
			System.exit(1);
		}*/
		
		//System.out.println("1");
		// Initialize tiles with edges and points
		tiles = new Tile[TILE_TOTAL];
		Point[] tempPoints;
		Edge[] tempEdges;
		Point[] edgePoints;
		allPoints = new ArrayList<Point>();
		
		// Tile 0
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[3] = new Edge(edgePoints);
		tempPoints[3] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		tempPoints[4] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[0] = new Tile(tempEdges, tempPoints);
		
		/*
		System.out.println("Created tile 0.");
		for (int i = 0; i < tiles[0].getEdges().length; i++){
			System.out.println(tiles[0].getEdges()[i].toString());
		}*/
		
		// Tile 1
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[0].getEdges()[0];
			tempPoints[3] = tiles[0].getPoints()[1];
			tempPoints[4] = tiles[0].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[1] = new Tile(tempEdges, tempPoints);
		
		/*System.out.println("Created tile 1.");
		for (int i = 0; i < tiles[1].getPoints().length; i++){
			System.out.println("tempPoint p" + i + ": " + tempPoints[i].getId());
			System.out.println("Tile1     p" + i + ": " + tiles[1].getPoints()[i].getId());
		}
		for (int i = 0; i < tiles[0].getPoints().length; i++){
			System.out.println("Tile0     p" + i + ": " + tiles[0].getPoints()[i].getId());
		}*/
		
		
		// Tile 2
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[1].getEdges()[0];
			tempPoints[3] = tiles[1].getPoints()[1];
			tempPoints[4] = tiles[1].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
			
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
			
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
			
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
			
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[2] = new Tile(tempEdges, tempPoints);
		
		// Tile 7 
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];

		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[3] = new Edge(edgePoints);
		tempPoints[3] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		tempPoints[4] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[7] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 8
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[7].getEdges()[0];
			tempPoints[3] = tiles[7].getPoints()[1];
			tempPoints[4] = tiles[7].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[8] = new Tile(tempEdges, tempPoints);
		
		
		
		//Tile 9
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[8].getEdges()[0];
			tempPoints[3] = tiles[8].getPoints()[1];
			tempPoints[4] = tiles[8].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[9] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 10
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[9].getEdges()[0];
			tempPoints[3] = tiles[9].getPoints()[1];
			tempPoints[4] = tiles[9].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[10] = new Tile(tempEdges, tempPoints);
		
		
		//Tile 11
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[10].getEdges()[0];
			tempPoints[3] = tiles[10].getPoints()[1];
			tempPoints[4] = tiles[10].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[11] = new Tile(tempEdges, tempPoints);
		
		// Tile 16
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];

		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[3] = new Edge(edgePoints);
		tempPoints[3] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		tempPoints[4] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[16] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 17
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[16].getEdges()[0];
			tempPoints[3] = tiles[16].getPoints()[1];
			tempPoints[4] = tiles[16].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[17] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 18
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[3] = tiles[17].getEdges()[0];
			tempPoints[3] = tiles[17].getPoints()[1];
			tempPoints[4] = tiles[17].getPoints()[0];
		
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[3];
		tempEdges[2] = new Edge(edgePoints);
		tempPoints[2] = edgePoints[0];
		
		edgePoints[0] = tempPoints[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[4] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tempPoints[0];
		tempEdges[5] = new Edge(edgePoints);
		tempPoints[5] = edgePoints[0];
		
		tiles[18] = new Tile(tempEdges, tempPoints);
		
		// Tile 3
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[0].getEdges()[4];
			tempEdges[4] = tiles[7].getEdges()[1];
			tempEdges[5] = tiles[8].getEdges()[2];
			tempPoints[0] = tiles[8].getPoints()[2];
			tempPoints[1] = tiles[0].getPoints()[5];
			tempPoints[2] = tiles[0].getPoints()[4];
			tempPoints[4] = tiles[7].getPoints()[2];
			tempPoints[5] = tiles[7].getPoints()[1];
			
		edgePoints[0] = tiles[8].getPoints()[2];
		edgePoints[1] = tiles[0].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		
		edgePoints[0] = tiles[0].getPoints()[4];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[2] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tiles[7].getPoints()[2];
		tempEdges[3] = new Edge(edgePoints);
		tempPoints[3] = edgePoints[0];
			
		tiles[3] = new Tile(tempEdges, tempPoints);
		
		// Tile 4
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[1].getEdges()[4];
			tempEdges[2] = tiles[0].getEdges()[5];
			tempEdges[3] = tiles[3].getEdges()[0];
			tempEdges[4] = tiles[8].getEdges()[1];
			tempEdges[5] = tiles[9].getEdges()[2];
			tempPoints[0] = tiles[9].getPoints()[2];
			tempPoints[1] = tiles[1].getPoints()[5];
			tempPoints[2] = tiles[1].getPoints()[4];
			tempPoints[3] = tiles[0].getPoints()[5];
			tempPoints[4] = tiles[8].getPoints()[2];
			tempPoints[5] = tiles[8].getPoints()[1];
			
		edgePoints[0] = tiles[9].getPoints()[2];
		edgePoints[1] = tiles[1].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		
		tiles[4] = new Tile(tempEdges, tempPoints);
		
		// Tile 5
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[2].getEdges()[4];
			tempEdges[2] = tiles[1].getEdges()[5];
			tempEdges[3] = tiles[4].getEdges()[0];
			tempEdges[4] = tiles[9].getEdges()[1];
			tempEdges[5] = tiles[10].getEdges()[2];
			tempPoints[0] = tiles[10].getPoints()[2];
			tempPoints[1] = tiles[2].getPoints()[5];
			tempPoints[2] = tiles[2].getPoints()[4];
			tempPoints[3] = tiles[1].getPoints()[5];
			tempPoints[4] = tiles[9].getPoints()[2];
			tempPoints[5] = tiles[9].getPoints()[1];
			
		edgePoints[0] = tiles[10].getPoints()[2];
		edgePoints[1] = tiles[2].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		
		tiles[5] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 6
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[2] = tiles[2].getEdges()[5];
			tempEdges[3] = tiles[5].getEdges()[0];
			tempEdges[4] = tiles[10].getEdges()[1];
			tempEdges[5] = tiles[11].getEdges()[2];
			tempPoints[0] = tiles[11].getPoints()[2];
			tempPoints[2] = tiles[2].getPoints()[0];
			tempPoints[3] = tiles[2].getPoints()[5];
			tempPoints[4] = tiles[10].getPoints()[2];
			tempPoints[5] = tiles[10].getPoints()[1];
			
		edgePoints[0] = tiles[11].getPoints()[2];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[0] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tiles[2].getPoints()[0];
		tempEdges[1] = new Edge(edgePoints);
		tempPoints[1] = edgePoints[0];
		
		tiles[6] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 12
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[8].getEdges()[4];
			tempEdges[2] = tiles[7].getEdges()[5];
			tempEdges[5] = tiles[16].getEdges()[2];
			tempPoints[0] = tiles[16].getPoints()[2];
			tempPoints[1] = tiles[8].getPoints()[5];
			tempPoints[2] = tiles[8].getPoints()[4];
			tempPoints[3] = tiles[7].getPoints()[5];
			tempPoints[5] = tiles[16].getPoints()[3];
			
		edgePoints[0] = tiles[16].getPoints()[2];
		edgePoints[1] = tiles[8].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		
		edgePoints[0] = tiles[7].getPoints()[5];
		edgePoints[1] = new Point();
		allPoints.add(edgePoints[1]);
		tempEdges[3] = new Edge(edgePoints);
		
		edgePoints[0] = edgePoints[1];
		edgePoints[1] = tiles[16].getPoints()[3];
		tempEdges[4] = new Edge(edgePoints);
		tempPoints[4] = edgePoints[0];
			
		tiles[12] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 13
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[9].getEdges()[4];
			tempEdges[2] = tiles[8].getEdges()[5];
			tempEdges[3] = tiles[11].getEdges()[0];
			tempEdges[4] = tiles[16].getEdges()[1];
			tempEdges[5] = tiles[17].getEdges()[2];
			tempPoints[0] = tiles[17].getPoints()[2];
			tempPoints[1] = tiles[9].getPoints()[5];
			tempPoints[2] = tiles[9].getPoints()[4];
			tempPoints[3] = tiles[8].getPoints()[5];
			tempPoints[4] = tiles[16].getPoints()[2];
			tempPoints[5] = tiles[16].getPoints()[1];
			
		edgePoints[0] = tiles[17].getPoints()[2];
		edgePoints[1] = tiles[9].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		
		tiles[13] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 14
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[10].getEdges()[4];
			tempEdges[2] = tiles[9].getEdges()[5];
			tempEdges[3] = tiles[12].getEdges()[0];
			tempEdges[4] = tiles[17].getEdges()[1];
			tempEdges[5] = tiles[18].getEdges()[2];
			tempPoints[0] = tiles[18].getPoints()[2];
			tempPoints[1] = tiles[10].getPoints()[5];
			tempPoints[2] = tiles[10].getPoints()[4];
			tempPoints[3] = tiles[9].getPoints()[5];
			tempPoints[4] = tiles[17].getPoints()[2];
			tempPoints[5] = tiles[17].getPoints()[1];
			
		edgePoints[0] = tiles[18].getPoints()[2];
		edgePoints[1] = tiles[10].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		
		tiles[14] = new Tile(tempEdges, tempPoints);
		
		
		// Tile 15
		tempEdges = new Edge[6];
		tempPoints = new Point[6];
		edgePoints = new Point[2];
		
			// Pre-existing
			tempEdges[1] = tiles[11].getEdges()[4];
			tempEdges[2] = tiles[10].getEdges()[5];
			tempEdges[3] = tiles[14].getEdges()[0];
			tempEdges[4] = tiles[18].getEdges()[1];
			tempPoints[1] = tiles[11].getPoints()[5];
			tempPoints[2] = tiles[11].getPoints()[4];
			tempPoints[3] = tiles[10].getPoints()[5];
			tempPoints[4] = tiles[18].getPoints()[2];
			tempPoints[5] = tiles[18].getPoints()[1];
			
		edgePoints[0] = new Point();
		allPoints.add(edgePoints[0]);
		edgePoints[1] = tiles[11].getPoints()[5];
		tempEdges[0] = new Edge(edgePoints);
		tempPoints[0] = edgePoints[0];
		
		edgePoints[1] = edgePoints[0];
		edgePoints[0] = tiles[18].getPoints()[1];
		
		tempEdges[5] = new Edge(edgePoints);
		
		tiles[15] = new Tile(tempEdges, tempPoints);
		
		//System.out.println("2");
		
		
		// Randomize and assign types
		int[] indecies = new int[TILE_TOTAL];
		
		for (int i = 0; i < indecies.length; i++){
			indecies[i] = i;
		}
		
		//System.out.println("3");
		shuffleArray(indecies);
		//System.out.println("4");
		for (int i = 0; i < WHEAT; i++){
			tiles[indecies[i]].setType("field");
		}
		
		for (int i = WHEAT; i < WHEAT + ORE; i++){
			tiles[indecies[i]].setType("mountain");
		}
		
		for (int i = WHEAT + ORE; i < WHEAT + ORE + WOOD; i++){
			tiles[indecies[i]].setType("forest");
		}
		
		for (int i = WHEAT + ORE + WOOD; i < WHEAT + ORE + WOOD + WOOL; i++){
			tiles[indecies[i]].setType("pasture");
		}
		
		for (int i = WHEAT + ORE + WOOD + WOOL; i < WHEAT + ORE + WOOD + WOOL + BRICK; i++){
			tiles[indecies[i]].setType("quarry");
		}
		
		for (int i = WHEAT + ORE + WOOD + WOOL + BRICK; i < WHEAT + ORE + WOOD + WOOL + BRICK + DESERT; i++){
			tiles[indecies[i]].setType("desert");
		}
		//System.out.println("5");
		
		// Randomize and assign numbers
		int[] values = new int[] {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
		
		shuffleArray(indecies);
		
		this.setBlockedTileId(-1);
		
		// forloop thru them, skip deserts!
		int j = 0;
		for (int i = 0; i < TILE_TOTAL; i++){
			if (tiles[indecies[i]].getType().equals("desert")){
				tiles[indecies[i]].setResource_value(-1);
				tiles[indecies[i]].setBlocked(true);
				this.setBlockedTileId(tiles[indecies[i]].getId());
				this.setBlockedTile(tiles[indecies[i]]);
			}
			else {
				tiles[indecies[i]].setResource_value(values[j]);
				j++;
			}
		}
		//System.out.println("6");
		// TODO: if a point totals 14 or higher, reroll the number assignment if reroll is enabled.
		
	
		
	} // end constructor
	
	public void addPort(int pointId, int portType){
		Point p = this.getPointById(pointId);
		p.setPort(portType);
		Location l = p.getBoardPoint().clone();
		World w = l.getWorld();
		
		// Create port signs
		l.setY(l.getY() + 2);
		Block b = w.getBlockAt(l);
		Sign s;
		org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.WALL_SIGN);
		String resource = "Any";
		ItemStack frameItem = new ItemStack(Material.GLASS);
		int ratio = 3;
		
		switch(portType){
		case 1:
			resource = "Wood Plank";
			frameItem.setType(Material.WOOD);
			ratio = 2;
			break;
		case 2:
			resource = "Bread";
			frameItem.setType(Material.BREAD);
			ratio = 2;
			break;
		case 3:
			resource = "Wool";
			frameItem.setType(Material.WOOL);
			ratio = 2;
			break;
		case 4:
			resource = "Brick";
			frameItem.setType(Material.CLAY_BRICK);
			ratio = 2;
			break;
		case 5:
			resource = "Iron Ingot";
			frameItem.setType(Material.IRON_INGOT);
			ratio = 2;
			break;
		default:
			
		}
		
		b.setType(Material.NETHER_BRICK);
		b.getRelative(BlockFace.NORTH).setType(Material.WALL_SIGN);
		s = (Sign) b.getRelative(BlockFace.NORTH).getState();
		s.setLine(0, "PORT");
		s.setLine(2, resource);
		s.setLine(3, ratio + " : 1");
		matSign.setFacingDirection(BlockFace.NORTH);
		s.setData(matSign);
		s.update();
		
		b.getRelative(BlockFace.SOUTH).setType(Material.WALL_SIGN);
		s = (Sign) b.getRelative(BlockFace.SOUTH).getState();
		s.setLine(0, "PORT");
		s.setLine(2, resource);
		s.setLine(3, ratio + " : 1");
		matSign.setFacingDirection(BlockFace.SOUTH);
		s.setData(matSign);
		s.update();
		
		b.getRelative(BlockFace.EAST).setType(Material.WALL_SIGN);
		s = (Sign) b.getRelative(BlockFace.EAST).getState();
		s.setLine(0, "PORT");
		s.setLine(2, resource);
		s.setLine(3, ratio + " : 1");
		matSign.setFacingDirection(BlockFace.EAST);
		s.setData(matSign);
		s.update();
		
		b.getRelative(BlockFace.WEST).setType(Material.WALL_SIGN);
		s = (Sign) b.getRelative(BlockFace.WEST).getState();
		s.setLine(0, "PORT");
		s.setLine(2, resource);
		s.setLine(3, ratio + " : 1");
		matSign.setFacingDirection(BlockFace.WEST);
		s.setData(matSign);
		s.update();
		
		l.setY(l.getY() + 1);
		w.getBlockAt(l).setType(Material.NETHER_BRICK);
		b = w.getBlockAt(l);
		
		ItemFrame fr1 = (ItemFrame) w.spawnEntity(b.getLocation(), EntityType.ITEM_FRAME);
		fr1.setFacingDirection(BlockFace.NORTH);
		fr1.setItem(frameItem);
	
		/* Leave out the other itemframes on port signs until you can place them properly.
		ItemFrame fr2 = (ItemFrame) w.spawnEntity(b.getLocation(), EntityType.ITEM_FRAME);
		fr2.setFacingDirection(BlockFace.SOUTH, true);
		fr2.setItem(frameItem);
		
		ItemFrame fr3 = (ItemFrame) w.spawnEntity(b.getLocation(), EntityType.ITEM_FRAME);
		fr3.setFacingDirection(BlockFace.EAST, true);
		fr3.setItem(frameItem);
		
		ItemFrame fr4 = (ItemFrame) w.spawnEntity(b.getLocation(), EntityType.ITEM_FRAME);
		fr4.setFacingDirection(BlockFace.WEST, true);
		fr4.setItem(frameItem);
		*/
	}
	
	static void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--){
			int index = rnd.nextInt(i + 1);
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
	    }
	}
	
	public Point[] allPoints() {
		Point[] a = new Point[this.getAllPoints().size()];
		a = this.getAllPoints().toArray(a);
		return a;
	}
	
	public Edge[] allEdges() {
		HashSet<Edge> edges = new HashSet<Edge>();
		
		for (int i = 0; i < this.getTiles().length; i++) {
			for (int j = 0; j < this.getTiles()[i].getEdges().length; j++) {
				if (!edges.add(this.getTiles()[i].getEdges()[j])){
					//System.out.println(this.getTiles()[i].getPoints()[j].getId() + " OMMITED");
				}
				else {
					//System.out.println(this.getTiles()[i].getPoints()[j].getId() + "            ADDED");
				}
					
				
			}
		}
		
		Edge[] a = new Edge[edges.size()];
		Iterator<Edge> iterator = edges.iterator();
		int count = 0;
		
		while (iterator.hasNext()) {
			a[count] = iterator.next();
			count++;
		}
		
		return a;
	}
	
	public void setTiles(Tile[] tiles) {
		this.tiles = tiles;
	}
	
	public Tile[] getTiles () {
		return this.tiles;
	}
	
	public Point getPointById(int id){
		/*for (int i = 0; i < this.getTiles().length; i++) {
			Point[] tempPoints = this.getTiles()[i].getPoints();
			
			for (int j = 0; j < tempPoints.length; j++) {
				if (tempPoints[j].getId() == id)
					return tempPoints[j];
			}
		}*/
		ArrayList<Point> points = this.getAllPoints();
		Iterator<Point> itPoints = points.iterator();
		
		while (itPoints.hasNext()){
			Point tempPoint = itPoints.next();
			if (tempPoint.getId() == id) {
				return tempPoint;
			}
		}
		
		System.out.println("Couldn't find point by ID " + id);
		return null;
	}
	
	public Edge getEdgeById(int id){
		for (int i = 0; i < this.getTiles().length; i++) {
			Edge[] tempEdges = this.getTiles()[i].getEdges();
			
			for (int j = 0; j < tempEdges.length; j++) {
				if (tempEdges[j].getId() == id)
					return tempEdges[j];
			}
		}
		
		System.out.println("Couldn't find Edge by ID " + id);
		return null;
	}
	
	public Tile getTileById(int id){
		for (int i = 0; i < this.getTiles().length; i++) {
			if (tiles[i].getId() == id)
				return tiles[i];
			
		}
		
		System.out.println("Couldn't find tile by ID " + id);
		return null;
	}
	
	public void unblockTiles() {
		for (int i = 0; i < this.getTiles().length; i++) {
			tiles[i].setBlocked(false);
			
		}
		
	}


	public int getBlockedTileId() {
		return blockedTileId;
	}


	public void setBlockedTileId(int blockedTileId) {
		this.blockedTileId = blockedTileId;
	}
	
	public int getTileTotal(){
		return this.TILE_TOTAL;
	}

	public ArrayList<Point> getAllPoints() {
		return this.allPoints;
	}

	public Tile getBlockedTile() {
		return blockedTile;
	}

	public void setBlockedTile(Tile blockedTile) {
		this.blockedTile = blockedTile;
	}


	
} // end normal board class
