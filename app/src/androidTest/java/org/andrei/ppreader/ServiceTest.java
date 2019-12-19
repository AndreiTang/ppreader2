package org.andrei.ppreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineSetting;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.task.PPReaderSearchUrlsTask;
import org.andrei.ppreader.service.engine.EngineNames;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.action.ViewActions.click;
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
            //super(path);
            ArrayList<PPReaderEngineSetting> infos = new ArrayList<>();
            PPReaderEngineSetting info = new PPReaderEngineSetting();
            info.name = EngineNames.ENGINE_88dushu;
            infos.add(info);
            m_engineSettings = infos;
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
        //PPReaderServiceFactory factory = new PPReaderServiceFactory(dataManager);
       // final IPPReaderService service = factory.createServiceInstance();

        PPReaderSearchUrlsTask task = new PPReaderSearchUrlsTask("大明");
        //service.addTask(task);
        //service.waitForExit();

    }

    //@Test
    public void testCancelTask(){

    }

    PPReaderNovel n;
    PPReaderChapter c;

}
