package pl.aetas.android.smscode.analyser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.SendersResource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SMSFilterTest {

    public static final String SMS_SENDER = "someSender";

    // SUT
    private SMSFilter smsFilter;
    @Mock
    private SendersResource sendersResource;
    @Mock
    private SMSCodeParser smsCodeParser;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsFilter = new SMSFilter(sendersResource, smsCodeParser, SMS_SENDER);
    }

    @Test
    public void shouldRecogniseSMSAsRelevantIfSenderIsKnownAndCodeHasBeenFoundInBody() throws Exception {
        when(sendersResource.isSenderKnown(SMS_SENDER)).thenReturn(true);
        when(smsCodeParser.checkIfBodyContainsCode()).thenReturn(true);

        assertThat(smsFilter.checkIfSMSIsRelevantForCodeReader(), is(true));
    }

    @Test
    public void shouldRecogniseSMSAsIrrelevantIfSenderIsUnknown() throws Exception {
        when(sendersResource.isSenderKnown(SMS_SENDER)).thenReturn(false);
        assertThat(smsFilter.checkIfSMSIsRelevantForCodeReader(), is(false));
    }

    @Test
    public void shouldRecogniseSMSAsIrrelevantIfSenderIsKnownButCodeHasNotBeenFoundInBody() throws Exception {
        when(sendersResource.isSenderKnown(SMS_SENDER)).thenReturn(true);
        when(smsCodeParser.checkIfBodyContainsCode()).thenReturn(false);

        assertThat(smsFilter.checkIfSMSIsRelevantForCodeReader(), is(false));
    }
}
