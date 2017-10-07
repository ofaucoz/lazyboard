package com.ofaucoz.lazyboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = (ViewPager) findViewById(R.id.main_layout);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(CurrentPosition);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainFragment fragment = new MainFragment();
            transaction.add(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return new MainFragment();
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
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.sample_content_fragment);
        try {
            // we will create a message to be send to the server using the command and additional args
            fragment.sendMessage("mode commande " + command + " " + additional_args );
        }
        catch (UnsupportedEncodingException e){
            Log.d("dialog_cmd_main", "unsupported");
        }

    }

    public void doNegativeClick() {
        // Do stuff here.

    }

}

//