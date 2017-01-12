package pszt_scheduling;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SchedulingAlgorithm implements Algorithm {
	private int processorsNumber;
	private int maxIterations;
	private int populationSize;
	private double mutationRate;
	private double surviveRate;
	private Population population;
	private ArrayList<Integer> processDuration;
	private ArrayList<Integer> solutionHistory;
	
	
	public SchedulingAlgorithm(int processorsNumber,int maxIterations, int populationSize, double mutationRate, double surviveRate) {
		this.processorsNumber = processorsNumber;
		this.maxIterations = maxIterations + 1; //we check iterations amount before calculating anything
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.surviveRate = surviveRate;
		processDuration = new ArrayList<>();
		solutionHistory = new ArrayList<>();
	}
	/**
	 * method used to read input data for algorithm
	 * data can be randomly generated (right now)
	 * or read from file //TODO
	 */
	public void readData() {
		Random random = new Random();
		int totalTime = 0;
		for (int i = 0; i < 200; i++){
			//int time = random.nextInt(10);
			int time = 1;
			processDuration.add(time);
			totalTime += time;
		}
		System.out.println(totalTime);
	}
	public void readData(ArrayList<Integer> durationOfTaskList){
		processDuration.addAll(durationOfTaskList);
		int totalTime = 0;
		for (int i: processDuration)
			totalTime += i;
		System.out.println(totalTime);
	}

	/**
	 * method used to initialise algorithm with random population
	 */
	public void createRandomPopulation() {
		population = new Population(processorsNumber, populationSize, processDuration, mutationRate, surviveRate);
	}
	/**
	 * method used to calculate fitness for every individual in population
	 * method leaves population sorted ascending by fitness value (processing type)
	 */
	public void calculateFitness() {
		population.calculateFitness();
		
	}


	/**
	 * method checking if the algorithm should finish execution
	 * right now the only criteria is number of iterations, but
	 * can easily be changed to stop after reaching satisfactory solution
	 */
	public boolean checkTerminationCriteria() {
		maxIterations--;
		//TODO add termination criteria based on satisfaction of solution: eg. terminate
		//when solution is smaller than 1,1 * minimal possible (sum_of_tasks_durations / nr_processors) 
		return maxIterations == 0;
	}

	/**
	 * method responsible for managing crossover and mutation in population
	 */
	public void createNewGeneration() {
		population.createNewGeneration();
	}
	
	
	public void printSolution() {
		System.out.println("solution: " + population.findBestIndividual().getFitnessValue());
	}
	
	public Solution calculateSolution() {
		createRandomPopulation();
		calculateFitness();
		solutionHistory.add(population.findBestIndividual().getFitnessValue());
		while (!checkTerminationCriteria()){
			createNewGeneration();
			calculateFitness();
			solutionHistory.add(population.findBestIndividual().getFitnessValue());
		}
		return new Solution(population.findBestIndividual().getFitnessValue(), solutionHistory);
	}
	

}
