package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.ui.fragment.PPReaderSettingFragment;

public class MockActivity2 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        if(savedInstanceState != null){

        }

        IPPReaderDataManager dataManager = new MockDataManager();
        PPReaderSettingFragment fragment = new PPReaderSettingFragment();
        fragment.init(dataManager, new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {

            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
    }
}
