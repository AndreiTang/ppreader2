package org.andrei.ppreader.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderService;
import org.andrei.ppreader.ui.fragment.PPReaderListFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;

public class TestActivity1 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        PPReaderListFragment fragment =  (PPReaderListFragment) getSupportFragmentManager().findFragmentById(R.id.test_frag);
        IPPReaderDataManager dataManager = new MockDataManager();
        IPPReaderService service = PPReaderService.createInstance(null);
        fragment.init(dataManager, new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                PPReaderSelectNovelRet r =  (PPReaderSelectNovelRet)ret;
                Log.i("test",r.novel.name);
            }
        },service);

        Log.i("test","end");
    }
}
