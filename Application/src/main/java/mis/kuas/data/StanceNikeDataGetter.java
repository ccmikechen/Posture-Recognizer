package mis.kuas.data;

import mis.kuas.sensor.Direction;

/**
 * Created by mingjia on 2016/3/3.
 */
public class StanceNikeDataGetter extends NikeDataGetter implements StanceDataCallbackListener {

    private StanceDataGetter stanceDataGetter;

    private InsoleDataGetter insoleDataGetter;

    private Direction direction;

    public StanceNikeDataGetter(StanceDataGetter stanceDataGetter, Direction direction) {
        super(direction);
        if (direction == Direction.LEFT) {
            this.insoleDataGetter = stanceDataGetter.getLeftInsoleDataGetter();
        } else {
            this.insoleDataGetter = stanceDataGetter.getRightInsoleDataGetter();
        }
        this.direction = direction;
        this.stanceDataGetter = stanceDataGetter;
        stanceDataGetter.addDataCallbackListener(this);
    }

    @Override
    public void onDataCallback() {
        this.x = this.insoleDataGetter.x;
        this.y = this.insoleDataGetter.y;
        this.z = this.insoleDataGetter.z;
        this.pa = this.insoleDataGetter.a;
        if (this.direction == Direction.LEFT) {
            this.pb = this.insoleDataGetter.b;
            this.pc = this.insoleDataGetter.c;
        } else {
            this.pb = this.insoleDataGetter.c;
            this.pc = this.insoleDataGetter.b;
        }

        this.pd = this.insoleDataGetter.d;
    }

    @Override
    public void close() {
        this.stanceDataGetter.removeDataCallbackListener(this);
    }

}
