package pl.aetas.android.smscode.resource;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import pl.aetas.android.smscode.db.SMSCodeReaderSQLiteHelper;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.model.Sender;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SendersResourceTest extends AndroidTestCase {

    private SMSCodeReaderSQLiteHelper smsCodeReaderSQLiteHelper;
    private SendersResource sendersResource;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        smsCodeReaderSQLiteHelper = new SMSCodeReaderSQLiteHelper(new RenamingDelegatingContext(getContext(), "test_"));
        smsCodeReaderSQLiteHelper.getWritableDatabase();

        sendersResource = new SendersResource(smsCodeReaderSQLiteHelper);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        smsCodeReaderSQLiteHelper.close();
    }

    public void testShouldReadMultipleSenderIdsFromDatabaseWhenGettingAllSenders() throws Exception {
        List<Sender> senders = sendersResource.getAllSenders();
        Sender dropboxSender = findSenderFromCollection(senders, "Dropbox");

        Set<String> senderIds = dropboxSender.getSenderIds();

        HashSet<String> expectedSenderIds = new HashSet<String>();
        assertTrue(senderIds.size() == 2);
        expectedSenderIds.add("+15105647313");
        expectedSenderIds.add("+15107882753");
        assertTrue(senderIds.containsAll(expectedSenderIds));
    }

    public void testShouldReadMultipleSenderRegularExpressionsFromDatabaseWhenGettingAllSenders() throws Exception {
        List<Sender> senders = sendersResource.getAllSenders();
        Sender dropboxSender = findSenderFromCollection(senders, "Dropbox");

        CodesRegularExpressions regularExpressions = dropboxSender.getCodesRegularExpressions();

        assertTrue(regularExpressions.checkIfBodyContainsCode("Kod zabezpieczajacy to 697281. Milego korzystania z Dropbox!"));
        assertTrue(regularExpressions.checkIfBodyContainsCode("Your security code is 533344. Happy Dropboxing!"));
        assertFalse(regularExpressions.checkIfBodyContainsCode("Unsupported format 371263 bla bla bla. Happy Dropboxing!"));
    }

    public void testShouldReadMultipleSenderIdsFromDatabaseWhenGettingSingleSenderByDisplayName() throws Exception {
        Sender dropboxSender = sendersResource.getSenderByDisplayName("Dropbox");

        Set<String> senderIds = dropboxSender.getSenderIds();

        HashSet<String> expectedSenderIds = new HashSet<String>();
        expectedSenderIds.add("+15105647313");
        expectedSenderIds.add("+15107882753");
        assertTrue(senderIds.size() == 2);
        assertTrue(senderIds.containsAll(expectedSenderIds));
    }

    public void testShouldReadMultipleSenderRegularExpressionFromDatabaseWhenGettingSingleSenderByDisplayName() throws Exception {
        Sender dropboxSender = sendersResource.getSenderByDisplayName("Dropbox");

        CodesRegularExpressions regularExpressions = dropboxSender.getCodesRegularExpressions();

        assertTrue(regularExpressions.checkIfBodyContainsCode("Kod zabezpieczajacy to 697281. Milego korzystania z Dropbox!"));
        assertTrue(regularExpressions.checkIfBodyContainsCode("Your security code is 533344. Happy Dropboxing!"));
        assertFalse(regularExpressions.checkIfBodyContainsCode("Unsupported format 371263 bla bla bla. Happy Dropboxing!"));
    }

    public void testShouldNotFindSenderByDisplayNameWhenCheckingIfSenderIsKnown() throws Exception {
        assertFalse(sendersResource.isSenderKnown("Dropbox"));
    }

    public void testShouldFindSenderByAnySenderIdWhenCheckingIfSenderIsKnown() throws Exception {
        assertTrue(sendersResource.isSenderKnown("+15105647313"));
        assertTrue(sendersResource.isSenderKnown("+15107882753"));
    }

    public void testShouldIgnoreCaseWhenLookingForCustomerByDisplayName() throws Exception {
        Sender dropboxSender = sendersResource.getSenderByDisplayName("dRoPboX");
        assertNotNull(dropboxSender);
    }

    public void testShouldIgnoreCaseWhenCheckingIfSenderIsKnown() throws Exception {
        assertTrue(sendersResource.isSenderKnown("TM Bankowe"));
        assertTrue(sendersResource.isSenderKnown("tm bankowe"));
        assertTrue(sendersResource.isSenderKnown("tm BaNkOwE"));
    }

    private Sender findSenderFromCollection(Collection<Sender> senders, String displayName) {
        Sender wantedSender = null;

        for (Sender sender : senders) {
            if (sender.getOfficialName().equals(displayName)) {
                wantedSender = sender;
                break;
            }
        }
        return wantedSender;
    }
}