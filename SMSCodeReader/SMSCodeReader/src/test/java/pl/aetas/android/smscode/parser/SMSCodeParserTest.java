package pl.aetas.android.smscode.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.model.Sender;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SMSCodeParserTest {

    public static final String SMS_BODY = "This is body of the SMS with some code";

    // SUT
    private SMSCodeParser smsCodeParser;

    @Mock
    private Sender sender;
    @Mock
    private CodesRegularExpressions codesRegularExpressions;
    @Mock
    private CodeRegularExpression codeRegularExpression;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsCodeParser = new SMSCodeParser(sender);

    }

    @Test
    public void shouldRetrieveCodeUsingRegularExpressionFromResource() throws Exception {
        when(sender.getCodesRegularExpressions()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.getMatchingRegularExpression(SMS_BODY)).thenReturn(codeRegularExpression);
        when(codeRegularExpression.getCodeFromString(SMS_BODY)).thenReturn("147852");

        assertThat(smsCodeParser.retrieveCodeFromSMSBodyForKnownSender(SMS_BODY), equalTo("147852"));
    }

    @Test
    public void shouldReturnTrueIfBodyContainsCode() throws Exception {
        when(sender.getCodesRegularExpressions()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.checkIfBodyContainsCode(SMS_BODY)).thenReturn(true);
        boolean containsCode = smsCodeParser.checkIfBodyContainsCode(SMS_BODY);
        assertThat(containsCode, is(true));
    }

    @Test
    public void shouldReturnFalseIfBodyDoesNotContainsCode() throws Exception {
        when(sender.getCodesRegularExpressions()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.checkIfBodyContainsCode(SMS_BODY)).thenReturn(false);

        boolean containsCode = smsCodeParser.checkIfBodyContainsCode(SMS_BODY);
        assertThat(containsCode, is(false));
    }
}
