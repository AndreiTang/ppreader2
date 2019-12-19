package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineSetting;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.engine.EngineNames;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.engine.PPReaderNovelEngineManager;
import org.andrei.ppreader.ui.fragment.PPReaderNovelTextFragment;

import java.util.ArrayList;

public class MockActivity5 extends FragmentActivity {

    @Override
    public void onBackPressed(){
        PPReaderNovelTextFragment text = (PPReaderNovelTextFragment)getSupportFragmentManager().findFragmentById(R.id.mock_root);


            //main.switchFragment(0);
        text.backPress();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        m_engineManager = new PPReaderNovelEngineManager();
        m_dataManager = new PPReaderDataManager();
        String path = getExternalFilesDir(null).getPath();
        m_dataManager.load(path);

        ArrayList<PPReaderEngineSetting> infos = new ArrayList<>();
        for(int i = 0; i < m_engineManager.count(); i++){
            PPReaderEngineSetting info = new PPReaderEngineSetting();
            info.name = m_engineManager.get(i).getName();
            info.contentUrl = m_engineManager.get(i).getContentUrl();
            info.imageUrl = m_engineManager.get(i).getImageUrl();
            infos.add(info);
        }
        //m_dataManager.setEngineInfos(infos);

        m_engineManager = new PPReaderNovelEngineManager();

        PPReaderNovelTextFragment textFragment = null;
        if(savedInstanceState != null) {
            textFragment = (PPReaderNovelTextFragment) getSupportFragmentManager().findFragmentById(R.id.mock_root);
        }
        else{
            textFragment = new PPReaderNovelTextFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mock_root,textFragment).commit();
        }

//        textFragment.addOnNotification(new IPPReaderNovelTextFragmentNotification() {
//            @Override
//            public void onSwitchFragment(int index) {
//
//            }
//
//            @Override
//            public void onAddNovel(PPReaderNovel novel) {
//                String path = getExternalFilesDir(null).getPath();
//                m_dataManager.addNovel(novel);
//                m_dataManager.save(path);
//            }
//        });


        PPReaderNovel novel = new PPReaderNovel();
        novel.id = "1";
        novel.currIndex = 1;
        novel.name = "官居一品";
        novel.engineName = EngineNames.ENGINE_88dushu;
        novel.detailUrl = "/xiaoshuo/2/2271/";
        novel.chapterUrl = novel.detailUrl;
        novel.author = "三戒大师";
        novel.desc = "数风流，论成败，百年一梦多慷慨有心要励精图治挽天倾，哪怕身后骂名滚滚来。轻生死，重兴衰，海雨天风独往来。谁不想万里长城永不倒，也难料恨水东逝归大海。改编自《得民心者得天下》，代为序。......";
        novel.img = "/2/2271/2271s.jpg";

        PPReaderTextPage page = new PPReaderTextPage();
        page.chapterIndex = 0;
        page.chapterId = "1";
        novel.textPages.add(page);

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
        page = new PPReaderTextPage();
        page.chapterIndex = 1;
        page.chapterId = "2";
        novel.textPages.add(page);


        chapter = new PPReaderChapter();
        chapter.id = "3";
        chapter.url = "13871365.html";
        chapter.title = "一梦五百年(下)";
        novel.chapters.add(chapter);
        page = new PPReaderTextPage();
        page.chapterIndex = 2;
        page.chapterId = "3";
        novel.textPages.add(page);

        //PPReaderNovel novel = m_dataManager.getNovel(0);
        textFragment.setNovel(novel);
    }

    private IPPReaderDataManager m_dataManager;
    private IPPReaderNovelEngineManager m_engineManager;
}
