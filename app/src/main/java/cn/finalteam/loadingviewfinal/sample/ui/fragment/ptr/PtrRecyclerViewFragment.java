package cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.http.Api;
import cn.finalteam.loadingviewfinal.sample.http.MyBaseHttpRequestCallback;
import cn.finalteam.loadingviewfinal.sample.http.model.GameInfo;
import cn.finalteam.loadingviewfinal.sample.http.model.NewGameResponse;
import cn.finalteam.loadingviewfinal.sample.ui.adapter.NewGameRvAdapter;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;
import cn.finalteam.loadingviewfinal.sample.utils.EmptyViewUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:12
 */
public class PtrRecyclerViewFragment extends BaseFragment {

    private final int LIMIT = 12;

    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;
    @Bind(R.id.rv_games)
    RecyclerViewFinal mRvGames;
    @Bind(R.id.ptr_rv_layout)
    PtrClassicFrameLayout mPtrRvLayout;

    private List<GameInfo> mGameList;
    private NewGameRvAdapter mNewGameRvAdapter;

    private int mPage = 1;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_ptr_recyclerview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mGameList = new ArrayList<>();
        mNewGameRvAdapter = new NewGameRvAdapter(getContext(), mGameList);
        //        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRvGames.setLayoutManager(linearLayoutManager);
        //        mRvGames.setLayoutManager(linearLayoutManager);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 2,
                GridLayoutManager.VERTICAL, false);
// 设置布局管理器
        mRvGames.setLayoutManager(gridLayoutManager);
//        mRvGames.setLayoutManager(new StaggeredGridLayoutManager(2,
//                StaggeredGridLayoutManager.VERTICAL));
        mRvGames.setEmptyView(mFlEmptyView);
        mRvGames.setAdapter(mNewGameRvAdapter);

        setSwipeRefreshInfo();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setSwipeRefreshInfo() {
        mRvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                requestData(mPage);
            }
        });

        mPtrRvLayout.setLastUpdateTimeRelateObject(this);
        mPtrRvLayout.disableWhenHorizontalMove(true);
        mPtrRvLayout.setOnRefreshListener(new OnDefaultRefreshListener() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestData(1);
            }
        });
        mPtrRvLayout.autoRefresh();
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
                    mRvGames.setHasLoadMore(false);
                } else {
                    mRvGames.setHasLoadMore(true);
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
                    mRvGames.showFailUI();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if (page == 1) {
                    mPtrRvLayout.onRefreshComplete();
                } else {
                    mRvGames.onLoadMoreComplete();
                }

                mNewGameRvAdapter.notifyDataSetChanged();
            }
        });
    }
}
