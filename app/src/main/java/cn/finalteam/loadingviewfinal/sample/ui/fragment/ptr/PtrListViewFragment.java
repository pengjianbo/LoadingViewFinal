package cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.http.Api;
import cn.finalteam.loadingviewfinal.sample.http.MyBaseHttpRequestCallback;
import cn.finalteam.loadingviewfinal.sample.http.model.GameInfo;
import cn.finalteam.loadingviewfinal.sample.http.model.NewGameResponse;
import cn.finalteam.loadingviewfinal.sample.ui.adapter.NewGameListAdapter;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;
import cn.finalteam.loadingviewfinal.sample.utils.EmptyViewUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:12
 */
public class PtrListViewFragment extends BaseFragment {

    private final int LIMIT = 12;

    @Bind(R.id.ptr_layout)
    public PtrClassicFrameLayout mPtrLayout;
    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;
    @Bind(R.id.lv_games)
    public ListViewFinal mLvGames;

    private List<GameInfo> mGameList;
    private NewGameListAdapter mNewGameListAdapter;

    private int mPage = 1;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_ptr_listview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(getContext(), mGameList);
        mLvGames.setAdapter(mNewGameListAdapter);

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
                requestData(1);
            }
        });
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        mLvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mPage);
            }
        });
        mPtrLayout.autoRefresh();
    }

    private void requestData(final int page) {
        RequestParams params = new RequestParams(this);
        params.addFormDataPart("page", page);
        params.addFormDataPart("limit", LIMIT);
        HttpRequest.post(Api.NEW_GAME, params, new MyBaseHttpRequestCallback<NewGameResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (mGameList.size() == 0) {
                    EmptyViewUtils.showLoading(mFlEmptyView);
                }
            }

            @Override
            public void onLogicSuccess(NewGameResponse newGameResponse) {
                if (page == 1) {
                    mGameList.clear();
                }
                mPage = page + 1;

                mGameList.addAll(newGameResponse.getData());
                if (newGameResponse.getData().size() < LIMIT) {
                    mLvGames.setHasLoadMore(false);
                } else {
                    mLvGames.setHasLoadMore(true);
                }
            }

            @Override
            public void onLogicFailure(NewGameResponse newGameResponse) {
                if (mGameList.size() == 0) {
                    EmptyViewUtils.showNoDataEmpty(mFlEmptyView);
                } else {
                    Toast.makeText(getContext(), newGameResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                if (mGameList.size() == 0) {
                    EmptyViewUtils.showNetErrorEmpty(mFlEmptyView);
                } else {
                    mLvGames.showFailUI();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if (page == 1) {
                    mPtrLayout.onRefreshComplete();
                } else {
                    mLvGames.onLoadMoreComplete();
                }

                mNewGameListAdapter.notifyDataSetChanged();
            }
        });
    }


}
