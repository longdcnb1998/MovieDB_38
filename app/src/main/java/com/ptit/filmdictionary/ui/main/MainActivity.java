package com.ptit.filmdictionary.ui.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.ptit.filmdictionary.R;
import com.ptit.filmdictionary.ui.home.HomeFragment;
import com.ptit.filmdictionary.ui.home.MainAdapter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.
        OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_FAVORITE = 1;
    private static final int FRAGMENT_SETTING = 2;
    private ViewPager mViewPager;
    private BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TransparentStatusTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initViewPager();
        registerEvents();
    }

    private void initViewPager() {
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mainAdapter);
        mViewPager.addOnPageChangeListener(this);
    }

    private void registerEvents() {
        mNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initViews() {
        mViewPager = findViewById(R.id.view_pager);
        mNavigationView = findViewById(R.id.bottom_navigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(FRAGMENT_HOME);
                return true;
            case R.id.menu_favorite:
                mViewPager.setCurrentItem(FRAGMENT_FAVORITE);
                return true;
            case R.id.menu_setting:
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i){
            case FRAGMENT_HOME:
                mNavigationView.setSelectedItemId(R.id.menu_home);
                break;
            case FRAGMENT_FAVORITE:
                mNavigationView.setSelectedItemId(R.id.menu_favorite);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
