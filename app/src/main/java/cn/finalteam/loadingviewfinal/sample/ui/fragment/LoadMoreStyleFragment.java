package cn.finalteam.loadingviewfinal.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.DefaultLoadMoreView;
import cn.finalteam.loadingviewfinal.ILoadMoreView;
import cn.finalteam.loadingviewfinal.ListViewFinal;
import cn.finalteam.loadingviewfinal.OnLoadMoreListener;
import cn.finalteam.loadingviewfinal.SwipeRefreshLayoutFinal;
import cn.finalteam.loadingviewfinal.loadingview.style.AVLoadMoreView;
import cn.finalteam.loadingviewfinal.loadingview.style.AVLoadingIndicatorView;
import cn.finalteam.loadingviewfinal.loadingview.style.LoadMoreStyle;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.event.RestartLoadMoreFragmentEvent;
import cn.finalteam.loadingviewfinal.sample.http.Api;
import cn.finalteam.loadingviewfinal.sample.http.MyBaseHttpRequestCallback;
import cn.finalteam.loadingviewfinal.sample.http.model.GameInfo;
import cn.finalteam.loadingviewfinal.sample.http.model.NewGameResponse;
import cn.finalteam.loadingviewfinal.sample.ui.adapter.NewGameListAdapter;
import cn.finalteam.loadingviewfinal.sample.utils.EmptyViewUtils;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.AppCacheUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/16 下午1:51
 */
public class LoadMoreStyleFragment extends BaseFragment {

    private final int LIMIT = 12;

    @Bind(R.id.lv_games)
    public ListViewFinal mLvGames;
    @Bind(R.id.refresh_layout)
    public SwipeRefreshLayoutFinal mRefreshLayout;
    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;

    private List<GameInfo> mGameList;
    private NewGameListAdapter mNewGameListAdapter;

    private int mPage = 1;

    Button mBtnLoadMoreStyle;

    private AVLoadMoreView mAvLoadMoreView;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_srl_listview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(getContext(), mGameList);
        mAvLoadMoreView = LoadMoreStyle.getAVLoadMoreViewFactory(getContext());
        mAvLoadMoreView.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        int which = AppCacheUtils.getInstance(getContext()).getInt("loadmorestyle_which_cache", 0);
        //设置setLoadMoreView要在setAdapter之前
        mLvGames.setLoadMoreView(getLoadMoreByIndex(which));
        mLvGames.setAdapter(mNewGameListAdapter);
        mLvGames.setEmptyView(mFlEmptyView);
        setSwipeRefreshInfo();



        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_loadmorestyle_head, null);
        mBtnLoadMoreStyle = (Button) v.findViewById(R.id.btn_load_more_style);
        mBtnLoadMoreStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("请选择加载更多样式")
                        .items(R.array.load_more_style)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                AppCacheUtils.getInstance(getContext()).put("loadmorestyle_which_cache", which);
                                EventBus.getDefault().post(new RestartLoadMoreFragmentEvent());
                            }
                        })
                        .show();
            }
        });
        mLvGames.addHeaderView(v);
    }

    private ILoadMoreView getLoadMoreByIndex(int index) {
        switch (index) {
            case 0:
                return new DefaultLoadMoreView(getContext());
            case 1:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulse);
                break;
            case 2:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallGridPulse);
                break;
            case 3:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallClipRotate);
                break;
            case 4:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallClipRotatePulse);
                break;
            case 5:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.SquareSpin);
                break;
            case 6:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallClipRotateMultiple);
                break;
            case 7:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulseRise);
                break;
            case 8:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.CubeTransition);
                break;
            case 9:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallZigZag);
                break;
            case 10:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallZigZagDeflect);
                break;
            case 11:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallTrianglePath);
                break;
            case 12:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScale);
                break;
            case 13:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScale);
                break;
            case 14:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScaleParty);
                break;
            case 15:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScaleMultiple);
                break;
            case 16:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallPulseSync);
                break;
            case 17:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallBeat);
                break;
            case 18:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScalePulseOut);
                break;
            case 19:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineScalePulseOutRapid);
                break;
            case 20:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScaleRipple);
                break;
            case 21:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallScaleRippleMultiple);
                break;
            case 22:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallSpinFadeLoader);
                break;
            case 23:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.LineSpinFadeLoader);
                break;
            case 24:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.TriangleSkewSpin);
                break;
            case 25:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.Pacman);
                break;
            case 26:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.BallGridBeat);
                break;
            case 27:
                mAvLoadMoreView.setIndicatorId(AVLoadingIndicatorView.SemiCircleSpin);
                break;
        }

        return mAvLoadMoreView;
    }

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
        mLvGames.setOnLoadMoreListener(new OnLoadMoreListener() {
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
                    mRefreshLayout.onRefreshComplete();
                } else {
                    mLvGames.onLoadMoreComplete();
                }

                mNewGameListAdapter.notifyDataSetChanged();
            }
        });
    }


}
