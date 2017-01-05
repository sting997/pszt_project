package pszt_scheduling;

import java.util.ArrayList;

public class Solution {
	private int solution;
	private ArrayList<Integer> solutionHistory = new ArrayList<>();
	
	public Solution(int solution, ArrayList<Integer> solutionHistory) {
		this.solution = solution;
		this.solutionHistory = solutionHistory;
	}
	
	public int getSolution() {
		return solution;
	}
	
	public ArrayList<Integer> getSolutionHistory() {
		return solutionHistory;
	}
}
