package wolfgang.bergbauer.de.kletterguide.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Wolfgang on 05.08.2015.
 */
public class MyValueFormatter implements IValueFormatter {

    public MyValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return (int) value + "";
    }
}