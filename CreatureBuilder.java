import java.awt.Color;
import java.util.HashMap;

public class CreatureBuilder {
	public double size, memory, selfColor_H, selfColor_S, selfColor_B;
	public int x, y;
	public Brain brain;
	
	public CreatureBuilder(CreatureBuilder cb) {
		size = cb.size;
		memory = cb.memory;
		selfColor_H = cb.selfColor_H;
		selfColor_S = cb.selfColor_S;
		selfColor_B = cb.selfColor_B;
		x = cb.x;
		y = cb.y;
		brain = cb.brain;
	}
	
	public CreatureBuilder() {}
	
	public CreatureBuilder fromCreature(Creature c) {
		CreatureBuilder b = c.getBuilder();
		return setSize(b.size)
				.setMemory(b.memory)
				.setBrain(b.brain)
				.setColor(Color.getHSBColor((float)b.selfColor_H, (float)b.selfColor_S, (float)b.selfColor_B))
				.setLocation(b.x, b.y);
	}
	
	public CreatureBuilder mutate() {
		return setBrain(Brain.Mutate(brain));
	}
	
	public CreatureBuilder setSize(double size) {
		this.size = size;
		return this;
	}
	
	public CreatureBuilder setMemory(double memory) {
		this.memory = memory;
		return this;
	}
	
	public CreatureBuilder setBrain(Brain brain) {
		this.brain = brain;
		return this;
	}
	
	public CreatureBuilder setColor(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		selfColor_H = hsb[0];
		selfColor_S = hsb[1];
		selfColor_B = hsb[2];
		
		return this;
	}
	
	public CreatureBuilder setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
}
