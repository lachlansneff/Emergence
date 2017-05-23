import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;

public class Board {
	// parent processing applet
	PApplet p;
	// Contains list of tiles and placement
	Tile[][] tiles;
	// list of creatures
	ArrayList<Creature> creatures;
	ArrayList<Creature> creaturesToKill;
	ArrayList<Creature> creaturesToAdd;
	// width and height of board
	int width, height;
	
	public Board(PApplet parent, int width, int height) {
		// TODO Auto-generated constructor stub
		p = parent;
		this.width = width;
		this.height = height;
		
		tiles = new Tile[width][height];
		long seed = new Random().nextLong();
		p.noiseSeed(seed);
		p.randomSeed(seed);
		
		creatures = new ArrayList<Creature>(300);
		creaturesToKill = new ArrayList<Creature>();
		creaturesToAdd = new ArrayList<Creature>();
	}
	
	public void addCreature(Creature creature) {
		creaturesToAdd.add(creature);
	}
	
	void display() {
		// fill screen with black
		p.fill(0, 0, 0);
		p.rect(0, 0, p.width, p.height);
		
		// display tiles
		for (Tile[] tilerow : tiles) {
			for (Tile tile : tilerow) {
				tile.update();
				tile.display();
			}
		}
		// display creatures
		for (Creature c : creatures) {
			if (c.x >= (width*10)+10) {
				c.x = 0;
			}
			if (c.y >= (height*10)+10) {
				c.y = 0;
			}
			if (c.x < 0) {
				c.x = width*10;
			}
			if (c.y < 0) {
				c.y = height*10;
			}
			c.update();
			c.display();
		}
		for (int i = 0; i < creaturesToKill.size(); i++) {
			creatures.remove(creaturesToKill.get(i));
		}
		creaturesToKill.clear();
		for (Creature c : creaturesToAdd) {
			creatures.add(c);
		}
		creaturesToAdd.clear();
		// 
	}
	
	public void killCreature(Creature c) {
		creaturesToKill.add(c);
	}
	
	public PApplet getPApplet() {
		return p;
	}
	
	public double[] colorAtPos(int x, int y, int x2, int y2) {
		double[] color = new double[5];
		// iterate through creatures
		for (Creature c : creatures) {
			if (c.getX() == x && c.getY() == y) {
				double[] cl = c.getColor();
				color[0] = cl[0];
				color[1] = cl[1];
				color[2] = cl[2];
				break;
			}
		}
		
		// iterate through tiles
		boolean isbreak = false;
		for (Tile[] tilerow : tiles) {
			for (Tile tile : tilerow) {
				if (tile.x == x && tile.y == y) {
					int c = tile.getColor();
					color[0] = p.hue(c);
					color[1] = p.saturation(c);
					color[2] = p.brightness(c);
					if (isbreak) {
						break;
					}
					isbreak = true;
				}
				if (tile.x == x2 && tile.y == y2) {
					// tile found
					color[3] = tile.foodLevel;
					color[4] = tile.fertility;
					tile.foodLevel /= 0.3;
					tile.fertility /= 0.35;
					if (isbreak) {
						break;
					}
					isbreak = true;
				}
			}
		}
		if (color[0] == 0) {
			color[0] = 218;
			color[1] = 66;
			color[2] = 90;
		}
		return color;
	}
	
	void generateBoard() {
		Perlin perlin = new Perlin(p, width, height);
		tiles = perlin.generatePerlinMap();
	}
	
	// ranks by size
	public Creature getTopCreature() {
		double max = Integer.MIN_VALUE;
		int index = 0;
		for (int i = 0; i < creatures.size(); i++) {
			Creature c = creatures.get(i);
			if (c.size > max) {
				max = c.size;
				index = i;
			}
		}
		return creatures.get(index);
	}

}
