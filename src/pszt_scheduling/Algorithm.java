package pszt_scheduling;

import java.util.ArrayList;

public interface Algorithm {
	public void readData();
	public void readData(ArrayList<Integer> arrayList);
	public Solution calculateSolution();
}
