package org.andrei.ppreader.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;


import org.andrei.ppreader.R;
import org.andrei.ppreader.ui.fragment.preference.PPReaderNovelEngineDialog;
import org.andrei.ppreader.ui.fragment.preference.PPReaderNovelEnginePreference;

public class PPReaderPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.view_ppreader_setting);

    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        PPReaderNovelEngineDialog dlg = null;
        if(preference instanceof PPReaderNovelEnginePreference){
            dlg = new PPReaderNovelEngineDialog();

        }
        if(dlg != null){
            dlg.setTargetFragment(this,0);
            dlg.show(this.getFragmentManager(),"ko");
            final Bundle b = new Bundle(1);
            b.putString("key", "key");
            dlg.setArguments(b);
        }
        else{
            super.onDisplayPreferenceDialog(preference);
        }


    }

}
