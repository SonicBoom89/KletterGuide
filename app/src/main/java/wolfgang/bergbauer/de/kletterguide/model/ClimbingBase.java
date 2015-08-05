package wolfgang.bergbauer.de.kletterguide.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wolfgang on 08.06.2015.
 */public class ClimbingBase implements Parcelable {

    private int id;
    private ClimbingAreaType climbingAreatype;
    private String name;
    private float ranking;
    private String drawableUrl;

    private float longitude;
    private float latitude;

    private List<Route> routes;

    public ClimbingBase(ClimbingAreaType climbingAreatype) {
        this.climbingAreatype = climbingAreatype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRanking() {
        return ranking;
    }

    public void setRanking(float ranking) {
        this.ranking = ranking;
    }

    public String getDrawableUrl() {
        return drawableUrl;
    }

    public void setDrawableUrl(String drawableUrl) {
        this.drawableUrl = drawableUrl;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    protected ClimbingBase(Parcel in) {
        id = in.readInt();
        climbingAreatype = (ClimbingAreaType) in.readValue(ClimbingAreaType.class.getClassLoader());
        name = in.readString();
        ranking = in.readFloat();
        drawableUrl = in.readString();
        longitude = in.readFloat();
        latitude = in.readFloat();
        if (in.readByte() == 0x01) {
            routes = new ArrayList<Route>();
            in.readList(routes, Route.class.getClassLoader());
        } else {
            routes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeValue(climbingAreatype);
        dest.writeString(name);
        dest.writeFloat(ranking);
        dest.writeString(drawableUrl);
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
        if (routes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(routes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClimbingBase> CREATOR = new Parcelable.Creator<ClimbingBase>() {
        @Override
        public ClimbingBase createFromParcel(Parcel in) {
            return new ClimbingBase(in);
        }

        @Override
        public ClimbingBase[] newArray(int size) {
            return new ClimbingBase[size];
        }
    };
}