package cn.finalteam.loadingviewfinal.sample.ui.fragment.ptr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.OnDefaultRefreshListener;
import cn.finalteam.loadingviewfinal.PtrClassicFrameLayout;
import cn.finalteam.loadingviewfinal.PtrFrameLayout;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.ui.fragment.BaseFragment;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:16
 */
public class PtrWebViewFragment extends BaseFragment {
    @Bind(R.id.fl_empty_view)
    FrameLayout mFlEmptyView;
    @Bind(R.id.webview)
    WebView mWebview;
    @Bind(R.id.ptr_layout)
    PtrClassicFrameLayout mPtrLayout;

    @Override
    public int getContentViewLayout() {
        return R.layout.fragment_ptr_webview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mWebview.setWebChromeClient(defaultWebChromeClient);
        mPtrLayout.setOnRefreshListener(defaultOnRefreshListener);

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
            if(isDetached()){
                return;
            }
            if (newProgress == 100) {
                mFlEmptyView.setVisibility(View.GONE);
                mPtrLayout.onRefreshComplete();
            } else {
                mFlEmptyView.setVisibility(View.VISIBLE);
            }
        }
    };

    private final OnDefaultRefreshListener defaultOnRefreshListener = new OnDefaultRefreshListener() {
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mFlEmptyView.setVisibility(View.VISIBLE);
            mWebview.reload();
        }
    };
}
