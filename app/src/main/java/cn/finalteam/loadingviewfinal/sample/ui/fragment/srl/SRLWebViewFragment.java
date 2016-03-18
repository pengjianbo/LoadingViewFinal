package cn.finalteam.loadingviewfinal.sample.ui.fragment.srl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.SwipeRefreshLayoutFinal;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:16
 */
public class SRLWebViewFragment extends BaseFragment {
    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;
    @Bind(R.id.webview)
    WebView mWebview;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayoutFinal mRefreshLayout;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_srl_webview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mWebview.setWebChromeClient(defaultWebChromeClient);
        mRefreshLayout.setOnRefreshListener(refreshListener);

        mWebview.loadUrl("https://github.com/pengjianbo");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private final WebChromeClient defaultWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(mFlEmptyView != null) {
                if (newProgress == 100) {
                    mFlEmptyView.setVisibility(View.GONE);
                    mRefreshLayout.onRefreshComplete();
                } else {
                    mFlEmptyView.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mFlEmptyView.setVisibility(View.VISIBLE);
            mWebview.reload();
        }
    };
}
