package pl.aetas.android.smscode.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.akquinet.android.androlog.Log;

public class SMSCodeReaderSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SENDERS = "senders";
    public static final String COL_SENDER_NAME = "name";
    public static final String COL_SENDER_OFFICIAL_NAME = "official_name";
    public static final String TABLE_REGULAR_EXPRESSIONS = "regular_expressions";
    public static final String COL_REGEXP_SENDER_NAME = "sender_name";
    public static final String COL_REGEXP_EXPRESSION = "expression";
    public static final String COL_REGEXP_RELEVANT_GROUP_NUMBER = "relevant_group_number";


    private static final String DATABASE_NAME = "smscodereader.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_DATABASE = "CREATE TABLE senders\n" +
            "    name TEXT PRIMARY KEY,\n" +
            "    official_name TEXT NOT NULL;\n" +
            "    \n" +
            "CREATE TABLE regular_expressions\n" +
            "    FOREIGN KEY (sender_name) REFERENCES sender(name) NOT NULL,\n" +
            "    FOREIGN KEY (type_name) REFERENCES sms_types(name) NOT NULL,\n" +
            "    expression TEXT NOT NULL,\n" +
            "    relevant_group_number INTEGER NOT NULL,\n" +
            "    PRIMARY KEY (sender_name, type);";

    public SMSCodeReaderSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SMSCodeReaderSQLiteHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGULAR_EXPRESSIONS);
        onCreate(db);
    }
}
