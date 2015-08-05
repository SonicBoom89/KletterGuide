package wolfgang.bergbauer.de.kletterguide.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wolfgang on 08.06.2015.
 */
public class ClimbingRoute extends ClimbingBase {

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
}
