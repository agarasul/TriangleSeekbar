package az.rasul.triangleseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import com.rasul.triangleseekbar.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TriangleSeekbar extends View implements View.OnTouchListener {

    enum Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
    }

    interface ProgressListener {
        void onProgressChange(float progress);
    }


    ProgressListener mProgressListener;
    private DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);

    private DecimalFormat df = new DecimalFormat("#.##", decimalFormatSymbols);


    private int mHeight = 0;
    private int mWidth = 0;

    private int mLoadedHeight = 0;

    private int mLoadedWidth = 0;

    private float mProgressX = 0f;
    private float mProgressY = 0f;

    private Position mProgressPosition = Position.CENTER;
    private Paint mTextPaint = new Paint();
    private Paint mSeekbarPaint = new Paint();
    private Paint mSeekbarLoadingPaint = new Paint();

    private Path mSeekbarPath = new Path();
    private Path mSeekbarLoadingPath = new Path();

    private int mTextColor;
    private int mSeekbarColor;
    private int mSeekbarLoadingColor;

    private boolean mIsProgressVisible = false;

    private String mFontName;

    private float mFontSize = 96f;

    private float percentage = 0.0f;


    public TriangleSeekbar(Context context) {
        super(context);
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        setOnTouchListener(this);
    }

    public TriangleSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        setOnTouchListener(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TriangleSeekbar);

        mSeekbarColor =
                typedArray.getColor(R.styleable.TriangleSeekbar_seekbarColor, getResources().getColor(R.color.seekbarPrimary));
        mSeekbarLoadingColor =
                typedArray.getColor(
                        R.styleable.TriangleSeekbar_seekbarLoadingColor,
                        getResources().getColor(R.color.seekbarPrimaryDark)
                );

        mTextColor =
                typedArray.getColor(
                        R.styleable.TriangleSeekbar_textColor,
                        Color.BLACK
                );


        mIsProgressVisible = typedArray.getBoolean(R.styleable.TriangleSeekbar_showProgress, false);

        mProgressPosition = Position.values()[(typedArray.getInt(R.styleable.TriangleSeekbar_progressTextPosition, 4))];
        mFontSize = typedArray.getDimension(R.styleable.TriangleSeekbar_textFontSize, 96f);
        mFontName = typedArray.getString(R.styleable.TriangleSeekbar_textFontName);

        percentage = typedArray.getFloat(R.styleable.TriangleSeekbar_progress, 0f);

        mSeekbarPaint.setColor(mSeekbarColor);
        mSeekbarLoadingPaint.setColor(mSeekbarLoadingColor);
        mTextPaint.setColor(mTextColor);

        mTextPaint.setTextSize(mFontSize);
        if (mFontName != null) {
            try {
                mTextPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "/fonts/$mFontName"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;

        mSeekbarPath.moveTo(mWidth, 0);
        mSeekbarPath.lineTo(mWidth, mHeight);
        mSeekbarPath.lineTo(0, mHeight);

        if (percentage > 0) {
            setProgress((int) percentage);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                if (event.getX() >= 0 && event.getX() <= mWidth) {
                    buildLoadingTriangle(event.getX());
                }
                break;
        }

        invalidate();
        return true;
    }


    private void buildLoadingTriangle(Float motionX) {
        mSeekbarLoadingPath.reset();

        double hypotenuse = Math.sqrt((mHeight * mHeight + mWidth * mWidth));
        double sinA = mHeight / hypotenuse;
        double cosA = Math.sqrt((1 - (sinA * sinA)));
        hypotenuse = motionX / cosA;
        mLoadedHeight = (int) Math.round(hypotenuse * sinA);
        mLoadedWidth = Math.round(motionX);
        mSeekbarLoadingPath.moveTo(0f, mHeight);

        mSeekbarLoadingPath.lineTo((float) mLoadedWidth, mHeight);
        mSeekbarLoadingPath.lineTo((float) mLoadedWidth, ((float) (mHeight - mLoadedHeight)));

        percentage = getPercentage();

        if (mProgressListener != null) {
            String aa = df.format(percentage);

            mProgressListener.onProgressChange(Float.parseFloat(aa));
        }

        setProgressPosition(mProgressPosition);
    }


    private void setProgressPosition(Position position) {
        Rect bounds = new Rect();

        String text = "" + Math.round(percentage) + " % ";
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);

        switch (position) {
            // TOP LEFT
            case TOP_LEFT:
                mProgressX = bounds.height() * 0.25f;
                mProgressY = bounds.height() + bounds.height() * 0.25f;
                break;

            // TOP RIGHT
            case TOP_RIGHT:
                mProgressX = mWidth - (bounds.width() + bounds.height() * 0.25f);
                mProgressY = bounds.height() + bounds.height() * 0.25f;
                break;
            // BOTTOM LEFT
            case BOTTOM_LEFT:
                mProgressX = bounds.height() / 2f;
                mProgressY = mHeight - bounds.height() * 0.25f;
                break;
            // BOTTOM RIGHT
            case BOTTOM_RIGHT:
                mProgressX = mWidth - (bounds.width() + bounds.height() * 0.25f);
                mProgressY = mHeight - bounds.height() * 0.25f;
                break;
            // CENTER
            case CENTER:
                mProgressX = mWidth / 2f;
                mProgressY = mHeight / 1.25f;
                break;
        }
        invalidate();
    }


    public void setTextColor(int color) {
        this.mTextColor = color;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public void setSeekBarColor(int color) {
        this.mSeekbarColor = color;
        mSeekbarPaint.setColor(mSeekbarColor);
        invalidate();
    }

    public void setSeekbarLoadingColor(int color) {
        this.mSeekbarLoadingColor = color;
        mSeekbarLoadingPaint.setColor(mSeekbarLoadingColor);
        invalidate();
    }


    private float getPercentage() {
        double loadedArea = (mLoadedHeight * mLoadedWidth);
        int fullArea = (mHeight * mWidth);
        return (float) ((loadedArea / fullArea) * 100f);
    }


    public void setProgress(float progress) {
        if (progress >= 0.0 && progress <= 1.0) {
            double newWidth = mWidth * Math.sqrt(progress);
            buildLoadingTriangle((float) Math.ceil(newWidth));
            invalidate();
        } else {
            throw new IllegalArgumentException("Progress must be between 0.0 and 1.0");
        }
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mSeekbarPath, mSeekbarPaint);
        canvas.drawPath(mSeekbarLoadingPath, mSeekbarLoadingPaint);
        if (mIsProgressVisible) {
            canvas.drawText((int) percentage + " % ", mProgressX, mProgressY, mTextPaint);
        }

    }


    public void setmProgressListener(ProgressListener mProgressListener) {
        this.mProgressListener = mProgressListener;
    }
}
