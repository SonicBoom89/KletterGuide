package wolfgang.bergbauer.de.kletterguide.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by berg21 on 06.08.2015.
 */
public class ClimbingImage implements Parcelable {

    private int climbingId;
    private int routeId;
    private String description;
    private String imageUrl;

    public ClimbingImage() {
    }

    public int getClimbingId() {
        return climbingId;
    }

    public void setClimbingId(int climbingId) {
        this.climbingId = climbingId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    protected ClimbingImage(Parcel in) {
        climbingId = in.readInt();
        routeId = in.readInt();
        description = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(climbingId);
        dest.writeInt(routeId);
        dest.writeString(description);
        dest.writeString(imageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ClimbingImage> CREATOR = new Parcelable.Creator<ClimbingImage>() {
        @Override
        public ClimbingImage createFromParcel(Parcel in) {
            return new ClimbingImage(in);
        }

        @Override
        public ClimbingImage[] newArray(int size) {
            return new ClimbingImage[size];
        }
    };
}
