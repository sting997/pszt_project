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
	
	@Override
	public void readData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 2000; i++)
			processDuration.add(1);
	}

	@Override
	public void createRandomPopulation() {
		population = new Population(processorsNumber, populationSize, processDuration, surviveRate);
	}

	@Override
	public void calculateFitness() {
		population.calculateFitness();
		
	}

	
	void performCrossingOver() {
		population.performCrossingOver();		
	}


	void performMutation() {
		Random random = new Random();
		if ( random.nextDouble() < mutationRate ) 
			population.performMutation();      
	}

	@Override
	public boolean checkTerminationCriteria() {
		maxIterations--;
		return maxIterations == 0;
	}

	@Override
	public void createNewGeneration() {
		population.createNewGeneration();
	}
	
	public static void main(String[] args) {
		
		Algorithm algorithm = new SchedulingAlgorithm(10, 1000, 5, 0.03, 0.5);
		algorithm.readData();
		algorithm.createRandomPopulation();
		algorithm.calculateFitness();
		algorithm.printSolution(); //print solution generated randomly
		while(!algorithm.checkTerminationCriteria()){
			algorithm.createNewGeneration();
			algorithm.calculateFitness();
			algorithm.printSolution(); //print solution for each generation
		}
	}

	@Override
	public void printSolution() {
		System.out.println("solution: " + population.findBestIndividual().getFitnessValue());
	}

}
