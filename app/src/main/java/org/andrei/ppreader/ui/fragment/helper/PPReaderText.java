package org.andrei.ppreader.ui.fragment.helper;

import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.message.IPPReaderMessageHandler;
import org.andrei.ppreader.service.message.PPReaderMessageTool;
import org.andrei.ppreader.service.task.PPReaderFetchTextTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderAllocateTextMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderDBClicksMessage;
import org.andrei.ppreader.service.message.PPReaderFetchTextMessage;
import org.andrei.ppreader.service.message.PPReaderMessageCenter;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.view.helper.PPReaderRxBinding;

import java.util.HashSet;

import io.reactivex.functions.Consumer;

public class PPReaderText  {

    public PPReaderText(final IPPReaderPageManager pageManager, IPPReaderService service) {
        m_pageManager = pageManager;
        m_service = service;
        //PPReaderMessageTool.collectInteresting(this,m_methods);
    }

    public void init(final ViewPager vp, final Fragment parent) {
        m_vp = vp;
        m_adapter = new PPReaderTextAdapter(parent, m_pageManager);
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
                else{
                    fetchText(position);
                }



                PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_CURR,position);
                PPReaderMessageCenter.instance().sendMessage(msg);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        PPReaderRxBinding.dbClicks(vp).subscribe(new Consumer<MotionEvent>() {
            @Override
            public void accept(MotionEvent motionEvent) throws Exception {
                PPReaderDBClicksMessage msg = new PPReaderDBClicksMessage(motionEvent);
                PPReaderMessageCenter.instance().sendMessage(msg);
            }
        });

        m_service.start();

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

                    PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_CURR,index);
                    PPReaderMessageCenter.instance().sendMessage(msg);
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


    public void fetchText(int index){
        PPReaderTextPage page = m_pageManager.getItem(index);
        if (page.status == PPReaderTextPage.STATUS_INIT) {
            page.status = PPReaderTextPage.STATUS_LOADING;
            m_adapter.notifyDataSetChanged();

            PPReaderFetchTextTask task = new PPReaderFetchTextTask(m_novel,m_novel.getChapter(page.chapterIndex));
            m_service.addTask(task);
        }
    }




    private IPPReaderPageManager m_pageManager;
    private IPPReaderService m_service;
    private PPReaderNovel m_novel;
    private PPReaderTextAdapter m_adapter;
    private ViewPager m_vp;
    //private HashSet<String> m_methods = new HashSet<>();


}
