package pszt_scheduling;

public interface Algorithm {
	public void readData();
	public void createRandomPopulation();
	public void calculateFitness();
	public boolean checkTerminationCriteria();
	public void createNewGeneration();
	public void printSolution();
}
