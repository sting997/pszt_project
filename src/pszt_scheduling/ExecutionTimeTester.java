package pszt_scheduling;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by root on 21.01.17.
 */
public class ExecutionTimeTester {
    private static ArrayList<Algorithm> algorithmsList = new ArrayList<Algorithm>();
    private static ArrayList<ArrayList<Integer>> tasksList= new ArrayList<>();
    private static  int testsNum = 1000;

    private static  int taskNum= 200;
    private static int processor = 20000;
    private static int population =20;
    private static double mutationRate = 0.03;
    private static double surviveRate = 0.4;
    private static int iteration = 100;
    private static long averageTime;
    private static long startTime;
    private static long endTime;

    public static void main (String[] args){

        initializeAlghorithmsList();
        initializeTaskList();
        readDataTOAlghorithms();
        System.out.println("Rozpoczeto pomiar");
        startTime = System.nanoTime();
        for (int i =0 ; i<= testsNum;i++){
            algorithmsList.get(i).calculateSolution();
        }
        endTime = System.nanoTime();
        System.out.println("Zakonoczno pomiar");
        averageTime = (endTime - startTime) / testsNum;
        System.out.println("Liczba populacji= " + population + " czas: " + (averageTime) / 1_000_000 +"ms");
    }

    private static void initializeTaskList(){
        ArrayList<Integer> taskDurationList;
        for (int i =0; i<=testsNum;i++){
            taskDurationList = genarateList(taskNum,10);
            tasksList.add(taskDurationList);
        }
    }
    private static void initializeAlghorithmsList(){
        Algorithm algorithm;
        for(int i =0; i<=testsNum;i++) {
            algorithm = new SchedulingAlgorithm(processor, iteration, population, mutationRate, surviveRate);
            algorithmsList.add(algorithm);
        }
    }
    private static void readDataTOAlghorithms(){
        for(int i =0 ; i<=testsNum;i++) {
            algorithmsList.get(i).readData(tasksList.get(i));
        }
    }
    private static ArrayList<Integer> genarateList(int taskNum, int range) {
        Random radom = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i =0; i<taskNum;i++){
            int time =  radom.nextInt(range);
            list.add(time);
        }
        return list;
    }
}
