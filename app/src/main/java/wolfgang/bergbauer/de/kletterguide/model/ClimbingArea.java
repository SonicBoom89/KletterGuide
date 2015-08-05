package wolfgang.bergbauer.de.kletterguide.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;

/**
 * Created by Wolfgang on 05.08.2015.
 */
public class ClimbingArea extends ClimbingBase implements Parcelable {

    private ClimbingAreaType climbingAreatype;

    private List<ClimbingRoute> routes;
    private String description;

    public ClimbingArea(ClimbingAreaType climbingAreatype) {
        super();
        this.climbingAreatype = climbingAreatype;
    }

    public List<ClimbingRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<ClimbingRoute> routes) {
        this.routes = routes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected ClimbingArea(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setRanking(in.readFloat());
        setDrawableUrl(in.readString());
        setLongitude(in.readFloat());
        setLatitude(in.readFloat());
        climbingAreatype = (ClimbingAreaType) in.readValue(ClimbingAreaType.class.getClassLoader());
        if (in.readByte() == 0x01) {
            routes = new ArrayList<>();
            in.readList(routes, ClimbingRoute.class.getClassLoader());
        } else {
            routes = null;
        }
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeFloat(getRanking());
        dest.writeString(getDrawableUrl());
        dest.writeFloat(getLongitude());
        dest.writeFloat(getLatitude());
        dest.writeValue(climbingAreatype);
        if (routes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(routes);
        }
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClimbingArea> CREATOR = new Parcelable.Creator<ClimbingArea>() {
        @Override
        public ClimbingArea createFromParcel(Parcel in) {
            return new ClimbingArea(in);
        }

        @Override
        public ClimbingArea[] newArray(int size) {
            return new ClimbingArea[size];
        }
    };
}