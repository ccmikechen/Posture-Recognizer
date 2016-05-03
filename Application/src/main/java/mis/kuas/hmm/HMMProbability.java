package mis.kuas.hmm;

public class HMMProbability {

	private double[][] probability;
	
	public HMMProbability(double[][] probability) {
		this.probability = probability;
	}
	
	public double[][] getProbability() {
		return this.probability.clone();
	}
	
	public double get(int row, int column) {
		return this.probability[row][column];
	}
	
	public int getRowLength() {
		return this.probability.length;
	}
	
	public int getColumnLength() {
		return this.probability[0].length;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < probability.length; i++) {
			for (int j = 0; j < probability[i].length; j++) {
				sb.append(probability[i][j])
					.append(", ");
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
}
