package mis.kuas.hmm;

import java.util.List;

public class SingleStanceModel {

	private String filePath;
	
	private int[] itemsList;
	
	private double[][] centerOfFeatures;
	
	private HMMProbability[] transmats;
	
	private HMMProbability[] obsmats;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int[] getItemsList() {
		return itemsList;
	}

	public void setItemsList(int[] itemsList) {
		this.itemsList = itemsList;
	}

	public double[][] getCenterOfFeatures() {
		return centerOfFeatures;
	}

	public void setCenterOfFeatures(double[][] centerOfFeatures) {
		this.centerOfFeatures = centerOfFeatures;
	}

	public HMMProbability[] getTransmats() { 
		return transmats;
	}

	public void setTransmats(HMMProbability[] transmats) {
		this.transmats = transmats;
	}

	public HMMProbability[] getObsmats() {
		return obsmats;
	}

	public void setObsmats(HMMProbability[] obsmats) {
		this.obsmats = obsmats;
	}

	
	
}
