package mis.kuas.cnn;

import java.util.ArrayList;
import java.util.List;

public class DataContainer {

	private List<double[]> dataList;
	
	private int size;
	
	public DataContainer(int size) {
		this.size = size;
		dataList = new ArrayList<double[]>();
	}
	
	synchronized public int size() {
		return this.dataList.size();
	}
	
	synchronized public boolean add(float[] data) {
		double[] convertData = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			convertData[i] = data[i];
		}
		return this.add(convertData);
	}
	
	synchronized public boolean add(double[] data) {
		if (this.isFull()) {
			return false;
		}
		return this.dataList.add(data);
	}
	
	public boolean isFull() {
		return this.dataList.size() == this.size;
	}
	
	synchronized public double[][] getAllData() {
		double[][] result = new double[this.dataList.size()][];
		for (int i = 0; i < this.dataList.size(); i++) {
			result[i] = this.dataList.get(i);
		}
		return result;
	}
	
	synchronized public void removeFromFirst(int n) {
		for (int i = 0; i < n; i++) {
			this.dataList.remove(0);
		}
	}
	
}
