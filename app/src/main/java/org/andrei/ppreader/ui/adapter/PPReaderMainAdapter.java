package org.andrei.ppreader.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PPReaderMainAdapter extends FragmentPagerAdapter {

    public PPReaderMainAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        m_fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return m_fragments[position];
    }

    @Override
    public int getCount() {
        return m_fragments.length;
    }

    Fragment[] m_fragments = null;
}
