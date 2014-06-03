package nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain;

import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

/**
 *
 * Sjoelbak
 *
 * Assumes poortenArea is to the left of the sjoelbak and the sjoelbak is horizontal.
 */
public class Sjoelbak {

    private final Point poortenAreaStart;

    private final Point poortenAreaEnd;

    private final Point sjoelbakLeftStart;

    private final Point sjoelbakLeftEnd;

    private final double POORTENAREA_HEIGHT_FACTOR = 5.15;

    private final double POORTENAREA_LENGTH_FACTOR = 5;

    public Sjoelbak(RotatedRect rectangle) {
        poortenAreaStart = new Point(rectangle.center.x + (rectangle.size.width / 2), rectangle.center.y - (rectangle.size.height / 2));
        poortenAreaEnd = new Point(poortenAreaStart.x, poortenAreaStart.y + (rectangle.size.height * POORTENAREA_HEIGHT_FACTOR));

        double poortenAreaLength = (rectangle.size.height * POORTENAREA_LENGTH_FACTOR);
        sjoelbakLeftStart = new Point(poortenAreaStart.x - poortenAreaLength, poortenAreaStart.y);
        sjoelbakLeftEnd = new Point(sjoelbakLeftStart.x, poortenAreaEnd.y);
    }

    /**
     * @return the poortenAreaStart
     */
    public Point getPoortenAreaStart() {
        return poortenAreaStart;
    }

    /**
     * @return the poortenAreaEnd
     */
    public Point getPoortenAreaEnd() {
        return poortenAreaEnd;
    }

    /**
     * @return the sjoelbakLeftStart
     */
    public Point getSjoelbakLeftStart() {
        return sjoelbakLeftStart;
    }

    /**
     * @return the sjoelbakLeftEnd
     */
    public Point getSjoelbakLeftEnd() {
        return sjoelbakLeftEnd;
    }
}
