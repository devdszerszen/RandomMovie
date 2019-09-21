package pl.dszerszen.randommovie;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.jaygoo.widget.RangeSeekBar;
import java.util.Calendar;

import androidx.annotation.Nullable;

public class MinMaxView extends LinearLayout implements FilterExpandView.AttachedView {

    public static final String TAG = "RandomMovie_log";
    RangeSeekBar rangeSeekBar;


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
        switch (filterType) {
            case YEAR: {
                rangeSeekBar.setRange(1980,Calendar.getInstance().get(Calendar.YEAR));
                rangeSeekBar.setProgress(1980,Calendar.getInstance().get(Calendar.YEAR));
                break;
            }
            case RUNTIME: {
                rangeSeekBar.setRange(60,240);
                rangeSeekBar.setProgress(60,240);
                break;
            }
            case VOTE: {
                rangeSeekBar.setRange(0,10);
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

    public int getMinValue() {
        return Math.round(rangeSeekBar.getLeftSeekBar().getProgress());
    }

    public int getMaxValue() {
        return Math.round(rangeSeekBar.getRightSeekBar().getProgress());
    }
}
