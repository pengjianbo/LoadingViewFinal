package cn.finalteam.loadingviewfinal.sample.http;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.loadingviewfinal.sample.http.model.BaseApiResponse;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/9/29 下午4:06
 */
public abstract class MyBaseHttpRequestCallback<T extends BaseApiResponse> extends BaseHttpRequestCallback<T>{

    @Override
    protected void onSuccess(T t) {
        int code = t.getCode();
        if ( code == 1 ) {
            onLogicSuccess(t);
        } else {
            onLogicFailure(t);
        }
    }

    public abstract void onLogicSuccess(T t);

    public abstract void onLogicFailure(T t) ;
}
