package cn.finalteam.loadingviewfinal.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.ui.adapter.BannerAdapter;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr.PtrListViewFragment;
import cn.finalteam.loadingviewfinal.sample.ui.widget.ChildViewPager;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/16 上午11:07
 */
public class PtrViewPagerFragment extends PtrListViewFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_viewpager, null);
        ChildViewPager vp = (ChildViewPager) v.findViewById(R.id.vp_banner);
        vp.setAdapter(new BannerAdapter(getContext()));
        mLvGames.addHeaderView(v);
        mPtrLayout.disableWhenHorizontalMove(true);
    }


}
