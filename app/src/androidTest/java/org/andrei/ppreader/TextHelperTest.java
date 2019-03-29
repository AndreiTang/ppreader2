package org.andrei.ppreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.andrei.ppreader.ui.view.PPReaderTextView;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class TextHelperTest {

    @Test
    public void testTextView(){

    }

    @Test
    public void testListView(){


    }




    //@Test
    public void testAddWithText(){
        //Looper.prepare();
        //IPPReaderPageManager pm = new MockPM();
        Context appContext = InstrumentationRegistry.getTargetContext();
        PPReaderTextView txView = new PPReaderTextView(appContext);
        txView.setWidth(320);
        txView.setHeight(180);
        int h = txView.getLineCount();
        assertEquals(3,h);
//        PPReaderTextFragmentViews views = new PPReaderTextFragmentViews();
//        views.textView = txView;
//        views.chapterIndex = 0;
//        views.pos = 0;
//        helper.setPageManager(pm);
//        helper.loadText(views);

      //Looper.loop();

    }
}
