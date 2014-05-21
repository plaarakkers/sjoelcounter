package nl.gellygwin.imageprocessing.opencvprocessor.input;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

/**
 *
 * ImageInputHandler
 */
public class ImageInputHandler implements InputHandler {
    private final String imageLocation;
    
    private boolean returned;

    public ImageInputHandler( String imageLocation) {
        this.imageLocation = imageLocation;
        returned = false;
    }

    
    @Override
    public Mat getImage() {
        returned = true;
        return Highgui.imread(imageLocation);
    }

    @Override
    public boolean hasNext() {
        return !returned;
    }

    @Override
    public void release() {
        //Nothing to release
    }

}
