package wolfgang.bergbauer.de.kletterguide.model;

import wolfgang.bergbauer.de.kletterguide.ClimbingAreaType;

/**
 * Created by Wolfgang on 08.08.2015.
 */
public class ClimbingHall extends ClimbingArea {

    private TicketInfo ticketInfo;
    private OpeningTimes openingTimes;

    public ClimbingHall(ClimbingAreaType climbingAreatype) {
        super(climbingAreatype);
    }

    public TicketInfo getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(TicketInfo ticketInfo) {
        this.ticketInfo = ticketInfo;
    }
}
