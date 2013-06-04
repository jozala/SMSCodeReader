package smscode.basic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.basic.Clipboard;
import pl.aetas.android.smscode.basic.SMSInfoPresenter;
import pl.aetas.android.smscode.basic.SMSProcessor;
import pl.aetas.android.smscode.parser.SMSCodeParser;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSProcessorTest {

    public static final String SMS_BODY = "This is SMS body";
    //SUT
    private SMSProcessor smsProcessor;

    @Mock
    private Clipboard clipboard;

    @Mock
    private SMSInfoPresenter smsInfoPresenter;

    @Mock
    private SMSCodeParser smsCodeParser;

    @Before
    public void setUp() {
        initMocks(this);
        smsProcessor = new SMSProcessor(clipboard, smsCodeParser, smsInfoPresenter);
    }

    @Test
    public void shouldCopyCodeToClipboardFromSMSWhichIsSMSWithCode() throws Exception {
        when(smsCodeParser.checkIfBodyContainsCode(SMS_BODY)).thenReturn(true);
        when(smsCodeParser.retrieveCodeFromSMSBody(SMS_BODY)).thenReturn("123456");

        smsProcessor.processSMS(SMS_BODY);
        verify(clipboard).save("123456");
    }

    @Test
    public void shouldNotTryToCopyCodeToClipboardFromSMSWhichIsNotSMSWithCode() throws Exception {
        when(smsCodeParser.checkIfBodyContainsCode(SMS_BODY)).thenReturn(false);

        smsProcessor.processSMS(SMS_BODY);
        verify(clipboard, never()).save(anyString());
    }

    @Test
    public void shouldPresentInfoToUserWhenSMSIsSMSWithCode() throws Exception {
        when(smsCodeParser.checkIfBodyContainsCode(SMS_BODY)).thenReturn(true);
        when(smsCodeParser.retrieveCodeFromSMSBody(SMS_BODY)).thenReturn("123456");

        smsProcessor.processSMS(SMS_BODY);

        verify(smsInfoPresenter).presentInfoToUserIfChosen("123456");
    }

    @Test
    public void shouldNotTryToPresentInfoToUserWhenSMSInNotSMSWithCode() throws Exception {
        when(smsCodeParser.checkIfBodyContainsCode(SMS_BODY)).thenReturn(false);
        smsProcessor.processSMS(SMS_BODY);
        verify(smsInfoPresenter, never()).presentInfoToUserIfChosen(anyString());
    }
}




