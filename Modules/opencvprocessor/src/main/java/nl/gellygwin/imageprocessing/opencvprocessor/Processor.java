package nl.gellygwin.imageprocessing.opencvprocessor;

import java.util.concurrent.locks.Lock;
import nl.gellygwin.imageprocessing.opencvprocessor.input.InputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PostProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PreProcessOutputHandler;
import nl.gellygwin.imageprocessing.opencvprocessor.pipeline.Pipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 *
 * Processor
 */
public class Processor {

    private final InputHandler inputHandler;

    private final PreProcessOutputHandler preProcessOutputHandler;

    private final PostProcessOutputHandler postProcessOutputHandler;

    private final Pipeline pipeline;

    private final Lock lock;

    private Result lastResult;

    /**
     * Initialize the processor.
     */
    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Constructor
     *
     * @param inputHandler
     * @param preProcessOutputHandler
     * @param postProcessOutputHandler
     * @param pipeline
     * @param lock
     *
     */
    public Processor(InputHandler inputHandler, PreProcessOutputHandler preProcessOutputHandler, PostProcessOutputHandler postProcessOutputHandler, Pipeline pipeline, Lock lock) {
        this.inputHandler = inputHandler;
        this.preProcessOutputHandler = preProcessOutputHandler;
        this.postProcessOutputHandler = postProcessOutputHandler;
        this.pipeline = pipeline;
        this.lock = lock;
    }

    /**
     * Start the processor.
     */
    public void start() {
        try {
            lock.lock();
            Result previousResult = null;
            while (inputHandler.hasNext() && !Thread.currentThread().isInterrupted()) {
                Mat image = inputHandler.getImage();

                if (!image.empty()) {
                    if (preProcessOutputHandler != null) {
                        preProcessOutputHandler.output(image);
                    }

                    Result result = new Result(image);

                    result = pipeline.process(result, previousResult);

                    lastResult = result;

                    previousResult = result;

                    postProcessOutputHandler.output(result);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public Result getLastResult() {
        return lastResult;
    }
}
