package mis.kuas.data;

import java.io.Closeable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StanceDataRecorder implements StanceDataCallbackListener, Closeable {

    private StanceDataGetter dataGetter;

    private SensorDatabase database;

    private boolean isRecording = false;

    private int recordId;

    private String startTime;

    private String endTime;

    public StanceDataRecorder(StanceDataGetter dataGetter, SensorDatabase database) {
        this.setDataGetter(dataGetter);
        this.setDatabase(database);
    }

    public void setDataGetter(StanceDataGetter dataGetter) {
        if (this.dataGetter != null) {
            this.dataGetter.removeDataCallbackListener(this);
        }
        dataGetter.addDataCallbackListener(this);
        this.dataGetter = dataGetter;
    }

    public void setDatabase(SensorDatabase database) {
        this.database = database;
    }

    synchronized public void start() {
        if (isRecording) {
            return;
        }
        this.recordId = this.database.getMaxCountOfRecords() + 1;
        this.startTime = getCurrentDateTime();
        this.isRecording = true;
    }

    synchronized public void stop() {
        if (!isRecording) {
            return;
        }
        this.endTime = getCurrentDateTime();
        this.database.insertInfo(this.recordId, this.startTime, this.endTime);
        this.isRecording = false;
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public void onDataCallback() {
        if (this.isRecording) {
            long currentTime = System.nanoTime();
            this.database.insertData(currentTime,
                    this.recordId,
                    this.dataGetter.getLeftInsoleDataGetter(),
                    this.dataGetter.getRightInsoleDataGetter(),
                    this.dataGetter.getBeltDataGetter(),
                    this.dataGetter.getBandDataGetter());
        }
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    @Override
    public void close() {
        if (this.isRecording) {
            stop();
        }
        this.dataGetter.removeDataCallbackListener(this);
    }
}
