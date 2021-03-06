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

        View down = convertView.findViewById(R.id.engine_down);
        View up = convertView.findViewById(R.id.engine_up);
        convertView.findViewById(R.id.engine_up).setVisibility(View.VISIBLE);
        if(info.isSelected){
            down.setVisibility(View.VISIBLE);
            up.setVisibility(View.VISIBLE);
        }
        else{
            down.setVisibility(View.GONE);
            up.setVisibility(View.GONE);
        }
        convertView.setTag(position);
        down.setTag(position);
        up.setTag(position);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                if (pos > 0){
                    PPReaderEngineInfo item = m_infos.remove(pos);
                    m_infos.add(pos-1,item);
                    notifyDataSetChanged();
                }
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                if (pos < m_infos.size() - 1){
                    PPReaderEngineInfo item = m_infos.remove(pos);
                    if(pos + 1 == m_infos.size()){
                        m_infos.add(item);
                    }
                    else{
                        m_infos.add(pos+1,item);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sel = (Integer) v.getTag();
                select(sel);
            }
        });
        return convertView;
    }

    public ArrayList<PPReaderEngineInfo> getInfos(){
        return m_infos;
    }

    public void select(int position){
        for(PPReaderEngineInfo info : m_infos){
            info.isSelected = false;
        }
        m_infos.get(position).isSelected = true;
        notifyDataSetChanged();
    }

    private ArrayList<PPReaderEngineInfo> m_infos = null;
    Fragment m_parent;
}
