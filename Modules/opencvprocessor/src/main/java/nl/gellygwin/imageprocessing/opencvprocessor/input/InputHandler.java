package nl.gellygwin.imageprocessing.opencvprocessor.input;

import org.opencv.core.Mat;

/**
 *
 * InputHandler
 */
public interface InputHandler {

    Mat getImage();

    boolean hasNext();

    void release();
}
