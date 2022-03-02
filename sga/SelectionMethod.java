package sga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectionMethod {

	/*
	 * Select a pair of parents
	 * 
	 * @param evaluated_population,tournamentSize,selectionMethod
	 * 
	 * @returns parents_selected
	 */
	public static List<Candidate> select_parents(List<Candidate> evaluated_population, Random rand, int tournamentSize,
			int selectionMethod) throws Exception {
		List<Candidate> parents_selected = new ArrayList<>();
		if (selectionMethod == 0) {
			Candidate parent1 = get_most_fit_individual_from_k_population(evaluated_population, rand, tournamentSize);
			parents_selected.add(parent1);
			Candidate parent2 = get_most_fit_individual_from_k_population(evaluated_population, rand, tournamentSize);
			parents_selected.add(parent2);
		} else {
			throw new Exception("Selection Method not implemented.");
		}
		return parents_selected;
	}

	/*
	 * Fetching most fit individual from K population
	 * 
	 * @param evaluated_population , tournamentSize
	 * 
	 * @returns most_fit_individual
	 */
	public static Candidate get_most_fit_individual_from_k_population(List<Candidate> evaluated_population, Random rand,
			int tournamentSize) {
		List<Candidate> k_candidates = new ArrayList<>();
		for (int i = 0; i < tournamentSize; i++) {
			k_candidates.add(evaluated_population.get(rand.nextInt(evaluated_population.size())));
		}

		return get_most_fit(k_candidates);
	}

	/*
	 * Fetching most fit individual from K population
	 * 
	 * @param k_candidates
	 * 
	 * @returns most_fit_candidate
	 */
	public static Candidate get_most_fit(List<Candidate> k_candidates) {
		Candidate most_fit_candidate = null;
		int max_fit = -1;
		for (Candidate cand : k_candidates) {
			if (cand.getFitness_value() > max_fit) {
				max_fit = cand.getFitness_value();
				most_fit_candidate = cand;
			}
		}
		return most_fit_candidate;
	}

}

