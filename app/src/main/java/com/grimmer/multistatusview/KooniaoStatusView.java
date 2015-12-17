package com.grimmer.multistatusview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 酷鸟加载状态view
 * 
 * @author ke.wei.quan
 * @date 2015年7月24日
 * @version 1.0
 *
 */
public class KooniaoStatusView extends MultiStatusView {

	public KooniaoStatusView(Context context) {
		this(context, null);
	}

	public KooniaoStatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		super.setLoadingView(new FlyBirdLoadingView(context), false);
		super.setErrView(R.layout.network_err, false);
	}
	
	public interface OnRetryListener {
		void retryListener();
	}
	
	/**
	 * 设置重试按钮的监听事件
	 * @param onRetryListener 重试按钮监听事件
	 */
	public void setOnRetryListener(final OnRetryListener onRetryListener) {
		if (onRetryListener != null) {
			getErrView().findViewById(R.id.bt_retry).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setViewStatus(ViewStatus.STATUS_LOADING);
					onRetryListener.retryListener(); 
				}
			});
		}
	}

}
