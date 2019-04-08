
package com.ewell.padwidget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ewell.padwidget.R;

/**
 * 菜单item控件中心图标和右上角图片控件
 * 自定义scaleType属性会导致奔溃
 * @author zhs
 *
 */
public class CircleTextImageView extends ImageView {

    private static ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE = 22;
    private static final int DEFAULT_TEXT_PADDING = 4;
    private static final int DEFAULT_IMG_PADDING = 4;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//不添加Paint.ANTI_ALIAS_FLAG属性画出的可能不圆滑
    private final Paint mBorderPaint = new Paint();
    private final Paint mFillPaint = new Paint();
    private final Paint mTextPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mFillColor = DEFAULT_FILL_COLOR;
    private String mTextString;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextPadding=DEFAULT_TEXT_PADDING;
    private int mImgPadding=DEFAULT_IMG_PADDING;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private Drawable mDrawable;

    private float mDrawableRadius;
    private float mBorderRadius;

    private ColorFilter mColorFilter;

    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;

    public CircleTextImageView(Context context) {
        super(context);

        init();
    }

    public CircleTextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTextImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleTextImageView, defStyle, 0);

        //外环的颜色和宽度
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleTextImageView_citv_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleTextImageView_citv_border_color, DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.CircleTextImageView_citv_border_overlay, DEFAULT_BORDER_OVERLAY);

        mFillColor = a.getColor(R.styleable.CircleTextImageView_citv_fill_color, DEFAULT_FILL_COLOR);
        mTextString = a.getString(R.styleable.CircleTextImageView_citv_text_text);
        mDrawable = a.getDrawable(R.styleable.CircleTextImageView_citv_bitmap);
        if(mDrawable!=null){
        	mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        }
        mTextColor = a.getColor(R.styleable.CircleTextImageView_citv_text_color, DEFAULT_TEXT_COLOR);
        mTextSize = a.getDimensionPixelSize(R.styleable.CircleTextImageView_citv_text_size, DEFAULT_TEXT_SIZE);
        mTextPadding=a.getDimensionPixelSize(R.styleable.CircleTextImageView_citv_text_padding, DEFAULT_TEXT_PADDING);
        mImgPadding=a.getDimensionPixelSize(R.styleable.CircleTextImageView_citv_img_padding, DEFAULT_IMG_PADDING);//图片与圆形区域padding，到这里读出来已经不是资源文件里那个值，像我升级是乘1.5

        int type =a.getInt(R.styleable.CircleTextImageView_citv_scaletype, 6);
        setScaleType(type);

        //super.setPadding(mImgPadding, mImgPadding, mImgPadding, mImgPadding);
        a.recycle();

        init();
    }

    public void setScaleType(int type){
    	switch (type) {
		case 0:
			SCALE_TYPE = ScaleType.MATRIX;
			break;
		case 1:
			SCALE_TYPE = ScaleType.FIT_XY;
			break;
		case 2:
			SCALE_TYPE = ScaleType.FIT_START;
			break;
		case 3:
			SCALE_TYPE = ScaleType.FIT_CENTER;
			break;
		case 4:
			SCALE_TYPE = ScaleType.FIT_END;
			break;
		case 5:
			SCALE_TYPE = ScaleType.CENTER;
			break;
		case 6:
			SCALE_TYPE = ScaleType.CENTER_CROP;
			break;
		case 7:
			SCALE_TYPE = ScaleType.CENTER_INSIDE;
			break;
		default:
			break;
		}
    }
    
    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }


    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null&& TextUtils.isEmpty(mTextString)) {
            return;
        }

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
        }
        if(mBitmap!=null) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);
        }
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
        }

        if (!TextUtils.isEmpty(mTextString)) {

            mTextPaint.setTextSize(mTextSize);
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            canvas.drawText(mTextString,
                    getWidth() / 2 - mTextPaint.measureText(mTextString) / 2,
                    getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public String getTextString() {
        return mTextString;
    }


    public void setText(int TextResId) {//@StringRes Android support library从19.1版本开始引入了一个新的注解库
        setText(getResources().getString(TextResId));
    }
    public void setText(String textString)
    {
        this.mTextString=textString;
        setup();//从没有文字到有文字后是需要setup的不然mDrawableRadius为0 by zhs
        invalidate();

    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {//@ColorInt 
        this.mTextColor = mTextColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public void setTextColorResource(int colorResource) {//@ColorRes 
        setTextColor(getResources().getColor(colorResource));
    }

    public int getTextSize() {
        return mTextSize;
    }
    public void setTextSize(int textSize)
    {
        this.mTextSize=textSize;
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int mTextPadding) {
        this.mTextPadding = mTextPadding;
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {//@ColorInt 
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public void setBorderColorResource(int borderColorRes) {//@ColorRes 
        setBorderColor(getContext().getResources().getColor(borderColorRes));
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor(int fillColor) {//@ColorInt 
        if (fillColor == mFillColor) {
            return;
        }

        mFillColor = fillColor;
        mFillPaint.setColor(fillColor);
        
        invalidate();
        
    }

    public void setFillColorResource(int fillColorRes) {//@ColorRes 
        setFillColor(getContext().getResources().getColor(fillColorRes));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {//@DrawableRes 
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setup();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }



        if (mBitmap == null&& TextUtils.isEmpty(mTextString)) {
            invalidate();
            return;
        }


        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        if(mBitmap!=null)
        {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            mBitmapPaint.setAntiAlias(true);
            mBitmapPaint.setShader(mBitmapShader);
            updateShaderMatrix();
        }
        invalidate();
    }

    /**
     * byzhs
     * 设置圆形区域内图片位置
     */
    private void updateShaderMatrix() {
        float scaleX = 1;
        float scaleY = 1;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);


        if (mBitmapWidth + mImgPadding*2 > mDrawableRect.width()) {
            scaleX = mDrawableRect.width() / ((float) mBitmapWidth+mImgPadding*2);
           // dx = (mDrawableRect.width() - ((float) mBitmapWidth+mImgPadding*2) * scaleX) * 0.5f;
        }else{
            scaleX = mDrawableRect.width() / ((float) mBitmapWidth+mImgPadding*2);
            //dx = (mDrawableRect.width() - ((float) mBitmapWidth+mImgPadding*2) * scaleX) * 0.5f;
        }
        if (mBitmapHeight + mImgPadding*2 > mDrawableRect.height()) {
            scaleY = mDrawableRect.height() / ((float) mBitmapHeight+mImgPadding*2);
            //dy = (mDrawableRect.height() - ((float) mBitmapHeight+mImgPadding*2) * scaleY) * 0.5f;
        }else{
            scaleY = mDrawableRect.height() / ((float) mBitmapHeight+mImgPadding*2);
            //dy = (mDrawableRect.height() - ((float) mBitmapHeight+mImgPadding*2) * scaleY) * 0.5f;
        }

        mShaderMatrix.setScale(scaleX, scaleY);
        mShaderMatrix.postTranslate( (dx+0.5f) + mDrawableRect.left+mImgPadding*scaleX,  (dy+0.5f) + mDrawableRect.top+mImgPadding*scaleY);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

//    private void updateShaderMatrix() {
//        float scale;
//        float dx = 0;
//        float dy = 0;
//
//        mShaderMatrix.set(null);
//
//        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
//            scale = mDrawableRect.height() / (float) mBitmapHeight;
//            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
//        } else {
//            scale = mDrawableRect.width() / (float) mBitmapWidth;
//            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
//        }
//
//        mShaderMatrix.setScale(scale, scale);
//        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left+mImgPadding, (int) (dy + 0.5f) + mDrawableRect.top+mImgPadding);
//
//        mBitmapShader.setLocalMatrix(mShaderMatrix);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize= MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecSize= MeasureSpec.getSize(heightMeasureSpec);

        if(!TextUtils.isEmpty(mTextString))
        {
            int textMeasuredSize= (int) (mTextPaint.measureText(mTextString));
            textMeasuredSize+=2*mTextPadding;
            if(widthMeasureSpecMode== MeasureSpec.AT_MOST&&heightMeasureSpecMode== MeasureSpec.AT_MOST)
            {
                if(textMeasuredSize>getMeasuredWidth()||textMeasuredSize>getMeasuredHeight())
                {
                    setMeasuredDimension(textMeasuredSize,textMeasuredSize);
                }
            }
        }

    }
}
