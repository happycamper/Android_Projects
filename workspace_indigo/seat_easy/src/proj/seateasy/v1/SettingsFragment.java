package proj.seateasy.v1;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	public static String KEY_LOCATION = "pref_location";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.preferences);
    }

}
