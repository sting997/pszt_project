package pszt_scheduling;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Population
{
    private static Random m_rand = new Random();  // random-number generator
    private ArrayList<Individual> currentPopulation;
    private ArrayList<Individual> newPopulation;
    private int totalFitness;
    private int populationSize;
    private ArrayList<Integer> processDuration;
	private int processorsNumber;
	private double mutationRate;
	private double surviveRate;
	
    public Population(int processorsNumber, int populationSize, ArrayList<Integer> data, double mutationRate, double surviveRate) {
		this.processorsNumber = processorsNumber;
    	this.populationSize = populationSize;
    	processDuration = new ArrayList<>(data);
        currentPopulation = new ArrayList<>();
        newPopulation = new ArrayList<>();
        this.mutationRate = mutationRate;
        this.surviveRate = surviveRate;
        
        createRandomPopulation();
    }
    
    /**
     * this method creates population consisting of populationSize unique individuals
     */
    public void createRandomPopulation() {
        int count = 0;
        while (count != populationSize){
            Individual individual = new Individual();
            individual.randomGenes();
            if (!currentPopulation.contains(individual)) {
            	currentPopulation.add(individual);
            	count++;
            }
        }
	}
    
    /**
     * method used to calculate fitness of every individual and whole current population
     * @return fitness of whole population
     */
    public int calculateFitness() {
        this.totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            this.totalFitness += currentPopulation.get(i).calculateFitness();
        }
        Collections.sort(currentPopulation);

        return this.totalFitness;
    }

    /**
     * implementation of selection based on the roulette wheel
     * inidividuals with better fitness value are more likely to be selected
     * @return
     */
    Individual rouletteWheelSelection() {
        double randNum = m_rand.nextDouble() * this.totalFitness;
        int i;
        for (i = 0; i < populationSize && randNum > 0; i++) {
            randNum -= currentPopulation.get(i).getFitnessValue();
        }
        return currentPopulation.get(i-1);
    }

    public Individual findBestIndividual() {
    	return currentPopulation.get(0);
    }

    /**
     * implementation of random point crossover algorithm
     * children are a mix of parents following the rule:
     * first part of child comes from one parent, second from another
     * eg(rand point shown by "."): (1 2 3 . 4 5 6) + (6 5 4 . 3 2 1) -> (1 2 3 3 2 1), (6 5 4 4 5 6)  
     * @param i1 first parent
     * @param i2 second parent
     * @return array of 2 children
     */
    public Individual[] crossover(Individual i1,Individual i2) {
        Individual[] newIndiv = new Individual[2];
        newIndiv[0] = new Individual();
        newIndiv[1] = new Individual();

        int randPoint = m_rand.nextInt(processDuration.size());
        int i;
        for (i=0; i < randPoint; i++) {
            newIndiv[0].setGene(i, i1.getGene(i));
            newIndiv[1].setGene(i, i2.getGene(i));
        }
        for (; i < processDuration.size(); i++) {
            newIndiv[0].setGene(i, i2.getGene(i));
            newIndiv[1].setGene(i, i1.getGene(i));
        }

        return newIndiv;
    }

   /**
    * method controlling the genetic process
    * creation of new population:
    * 1. add surviveRate of current population to new population (save best individuals) 
    * 2. crossover current population and add children to new population (if children are unique)
    * 3. set new population as current population
    * 4. mutate
    */
    public void createNewGeneration() {
		selectSurivingIndividuals();
		
		Individual[] children = new Individual[2];
		while (newPopulation.size() != populationSize){
			children = performCrossingOver();
			if(!newPopulation.contains(children[0])) 
					newPopulation.add(children[0]);
			if (newPopulation.size() == populationSize)
				break;
			if(!newPopulation.contains(children[1])) 
					newPopulation.add(children[1]);
		}
		currentPopulation = newPopulation;
		performMutation();
	}
    
    void selectSurivingIndividuals() {
    	newPopulation = new ArrayList<>();
		for (int i = 0; i < populationSize * surviveRate; i++)
			newPopulation.add(currentPopulation.get(i));
	}
    
    Individual[] performCrossingOver() {
		Individual[] parents = new Individual[2];
		parents[0] = rouletteWheelSelection();
		while(true){
			parents[1] = rouletteWheelSelection();
			if (! parents[0].equals(parents[1]))
				break;
		}
		return crossover(parents[0], parents[1]);
	}

	void performMutation() {
		if ( m_rand.nextDouble() < mutationRate ) {
			int i = m_rand.nextInt(populationSize);
			currentPopulation.get(i).mutate();
		}
	}
    
    /**
     * class reprezenting individual in population
     * chromosome is represented by array of genes which represents asocciation between task and processor
     * index of the array represents a task, value under that index represents processor which executes task
     * @author mike
     *
     */
    class Individual implements Comparable<Individual>
    {
        private int[] genes = new int[processDuration.size()];
        private int fitnessValue;

        public Individual() {}

        public int getFitnessValue() {
            return fitnessValue;
        }

        public void setFitnessValue(int fitnessValue) {
            this.fitnessValue = fitnessValue;
        }

        public int getGene(int index) {
            return genes[index];
        }

        public void setGene(int index, int gene) {
            this.genes[index] = gene;
        }

        public void randomGenes() {
            Random rand = new Random();
            for(int i = 0; i < genes.length; i++) {
                this.setGene(i, rand.nextInt(processorsNumber));
            }
        }

        public void mutate() {
            Random rand = new Random();
            int index = rand.nextInt(genes.length);
            //trick for generating new processor for a certain task 
            this.setGene(index, (getGene(index) + rand.nextInt(processorsNumber - 1) + 1) % processorsNumber);   
        }

        public int calculateFitness() {
            int [] schedulingTimes = new int[processorsNumber];
            for(int i = 0; i < genes.length; i++) {
                schedulingTimes[getGene(i)] += processDuration.get(i);
            }
            //fitness for individual is the maximal scheduling time for one of the processors
            int max = 0;
            for (int i = 0; i < schedulingTimes.length; i++)
            	if (schedulingTimes[i] > max)
            		max = schedulingTimes[i];
            
            this.setFitnessValue(max);
            return max;
        }

		@Override
		public int compareTo(Individual o) {
			//a "better" individual has shorter execution time
			return fitnessValue > o.getFitnessValue() ? 1 : (fitnessValue < o.getFitnessValue() ? -1 : 0);
		}
		
		public boolean equals(Object o) {
		    if(!(o instanceof Individual)) 
		        return false;
		    //individuals are equal only when all the task assignments are the same
		    for (int i = 0; i < genes.length; i++)
		    	if (genes[i] != ((Individual)o).getGene(i))
		    		return false;
		    
		    return true;
		}
    }
}