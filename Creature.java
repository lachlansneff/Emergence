import processing.core.PApplet;

public class Creature {
	public int x, y; // actual position
	private Brain brain;
	private CreatureBuilder builder;
	private Board board;
	// outputs
	double sidewayVel, forwardVel, selfColor_H, selfColor_S, selfColor_B, duplicate, memory, attack, lookDistance, angle;
	// inputs
	double frontColor_H, frontColor_S, frontColor_B, size;
	
	public Creature(CreatureBuilder builder, Board board) {
		this.builder = builder;
		this.x = builder.x;
		this.y = builder.y;
		this.size = builder.size;
		this.brain = builder.brain;
		this.board = board;
	}
	
	public void update() {
		// do the thinking
		double[] inputs = new double[brain.getInputNum()];
		// set inputs
		inputs[0] = frontColor_H;
		inputs[1] = frontColor_S;
		inputs[2] = frontColor_B;
		inputs[3] = sidewayVel;
		inputs[4] = forwardVel;
		inputs[5] = selfColor_H;
		inputs[6] = selfColor_S;
		inputs[7] = selfColor_B;
		inputs[8] = size;
		inputs[9] = memory;
		inputs[11] = angle;
		// process inputs
		double[] outputs = brain.Process(inputs);
		// process outputs
		sidewayVel = outputs[0];
		forwardVel = outputs[1];
		selfColor_H = outputs[2];
		selfColor_S = outputs[3];
		selfColor_B = outputs[4];
		duplicate = outputs[5];
		memory = outputs[6];
		attack = outputs[7];
		lookDistance = outputs[8];
		angle = outputs[9];
		// update values
		// update front color
		double[] frontColor = getFrontColor(this.x, this.y);
		frontColor_H = frontColor[0];
		frontColor_S = frontColor[1];
		frontColor_B = frontColor[2];
		// update position
		int xvel = getXVel();
		int yvel = getYVel();
		x += xvel;
		y += yvel;
		// update size, if needed
		if (d2b(duplicate) && size >= 1.3) {
			// creature wants to duplicate
			// does the creature divide in half?, or some percentage
			// do I want to add reproduction with multiple creatures?
			// for now 50/50 divide
			size /= 2;
			board.addCreature(mutate());
		}
		if (size <= 0.5) {
			board.killCreature(this);
		}
		if (size < 2.0) {
			//System.out.println(frontColor[3]);
			size += (frontColor[3]/100.0);
		}
		
	}
	
	public void display() {
		PApplet p = board.getPApplet();
		p.fill(p.color((int)(selfColor_H*360.0), (int)(selfColor_S*100.0), (int)(selfColor_B*100.0)));
		p.ellipse(x, y, (float)(size*10), (float)(size*10));
	}
	
	public double[] getFrontColor(int x, int y) {
		double[] color = new double[5];
		// convert angle to radians
		double rad = (angle+1.0)*Math.PI;
		
		double xc = Math.cos(rad)*(lookDistance*100);
		double yc = Math.sin(rad)*(lookDistance*100);
		
		return board.colorAtPos((int)xc, (int)yc, x, y);
	}
	
	public int getXVel() {
		// convert -1..1 to radians
		double rad = (angle+1.0)*Math.PI;
		//
		double xvel0 = forwardVel*Math.cos(rad);
		// fix sideways
		double siderad = rad+(Math.PI/2.0);
		double xvel1 = sidewayVel*Math.cos(siderad);
		return (int) Math.round(xvel0-xvel1);
	}
	
	public int getYVel() {
		double rad = (angle+1.0)*Math.PI;
		//
		double yvel0 = forwardVel*Math.sin(rad);
		// fix sideways
		double siderad = rad+(Math.PI/2.0);
		double yvel1 = sidewayVel*Math.sin(siderad);
		return (int)Math.round(yvel0-yvel1);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public double[] getColor() {
		return new double[]{selfColor_H, selfColor_S, selfColor_B};
	}
	
	public boolean d2b(double d) {
		return d>0;
	}
	
	public CreatureBuilder getBuilder() {
		return builder;
	}
	
	// returns mutated creature
	public Creature mutate() {
		CreatureBuilder b = new CreatureBuilder(builder);
		b.setLocation(x, y);
		b.setMemory(memory);
		b.setSize(size);
		b.setBrain(Brain.Mutate(brain));
		
		Creature c = new Creature(b, board);
		c.angle = angle;
		c.attack = angle;
		c.frontColor_H = frontColor_H;
		c.frontColor_S = frontColor_S;
		c.frontColor_B = frontColor_B;
		c.lookDistance = c.lookDistance;
		c.memory = memory;
		c.selfColor_H = selfColor_H;
		c.selfColor_S = selfColor_S;
		c.selfColor_B = selfColor_B;
		
		board.addCreature(c);
		
		return c;
	}
}
