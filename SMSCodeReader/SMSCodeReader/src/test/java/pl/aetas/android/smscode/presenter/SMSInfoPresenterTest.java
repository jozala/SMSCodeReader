package pl.aetas.android.smscode.presenter;

import android.content.Context;
import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import pl.aetas.android.smscode.model.Sender;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
public class SMSInfoPresenterTest {

    // SUT
    private SMSInfoPresenter smsInfoPresenter;

    @Mock
    private Context context;

    @Mock
    private Clipboard clipboardMock;

    @Mock
    private Sender senderMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCopyCodeToClipboardWhenPresenterWithAutoCopyAndBasicCodeInfo() throws Exception {
        when(context.getString(anyInt())).thenReturn("Some String");

        smsInfoPresenter = SMSInfoPresenter.createPresenterWithAutoCopyAndBasicCodeInfo(context, clipboardMock, senderMock, "Some SMS Content");
        smsInfoPresenter.presentInfoToUser("123456");
        verify(clipboardMock).save("123456");
    }

    @Test
    public void shouldAskUserIfCopyWhenPresenter() throws Exception {
        smsInfoPresenter = SMSInfoPresenter.createPresenterWithAskingIfCopyAndBasicCodeInfo(context, clipboardMock, senderMock, "Some SMS Content");
        smsInfoPresenter.presentInfoToUser("123456");
        verify(context).startActivity(any(Intent.class));
    }

    @Test
    public void shouldNotCopyCodeToClipboardAutomaticallyWhenPresenterWithAskingIfCopyAndBasicCodeInfo() throws Exception {
        smsInfoPresenter = SMSInfoPresenter.createPresenterWithAskingIfCopyAndBasicCodeInfo(context, clipboardMock, senderMock, "Some SMS Content");
        smsInfoPresenter.presentInfoToUser("123456");
        verify(clipboardMock, never()).save(anyString());
    }

}
