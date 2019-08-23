package org.andrei.ppreader.ui.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderRangeAdapter extends PPReaderBaseAdapter {

    public PPReaderRangeAdapter(PPReaderNovel novel,Fragment parent){
        m_parent = parent;

        int ranges = novel.chapters.size()/50;
        if(novel.chapters.size()%50 > 0)
        {
            ranges++;
        }
        for(int i = 0; i < ranges; i++){
            String name = m_parent.getString(R.string.novel_catalog_chapter);
            int end = (i+1)*50;
            if(i == ranges -1 && end >= novel.chapters.size()){
                end =  novel.chapters.size();
            }
            name = String.format(name,i*50+1,end);
            m_ranges.add(name);
        }

    }

    @Override
    public int getCount() {
        return m_ranges.size();
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
        if (convertView == null) {
            convertView = m_parent.getLayoutInflater().inflate(R.layout.view_ppreader_catalog_item, null);
            final View v = convertView;
            RxView.clicks(convertView).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object obj) throws Exception {
                    int index = (Integer) v.getTag();
                    PPReaderCommonMessage message = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_SET_RANGE,index*50);
                    sendMessage(message);
                }
            });
        }
        TextView tv = convertView.findViewById(R.id.novel_catalog_item);
        tv.setText(m_ranges.get(position));
        convertView.setTag(position);
        return convertView;
    }

    private ArrayList<String> m_ranges = new ArrayList<>();
    private Fragment m_parent;
}
