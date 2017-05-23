import java.util.Random;

import processing.core.PApplet;

public class Perlin {
	int width, height;
	PApplet p;
	
	public Perlin(PApplet parent, int width, int height) {
		this.width = width;
		this.height = height;
		this.p = parent;
	}
	
	public Tile[][] generatePerlinMap() {
		double[][] baseNoise = generateWhiteNoise();
		double[][] perlinNoise = generatePerlinNoise(baseNoise, 4, 0.2);
		Tile[][] tiles = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double perlin = perlinNoise[i][j];
				//System.out.println(perlin);
				int fertility, foodLevel;
				if (perlin < 0.4) {
					// is water
					fertility = 101;
				} else {
					fertility = (int) (p.noise(i*0.05f, j*0.02f) * 100.0);
					fertility = ((fertility-40)*5);
				}
				float ffoodLevel = p.noise(i*0.07f, j*0.07f);
				
				tiles[i][j] = new Tile(p, i, j, (int)(((ffoodLevel*100))), fertility);
			}
		}
		
		return tiles;
	}
	
	private double[][] generateWhiteNoise() {
		// generates a matrix of values between 0.0 and 1.0
		Random rand = new Random();
		double[][] noise = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				noise[i][j] = rand.nextDouble();
			}
		}
		return noise;
	}
	
	private double Interpolate(double x0, double x1, double alpha)
	{
	   return x0 * (1 - alpha) + alpha * x1;
	}
	
	private double[][] generateSmoothNoise(double[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		double[][] smoothNoise = new double[width][height];
		int samplePeriod = 1 << octave;
		double sampleFrequency = 1.0 / (double)samplePeriod;
		
		for (int i = 0; i < width; i++) {
			int sampleI0 = (i / samplePeriod) * samplePeriod;
			int sampleI1 = (sampleI0 + samplePeriod) % width;
			double horizontalBlend = (i - sampleI0) * sampleFrequency;
			
			for (int j = 0; j < height; j++) {
				int sampleJ0 = (j / samplePeriod) * samplePeriod;
				int sampleJ1 = (sampleJ0 + samplePeriod) % height;
				double verticalBlend = (j - sampleJ0) * sampleFrequency;
				
				double top = Interpolate(baseNoise[sampleI0][sampleJ0], baseNoise[sampleI1][sampleJ0], horizontalBlend);
				double bottom = Interpolate(baseNoise[sampleI0][sampleJ1], baseNoise[sampleI1][sampleJ1], horizontalBlend);
				
				smoothNoise[i][j] = Interpolate(top, bottom, verticalBlend);
			}
		}
		
		return smoothNoise;
	}
	
	private double[][] generatePerlinNoise(double[][] baseNoise, int octaveCount, double persistance) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		double[][][] smoothNoise = new double[octaveCount][][];
		
		// generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = generateSmoothNoise(baseNoise, i);
		}
		
		double[][] perlinNoise = new double[width][height];
		double amplitude = 1.0;
		double totalAmplitude = 0.0;
		
		for (int octave = octaveCount-1; octave > 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}
		
		// normalization
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
			}
		}
		
		return perlinNoise;
	}
}
