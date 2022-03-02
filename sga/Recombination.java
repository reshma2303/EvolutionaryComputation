package sga;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Recombination {

	/*
	 * Recombination of parents
	 * 
	 * @param parents_selected,crossoverMethod,probApplyCrossover
	 * 
	 * @return children_after_recombination
	 */
	public static List<String> recombination(List<Candidate> parents_selected, int crossoverMethod, Random rand,
			double probApplyCrossover) throws Exception {
		List<String> children_after_recombination = new ArrayList<>();
		String child1 = "";
		String child2 = "";
		String technique = "";
		if (crossoverMethod == 0) {
			technique = "Uniform Crossover";
			char[] parent1 = parents_selected.get(0).getBinary_rep().toCharArray();
			char[] parent2 = parents_selected.get(1).getBinary_rep().toCharArray();
			for (int i = 0; i < parent1.length; i++) {
				if (rand.nextDouble() < probApplyCrossover) {
					child1 += parent2[i];
					child2 += parent1[i];

				} else {
					child1 += parent1[i];
					child2 += parent2[i];
				}

			}

			children_after_recombination.add(child1);
			children_after_recombination.add(child2);
		}else if(crossoverMethod == -1) { //If recombination is not given , offsprings are identical to parents
			for(int i=0;i<parents_selected.size();i++) {
			
				children_after_recombination.add(parents_selected.get(i).getBinary_rep());
			}

  
		}else {
			throw new Exception("Recombination technique " + technique + " not implemented.");
		}

		return children_after_recombination;
	}

}

