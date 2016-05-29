package pl.aetas.android.smscode.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.akquinet.android.androlog.Log;
import org.xml.sax.SAXException;
import pl.aetas.android.smscode.R;
import pl.aetas.android.smscode.exception.LoadingDatabaseFailedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static pl.aetas.android.smscode.db.SendersXmlDataReader.MessageData;
import static pl.aetas.android.smscode.db.SendersXmlDataReader.SenderData;

public class SMSCodeReaderSQLiteHelper extends SQLiteOpenHelper {

    private final Context context;

    public static final String TABLE_SENDERS = "senders";
    public static final String COL_SENDER_DISPLAY_NAME = "display_name";
    public static final String TABLE_SENDERS_IDS = "senders_ids";
    public static final String COL_SENDERS_IDS_DISPLAY_NAME = "display_name";
    public static final String COL_SENDERS_IDS_SENDER_ID = "sender_id";
    public static final String TABLE_REGULAR_EXPRESSIONS = "regular_expressions";
    public static final String COL_REGEXP_SENDER_DISPLAY_NAME = "display_name";
    public static final String COL_REGEXP_EXPRESSION = "expression";
    public static final String COL_REGEXP_RELEVANT_GROUP_NUMBER = "relevant_group_number";
    private static final String DATABASE_NAME = "smscodereader.db";
    private static final int DATABASE_VERSION = 13;
    private static final String CREATE_TABLE_SENDERS = "CREATE TABLE senders (" +
            "    display_name TEXT PRIMARY KEY" +
            ");";
    private static final String CREATE_TABLE_SENDERS_IDS = "CREATE TABLE senders_ids (" +
            "    display_name TEXT NOT NULL," +
            "    sender_id TEXT PRIMARY KEY," +
            "    FOREIGN KEY (display_name) REFERENCES sender(display_name)" +
            ");";
    private static final String CREATE_TABLE_REGEXP = "CREATE TABLE regular_expressions (" +
            "    display_name TEXT NOT NULL," +
            "    type TEXT NOT NULL," +
            "    expression TEXT NOT NULL," +
            "    relevant_group_number INTEGER NOT NULL," +
            "    PRIMARY KEY (display_name, type)," +
            "    FOREIGN KEY (display_name) REFERENCES sender(display_name)" +
            ");";
    private static final String FAILED_TO_LOAD_XML_TO_DB_MESSAGE = "Failed to load SMS data to database";

    public SMSCodeReaderSQLiteHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(SMSCodeReaderSQLiteHelper.class, "Creating database with SMS Codes");
        db.execSQL(CREATE_TABLE_SENDERS);
        db.execSQL(CREATE_TABLE_SENDERS_IDS);
        db.execSQL(CREATE_TABLE_REGEXP);
        copyDataToDatabase(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SMSCodeReaderSQLiteHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDERS_IDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGULAR_EXPRESSIONS);
        onCreate(db);
    }


    private void copyDataToDatabase(final SQLiteDatabase db) {
        try {
            InputStream xmlDataFileInputStream = context.getResources().openRawResource(R.raw.sms_data);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            SendersXmlDataReader sendersXmlDataReader = new SendersXmlDataReader(documentBuilder);
            List<SenderData> senders = sendersXmlDataReader.loadSendersDataFromXml(xmlDataFileInputStream);
            for (SenderData sender : senders) {
                db.execSQL("INSERT INTO senders VALUES(?);", new String[] {sender.getDisplayName()});
                for (String senderId : sender.getSenderIds()) {
                    db.execSQL("INSERT INTO senders_ids VALUES(?,?);",
                            new Object[] {sender.getDisplayName(), senderId});
                }
                for (MessageData message : sender.getMessages()) {
                    db.execSQL("INSERT INTO regular_expressions VALUES(?,?,?,?);",
                            new Object[] {sender.getDisplayName(), message.getType(), message.getRegexp(), message.getRelevantGroup()});
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
        final String[] sendersColumns = {COL_SENDER_DISPLAY_NAME};

        return db.query(TABLE_SENDERS, sendersColumns, null, null, null, null, "LOWER(" + COL_SENDER_DISPLAY_NAME + ")");
    }
}