package pl.aetas.android.smscode.analyser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.KnownSendersResource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSFilterTest {

    public static final String SMS_SENDER = "someSender";
    public static final String SMS_BODY = "This is body if the text message with the code or without it";
    public static final String KNOWN_SENDER = "someKnownSender";
    public static final String UNKNOWN_SENDER = "unknownSender";
    public static final String SMS_BODY_WITH_CODE = "This is body of the message with code 654321 in it.";
    public static final String SMS_BODY_WITHOUT_CODE = "This is body of the message without code in it.";

    // SUT
    private SMSFilter smsFilter;
    @Mock
    private KnownSendersResource knownSendersResource;
    @Mock
    private SMSCodeParser smsCodeParser;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsFilter = new SMSFilter(knownSendersResource, smsCodeParser, SMS_SENDER);
    }

    @Test
    public void shouldRecogniseSMSAsRelevantIfSenderIsKnownAndCodeHasBeenFoundInBody() throws Exception {
        when(knownSendersResource.isSenderKnown(SMS_SENDER)).thenReturn(true);
        when(smsCodeParser.checkIfBodyContainsCode()).thenReturn(true);

        assertThat(smsFilter.checkIfSMSIsRelevantForCodeReader(), is(true));
    }

    @Test
    public void shouldRecogniseSMSAsIrrelevantIfSenderIsUnknown() throws Exception {
        when(knownSendersResource.isSenderKnown(SMS_SENDER)).thenReturn(false);
        assertThat(smsFilter.checkIfSMSIsRelevantForCodeReader(), is(false));
    }

    @Test
    public void shouldRecogniseSMSAsIrrelevantIfSenderIsKnownButCodeHosNotBeenFoundInBody() throws Exception {
        when(knownSendersResource.isSenderKnown(SMS_SENDER)).thenReturn(true);
        when(smsCodeParser.checkIfBodyContainsCode()).thenReturn(false);

        assertThat(smsFilter.checkIfSMSIsRelevantForCodeReader(), is(false));
    }
}
