package sga;
import java.util.List;

public class ReplacedOffsprings {

	List<Candidate> final_populatio_after_replacemnt;
	int best_fit;
	double avg_fit;
	int worst_fit;
	int best_child_index;
	String best_fit_string;
	String worst_fit_string;

	public String getBest_fit_string() {
		return best_fit_string;
	}

	public void setBest_fit_string(String best_fit_string) {
		this.best_fit_string = best_fit_string;
	}

	public String getWorst_fit_string() {
		return worst_fit_string;
	}

	public void setWorst_fit_string(String worst_fit_string) {
		this.worst_fit_string = worst_fit_string;
	}

	public int getBest_child_index() {
		return best_child_index;
	}

	public void setBest_child_index(int best_child_index) {
		this.best_child_index = best_child_index;
	}

	public List<Candidate> getFinal_populatio_after_replacemnt() {
		return final_populatio_after_replacemnt;
	}

	public void setFinal_populatio_after_replacemnt(List<Candidate> final_populatio_after_replacemnt) {
		this.final_populatio_after_replacemnt = final_populatio_after_replacemnt;
	}

	public int getBest_fit() {
		return best_fit;
	}

	public void setBest_fit(int best_fit) {
		this.best_fit = best_fit;
	}

	public double getAvg_fit() {
		return avg_fit;
	}

	public void setAvg_fit(double d) {
		this.avg_fit = d;
	}

	public int getWorst_fit() {
		return worst_fit;
	}

	public void setWorst_fit(int worst_fit) {
		this.worst_fit = worst_fit;
	}

}

