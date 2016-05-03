package mis.kuas.chart;

public class KalmanFilter {

    private static final float EXP_DIFF = 1.0f;

    private float lastValue;

    private float lastRealDiff;

    public KalmanFilter() {
        this.lastValue = 0.0f;
        this.lastRealDiff = 0.0f;
    }

    public float getRealValue(float measuredValue) {
        float preDiff = (float) (Math.sqrt(Math.pow(lastRealDiff, 2) + Math.pow(EXP_DIFF, 2)));
        float kg = getKG(preDiff);
        float realValue = lastValue + kg * (measuredValue - lastValue);
        lastRealDiff = (float) (Math.sqrt((1 - kg) * Math.pow(preDiff, 2)));
        lastValue = realValue;
        return realValue;
    }

    private float getKG(float preDiff) {
        return (float) (Math.sqrt(Math.pow(preDiff, 2) /
                (Math.pow(preDiff, 2) + Math.pow(EXP_DIFF, 2))));
    }

}
