package mis.kuas.hmm;

public class StanceModel {

	private SingleStanceModel[] modelArray;
	
	StanceModel(SingleStanceModel[] modelArray) {
		this.modelArray = modelArray;
	}
	
	public SingleStanceModel getSingleModel(int index) {
		return this.modelArray[index];
	}
	
	public int size() {
		return this.modelArray.length;
	}

	public int getMaxItems() {
		int maxItems = 0;
		for (SingleStanceModel model : modelArray) {
			for (int items : model.getItemsList()) {
				if (items > maxItems) {
					maxItems = items;
				}
			}
		}
		return maxItems;
	}

}
