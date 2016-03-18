package cn.finalteam.loadingviewfinal.sample.ui.fragment.srl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.linearlistview.LinearListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.ScrollViewFinal;
import cn.finalteam.loadingviewfinal.SwipeRefreshLayoutFinal;
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
 * Date:16/3/8 下午4:15
 */
public class SRLScrollViewFragment extends BaseFragment {
    private final int LIMIT = 12;

    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;
    @Bind(R.id.linear_list_view)
    LinearListView mLinearListView;
    @Bind(R.id.sv_games)
    ScrollViewFinal mSvGames;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayoutFinal mRefreshLayout;

    private List<GameInfo> mGameList;
    private NewGameListAdapter mNewGameListAdapter;

    private int mPage = 1;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_srl_scrollview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(getContext(), mGameList);
        mLinearListView.setAdapter(mNewGameListAdapter);

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
        mSvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
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
                    mFlEmptyView.setVisibility(View.VISIBLE);
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
                    mSvGames.setHasLoadMore(false);
                } else {
                    mSvGames.setHasLoadMore(true);
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
                    mSvGames.showFailUI();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if(mGameList.size() != 0) {
                    mFlEmptyView.setVisibility(View.GONE);
                } else {
                    mFlEmptyView.setVisibility(View.VISIBLE);
                }
                if (page == 1) {
                    mRefreshLayout.onRefreshComplete();
                } else {
                    mSvGames.onLoadMoreComplete();
                }

                mNewGameListAdapter.notifyDataSetChanged();
            }
        });
    }
}
