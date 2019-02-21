package org.andrei.ppreader;

import android.support.test.runner.AndroidJUnit4;

import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ServiceTest {
    class TestRet implements IPPReaderTaskRet {

        public TestRet(){
            m_nRet++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String type() {
            return null;
        }


    };

    class TestTask implements IPPReaderTask {

        @Override
        public IPPReaderTaskRet run() {
            return new TestRet();
        }

        @Override
        public int getRetCode() {
            return 0;
        }
    }


    @Test
    public void testAllTask(){

        final PPReaderService service = PPReaderService.createInstance(new IPPReaderTaskNotification() {

            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(m_nRet == 3){
                    m_service.exit();
                }

            }
        });

        m_service = service;

        for(int i = 0 ; i < 3 ; i++){
            TestTask task = new TestTask();
            service.addTask(task);
        }

        service.waitForEnd();
        m_service = null;
        assertEquals(3,m_nRet);
    }

    @Test
    public void testCancelTask(){
        final PPReaderService service = PPReaderService.createInstance(new IPPReaderTaskNotification() {

            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                m_nRet1 ++;
                if(m_nRet1 == 2){
                    m_service.exit();
                }

            }
        });

        m_service = service;

        for(int i = 0 ; i < 4; i++){
            TestTask task = new TestTask();
            service.addTask(task);
        }

        service.waitForEnd();
        m_service = null;
        assertEquals(2,m_nRet1);
        assertEquals(3,m_nRet);
    }

    private int m_nRet = 0;
    private int m_nRet1 = 0;
    private PPReaderService m_service;

}
