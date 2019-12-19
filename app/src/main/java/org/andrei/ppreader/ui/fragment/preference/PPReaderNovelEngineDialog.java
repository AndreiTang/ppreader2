package org.andrei.ppreader.ui.fragment.preference;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.ListView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineSetting;
import org.andrei.ppreader.ui.adapter.PPReaderEngineInfoAdapter;

import java.util.ArrayList;

public class PPReaderNovelEngineDialog extends PreferenceDialogFragmentCompat {

    public void init(IPPReaderDataManager dataManager){
        m_dataManager = dataManager;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            ListView lv = getDialog().findViewById(R.id.engine_list);
            PPReaderEngineInfoAdapter adapter = (PPReaderEngineInfoAdapter)lv.getAdapter();
            m_dataManager.setEngineSettings(adapter.getInfos());
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        ListView lv = view.findViewById(R.id.engine_list);
        ColorDrawable cd = new ColorDrawable(Color.TRANSPARENT);
        lv.setDivider(cd);
        lv.setDividerHeight(5);
        cd = new ColorDrawable(Color.RED);
        lv.setSelector(cd);

        final PPReaderEngineInfoAdapter adapter = new PPReaderEngineInfoAdapter();
        ArrayList<PPReaderEngineSetting> infos = new ArrayList<>();
        for(int i = 0; i < m_dataManager.getEngineSettingCount(); i++){
            infos.add(m_dataManager.getEngineSetting(i));
        }
        adapter.init(this,infos);
        lv.setAdapter(adapter);
    }

    private IPPReaderDataManager m_dataManager;

}
