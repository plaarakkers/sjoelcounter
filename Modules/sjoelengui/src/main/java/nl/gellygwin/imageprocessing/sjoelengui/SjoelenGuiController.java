package nl.gellygwin.imageprocessing.sjoelengui;

import java.nio.file.Paths;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import nl.gellygwin.imageprocessing.sjoelengui.output.JavaFXPostProcessOutputHandler;
import nl.gellygwin.imageprocessing.sjoelengui.output.JavaFXPreProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.Processor;
import nl.gellygwin.imageprocessing.opencvprocessor.input.ImageInputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.input.InputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.input.VideoFileCaptureInputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.input.WebcamCaptureInputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PostProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PreProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.pipeline.Pipeline;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelbakElement;
import nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.pipeline.DetectSjoelstenenElement;

/**
 *
 * SjoelenGuiController
 */
public class SjoelenGuiController {

    @FXML
    private TextArea output;

    @FXML
    private Button stop;

    @FXML
    private Button start;

    @FXML
    private ImageView preProcess;

    @FXML
    private ImageView postProcess;

    private Processor processor;

    private Task<Void> processorTask;

    private InputHandler inputHandler;

    @FXML
    private void startAction(ActionEvent event) {
        //inputHandler = new WebcamCaptureInputHandler(0);
        //inputHandler = new ImageInputHandler(Paths.get("") + "testfoto.jpg");
        inputHandler = new VideoFileCaptureInputHandler(Paths.get("") + "VID_20140519_124054.mp4");
        PreProcessOutputHandler preProcessOutputHandler = new JavaFXPreProcessOutputHandler(preProcess);
        PostProcessOutputHandler postProcessOutputHandler = new JavaFXPostProcessOutputHandler(postProcess, output);
        Pipeline pipeline = new Pipeline()
                .addElement(new DetectSjoelbakElement())
                .addElement(new DetectSjoelstenenElement());

        processor = new Processor(inputHandler, preProcessOutputHandler, postProcessOutputHandler, pipeline);

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
                output.appendText(String.format("task failed%s", System.lineSeparator()));
                output.appendText(String.format("%s%s", getException().toString(), System.lineSeparator()));

                stop();
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

        start.setDisable(true);
        stop.setDisable(false);
    }

    @FXML
    private void stopAction(ActionEvent event) {
        processorTask.cancel(true);
    }

    private void stop() {
        inputHandler.release();
        stop.setDisable(true);
        start.setDisable(false);
    }
}
