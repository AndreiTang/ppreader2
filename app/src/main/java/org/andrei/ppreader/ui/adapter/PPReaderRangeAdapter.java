package org.andrei.ppreader.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderNovel;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderRangeAdapter extends PPReaderBaseAdapter {

    public interface IPPReaderRangeAdapterNotify{
        void onNotify(int curIndex);
    }

    public PPReaderRangeAdapter(PPReaderNovel novel,Context context,IPPReaderRangeAdapterNotify notify){
        m_context = context;
        m_notify = notify;

        int ranges = novel.chapters.size()/50;
        if(novel.chapters.size()%50 > 0)
        {
            ranges++;
        }
        for(int i = 0; i < ranges; i++){
            String name = m_context.getString(R.string.novel_catalog_chapter);
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
            convertView = LayoutInflater.from(m_context).inflate(R.layout.view_ppreader_catalog_item, null);
            final View v = convertView;
            RxView.clicks(convertView).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object obj) throws Exception {
                    int index = (Integer) v.getTag();
                    if(m_notify != null){
                        m_notify.onNotify(index*50);
                    }
                }
            });
        }
        TextView tv = convertView.findViewById(R.id.novel_catalog_item);
        tv.setText(m_ranges.get(position));
        convertView.setTag(position);
        return convertView;
    }

    private ArrayList<String> m_ranges = new ArrayList<>();
    private Context m_context;
    private IPPReaderRangeAdapterNotify m_notify;
}
