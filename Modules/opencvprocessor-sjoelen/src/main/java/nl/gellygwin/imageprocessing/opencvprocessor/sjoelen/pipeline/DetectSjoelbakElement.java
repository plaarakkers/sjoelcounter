package nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline;

import java.util.ArrayList;
import java.util.List;
import nl.gellygwin.imageprocessing.opencvprocessor.Result;
import nl.gellygwin.imageprocessing.opencvprocessor.pipeline.PipelineElement;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelbak;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * DetectSjoelbakElement
 */
public class DetectSjoelbakElement implements PipelineElement {

    public static final String SJOELBAK = "sjoelbak";

    @Override
    public Result processImage(Result result, Result previousImageResult) {

        Mat workingImage = result.getOriginalImage().clone();

        Imgproc.cvtColor(workingImage, workingImage, Imgproc.COLOR_BGR2HSV);

        //Hue range is [0,179], Saturation range is [0,255] and Value range is [0,255]
        //If another programm is used to get the values, the values have to be corrected for this range.
        Core.inRange(workingImage, new Scalar(80, 128, 204), new Scalar(110, 255, 255), workingImage);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(workingImage.clone(), contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        RotatedRect rectangle = new RotatedRect(new Point(0, 0), new Size(0, 0), 1);

        for (MatOfPoint contour : contours) {
            MatOfPoint2f contourCheck = new MatOfPoint2f();
            contour.convertTo(contourCheck, CvType.CV_32FC2);
            RotatedRect contourRectangle = Imgproc.minAreaRect(contourCheck);
            if (contourRectangle.size.area() > rectangle.size.area()) {
                rectangle = contourRectangle;
            }
        }

        Point poortenAreaStart = new Point(rectangle.center.x + (rectangle.size.width / 2), rectangle.center.y - (rectangle.size.height / 2));
        Point poortenAreaEnd = new Point(poortenAreaStart.x, poortenAreaStart.y + (rectangle.size.height * 5.15));

        Sjoelbak sjoelbak = new Sjoelbak(poortenAreaStart, poortenAreaEnd);

        result.getData().put(SJOELBAK, sjoelbak);

        return result;
    }

}
