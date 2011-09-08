package ch.zhaw.helloworld;

import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;

/**
 * Class informations
 */

public class Painter extends Graphics implements Comparable<Painter>, ActionListener {

	/**
	 * Some Information about the 1st Constructor
	 */
	public Painter() {
		//Cunstruct something
	}
	
	/**
	 * Some Information about the 2nd Constructor
	 */
	public Painter(Image img) {
		
	}
	
	/**
	 * Some Information about the 3rd Constructor
	 */
	public Painter (Image img, Frame f) {
		
	}
	
	/**
	 * Does draw some stuff in an Image
	 */
	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Does a Dispose on nothing
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * Compares a Painter with another
	 */
	@Override
	public int compareTo(Painter o) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Does catch Actions from a Eventhandlers
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
