package sga;

public class Candidate {
	String binary_rep;
	int fitness_value;
	boolean is_evaluated;

	public Candidate(String binary_rep) {

		this.binary_rep = binary_rep;
		this.fitness_value = 0;
		this.is_evaluated = false;
	}

	public String getBinary_rep() {
		return binary_rep;
	}

	public void setBinary_rep(String binary_rep) {
		this.binary_rep = binary_rep;
	}

	public int getFitness_value() {
		return fitness_value;
	}

	public void setFitness_value(int fitness_value) {
		this.fitness_value = fitness_value;
	}

	public boolean isIs_evaluated() {
		return is_evaluated;
	}

	public void setIs_evaluated(boolean is_evaluated) {
		this.is_evaluated = is_evaluated;
	}

}