package org.andrei.ppreader.ui.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderSearchAdapter extends BaseAdapter {

    public PPReaderSearchAdapter(Fragment parent, IPPReaderTaskNotification notification){
        m_parent = parent;
        m_notification = m_notification;
    }

    @Override
    public int getCount() {
        return m_novels.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addNovel(PPReaderNovel novel){
        m_novels.add(novel);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = createView();
        }
        updateView(position,convertView);
        return convertView;
    }

    private View createView(){
        final View v = m_parent.getLayoutInflater().inflate(R.layout.view_ppreader_search,null);
        RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
               int index= (Integer) v.getTag();
               PPReaderNovel novel = m_novels.get(index);
                PPReaderSelectNovelRet ret = new PPReaderSelectNovelRet();
                ret.novel = novel;
                m_notification.onNotify(ret);
            }
        });
        return v;
    }

    private void updateView(int i, View view) {
        if (i >= m_novels.size()) {
            return;
        }
        PPReaderNovel novel = m_novels.get(i);
        TextView tx = (TextView) view.findViewById(R.id.novel_search_author);
        tx.setText(novel.author);
        tx = (TextView) view.findViewById(R.id.novel_search_decs);
        tx.setText(novel.desc);
        tx = (TextView) view.findViewById(R.id.novel_search_name);
        tx.setText(novel.name);
        tx = (TextView) view.findViewById(R.id.novel_search_type);
        if (novel.type == PPReaderNovel.TYPE_ING) {
            tx.setText(R.string.novel_type_ing);
        } else {
            tx.setText(R.string.novel_type_over);
        }

        ImageView img = (ImageView) view.findViewById(R.id.novel_search_cover);
        Glide.with(view).clear(img);
        Glide.with(view).load(novel.img).apply(RequestOptions.fitCenterTransform().error(R.drawable.nocover)).into(img);

        view.setTag(i);

    }

    private ArrayList<PPReaderNovel> m_novels = new ArrayList<>();
    private Fragment m_parent;
    private IPPReaderTaskNotification m_notification;

}
