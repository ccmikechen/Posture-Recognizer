package mis.kuas.hmm;

import java.util.ArrayList;
import java.util.List;

import mis.kuas.data.StanceDataCallbackListener;
import mis.kuas.data.StanceDataGetter;

/**
 * Created by mingjia on 2016/3/2.
 */
public class StanceRecognizer {

    public interface OnRecognizeListener {

        public void onRecognize(HMMRecognizeResult result);

    }

    public final static double[][] NORMALIZATION_RANGE = {
            {-2, 2}, {-2, 2}, {-2, 2}, {0, 255}, {0, 255}, {0, 255}, {0, 255},
            {-2, 2}, {-2, 2}, {-2, 2}, {0, 255}, {0, 255}, {0, 255}, {0, 255},
            {-2, 2}, {-2, 2}, {-2, 2}, {-2, 2}, {-2, 2}, {-2, 2}
    };

    private List<OnRecognizeListener> onRecognizeListenerList;

    private StanceModel stanceModel;
    private DataContainer container;

    private StanceDataGetter dataGetter;

    private StanceDataCallbackListener dataCallbackListener = new StanceDataCallbackListener() {
        @Override
        public void onDataCallback() {
            double[] data = dataGetter.getAllData();
            container.add(data);
            if (container.isFull()) {
                HMMRecognizeResult result = recognize();
                notifyAllOnRecognizeListener(result);
                container.removeFromFirst(4);
            }
        }

    };

    public StanceRecognizer(StanceModel stanceModel, StanceDataGetter dataGetter) {
        this(stanceModel);
        setStanceDataGetter(dataGetter);
    }

    public StanceRecognizer(StanceModel stanceModel) {
        this.stanceModel = stanceModel;
        this.container = new DataContainer(stanceModel.getMaxItems());
        this.onRecognizeListenerList = new ArrayList<OnRecognizeListener>();
    }

    public void setStanceDataGetter(StanceDataGetter dataGetter) {
        removeStanceDataGetter();
        this.dataGetter = dataGetter;
        dataGetter.addDataCallbackListener(this.dataCallbackListener);
    }

    public void removeStanceDataGetter() {
        if (this.dataGetter == null) {
            return;
        }
        this.dataGetter.removeDataCallbackListener(this.dataCallbackListener);
    }

    private HMMRecognizeResult recognize() {
        double[][] allData = container.getAllData();
        double[][] normalizedData = HMMHelper.normalize(allData, NORMALIZATION_RANGE);
        HMMRecognizeResult result = HMMRecognizeResult.UNKNOWN_RESULT;
        for (int i = 0; i < stanceModel.size(); i++) {
            SingleStanceModel singleStanceModel = stanceModel.getSingleModel(i);
            HMMRecognizeResult recognizeResult =
                    HMMHelper.recognize(normalizedData, singleStanceModel);
            if (recognizeResult.isUnknown()) {
                continue;
            }
            if (result.isUnknown() || (recognizeResult.getValue() > result.getValue())) {
                result = recognizeResult;
            }
        }
        return result;
    }

    public boolean addOnRecognizeListener(OnRecognizeListener listener) {
        return this.onRecognizeListenerList.add(listener);
    }

    public boolean removeOnRecognizeListener(OnRecognizeListener listener) {
        return this.onRecognizeListenerList.remove(listener);
    }

    public void notifyAllOnRecognizeListener(HMMRecognizeResult result) {
        for (OnRecognizeListener listener : this.onRecognizeListenerList) {
            listener.onRecognize(result);
        }
    }

    public void destroy() {
        this.dataGetter.removeDataCallbackListener(dataCallbackListener);
        this.onRecognizeListenerList.clear();
    }
}
