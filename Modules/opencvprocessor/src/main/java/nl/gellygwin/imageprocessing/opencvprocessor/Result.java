package nl.gellygwin.imageprocessing.opencvprocessor;

import java.util.HashMap;
import java.util.Map;
import org.opencv.core.Mat;

/**
 *
 * Result
 */
public class Result {
    
    private Mat originalImage;
    
    private Mat processedImage;
    
    private final Map<String, Object> data;

    /**
     *
     * @param image
     */
    public Result(Mat image) {
        originalImage = image;
        processedImage = image.clone();
        data = new HashMap<>();
    }
    
    
    /**
     * @return the originalImage
     */
    public Mat getOriginalImage() {
        return originalImage;
    }

    /**
     * @param originalImage the originalImage to set
     */
    public void setOriginalImage(Mat originalImage) {
        this.originalImage = originalImage;
    }

    /**
     * @return the processedImage
     */
    public Mat getProcessedImage() {
        return processedImage;
    }

    /**
     * @param processedImage the processedImage to set
     */
    public void setProcessedImage(Mat processedImage) {
        this.processedImage = processedImage;
    }

    /**
     * @return the data
     */
    public Map<String, Object> getData() {
        return data;
    }
}
