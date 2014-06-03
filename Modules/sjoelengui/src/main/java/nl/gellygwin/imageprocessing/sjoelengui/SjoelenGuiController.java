package nl.gellygwin.imageprocessing.sjoelengui;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import nl.gellygwin.imageprocessing.opencvprocessor.Processor;
import nl.gellygwin.imageprocessing.opencvprocessor.Result;
import nl.gellygwin.imageprocessing.opencvprocessor.input.ImageInputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.input.InputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.input.VideoFileCaptureInputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.input.WebcamCaptureInputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PostProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.pipeline.Pipeline;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelbak;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain.Sjoelsteen;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelbakElement;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelstenenElement;
import nl.gellygwin.imageprocessing.sjoelengui.configuration.CaptureType;
import nl.gellygwin.imageprocessing.sjoelengui.configuration.SjoelenGuiConfiguration;
import nl.gellygwin.imageprocessing.sjoelengui.models.Round;
import nl.gellygwin.imageprocessing.sjoelengui.models.RoundResult;
import nl.gellygwin.imageprocessing.sjoelengui.output.JavaFXPostProcessOutputHandler;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * SjoelenGuiController
 */
public class SjoelenGuiController {

    @FXML
    private Button stop;

    @FXML
    private Button start;

    @FXML
    private ImageView postProcess;

    @FXML
    private Label rondeLabel;

    @FXML
    private Button reset;

    @FXML
    private TableView<RoundResult> result;

    private final Round round;

    private final ObservableList<RoundResult> resultList;

    private Processor processor;

    private Task<Void> processorTask;

    private InputHandler inputHandler;

    private final SjoelenGuiConfiguration sjoelenGuiConfiguration;

    private RoundResult previousRoundResult;

    private final SjoelstenenMapper sjoelstenenMapper;

    private final Lock lock;

    public SjoelenGuiController() {
        round = new Round();
        sjoelstenenMapper = new SjoelstenenMapper();
        resultList = FXCollections.observableArrayList();
        sjoelenGuiConfiguration = new SjoelenGuiConfiguration();
        lock = new ReentrantLock();
    }

    @FXML
    private void startAction(ActionEvent event) {
        if (round.getCurrent() == 3) {
            Dialogs.create().nativeTitleBar().masthead(null).message("Er zijn al drie ronden gespeeld. Druk eerst op de \"Reset\" knop.").showError();
        } else {
            round.increase();

            inputHandler = createInputHandler();
            PostProcessOutputHandler postProcessOutputHandler = new JavaFXPostProcessOutputHandler(postProcess, this::resultToRoundResult, this::updateResults);
            Pipeline pipeline = new Pipeline()
                    .addElement(new DetectSjoelbakElement())
                    .addElement(new DetectSjoelstenenElement());

            processor = new Processor(inputHandler, null, postProcessOutputHandler, pipeline, lock);

            processorTask = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    processor.start();

                    return null;
                }

                @Override
                protected void cancelled() {
                    super.cancelled();
                    stop();
                }

                @Override
                protected void failed() {
                    super.failed();
                    stop();
                    throw new SjoelenGuiException(getException().getMessage());
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    stop();
                }
            };

            Thread thread = new Thread(processorTask);
            thread.setDaemon(true);
            thread.start();

            setButtonsState(true);
        }
    }

    @FXML
    private void initialize() {
        rondeLabel.textProperty().bind(round.getProperty());
        result.setItems(resultList);
    }

    @FXML
    private void stopAction(ActionEvent event) {
        processorTask.cancel(true);
    }

    @FXML
    private void resetAction(ActionEvent event) {
        round.reset();
        resultList.clear();
        previousRoundResult = null;
    }

    private void stop() {
        try {
            lock.lock();
            inputHandler.release();
            setButtonsState(false);

            //First update the results for the current round.
            RoundResult roundResult = resultToRoundResult(processor.getLastResult());
            updateResults(roundResult);

            previousRoundResult = roundResult;
        } finally {
            lock.unlock();
        }
    }

    private void setButtonsState(boolean running) {
        start.setDisable(running);
        stop.setDisable(!running);
        reset.setDisable(running);
    }

    private InputHandler createInputHandler() {
        CaptureType captureType = sjoelenGuiConfiguration.getCaptureType();
        switch (captureType) {
            case WEBCAM:
                return new WebcamCaptureInputHandler(sjoelenGuiConfiguration.getWebcamCaptureDevice());
            case IMAGE:
                return new ImageInputHandler(String.format("%s%s", Paths.get(""), sjoelenGuiConfiguration.getImageCaptureFile()));
            case VIDEO:
                return new VideoFileCaptureInputHandler(String.format("%s%s", Paths.get(""), sjoelenGuiConfiguration.getVideoCaptureFile()));
            default:
                throw new SjoelenGuiException(String.format("Onbekende video capture type [%s] gevonden.", captureType.name()));
        }
    }

    private RoundResult resultToRoundResult(Result result) {
        @SuppressWarnings("unchecked")
        Sjoelbak sjoelbak = (Sjoelbak) result.getData().get(DetectSjoelbakElement.SJOELBAK);

        @SuppressWarnings("unchecked")
        List<Sjoelsteen> sjoelstenen = (List<Sjoelsteen>) result.getData().get(DetectSjoelstenenElement.SJOELSTENEN);

        Map<Integer, Integer> amountInPoorten = sjoelstenenMapper.mapSjoelstenenToPoorten(sjoelbak, sjoelstenen, previousRoundResult);

        return new RoundResult(round.getCurrent(), amountInPoorten);
    }

    private void updateResults(RoundResult roundResult) {
        if (resultList.size() == roundResult.getRound()) {
            resultList.set(roundResult.getRound() - 1, roundResult);
        } else {
            resultList.add(roundResult);
        }
    }

}
