package sga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutation {

	/*
	 * Mutating the resulting offsprings
	 * 
	 * @param children_after_recombination,mutationMethod,probApplyMutation
	 * 
	 * @returns mutated_offsprings
	 */
	public static List<String> mutated_offSpring(List<String> children_after_recombination, int mutationMethod,
			Random rand, double probApplyMutation) throws Exception {

		List<String> mutated_offsprings = new ArrayList<>();
		String mutated_child1 = "";
		String mutated_child2 = "";
		String technique = "";
		if (mutationMethod == 0) {
			technique = "Bit Flip";
			char[] child1 = children_after_recombination.get(0).toCharArray();
			char[] child2 = children_after_recombination.get(1).toCharArray();
			for (int i = 0; i < child1.length; i++) {
				double d = rand.nextDouble();
				if (d < probApplyMutation) {
					if (child1[i] == '0') {
						mutated_child1 += "1";
					} else {
						mutated_child1 += "0";
					}

					if (child2[i] == '0') {
						mutated_child2 += "1";
					} else {
						mutated_child2 += "0";
					}
				} else {
					mutated_child1 += child1[i];
					mutated_child2 += child2[i];
				}
			}

			mutated_offsprings.add(mutated_child1);
			mutated_offsprings.add(mutated_child2);
		} else if(mutationMethod == -1) {//If mutation is not given , offsprings are identical to the one produced from recombination
			
			mutated_offsprings.addAll(children_after_recombination);
  
		}else {
			throw new Exception("Mutation technique " + technique + " not implemented.");
		}
		return mutated_offsprings;
	}

}

