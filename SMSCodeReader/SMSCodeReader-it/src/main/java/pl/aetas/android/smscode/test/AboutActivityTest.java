package pl.aetas.android.smscode.test;

import android.test.ActivityInstrumentationTestCase2;
import pl.aetas.android.smscode.activity.AboutActivity;

public class AboutActivityTest extends ActivityInstrumentationTestCase2<AboutActivity> {

    public AboutActivityTest() {
        super(AboutActivity.class);
    }

    public void testActivity() {
        AboutActivity activity = getActivity();
        assertNotNull(activity);
    }
}

