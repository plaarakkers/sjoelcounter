package nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline;

import java.util.ArrayList;
import java.util.List;
import nl.gellygwin.imageprocessing.opencvprocessor.Result;
import nl.gellygwin.imageprocessing.opencvprocessor.pipeline.PipelineElement;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelsteen;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 *
 * DetectSjoelstenenElement
 */
public class DetectSjoelstenenElement implements PipelineElement {

    public static final String SJOELSTENEN = "sjoelstenen";

    @Override
    public Result processImage(Result result, Result previousImageResult) {
        Mat image = result.getOriginalImage();

        Mat workingImage = image.clone();

        Imgproc.cvtColor(workingImage, workingImage, Imgproc.COLOR_RGB2GRAY);
        Imgproc.medianBlur(workingImage, workingImage, 5);

        Mat imageCircles = new Mat();
        Imgproc.HoughCircles(workingImage, imageCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 30, 100, 30, 15, 50);

        List<Sjoelsteen> sjoelstenen = new ArrayList<>();

        for (int i = 0; i < imageCircles.cols(); i++) {
            sjoelstenen.add(new Sjoelsteen(imageCircles.get(0, i)));
        }

        result.getData().put(SJOELSTENEN, sjoelstenen);

        return result;
    }

}
