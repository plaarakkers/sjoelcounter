package nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.opencv.core.Point;

/**
 *
 * Sjoelbak
 *
 * Assumes poortenArea is to the left of the sjoelbak and the sjoelbak is horizontal.
 */
public class Sjoelbak {

    private final Point poortenAreaStart;

    private final Point poortenAreaEnd;

    /**
     *
     * @param poortenAreaStart Top point for the poorten area.
     * @param poortenAreaEnd Bottom point for the poorten area.
     */
    public Sjoelbak(Point poortenAreaStart, Point poortenAreaEnd) {
        this.poortenAreaStart = poortenAreaStart;
        this.poortenAreaEnd = poortenAreaEnd;
    }

    public Map<Integer, Long> getAmountInLanes(List<Sjoelsteen> sjoelstenen) {
        double portLaneWidth = (poortenAreaEnd.y - poortenAreaStart.y) / 4;

        return sjoelstenen.parallelStream().filter(sjoelsteen -> {
            return sjoelsteen.getCenter().x + sjoelsteen.getRadius() < poortenAreaStart.x;
        }).collect(Collectors.groupingBy((Sjoelsteen sjoelsteen) -> {
            //Check poort 1.
            if (poortenAreaStart.y + portLaneWidth > sjoelsteen.getCenter().y - sjoelsteen.getRadius()) {
                return Integer.valueOf(1);
            }
            //Check poort 4.
            if (poortenAreaStart.y + portLaneWidth * 2 > sjoelsteen.getCenter().y - sjoelsteen.getRadius()) {
                return Integer.valueOf(4);
            }
            //Check poort 3.
            if (poortenAreaStart.y + portLaneWidth * 3 > sjoelsteen.getCenter().y - sjoelsteen.getRadius()) {
                return Integer.valueOf(3);
            }

            return Integer.valueOf(2);
        }, Collectors.counting()));
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
}
