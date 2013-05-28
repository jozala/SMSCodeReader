package smscode.basic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.analyser.SMSAnalyser;
import pl.aetas.android.smscode.analyser.SMSInfo;
import pl.aetas.android.smscode.basic.Clipboard;
import pl.aetas.android.smscode.basic.SMSInfoPresenter;
import pl.aetas.android.smscode.basic.SMSReader;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSReaderTest {

    private static final String KNOWN_BANK_SENDER = "KnownBankSender";
    private static final String SMS_BODY_WITH_CODE = "This body of the message contains code here: 123456.";
    public static final String UNKNOWN_BANK_SENDER = "unknownBankSender";
    public static final String SMS_BODY_WITHOUT_CODE = "This body of the message does not contain code.";

    //SUT
    private SMSReader smsReader;

    @Mock
    private Clipboard clipboard;

    @Mock
    private SMSAnalyser smsAnalyser;

    @Mock
    private SMSInfoPresenter smsInfoPresenter;

    @Mock
    private SMSInfo smsInfo;

    @Before
    public void setUp() {
        initMocks(this);
        smsReader = new SMSReader(smsAnalyser, clipboard, smsInfoPresenter);

        when(smsAnalyser.analyse(anyString(), anyString())).thenReturn(smsInfo);
    }

    @Test
    public void shouldCopyCodeToClipboardFromSMSWhichSenderIsKnownAndContainCode() throws Exception {
        when(smsInfo.isSenderKnown()).thenReturn(true);
        when(smsInfo.isContainsCode()).thenReturn(true);
        when(smsInfo.getCode()).thenReturn("123456");

        smsReader.readSMS(KNOWN_BANK_SENDER, SMS_BODY_WITH_CODE);
        verify(clipboard).save("123456");
    }

    @Test
    public void shouldNotTryToCopyCodeToClipboardFromSMSWhichSenderIsNotKnown() throws Exception {
        when(smsInfo.isSenderKnown()).thenReturn(false);
        when(smsInfo.isContainsCode()).thenReturn(true);

        smsReader.readSMS(UNKNOWN_BANK_SENDER, SMS_BODY_WITH_CODE);
        verify(clipboard, never()).save(anyString());
    }

    @Test
    public void shouldNotTryToCopyCodeToClipboardFromSMSWhichDoesNotContainCode() throws Exception {
        when(smsInfo.isSenderKnown()).thenReturn(true);
        when(smsInfo.isContainsCode()).thenReturn(false);

        smsReader.readSMS(KNOWN_BANK_SENDER, SMS_BODY_WITHOUT_CODE);
        verify(clipboard, never()).save(anyString());
    }

    @Test
    public void shouldPresentInfoToUserWhenSMSIsFromKnownSenderAndContainsCode() throws Exception {
        when(smsInfo.isSenderKnown()).thenReturn(true);
        when(smsInfo.isContainsCode()).thenReturn(true);

        smsReader.readSMS(KNOWN_BANK_SENDER, SMS_BODY_WITH_CODE);

        verify(smsInfoPresenter).presentInfoToUserIfChosen(smsInfo);
    }
}




