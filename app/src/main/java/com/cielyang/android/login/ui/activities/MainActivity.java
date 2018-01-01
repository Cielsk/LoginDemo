package com.cielyang.android.login.ui.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.cielyang.android.login.R;
import com.cielyang.android.login.base.BaseActivity;
import com.cielyang.android.login.databinding.ActivityMainBinding;
import com.cielyang.android.login.viewmodel.MainViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.nav_view)
    NavigationView mNavView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.text_view_main_content)
    TextView mTextViewMainContent;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private ActivityMainBinding mBinding;
    private ActionBarDrawerToggle mToggle;

    private MainViewModel mMainViewModel;

    public static void actionStart(@NonNull Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainViewModel.start();
    }

    private void initViewModel() {
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);

        mBinding.setViewModel(mMainViewModel);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    private void initView() {
        setSupportActionBar(mToolbar);

        mToggle =
                new ActionBarDrawerToggle(
                        this,
                        mDrawerLayout,
                        mToolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);

        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_camera:
                mTextViewMainContent.setText(R.string.photo);
                break;
            case R.id.nav_gallery:
                mTextViewMainContent.setText(R.string.gallery);
                break;
            case R.id.nav_slideshow:
                mTextViewMainContent.setText(R.string.slideshow);
                break;
            case R.id.nav_share:
                mTextViewMainContent.setText(R.string.share);
                break;
            case R.id.nav_send:
                mTextViewMainContent.setText(R.string.send);
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
