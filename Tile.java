import processing.core.PApplet;

public class Tile {
	public int x, y; // tile matrix coords
	int foodLevel;	// 0..100 food on tile
	int fertility;	// 0..100 how fast food will grow back, >100 if water
	
	PApplet p;
	
	final int TILE_SIDE_LENGTH = 10;
	
	final int WATER_COLOR;
	final int FERTILE_COLOR;
	final int BARREN_COLOR;
	
	double update_counter;
	
	public Tile(PApplet parent, int x, int y, int foodLevel, int fertility) {
		this.p = parent;
		this.x = x;
		this.y = y;
		this.foodLevel = foodLevel;
		this.fertility = fertility;
		
		WATER_COLOR = p.color(218, 66, 90);
		FERTILE_COLOR = p.color(100, 100, 60);
		BARREN_COLOR = p.color(60, 100, 60);
		update_counter = 0;
	}
	
	public void update() {
		// update food level, etc
		update_counter += 1;
		if (update_counter >= 1) {
			if (fertility < 100) {
				fertility++;
			}
			if (foodLevel < 100) {
				foodLevel+= fertility/50;
			}
			update_counter = 0;
		}
	}
	
	public void display() {
		// converts matrix coords into real coordinates
		int rect_x = (TILE_SIDE_LENGTH*x);
		int rect_y = (TILE_SIDE_LENGTH*(y+1));
		// gets tile type color
		p.fill(getColor());
		//p.noStroke();
		p.rect(rect_x, rect_y, TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
	}
	
	public int getColor() {
		int color;
		if (fertility > 100) {
			color = WATER_COLOR;
		}
		else {
			color = p.lerpColor(BARREN_COLOR, FERTILE_COLOR, (float) (((float)fertility)/100.0));
			color = p.color(p.hue(color), foodLevel, p.brightness(color));
		}
		
		return color;
	}
}
