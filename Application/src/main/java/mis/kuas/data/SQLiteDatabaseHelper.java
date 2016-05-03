package mis.kuas.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stance.db";

    public static final int DATABASE_VERSION = 1;

    public static SQLiteDatabase database;

    public SQLiteDatabaseHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory,
                                int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if ((database == null) || (!database.isOpen())) {
            database = new SQLiteDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
