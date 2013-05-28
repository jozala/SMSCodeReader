package smscode.basic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.basic.Clipboard;
import pl.aetas.android.smscode.basic.SMSReader;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.verifier.SMSBodyVerifier;
import pl.aetas.android.smscode.verifier.SMSSenderVerifier;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSReaderTest {

    private static final String KNOWN_BANK_SENDER = "KnownBankSender";
    private static final String SMS_BODY_WITH_CODE = "This body of the message contains code here: 123456.";

    //SUT
    private SMSReader smsReader;

    @Mock
    private SMSSenderVerifier senderVerifier;

    @Mock
    private SMSBodyVerifier bodyVerifier;

    @Mock
    private SMSCodeParser smsCodeParser;

    @Mock
    private Clipboard clipboard;


    @Before
    public void setUp() {
        initMocks(this);
        smsReader = new SMSReader(smsCodeParser, senderVerifier, bodyVerifier, clipboard, new pl.aetas.android.smscode.basic.SMSCodePresenter());

        when(senderVerifier.checkIfSenderKnown(KNOWN_BANK_SENDER)).thenReturn(true);
        when(bodyVerifier.checkIfContainsCode(SMS_BODY_WITH_CODE)).thenReturn(true);
    }


    @Test
    public void shouldRetrieveCodeFromSMSWhichSenderIsKnownAndContainsCode() throws Exception {
        smsReader.readSMS(KNOWN_BANK_SENDER, SMS_BODY_WITH_CODE);
        verify(smsCodeParser, only()).retrieveCode(SMS_BODY_WITH_CODE);
    }

    @Test
    public void shouldNotTryToRetrieveCodeFromSMSWhichSenderIsNotKnown() throws Exception {
        final String unknownBankSender = "unknownBankSender";
        final String smsBody = "This body of the message contains code here: 123456, but it is not important here";

        when(senderVerifier.checkIfSenderKnown(unknownBankSender)).thenReturn(false);

        smsReader.readSMS(unknownBankSender, smsBody);

        verify(smsCodeParser, never()).retrieveCode(anyString());
    }

    @Test
    public void shouldNotTryToRetrieveCodeFromSMSWhichDoesNotContainCode() throws Exception {
        final String knownBankSender = "KnownBankSender";
        final String smsBodyWithoutCode = "This body of the message does not contain code.";

        when(senderVerifier.checkIfSenderKnown(knownBankSender)).thenReturn(true);
        when(bodyVerifier.checkIfContainsCode(smsBodyWithoutCode)).thenReturn(false);

        smsReader.readSMS(knownBankSender, smsBodyWithoutCode);

        verify(smsCodeParser, never()).retrieveCode(anyString());
    }

    @Test
    public void shouldCopyCodeToClipboardFromSMSWhichSenderIsKnownAndContainCode() throws Exception {
        smsReader.readSMS(KNOWN_BANK_SENDER, SMS_BODY_WITH_CODE);
        verify(clipboard).save(anyString());
    }
}




