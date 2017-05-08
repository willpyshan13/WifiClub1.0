package cn.situne.itee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

public class TeeTimeListPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> list;
    private FragmentManager fm;

    public TeeTimeListPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.fm = fm;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    public void setFragments(ArrayList fragments) {
        if (this.list != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.list) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.list = fragments;
        notifyDataSetChanged();
    }
}
