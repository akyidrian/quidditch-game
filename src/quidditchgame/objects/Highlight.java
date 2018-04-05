package quidditchgame.objects;

import java.awt.geom.Point2D;
import quidditchgame.Team;

/**
 * Highlight defines the Highlight's characteristics.
 *
 * @author Sam Leichter
 */
public class Highlight extends DrawnObject {

    /**
     * Allocate the Highlight a sprite image location as well as starting
     * position
     *
     * @param team the team to set the color of the highlight
     * @param position the position of the highlight sprite on the field
     */
    public Highlight(Team team, Point2D.Double position) {
        super(team.getColor() + "Highlight", position);
    }

    /**
     * Allocate the Highlight a sprite image location as well as starting
     * position
     * @param position the position of the highlight sprite on the field
     */
    public Highlight(Point2D.Double position) {
        super("highlightBall", position);
    }
}
