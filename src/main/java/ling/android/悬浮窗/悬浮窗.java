package ling.android.悬浮窗;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.*;
import ling.android.工具.ScaleUtils;
import ling.android.操作.时间操作;

public class 悬浮窗 {
    private final static String TAG = "FloatWindow";
    private Context mContext;   //声明一个上下文对象
    private WindowManager wm;   //声明一个窗口管理器对象
    private WindowManager.LayoutParams wmParams;
    public View mContentView; //声明一个内容视图对象
    private float mScreenX, mScreenY;    //触摸点在屏幕上的横纵坐标
    private float mLastX, mLastY;    //上次触摸点的横纵坐标
    private float mDownX, mDownY;    //按下点的横纵坐标
    private boolean isShowing = false;  //是否正在显示
    private boolean isHide = false; //是否正在隐藏
    private boolean isUpdateViewPosition = true;//是否允许移动悬浮窗
    private FloatClickListener mListener;   //声明一个悬浮窗的点击监听对象
    private FloatLongClickListener mLongListener;   //长按监听
    private int width = WindowManager.LayoutParams.WRAP_CONTENT;    //宽度
    private int height = WindowManager.LayoutParams.WRAP_CONTENT;   //高度
    private long onTime = 0;    //点按时间
    private boolean longOnClick = false;
    @SuppressLint("RtlHardcoded")
    private int gravity = Gravity.LEFT | Gravity.TOP;
    public int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : Build.VERSION.SDK_INT == Build.VERSION_CODES.M ? WindowManager.LayoutParams.TYPE_TOAST : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

    public 悬浮窗(Context context) {

        //从系统服务中获取窗口管理器，后续将通过该管理器添加悬浮窗
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        mContext = context;
    }

    public 悬浮窗(Context context, int width, int height) {

        this.width = ScaleUtils.dip2px(context, width);
        this.height = ScaleUtils.dip2px(context, height);

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        mContext = context;
    }


    /**
     * 设置悬浮窗的内容布局
     */
    public 悬浮窗 setLayout(View layout) {
        //从指定资源编号的布局文件中获取内容视图对象
        mContentView = layout;
        //接管悬浮窗的触摸事件，使之即可随手势拖动，又可处理点击动作
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            //在发生触摸事件时触发
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScreenX = event.getRawX();
                mScreenY = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   //手指按下
                        mDownX = mScreenX;
                        mDownY = mScreenY;
                        longOnClick = true;
                        break;
                    case MotionEvent.ACTION_MOVE:   //手指移动
                        updateViewPosition();   //更新视图的位置
                        if (Math.abs(mScreenX - mDownX) > 3 || Math.abs(mScreenY - mDownY) > 3)
                            longOnClick = false;
                        break;
                    case MotionEvent.ACTION_UP:     //手指松开
                        updateViewPosition();   //更新视图的位置
                        //响应悬浮窗点击事件
                        if (longOnClick && Math.abs(mScreenX - mDownX) < 3 && Math.abs(mScreenY - mDownY) < 3) {
                            if (mListener != null) {
                                mListener.onFloatClick(v);
                            }
                        }
                        break;
                }
                mLastX = mScreenX;
                mLastY = mScreenY;
                return false;
            }
        });
        mContentView.setOnLongClickListener((v) -> {
            if (longOnClick && mLongListener != null) {
                longOnClick = false;
                return mLongListener.onFloatLongClick(v);
            }
            return false;
        });
        return this;
    }

    /**
     * 更新悬浮窗的视图位置
     */
    private 悬浮窗 updateViewPosition() {
        if (this.isUpdateViewPosition) {
            //此处不能直接转换为整型，因为小数部分会被截掉，重复多次后就会照成偏移越来越大
            wmParams.x = Math.round(wmParams.x + mScreenX - mLastX);
            wmParams.y = Math.round(wmParams.y + mScreenY - mLastY);
            //通过窗口管理器更新内容视图的布局参数
            wm.updateViewLayout(mContentView, wmParams);
        }
        return this;
    }

    /**
     * 设置悬浮窗拖动策略
     */
    public 悬浮窗 setUpdate(boolean s) {
        this.isUpdateViewPosition = s;
        return this;
    }

    public 悬浮窗 setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public 悬浮窗 setType(int type) {
        this.type = type;
        return this;
    }

    /**
     * 显示悬浮窗
     */
    public 悬浮窗 show() {
        if (mContentView != null) {
            if (!this.isShowing) {
                //设置为TYPE_SYSTEM_ALERT类型，才能悬浮在其他页面之上
                wmParams.type = this.type;
                wmParams.format = PixelFormat.RGBA_8888;
                wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                wmParams.alpha = 1.0f;      //1.0为完全不透明，0.0为完全透明
                //对齐方式为靠左且靠上，因此悬浮窗的初始位置在屏幕的坐上角
                wmParams.gravity = this.gravity;
                wmParams.x = 0;
                wmParams.y = 0;
                //设置悬浮窗的宽度和高度
                wmParams.width = this.width;
                wmParams.height = this.height;
                //添加自定义的窗口布局，然后屏幕上就能看到悬浮窗了
                wm.addView(mContentView, wmParams);
                isShowing = true;
            } else if (this.isHide) {
                mContentView.setVisibility(View.VISIBLE);
                wmParams.width = this.width;
                wmParams.height = this.height;
                this.isHide = false;
            }
        }
        return this;
    }

    /**
     * 隐藏悬浮窗
     */
    public 悬浮窗 hide() {
        if (isShowing) {
            mContentView.setVisibility(View.GONE);
            this.isHide = true;
        }
        return this;
    }

    /**
     * 关闭悬浮窗
     */
    public 悬浮窗 close() {
        //移除自定义的窗口布局
        if (isShowing) {
            wm.removeView(mContentView);
            isShowing = false;
        }
        return this;
    }

    /**
     * 判断悬浮窗是否打开
     */
    public boolean isShwo() {
        return isShowing;
    }

    /**
     * 判断悬浮窗是否隐藏
     */
    public boolean isHide() {
        return this.isHide;
    }

    /**
     * 设置悬浮窗的点击监听器
     */
    public 悬浮窗 setOnFloatListener(FloatClickListener listener) {
        mListener = listener;
        return this;
    }

    public 悬浮窗 setOnFloatLongListener(FloatLongClickListener longListener) {
        mLongListener = longListener;
        return this;
    }

    /**
     * 定义一个悬浮窗的点击监听器接口，用于触发点击行为
     */
    public interface FloatClickListener {
        void onFloatClick(View v);
    }

    public interface FloatLongClickListener {
        boolean onFloatLongClick(View v);
    }
}
