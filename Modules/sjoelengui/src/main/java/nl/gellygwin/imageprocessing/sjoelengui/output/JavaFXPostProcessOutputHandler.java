package nl.gellygwin.imageprocessing.sjoelengui.output;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import nl.gellygwin.imageprocessing.opencvprocessor.Result;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PostProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelbak;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelsteen;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelbakElement;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelstenenElement;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 *
 * JavaFXPostProcessOutputHandler
 */
public class JavaFXPostProcessOutputHandler extends AbstractJavaFXOutputHandler implements PostProcessOutputHandler {

    private final TextArea output;

    public JavaFXPostProcessOutputHandler(ImageView imageView, TextArea output) {
        super(imageView);

        this.output = output;
    }

    @Override
    public void output(Result result) {
        Mat image = result.getProcessedImage();

        @SuppressWarnings("unchecked")
        Sjoelbak sjoelbak = (Sjoelbak) result.getData().get(DetectSjoelbakElement.SJOELBAK);

        @SuppressWarnings("unchecked")
        List<Sjoelsteen> sjoelstenen = (List<Sjoelsteen>) result.getData().get(DetectSjoelstenenElement.SJOELSTENEN);

        drawSjoelbak(image, sjoelbak);
        drawSjoelstenen(image, sjoelstenen);

        Platform.runLater(() -> {
            output.setText(String.format("%d sjoelste%s gevonden.\n", sjoelstenen.size(), sjoelstenen.size() == 1 ? "en" : "nen"));

            sjoelbak.getAmountInLanes(sjoelstenen).entrySet().stream().forEach(entry -> {
                output.appendText(String.format("Poort %d bevat %d ste%s\n", entry.getKey(), entry.getValue(), entry.getValue() == 1 ? "en" : "nen"));
            });
        });

        setImage(image);
    }

    private void drawSjoelbak(Mat image, Sjoelbak sjoelbak) {
        Core.line(image, sjoelbak.getPoortenAreaStart(), sjoelbak.getPoortenAreaEnd(), new Scalar(0, 255, 0), 2);
    }

    private void drawSjoelstenen(Mat image, List<Sjoelsteen> sjoelstenen) {
        sjoelstenen.parallelStream().forEach((sjoelsteen) -> {
            Core.circle(image, sjoelsteen.getCenter(), 2, new Scalar(0, 0, 255), -1);
            Core.circle(image, sjoelsteen.getCenter(), sjoelsteen.getRadius(), new Scalar(0, 0, 255), 2);
        });
    }
}
