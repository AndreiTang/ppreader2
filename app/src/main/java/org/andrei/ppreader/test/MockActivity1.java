package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.andrei.ppreader.R;

import org.andrei.ppreader.ui.fragment.PPReaderSearchFragment;

public class MockActivity1 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        PPReaderSearchFragment fragment = new PPReaderSearchFragment();



        getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
    }
}
