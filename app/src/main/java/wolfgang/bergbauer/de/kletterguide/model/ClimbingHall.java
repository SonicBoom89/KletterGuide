package wolfgang.bergbauer.de.kletterguide.model;

import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;

/**
 * Created by Wolfgang on 08.08.2015.
 */
public class ClimbingHall extends ClimbingArea {

    private String ticketInfo;
    private String openingTimes;

    public ClimbingHall() {

    }

    public ClimbingHall(ClimbingAreaType climbingAreatype) {
        super(climbingAreatype);
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }
}
