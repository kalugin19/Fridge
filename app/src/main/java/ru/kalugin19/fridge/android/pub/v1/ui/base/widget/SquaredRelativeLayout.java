package ru.kalugin19.fridge.android.pub.v1.ui.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Кастомный контейнер для квадратных областей
 *
 * @author Natochij Alexander
 */


public class SquaredRelativeLayout extends RelativeLayout {
    public SquaredRelativeLayout(Context context) {
        super(context);
    }

    public SquaredRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquaredRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //noinspection SuspiciousNameCombination
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
