package pl.aetas.android.smscode.resource;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.db.SMSCodeReaderSQLiteHelper;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.NoSenderIdsForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.model.Sender;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pl.aetas.android.smscode.db.SMSCodeReaderSQLiteHelper.*;

/**
 * Class responsible for getting information about known senders available
 */
public class SendersResource {
    private final SMSCodeReaderSQLiteHelper dbHelper;

    public SendersResource(final SMSCodeReaderSQLiteHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean isSenderKnown(String senderId) {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final String sqlQuery = "SELECT count(*) FROM " + TABLE_SENDERS_IDS + " WHERE " + COL_SENDERS_IDS_SENDER_ID + " = '" + senderId + "' COLLATE NOCASE";
        final Cursor countSendersIdsCursor = database.rawQuery(sqlQuery, null);
        countSendersIdsCursor.moveToFirst();
        int numberOfSendersWithGivenId = countSendersIdsCursor.getInt(0);
        countSendersIdsCursor.close();
        database.close();
        return numberOfSendersWithGivenId > 0;
    }

    public Sender getSenderByDisplayName(String senderDisplayName) throws UnknownSenderException {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();

        Set<String> senderIds = fetchSenderIds(senderDisplayName, database);
        CodesRegularExpressions codesRegularExpressions = fetchRegularExpressions(senderDisplayName, database);
        database.close();

        return new Sender(senderDisplayName, senderIds, codesRegularExpressions);
    }

    public List<Sender> getAllSenders() {
        List<Sender> allSenders = new LinkedList<Sender>();
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor sendersCursor = dbHelper.fetchAllSenders(db);
        sendersCursor.moveToFirst();
        while (!sendersCursor.isAfterLast()) {
            final String senderDisplayName = sendersCursor.getString(sendersCursor.getColumnIndexOrThrow(COL_SENDER_DISPLAY_NAME));

            Set<String> senderIds = fetchSenderIds(senderDisplayName, db);
            CodesRegularExpressions codesRegularExpressions = fetchRegularExpressions(senderDisplayName, db);
            Sender sender = new Sender(senderDisplayName, senderIds, codesRegularExpressions);
            allSenders.add(sender);
            sendersCursor.moveToNext();
        }
        sendersCursor.close();
        db.close();
        return allSenders;
    }

    private Set<String> fetchSenderIds(String displayName, SQLiteDatabase database) {
        String[] sendersIdsColumns = {COL_SENDERS_IDS_SENDER_ID};
        Cursor sendersIdsCursor = database.query(TABLE_SENDERS_IDS, sendersIdsColumns, COL_SENDERS_IDS_DISPLAY_NAME + "='" + displayName + "' COLLATE NOCASE", null, null, null, null);
        if (sendersIdsCursor.getCount() < 1) {
            Log.e(SendersResource.class, "No sender IDs (caller IDs) found in DB for known Sender: " + displayName);
            throw new NoSenderIdsForKnownSenderException("No codes for sender " + displayName + " found");
        }
        sendersIdsCursor.moveToFirst();

        int senderIdColumnIndex = sendersIdsCursor.getColumnIndexOrThrow(COL_SENDERS_IDS_SENDER_ID);
        Set<String> senderIds = new HashSet<String>(sendersIdsCursor.getCount());
        while (!sendersIdsCursor.isAfterLast()) {
            String senderId = sendersIdsCursor.getString(senderIdColumnIndex);
            senderIds.add(senderId);
            sendersIdsCursor.moveToNext();
        }
        sendersIdsCursor.close();
        return senderIds;
    }

    private CodesRegularExpressions fetchRegularExpressions(String senderDisplayName, SQLiteDatabase database) {
        final String[] regularExpressionsColumns = {COL_REGEXP_EXPRESSION, COL_REGEXP_RELEVANT_GROUP_NUMBER};
        final Cursor regularExpressionsCursor = database.query(TABLE_REGULAR_EXPRESSIONS, regularExpressionsColumns, COL_REGEXP_SENDER_DISPLAY_NAME + "='" + senderDisplayName + "' COLLATE NOCASE", null, null, null, null);
        if (regularExpressionsCursor.getCount() < 1) {
            Log.e(SendersResource.class, "No codes regular expression found in DB for known Sender: " + senderDisplayName);
            throw new NoCodesForKnownSenderException("No codes for sender " + senderDisplayName + " found");
        }
        regularExpressionsCursor.moveToFirst();

        final int regexpRelevantGroupNumberColumnIndex = regularExpressionsCursor.getColumnIndexOrThrow(COL_REGEXP_RELEVANT_GROUP_NUMBER);
        final int regexpExpressionColumnIndex = regularExpressionsCursor.getColumnIndexOrThrow(COL_REGEXP_EXPRESSION);

        Set<CodeRegularExpression> codesRegularExpressionSet = new HashSet<CodeRegularExpression>(regularExpressionsCursor.getCount());
        while (!regularExpressionsCursor.isAfterLast()) {
            final String expression = regularExpressionsCursor.getString(regexpExpressionColumnIndex);
            final int relevantGroup = regularExpressionsCursor.getInt(regexpRelevantGroupNumberColumnIndex);
            codesRegularExpressionSet.add(new CodeRegularExpression(expression, relevantGroup));
            regularExpressionsCursor.moveToNext();
        }

        CodesRegularExpressions codesRegularExpressions = new CodesRegularExpressions(codesRegularExpressionSet);
        regularExpressionsCursor.close();
        return codesRegularExpressions;
    }

}
