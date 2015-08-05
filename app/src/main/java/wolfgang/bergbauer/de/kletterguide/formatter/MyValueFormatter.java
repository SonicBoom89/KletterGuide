package wolfgang.bergbauer.de.kletterguide.formatter;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by Wolfgang on 05.08.2015.
 */
public class MyValueFormatter implements ValueFormatter {

    public MyValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value) {
       return (int) value + "";
    }
}