package mis.kuas.cnn;

import android.content.Context;

import mis.kuas.data.StanceDataGetter;

/**
 * Created by kuasmis on 16/9/21.
 */
public class CNNStanceRecognizer extends StanceRecognizer {

    private boolean isConnected = false;

    private Context context;

    private OnRecognizeListener onRecognizeListener = new OnRecognizeListener() {
        @Override
        public void onRecognize(StanceType resultType) {
            notifyAllOnRecognizeListener(resultType);
        }
    };

    private CNNServerConnector serverConnector;

    public CNNStanceRecognizer(StanceDataGetter dataGetter, int dataContainerSize, Context context) {
        super(dataGetter, dataContainerSize);
        this.context = context;
    }

    public void connectToServer(String url) {
        this.serverConnector = new CNNServerConnector(url, this.context);

        this.serverConnector.connect();
        this.serverConnector.setOnRecognizeListener(this.onRecognizeListener);
    }

    protected void pushData() {
        double[][] allData = container.getAllData();
        double[][] normalizedData = CNNHelper.normalize(allData, NORMALIZATION_RANGE);
        serverConnector.sendSensorData(normalizedData);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.serverConnector.disconnect();
    }

}
