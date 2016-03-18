package cn.finalteam.loadingviewfinal.sample.ui.fragment.srl;

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
 * Date:16/3/9 上午9:35
 */
public class SRLTabFragment extends BaseFragment {


    @Bind(R.id.tab_srl_layout)
    TabLayout mTabSrlLayout;
    @Bind(R.id.vp_srl_tabs)
    ViewPager mVpSrlTabs;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_srl_tab;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SRLListViewFragment());
        fragmentList.add(new SRLGridViewFragment());
        fragmentList.add(new SRLRecyclerViewFragment());
        fragmentList.add(new SRLScrollViewFragment());
        fragmentList.add(new SRLExpandableListViewFragment());
        fragmentList.add(new SRLWebViewFragment());
        List<String> fragmentNameList = new ArrayList<>();
        fragmentNameList.add("ListView");
        fragmentNameList.add("GridView");
        fragmentNameList.add("RecyclerView");
        fragmentNameList.add("ScrollView");
        fragmentNameList.add("ExpandableListView");
        fragmentNameList.add("WebView");
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, fragmentNameList);
        mVpSrlTabs.setAdapter(fragmentAdapter);
        mTabSrlLayout.setupWithViewPager(mVpSrlTabs);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
