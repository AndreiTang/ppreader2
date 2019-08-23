package org.andrei.ppreader.test;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.PPReaderService;
import org.andrei.ppreader.ui.fragment.PPReaderListFragment;

public class MockActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        PPReaderListFragment fragment = new PPReaderListFragment();
        IPPReaderDataManager dataManager = new MockDataManager();
        IPPReaderService service = PPReaderService.createInstance(null);

        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
    }
}
