package org.andrei.ppreader.ui.fragment.helper;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.ui.adapter.PPReaderCatalogAdapter;
import org.andrei.ppreader.ui.adapter.PPReaderRangeAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderTextCatalog {

    public PPReaderTextCatalog(View catalogView, PPReaderNovel novel, Fragment fragment,IPPReaderTaskNotification notification){
        m_catalogView = catalogView;
        m_novel = novel;
        m_notification = notification;
        m_fragment = fragment;
        init();
    }

    public void show(int curr, long duration){
        m_catalogView.setVisibility(View.VISIBLE);
        ListView l = m_catalogView.findViewById(R.id.novel_catalog_chapter_list);
        PPReaderCatalogAdapter adapter = new PPReaderCatalogAdapter(m_novel,m_notification,m_fragment);
        l.setAdapter(adapter);
        l.setSelection(curr);

        l = m_catalogView.findViewById(R.id.novel_catalog_range_list);
        PPReaderRangeAdapter ra = new PPReaderRangeAdapter(m_novel, new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                m_catalogView.findViewById(R.id.novel_catalog_chapter_list).setVisibility(View.VISIBLE);
                m_catalogView.findViewById(R.id.novel_catalog_range_list).setVisibility(View.GONE);

                PPReaderCommonRet r = (PPReaderCommonRet)ret;
                ListView l = m_catalogView.findViewById(R.id.novel_catalog_chapter_list);
                l.setSelection(r.index);
            }
        }, m_fragment);
        l.setAdapter(ra);

        long h = duration/3600;
        long m = (duration%3600)/60;
        String ds = m_fragment.getString(R.string.novel_catalog_duration);
        ds = String.format(ds,h,m);
        TextView tv = m_fragment.getView().findViewById(R.id.novel_catalog_duration);
        tv.setText(ds);
    }

    private void init(){
        TextView tv = m_catalogView.findViewById(R.id.novel_catalog_author);
        tv.setText(m_novel.author);
        tv = m_catalogView.findViewById(R.id.novel_catalog_name);
        tv.setText(m_novel.name);
        ImageView img = m_catalogView.findViewById(R.id.novel_catalog_img);
        Glide.with(m_catalogView).clear(img);
        Glide.with(m_catalogView).load(m_novel.img).apply(RequestOptions.fitCenterTransform()).into(img);

        //click to return reader page
        View v = m_catalogView.findViewById(R.id.novel_catalog_mask);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                m_catalogView.setVisibility(View.GONE);
            }
        });

        //switch between group and dict
        v = m_catalogView.findViewById(R.id.novel_catalog_range);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                View l = m_catalogView.findViewById(R.id.novel_catalog_chapter_list);
                View g = m_catalogView.findViewById(R.id.novel_catalog_range_list);
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
        v = m_catalogView.findViewById(R.id.novel_catalog_prev);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                ListView l = m_catalogView.findViewById(R.id.novel_catalog_chapter_list);
                ListView g = m_catalogView.findViewById(R.id.novel_catalog_range_list);
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
        v = m_catalogView.findViewById(R.id.novel_catalog_next);
        RxView.clicks(v).throttleFirst(200, TimeUnit.MICROSECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object obj) throws Exception {
                ListView l = m_catalogView.findViewById(R.id.novel_catalog_chapter_list);
                ListView g = m_catalogView.findViewById(R.id.novel_catalog_range_list);
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

    private View m_catalogView;
    private PPReaderNovel m_novel;
    private IPPReaderTaskNotification m_notification;
    private Fragment m_fragment;

}
