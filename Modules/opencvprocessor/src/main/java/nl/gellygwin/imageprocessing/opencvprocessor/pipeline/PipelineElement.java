package nl.gellygwin.imageprocessing.opencvprocessor.pipeline;

import nl.gellygwin.imageprocessing.opencvprocessor.Result;

/**
 *
 * PipelineElement
 */
public interface PipelineElement {

    Result processImage(Result result, Result previousImageResult);
}
