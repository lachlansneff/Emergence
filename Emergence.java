import java.awt.Color;
import java.util.Arrays;

import processing.core.*;

public class Emergence extends PApplet {
	// game board
	Board board;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("Emergence");
	}
	
	public void settings() {
		size(1280, 800);
	}
	
	public void setup() {
		colorMode(HSB, 360, 100, 100);
		board = new Board(this, 125, 68);
		board.generateBoard();
		
		// add 300 creatures
		for (int i = 0; i < 300; i++) {
			CreatureBuilder cb = new CreatureBuilder()
					.setSize(1.0)
					.setColor(Color.BLUE)
					.setBrain(new Brain(new int[]{12, 20, 10}))
					.setLocation((int)random(1280), (int)random(800));
			Creature c = new Creature(cb, board);
			board.addCreature(c);
		}
		
	}
	
	public void draw() {
		//long t0 = System.currentTimeMillis();
		board.display();
		//long t1 = System.currentTimeMillis();
		// draw fps meter
		//println(t1-t0);
	}

}
