package songkun.com.simplelinechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       SimpleLineChart slc = (SimpleLineChart) findViewById(R.id.slc);

        ArrayList<SimpleLineChart.Point> points = new ArrayList<>();
        points.add(new SimpleLineChart.Point("5-12",3000));
        points.add(new SimpleLineChart.Point("5-13",4000));
        points.add(new SimpleLineChart.Point("5-14",1200));
        points.add(new SimpleLineChart.Point("5-15",800));
        points.add(new SimpleLineChart.Point("5-17",2500));
        points.add(new SimpleLineChart.Point("5-18",1800));
        points.add(new SimpleLineChart.Point("5-19",2400));

        slc.setData(points);

    }
}
