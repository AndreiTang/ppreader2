package org.andrei.ppreader.ui.adapter;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.fragment.helper.PPReaderCommonRet;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderCatalogAdapter extends BaseAdapter {

    public PPReaderCatalogAdapter(PPReaderNovel novel, IPPReaderTaskNotification notification, Fragment parent) {
        m_notification = notification;
        m_novel = novel;
        m_parent = parent;
    }

    @Override
    public int getCount() {
        return m_novel.chapters.size();
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
                    PPReaderCommonRet ret = new PPReaderCommonRet(PPReaderCommonRet.TYPE_SET_CURR);
                    ret.index  = index;
                    m_notification.onNotify(ret);
                }
            });
        }
        PPReaderChapter chapter = m_novel.chapters.get(position);
        TextView tv = convertView.findViewById(R.id.novel_catalog_item);
        tv.setText(chapter.title);
        convertView.setTag(position);
        return convertView;
    }

    private PPReaderNovel m_novel;
    private IPPReaderTaskNotification m_notification;
    private Fragment m_parent;

}
