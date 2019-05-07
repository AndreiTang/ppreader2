package org.andrei.ppreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsTask;
import org.andrei.ppreader.service.PPReaderSearchUrlsRet;
import org.andrei.ppreader.service.PPReaderSearchUrlsTask;
import org.andrei.ppreader.service.PPReaderService;
import org.andrei.ppreader.service.PPReaderServiceFactory;
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


    static class MockDataManager extends PPReaderDataManager{
        public MockDataManager(){
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

        PPReaderServiceFactory factory = new PPReaderServiceFactory(new MockDataManager());
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
                   service.stop();
                   PPReaderSearchNovelsRet r = (PPReaderSearchNovelsRet)ret;
                   assertEquals(10,r.novels.size());
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

    private String name;

}
