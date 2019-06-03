package org.andrei.ppreader;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderFetchTextRet;
import org.andrei.ppreader.service.PPReaderFetchTextTask;
import org.andrei.ppreader.service.PPReaderSearchNovelsRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsTask;
import org.andrei.ppreader.service.PPReaderSearchUrlsRet;
import org.andrei.ppreader.service.PPReaderSearchUrlsTask;
import org.andrei.ppreader.service.PPReaderService;
import org.andrei.ppreader.service.PPReaderServiceFactory;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.engine.EngineNames;
import org.andrei.ppreader.util.TaskNames;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ServiceTest {

    static String path;
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        path = appContext.getExternalFilesDir(null).getPath();
    }


    static class MockDataManager extends PPReaderDataManager{
        public MockDataManager(){
            super(path);
            ArrayList<PPReaderEngineInfo> infos = new ArrayList<>();
            PPReaderEngineInfo info = new PPReaderEngineInfo();
            info.name = EngineNames.ENGINE_88dushu;
            infos.add(info);
            m_infos = infos;
        }
    }

    //@Test
    public void testExample(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        TextView tv = new TextView(appContext);
        tv.setText("ffffff");
        tv.setWidth(300);
        tv.setHeight(200);
        assertEquals(3,3);
    }

    @Test
    public void testAllTask(){

        final IPPReaderDataManager dataManager = new MockDataManager();
        PPReaderServiceFactory factory = new PPReaderServiceFactory(dataManager);
        final IPPReaderService service = factory.createServiceInstance();
        service.start(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {

               assertEquals(ServiceError.ERR_OK,ret.getRetCode());
               if(ret.type().compareTo(PPReaderSearchUrlsRet.class.getName())==0){
                   PPReaderSearchUrlsRet r = (PPReaderSearchUrlsRet)ret;
                   assertEquals(10,r.urls.size());
                   PPReaderSearchNovelsTask task = new PPReaderSearchNovelsTask(r.urls.get(0),r.engineName);
                   service.addTask(task);
               }
               else if(ret.type().compareTo(PPReaderSearchNovelsRet.class.getName())==0){

                   PPReaderSearchNovelsRet r = (PPReaderSearchNovelsRet)ret;
                   assertEquals(10,r.novels.size());
                   n = r.novels.get(0);
                   int c = n.name.compareTo("大明枭");
                   assertEquals(0,c);

                   for(PPReaderNovel it : r.novels){
                        dataManager.addNovel(it);
                   }

                   PPReaderUpdateNovelTask task = new PPReaderUpdateNovelTask(n);
                   service.addTask(task);
               }
               else if(ret.type().compareTo(PPReaderUpdateNovelRet.class.getName()) == 0){

                   PPReaderUpdateNovelRet r = (PPReaderUpdateNovelRet)ret;
                   int i = 0;
                   if(r.delta.size() > 0 ){
                       i = 1;
                   }
                   assertEquals(1,i);

                   i = n.id.compareTo(r.id);
                   assertEquals(0,i);
                   n.chapters.addAll(r.delta);

                   c = n.chapters.get(0);
                   i = c.title.compareTo("第一章 祟祯二年");
                   assertEquals(0,i);

                   PPReaderFetchTextTask task = new PPReaderFetchTextTask(n,c);
                   service.addTask(task);

               }
               else if(ret.type().compareTo(PPReaderFetchTextRet.class.getName()) == 0){
                   service.stop();
                   PPReaderFetchTextRet r = (PPReaderFetchTextRet)ret;
                   int index = r.text.indexOf("湖广熟");
                   int i = 0;
                   if(index != -1){
                       i = 1;
                   }
                   assertEquals(1,i);
                   i = r.chapterId.compareTo(c.id);
                   assertEquals(0,i);
                   i = r.novelId.compareTo(n.id);
                   assertEquals(0,i);


                   PPReaderEngineInfo i2 = new PPReaderEngineInfo();
                   i2.name = "USSB";
                   i2.isUsed = true;
                   i2.isSelected = true;
                   ArrayList<PPReaderEngineInfo> ins = new ArrayList<>();
                   ins.add(i2);
                   dataManager.setEngineInfos(ins);

                   dataManager.save();

                   IPPReaderDataManager d = new MockDataManager();
                   d.load();
                   assertEquals(d.getNovelCount(),10);
                   PPReaderNovel n1 = d.getNovel(0);
                    i = 0;
                    if(n1.chapters.size() > 0){
                        i = 1;
                    }
                   assertEquals(1,i);

                    PPReaderEngineInfo i3 = d.getEngineInfo(0);
                    assertEquals(i3.name,"USSB");
                    assertEquals(i3.isUsed,true);

               }
            }
        });
        PPReaderSearchUrlsTask task = new PPReaderSearchUrlsTask("大明");
        service.addTask(task);
        service.waitForExit();

    }

    //@Test
    public void testCancelTask(){

    }

    PPReaderNovel n;
    PPReaderChapter c;

}
