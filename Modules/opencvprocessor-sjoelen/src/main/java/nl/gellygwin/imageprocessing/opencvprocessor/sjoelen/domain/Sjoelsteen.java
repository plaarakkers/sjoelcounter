package nl.gellygwin.imageprocessing.opencvprocessor.sjoelen.domain;

import org.opencv.core.Point;

/**
 *
 * Sjoelsteen
 */
public class Sjoelsteen {
	private final Point center;
	private final int radius;

	public Sjoelsteen(double[] information) {
		center = new Point(information[0], information[1]);
		this.radius = (int) information[2];
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

}
