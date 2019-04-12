package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.andrei.ppreader.R;

import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.ui.fragment.PPReaderSearchFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;

public class MockActivity1 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        if(savedInstanceState != null){
            Log.i("andrei","in activity");
            PPReaderSearchFragment fragment = (PPReaderSearchFragment)getSupportFragmentManager().findFragmentById(R.id.mock_root);
            if(fragment != null){
                Log.i("andrei","ac frag");
                fragment.init(new IPPReaderTaskNotification() {
                    @Override
                    public void onNotify(IPPReaderTaskRet ret) {
                        if(ret.type().compareTo(PPReaderSelectNovelRet.class.getName()) == 0){
                            PPReaderSelectNovelRet r = (PPReaderSelectNovelRet)ret;
                            Log.i("Andrei",r.novel.name);
                        }
                    }
                },new MockService());
                return;
            }
        }

        PPReaderSearchFragment fragment = new PPReaderSearchFragment();
        fragment.init(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(ret.type().compareTo(PPReaderSelectNovelRet.class.getName()) == 0){
                    PPReaderSelectNovelRet r = (PPReaderSelectNovelRet)ret;
                    Log.i("Andrei",r.novel.name);
                }
            }
        },new MockService());
        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
        Log.i("andrei","end");
    }
}
