package pszt_scheduling;

import java.util.ArrayList;
import java.util.Random;

public class SchedulingAlgorithm implements Algorithm {
	private int processorsNumber;
	private int maxIterations;
	private int populationSize;
	private double mutationRate;
	private double surviveRate;
	private Population population;
	private ArrayList<Integer> processDuration;
	
	public SchedulingAlgorithm(int processorsNumber,int maxIterations, int populationSize, double mutationRate, double surviveRate) {
		this.processorsNumber = processorsNumber;
		this.maxIterations = maxIterations + 1; //we check iterations amount before calculating anything
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.surviveRate = surviveRate;
		processDuration = new ArrayList<>();
	}
	/**
	 * method used to read input data for algorithm
	 * data can be randomly generated (right now)
	 * or read from file //TODO
	 */
	@Override
	public void readData() {
		Random random = new Random();
		int totalTime = 0;
		for (int i = 0; i < 2000; i++){
			int time = random.nextInt(10);
			//int time = 1;
			processDuration.add(time);
			totalTime += time;
		}
		System.out.println(totalTime);
	}
	/**
	 * method used to initialise algorithm with random population
	 */
	@Override
	public void createRandomPopulation() {
		population = new Population(processorsNumber, populationSize, processDuration, mutationRate, surviveRate);
	}
	/**
	 * method used to calculate fitness for every individual in population
	 * method leaves population sorted ascending by fitness value (processing type)
	 */
	@Override
	public void calculateFitness() {
		population.calculateFitness();
		
	}


	/**
	 * method checking if the algorithm should finish execution
	 * right now the only criteria is number of iterations, but
	 * can easily be changed to stop after reaching satisfactory solution
	 */
	@Override
	public boolean checkTerminationCriteria() {
		maxIterations--;
		return maxIterations == 0;
	}

	/**
	 * method responsible for managing crossover and mutation in population
	 */
	@Override
	public void createNewGeneration() {
		population.createNewGeneration();
	}
	
	public static void main(String[] args) {
		
		Algorithm algorithm = new SchedulingAlgorithm(10, 1000, 20, 0.03, 0.4);
		algorithm.readData();
		algorithm.createRandomPopulation();
		algorithm.calculateFitness();
		algorithm.printSolution(); //print solution generated randomly
		while(!algorithm.checkTerminationCriteria()){
			algorithm.createNewGeneration();
			algorithm.calculateFitness();
			//algorithm.printSolution(); //print solution for each generation
		}
		algorithm.printSolution(); //print solution for each generation
	}

	@Override
	public void printSolution() {
		System.out.println("solution: " + population.findBestIndividual().getFitnessValue());
	}

}
