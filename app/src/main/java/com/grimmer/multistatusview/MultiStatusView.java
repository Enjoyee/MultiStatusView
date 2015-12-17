package com.grimmer.multistatusview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 多状态选择view(加载中，加载失败，加载成功)
 * Created by keweiquan on 15/11/26.
 */
public class MultiStatusView extends FrameLayout {
    private static final int UNKNOWN_VIEW = -1;
    private static final int LOADING_VIEW = 0;
    private static final int ERROR_VIEW = 1;
    private static final int CONTENT_VIEW = 2;
    private static final int EMPTY_VIEW = 3;

    public enum ViewStatus {
        // 加载
        STATUS_LOADING,
        // 错误
        STATUS_ERR,
        // 成功
        STATUS_SUCCESS,
        // 空
        STATUS_EMPTY
    }

    public MultiStatusView(Context context) {
        this(context, null);
    }

    private Context mContext;

    public MultiStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(attrs);
    }

    private View mLoadingView;
    private View mErrView;
    private View mEmptyView;
    private View mContentView;
    private ViewStatus mViewStatus;

    private void initView(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.multi_status_view);

        int loadingResourceId = ta.getResourceId(R.styleable.multi_status_view_msv_loading, R.layout.empty_loading);
        mLoadingView = LayoutInflater.from(mContext).inflate(loadingResourceId, this, false);
        addView(mLoadingView, mLoadingView.getLayoutParams());

        int failResourceId = ta.getResourceId(R.styleable.multi_status_view_msv_err, R.layout.empty_err);
        mErrView = LayoutInflater.from(mContext).inflate(failResourceId, this, false);
        addView(mErrView, mErrView.getLayoutParams());

        int emptyResourceId = ta.getResourceId(R.styleable.multi_status_view_msv_empty, R.layout.empty);
        mEmptyView = LayoutInflater.from(mContext).inflate(emptyResourceId, this, false);
        addView(mEmptyView, mEmptyView.getLayoutParams());

        int viewStatus = ta.getInt(R.styleable.multi_status_view_msv_view_status, UNKNOWN_VIEW);
        if (viewStatus != UNKNOWN_VIEW) {
            switch (viewStatus) {
                case LOADING_VIEW:
                    mViewStatus = ViewStatus.STATUS_LOADING;
                    break;

                case ERROR_VIEW:
                    mViewStatus = ViewStatus.STATUS_ERR;
                    break;

                case CONTENT_VIEW:
                    mViewStatus = ViewStatus.STATUS_SUCCESS;
                    break;

                case EMPTY_VIEW:
                    mViewStatus = ViewStatus.STATUS_EMPTY;
                    break;
            }
        }

        ta.recycle();
    }

    /**
     * 设置view的状态
     *
     * @param viewStatus 状态
     */
    public void setViewStatus(ViewStatus viewStatus) {
        if (viewStatus == ViewStatus.STATUS_LOADING) { // 加载中
            mLoadingView.setVisibility(View.VISIBLE);
            mErrView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        } else if (viewStatus == ViewStatus.STATUS_ERR) { // 加载失败
            mLoadingView.setVisibility(View.GONE);
            mErrView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        } else if (viewStatus == ViewStatus.STATUS_SUCCESS) { // 加载成功
            mLoadingView.setVisibility(View.GONE);
            mErrView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.VISIBLE);
        } else if (viewStatus == ViewStatus.STATUS_EMPTY) { // 空
            mLoadingView.setVisibility(View.GONE);
            mErrView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        } else {
            mLoadingView.setVisibility(View.GONE);
            mErrView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null)
            throw new IllegalArgumentException("Content view is not defined");
        setViewStatus(mViewStatus);
    }

    @Override
    public void addView(View child) {
        if (isValidContentView(child))
            mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child))
            mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child))
            mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child))
            mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child))
            mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child))
            mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child))
            mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mErrView && view != mEmptyView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setLoadingView(View loadingView, boolean isSwitchToStatus) {
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        this.mLoadingView = loadingView;
        if (!isSwitchToStatus) {
            mLoadingView.setVisibility(GONE);
        } else {
            mViewStatus = ViewStatus.STATUS_LOADING;
            setViewStatus(mViewStatus);
        }
        addView(loadingView);
    }

    public void setLoadingView(@LayoutRes int resId, boolean isSwitchToStatus) {
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        View loadingView = LayoutInflater.from(mContext).inflate(resId, this, false);
        this.mLoadingView = loadingView;
        if (!isSwitchToStatus) {
            mLoadingView.setVisibility(GONE);
        } else {
            mViewStatus = ViewStatus.STATUS_LOADING;
            setViewStatus(mViewStatus);
        }
        addView(loadingView);
    }

    public View getErrView() {
        return mErrView;
    }

    public void setErrView(View errView, boolean isSwitchToStatus) {
        if (mErrView != null) {
            removeView(mErrView);
        }
        this.mErrView = errView;
        if (!isSwitchToStatus) {
            mErrView.setVisibility(GONE);
        } else {
            mViewStatus = ViewStatus.STATUS_ERR;
            setViewStatus(mViewStatus);
        }
        addView(errView);
    }

    public void setErrView(@LayoutRes int resId, boolean isSwitchToStatus) {
        if (mErrView != null) {
            removeView(mErrView);
        }
        View errView = LayoutInflater.from(mContext).inflate(resId, this, false);
        this.mErrView = errView;
        if (!isSwitchToStatus) {
            mErrView.setVisibility(GONE);
        } else {
            mViewStatus = ViewStatus.STATUS_ERR;
            setViewStatus(mViewStatus);
        }
        addView(errView);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView, boolean isSwitchToStatus) {
        if (mEmptyView != null) {
            removeView(mEmptyView);
        }
        this.mEmptyView = emptyView;
        if (!isSwitchToStatus) {
            mEmptyView.setVisibility(GONE);
        } else {
            mViewStatus = ViewStatus.STATUS_EMPTY;
            setViewStatus(mViewStatus);
        }
        addView(emptyView);
    }

    public void setEmptyView(int resId, boolean isSwitchToStatus) {
        if (mEmptyView != null) {
            removeView(mEmptyView);
        }
        View emptyView = LayoutInflater.from(mContext).inflate(resId, this, false);
        this.mEmptyView = emptyView;
        if (!isSwitchToStatus) {
            mEmptyView.setVisibility(GONE);
        } else {
            mViewStatus = ViewStatus.STATUS_EMPTY;
            setViewStatus(mViewStatus);
        }
        addView(emptyView);
    }

}
