package sga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Replacement {
	/*
	 * Fetching final population after replacing best fit parent with worst fit
	 * offspring and best,worst and average fitness of the generation
	 * 
	 * @param offSpring_population,best_fit_parent
	 * 
	 * @returns final_population_after_replacemnt
	 */

	public static ReplacedOffsprings replacement_of_parents_with_offsprings(List<Candidate> offSpring_population,
			Candidate best_fit_parent, int replacementMethod) throws Exception {
		ReplacedOffsprings replacedOffsprings = new ReplacedOffsprings();

		if (replacementMethod == 0) {

			replacedOffsprings = elitism_replacement(offSpring_population, best_fit_parent);

		}else if(replacementMethod== -1) {
			replacedOffsprings=elitism_replacement(offSpring_population, best_fit_parent);
			replacedOffsprings.setFinal_populatio_after_replacemnt(offSpring_population);
		}else {
			throw new Exception("Replacement method not implemented");
		}

		return replacedOffsprings;

	}

	public static ReplacedOffsprings elitism_replacement(List<Candidate> offSpring_population,
			Candidate best_fit_parent) {
		ReplacedOffsprings replacedOffsprings = new ReplacedOffsprings();
		List<Integer> off_springs_fit_values = new ArrayList<>();
		int best_child_fit = -1;
		int worst_child_fit = 99999999;
		int worst_child_index = -1;
		int best_child_index = -1;
		String best_fit_string = "";
		String worst_fit_string = "";
		for (int i = 0; i < offSpring_population.size(); i++) {
			if (offSpring_population.get(i).getFitness_value() < worst_child_fit) {
				worst_child_fit = offSpring_population.get(i).getFitness_value();
				worst_child_index = i;
			}
			if (offSpring_population.get(i).getFitness_value() > best_child_fit) {
				best_child_fit = offSpring_population.get(i).getFitness_value();
				best_child_index = i;
				best_fit_string = offSpring_population.get(i).getBinary_rep();
			}
		}
		// Removing the worst fit offspring from the offsprings
		offSpring_population.remove(worst_child_index);

		// Adding the best fit parent to the offsprings
		offSpring_population.add(best_fit_parent);
		for (Candidate cand : offSpring_population) {
			off_springs_fit_values.add(cand.getFitness_value());
		}

		int min_fitness = Collections.min(off_springs_fit_values);

		List<Candidate> worst_Strings = offSpring_population.stream()
				.filter(a -> Objects.equals(a.fitness_value, min_fitness)).collect(Collectors.toList());
		replacedOffsprings.setWorst_fit_string(worst_Strings.get(0).getBinary_rep());
		replacedOffsprings.setFinal_populatio_after_replacemnt(offSpring_population);
		replacedOffsprings.setBest_fit(Collections.max(off_springs_fit_values));
		replacedOffsprings.setWorst_fit(min_fitness);
		replacedOffsprings.setAvg_fit(off_springs_fit_values.stream().mapToInt(a -> a).average().orElse(0));
		replacedOffsprings.setBest_child_index(best_child_index);
		replacedOffsprings.setBest_fit_string(best_fit_string);

		return replacedOffsprings;
	}

}
