package cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;
import cn.finalteam.toolsfinal.adapter.FragmentAdapter;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:57
 */
public class PtrTabFragment extends BaseFragment {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_tabs)
    ViewPager mVpTabs;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PtrListViewFragment());
        fragmentList.add(new PtrGridViewFragment());
        fragmentList.add(new PtrRecyclerViewFragment());
        fragmentList.add(new PtrScrollViewFragment());
        fragmentList.add(new PtrExpandableListViewFragment());
        fragmentList.add(new PtrWebViewFragment());
        List<String> fragmentNameList = new ArrayList<>();
        fragmentNameList.add("ListView");
        fragmentNameList.add("GridView");
        fragmentNameList.add("RecyclerView");
        fragmentNameList.add("ScrollView");
        fragmentNameList.add("ExpandableListView");
        fragmentNameList.add("WebView");
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, fragmentNameList);
        mVpTabs.setAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(mVpTabs);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
