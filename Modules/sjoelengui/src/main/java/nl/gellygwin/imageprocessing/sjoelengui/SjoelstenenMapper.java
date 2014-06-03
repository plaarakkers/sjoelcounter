package nl.gellygwin.imageprocessing.sjoelengui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelbak;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelsteen;
import nl.gellygwin.imageprocessing.sjoelengui.models.RoundResult;

/**
 *
 * SjoelstenenMapper
 */
public class SjoelstenenMapper {

    public Map<Integer, Integer> mapSjoelstenenToPoorten(Sjoelbak sjoelbak, List<Sjoelsteen> sjoelstenen, RoundResult previousRoundResult) {
        Map<Integer, Integer> result = initResultMap(previousRoundResult);

        final double poortWidth = (sjoelbak.getPoortenAreaEnd().y - sjoelbak.getPoortenAreaStart().y) / 4;
        final int previousPoort1Count = previousRoundResult == null ? 0 : previousRoundResult.getPoort1Count();
        final int previousPoort2Count = previousRoundResult == null ? 0 : previousRoundResult.getPoort2Count();
        final int previousPoort3Count = previousRoundResult == null ? 0 : previousRoundResult.getPoort3Count();
        final int previousPoort4Count = previousRoundResult == null ? 0 : previousRoundResult.getPoort4Count();

        sjoelstenen.stream().filter((sjoelsteen) -> (sjoelsteen.getCenter().x + sjoelsteen.getRadius() < sjoelbak.getPoortenAreaStart().x)).forEach((Sjoelsteen sjoelsteen) -> {
            boolean poortMatched = processPoort(result, sjoelbak, poortWidth, sjoelsteen, 1, 1, previousPoort1Count);
            if (!poortMatched) {
                poortMatched = processPoort(result, sjoelbak, poortWidth, sjoelsteen, 4, 2, previousPoort4Count);
            }
            if (!poortMatched) {
                poortMatched = processPoort(result, sjoelbak, poortWidth, sjoelsteen, 3, 3, previousPoort3Count);
            }
            if (!poortMatched) {
                processPoort(result, sjoelbak, poortWidth, sjoelsteen, 2, 4, previousPoort2Count);
            }
        });

        return result;
    }

    private boolean processPoort(Map<Integer, Integer> result, Sjoelbak sjoelbak, double poortWidth, Sjoelsteen sjoelsteen, int poort, int poortLocation, int previousAmount) {
        if (sjoelbak.getPoortenAreaStart().y + poortWidth * poortLocation > sjoelsteen.getCenter().y - sjoelsteen.getRadius()) {
            //Ignore the sjoelstenen stack if there is a previous amount in the poort.
            if (previousAmount == 0 || sjoelsteen.getCenter().x > sjoelbak.getSjoelbakLeftEnd().x + sjoelsteen.getRadius()) {
                increaseResultForPoort(result, poort);
            }

            return true;
        }

        return false;
    }

    private void increaseResultForPoort(Map<Integer, Integer> result, int poort) {
        int amount = result.getOrDefault(poort, 0);
        amount++;

        result.put(poort, amount);
    }

    private Map<Integer, Integer> initResultMap(RoundResult previousRoundResult) {
        Map<Integer, Integer> result = new HashMap<>();
        if (previousRoundResult != null) {
            result.put(1, previousRoundResult.getPoort1Count());
            result.put(2, previousRoundResult.getPoort2Count());
            result.put(3, previousRoundResult.getPoort3Count());
            result.put(4, previousRoundResult.getPoort4Count());
        }

        return result;
    }
}
