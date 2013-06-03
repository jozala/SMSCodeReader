package pl.aetas.android.smscode.model;

import org.junit.Before;
import org.junit.Test;
import pl.aetas.android.smscode.exception.CodeNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CodeRegularExpressionTest {

    // SUT
    private CodeRegularExpression codeRegularExpression;

    @Before
    public void setUp() throws Exception {
        codeRegularExpression = new CodeRegularExpression(".*(\\s?code: )(\\S+)(\\s?).*", 2);
    }

    @Test
    public void shouldRetrieveCodeFromString() throws Exception {
        assertThat(codeRegularExpression.getCodeFromString("This is body of the text message to find code: 147852 in it"), equalTo("147852"));
    }

    @Test(expected = CodeNotFoundException.class)
    public void shouldThrowExceptionIfCodeNotFoundInGivenString() throws Exception {
        codeRegularExpression.getCodeFromString("String which does not contain code");
    }

    @Test
    public void shouldReturnFalseWhenGivenStringCannotBeMatchedAgainstRegularExpression() throws Exception {
        final String stringToMatch = "String which does not match against set regular expression code";
        final boolean isMatch = codeRegularExpression.matches(stringToMatch);
        assertThat(isMatch, is(false));
    }

    @Test
    public void shouldReturnTrueWhenGivenStringMatchesAgainsRegularExpression() throws Exception {
        String stringToMatch = "String which does match against set regular expression with code: 123456789";
        final boolean isMatch = codeRegularExpression.matches(stringToMatch);
        assertThat(isMatch, is(true));
    }
}
