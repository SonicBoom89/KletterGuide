package wolfgang.bergbauer.de.kletterguide;

/**
 * Created by Wolfgang on 08.08.2015.
 */

import android.net.ParseException;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class is a JSON Parser to convert the busline schedules written in json to java
 */
public class JsonParser {

    /* Tag for Logging */
    private static final String TAG = JsonParser.class.getSimpleName();

    /**
     * The file name for the showcase busline
     */
    public static final String LINE_8_BUS_STOPS_DIRECTION_KONEIGSCHALDING_FILE_PATH= "linie8_koenigschalding.json";

    private static final String JSON_DATE_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * This method returns a list of {@link de.uni_passau.fim.eislab.pub.patrack.patrack.dto.BusStop} based on an json file
     * @param c the current context
     * @param path the path of the file which should be read from
     * @return the list of {@link de.uni_passau.fim.eislab.pub.patrack.patrack.dto.BusStop}

    public static List<BusStop> readBusStopsFromFile(Context c, String path) {
        List<BusStop> ret = new ArrayList<BusStop>();
        try {
            InputStream inputStream = c.getAssets().open(path);

            if ( inputStream != null ) {
                ret = readJsonStream(inputStream);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Parser ", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Parser activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private static List<BusStop> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            List<BusStop> busstops = new ArrayList<BusStop>();
            reader.beginArray();
            while (reader.hasNext()) {
                busstops.add(readBusStop(reader));
            }
            reader.endArray();
            return busstops;
        }
        finally {
            reader.close();
        }
    }

    private static BusStop readBusStop(JsonReader reader) throws IOException {
        String busstopName = null;
        List<Date> arrivals = null;
        List<Date> departures = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                busstopName = reader.nextString();
            } else if (name.equals("arrivals") && reader.peek() != JsonToken.NULL) {
                arrivals = readDateArray(reader);
            } else if (name.equals("departures") && reader.peek() != JsonToken.NULL) {
                departures = readDateArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        BusStop busStop = new BusStop();
        busStop.setName(busstopName);
        busStop.setArrivals(arrivals);
        busStop.setDepartures(departures);
        return busStop;
    }

    private static List readDateArray(JsonReader reader) throws IOException {
        List<Date> dates = new ArrayList<Date>();
        reader.beginArray();
        SimpleDateFormat sd = new SimpleDateFormat(JSON_DATE_PATTERN);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        while (reader.hasNext()) {
            try {
                dates.add(sd.parse(year + "-" + month + "-" + day + " " + reader.nextString()));
            } catch (ParseException e) {
                Log.e(TAG, "Error while parsing Date from Busline: " + e.getMessage());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        reader.endArray();
        return dates;
    }
     */
}