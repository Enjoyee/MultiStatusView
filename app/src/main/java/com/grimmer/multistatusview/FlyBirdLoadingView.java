package com.grimmer.multistatusview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 飞翔的酷鸟加载页
 * 
 * @author ke.wei.quan
 * @date 2015年7月23日
 * @version 1.0
 *
 */
public class FlyBirdLoadingView extends LinearLayout {
	private static float WIDTH;
	private static float DENSITY;

	private static float BIRD_MAX_SCALE = 0f;
	private static float BIRD_MIN_SCALE = 0f;

	private static float SHADOW_MAX_SCALE = 1f;
	private static float SHADOW_MIN_SCALE = 0.7f;

	private ImageView mFlyBirdImageView; // 飞翔的小鸟
	private View mShadowView; // 阴影
	private View mCloudUpView; // 顶部的云
	private View mCloudDownView; // 底部的云

	// 动画时长
	final int DURATION_TIME = 600;

	public FlyBirdLoadingView(Context context) {
		this(context, null);
	}

	public FlyBirdLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		initData();
		initView(context);
		startAnimation();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		DENSITY = getResources().getDisplayMetrics().density;
		WIDTH = getResources().getDisplayMetrics().widthPixels;
		BIRD_MAX_SCALE = 20 * DENSITY;
	}

	/**
	 * 初始化view
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		View contentView = LayoutInflater.from(context).inflate(R.layout.fly_bird_loading, this);
		mFlyBirdImageView = (ImageView) contentView.findViewById(R.id.iv_fly_bird);
		mShadowView = contentView.findViewById(R.id.view_shadow);
		mCloudUpView = contentView.findViewById(R.id.iv_cloud_up);
		mCloudDownView = contentView.findViewById(R.id.iv_cloud_down);
	}

	/**
	 * 启动动画
	 */
	private void startAnimation() {
		// 小鸟上下运动的动画
		ValueAnimator birdAnimator = ObjectAnimator.ofFloat(mFlyBirdImageView, "translationY", BIRD_MIN_SCALE, -BIRD_MAX_SCALE);
		birdAnimator.setInterpolator(new LinearInterpolator());
		birdAnimator.setRepeatMode(ValueAnimator.REVERSE);
		birdAnimator.setRepeatCount(ValueAnimator.INFINITE);
		birdAnimator.setDuration(DURATION_TIME).start();
		birdAnimator.setStartDelay(300);

		// 阴影放大缩小的动画
		PropertyValuesHolder shadowXValuesHolder = PropertyValuesHolder.ofFloat("scaleX", SHADOW_MAX_SCALE, SHADOW_MIN_SCALE);
		PropertyValuesHolder shadowYValuesHolder = PropertyValuesHolder.ofFloat("scaleY", SHADOW_MAX_SCALE, SHADOW_MIN_SCALE);
		ObjectAnimator shadowAnimator = ObjectAnimator.ofPropertyValuesHolder(mShadowView, shadowXValuesHolder, shadowYValuesHolder);
		shadowAnimator.setRepeatMode(ObjectAnimator.REVERSE);
		shadowAnimator.setRepeatCount(ObjectAnimator.INFINITE);
		shadowAnimator.setDuration(DURATION_TIME).start();
		shadowAnimator.setStartDelay(300);

		// 云朵移动的动画
		final float maxTranslationX = WIDTH / 3 + DENSITY * 100;
		ValueAnimator cloudAnimator = ValueAnimator.ofFloat(0, maxTranslationX);
		cloudAnimator.setInterpolator(new LinearInterpolator());
		cloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
		cloudAnimator.setDuration(1500).start();
		cloudAnimator.setStartDelay(300);
		cloudAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float animatedValue = (float) animation.getAnimatedValue();
				// 上面的云
				float cloudUpTranslationX = (DENSITY * 15 + animatedValue - maxTranslationX) % maxTranslationX;
				mCloudUpView.setTranslationX(-cloudUpTranslationX - DENSITY * 25);

				// 下面的云
				float cloudDownTranslationX = (DENSITY * 25 + animatedValue) % maxTranslationX;
				mCloudDownView.setTranslationX(-cloudDownTranslationX + DENSITY * 25);
			}
		});
	}
}
