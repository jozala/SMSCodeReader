package pl.aetas.android.smscode.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;
import pl.aetas.android.smscode.resource.KnownSendersResource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SMSCodeParserTest {

    private static final String UNKNOWN_SENDER = "unknownSender";
    private static final String KNOWN_SENDER_WITHOUT_CODES = "knownSenderWithoutCodes";
    public static final String KNOWN_SENDER_WITH_CODES = "knownSenderWithCodes";

    // SUT
    private SMSCodeParser smsCodeParser;

    @Mock
    private CodesRegularExpressionsResource codesRegularExpressionsResource;
    @Mock
    private KnownSendersResource knownSendersResource;
    @Mock
    private CodesRegularExpressions codesRegularExpressions;
    @Mock
    private CodeRegularExpression codeRegularExpression;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsCodeParser = new SMSCodeParser(codesRegularExpressionsResource);

        when(knownSendersResource.isSenderKnown(KNOWN_SENDER_WITH_CODES)).thenReturn(true);
    }

    @Test(expected = UnknownSenderException.class)
    public void shouldThrowExceptionWhenSenderUnknown() throws Exception {
        when(codesRegularExpressionsResource.getCodesRegularExpressionsForSender()).thenThrow(new UnknownSenderException("Mocked Exception"));
        smsCodeParser.retrieveCodeFromSMSBodyForKnownSender();
    }

    @Test(expected = NoCodesForKnownSenderException.class)
    public void shouldThrowExceptionWhenNoCodesForKnownSenderExists() throws Exception {
        when(codesRegularExpressionsResource.getCodesRegularExpressionsForSender()).thenThrow(new NoCodesForKnownSenderException("Mocked Exception"));
        smsCodeParser.retrieveCodeFromSMSBodyForKnownSender();
    }

    @Test
    public void shouldRetrieveCodeFromSMSBodyUsingRegularExpressionFromResource() throws Exception {
        when(codesRegularExpressionsResource.getCodesRegularExpressionsForSender()).thenReturn(codesRegularExpressions);
        String smsBody = "Some SMS body with some code: 147852 inside";
        when(codesRegularExpressions.getMatchingRegularExpression()).thenReturn(codeRegularExpression);
        when(codeRegularExpression.getCodeFromString()).thenReturn("147852");

        assertThat(smsCodeParser.retrieveCodeFromSMSBodyForKnownSender(), equalTo("147852"));
    }

    @Test
    public void shouldReturnTrueIfBodyContainsCode() throws Exception {
        String smsBody = "Some SMS body with some code: 147852 inside";
        when(codesRegularExpressionsResource.getCodesRegularExpressionsForSender()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.checkIfBodyContainsCode()).thenReturn(true);
        boolean containsCode = smsCodeParser.checkIfBodyContainsCode();
        assertThat(containsCode, is(true));
    }

    @Test
    public void shouldReturnFalseIfBodyDoesNotContainsCode() throws Exception {
        String smsBody = "Some SMS body without any code included";
        when(codesRegularExpressionsResource.getCodesRegularExpressionsForSender()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.checkIfBodyContainsCode()).thenReturn(false);

        boolean containsCode = smsCodeParser.checkIfBodyContainsCode();
        assertThat(containsCode, is(false));
    }
}
