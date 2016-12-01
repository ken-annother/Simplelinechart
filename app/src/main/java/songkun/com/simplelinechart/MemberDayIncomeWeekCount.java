package songkun.com.simplelinechart;

import java.util.List;

/**
 * @描述: 用户七天内收入统计
 * @项目名: ugou
 * @包名: com.ugou88.ugou.model
 * @类名: ServiceFeedbackActivity
 * @作者: soongkun
 * @创建时间: 2016/4/27 10:32
 */
public class MemberDayIncomeWeekCount {

    public DataBean data;
    public String errcode;

    public static class DataBean {
        public double dayIncomeMax;
        public List<CountsBean> counts;

        public static class CountsBean {
            public double amount;
            public String date;
        }
    }
}
