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

    public void loadNovel(final PPReaderNovel novel,final String imageRootUrl){
        m_novel = novel;
        m_imageRootUrl = imageRootUrl;
    }

    public void addOnClickItemNotify(PPReaderCatalogAdapter.IPPReaderCatalogAdapterNotify notify){
        m_notify = notify;
    }

    public void show(int curIndex, long duration){
        setVisibility(View.VISIBLE);
        setNovelDetail();
        initAdaptersFromNovel();
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
        Glide.with(this).load(m_imageRootUrl + m_novel.img).apply(RequestOptions.fitCenterTransform()).into(img);
    }

    private void initAdaptersFromNovel(){
        final ListView chapterList = findViewById(R.id.novel_catalog_chapter_list);
        BaseAdapter adapter = new PPReaderCatalogAdapter(m_novel, getContext(), new PPReaderCatalogAdapter.IPPReaderCatalogAdapterNotify() {
            @Override
            public void onClickItem(int index) {
                if(m_notify != null){
                    m_notify.onClickItem(index);
                }
            }
        });
        chapterList.setAdapter(adapter);

        final ListView rangeList = findViewById(R.id.novel_catalog_range_list);
        adapter= new PPReaderRangeAdapter(m_novel, getContext(), new PPReaderRangeAdapter.IPPReaderRangeAdapterNotify() {
            @Override
            public void onNotify(int curIndex) {
//                m_notify.onClickItem(curIndex);
                chapterList.setSelection(curIndex);
                chapterList.setVisibility(View.VISIBLE);
                rangeList.setVisibility(View.GONE);
            }
        });
        rangeList.setAdapter(adapter);
    }


    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_ppreader_catalog,this,true);
        initClickEvents();
    }

    private void initClickEvents(){
        //click to return reader page
        View mask = findViewById(R.id.novel_catalog_mask);
        RxView.clicks(mask).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                setVisibility(View.GONE);
            }
        });

        //switch between group and dict
        View rangView = findViewById(R.id.novel_catalog_range);
        RxView.clicks(rangView).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                View chapterList = findViewById(R.id.novel_catalog_chapter_list);
                View rangList = findViewById(R.id.novel_catalog_range_list);
                if (chapterList.getVisibility() == View.GONE) {
                    chapterList.setVisibility(View.VISIBLE);
                    rangList.setVisibility(View.GONE);
                } else {
                    chapterList.setVisibility(View.GONE);
                    rangList.setVisibility(View.VISIBLE);
                }

            }
        });

        //prev page
        View  btn = findViewById(R.id.novel_catalog_prev);
        RxView.clicks(btn).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                ListView chapterList = findViewById(R.id.novel_catalog_chapter_list);
                ListView rangList = findViewById(R.id.novel_catalog_range_list);
                ListView lv = chapterList;
                if (rangList.getVisibility() == View.VISIBLE) {
                    lv = rangList;
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
        btn = findViewById(R.id.novel_catalog_next);
        RxView.clicks(btn).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                ListView chapterList = findViewById(R.id.novel_catalog_chapter_list);
                ListView rangList = findViewById(R.id.novel_catalog_range_list);
                ListView lv = chapterList;
                if (rangList.getVisibility() == View.VISIBLE) {
                    lv = rangList;
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
    private String m_imageRootUrl;
}
