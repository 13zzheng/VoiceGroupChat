package stu.edu.cn.zing.voicechat.mywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/9/7.
 */
public class LineEditText extends EditText {
    private Paint mPaint=new Paint();
    private int mLineColor= Color.BLACK;
    public LineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LineEditText(Context context) {
        super(context);
    }

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLineColor(int color){
        this.mLineColor=color;
        invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(3);
        canvas.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1, mPaint);
    }
}
