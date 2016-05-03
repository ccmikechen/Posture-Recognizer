package mis.kuas.data;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import mis.kuas.devices.ConnectionWatcher;

/**
 * Created by mingjia on 2016/2/14.
 */
public class SensorDataLogger implements StanceDataCallbackListener {



    public static final String TAG = SensorDataLogger.class.getName();

    private final String dataWriteFormat = "%d,%d,%d,%f,%f,%f,%d,%d,%d,%d,%f,%f,%f,%d,%d,%d,%d,%f,%f,%f,%f,%f,%f\n";

    private StanceDataGetter dataGetter;

    private int state = StanceDataState.NONE;

    private String filePath;

    private PrintWriter writer;

    private boolean isLogging = false;

    private boolean isCallbacked = true;

    private int count;

    private DataLoggedEvent event;

    private ConnectionWatcher connectionWatcher;

    private ConnectionWatcher.OnDisconnectedListener onDisconnectedListener =
            new ConnectionWatcher.OnDisconnectedListener() {
                @Override
                public void onDisconnected() {
                    if (isLogging) {
                        callback(DataLoggedEvent.DISCONNECTED);
                        stop();
                    }
                }
            };

    public SensorDataLogger(StanceDataGetter dataGetter) {
        setDataGetter(dataGetter);
        this.connectionWatcher = new ConnectionWatcher(1000, 1000);
    }

    public void setDataGetter(StanceDataGetter dataGetter) {
        if (this.dataGetter != null) {
            this.dataGetter.removeDataCallbackListener(this);
        }
        dataGetter.addDataCallbackListener(this);
        this.dataGetter = dataGetter;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void start(File file, DataLoggedEvent event) {
        this.isCallbacked = false;
        this.event = event;
        try {
            this.start(file);
        } catch (IOException e) {
            callback(DataLoggedEvent.FAILED);
            e.printStackTrace();
            stop();
        }
    }

    public void start(File file) throws IOException {
        if (isLogging) {
            return;
        }
        this.count = 0;
        this.filePath = file.getAbsolutePath();
        this.writer = new PrintWriter(new FileWriter(file), true);
        this.writer.println("Seq,Time,State,LX,LY,LZ,LA,LB,LC,LD,RX,RY,RZ,RA,RB,RC,RD,BeltX,BeltY,BeltZ,BandX,BandY,BandZ");
        this.isLogging = true;
        this.connectionWatcher.start(this.onDisconnectedListener);
    }

    public void stop() {
        if (!isLogging) {
            callback(DataLoggedEvent.FAILED);
        }
        this.isLogging = false;
        this.writer.flush();
        this.writer.close();
        callback(DataLoggedEvent.SUCCESSED);
    }

    @Override
    public void onDataCallback() {
        if (!this.isLogging) {
            return;
        }
        this.connectionWatcher.update();
        this.writeData();
        count++;
    }

    private void writeData() {
        try {
            this.writer.printf(dataWriteFormat,
                    count,
                    System.nanoTime(),
                    state,
                    this.dataGetter.getLeftInsoleDataGetter().getX(),
                    this.dataGetter.getLeftInsoleDataGetter().getY(),
                    this.dataGetter.getLeftInsoleDataGetter().getZ(),
                    this.dataGetter.getLeftInsoleDataGetter().getA(),
                    this.dataGetter.getLeftInsoleDataGetter().getB(),
                    this.dataGetter.getLeftInsoleDataGetter().getC(),
                    this.dataGetter.getLeftInsoleDataGetter().getD(),
                    this.dataGetter.getRightInsoleDataGetter().getX(),
                    this.dataGetter.getRightInsoleDataGetter().getY(),
                    this.dataGetter.getRightInsoleDataGetter().getZ(),
                    this.dataGetter.getRightInsoleDataGetter().getA(),
                    this.dataGetter.getRightInsoleDataGetter().getB(),
                    this.dataGetter.getRightInsoleDataGetter().getC(),
                    this.dataGetter.getRightInsoleDataGetter().getD(),
                    this.dataGetter.getBeltDataGetter().getX(),
                    this.dataGetter.getBeltDataGetter().getY(),
                    this.dataGetter.getBeltDataGetter().getZ(),
                    this.dataGetter.getBandDataGetter().getX(),
                    this.dataGetter.getBandDataGetter().getY(),
                    this.dataGetter.getBandDataGetter().getZ());
        } catch (Exception e) {
            if (this.event != null) {
                callback(DataLoggedEvent.FAILED);
                stop();
            }
            e.printStackTrace();
        }

    }

    private boolean callback(int status) {
        if (isCallbacked) {
            return false;
        }
        this.event.onDataLogged(status, this.filePath);
        this.isCallbacked = true;
        this.event = null;
        return true;
    }

    public boolean isLogging() {
        return isLogging;
    }
    
}
