package nl.gellygwin.imageprocessing.opencvprocessor.input;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 *
 * WebcamCaptureInputHandler
 */
public class WebcamCaptureInputHandler implements InputHandler {

	private final VideoCapture videoCapture;

	public WebcamCaptureInputHandler(int device) {
		videoCapture = new VideoCapture(device);
	}

	@Override
	public Mat getImage() {
		Mat image = new Mat();

		if (videoCapture.isOpened()) {
			videoCapture.read(image);
		}

		return image;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public void release() {
		videoCapture.release();
	}

}
