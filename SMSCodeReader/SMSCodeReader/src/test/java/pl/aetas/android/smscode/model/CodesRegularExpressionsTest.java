package pl.aetas.android.smscode.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CodesRegularExpressionsTest {

    public static final String SMS_BODY = "Some SMS Body";
    // SUT
    private CodesRegularExpressions codesRegularExpressions;

    @Mock
    private CodeRegularExpression regExp1;

    @Mock
    private CodeRegularExpression regExp2;

    @Mock
    private CodeRegularExpression regExp3;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Set<CodeRegularExpression> regExps = new HashSet<CodeRegularExpression>();
        regExps.add(regExp1);
        regExps.add(regExp2);
        regExps.add(regExp3);

        codesRegularExpressions = new CodesRegularExpressions(SMS_BODY, regExps);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionTryingToGetMatchingRegExpWhenNoMatchingRegExpAvailable() throws Exception {
        codesRegularExpressions.getMatchingRegularExpression();
    }

    @Test
    public void shouldReturnMatchingRegularExpression() throws Exception {
        Mockito.when(regExp2.matches(SMS_BODY)).thenReturn(true);
        assertThat(codesRegularExpressions.getMatchingRegularExpression(), is(sameInstance(regExp2)));

    }
}
