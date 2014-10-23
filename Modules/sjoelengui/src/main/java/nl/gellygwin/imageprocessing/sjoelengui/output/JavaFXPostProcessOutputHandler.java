package nl.gellygwin.imageprocessing.sjoelengui.output;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nl.gellygwin.imageprocessing.opencvprocessor.Result;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PostProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelbak;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelsteen;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelbakElement;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelstenenElement;
import nl.gellygwin.imageprocessing.sjoelengui.models.RoundResult;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

/**
 *
 * JavaFXPostProcessOutputHandler
 */
public class JavaFXPostProcessOutputHandler implements PostProcessOutputHandler {

    private final ImageView imageView;

    private final Function<Result, RoundResult> resultToRoundResult;

    private final Consumer<RoundResult> updateResults;

    public JavaFXPostProcessOutputHandler(ImageView imageView, Function<Result, RoundResult> resultToRoundResult, Consumer<RoundResult> updateResults) {
        this.imageView = imageView;
        this.resultToRoundResult = resultToRoundResult;
        this.updateResults = updateResults;
    }

    @Override
    public void output(Result result) {
        Mat image = result.getOriginalImage();

        @SuppressWarnings("unchecked")
        Sjoelbak sjoelbak = (Sjoelbak) result.getData().get(DetectSjoelbakElement.SJOELBAK);

        @SuppressWarnings("unchecked")
        List<Sjoelsteen> sjoelstenen = (List<Sjoelsteen>) result.getData().get(DetectSjoelstenenElement.SJOELSTENEN);

        drawSjoelbak(image, sjoelbak);
        drawSjoelstenen(image, sjoelstenen);

        Image fxImage = matToImage(image);

        RoundResult rondeResult = resultToRoundResult.apply(result);

        Platform.runLater(() -> {
            imageView.setImage(fxImage);
            updateResults.accept(rondeResult);
        });
    }

    private void drawSjoelbak(Mat image, Sjoelbak sjoelbak) {
        Core.line(image, sjoelbak.getPoortenAreaStart(), sjoelbak.getPoortenAreaEnd(), new Scalar(0, 255, 0), 2);
        Core.line(image, sjoelbak.getSjoelbakLeftEnd(), sjoelbak.getSjoelbakLeftStart(), new Scalar(255, 0, 0), 2);
    }

    private void drawSjoelstenen(Mat image, List<Sjoelsteen> sjoelstenen) {
        sjoelstenen.stream().forEach((sjoelsteen) -> {
            Core.circle(image, sjoelsteen.getCenter(), 2, new Scalar(0, 0, 255), -1);
            Core.circle(image, sjoelsteen.getCenter(), sjoelsteen.getRadius(), new Scalar(0, 0, 255), 2);
        });
    }

    private Image matToImage(Mat image) {
        MatOfByte matOfByte = new MatOfByte();
        Highgui.imencode(".jpg", image, matOfByte);
        ByteArrayInputStream stream = new ByteArrayInputStream(matOfByte.toArray());

        return new Image(stream);
    }
}
