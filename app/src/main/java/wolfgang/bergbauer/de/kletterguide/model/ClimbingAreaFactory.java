package wolfgang.bergbauer.de.kletterguide.model;

import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;

import static wolfgang.bergbauer.de.kletterguide.ClimbingAreaType.*;

/**
 * Created by Wolfgang on 08.08.2015.
 */
public class ClimbingAreaFactory {

    public static ClimbingArea createClimbingArea(ClimbingAreaType type)
    {
        switch (type) {
            case INDOOR:
                return new ClimbingHall(type);
            case OUTDOOR:
                return new ClimbingArea(type);
            default:
                throw new IllegalArgumentException("ClimbingAreaType");
        }

    }

}
