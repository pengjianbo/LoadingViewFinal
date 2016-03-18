package cn.finalteam.loadingviewfinal.sample.ui.fragment.srl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.loadingviewfinal.GridViewFinal;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.SwipeRefreshLayoutFinal;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.http.Api;
import cn.finalteam.loadingviewfinal.sample.http.MyBaseHttpRequestCallback;
import cn.finalteam.loadingviewfinal.sample.http.model.GameInfo;
import cn.finalteam.loadingviewfinal.sample.http.model.NewGameResponse;
import cn.finalteam.loadingviewfinal.sample.ui.adapter.NewGameGridAdapter;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;
import cn.finalteam.loadingviewfinal.sample.utils.EmptyViewUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:11
 */
public class SRLGridViewFragment extends BaseFragment {

    private final int LIMIT = 12;

    @Bind(R.id.gv_games)
    GridViewFinal mGvGames;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayoutFinal mRefreshLayout;
    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;

    private List<GameInfo> mGameList;
    private NewGameGridAdapter mNewGameGridAdapter;

    private int mPage = 1;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_srl_gridview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mGameList = new ArrayList<>();
        mNewGameGridAdapter = new NewGameGridAdapter(getContext(), mGameList);
        mGvGames.setAdapter(mNewGameGridAdapter);
        mGvGames.setEmptyView(mFlEmptyView);

        setSwipeRefreshInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setSwipeRefreshInfo() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(1);
            }
        });
        mGvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mPage);
            }
        });
        mRefreshLayout.autoRefresh();
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
                    mGvGames.setHasLoadMore(false);
                } else {
                    mGvGames.setHasLoadMore(true);
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
                    mGvGames.showFailUI();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if (page == 1) {
                    mRefreshLayout.onRefreshComplete();
                } else {
                    mGvGames.onLoadMoreComplete();
                }

                mNewGameGridAdapter.notifyDataSetChanged();
            }
        });
    }
}
