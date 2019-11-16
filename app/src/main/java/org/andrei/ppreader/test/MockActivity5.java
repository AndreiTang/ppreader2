package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.PPReaderServiceFactory;
import org.andrei.ppreader.service.engine.EngineNames;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.engine.PPReaderNovelEngineManager;
import org.andrei.ppreader.service.message.PPReaderMessageCenter;
import org.andrei.ppreader.ui.adapter.PPReaderBaseAdapter;
import org.andrei.ppreader.ui.fragment.PPReaderBaseFragment;
import org.andrei.ppreader.ui.fragment.PPReaderNovelTextFragment;

public class MockActivity5 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        m_dataManager = new MockDataManager();
        m_engineManager = new PPReaderNovelEngineManager();
        PPReaderBaseAdapter.setDataManager(m_dataManager);
        PPReaderBaseFragment.setDataManager(m_dataManager);
        PPReaderBaseAdapter.setMessageCenter(PPReaderMessageCenter.instance());
        PPReaderBaseFragment.setMessageCenter(PPReaderMessageCenter.instance());
        PPReaderBaseFragment.setServiceFactory(new PPReaderServiceFactory(m_engineManager,m_dataManager));

        PPReaderNovelTextFragment textFragment = null;
        if(savedInstanceState != null) {
            textFragment = (PPReaderNovelTextFragment) getSupportFragmentManager().findFragmentById(R.id.mock_root);
        }
        else{
            textFragment = new PPReaderNovelTextFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mock_root,textFragment).commit();
        }


        PPReaderNovel novel = new PPReaderNovel();
        novel.id = "1";
        novel.currIndex = 1;
        novel.name = "官居一品";
        novel.engineName = EngineNames.ENGINE_88dushu;
        novel.detailUrl = "/xiaoshuo/2/2271/";
        novel.url = novel.detailUrl;

        PPReaderChapter chapter = new PPReaderChapter();
        chapter.id = "1";
        chapter.url = "13871363.html";
        chapter.title = "一梦五百年(上)";
        novel.chapters.add(chapter);


        chapter = new PPReaderChapter();
        chapter.id = "2";
        chapter.url = "13871364.html";
        chapter.title = "一梦五百年(中)";
        novel.chapters.add(chapter);

        chapter = new PPReaderChapter();
        chapter.id = "3";
        chapter.url = "13871365.html";
        chapter.title = "一梦五百年(下)";
        novel.chapters.add(chapter);

        textFragment.setNovel(novel);
    }

    private IPPReaderDataManager m_dataManager;
    private IPPReaderNovelEngineManager m_engineManager;
}
