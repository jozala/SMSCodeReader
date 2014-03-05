package pl.aetas.android.smscode.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.akquinet.android.androlog.Log;
import org.xml.sax.SAXException;
import pl.aetas.android.smscode.exception.LoadingDatabaseFailedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

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

    private static final String FAILED_TO_LOAD_XML_TO_DB_MESSAGE = "Failed to load SMS data to database";
    private static final String SMS_DATA_XML_FILE_PATH = "res/raw/sms_data.xml";

    public SMSCodeReaderSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        try {
            FileInputStream xmlDataFileInputStream = new FileInputStream(SMS_DATA_XML_FILE_PATH);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            SendersXmlDataReader sendersXmlDataReader = new SendersXmlDataReader(documentBuilder);
            List<SendersXmlDataReader.SenderData> senders = sendersXmlDataReader.loadSendersDataFromXml(xmlDataFileInputStream);
            for (SendersXmlDataReader.SenderData sender : senders) {
                db.execSQL("INSERT INTO senders VALUES(?,?);", new String[] {sender.getName(), sender.getDisplayName()});
                for (SendersXmlDataReader.MessageData message : sender.getMessages()) {
                    db.execSQL("INSERT INTO regular_expressions VALUES(?,?,?,?);",
                            new Object[] {sender.getName(), message.getType(), message.getRegexp(), message.getRelevantGroup()});
                }
            }

        } catch (FileNotFoundException e) {
            Log.e(SMSCodeReaderSQLiteHelper.class, FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
            throw new LoadingDatabaseFailedException(FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
        } catch (SAXException e) {
            Log.e(SMSCodeReaderSQLiteHelper.class, FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
            throw new LoadingDatabaseFailedException(FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
        } catch (IOException e) {
            Log.e(SMSCodeReaderSQLiteHelper.class, FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
            throw new LoadingDatabaseFailedException(FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
        } catch (ParserConfigurationException e) {
            Log.e(SMSCodeReaderSQLiteHelper.class, FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
            throw new LoadingDatabaseFailedException(FAILED_TO_LOAD_XML_TO_DB_MESSAGE, e);
        }
    }

    public Cursor fetchAllSenders(final SQLiteDatabase db) {
        final String[] sendersColumns = {COL_SENDER_NAME, COL_SENDER_OFFICIAL_NAME};

        return db.query(TABLE_SENDERS, sendersColumns, null, null, null, null, "LOWER(" + COL_SENDER_OFFICIAL_NAME + ")");
    }
}