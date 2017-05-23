import java.util.Random;

public class Brain {
	private Neuron[][] neurons;
	private int[] nn_shape;
	private int input_num, output_num, hidden_num;
	
	// constructs a brain with shape
	// For example:
	// shape = [3, 4, 4, 2] creates network with 3 inputs, 2 hidden layers of 4 each, and 2 outputs
	public Brain(int[] shape) {
		if (shape.length < 3) {
			throw new IllegalArgumentException("Network must have input, output, and at least one hidden layer");
		}
		input_num = shape[0];
		output_num = shape[shape.length-1];
		hidden_num = shape.length-(input_num+output_num);
		
		neurons = new Neuron[shape.length][];
		for (int i = 0; i < shape.length; i++) {
			neurons[i] = new Neuron[shape[i]];
			for (int j = 0; j < shape[i]; j++) {
				if (i == 0) {
					neurons[i][j] = new Neuron(1);
				}
				else {
					neurons[i][j] = new Neuron(shape[i-1]);
				}
			}
		}
		nn_shape = shape;
	}
	
	public Brain(Brain b) {
		// copy neurons
		neurons = new Neuron[b.neurons.length][];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron[b.neurons[i].length];
			for (int j = 0; j < neurons[i].length; j++) {
				neurons[i][j] = new Neuron(b.neurons[i][j]);
			}
		}
		// bad practice?
		nn_shape = b.nn_shape.clone();
		
		input_num = b.input_num;
		hidden_num = b.hidden_num;
		output_num = b.output_num;
	}
	
	// mutation
	public static Brain Mutate(Brain brain) {
		Brain new_brain = new Brain(brain);
		// mutates various neurons
		// over time, the neural networks should shift towards effective behavior
		Random rand = new Random();
		Neuron[][] neurons = brain.neurons;
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].length; j++) {
				Neuron n = neurons[i][j];
				// create new mutation
				new_brain.neurons[i][j] = Neuron.mutation(n);
			}
		}
		return new_brain;
	}
	
	public double[] Process(double[] input) {
		if (input.length != input_num) {
			// inputs don't match
			return null;
		}
		// the magic
		double[] input_layer_output = new double[neurons[0].length];
		for (int i = 0; i < neurons[0].length; i++) {
			// do inputs
			input_layer_output[i] = neurons[0][i].Process(new double[]{input[i]});
		}
		double[] last_output = input_layer_output;
		double[] new_output;
		for (int i = 1; i < neurons.length; i++) {
			// create new array for new_output
			new_output = new double[neurons[i].length];
			// for each neuron in each layer process inputs
			for (int j = 0; j < neurons[i].length; j++) {
				new_output[j] = neurons[i][j].Process(last_output);
			}
			// queue this layers outputs for input into the next layer
			last_output = new_output;
		}
		// last_output is the output of the neural network
		return last_output;
	}
	
	public int getInputNum() {
		return input_num;
	}
	
}
