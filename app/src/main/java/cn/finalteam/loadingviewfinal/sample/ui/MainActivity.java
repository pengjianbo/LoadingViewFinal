package cn.finalteam.loadingviewfinal.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.sample.Global;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.event.RestartLoadMoreFragmentEvent;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.LoadMoreStyleFragment;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.PtrViewPagerFragment;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.SRLViewPagerFragment;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr.PtrTabFragment;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.srl.SRLTabFragment;
import cn.finalteam.loadingviewfinal.sample.ui.widget.ExViewPager;
import cn.finalteam.toolsfinal.AppCacheUtils;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.adapter.FragmentAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.view_pager)
    ExViewPager mViewPager;

    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList;
    private LoadMoreStyleFragment mLoadMoreStyleFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        Global.SCREEN_WIDTH = dm.widthPixels;
        Global.SCREEN_HEIGHT = dm.heightPixels;
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);

        mLoadMoreStyleFragment = new LoadMoreStyleFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new PtrTabFragment());
        mFragmentList.add(new SRLTabFragment());
        mFragmentList.add(new PtrViewPagerFragment());
        mFragmentList.add(new SRLViewPagerFragment());
        mFragmentList.add(mLoadMoreStyleFragment);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setNoScroll(true);
        mViewPager.setOffscreenPageLimit(10);

        int index = AppCacheUtils.getInstance(this).getInt("last_viewpager_index", 0);
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ptr) {
            mViewPager.setCurrentItem(0);
        } else if (id == R.id.nav_srf) {
            mViewPager.setCurrentItem(1);
        } else if (id == R.id.nav_ptr_viewpager) {
            mViewPager.setCurrentItem(2);
        } else if (id == R.id.nav_srl_viewpager) {
            mViewPager.setCurrentItem(3);
        } else if (id == R.id.nav_custom_loadmore) {
            mViewPager.setCurrentItem(4);
        }

        mNavView.setCheckedItem(id);

        AppCacheUtils.getInstance(this).put("last_viewpager_index", mViewPager.getCurrentItem());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onRestartLoadMoreFragmentEvent(RestartLoadMoreFragmentEvent event) {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }
}
