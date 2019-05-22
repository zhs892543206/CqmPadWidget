package com.ewell.padwidget.edittext;

/**
 * 左侧有固定文字的edit
 * 其实textview+edittext达到的效果是一样的啦
 * 不过这个存在个bug当输入内容过多，会导致左侧提示文字被推动到看不到的地方。
 * lefttext属性，需要和android:maxLength="10"结合使用或者不要设置单行。不过行数过多还是会看不到。不过这里就无所谓啦。影响不大
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.ewell.padwidget.R;
import com.ewell.padwidget.keyboard.security.SecurityEditText;


public class LeftSecurityEditText extends SecurityEditText implements
        View.OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;

    /**
     * 左图片
     */
    private Drawable mLeftDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    private Context mContext;
    private static final String DEFAULT_LEFT_TXT = "";//左侧默认文本
    private static final int DEFAULT_LEFT_TEXT_COLOR = Color.BLACK;
    private static int DEFAULT_LEFT_TEXT_SIZE = 22;//单位px
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//不添加Paint.ANTI_ALIAS_FLAG属性画出的可能不圆滑
    private int mLeftTextColor = DEFAULT_LEFT_TEXT_COLOR;
    private int mLeftTextSize = DEFAULT_LEFT_TEXT_SIZE;
    //private int mLeftTextPadding = 0;//只是用来设置左边距了
    private String leftText="";//从xml获取到的左侧文本
    private boolean isAddLeftTxt = false;
    private boolean isErrorInfo = false;//是否显示错误提示

    // 最小字体
    private static final float DEFAULT_MIN_TEXT_SIZE = 8.0f;
    // 最大字体
    private static final float DEFAULT_MAX_TEXT_SIZE = 16.0f;

    private Paint textPaint;
    private float minTextSize = DEFAULT_MIN_TEXT_SIZE;
    private float maxTextSize = DEFAULT_MAX_TEXT_SIZE;

    private static final int DefaultDrawablePadding = 12;//px

    //设置左图padding,因为图片原因其实一般要求等宽高的，所以宽和高都由纵向大小决定，leftDrawablePaddingRight是没用的，leftDrawablePaddingLeft是有用的
//    private int leftDrawablePadding = DefaultDrawablePadding;
    private int leftDrawablePaddingLeft = 0;
//    private int leftDrawablePaddingRight = DefaultDrawablePadding;
//    private int leftDrawablePaddingTop = DefaultDrawablePadding;
//    private int leftDrawablePaddingBottom = DefaultDrawablePadding;

    private int compoundDrawablePadding = 36;//用于setCompoundDrawablePadding方法设置左侧图片边距

    private int viewWidth;
    private int viewHeight;
    private OnClickListener mListener;
    //如果在Code中实例化一个View会调用第一个构造函数，如果在xml中定义会调用第二个构造函数，而第三个函数系统是不调用的，要由View（我们自定义的或系统预定义的View，如此处的CustomTextView和Button）显式调用
    public LeftSecurityEditText(Context context){
        this(context, null);
    }

    public LeftSecurityEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public LeftSecurityEditText(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);

        mContext =context;
        //textsize得到的就是px的
        DEFAULT_LEFT_TEXT_SIZE = (int)getTextSize();//DensityUtil.px2sp(context, getTextSize());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeftTxtEditText, defstyle, 0);
        mLeftTextColor = a.getColor(R.styleable.LeftTxtEditText_left_text_color, DEFAULT_LEFT_TEXT_COLOR);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_left_text_size, DEFAULT_LEFT_TEXT_SIZE);

        //获取左侧图片边距
//        leftDrawablePadding = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_leftdrawable_padding, DefaultDrawablePadding);
        //其他方向padding没设置则变为leftDrawablePadding的值
        leftDrawablePaddingLeft = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_leftdrawable_paddingleft, 0);
//        leftDrawablePaddingRight = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_leftdrawable_paddingright, leftDrawablePadding);
//        leftDrawablePaddingTop = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_leftdrawable_paddingtop, leftDrawablePadding);
//        leftDrawablePaddingBottom = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_leftdrawable_paddingbottom, leftDrawablePadding);
//        leftDrawablePaddingLeft = DensityUtil.px2dip(mContext, leftDrawablePaddingLeft);
//        leftDrawablePaddingRight = DensityUtil.px2dip(mContext, leftDrawablePaddingRight);
//        leftDrawablePaddingTop = DensityUtil.px2dip(mContext, leftDrawablePaddingTop);
//        leftDrawablePaddingBottom = DensityUtil.px2dip(mContext, leftDrawablePaddingBottom);
        //setCompoundDrawablePadding(leftDrawablePaddingRight);

        compoundDrawablePadding = a.getDimensionPixelSize(R.styleable.LeftTxtEditText_compounddrawable_padding, DefaultDrawablePadding);
        leftText = a.getString(R.styleable.LeftTxtEditText_left_text);
        if (leftText==null){
            leftText = "";
        }
        //文字右侧加点空格，代替边距
        if(leftText.length()>0) {
            leftText += "  ";
        }
       //mLeftTextPadding = getResources().getDimensionPixelSize(R.dimen.widget_padding);
        //mLeftTextPadding = getPaddingLeft();px
        //转换为dp
//        float scale = getResources().getDisplayMetrics().density;
//        mLeftTextPadding = (int)((mLeftTextPadding-0.5f)/scale);
        setLeftText();

        int rightDrawablePadding = getResources().getDimensionPixelSize(R.dimen.widget_padding);

        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            //return;//当没设置该图片则不显示默认的
            //mClearDrawable = getResources().getDrawable(R.mipmap.edit_del);
        }else {

            /**
             *  第一个属性只是为了给右边加点边距，则第三个属性需要加等量的值。三四属性默认-rightDrawablePadding是为了让图片小点
             *  setBounds里参数估计是dp吧
             *  (mClearDrawable.getIntrinsicWidth()返回的是dp
             */

            mClearDrawable.setBounds(-1 * (int) (rightDrawablePadding), 0, (int) (mClearDrawable.getIntrinsicWidth() + (-1 * rightDrawablePadding * (1 + 3))), mClearDrawable.getIntrinsicHeight() - (int) (rightDrawablePadding * 3));

        }setRightClearDrawable();
        setLeftDrawable();
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
        initialiseTextPaint();
//
//        ViewTreeObserver vto = getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                getViewTreeObserver().removeOnPreDrawListener(this);
//                int height = getMeasuredHeight();
//                viewWidth =getMeasuredWidth();
//                return true;
//            }
//        });
    }

    private void initialiseTextPaint() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        if (this.textPaint == null) {
            this.textPaint = new Paint();
            this.textPaint.set(this.getPaint());
        }
        //getTextSize是px 不是我们设置的sp需要除以fontScale
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        this.maxTextSize = getTextSize()/fontScale;//TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.maxTextSize, displayMetrics);
        //如果设置的最大比默认最小还小则最消变为默认最大
        if (DEFAULT_MIN_TEXT_SIZE >= maxTextSize) {
            this.minTextSize = this.maxTextSize;
        }
    }

    /**
     * 设置左侧图片
     */
    public void setRightClearDrawable(){
        //获取EditText的DrawableLeft
        mClearDrawable = getCompoundDrawables()[2];
        //edit设置的padding,dipValue * scale + 0.5f = px;
        int rightDrawablePadding = getPaddingRight();//getResources().getDimensionPixelSize(R.dimen.widget_padding);//DensityUtil.px2dip(mContext, getPaddingLeft());

        if (mClearDrawable == null) {
        }else {
            /**
             *  第一个属性只是为了给右边加点边距，则第三个属性需要加等量的值。三四属性默认-rightDrawablePadding是为了让图片小点
             *  setBounds里参数估计是dp吧
             *  (mClearDrawable.getIntrinsicWidth()返回的是dp
             */
            //(int) (DensityUtil.px2dip(mContext, viewHeight)
            //mLeftDrawable.setBounds(-1*leftDrawablePadding, 0, (int) (mLeftDrawable.getIntrinsicWidth() + (-1 * leftDrawablePadding * (2))), mLeftDrawable.getIntrinsicHeight() - (int) (1*leftDrawablePadding));
            //宽度最终是(int) (mLeftDrawable.getIntrinsicWidth() - leftDrawablePaddingRight) - (int) (leftDrawablePaddingLeft)
            //设置leftdrawable与右侧文字之间距离
            int drawToTxtPadding = px2dip(mContext, compoundDrawablePadding);//compoundDrawablePadding;//DensityUtil.px2dip(MyApplication.getInstance(), compoundDrawablePadding);
            int x = leftDrawablePaddingLeft;
            //只是根据文字的边距还是有点大的，所以加个compoundDrawablePadding
            mClearDrawable.setBounds(viewHeight - getPaddingTop() - getPaddingBottom() - compoundDrawablePadding, 0
                    , 0
                    , viewHeight - getPaddingTop() - getPaddingBottom()-compoundDrawablePadding);
            //mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
            setCompoundDrawablePadding(drawToTxtPadding);//设置leftdrawable与右侧文字之间距离，单位dp
        }
    }

    /**
     * 设置左侧图片
     */
    public void setLeftDrawable(){
        //获取EditText的DrawableLeft
        mLeftDrawable = getCompoundDrawables()[0];
        //edit设置的padding,dipValue * scale + 0.5f = px;
        int leftDrawablePadding = getPaddingLeft();//getResources().getDimensionPixelSize(R.dimen.widget_padding);//DensityUtil.px2dip(mContext, getPaddingLeft());

        if (mLeftDrawable == null) {
        }else {
            /**
             *  第一个属性只是为了给右边加点边距，则第三个属性需要加等量的值。三四属性默认-rightDrawablePadding是为了让图片小点
             *  setBounds里参数估计是dp吧
             *  (mClearDrawable.getIntrinsicWidth()返回的是dp
             */
            //(int) (DensityUtil.px2dip(mContext, viewHeight)
            //mLeftDrawable.setBounds(-1*leftDrawablePadding, 0, (int) (mLeftDrawable.getIntrinsicWidth() + (-1 * leftDrawablePadding * (2))), mLeftDrawable.getIntrinsicHeight() - (int) (1*leftDrawablePadding));
            //宽度最终是(int) (mLeftDrawable.getIntrinsicWidth() - leftDrawablePaddingRight) - (int) (leftDrawablePaddingLeft)
            //设置leftdrawable与右侧文字之间距离
            int drawToTxtPadding = px2dip(mContext, compoundDrawablePadding);//compoundDrawablePadding;//DensityUtil.px2dip(MyApplication.getInstance(), compoundDrawablePadding);
            int x = leftDrawablePaddingLeft;
            //只是根据文字的边距还是有点大的，所以加个compoundDrawablePadding
            mLeftDrawable.setBounds(0, 0
                    , viewHeight - getPaddingTop() - getPaddingBottom() - compoundDrawablePadding
                    , viewHeight - getPaddingTop() - getPaddingBottom()-compoundDrawablePadding);
            //mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
            setCompoundDrawablePadding(drawToTxtPadding);//设置leftdrawable与右侧文字之间距离，单位dp
        }
    }


    /**
     * 设置左侧文本
     */
    public void setLeftText() {
        mTextPaint.setColor(mLeftTextColor);
        mTextPaint.setTextSize(mLeftTextSize);

        int left = (int) mTextPaint.measureText(leftText)+ getPaddingLeft();
        setPadding(left, getPaddingTop(), getPaddingBottom(), getPaddingRight());
        invalidate();
    }

    public void setDrawableClk(OnClickListener listener) {
        mListener = listener;
    }

    /**
     * 刷新一般是把原来的清空，然后再写
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(leftText)) {

            //添加左侧文字
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            canvas.drawText(leftText,
                    getPaddingLeft(),
                    getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2, mTextPaint);
        }
        //当宽度变化，说明是布局画好了，计算出高度了
        if(viewWidth!=getMeasuredWidth()) {
            //px
            viewWidth = getMeasuredWidth();
            viewHeight = getMeasuredHeight();
            setLeftDrawable();
            //默认设置隐藏图标
            setClearIconVisible(false);
        }
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mListener != null &&
        if (getCompoundDrawables()[2] != null) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_UP:
                    //int i = getMeasuredWidth() - getCompoundDrawables()[2].getIntrinsicWidth();
//                    if (event.getX() > i) {
//                        this.setText("");
//                        //mListener.onClick(this);
//                        return true;
//                    }
                    //就减发现范围太小就除2和乘1.5了。不知道是不是我上面mClearDrawable.setBounds导致的
                    boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()*1.5)
                            && (event.getX() < ((getWidth() - getPaddingRight()/2)));
                    if (touchable) {
                        this.setText("");
                    }
                    break;
                default:
                    break;
            }

        }

        return super.onTouchEvent(event);
    }



    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //当焦点改变或者内容改变错误提示关闭
        if(isErrorInfo) {
            setNormalStatus();
        }
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }




    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(mLeftDrawable,
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);

    }




    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        super.onTextChanged(s, start, count, after);
        if(s!=null) {
            // 计算控件宽度
            int textWidth = viewWidth;
            // 拿到宽度和内容，进行调整
            this.fitText(s.toString(), textWidth);
        }
        if(isErrorInfo) {
            setNormalStatus();
        }
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {


    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置错误提示
     */
    public void setErrorInfo(String errorMsg){
        //设置红色下划线
        setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_whitebg_error));
        //设置输入内容颜色为红色
        setTextColor(getResources().getColor(R.color.red));
        setError(errorMsg);
        isErrorInfo = true;
    }

    /**
     * 错误提示后恢复正常状态
     */
    public void setNormalStatus(){
        //设置红色下划线
        setBackgroundDrawable(getResources().getDrawable(R.color.white));
        //设置输入内容颜色为红色
        setTextColor(getResources().getColor(R.color.black));
        isErrorInfo = false;
    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }




    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void fitText(String text, int textWidth) {
        if (textWidth > 0) {
            //左右图片大小
            int leftDrawableWidth = 0;
            if(mLeftDrawable!=null){
                //px
                leftDrawableWidth = mLeftDrawable.getIntrinsicWidth();
            }
            int rightDrawableWidth = 0;
            if(mClearDrawable!=null){
                rightDrawableWidth = mClearDrawable.getIntrinsicWidth();
            }
            // 单行可见文字宽度,getpadding是px
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight() - leftDrawableWidth - rightDrawableWidth;
            float trySize = maxTextSize;
            // 先用最大字体写字
            textPaint.setTextSize(trySize);
            // 如果最大字体>最小字体 && 最大字体画出字的宽度>单行可见文字宽度。measureText返回dp
            while ((trySize > minTextSize) && (dip2px(mContext, textPaint.measureText(text)) > availableWidth)) {
                // 最大字体小一号
                trySize -= 1;
                // 保证大于最小字体
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                // 再次用新字体写字
                textPaint.setTextSize(trySize);
            }
            setTextSize(trySize);
        }
    }


//    /**
//     * 重写setText
//     * 每次setText的时候
//     *
//     * @param text
//     * @param type
//     */
//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        if(text!=null) {
//            String textString = text.toString();
//            float trySize = maxTextSize;
//            if (this.textPaint == null) {
//                this.textPaint = new Paint();
//                this.textPaint.set(this.getPaint());
//            }
//            this.textPaint.setTextSize(trySize);
//            // 计算设置内容前 内容占据的宽度
//            int textWidth = (int) this.textPaint.measureText(textString);
//            // 拿到宽度和内容，进行调整
//            this.fitText(textString, textWidth);
//        }
//        super.setText(text, type);
//    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }
}