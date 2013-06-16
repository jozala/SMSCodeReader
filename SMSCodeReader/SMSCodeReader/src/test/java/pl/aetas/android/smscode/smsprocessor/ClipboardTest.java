package pl.aetas.android.smscode.smsprocessor;

import android.content.ClipData;
import android.content.ClipboardManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.presenter.Clipboard;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
        ArgumentCaptor model = ArgumentCaptor.forClass(ClipData.class);

        clipboard.save("123456");

        verify(clipboardManager).setPrimaryClip((ClipData) model.capture());

        ClipData clipData = (ClipData) model.getValue();
        assertThat(clipData.getItemAt(0).getText().toString(), equalTo("123456"));
    }
}
