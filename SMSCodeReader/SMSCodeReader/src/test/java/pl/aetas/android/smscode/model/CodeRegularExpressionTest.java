package pl.aetas.android.smscode.model;

import org.junit.Before;
import org.junit.Test;
import pl.aetas.android.smscode.exception.CodeNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CodeRegularExpressionTest {

    // SUT
    private CodeRegularExpression codeRegularExpression;

    @Before
    public void setUp() throws Exception {
        codeRegularExpression = new CodeRegularExpression(".*(\\s?code: )(\\S+)(\\s?).*");
    }

    @Test
    public void shouldRetrieveCodeFromString() throws Exception {
        assertThat(codeRegularExpression.getCodeFromString("This is body of the text message to find code: 147852 in it"), equalTo("147852"));
    }

    @Test(expected = CodeNotFoundException.class)
    public void shouldThrowExceptionIfCodeNotFoundInGivenString() throws Exception {
        codeRegularExpression.getCodeFromString("String which does not contain code");
    }
}
