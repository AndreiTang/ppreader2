package org.andrei.ppreader.ui.fragment.helper;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderFetchTextRet;
import org.andrei.ppreader.service.PPReaderFetchTextTask;
import org.andrei.ppreader.service.PPReaderTextRet;
import org.andrei.ppreader.service.PPReaderTextTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.view.helper.PPReaderRxBinding;

import io.reactivex.functions.Consumer;

public class PPReaderText implements IPPReaderTaskNotification {

    public PPReaderText(final IPPReaderPageManager pageManager, IPPReaderService service, final IPPReaderTaskNotification notification) {
        m_pageManager = pageManager;
        m_service = service;
        m_notification = notification;
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        if (ret.type().compareTo(PPReaderFetchTextRet.class.getName()) == 0) {//
            if (ret.getRetCode() != 0) {
                return;
            }

            PPReaderFetchTextRet textTaskRet = (PPReaderFetchTextRet) ret;
            if (textTaskRet.novelId.compareTo(m_novel.id) != 0) {
                return;
            }

            if (textTaskRet.getRetCode() == ServiceError.ERR_OK) {
                m_pageManager.updateText(textTaskRet.chapterId, textTaskRet.text);
                int index = m_pageManager.getChapterFirstPageIndex(textTaskRet.chapterId);
                PPReaderTextPage page = m_pageManager.getItem(index);
                if(page.chapterIndex == m_novel.currIndex){
                    page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
                }
                else{
                    page.status = PPReaderTextPage.STATUS_LOADED;
                }

            } else {
                int index = m_pageManager.getChapterFirstPageIndex(textTaskRet.chapterId);
                PPReaderTextPage page = m_pageManager.getItem(index);
                page.status = PPReaderTextPage.STATUS_FAIL;
            }
            m_adapter.notifyDataSetChanged();
        }
        else if(ret.type().compareTo(PPReaderAllocateTextRet.class.getName())==0){
            PPReaderAllocateTextRet r = (PPReaderAllocateTextRet)ret;
            int index = m_pageManager.getIndex(r.page);
            m_pageManager.injectText(index,r.tv);
            m_adapter.notifyDataSetChanged();
        }
        else if(ret.type().compareTo(PPReaderCommonRet.TYPE_FETCH_TEXT) == 0){
            PPReaderCommonRet r = (PPReaderCommonRet)ret;
            fetchText(r.index);
        }
    }

    public void init(final ViewPager vp, final Fragment parent) {
        m_vp = vp;
        m_adapter = new PPReaderTextAdapter(parent, m_pageManager, this);
        m_vp.setAdapter(m_adapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                PPReaderTextPage page = m_pageManager.getItem(position);
                m_novel.currIndex = page.chapterIndex;
                PPReaderChapter chapter = m_novel.chapters.get(page.chapterIndex);
                chapter.offset = page.offset;

                if(page.status == PPReaderTextPage.STATUS_LOADED){
                    page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
                    notifyDataSetChanged();
                }

                if (m_notification == null) {
                    return;
                }
                PPReaderCommonRet ret = new PPReaderCommonRet(PPReaderCommonRet.TYPE_CURR);
                ret.index = position;
                m_notification.onNotify(ret);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        PPReaderRxBinding.dbClicks(vp).subscribe(new Consumer<MotionEvent>() {
            @Override
            public void accept(MotionEvent motionEvent) throws Exception {
                PPReaderDBClicksRet ret = new PPReaderDBClicksRet();
                ret.event = motionEvent;
                m_notification.onNotify(ret);
            }
        });

        m_service.start(this);

    }

    public void loadNovel(final PPReaderNovel novel) {
        m_novel = novel;

        m_service.clearTasks();
        final PPReaderChapter chapter = m_novel.chapters.get(m_novel.currIndex);
        final int offset = chapter.offset;

        m_pageManager.load(m_novel);
        m_adapter.notifyDataSetChanged();
        m_vp.setCurrentItem(m_novel.currIndex);
        if (m_novel.currIndex == 0) {
            fetchText(0);
            PPReaderTextPage page = m_pageManager.getItem(0);
            if(page.status == PPReaderTextPage.STATUS_LOADED){
                page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
            }
        }


        if (offset > 0) {
            m_adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    m_adapter.unregisterDataSetObserver(this);
                    int index = m_pageManager.getChapterFirstPageIndex(chapter.id) + offset;
                    m_vp.setCurrentItem(index);
                    PPReaderCommonRet ret = new PPReaderCommonRet(PPReaderCommonRet.TYPE_CURR);
                    ret.index = index;
                    m_notification.onNotify(ret);
                }
            });
        }
    }

    public void notifyDataSetChanged() {
        m_adapter.notifyDataSetChanged();
    }

    public void setCurrentItem(int index) {
        m_vp.setCurrentItem(index);
        if (index == 0) {
            m_adapter.notifyDataSetChanged();
        }
    }

    public int getCurrentIndex() {
        return m_vp.getCurrentItem();
    }

    private void fetchText(int pos) {
        PPReaderTextPage page = m_pageManager.getItem(pos);
        if (page.status == PPReaderTextPage.STATUS_INIT) {
            page.status = PPReaderTextPage.STATUS_LOADING;
            m_adapter.notifyDataSetChanged();

            PPReaderFetchTextTask task = new PPReaderFetchTextTask(m_novel,m_novel.getChapter(page.chapterIndex));
            m_service.addTask(task);
        }
    }

    private IPPReaderTaskNotification m_notification;
    private IPPReaderPageManager m_pageManager;
    private IPPReaderService m_service;
    private PPReaderNovel m_novel;
    private PPReaderTextAdapter m_adapter;
    private ViewPager m_vp;

}
