package com.seventh.view;

import com.seventh.personalfinance.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author naiyu(http://snailws.com)
 * @version 1.0
 */
public class TasksCompletedView extends View {

	// ��ʵ��Բ�Ļ���
	private Paint mCirclePaint;
	// ��Բ���Ļ���
	private Paint mRingPaint;
	// ������Ļ���
	private Paint mTextPaint;
	// Բ����ɫ
	private int mCircleColor;
	// Բ����ɫ
	private int mRingColor;
	// �뾶
	private float mRadius;
	// Բ���뾶
	private float mRingRadius;
	// Բ������
	private float mStrokeWidth;
	// Բ��x����
	private int mXCenter;
	// Բ��y����
	private int mYCenter;
	// �ֵĳ���
	private float mTxtWidth;
	// �ֵĸ߶�
	private float mTxtHeight;
	// �ܽ���
	private int mTotalProgress = 100;
	// ��ǰ����
	private int mProgress;
	// ԲȦ�в�����
	private String[] mText;

	public TasksCompletedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ��ȡ�Զ��������
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TasksCompletedView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
		mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
		mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
		mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);

		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Paint.Style.FILL);

		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setARGB(255, 255, 255, 255);
		mTextPaint.setTextSize(mRadius / 4);

		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;

		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

		if (mProgress >= 0) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			//����Բ������ɫ
			mRingPaint.setColor(getResources().getColor(R.color.gray));
			canvas.drawArc(oval, -90, 360, false, mRingPaint); 
			mRingPaint.setColor(mRingColor);
			canvas.drawArc(oval, -90, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint); 
			mTxtWidth = mTextPaint.measureText(mText[0], 0, mText[0].length());
			canvas.drawText(mText[0], mXCenter - mTxtWidth / 2, mYCenter - mTxtHeight / 4, mTextPaint);
			mTxtWidth = mTextPaint.measureText(mText[1], 0, mText[1].length());
			canvas.drawText(mText[1], mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight, mTextPaint);
		}
	}

	public void setText(String text) {
		String string = text;
		mText = string.split("_");
		
	}

	public void setTotalProgress(int totalprogress) {
		mTotalProgress = totalprogress;
	}

	public void setProgress(int progress) {
		mProgress = progress;
		// invalidate();
		postInvalidate();
	}

}