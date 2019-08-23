package org.andrei.ppreader.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderServiceFactory;
import org.andrei.ppreader.ui.fragment.PPReaderMainFragment;

public class MockActivity3 extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        Log.i("Andrei",Integer.toString(getSupportFragmentManager().getFragments().size()));;

        IPPReaderDataManager dataManager = new MockDataManager();
        IPPReaderServiceFactory factory = new MockServiceFactory();

        PPReaderMainFragment fragment = null;

        if(savedInstanceState != null){
            fragment = (PPReaderMainFragment)getSupportFragmentManager().findFragmentById(R.id.mock_root);

            Log.i("Andrei","in MockActivity");
        }
        else{
            fragment = new PPReaderMainFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mock_root,fragment).commit();
        }

    }

}
