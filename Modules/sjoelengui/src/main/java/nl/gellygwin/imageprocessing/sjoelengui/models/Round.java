package nl.gellygwin.imageprocessing.sjoelengui.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * Round
 */
public class Round {

    private int round;
    
    private final StringProperty roundProperty;

    public Round() {
        this.roundProperty= new SimpleStringProperty();
        setRonde(0);
    }
    
    private void setRonde(int value) {
        round = value;     
        roundProperty.set(Integer.toString(value));
    }
    
    public void increase() {
        int current = getCurrent();
        setRonde(++current);
    }
    
    public void reset() {
        setRonde(0);
    }

    public int getCurrent() {
        return round;
    }

    public StringProperty getProperty() {
        return roundProperty;
    }
    
    
}
