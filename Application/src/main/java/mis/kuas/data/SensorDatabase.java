package mis.kuas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mingjia on 2016/2/13.
 */
public class SensorDatabase {

    public static final String TABLE_RECORD_DATA = "RecordData";

    public static final String TABLE_RECORD_INFO = "RecordInfo";

    public static final String COLUMN_RECORD_ID = "RecordID";

    public static final String COLUMN_RECORD_SEQUENCE_ID = "RecordSequenceID";

    public static final String COLUMN_RECORD_SEQUENCE_TIME = "RecordSequenceTime";

    public static final String COLUMN_LEFT_PRESSURE_A = "LeftPressureA";

    public static final String COLUMN_LEFT_PRESSURE_B = "LeftPressureB";

    public static final String COLUMN_LEFT_PRESSURE_C = "LeftPressureC";

    public static final String COLUMN_LEFT_PRESSURE_D = "LeftPressureD";

    public static final String COLUMN_LEFT_ACC_X = "LeftAccX";

    public static final String COLUMN_LEFT_ACC_Y = "LeftAccY";

    public static final String COLUMN_LEFT_ACC_Z = "LeftAccZ";

    public static final String COLUMN_RIGHT_PRESSURE_A = "RightPressureA";

    public static final String COLUMN_RIGHT_PRESSURE_B = "RightPressureB";

    public static final String COLUMN_RIGHT_PRESSURE_C = "RightPressureC";

    public static final String COLUMN_RIGHT_PRESSURE_D = "RightPressureD";

    public static final String COLUMN_RIGHT_ACC_X = "RightAccX";

    public static final String COLUMN_RIGHT_ACC_Y = "RightAccY";

    public static final String COLUMN_RIGHT_ACC_Z = "RightAccZ";

    public static final String COLUMN_BELT_ACC_X = "BeltAccX";

    public static final String COLUMN_BELT_ACC_Y = "BeltAccY";

    public static final String COLUMN_BELT_ACC_Z = "BeltAccZ";

    public static final String COLUMN_BAND_ACC_X = "BandAccX";

    public static final String COLUMN_BAND_ACC_Y = "BandAccY";

    public static final String COLUMN_BAND_ACC_Z = "BandAccZ";



    public static final String COLUMN_START_TIME = "StartTime";

    public static final String COLUMN_END_TIME = "EndTime";

    public static final String DATA_LEFT_INSOLE_PRESSURE = "leftInsolePressure";

    public static final String DATA_RIGHT_INSOLE_PRESSURE = "rightInsolePressure";

    public static final String DATA_LEFT_INSOLE_ACC = "leftInsoleAcc";

    public static final String DATA_RIGHT_INSOLE_ACC = "rightInsoleAcc";

    public static final String DATA_BELT_ACC = "beltAcc";

    public static final String DATA_BAND_ACC = "bandAcc";

    public static final String SQL_CREATE_TABLE_RECORD_DATA =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RECORD_DATA + "(" +
                    COLUMN_RECORD_SEQUENCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_RECORD_SEQUENCE_TIME + " INTEGER NOT NULL,"+
                    COLUMN_RECORD_ID + " INTEGER NOT NULL," +
                    COLUMN_LEFT_PRESSURE_A + " INTEGER," +
                    COLUMN_LEFT_PRESSURE_B + " INTEGER," +
                    COLUMN_LEFT_PRESSURE_C + " INTEGER," +
                    COLUMN_LEFT_PRESSURE_D + " INTEGER," +
                    COLUMN_LEFT_ACC_X + " REAL," +
                    COLUMN_LEFT_ACC_Y + " REAL," +
                    COLUMN_LEFT_ACC_Z + " REAL," +
                    COLUMN_RIGHT_PRESSURE_A + " INTEGER," +
                    COLUMN_RIGHT_PRESSURE_B + " INTEGER," +
                    COLUMN_RIGHT_PRESSURE_C + " INTEGER," +
                    COLUMN_RIGHT_PRESSURE_D + " INTEGER," +
                    COLUMN_RIGHT_ACC_X + " REAL," +
                    COLUMN_RIGHT_ACC_Y + " REAL," +
                    COLUMN_RIGHT_ACC_Z + " REAL," +
                    COLUMN_BELT_ACC_X + " REAL," +
                    COLUMN_BELT_ACC_Y + " REAL," +
                    COLUMN_BELT_ACC_Z + " REAL," +
                    COLUMN_BAND_ACC_X + " REAL," +
                    COLUMN_BAND_ACC_Y + " REAL," +
                    COLUMN_BAND_ACC_Z + " REAL)";

    public static final String SQL_CREATE_TABLE_RECORD_INFO =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RECORD_INFO + "(" +
                    COLUMN_RECORD_ID + " INTEGER PRIMARY KEY NOT NULL," +
                    COLUMN_START_TIME + " TEXT," +
                    COLUMN_END_TIME + " TEXT)";

    private SQLiteDatabase database;

    public SensorDatabase(Context context) {
        this.database = SQLiteDatabaseHelper.getDatabase(context);
        database.execSQL(SQL_CREATE_TABLE_RECORD_DATA);
        database.execSQL(SQL_CREATE_TABLE_RECORD_INFO);
    }

    public void insertData(long time,
                           int recordId,
                           InsoleDataGetter leftInsoleDataGetter,
                           InsoleDataGetter rightInsoleDataGetter,
                           BeltDataGetter beltDataGetter,
                           BandDataGetter bandDataGetter) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECORD_SEQUENCE_TIME, time);
        cv.put(COLUMN_RECORD_ID, recordId);
        cv.put(COLUMN_LEFT_PRESSURE_A, leftInsoleDataGetter.getA());
        cv.put(COLUMN_LEFT_PRESSURE_B, leftInsoleDataGetter.getB());
        cv.put(COLUMN_LEFT_PRESSURE_C, leftInsoleDataGetter.getC());
        cv.put(COLUMN_LEFT_PRESSURE_D, leftInsoleDataGetter.getD());
        cv.put(COLUMN_LEFT_ACC_X, leftInsoleDataGetter.getX());
        cv.put(COLUMN_LEFT_ACC_Y, leftInsoleDataGetter.getY());
        cv.put(COLUMN_LEFT_ACC_Z, leftInsoleDataGetter.getZ());
        cv.put(COLUMN_RIGHT_PRESSURE_A, rightInsoleDataGetter.getA());
        cv.put(COLUMN_RIGHT_PRESSURE_B, rightInsoleDataGetter.getB());
        cv.put(COLUMN_RIGHT_PRESSURE_C, rightInsoleDataGetter.getC());
        cv.put(COLUMN_RIGHT_PRESSURE_D, rightInsoleDataGetter.getD());
        cv.put(COLUMN_RIGHT_ACC_X, rightInsoleDataGetter.getX());
        cv.put(COLUMN_RIGHT_ACC_Y, rightInsoleDataGetter.getY());
        cv.put(COLUMN_RIGHT_ACC_Z, rightInsoleDataGetter.getZ());
        cv.put(COLUMN_BELT_ACC_X, beltDataGetter.getX());
        cv.put(COLUMN_BELT_ACC_Y, beltDataGetter.getY());
        cv.put(COLUMN_BELT_ACC_Z, beltDataGetter.getZ());
        cv.put(COLUMN_BAND_ACC_X, bandDataGetter.getX());
        cv.put(COLUMN_BAND_ACC_Y, bandDataGetter.getY());
        cv.put(COLUMN_BAND_ACC_Z, bandDataGetter.getZ());
        database.insert(TABLE_RECORD_DATA, null, cv);
    }

    public void insertInfo(int recordId, String startTime, String endTime) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECORD_ID, recordId);
        cv.put(COLUMN_START_TIME, startTime);
        cv.put(COLUMN_END_TIME, endTime);
        database.insert(TABLE_RECORD_INFO, null, cv);
    }

    public int getMaxCountOfRecords() {
        final String sql =
                "SELECT MAX(" + COLUMN_RECORD_ID + ") FROM " + TABLE_RECORD_INFO;
        Cursor cursor = database.rawQuery(sql, null);
        int maxCount = 0;
        if (cursor.moveToNext()) {
            maxCount = cursor.getInt(0);
        }
        cursor.close();
        return maxCount;
    }

    public List<SensorRecordData> getRecordDataById(final int recordId) {
        final String sql =
                "SELECT * FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId +
                        " ORDER BY " + COLUMN_RECORD_SEQUENCE_ID;
        Cursor cursor = database.rawQuery(sql, null);
        List<SensorRecordData> recordList = new ArrayList<SensorRecordData>();
        while (cursor.moveToNext()) {
            recordList.add(getRecord(cursor));
        }
        cursor.close();
        return recordList;
    }

    private SensorRecordData getRecord(Cursor cursor) {
        SensorRecordData record = new SensorRecordData();
        record.setSequenceId(cursor.getInt(0));
        record.setSequenceTime(cursor.getLong(1));
        record.setRecordId(cursor.getInt(2));
        InsoleDataGetter leftInsoleDataGetter = new InsoleDataGetter();
        leftInsoleDataGetter.a = cursor.getShort(3);
        leftInsoleDataGetter.b = cursor.getShort(4);
        leftInsoleDataGetter.c = cursor.getShort(5);
        leftInsoleDataGetter.d = cursor.getShort(6);
        leftInsoleDataGetter.x = cursor.getFloat(7);
        leftInsoleDataGetter.y = cursor.getFloat(8);
        leftInsoleDataGetter.z = cursor.getFloat(9);
        record.setLeftInsoleDataGetter(leftInsoleDataGetter);
        InsoleDataGetter rightInsoleDataGetter = new InsoleDataGetter();
        rightInsoleDataGetter.a = cursor.getShort(10);
        rightInsoleDataGetter.b = cursor.getShort(11);
        rightInsoleDataGetter.c = cursor.getShort(12);
        rightInsoleDataGetter.d = cursor.getShort(13);
        rightInsoleDataGetter.x = cursor.getFloat(14);
        rightInsoleDataGetter.y = cursor.getFloat(15);
        rightInsoleDataGetter.z = cursor.getFloat(16);
        record.setRightInsoleDataGetter(rightInsoleDataGetter);
        BeltDataGetter beltDataGetter = new BeltDataGetter();
        beltDataGetter.x = cursor.getFloat(17);
        beltDataGetter.y = cursor.getFloat(18);
        beltDataGetter.z = cursor.getFloat(19);
        record.setBeltDataGetter(beltDataGetter);
        BandDataGetter bandDataGetter = new BandDataGetter();
        bandDataGetter.x = cursor.getFloat(20);
        bandDataGetter.y = cursor.getFloat(21);
        bandDataGetter.z = cursor.getFloat(22);
        record.setBandDataGetter(bandDataGetter);
        return record;
    }

    public List<PressureDataGetter> getPressureDataById(int recordId, String dataName) {
        final String sql;
        switch (dataName) {
            case DATA_LEFT_INSOLE_PRESSURE:
                sql = "SELECT " +
                        COLUMN_LEFT_PRESSURE_A + "," +
                        COLUMN_LEFT_PRESSURE_B + "," +
                        COLUMN_LEFT_PRESSURE_C + "," +
                        COLUMN_LEFT_PRESSURE_D +
                        " FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId;
                break;
            case DATA_RIGHT_INSOLE_PRESSURE:
                sql = "SELECT " +
                        COLUMN_RIGHT_PRESSURE_A + "," +
                        COLUMN_RIGHT_PRESSURE_B + "," +
                        COLUMN_RIGHT_PRESSURE_C + "," +
                        COLUMN_RIGHT_PRESSURE_D +
                        " FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId;
                break;
            default:
                return null;
        }
        Cursor cursor = database.rawQuery(sql, null);
        List<PressureDataGetter> dataList = new ArrayList<PressureDataGetter>();
        while (cursor.moveToNext()) {
            PressureDataModel dataModel = new PressureDataModel();
            dataModel.setA(cursor.getShort(0));
            dataModel.setB(cursor.getShort(1));
            dataModel.setC(cursor.getShort(2));
            dataModel.setD(cursor.getShort(3));
            dataList.add(dataModel);
        }
        return dataList;
    }

    public List<AccDataGetter> getAccDataById(int recordId, String dataName) {
        final String sql;
        switch (dataName) {
            case DATA_LEFT_INSOLE_ACC:
                sql = "SELECT " +
                        COLUMN_LEFT_ACC_X + "," +
                        COLUMN_LEFT_ACC_Y + "," +
                        COLUMN_LEFT_ACC_Z +
                        " FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId;
                break;
            case DATA_RIGHT_INSOLE_ACC:
                sql = "SELECT " +
                        COLUMN_RIGHT_ACC_X + "," +
                        COLUMN_RIGHT_ACC_Y + "," +
                        COLUMN_RIGHT_ACC_Z +
                        " FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId;
                break;
            case DATA_BELT_ACC:
                sql = "SELECT " +
                        COLUMN_BELT_ACC_X + "," +
                        COLUMN_BELT_ACC_Y + "," +
                        COLUMN_BELT_ACC_Z +
                        " FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId;
                break;
            case DATA_BAND_ACC:
                sql = "SELECT " +
                        COLUMN_BAND_ACC_X + "," +
                        COLUMN_BAND_ACC_Y + "," +
                        COLUMN_BAND_ACC_Z +
                        " FROM " + TABLE_RECORD_DATA +
                        " WHERE " + COLUMN_RECORD_ID + " = " + recordId;
                break;
            default:
                return null;
        }
        Cursor cursor = database.rawQuery(sql, null);
        List<AccDataGetter> dataList = new ArrayList<AccDataGetter>();
        while (cursor.moveToNext()) {
            AccDataModel dataModel = new AccDataModel();
            dataModel.setX(cursor.getFloat(0));
            dataModel.setY(cursor.getFloat(1));
            dataModel.setZ(cursor.getFloat(2));
            dataList.add(dataModel);
        }
        return dataList;
    }

    public SensorRecordInfo getRecordInfoById(final int recordId) {
        final String sql =
                "SELECT * FROM " + TABLE_RECORD_INFO +
                        "WHERE " + COLUMN_RECORD_ID + " = " + recordId;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            return getRecordInfo(cursor);
        }
        return null;
    }

    private SensorRecordInfo getRecordInfo(Cursor cursor) {
        SensorRecordInfo info = new SensorRecordInfo();
        info.setRecordId(cursor.getInt(0));
        info.setStartTime(cursor.getString(1));
        info.setEndTime(cursor.getString(2));
        return info;
    }

    public List<SensorRecordInfo> getRecordInfoList() {
        final String sql =
                "SELECT * FROM " + TABLE_RECORD_INFO;
        Cursor cursor = database.rawQuery(sql, null);
        List<SensorRecordInfo> recordInfoList = new ArrayList<SensorRecordInfo>();
        while (cursor.moveToNext()) {
            SensorRecordInfo recordInfo = new SensorRecordInfo();
            recordInfo.setRecordId(cursor.getInt(0));
            recordInfo.setStartTime(cursor.getString(1));
            recordInfo.setEndTime(cursor.getString(2));
            recordInfoList.add(recordInfo);
        }
        return recordInfoList;
    }

}
