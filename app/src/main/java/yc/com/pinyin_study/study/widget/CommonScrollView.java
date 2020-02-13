package yc.com.pinyin_study.study.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.core.widget.NestedScrollView;

/**
 * Created by wanglin  on 2018/11/16 16:29.
 */
public class CommonScrollView extends NestedScrollView {

    private int scaledTouchSlop;
    private int y = 0;
    private int x = 0;

    public CommonScrollView(Context context) {
        super(context);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommonScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    protected void onScrollChanged(int l, int scrollY, int oldl, int oldScrollY) {
        super.onScrollChanged(l, scrollY, oldl, oldScrollY);
        if (onScrollListener != null) {
            onScrollListener.onScroll(l, scrollY, oldl, oldScrollY);
        }

    }


    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int curY = (int) event.getY();
                int curX = (int) event.getX();
                if (Math.abs(curY - this.y) > scaledTouchSlop) {
                    return true;
                }

        }

        return super.onInterceptHoverEvent(event);
    }


    private onScrollListener onScrollListener;

    public void setOnScrollListener(CommonScrollView.onScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface onScrollListener {
        void onScroll(int l, int scrollY, int oldl, int oldScrollY);
    }
}
