package pl.aetas.android.smscode.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.R;
import pl.aetas.android.smscode.db.SMSCodeReaderSQLiteHelper;
import pl.aetas.android.smscode.model.Sender;
import pl.aetas.android.smscode.resource.SendersResource;

import java.util.LinkedList;
import java.util.List;

import static android.preference.Preference.OnPreferenceClickListener;


public class PreferencesActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        Log.init(this);
        if (android.os.Build.VERSION.SDK_INT >= 6 && android.os.Build.VERSION.SDK_INT < 11) {
            // there's a display bug in 2.1, 2.2, 2.3 (unsure about 2.0)
            // which causes PreferenceScreens to have a black background.
            // http://code.google.com/p/android/issues/detail?id=4611
            setTheme(android.R.style.Theme_Black);
        }

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setupAboutPreference();
        addSenders();

    }

    private void setupAboutPreference() {
        findPreference("about").setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                Intent aboutActivity = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(aboutActivity);
                return true;
            }
        });
    }

    private void addSenders() {

        final List<CheckBoxPreference> sendersPreferenceList = getSendersListForPreferences();

        final PreferenceScreen supportedSendersPreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("supportedSenders");
        for (CheckBoxPreference senderPreferenceCheckBox : sendersPreferenceList) {
            supportedSendersPreferenceScreen.addPreference(senderPreferenceCheckBox);
        }
    }

    private List<CheckBoxPreference> getSendersListForPreferences() {
        final List<CheckBoxPreference> sendersPreferences = new LinkedList<CheckBoxPreference>();
        final SendersResource sendersResource = new SendersResource(new SMSCodeReaderSQLiteHelper(this));
        final List<Sender> allSenders = sendersResource.getAllSenders();

        for (Sender sender : allSenders) {
            final CheckBoxPreference senderCheckBoxPreference = new CheckBoxPreference(this);
            senderCheckBoxPreference.setTitle(sender.getOfficialName());
            senderCheckBoxPreference.setKey(sender.getOfficialName());
            senderCheckBoxPreference.setDefaultValue(true);
            senderCheckBoxPreference.setEnabled(true);
            sendersPreferences.add(senderCheckBoxPreference);
        }


        return sendersPreferences;
    }


}