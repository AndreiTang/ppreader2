package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.andrei.ppreader.R;

import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.ui.fragment.PPReaderSearchFragment;

public class MockActivity1 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        PPReaderSearchFragment fragment = new PPReaderSearchFragment();
        fragment.init(new MockDataManager(), new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {

            }
        },new MockService());
        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
    }
}
