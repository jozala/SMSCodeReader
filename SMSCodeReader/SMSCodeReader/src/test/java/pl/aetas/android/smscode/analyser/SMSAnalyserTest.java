package pl.aetas.android.smscode.analyser;

import org.junit.Before;
import org.junit.Test;

public class SMSAnalyserTest {

    // SUT
    private SMSAnalyser smsAnalyser;

    @Before
    public void setUp() throws Exception {
        smsAnalyser = new SMSAnalyser();
    }

    @Test
    public void shouldGetCodeFromSMSBodyAndReturnSMSInfoWithThisCode() throws Exception {
//        SMSInfo smsInfo = smsAnalyser.analyse("someKnownSender", "This is body of the message with code 654321 in it.");
//        assertThat(smsInfo.getCode(), is(equalTo("654321")));
    }
}
