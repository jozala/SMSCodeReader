package pl.aetas.android.smscode.smsprocessor;

import android.text.ClipboardManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.presenter.Clipboard;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ClipboardTest {

    // SUT
    private Clipboard clipboard;
    @Mock
    private ClipboardManager clipboardManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clipboard = new Clipboard(clipboardManager);
    }

    @Test
    public void shouldSaveGivenStringToClipboardOnSave() throws Exception {
        clipboard.save("123456");

        verify(clipboardManager).setText("123456");
    }
}
