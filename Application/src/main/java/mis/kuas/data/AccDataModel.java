package mis.kuas.data;

/**
 * Created by mingjia on 2016/2/14.
 */
public class AccDataModel implements AccDataGetter {

    private float x;

    private float y;

    private float z;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
