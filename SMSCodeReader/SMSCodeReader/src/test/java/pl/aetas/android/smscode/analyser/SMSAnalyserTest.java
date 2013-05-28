package pl.aetas.android.smscode.analyser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.KnownSendersResource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSAnalyserTest {

    public static final String KNOWN_SENDER = "someKnownSender";
    public static final String UNKNOWN_SENDER = "unknownSender";
    public static final String SMS_BODY_WITH_CODE = "This is body of the message with code 654321 in it.";
    public static final String SMS_BODY_WITHOUT_CODE = "This is body of the message without code in it.";

    // SUT
    private SMSAnalyser smsAnalyser;
    @Mock
    private KnownSendersResource knownSendersResource;
    @Mock
    private SMSCodeParser smsCodeParser;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsAnalyser = new SMSAnalyser(knownSendersResource, smsCodeParser);

        when(knownSendersResource.isSenderKnown(KNOWN_SENDER)).thenReturn(true);
        when(knownSendersResource.isSenderKnown(UNKNOWN_SENDER)).thenReturn(false);
        when(smsCodeParser.checkIfBodyContainsCode(SMS_BODY_WITH_CODE)).thenReturn(true);
        when(smsCodeParser.checkIfBodyContainsCode(SMS_BODY_WITHOUT_CODE)).thenReturn(false);
    }

    @Test
    public void shouldGetCodeFromSMSBodyAndReturnSMSInfoWithThisCodeWhenSenderIsKnown() throws Exception {
        String code = "654321";
        when(smsCodeParser.retrieveCodeFromSMSBodyForKnownSender(KNOWN_SENDER, SMS_BODY_WITH_CODE)).thenReturn(code);
        SMSInfo smsInfo = smsAnalyser.analyse(KNOWN_SENDER, SMS_BODY_WITH_CODE);
        assertThat(smsInfo.getCode(), is(equalTo(code)));
    }

    @Test
    public void shouldReturnSMSInfoWithoutCodeWhenSenderIsNotKnown() throws Exception {
        SMSInfo smsInfo = smsAnalyser.analyse(UNKNOWN_SENDER, SMS_BODY_WITH_CODE);
        assertThat(smsInfo.isSMSWithCode(), is(false));
    }

    @Test
    public void shouldReturnSMSInfoWithoutCodeWhenSenderIsKnownButCodeHasNotBeenFoundInSMSBody() throws Exception {
        SMSInfo smsInfo = smsAnalyser.analyse(KNOWN_SENDER, SMS_BODY_WITHOUT_CODE);
        assertThat(smsInfo.isSMSWithCode(), is(false));

    }

    @Test
    public void shouldReturnSMSInfoWithOfficialSenderNameNotTheSenderNumber() throws Exception {
        String officialSenderName = "Official sender name";
        when(knownSendersResource.getSenderOfficialName(KNOWN_SENDER)).thenReturn(officialSenderName);
        SMSInfo smsInfo = smsAnalyser.analyse(KNOWN_SENDER, SMS_BODY_WITH_CODE);
        assertThat(smsInfo.getSenderOfficialName(), equalTo(officialSenderName));

    }
}
