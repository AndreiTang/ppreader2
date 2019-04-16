package org.andrei.ppreader.ui.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderEngineInfo;

import java.util.ArrayList;

public class PPReaderEngineInfoAdapter extends BaseAdapter {

    public void init(Fragment parent, ArrayList<PPReaderEngineInfo> infos){
        m_parent = parent;
        m_infos = infos;
    }

    @Override
    public int getCount() {
        if(m_infos == null){
            return 0;
        }
        return  m_infos.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = m_parent.getLayoutInflater().inflate(R.layout.view_ppreader_novelengine_item,null);
        }
        PPReaderEngineInfo info = m_infos.get(position);
        CheckBox cb = convertView.findViewById(R.id.engine_item);
        cb.setText(info.name);
        cb.setChecked(info.isUsed);
        return convertView;
    }

    public ArrayList<PPReaderEngineInfo> getInfos(){
        return m_infos;
    }

    private ArrayList<PPReaderEngineInfo> m_infos = null;
    Fragment m_parent;
}
