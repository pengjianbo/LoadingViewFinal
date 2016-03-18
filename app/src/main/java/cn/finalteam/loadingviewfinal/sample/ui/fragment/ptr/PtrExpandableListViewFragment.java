package cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.ExpandableListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:14
 */
public class PtrExpandableListViewFragment extends BaseFragment {

    private final int MAX_SIZE = 50;

    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;
    @Bind(R.id.lv_games)
    ExpandableListViewFinal mLvGames;
    @Bind(R.id.ptr_layout)
    PtrClassicFrameLayout mPtrLayout;

    private static final String KEY = "key";
    private SimpleExpandableListAdapter mExpandableListViewAdapter;
    private List<Map<String, String>> mGroupList;
    private List<List<Map<String, String>>> mChildList;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_ptr_expandable_listview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mExpandableListViewAdapter = new SimpleExpandableListAdapter(getContext(), mGroupList, android.R.layout.simple_expandable_list_item_1,
                new String[]{KEY}, new int[]{android.R.id.text1}, mChildList,
                android.R.layout.simple_expandable_list_item_2, new String[]{KEY}, new int[]{android.R.id.text1});

        mLvGames.setAdapter(mExpandableListViewAdapter);
        mLvGames.setEmptyView(mFlEmptyView);

        setSwipeRefreshInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setSwipeRefreshInfo() {
        mPtrLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData(true);
            }
        });
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        mLvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(false);
            }
        });
        mPtrLayout.autoRefresh();
    }

    private void requestData(final boolean refresh) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int loadMoreSize = 2;
                if (refresh) {
                    mChildList.clear();
                    mGroupList.clear();
                    loadMoreSize = 5;
                }
                for (int i = 0; i < loadMoreSize; i++) {

                    Map<String, String> groupMap1 = new HashMap<String, String>();
                    groupMap1.put(KEY, "Group:" + mGroupList.size());
                    mGroupList.add(groupMap1);

                    List<Map<String, String>> childList = new ArrayList<>();
                    for (int j = 0; j < 6; j++) {
                        Map<String, String> childMap = new HashMap<>();
                        childList.add(childMap);
                        if (refresh) {
                            childMap.put(KEY, "Child item " + j);
                        } else {
                            childMap.put(KEY, "Child item " + j + "(LoadMore)");
                        }
                    }
                    mChildList.add(childList);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isDetached()) {
                    return;
                }

                if (mGroupList.size() + mChildList.size() >= MAX_SIZE) {
                    mLvGames.setHasLoadMore(false);
                } else {
                    mLvGames.setHasLoadMore(true);
                }

                if (refresh) {
                    mLvGames.expandGroup(0);
                    mPtrLayout.onRefreshComplete();
                } else {
                    mLvGames.onLoadMoreComplete();
                }

                mExpandableListViewAdapter.notifyDataSetChanged();

            }
        }.execute();
    }
}
