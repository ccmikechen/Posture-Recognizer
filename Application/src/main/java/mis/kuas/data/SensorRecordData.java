package mis.kuas.data;

/**
 * Created by mingjia on 2016/2/13.
 */
public class SensorRecordData {

    private int sequenceId;

    private long sequenceTime;

    private int recordId;

    private InsoleDataGetter leftInsoleDataGetter;

    private InsoleDataGetter rightInsoleDataGetter;

    private BeltDataGetter beltDataGetter;

    private BandDataGetter bandDataGetter;

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public long getSequenceTime() {
        return sequenceTime;
    }

    public void setSequenceTime(long sequenceTime) {
        this.sequenceTime = sequenceTime;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public InsoleDataGetter getLeftInsoleDataGetter() {
        return leftInsoleDataGetter;
    }

    public void setLeftInsoleDataGetter(InsoleDataGetter leftInsoleDataGetter) {
        this.leftInsoleDataGetter = leftInsoleDataGetter;
    }

    public InsoleDataGetter getRightInsoleDataGetter() {
        return rightInsoleDataGetter;
    }

    public void setRightInsoleDataGetter(InsoleDataGetter rightInsoleDataGetter) {
        this.rightInsoleDataGetter = rightInsoleDataGetter;
    }

    public BeltDataGetter getBeltDataGetter() {
        return beltDataGetter;
    }

    public void setBeltDataGetter(BeltDataGetter beltDataGetter) {
        this.beltDataGetter = beltDataGetter;
    }

    public BandDataGetter getBandDataGetter() {
        return bandDataGetter;
    }

    public void setBandDataGetter(BandDataGetter bandDataGetter) {
        this.bandDataGetter = bandDataGetter;
    }

}
