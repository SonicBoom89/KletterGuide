package wolfgang.bergbauer.de.kletterguide.model;

import java.util.List;

/**
 * Created by Wolfgang on 08.06.2015.
 */
public class ClimbingBase {

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

    @Override
    public String toString() {
        return String.format("%1s %2s %3s %4s %5s", getId(), getName(), getLatitude(), getLongitude(), getRanking());
    }
}
