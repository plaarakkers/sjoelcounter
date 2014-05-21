package nl.gellygwin.imageprocessing.sjoelengui.output;

import java.io.ByteArrayInputStream;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

/**
 *
 * AbstractJavaFXOutputHandler
 */
public abstract class AbstractJavaFXOutputHandler {

	private final ImageView imageView;

	protected AbstractJavaFXOutputHandler(ImageView imageView) {
		this.imageView = imageView;
	}

	protected void setImage(Mat image) {
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".jpg", image, matOfByte);
		ByteArrayInputStream stream = new ByteArrayInputStream(matOfByte.toArray());

		Image fxImage = new Image(stream);

		Platform.runLater(() -> {
			imageView.setImage(fxImage);
		});
	}
}
