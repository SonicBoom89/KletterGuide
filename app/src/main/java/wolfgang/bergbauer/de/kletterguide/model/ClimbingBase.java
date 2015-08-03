package wolfgang.bergbauer.de.kletterguide.model;

/**
 * Created by Wolfgang on 08.06.2015.
 */
public class ClimbingBase {

    private int id;
    private String name;
    private float ranking;

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
}
