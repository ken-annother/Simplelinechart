package songkun.com.simplelinechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soongkun
 * @version $Rev$
 * @time 2016/5/10 21:53
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SimpleLineChart extends View {

    /**
     * 前面的点
     */
    private  PixelsPoint prePonit;

    private Paint mPaintText;   //画文字的

    private Paint mPaintAixsLine;       //画坐标轴的

    private Paint mPaintLine;    //坐标轴的点

    private float aixsFontSize = 40;   //坐标轴下方的文字大小

    private float padding; //坐标轴下方的空白高度

    //数据中的最大值
    private  float maxDataPointValue;

    //数据中的最小值
    private  float minDataPointValue;

    //Y轴的起始坐标
    private float mStartYPixel;

    /**
     * 要显示的数据
     */
    private List<Point> mData;

    //画布的高度
    private int mHeight;
    //画布的宽度
    private int mWidth;

    //每一个像素的金额
    private float mValuePerPixels;
    // 分成指定的段数,得到每一段的长度
    private float mPerWidth;

    //Y轴的起始坐标对应的值
    private float mStartYValue;

    /**
     * 线上的大点 空白的
     */
    private Paint mPaintLineWhitePoint;

    /**
     * 大点
     */
    private Paint mPaintLinePoint;

    //真实点的集合
    private List<PixelsPoint> mPixelsPoints;

    //画布
    private Canvas mCanvas;

    public SimpleLineChart(Context context) {
        this(context,null);
    }

    public SimpleLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        aixsFontSize = 50;
        padding = 1.5f * aixsFontSize;

        mPixelsPoints = new ArrayList<>();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaintText = new Paint();
        mPaintText.setTextSize(aixsFontSize);
        mPaintText.setColor(0xff333333);

        mPaintAixsLine = new Paint();
        mPaintAixsLine.setStrokeWidth(5);
        mPaintAixsLine.setAntiAlias(true);
        mPaintAixsLine.setStrokeJoin(Paint.Join.BEVEL);
        mPaintAixsLine.setColor(0x10000000);

        mPaintLine = new Paint();
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setStrokeCap(Paint.Cap.ROUND);
        mPaintLine.setStrokeWidth(10);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setColor(0xffff0000);

        mPaintLineWhitePoint = new Paint();
        mPaintLineWhitePoint.setStyle(Paint.Style.FILL);
        mPaintLineWhitePoint.setStrokeCap(Paint.Cap.ROUND);
        mPaintLineWhitePoint.setStrokeWidth(40);
        mPaintLineWhitePoint.setAntiAlias(true);
        mPaintLineWhitePoint.setColor(0xffffffff);

        mPaintLinePoint = new Paint();
        mPaintLinePoint.setStyle(Paint.Style.FILL);
        mPaintLinePoint.setStrokeCap(Paint.Cap.ROUND);
        mPaintLinePoint.setStrokeWidth(25);
        mPaintLinePoint.setAntiAlias(true);

        mPaintLinePoint.setColor(0xffff0000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthSpecMode ==  MeasureSpec.AT_MOST){      //包裹内容的话，就可200dp
            widthSpecSize = dip2px(getContext(),400);
        }

        if(heightSpecMode ==  MeasureSpec.AT_MOST){
            heightSpecSize = dip2px(getContext(),200);
        }

        setMeasuredDimension(widthSpecSize ,heightSpecSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.mCanvas = canvas;
        mHeight = canvas.getHeight();
        mWidth = canvas.getWidth();

        //画X轴
        canvas.drawLine(0, mHeight -padding, mWidth, mHeight -padding,mPaintAixsLine);


        if(mData == null || mData.size() == 0){      //如果数据集为空
            Log.i("SimpleLineChart","图表未绑定数据,或者数据集为空");
            return;
        }

        //得到集合中最大的值和最小的值，绘制时候的Y轴的起点和终点
        preOperatorData();

        //分成指定的段数,得到每一段的长度
        mPerWidth = (mWidth - padding * 2)/ (mData.size() - 1);

        for (int i = 0; i < mData.size(); i++) {

            //文字的宽度，画文字
            float v = mPaintText.measureText(mData.get(i).description);
            canvas.drawText(mData.get(i).description,padding + mPerWidth * i - v / 2.0f, mHeight -padding + aixsFontSize,mPaintText);

            drawLineWithPoint(i);
        }

        //专门来画点
        for (int i = 0; i < mData.size(); i++) {
            drawWithPoint(i);
        }

    }

    private void drawWithPoint(int position) {

        float value = mData.get(position).value;
        //该点Y轴的坐标
        float v = mStartYPixel -  (value - mStartYValue) / mValuePerPixels;

        // 画出起空白的大点来
        mCanvas.drawPoint(padding + mPerWidth * position,v,mPaintLineWhitePoint);

        // 画出有颜色的的小点来
        mCanvas.drawPoint(padding + mPerWidth * position,v,mPaintLinePoint);
    }


    private void drawLineWithPoint( int position){
        float value = mData.get(position).value;
        //该点Y轴的坐标
        float v = mStartYPixel -  (value - mStartYValue) / mValuePerPixels;

        if(prePonit != null){
            mCanvas.drawLine(prePonit.x,prePonit.y,padding + mPerWidth * position,v,mPaintLine);
        }

        //要画出该点到X轴的垂线
        mCanvas.drawLine(padding + mPerWidth * position,v,padding + mPerWidth * position,mHeight -padding,mPaintAixsLine);

        prePonit = new PixelsPoint(padding + mPerWidth * position,v);

        mPixelsPoints.add(prePonit);
    }

    /**
     * 得到集合中最大的值和最小的值，绘制时候的Y轴的起点和终点
     */
    private void preOperatorData() {

        float max =mData.get(0).value;
        float min =mData.get(1).value;

        for(int i = 0; i < mData.size(); i++){
            if(mData.get(i).value > max){
                max =  mData.get(i).value;
            }

            if(mData.get(i).value < min){
                min = mData.get(i).value;
            }
        }

        maxDataPointValue = max;
        minDataPointValue = min;

        Log.d("SimpleLineChart","最大值是：" + max);
        Log.d("SimpleLineChart","最小值是：" + min);

        //正常的情况下的每一个像素的金额
        float valuePerPixels = maxDataPointValue / (mHeight - 2 * padding);

        //正常情况下起始的Y轴像素
        float startYPixel = mHeight - padding;       //默认是一个padding

        //最小值代表的像素
        float minValuesPixels = minDataPointValue / mValuePerPixels;

        //Y轴默认的起始值
        float startYValue =  0;

        if(minValuesPixels > (mHeight - 2 * padding) / 3){      //最小值得像素就占了像素的1/3,那么 开始点的像素为
            startYPixel = mHeight - 2 * padding;          //往上挪一个padding大小作为起始点

            //此时每一像素代表的金额发生了改变
            valuePerPixels = (maxDataPointValue - minDataPointValue) / (mHeight - 3 * padding);

            //将最小值作为起始值
            startYValue = min;
        }

        mStartYValue = startYValue;

        mStartYPixel = startYPixel;

        mValuePerPixels = valuePerPixels;

        Log.d("SimpleLineChart","Y轴起始值是" + mStartYValue);
        Log.d("SimpleLineChart","起始值的像素是" + mStartYPixel);
        Log.d("SimpleLineChart","每一像素代表的值是" + mValuePerPixels);

    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 绑定数据
     * @param data
     */
    public void setData(List<Point> data){
        this.mData = data;
        invalidate();       //重新绘制
    }



    public  class PixelsPoint{
         public float x;
         public float y;

        public PixelsPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    /**
     * 点
     */
    public static  class Point{

        public Point() {}

        public Point(String description, float value) {
            this.description = description;
            this.value = value;
        }

        /**
         * 描述
         */
        public String description;

        /**
         * 值
         */
        public float value;
    }

    private int mNearstPont;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:       //按下去的时候
                float x = event.getX();     //获得X坐标
                float y = event.getY();     //获得Y坐标

                mNearstPont = getTheNearestPoint(x,y);

                Log.d(this.getClass().getSimpleName(),"按下的时候，检测到的最下的点是：" + mNearstPont);

                if(mNearstPont != -1){     //如果右符合的最近点，则弹出该点信息

                    // 画出起空白的大点来
                    mCanvas.drawPoint(padding + mPixelsPoints.get(mNearstPont).x,mPixelsPoints.get(mNearstPont).y,mPaintLineWhitePoint);

                    // 画出有颜色的的小点来
                    mPaintLinePoint.setStrokeWidth(30);
                    mCanvas.drawPoint(padding + mPixelsPoints.get(mNearstPont).x,mPixelsPoints.get(mNearstPont).y,mPaintLinePoint);
//                    Toast.makeText(getContext(),mData.get(mNearstPont).value + "",Toast.LENGTH_SHORT).show();
                }

                break;
            case MotionEvent.ACTION_UP:         //抬起来的时候
            case MotionEvent.ACTION_CANCEL:         //抬起来的时候
                // 画出起空白的大点来
                mCanvas.drawPoint(padding + mPixelsPoints.get(mNearstPont).x,mPixelsPoints.get(mNearstPont).y,mPaintLineWhitePoint);

                // 画出有颜色的的小点来
                mPaintLinePoint.setStrokeWidth(20);
                mCanvas.drawPoint(padding + mPixelsPoints.get(mNearstPont).x,mPixelsPoints.get(mNearstPont).y,mPaintLinePoint);
                invalidate();
                break;
        }

        //不消费事件
        return  false;
    }

    /**
     * 获得最近点的点
     * @param x
     * @param y
     * @return  position 选中点在列表中的位置
     */
    private int getTheNearestPoint(float x, float y) {

        //敏锐度不能大于20px
        float delta = mPerWidth / 3 > 40 ? 40 : mPerWidth / 3;

        for(int i = 0 ; i < mPixelsPoints.size(); i ++ ){
            PixelsPoint point = mPixelsPoints.get(i);

            float pingfanghe = (x - point.x) * (x - point.x) - (y - point.y) * (y - point.y);

            double distance = Math.sqrt(pingfanghe);

            if(distance < delta){       //如果小于阈值，则判断点击中了
                return i;
            }
        }

        return -1;
    }
}
