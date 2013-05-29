package pl.aetas.android.smscode.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;

import static org.mockito.MockitoAnnotations.initMocks;

public class SMSCodeParserTest {

    // SUT
    private SMSCodeParser smsCodeParser;

    @Mock
    private CodesRegularExpressionsResource codesRegularExpressionsResource;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsCodeParser = new SMSCodeParser(codesRegularExpressionsResource);
    }

    @Test(expected = UnknownSenderException.class)
    public void shouldThrowExceptionWhenNoCodesForGivenSenderExists() throws Exception {
        smsCodeParser.retrieveCodeFromSMSBodyForKnownSender("unknownSender", "Some SMS body");

    }
}
