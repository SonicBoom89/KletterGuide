package wolfgang.bergbauer.de.kletterguide.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wolfgang on 08.06.2015.
 */
public class ClimbingRoute extends ClimbingBase implements Parcelable {

    private String UIAARank;
    private boolean followClimbAccomplished;
    private boolean leadClimbAccomplished;
    private boolean routeAccomplished;

    public ClimbingRoute(){

    }

    public String getUIAARank() {
        return UIAARank;
    }

    public void setUIAARank(String UIAARank) {
        this.UIAARank = UIAARank;
    }

    public boolean isFollowClimbAccomplished() {
        return followClimbAccomplished;
    }

    public void setFollowClimbAccomplished(boolean followClimbAccomplished) {
        this.followClimbAccomplished = followClimbAccomplished;
    }

    public boolean isLeadClimbAccomplished() {
        return leadClimbAccomplished;
    }

    public void setLeadClimbAccomplished(boolean leadClimbAccomplished) {
        this.leadClimbAccomplished = leadClimbAccomplished;
    }

    public boolean isRouteAccomplished() {
        return routeAccomplished;
    }

    public void setRouteAccomplished(boolean routeAccomplished) {
        this.routeAccomplished = routeAccomplished;
    }

    protected ClimbingRoute(Parcel in) {
        UIAARank = in.readString();
        followClimbAccomplished = in.readByte() != 0x00;
        leadClimbAccomplished = in.readByte() != 0x00;
        routeAccomplished = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(UIAARank);
        dest.writeByte((byte) (followClimbAccomplished ? 0x01 : 0x00));
        dest.writeByte((byte) (leadClimbAccomplished ? 0x01 : 0x00));
        dest.writeByte((byte) (routeAccomplished ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClimbingRoute> CREATOR = new Parcelable.Creator<ClimbingRoute>() {
        @Override
        public ClimbingRoute createFromParcel(Parcel in) {
            return new ClimbingRoute(in);
        }

        @Override
        public ClimbingRoute[] newArray(int size) {
            return new ClimbingRoute[size];
        }
    };
}