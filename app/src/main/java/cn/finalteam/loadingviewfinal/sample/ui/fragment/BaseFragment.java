package cn.finalteam.loadingviewfinal.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpTaskHandler;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/8 下午4:57
 */
public abstract class BaseFragment extends Fragment implements HttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        return view;
    }

    public abstract int getContentViewLayout();

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
