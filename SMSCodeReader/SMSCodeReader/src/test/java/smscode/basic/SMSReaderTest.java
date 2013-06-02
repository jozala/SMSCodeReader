package smscode.basic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.analyser.SMSFilter;
import pl.aetas.android.smscode.basic.Clipboard;
import pl.aetas.android.smscode.basic.SMSInfoPresenter;
import pl.aetas.android.smscode.basic.SMSReader;
import pl.aetas.android.smscode.parser.SMSCodeParser;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSReaderTest {

    private static final String KNOWN_BANK_SENDER = "KnownBankSender";
    private static final String SMS_BODY_WITH_CODE = "This body of the message contains code here: 123456.";
    public static final String SMS_BODY_WITHOUT_CODE = "This body of the message does not contain code.";

    //SUT
    private SMSReader smsReader;

    @Mock
    private Clipboard clipboard;

    @Mock
    private SMSFilter smsFilter;

    @Mock
    private SMSInfoPresenter smsInfoPresenter;

    @Mock
    private SMSCodeParser smsCodeParser;
    public static final String SMS_SENDER = "someSender";
    public static final String SMS_BODY = "This is some text message body";

    @Before
    public void setUp() {
        initMocks(this);
        smsReader = new SMSReader(smsFilter, clipboard, smsCodeParser, smsInfoPresenter);
    }

    @Test
    public void shouldCopyCodeToClipboardFromSMSWhichIsSMSWithCode() throws Exception {
        when(smsFilter.checkIfSMSIsRelevantForCodeReader(SMS_SENDER)).thenReturn(true);
        when(smsCodeParser.retrieveCodeFromSMSBodyForKnownSender()).thenReturn("123456");

        smsReader.readSMS(SMS_SENDER, SMS_BODY);
        verify(clipboard).save("123456");
    }

    @Test
    public void shouldNotTryToCopyCodeToClipboardFromSMSWhichIsNotSMSWithCode() throws Exception {
        when(smsFilter.checkIfSMSIsRelevantForCodeReader(SMS_SENDER)).thenReturn(false);

        smsReader.readSMS(SMS_SENDER, SMS_BODY);
        verify(clipboard, never()).save(anyString());
    }

    @Test
    public void shouldPresentInfoToUserWhenSMSIsSMSWithCode() throws Exception {
        when(smsFilter.checkIfSMSIsRelevantForCodeReader(SMS_SENDER)).thenReturn(true);
        when(smsCodeParser.retrieveCodeFromSMSBodyForKnownSender()).thenReturn("123456");

        smsReader.readSMS(SMS_SENDER, SMS_BODY);

        verify(smsInfoPresenter).presentInfoToUserIfChosen(SMS_SENDER, SMS_BODY, "123456");
    }
}




