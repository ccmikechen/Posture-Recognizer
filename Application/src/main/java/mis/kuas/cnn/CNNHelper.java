package mis.kuas.cnn;

/**
 * Created by mingjia on 2016/10/6.
 */
public class CNNHelper {

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

}
