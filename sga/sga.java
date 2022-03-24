package sga;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class sga {

	public static void main(String[] args) throws Exception {

		String file_name = "gasettings.dat";
		String arg = "no flags";
		if (args.length == 1) {
			arg = args[0];
		} else if (args.length == 2) {
			arg = args[0];
			file_name = args[1];
		}

		if (arg.equalsIgnoreCase("-h")) {
			System.out.println(
					"\n\nA Simple Genetic Algorithm Script that builds the best generation using fitness \"onemax\" and \"trap-4\" function. Bisection can be used in"
					+ "order to get the minimum size of population that could solve the problem.The steps performed are Selection, Recombination,"
							+ "Mutation and Replacement (iteratively)\n" + "\n"
							+ "-g : Limited debugging information will be displayed while running.  The output will contain the best and worst strings "
							+ "(duplicates do not matter) and the entire population for each population size of N using bisection and"
							+ " also success and failure cases.\n" + "\n"
							+ "-G : Full debugging will be displayed while running. For example, for each population size of N, showing the "
							+ "individual parents selected, then the result after recombination. Mutation would also show "
							+ "individuals before and after mutation and whether it is success or failure.");
		} else {
			int populationSize = 0;
			int stringSize = 0;
			long randSeed = 0;
			int tournamentSize = 0;
			double probApplyCrossover = 0.0;
			double probApplyMutation = 0.0;
			int selectionMethod = -1;
			int fitnessFunction = -1;
			int crossoverMethod = -1;
			int mutationMethod = -1;
			int replacementMethod = -1;
			int bisection = -1;
			boolean successFlag=false;
			int success_generations=0;

			/*
			 * Reading the settings file
			 */
			File f = new File(file_name);
			
//		String absolute = f.getAbsolutePath();
//		File file = new File(absolute);
			Scanner sc = new Scanner(f);

			while (sc.hasNextLine()) {
				String[] splited = sc.nextLine().split(" ");
				if (splited[0].contains("populationSizeN")) {
					populationSize = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("stringSizen")) {
					stringSize = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("randSeed")) {
					randSeed = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("tournamentSizek")) {
					tournamentSize = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("probApplyCrossover")) {
					probApplyCrossover = Double.parseDouble(splited[1]);
				} else if (splited[0].contains("probApplyMutation")) {
					probApplyMutation = Double.parseDouble(splited[1]);
				} else if (splited[0].contains("selectionMethod")) {
					selectionMethod = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("fitnessFunction")) {
					fitnessFunction = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("crossoverMethod")) {
					crossoverMethod = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("mutationMethod")) {
					mutationMethod = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("replacementMethod")) {
					replacementMethod = Integer.parseInt(splited[1]);
				} else if (splited[0].contains("bisection")) {
					bisection = Integer.parseInt(splited[1]);
				}else if (splited[0].contains("bisection")) {
					bisection = Integer.parseInt(splited[1]);
				}else if (splited[0].contains("generations")) {
					success_generations = Integer.parseInt(splited[1]);
				}
			}

			Random rand = new Random();
			rand.setSeed(randSeed);

			
			// If bisection is 0 : single run
			if (bisection == 0) {
				bisection(populationSize, stringSize, tournamentSize, probApplyCrossover, probApplyMutation,
						selectionMethod, fitnessFunction, crossoverMethod, mutationMethod, replacementMethod, arg,
						rand,successFlag,success_generations);
				
			// If bisection is 1 : finding minimum population required solve the problem	
			}else if(bisection ==1 ) {
				
				//Starting small size population
				int N=10;

				//Whether population size tried to solve the problem : success or not
				while(!successFlag) {
					N=N*2;
					successFlag=bisection(N, stringSize, tournamentSize, probApplyCrossover, probApplyMutation,
							selectionMethod, fitnessFunction, crossoverMethod, mutationMethod, replacementMethod, arg,
							rand,successFlag,success_generations);
					if(successFlag) {
						System.out.println("N = "+N+" : Success");
					}else {
						System.out.println("N = "+N+" : Failure");
					}
				}
				double min=N/2;
				double max=N;
				double threshold=(max-min)/min;
				int min_population_size_required=N;
				while(threshold >= 0.1) {
					System.out.println("\n");
					System.out.println("min = "+min);
					System.out.println("max = "+max);
					System.out.println("\n");
					N=(int) ((min+max)/2);
					successFlag=bisection(N, stringSize, tournamentSize, probApplyCrossover, probApplyMutation,
							selectionMethod, fitnessFunction, crossoverMethod, mutationMethod, replacementMethod, arg,
							rand,successFlag,success_generations);
					if(successFlag) {
						System.out.println("N = "+N+" : Success");
						max=N;
						min_population_size_required=N;
					}else {
						System.out.println("N = "+N+" : Failure");
						min=N;
					}
					threshold=(max-min)/min;
					
				}
				
				System.out.println("Minimum population required (N) :"+min_population_size_required);
				
			}

		}

	}

	
	
	public static boolean bisection(int populationSize, int stringSize, int tournamentSize, double probApplyCrossover,
			double probApplyMutation, int selectionMethod, int fitnessFunction, int crossoverMethod, int mutationMethod,
			int replacementMethod, String arg, Random rand,boolean successFlag,int success_generations) throws Exception {

		// Keep track of generation
		int generation_id = 1;

		// Keep track of string and fitness values using a map
		Map<String, Integer> string_fitness_map = new HashMap<>();

		// Keep track of best and average fitness of previous three generations
		List<Integer> previous_generations_bests = new ArrayList<>();
		List<Integer> previous_generations_avgs = new ArrayList<>();

		// Check for termination conditions
		boolean termination_flag = false;
		boolean check_if_best_case_does_not_improve = false;
		boolean check_if_avg_case_does_not_improve = false;

		/*
		 * Initialize population
		 * 
		 * @param stringSize,populationSize- Size of population and each chromosome
		 * length
		 * 
		 * @return final_population Initial- population generated
		 */
		List<String> final_population = Population.generate_population(stringSize, populationSize, rand);

		/*
		 * Evaluate population
		 * 
		 * @param final_population,fitnessFunction -Initial population generated and the
		 * fitness function
		 * 
		 * @returns evaluated_population- population evaluated using fitness function
		 */
		List<Candidate> evaluated_population = Population.evaluate_candidates(final_population, string_fitness_map,
				fitnessFunction);
		if (arg.equalsIgnoreCase("-g") || arg.equalsIgnoreCase("-G")) {
			System.out.println("\nPOPULATION AFTER EVALUATION:");
			for (int i = 0; i < evaluated_population.size(); i++) {
				System.out.println("Candidate: " + evaluated_population.get(i).getBinary_rep() + "-> Fitness: "
						+ evaluated_population.get(i).getFitness_value());
			}
		}

		/*
		 * Termination condition - check if best and average fitness doesnot improve in
		 * the previous 3 generations
		 */
		while (!termination_flag) {

			// Keep track of best fit parent in each generation
			Candidate best_fit_parent = Population.best_parent(evaluated_population);
			if (arg.equals("-G")) {
				System.out.println("\nBEST FIT PARENT :" + best_fit_parent.getBinary_rep() + " -> "
						+ best_fit_parent.getFitness_value());
			}
			List<Candidate> offSpring_population = new ArrayList<>();

			/*
			 * Termination condition - check if enough offsprings are produced
			 */
			while (offSpring_population.size() < populationSize) {

				/*
				 * Select a pair of parents
				 * 
				 * @param evaluated_population,tournamentSize,selectionMethod
				 * 
				 * @returns parents_selected
				 */
				List<Candidate> parents_selected = SelectionMethod.select_parents(evaluated_population, rand,
						tournamentSize, selectionMethod);

				if (arg.equals("-G")) {
					System.out.println("\n \nSELECTED PARENTS :");
					for (int i = 0; i < parents_selected.size(); i++) {
						System.out.println("Parent " + (i + 1) + ": " + parents_selected.get(i).getBinary_rep()
								+ "-> Fitness: " + parents_selected.get(i).getFitness_value());
					}
				}

				/*
				 * Recombination of parents
				 * 
				 * @param parents_selected,crossoverMethod,probApplyCrossover
				 * 
				 * @return children_after_recombination
				 */
				List<String> children_after_recombination = Recombination.recombination(parents_selected,
						crossoverMethod, rand, probApplyCrossover, stringSize);

				if (arg.equals("-G")) {
					System.out.println("\n \nAFTER  RECOMBINATION :");
					for (int i = 0; i < children_after_recombination.size(); i++) {
						System.out.println("Child " + (i + 1) + ": " + children_after_recombination.get(i));
					}
				}

				/*
				 * Mutating the resulting offsprings
				 * 
				 * @param children_after_recombination,mutationMethod,probApplyMutation
				 * 
				 * @returns mutated_offsprings
				 */
				List<String> mutated_offsprings = Mutation.mutated_offSpring(children_after_recombination,
						mutationMethod, rand, probApplyMutation);

				if (arg.equals("-G")) {
					System.out.println("\n \nAFTER  MUTATION :");
					for (int i = 0; i < mutated_offsprings.size(); i++) {
						System.out.println("Mutated Child " + (i + 1) + ": " + mutated_offsprings.get(i));
					}
				}

				/*
				 * Evaluate population
				 * 
				 * @param final_population,fitnessFunction -Initial population generated and the
				 * fitness function
				 * 
				 * @returns evaluated_population- population evaluated using fitness function
				 */
				List<Candidate> evaluated_offsprings = Population.evaluate_candidates(mutated_offsprings,
						string_fitness_map, fitnessFunction);

				if (arg.equals("-G")) {
					System.out.println("\n \nEVALUATED OFFSPRINGS :");
					for (int i = 0; i < evaluated_offsprings.size(); i++) {
						System.out.println("Child " + (i + 1) + ": " + evaluated_offsprings.get(i).getBinary_rep()
								+ " -> Fitness: " + evaluated_offsprings.get(i).getFitness_value());
					}
				}

				/*
				 * Adding new candidates to the offspring population
				 */
				offSpring_population.addAll(evaluated_offsprings);

				if (arg.equals("-G")) {
					System.out.println("\n \n OFFSPRING POPULATION :");
					for (int i = 0; i < offSpring_population.size(); i++) {
						System.out.println("Child " + (i + 1) + ": " + offSpring_population.get(i).getBinary_rep()
								+ " -> Fitness: " + offSpring_population.get(i).getFitness_value());
					}
				}

			}

			/*
			 * Fetching final population after replacing best fit parent with worst fit
			 * offspring and best,worst and average fitness of the generation
			 * 
			 * @param offSpring_population,best_fit_parent
			 * 
			 * @returns final_population_after_replacemnt
			 */
			ReplacedOffsprings replacedOffsprings=Replacement
					.replacement_of_parents_with_offsprings(offSpring_population, best_fit_parent, replacementMethod);
			List<Candidate> final_population_after_replacemnt = replacedOffsprings.getFinal_populatio_after_replacemnt();

			/*
			 * Replacing offsprings with parents for next generation
			 */
			evaluated_population = final_population_after_replacemnt;

			// Fetching best , worst and average fitness of the generation

			int best=replacedOffsprings.getBest_fit();
			int avg = (int) replacedOffsprings.getAvg_fit();
			int worst= replacedOffsprings.getWorst_fit();

			
			// check if best and average fitness doesnot improve in the previous 3
			// generations
			if(success_generations==0) {
			if (previous_generations_bests.size() < 5) {
				previous_generations_bests.add(best);
				previous_generations_avgs.add(avg);
				if (previous_generations_bests.size() == 4) {
					check_if_best_case_does_not_improve = check_if_best_case_does_not_improve(
							previous_generations_bests);
					check_if_avg_case_does_not_improve = check_if_avg_case_does_not_improve(previous_generations_avgs);

				}
			} else {
				previous_generations_bests.remove(0);
				previous_generations_bests.add(best);
				previous_generations_avgs.remove(0);
				previous_generations_avgs.add(avg);
			}

			if (check_if_best_case_does_not_improve && check_if_avg_case_does_not_improve) {
				System.out.println(
						"\n\nBest fitness and average fitness has not improved in three generations and hence we are terminating.");
				System.out.println("\n\nBest String: "
						+ replacedOffsprings.getBest_fit_string()
						+ " Worst String : " + replacedOffsprings.getWorst_fit_string());
				previous_generations_bests.remove(previous_generations_bests.size() - 1);
				previous_generations_avgs.remove(previous_generations_avgs.size() - 1);
				System.out.println(
						"\nPrevious generations bests: " + Arrays.toString(previous_generations_bests.toArray()));
				System.out.println(
						"\nPrevious generations avgs: " + Arrays.toString(previous_generations_avgs.toArray()));
				termination_flag = true;
				break;
			}
			
		

			// If global fitness is achieved in this generation
			if (best == stringSize) {
				System.out
						.println("\n\nGlobal Fitness of " + best + " is achieved at location: "
								+ replacedOffsprings.getBest_child_index()
								+ " after " + generation_id + " generations");
				System.out.println("\n\nAverage Fitness : " + avg + ", Worst Fitness : " + worst);
				break;
			}

			System.out.println(
					"\n\nGeneration " + generation_id + " : ( B: " + best + ", A: " + avg + ", W: " + worst + ")");
			generation_id++;
			 if(arg.equalsIgnoreCase("-g") || arg.equalsIgnoreCase("-G")) {
			System.out.println("Best String: "
					+ replacedOffsprings.getBest_fit_string()
					+ " Worst String : " + replacedOffsprings.getWorst_fit_string());

			 }
			}else{
				
				if(generation_id > success_generations) {
					termination_flag=true;
					successFlag = false;
				}
				else if(best == stringSize && generation_id <= success_generations) {
					successFlag = true;
					
					return successFlag;
				}

				generation_id++;
				if(arg.equalsIgnoreCase("-g") || arg.equalsIgnoreCase("-G")) {
					System.out.println(
					"\n\nGeneration " + generation_id + " : ( B: " + best + ", A: " + avg + ", W: " + worst + ")");
//				System.out.println("Best String: "
//						+ replacedOffsprings.getBest_fit_string()
//						+ " Worst String : " + replacedOffsprings.getWorst_fit_string());

				 }
				
			}

		}
		return false;
	}

	/*
	 * check if best fitness doesnt improve compared to the previous 3 generations
	 * 
	 * @param previous_generations_bests
	 * 
	 * @returns boolean
	 */
	public static boolean check_if_best_case_does_not_improve(List<Integer> previous_generations_bests) {
		if (previous_generations_bests.get(3) > previous_generations_bests.get(0)
				&& previous_generations_bests.get(3) > previous_generations_bests.get(1)
				&& previous_generations_bests.get(3) > previous_generations_bests.get(2)) {
			return false;
		}
		return true;

	}

	/*
	 * check if average fitness doesnt improve compared to the previous 3
	 * generations
	 * 
	 * @param previous_generations_avgs
	 * 
	 * @returns boolean
	 */
	public static boolean check_if_avg_case_does_not_improve(List<Integer> previous_generations_avgs) {
		if (previous_generations_avgs.get(3) > previous_generations_avgs.get(0)
				&& previous_generations_avgs.get(3) > previous_generations_avgs.get(1)
				&& previous_generations_avgs.get(3) > previous_generations_avgs.get(2)) {
			return false;
		}
		return true;

	}

}
