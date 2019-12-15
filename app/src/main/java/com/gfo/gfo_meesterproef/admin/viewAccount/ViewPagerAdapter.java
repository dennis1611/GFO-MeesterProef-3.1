package com.gfo.gfo_meesterproef.admin.viewAccount;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Denni on 29-11-2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();

    public void addFragment(Fragment fragment, String title){
        this.fragments.add(fragment);
        this.tabTitles.add(title);
    }

    public ViewPagerAdapter(FragmentManager fm){ super(fm); }

    @Override
    public Fragment getItem(int position) { return fragments.get(position); }

    @Override
    public int getCount() { return fragments.size(); }

    @Override
    public CharSequence getPageTitle(int position) { return tabTitles.get(position); }
}