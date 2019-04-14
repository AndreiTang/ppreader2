package org.andrei.ppreader.ui.fragment.preference;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import org.andrei.ppreader.R;

public class PPReaderNovelEnginePreference extends DialogPreference {
    public PPReaderNovelEnginePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PPReaderNovelEnginePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PPReaderNovelEnginePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PPReaderNovelEnginePreference(Context context) {
        super(context);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.view_ppreader_novelengine_setting;
    }

}
