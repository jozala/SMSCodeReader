package pl.aetas.android.smscode.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SMSCodeParserTest {


    // SUT
    private SMSCodeParser smsCodeParser;

    @Mock
    private CodesRegularExpressionsResource codesRegularExpressionsResource;
    @Mock
    private CodesRegularExpressions codesRegularExpressions;
    @Mock
    private CodeRegularExpression codeRegularExpression;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        smsCodeParser = new SMSCodeParser(codesRegularExpressionsResource);

    }

    @Test
    public void shouldRetrieveCodeUsingRegularExpressionFromResource() throws Exception {
        when(codesRegularExpressionsResource.getCodesRegularExpressions()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.getMatchingRegularExpression()).thenReturn(codeRegularExpression);
        String smsBody = "This is body of the SMS with some code";
        when(codeRegularExpression.getCodeFromString(smsBody)).thenReturn("147852");

        assertThat(smsCodeParser.retrieveCodeFromSMSBodyForKnownSender(smsBody), equalTo("147852"));
    }

    @Test
    public void shouldReturnTrueIfBodyContainsCode() throws Exception {
        when(codesRegularExpressionsResource.getCodesRegularExpressions()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.checkIfBodyContainsCode()).thenReturn(true);
        boolean containsCode = smsCodeParser.checkIfBodyContainsCode();
        assertThat(containsCode, is(true));
    }

    @Test
    public void shouldReturnFalseIfBodyDoesNotContainsCode() throws Exception {
        when(codesRegularExpressionsResource.getCodesRegularExpressions()).thenReturn(codesRegularExpressions);
        when(codesRegularExpressions.checkIfBodyContainsCode()).thenReturn(false);

        boolean containsCode = smsCodeParser.checkIfBodyContainsCode();
        assertThat(containsCode, is(false));
    }
}
