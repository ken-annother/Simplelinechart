package songkun.com.simplelinechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ugoutech.linechart.holder.PointValue;
import com.ugoutech.linechart.view.LineChartView;
import com.ugoutech.linechart.view.MarkView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChartView mChart;
    private int dayNum = 7;
    private MemberDayIncomeWeekCount mMemberDayIncomeWeekCountBean;
    private double mCountsBeanListDataMin;
    private double mCountsBeanListDataMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (LineChartView) findViewById(R.id.chart);

        genData();
        handleChartTrend();
    }

    private void genData() {
        mMemberDayIncomeWeekCountBean = new MemberDayIncomeWeekCount();
        mMemberDayIncomeWeekCountBean.errcode = "200";
        MemberDayIncomeWeekCount.DataBean data = new MemberDayIncomeWeekCount.DataBean();
        mMemberDayIncomeWeekCountBean.data = data;
        data.counts = new ArrayList<>();


        for (int i = 0; i < dayNum; i++) {
            MemberDayIncomeWeekCount.DataBean.CountsBean bean = new MemberDayIncomeWeekCount.DataBean.CountsBean();
            bean.amount = (int)(Math.random() * 100) % 100;
//            bean.amount = 0;
            Log.d("amount", (Math.random() * 100) % 300 + "");
            bean.date = "11-" + (i + 1);
            data.counts.add(bean);
        }

    }


    /**
     * 处理七天的走势
     */
    private void handleChartTrend() {

        List<MemberDayIncomeWeekCount.DataBean.CountsBean> countsBeanList = mMemberDayIncomeWeekCountBean.data.counts;
        int dayNum = countsBeanList.size();


        mCountsBeanListDataMin = countsBeanList.get(0).amount;
        mCountsBeanListDataMax = countsBeanList.get(0).amount;

        for (int i = 1; i < dayNum; i++) {
            if (countsBeanList.get(i).amount < mCountsBeanListDataMin) {
                mCountsBeanListDataMin = countsBeanList.get(i).amount;
            }

            if (countsBeanList.get(i).amount > mCountsBeanListDataMax) {
                mCountsBeanListDataMax = countsBeanList.get(i).amount;
            }
        }


        //数据点集合
        List<PointValue> values = new ArrayList<>();


        for (int i = 0; i < dayNum; i++) {
            PointValue pointValue;

            if (i == dayNum - 1) { //6
                pointValue = new PointValue((float) countsBeanList.get(i).amount, "昨天");
            } else {     //0 1 2 3 4 5
                pointValue = new PointValue((float) countsBeanList.get(i).amount, countsBeanList.get
                        (i).date);
            }

            values.add(pointValue);
        }

        MarkView markView = new MarkView(this);
        mChart.setMarkView(markView);

        mChart.setData(values);

    }

}
