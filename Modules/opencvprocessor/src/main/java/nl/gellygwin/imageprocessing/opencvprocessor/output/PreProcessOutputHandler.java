package nl.gellygwin.imageprocessing.opencvprocessor.output;

import org.opencv.core.Mat;

/**
 *
 * PreProcessOutputHandler
 */
public interface PreProcessOutputHandler {

    void output(Mat image);
}
