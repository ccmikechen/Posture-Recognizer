package mis.kuas.hmm;

import java.io.File;
import java.io.IOException;

import com.jmatio.io.*;
import com.jmatio.types.*;

public class StanceModelLoader {

	public static StanceModel loadFromMatFile(String path) throws IOException {
		return loadFromMatFile(new File(path));
	}
	
	public static StanceModel loadFromMatFile(File file) throws IOException {
		MatFileReader reader = new MatFileReader(file);
		MLCell modelCell = (MLCell) reader.getMLArray("model");
		SingleStanceModel[] modelArray = new SingleStanceModel[modelCell.getSize()];
		
		for (int i = 0; i < modelCell.getSize(); i++) {
			MLStructure modelStruct = (MLStructure) modelCell.get(i);
			SingleStanceModel singleStanceModel = new SingleStanceModel();
			
			MLChar mlFilePath = (MLChar) modelStruct.getField("file_path");
			String filePath = mlFilePath.getString(0);
			singleStanceModel.setFilePath(filePath);
			
			MLDouble mlItemsList = (MLDouble) modelStruct.getField("items_list");
			int[] intItemsList = new int[mlItemsList.getSize()];
			for (int j = 0; j < mlItemsList.getSize(); j++) {
				intItemsList[j] = (int) Math.round(mlItemsList.get(j));
			}
			singleStanceModel.setItemsList(intItemsList);
			
			MLDouble mlCenterOfFeatures = (MLDouble) modelStruct.getField("center_of_features");
			singleStanceModel.setCenterOfFeatures(mlCenterOfFeatures.getArray());
			
			MLCell mlTransmats = (MLCell) modelStruct.getField("transmats");
			HMMProbability[] transmats = new HMMProbability[mlTransmats.getSize()];
			for (int j = 0; j < mlTransmats.getSize(); j++) {
				MLDouble mlTransmat = (MLDouble) mlTransmats.get(j);
				double[][] transmat = mlTransmat.getArray();
				transmats[j] = new HMMProbability(transmat);
			}
			singleStanceModel.setTransmats(transmats);
			
			MLCell mlObsmats = (MLCell) modelStruct.getField("obsmats");
			HMMProbability[] obsmats = new HMMProbability[mlObsmats.getSize()];
			for (int j = 0; j < mlObsmats.getSize(); j++) {
				MLDouble mlObsmat = (MLDouble) mlObsmats.get(j);
				double[][] obsmat = mlObsmat.getArray();
				obsmats[j] = new HMMProbability(obsmat);
			}
			singleStanceModel.setObsmats(obsmats);
			
			modelArray[i] = singleStanceModel;
		}
		
		return new StanceModel(modelArray);
	}
	
}
