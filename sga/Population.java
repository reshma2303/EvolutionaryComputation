package sga;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Population {

	/*
	 * Initialize population
	 * 
	 * @param stringSize,populationSize- Size of population and each chromosome
	 * length
	 * 
	 * @return final_population Initial- population generated
	 */
	public static List<String> generate_population(int stringSize, int populationSize, Random rand) {

		List<String> final_population = new ArrayList<>();
		int counter = 0;
		while (counter < populationSize) {
			String curr_binary_str = generate_binary_string(stringSize, rand);
			if (!final_population.contains(curr_binary_str)) {
				final_population.add(curr_binary_str);
				counter++;

			}

		}

		return final_population;
	}

	/*
	 * Binary string generation
	 * 
	 * @param stringSize - chromosome length
	 * 
	 * @returns binary_string
	 */
	public static String generate_binary_string(int stringSize, Random rand) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < stringSize; i++) {
			sb.append(uniform_binary_distribution(rand));

		}
		return sb.toString();

	}

	/*
	 * Uniform Binary Distribution method to generate even or odd numbers with equal
	 * probability
	 * 
	 * @returns int
	 */
	public static int uniform_binary_distribution(Random rand) {

		return (int) (10 * rand.nextDouble()) & 1;
	}

	/*
	 * Evaluate population
	 * 
	 * @param final_population,fitnessFunction -Initial population generated and the
	 * fitness function
	 * 
	 * @returns evaluated_population- population evaluated using fitness function
	 */
	public static List<Candidate> evaluate_candidates(List<String> final_population,
			Map <String,Integer> string_fitness_map, int fitnessFunction) throws Exception {
		List<Candidate> initialized_population = new ArrayList<>();
		String technique = "";
		if (fitnessFunction == 0) {
			technique = "One Max";
			for (String str : final_population) {

				// Check if the fitness value is already computed.If yes , no need to compute
				// again.

				int fitness=string_fitness_map.getOrDefault(str, -1);
				if (fitness==-1) {
					Candidate cand = new Candidate(str);
					cand.setIs_evaluated(true);
					cand.setFitness_value(onemax_fitness_value(str));
					initialized_population.add(cand);
					string_fitness_map.put(str, cand.getFitness_value());
				}else {
					Candidate cand = new Candidate(str);
					cand.setIs_evaluated(true);
					cand.setFitness_value(fitness);
					initialized_population.add(cand);
				}

			}

		} else {
			throw new Exception("Fitness technique " + technique + " not implemented.");
		}

		return initialized_population;
	}

	/*
	 * One Max Fitness value
	 * 
	 * @param candidate_bianry_rep - chromosome of individual
	 * 
	 * @returns fitness_value -using one max
	 */
	public static int onemax_fitness_value(String candidate_bianry_rep) {
		int[] chars = candidate_bianry_rep.chars().map(x -> x - '0').toArray();

		return Arrays.stream(chars).sum();

	}

	/*
	 * Best Parent of each generation
	 * 
	 * @param initialized_population - List of population
	 * 
	 * @returns best_fit_parent - Candidate with maximum fitness value
	 * 
	 */
	public static Candidate best_parent(List<Candidate> initialized_population) {

		Candidate best_fit_parent = null;
		int best_parent_fit_val = -1;
		for (Candidate cand : initialized_population) {
			if (cand.getFitness_value() > best_parent_fit_val) {
				best_parent_fit_val = cand.getFitness_value();
				best_fit_parent = cand;
			}

		}
		return best_fit_parent;
	}

}
