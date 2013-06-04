package pl.aetas.android.smscode.model;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SenderTest {

    // SUT
    private Sender sender;
    @Mock
    private CodesRegularExpressions codesRegularExpressions;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sender = new Sender("SenderName", "SenderOfficialName", codesRegularExpressions);
    }
}
