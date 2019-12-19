package org.andrei.ppreader.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.PPReader;
import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineSetting;
import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderSearchAdapter extends BaseAdapter {

    public interface IPPReaderSearchAdapterAction{
        void openNovel(PPReaderNovel novel);
    }

    public PPReaderSearchAdapter(Fragment parent,IPPReaderSearchAdapterAction action){
        m_parent = parent;
        m_action = action;
        m_dataManager  = PPReader.getDataManager();
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

    public void saveInstanceState(Bundle savedInstanceState,String key){
        if(m_novels.size() > 0){
            savedInstanceState.putSerializable(key,m_novels);
        }
    }

    public void clear(){
        m_novels.clear();
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
               if(m_action != null){
                   m_action.openNovel(novel);
               }
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

        PPReaderEngineSetting info = m_dataManager.getEngineSetting(novel.engineName);
        String imgUrl =  info.imageUrl + novel.img;
        Glide.with(view).load(imgUrl).apply(RequestOptions.fitCenterTransform().error(R.drawable.nocover)).into(img);

        view.setTag(i);

    }

    private ArrayList<PPReaderNovel> m_novels = new ArrayList<>();
    private Fragment m_parent;
    private IPPReaderSearchAdapterAction m_action;
    private IPPReaderDataManager m_dataManager;
}
