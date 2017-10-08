package com.ofaucoz.lazyboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {
    // Starting position is 1(middle)
    private int CurrentPosition = 1;

    private static final int NUM_PAGES = 3;
    // ViewPager from the activity_main.xml
    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    private MainFragment ConnectionMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = (ViewPager) findViewById(R.id.main_layout);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(CurrentPosition);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                MainFragment fragment = new MainFragment();
                ConnectionMainFragment = fragment;
                return fragment;
            } else {
                //TODO
                return new GridCommandFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    public void doPositiveClick(String command, String additional_args) {
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        try {
            ConnectionMainFragment.sendMessage("mode commande " + command + " " + additional_args + "\n");

        } catch (UnsupportedEncodingException e) {
            Log.d("dialog_cmd_main", "unsupported");
        }

    }

    public void doNegativeClick() {
        // Do stuff here.

    }

}

//