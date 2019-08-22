package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.andrei.ppreader.R;

import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderServiceFactory;
import org.andrei.ppreader.ui.fragment.PPReaderSearchFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;

public class MockActivity1 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        PPReaderServiceFactory factory = new PPReaderServiceFactory(new MockDataManager());

        if(savedInstanceState != null){
            Log.i("andrei","in activity");
            PPReaderSearchFragment fragment = (PPReaderSearchFragment)getSupportFragmentManager().findFragmentById(R.id.mock_root);
            if(fragment != null){
                Log.i("andrei","ac frag");
                fragment.init(factory.createServiceInstance());
                return;
            }
        }

        PPReaderSearchFragment fragment = new PPReaderSearchFragment();
        fragment.init(factory.createServiceInstance());
        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
        Log.i("andrei","end");
    }
}
