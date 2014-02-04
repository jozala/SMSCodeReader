package pl.aetas.android.smscode.db;

import android.content.Context;
import android.database.Cursor;
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
    private static final int DATABASE_VERSION = 3;
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

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SMSCodeReaderSQLiteHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGULAR_EXPRESSIONS);
        onCreate(db);
    }


    private void copyDataToDatabase(final SQLiteDatabase db) {
        // TODO load data from resource files and put them to tables senders and regular_expressions
        final String insertMBankSender = "INSERT INTO senders VALUES('3388','mBank');";
        final String insertMBankRegExp = "INSERT INTO regular_expressions VALUES('3388','general','.*(haslo: )(\\d{8})(\\s?).*',2);";
        db.execSQL(insertMBankSender);
        db.execSQL(insertMBankRegExp);

        final String insertAliorBankSender = "INSERT INTO senders VALUES('Alior Bank','Alior Bank');";
        final String insertAliorBankRegExp = "INSERT INTO regular_expressions VALUES('Alior Bank','general','.*(\\s?)(\\d{6})$',2);";
        db.execSQL(insertAliorBankSender);
        db.execSQL(insertAliorBankRegExp);

        final String insertAliorSyncSender = "INSERT INTO senders VALUES('Alior Sync','Alior Sync');";
        final String insertAliorSyncRegExp = "INSERT INTO regular_expressions VALUES('Alior Sync','general','.*(\\s?)(\\d{6})$',2);";
        db.execSQL(insertAliorSyncSender);
        db.execSQL(insertAliorSyncRegExp);

        final String insertBzwbkSender = "INSERT INTO senders VALUES('BZWBK24','BZWBK24');";
        final String insertBzwbkRegExp = "INSERT INTO regular_expressions VALUES('BZWBK24','general','[\\s\\S]*(smsKod: )(\\d{8})(\\s?).*',2);";
        db.execSQL(insertBzwbkSender);
        db.execSQL(insertBzwbkRegExp);

        final String insertIPKOSender = "INSERT INTO senders VALUES('PKOBP','iPKO');";
        final String insertIPKORegExp = "INSERT INTO regular_expressions VALUES('PKOBP','general','.*(\\s?)(\\d{6})$',2);";
        db.execSQL(insertIPKOSender);
        db.execSQL(insertIPKORegExp);

        final String insertIPKO2Sender = "INSERT INTO senders VALUES('PKO BP','iPKO');";
        final String insertIPKO2RegExp = "INSERT INTO regular_expressions VALUES('PKO BP','general','.*(\\s?)(\\d{6})$',2);";
        db.execSQL(insertIPKO2Sender);
        db.execSQL(insertIPKO2RegExp);

        final String insertIngSender = "INSERT INTO senders VALUES('ING','ING');";
        final String insertIngSyncRegExp = "INSERT INTO regular_expressions VALUES('ING','general','.*(\\s?to: )(\\S+)(\\s?).*',2);";
        db.execSQL(insertIngSender);
        db.execSQL(insertIngSyncRegExp);

        final String insertInteligoSender = "INSERT INTO senders VALUES('Inteligo','Inteligo');";
        final String insertInteligoRegExp = "INSERT INTO regular_expressions VALUES('Inteligo','general','.*(\\s?)(\\d{6})$',2);";
        db.execSQL(insertInteligoSender);
        db.execSQL(insertInteligoRegExp);

        final String insertMillenniumSender = "INSERT INTO senders VALUES('HasloSMS','Millennium');";
        final String insertMillenniumRegExp = "INSERT INTO regular_expressions VALUES('HasloSMS','general','.*(HasloSMS: )(\\d{6})$',2);";
        db.execSQL(insertMillenniumSender);
        db.execSQL(insertMillenniumRegExp);

        final String insertMultibankSender = "INSERT INTO senders VALUES('3003','MultiBank');";
        final String insertMultibankRegExp = "INSERT INTO regular_expressions VALUES('3003','general','.*(haslo: )(\\d{8})(\\s?).*',2);";
        db.execSQL(insertMultibankSender);
        db.execSQL(insertMultibankRegExp);

        final String insertWalutomatSender = "INSERT INTO senders VALUES('Walutomat','Walutomat');";
        final String insertWalutomatRegExp = "INSERT INTO regular_expressions VALUES('Walutomat','general','.*(\\s?)(\\d{6})$',2);";
        db.execSQL(insertWalutomatSender);
        db.execSQL(insertWalutomatRegExp);

        final String insertBGZOptimaSender = "INSERT INTO senders VALUES('BGZOptima','BGZ Optima');";
        final String insertBGZOptimaRegExp = "INSERT INTO regular_expressions VALUES('BGZOptima','general','.*(Haslo: )(\\d{8})$',2);";
        db.execSQL(insertBGZOptimaSender);
        db.execSQL(insertBGZOptimaRegExp);

        final String insertBGZSender = "INSERT INTO senders VALUES('Bank BGZ','Bank BGZ');";
        final String insertBGZRegExp = "INSERT INTO regular_expressions VALUES('Bank BGZ','general','.*(kod: )(\\d{8}).*',2);";
        db.execSQL(insertBGZSender);
        db.execSQL(insertBGZRegExp);

        final String insertPocztowySender = "INSERT INTO senders VALUES('Pocztowy','Bank Pocztowy');";
        final String insertPocztowyRegExp = "INSERT INTO regular_expressions VALUES('Pocztowy','general','.*(Kod: )(\\d{6}).*',2);";
        db.execSQL(insertPocztowySender);
        db.execSQL(insertPocztowyRegExp);

//        final String insertMBankSKRegExp = "INSERT INTO regular_expressions VALUES('3388','general','.*(kod: )(\\d{8})(\\s?).*',2);";
//        db.execSQL(insertMBankSKRegExp);
//
//        final String insertFIOSender = "INSERT INTO senders VALUES('+420725664075','FIO SK');";
//        final String insertFIORegExp = "INSERT INTO regular_expressions VALUES('+420725664075','general','^(Aut.kod: )(\\d{7})(\\s?).*',2);";
//        db.execSQL(insertFIOSender);
//        db.execSQL(insertFIORegExp);

        final String insertDeutscheBankSender = "INSERT INTO senders VALUES('DB PBC','Deutsche Bank');";
        final String insertDeutscheBankRegExp = "INSERT INTO regular_expressions VALUES('DB PBC','general','.*(Haslo: )(\\d{8})$',2);";
        db.execSQL(insertDeutscheBankSender);
        db.execSQL(insertDeutscheBankRegExp);

        final String insertGoogleSender = "INSERT INTO senders VALUES('Google','Google');";
        final String insertGooglePLRegExp = "INSERT INTO regular_expressions VALUES('Google','2-step verification PL','.*(Google to )(\\d{6}).*',2);";
        final String insertGoogleENRegExp = "INSERT INTO regular_expressions VALUES('Google','2-step verification EN','.*(code is )(\\d{6}).*',2);";
        db.execSQL(insertGoogleSender);
        db.execSQL(insertGooglePLRegExp);
        db.execSQL(insertGoogleENRegExp);

        final String insertDropboxSender = "INSERT INTO senders VALUES('+15105647313','Dropbox');";
        final String insertDropboxPLRegExp = "INSERT INTO regular_expressions VALUES('+15105647313','2-step verification PL','.*(Kod zabezpieczajacy to )(\\d{6}).*',2);";
        final String insertDropboxENRegExp = "INSERT INTO regular_expressions VALUES('+15105647313','2-step verification EN','.*(security code is )(\\d{6}).*',2);";
        db.execSQL(insertDropboxSender);
        db.execSQL(insertDropboxPLRegExp);
        db.execSQL(insertDropboxENRegExp);

        final String insertIdeaBankSender = "INSERT INTO senders VALUES('IDEA BANK','IDEA Bank');";
        final String insertIdeaBankRegExp = "INSERT INTO regular_expressions VALUES('IDEA Bank','general','.*(Haslo: )([a-zA-Z0-9]{6})$',2);";
        db.execSQL(insertIdeaBankSender);
        db.execSQL(insertIdeaBankRegExp);

        final String insertGetinSender = "INSERT INTO senders VALUES('GetinOnline','Getin Bank');";
        final String insertGetinRegExp = "INSERT INTO regular_expressions VALUES('GetinOnline','general','[\\s\\S]*(Haslo nr[0-9]*: )([a-zA-Z0-9]{6})$',2);";
        db.execSQL(insertGetinSender);
        db.execSQL(insertGetinRegExp);
    }

    public Cursor fetchAllSenders(final SQLiteDatabase db) {
        final String[] sendersColumns = {COL_SENDER_NAME, COL_SENDER_OFFICIAL_NAME};

        return db.query(TABLE_SENDERS, sendersColumns, null, null, null, null, "LOWER(" + COL_SENDER_OFFICIAL_NAME + ")");
    }
}