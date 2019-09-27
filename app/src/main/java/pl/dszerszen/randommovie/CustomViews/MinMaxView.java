package pl.dszerszen.randommovie.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Filter;
import android.widget.LinearLayout;
import com.jaygoo.widget.RangeSeekBar;
import java.util.Calendar;

import androidx.annotation.Nullable;
import pl.dszerszen.randommovie.Filter.FilterData;
import pl.dszerszen.randommovie.Filter.SingleFilter;
import pl.dszerszen.randommovie.R;

public class MinMaxView extends LinearLayout implements FilterExpandView.AttachedView {

    public static final String TAG = "RandomMovie_log";
    RangeSeekBar rangeSeekBar;

    FilterData.FilterType filterType;


    public MinMaxView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.minmax_layout,this);
        initComponents(context);
    }

    public void initComponents(Context context) {
        rangeSeekBar = findViewById(R.id.minmax_seekbar);
        rangeSeekBar.setIndicatorTextDecimalFormat("1");
    }

    public void setupSelectorType (FilterData.FilterType filterType) {
        this.filterType = filterType;
        switch (filterType) {
            case YEAR: {
                rangeSeekBar.setRange(1980,Calendar.getInstance().get(Calendar.YEAR),6f);
                rangeSeekBar.setProgress(1980,Calendar.getInstance().get(Calendar.YEAR));
                break;
            }
            case RUNTIME: {
                rangeSeekBar.setRange(60,240,20f);
                rangeSeekBar.setProgress(60,240);
                break;
            }
            case VOTE: {
                rangeSeekBar.setRange(0,10,2);
                rangeSeekBar.setProgress(0,10);
                break;
            }
        }
    }

    @Override
    public String getCurrentValue() {
        StringBuilder builder = new StringBuilder();
        int leftValue = Math.round(rangeSeekBar.getLeftSeekBar().getProgress());
        int rightValue = Math.round(rangeSeekBar.getRightSeekBar().getProgress());
        builder.append(leftValue).append("-").append(rightValue);
        return builder.toString();
    }

    @Override
    public SingleFilter getFilter() {
        SingleFilter singleFilter = new SingleFilter();
        if (filterType!=null) {
            singleFilter.type = filterType;
            singleFilter.min = Math.round(rangeSeekBar.getLeftSeekBar().getProgress());
            singleFilter.max = Math.round(rangeSeekBar.getRightSeekBar().getProgress());
        }
        return singleFilter;

    }

}
