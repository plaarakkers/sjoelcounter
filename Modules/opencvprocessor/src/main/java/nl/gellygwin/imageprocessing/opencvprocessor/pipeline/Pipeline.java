package nl.gellygwin.imageprocessing.opencvprocessor.pipeline;

import java.util.ArrayList;
import java.util.List;
import nl.gellygwin.imageprocessing.opencvprocessor.Result;

/**
 *
 * Pipeline
 */
public class Pipeline {

    private List<PipelineElement> pipelineElements;

    /**
     * Constructor
     */
    public Pipeline() {
        pipelineElements = new ArrayList<>();
    }
    
    /**
     * 
     * @param pipelineElement
     * @return 
     */
    public Pipeline addElement(PipelineElement pipelineElement) {
        pipelineElements.add(pipelineElement);
        
        return this;
    }
    
    /**
     *
     * @param image
     * @param result
     * @param previousImageResult
     * @return
     */
    public Result process(Result result, Result previousImageResult) {
        Result elementResult = result;
        
        for (PipelineElement pipelineElement : pipelineElements) {
            elementResult = pipelineElement.processImage(elementResult, previousImageResult);
        }
        
        return elementResult;
    }
    
    
}
