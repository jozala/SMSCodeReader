package pl.aetas.android.smscode.resource;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.db.SMSCodeReaderSQLiteHelper;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.model.Sender;

import java.util.HashSet;
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

    public boolean isSenderKnown(final String senderName) {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final long numberOfSendersWithGivenName = DatabaseUtils.queryNumEntries(database, TABLE_SENDERS, COL_SENDER_NAME + "=" + senderName);
        final boolean senderExists = numberOfSendersWithGivenName > 0;
        database.close();
        return senderExists;
    }

    public Sender getSenderByName(final String senderName) throws UnknownSenderException {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor senderCursor = getSenderByName(senderName, database);
        final Cursor regularExpressionsCursor = getRegularExpressionsForSender(senderName, database);

        Sender sender = cursorToSender(senderCursor, regularExpressionsCursor);

        senderCursor.close();
        regularExpressionsCursor.close();
        database.close();

        return sender;
    }

    private Cursor getRegularExpressionsForSender(final String senderName, final SQLiteDatabase database) {
        final String[] regularExpressionsColumns = {COL_REGEXP_EXPRESSION, COL_REGEXP_RELEVANT_GROUP_NUMBER};
        final Cursor regularExpressionsCursor = database.query(TABLE_REGULAR_EXPRESSIONS, regularExpressionsColumns, COL_REGEXP_SENDER_NAME + "=" + senderName, null, null, null, null);
        if (regularExpressionsCursor.getCount() < 1) {
            Log.e(SendersResource.class, "No codes regular expression found in DB for known Sender: " + senderName);
            throw new NoCodesForKnownSenderException("No codes for sender " + senderName + " found");
        }
        regularExpressionsCursor.moveToFirst();
        return regularExpressionsCursor;
    }

    private Cursor getSenderByName(final String senderName, final SQLiteDatabase database) throws UnknownSenderException {
        final String[] sendersColumns = {COL_SENDER_NAME, COL_SENDER_OFFICIAL_NAME};
        final Cursor senderCursor = database.query(TABLE_SENDERS, sendersColumns, COL_SENDER_NAME + "=" + senderName, null, null, null, null);
        final int sendersCount = senderCursor.getCount();
        if (sendersCount != 1) {
            throw new UnknownSenderException("Should be exactly 1 sender but found: " + sendersCount + " senders with name " + senderName);
        }

        senderCursor.moveToFirst();
        return senderCursor;
    }

    private Sender cursorToSender(final Cursor senderCursor, final Cursor regularExpressionsCursor) {
        CodesRegularExpressions codesRegularExpressions = cursorToCodesRegularExpressions(regularExpressionsCursor);

        final int senderNameColumnIndex = senderCursor.getColumnIndexOrThrow(COL_SENDER_NAME);
        final int senderOfficialNameColumnIndex = senderCursor.getColumnIndexOrThrow(COL_SENDER_OFFICIAL_NAME);
        final String senderName = senderCursor.getString(senderNameColumnIndex);
        final String senderOfficialName = senderCursor.getString(senderOfficialNameColumnIndex);

        return new Sender(senderName, senderOfficialName, codesRegularExpressions);
    }

    private CodesRegularExpressions cursorToCodesRegularExpressions(final Cursor regularExpressionsCursor) {
        final int regexpRelevantGroupNumberColumnIndex = regularExpressionsCursor.getColumnIndexOrThrow(COL_REGEXP_RELEVANT_GROUP_NUMBER);
        final int regexpExpressionColumnIndex = regularExpressionsCursor.getColumnIndexOrThrow(COL_REGEXP_EXPRESSION);

        Set<CodeRegularExpression> codesRegularExpressionSet = new HashSet<CodeRegularExpression>(regularExpressionsCursor.getCount());
        while (regularExpressionsCursor.isAfterLast()) {
            final String expression = regularExpressionsCursor.getString(regexpExpressionColumnIndex);
            final int relevantGroup = regularExpressionsCursor.getInt(regexpRelevantGroupNumberColumnIndex);
            codesRegularExpressionSet.add(new CodeRegularExpression(expression, relevantGroup));
        }

        return new CodesRegularExpressions(codesRegularExpressionSet);
    }
}
