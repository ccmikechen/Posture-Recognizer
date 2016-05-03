package mis.kuas.hmm;

import java.io.IOException;
import java.util.Arrays;

public class HMMHelper {
	
	public static double[][] normalize(double[][] data, double[][] range) {
		double[][] result = new double[data.length][];
		for (int i = 0; i < data.length; i++) {
			result[i] = new double[data[i].length];
			for (int j = 0; j < data[i].length; j++) {
				double min = range[j][0];
				double max = range[j][1];
				result[i][j] = (data[i][j] - min) / (max - min);
			}
		}
		return result;
	}
	
	public static HMMRecognizeResult recognize(double[][] data, SingleStanceModel model) {
		int[] itemsList = model.getItemsList();
		HMMProbability[] transmats = model.getTransmats();
		HMMProbability[] obsmats = model.getObsmats();
		int numState = itemsList.length;
		int[] seq = codebook(data, model.getCenterOfFeatures());
		
		double[] loglik = new double[numState];
		for (int i = 0; i < numState; i++) {
			int itemsSize = Math.min(seq.length, itemsList[i]);
			int fromIndex = seq.length - itemsSize;
			int[] newSeq = Arrays.copyOfRange(seq, fromIndex, seq.length);
			loglik[i] = decode(newSeq, transmats[i], obsmats[i]);
		}
		int maxIndex = -1;
		double maxValue = Double.NaN;
		for (int i = 0; i < loglik.length; i++) {
			if (Double.isNaN(loglik[i])) {
				continue;
			}
			if ((loglik[i] > maxValue) || Double.isNaN(maxValue)) {
				maxValue = loglik[i];
				maxIndex = i;
			}
		}
		HMMRecognizeResult result = new HMMRecognizeResult(maxIndex, maxValue);
		return result;
	}
	
	public static int[] codebook(double[][] data, double[][] centerOfData) {
		int[] codebook = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			double[] rowData = data[i];
			double minDistance = 1000;
			for (int j = 0; j < centerOfData.length; j++) {
				double[] center = centerOfData[j];
				double val = 0.0;
				for (int k = 0; k < center.length; k++) {
					double dis = Math.pow(center[k] - rowData[k], 2);
					val += dis;
				}
				double distance = Math.sqrt(val);
				if (distance < minDistance) {
					minDistance = distance;
					codebook[i] = j;
				}
			}
		}
		return codebook;
	}
	
	public static double decode(int[] codebookSeq, 
			HMMProbability transmat, 
			HMMProbability obsmats) {
		
		int numSymbols = obsmats.getColumnLength();
		int[] seq = new int[codebookSeq.length + 1];
		System.arraycopy(codebookSeq, 0, seq, 1, codebookSeq.length);
		seq[0] = numSymbols + 1;
		
		int numStates = transmat.getRowLength();
		double[][] fs = new double[numStates][seq.length];
		fs[0][0] = 1;
		double[] s = new double[seq.length];
		s[0] = 1;
		for (int count = 1; count < seq.length; count++) {
			for (int state = 0; state < numStates; state++) {
				double sum = 0.0;
				for (int n = 0; n < fs.length; n++) {
					sum += fs[n][count - 1] * transmat.get(n, state);
				}
				
				fs[state][count] = obsmats.get(state, seq[count]) * sum;
			}
			
			double fsSum = 0.0;
			for (int n = 0; n < numStates; n++) {
				fsSum += fs[n][count];
			}
			
			s[count] = fsSum;
			for (int m = 0; m < numStates; m++) {
				fs[m][count] /= s[count];
			}
		}
		double result = 0.0;
		for (int i = 0; i < s.length; i++) {
			result += Math.log(s[i]);
		}
		return result;
	}

}
