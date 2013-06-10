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
    private static final String CREATE_TABLE_SENDERS = "CREATE TABLE senders (" +
            "    name TEXT PRIMARY KEY," +
            "    official_name TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_REGEXP = "CREATE TABLE regular_expressions (" +
            "    sender_name TEXT NOT NULL," +
            "    type TEXT NOT NULL," +
            "    expression TEXT NOT NULL," +
            "    relevant_group_number INTEGER NOT NULL," +
            "    PRIMARY KEY (sender_name, type)," +
            "    FOREIGN KEY (sender_name) REFERENCES sender(name)" +
            ");";

    private final Context context;

    public SMSCodeReaderSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(SMSCodeReaderSQLiteHelper.class, "Creating database with SMS Codes");
        db.execSQL(CREATE_TABLE_SENDERS);
        db.execSQL(CREATE_TABLE_REGEXP);
        copyDataToDatabase(db);
    }

    private void copyDataToDatabase(final SQLiteDatabase db) {
        // TODO load data from resource files and put them to tables senders and regular_expressions
        final String insertMBankSender = "INSERT INTO senders VALUES(3388,'mBank');";
        final String insertMBankRegExp = "INSERT INTO regular_expressions VALUES(3388,'transfer','.*(\\s?haslo: )(\\S+)(\\s?).*',2);";
        db.execSQL(insertMBankSender);
        db.execSQL(insertMBankRegExp);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SMSCodeReaderSQLiteHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGULAR_EXPRESSIONS);
        onCreate(db);
    }
}
