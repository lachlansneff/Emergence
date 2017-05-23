import java.util.Random;

public class Neuron {
	// perceptron
	private double[] weights;
	private double bias;
	private double result;
	
	// Create random neuron
	// n inputs
	public Neuron(int n) {
		weights = new double[n];
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			weights[i] = rand.nextDouble()*2 -1;
		}
		bias = rand.nextDouble()*2 -1;
	}
	
	public Neuron(Neuron n) {
		weights = new double[n.weights.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = n.weights[i];
		}
		bias = n.bias;
		result = n.result;
	}
	
	// creates neuron from data
	public Neuron(double[] weights, double bias) {
		this.weights = weights.clone();
		this.bias = bias;
	}
	
	public double Process(double[] inputs) {
		double sum = bias;
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs[i] * weights[i];
		}
		
		result = Math.sin(sum);
		return result;
	}
	
	public double getResult() {
		return result;
	}
	
	public void Adjust(double[] inputs, double delta, double learningRate) {
		for (int i = 0; i < inputs.length; i++) {
			weights[i] = inputs[i] * delta * learningRate;
		}
		bias += delta * learningRate;
	}
	
	public static Neuron mutation(Neuron n) {
		double[] weights = new double[n.weights.length];
		for (int i = 0; i < n.weights.length; i++) {
			// randomly modify weights
			// only change weights a little bit
			double r = (Math.random()/10.0) + 0.95;
			weights[i] = n.weights[i] * r;
		}
		// mutate bias
		double r = (Math.random()/10.0) + 0.95;
		return new Neuron(weights, n.bias*r);
	}
}
