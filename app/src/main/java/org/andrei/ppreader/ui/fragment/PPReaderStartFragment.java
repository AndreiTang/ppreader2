package org.andrei.ppreader.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.andrei.ppreader.R;

public class PPReaderStartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_start, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/wawa.ttf");
        TextView tv = (TextView) getActivity().findViewById(R.id.start_title);
        tv.setTypeface(typeface);
        getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        //fix the bug that Glide will on the top of the page. It cause the fragment can't exit.
        Glide.with(this).load(R.drawable.rm);

    }

}
