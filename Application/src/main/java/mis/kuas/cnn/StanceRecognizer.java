package mis.kuas.cnn;

import java.util.ArrayList;
import java.util.List;

import mis.kuas.data.StanceDataCallbackListener;
import mis.kuas.data.StanceDataGetter;

/**
 * Created by mingjia on 2016/3/2.
 */
public abstract class StanceRecognizer {

    public interface OnRecognizeListener {

        public void onRecognize(StanceType resultType);

    }

    public final static int DEFAULT_DATA_CONTAINER_SIZE = 16;

    public final static double[][] NORMALIZATION_RANGE = {
            {-2, 2}, {-2, 2}, {-2, 2}, {0, 255}, {0, 255}, {0, 255}, {0, 255},
            {-2, 2}, {-2, 2}, {-2, 2}, {0, 255}, {0, 255}, {0, 255}, {0, 255},
            {-2, 2}, {-2, 2}, {-2, 2}, {-2, 2}, {-2, 2}, {-2, 2}
    };

    private List<OnRecognizeListener> onRecognizeListenerList;

    protected DataContainer container;

    private StanceDataGetter dataGetter;

    private StanceDataCallbackListener dataCallbackListener = new StanceDataCallbackListener() {
        @Override
        public void onDataCallback() {
            double[] data = dataGetter.getAllData();
            container.add(data);
            if (container.isFull()) {
                pushData();
                container.removeFromFirst(4);
            }
        }

    };

    public StanceRecognizer(StanceDataGetter dataGetter) {
        this(dataGetter, DEFAULT_DATA_CONTAINER_SIZE);
    }

    public StanceRecognizer(StanceDataGetter dataGetter, int dataContainerSize) {
        setStanceDataGetter(dataGetter);
        this.container = new DataContainer(dataContainerSize);
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

    public boolean addOnRecognizeListener(OnRecognizeListener listener) {
        return this.onRecognizeListenerList.add(listener);
    }

    public boolean removeOnRecognizeListener(OnRecognizeListener listener) {
        return this.onRecognizeListenerList.remove(listener);
    }

    protected void notifyAllOnRecognizeListener(StanceType resultType) {
        for (OnRecognizeListener listener : this.onRecognizeListenerList) {
            listener.onRecognize(resultType);
        }
    }

    public void destroy() {
        this.dataGetter.removeDataCallbackListener(dataCallbackListener);
        this.onRecognizeListenerList.clear();
    }

    protected abstract void pushData();
}
