package nl.gellygwin.imageprocessing.sjoelengui.output;

import javafx.scene.image.ImageView;
import nl.gellygwin.imageprocessing.opencvprocessor.output.PreProcessOutputHandler;
import org.opencv.core.Mat;

/**
 *
 * JavaFXPreProcessOutputHandler
 */
public class JavaFXPreProcessOutputHandler extends AbstractJavaFXOutputHandler implements PreProcessOutputHandler {

	public JavaFXPreProcessOutputHandler(ImageView imageView) {
		super(imageView);
	}

	@Override
	public void output(Mat image) {
		setImage(image);
	}

}
