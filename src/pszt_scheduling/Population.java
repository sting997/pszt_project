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
	private double surviveRate;
	
    public Population(int processorsNumber, int populationSize, ArrayList<Integer> data, double surviveRate) {
		this.processorsNumber = processorsNumber;
    	this.populationSize = populationSize;
    	processDuration = new ArrayList<>(data);
        currentPopulation = new ArrayList<>();
        newPopulation = new ArrayList<>();
        this.surviveRate = surviveRate;
        
        createRandomPopulation();
    }

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
    
    public int calculateFitness() {
        this.totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            this.totalFitness += currentPopulation.get(i).calculateFitness();
        }
        Collections.sort(currentPopulation);
//        for (Individual i : currentPopulation)
//    		System.out.println("fitness " + i.getFitnessValue());
        return this.totalFitness;
    }

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
		int i = m_rand.nextInt(populationSize);
		currentPopulation.get(i).mutate();
	}
    
    
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
            this.setGene(index, (getGene(index) + rand.nextInt(processorsNumber - 1) + 1) % processorsNumber);   
        }

        public int calculateFitness() {
            int [] schedulingTimes = new int[processorsNumber];
            for(int i = 0; i < genes.length; i++) {
                schedulingTimes[getGene(i)] += processDuration.get(i);
            }
            
            int max = 0;
            for (int i = 0; i < schedulingTimes.length; i++)
            	if (schedulingTimes[i] > max)
            		max = schedulingTimes[i];
            
            this.setFitnessValue(max);
            return max;
        }

		@Override
		public int compareTo(Individual o) {
			return fitnessValue > o.getFitnessValue() ? 1 : (fitnessValue < o.getFitnessValue() ? -1 : 0);
		}
		
		public boolean equals(Object o) {
		    if(!(o instanceof Individual)) 
		        return false;
		    
		    for (int i = 0; i < genes.length; i++)
		    	if (genes[i] != ((Individual)o).getGene(i))
		    		return false;
		    
		    return true;
		}
    }
}