package org.andrei.ppreader.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderService;
import org.andrei.ppreader.ui.fragment.PPReaderListFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;

public class MockActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        PPReaderListFragment fragment = new PPReaderListFragment();
        IPPReaderDataManager dataManager = new MockDataManager();
        IPPReaderService service = PPReaderService.createInstance(null);
        fragment.init(dataManager, new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                PPReaderSelectNovelRet r =  (PPReaderSelectNovelRet)ret;
                Log.i("test",r.novel.name);
            }
        },service);

        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
    }
}
