package org.andrei.ppreader.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.ui.adapter.PPReaderCatalogAdapter;
import org.andrei.ppreader.ui.adapter.PPReaderRangeAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderNovelTextCatalog extends LinearLayout {
    public PPReaderNovelTextCatalog(Context context) {
        super(context);
        init(context);
    }

    public PPReaderNovelTextCatalog(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PPReaderNovelTextCatalog(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PPReaderNovelTextCatalog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void loadNovel(final PPReaderNovel novel){
        m_novel = novel;
        setNovelDetail();
        initAdaptersFromNovel();
    }

    public void addOnClickItemNotify(PPReaderCatalogAdapter.IPPReaderCatalogAdapterNotify notify){
        m_notify = notify;
    }

    public void show(int curIndex, long duration){
        setVisibility(View.VISIBLE);
        setDuration(duration);
        setCurrentChapter(curIndex);
    }

    private void setCurrentChapter(int curIndex){
        ListView listView = findViewById(R.id.novel_catalog_chapter_list);
        listView.setSelection(curIndex);
    }

    private void setDuration(long duration){
        long h = duration/3600;
        long m = (duration%3600)/60;
        String ds = getContext().getString(R.string.novel_catalog_duration);
        ds = String.format(ds,h,m);
        TextView tv = findViewById(R.id.novel_catalog_duration);
        tv.setText(ds);
    }

    private void setNovelDetail(){
        TextView tv = findViewById(R.id.novel_catalog_author);
        tv.setText(m_novel.author);
        tv = findViewById(R.id.novel_catalog_name);
        tv.setText(m_novel.name);
        ImageView img = findViewById(R.id.novel_catalog_img);
        Glide.with(this).clear(img);
        Glide.with(this).load(m_novel.img).apply(RequestOptions.fitCenterTransform()).into(img);
    }

    private void initAdaptersFromNovel(){
        ListView listView = findViewById(R.id.novel_catalog_chapter_list);
        BaseAdapter adapter = new PPReaderCatalogAdapter(m_novel, getContext(), new PPReaderCatalogAdapter.IPPReaderCatalogAdapterNotify() {
            @Override
            public void onClickItem(int index) {
                if(m_notify != null){
                    m_notify.onClickItem(index);
                }
            }
        });
        listView.setAdapter(adapter);

        listView = findViewById(R.id.novel_catalog_range_list);
        adapter= new PPReaderRangeAdapter(m_novel, getContext(), new PPReaderRangeAdapter.IPPReaderRangeAdapterNotify() {
            @Override
            public void onNotify(int curIndex) {
                setCurrentChapter(curIndex);
            }
        });

    }


    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_ppreader_catalog,this,true);
        initClickEvents();
    }

    private void initClickEvents(){
        //click to return reader page
        View v = findViewById(R.id.novel_catalog_mask);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                setVisibility(View.GONE);
            }
        });

        //switch between group and dict
        v = findViewById(R.id.novel_catalog_range);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                View l = findViewById(R.id.novel_catalog_chapter_list);
                View g = findViewById(R.id.novel_catalog_range_list);
                if (l.getVisibility() == View.GONE) {
                    l.setVisibility(View.VISIBLE);
                    g.setVisibility(View.GONE);
                } else {
                    l.setVisibility(View.GONE);
                    g.setVisibility(View.VISIBLE);
                }

            }
        });

        //prev page
        v = findViewById(R.id.novel_catalog_prev);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                ListView l = findViewById(R.id.novel_catalog_chapter_list);
                ListView g = findViewById(R.id.novel_catalog_range_list);
                ListView lv = l;
                if (g.getVisibility() == View.VISIBLE) {
                    lv = g;
                }

                int begin = lv.getFirstVisiblePosition() - 1;
                int len = lv.getLastVisiblePosition() - lv.getFirstVisiblePosition();
                begin = begin - len;
                if (begin >= 0) {
                    lv.setSelection(begin);
                }
                else{
                    lv.setSelection(0);
                }
            }
        });

        //next page
        v = findViewById(R.id.novel_catalog_next);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                ListView l = findViewById(R.id.novel_catalog_chapter_list);
                ListView g = findViewById(R.id.novel_catalog_range_list);
                ListView lv = l;
                if (g.getVisibility() == View.VISIBLE) {
                    lv = g;
                }
                int end = lv.getLastVisiblePosition() + 1 ;
                if(end <= lv.getAdapter().getCount()-1){
                    lv.setSelection(end);
                }
            }
        });
    }

    private PPReaderNovel m_novel;
    private PPReaderCatalogAdapter.IPPReaderCatalogAdapterNotify m_notify;
}
