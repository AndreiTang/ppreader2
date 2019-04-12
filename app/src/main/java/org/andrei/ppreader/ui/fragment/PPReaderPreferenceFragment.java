package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import org.andrei.ppreader.R;

public class PPReaderPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.test);
    }
}
