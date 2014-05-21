package nl.gellygwin.imageprocessing.opencvprocessor.input;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 *
 * VideoFileCaptureInputHandler
 */
public class VideoFileCaptureInputHandler implements InputHandler {

	private final VideoCapture videoCapture;
	private Mat image;

	public VideoFileCaptureInputHandler(String filename) {
		videoCapture = new VideoCapture(filename);
	}

	@Override
	public Mat getImage() {
		return image;
	}

	@Override
	public boolean hasNext() {
		processNextImage();
		return !image.empty();
	}

	@Override
	public void release() {
		videoCapture.release();
	}

	private void processNextImage() {
		image = new Mat();

		if (videoCapture.isOpened()) {
			videoCapture.read(image);
		}
	}

}
