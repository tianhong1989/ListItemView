package com.lucasurbas.listitemview.util.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.lucasurbas.listitemview.util.ViewUtils;

/**
 * Description.
 *
 * @author urbl
 */
public class CircularIconView extends View {

    private Paint mPaint;

    private RectF mRect;

    private Bitmap mMask;

    private Xfermode xfermode;

    private Drawable mIconDrawable;

    @ColorInt
    private int mColor;

    public CircularIconView(final Context context) {
        super(context);
        init();
    }

    public CircularIconView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mColor = ViewUtils.getDefaultColor(getContext());
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(0, 0, w, h);
        swapBitmapMask(makeBitmapMask(mIconDrawable));
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (canvas.getHeight() > 0 && canvas.getWidth() > 0 && mMask != null) {

            int sc = canvas.save(Canvas.ALL_SAVE_FLAG);

            mPaint.setColor(mColor);
            mPaint.setXfermode(null);
            canvas.drawOval(mRect, mPaint);

            mPaint.setColor(Color.WHITE);
            mPaint.setXfermode(xfermode);
            canvas.drawBitmap(mMask, 0.0f, 0.0f, mPaint);

            mPaint.setXfermode(null);

            canvas.restoreToCount(sc);
        }
    }

    public void setIcon(final Drawable iconDrawable) {
        mIconDrawable = iconDrawable;
        swapBitmapMask(makeBitmapMask(iconDrawable));
    }

    @Nullable
    private Bitmap makeBitmapMask(@Nullable Drawable drawable) {
        int mh = getMeasuredHeight();
        int mw = getMeasuredWidth();
        if (drawable != null) {
            if (mw > 0 && mh > 0) {
                Bitmap mask = Bitmap.createBitmap(mw, mh,
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                int h = drawable.getIntrinsicHeight();
                int w = drawable.getIntrinsicWidth();
                drawable.setBounds((mw - w) / 2, (mh - h) / 2, ((mw - w) / 2) + w, ((mh - h) / 2) + h);
                drawable.draw(canvas);
                return mask;
            }
        }
        return null;
    }

    private void swapBitmapMask(@Nullable Bitmap newMask) {
        if (newMask != null) {
            if (mMask != null && !mMask.isRecycled()) {
                mMask.recycle();
            }
            mMask = newMask;
        }
    }
}