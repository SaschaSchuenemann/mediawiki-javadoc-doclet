package ch.zhaw.helloworld;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>This is a Very bigggg Description about this Hello World Class and its purpose 
 * in the big Scary World. Since it was born as a single child it had not many 
 * friends during its childhood. But since it got the attention of many developers, 
 * its popularity exploded. Everyone wanted to get a hand on the Hello World 
 * Class and every single developer at least used it once during his process of 
 * learning a new programming language.</p>
 * <a href="http://www.baal.ch">Test</a>
 * <a href="http://www.baal2.ch">Test</a>
 * asf
 * <a href="http://www.baal4.ch">Test</a>
 * 
 * 
 * <p> * A mutable sequence of characters.  This class provides an API compatible
 * with <code>StringBuffer</code>, but with no guarantee of synchronization.
 * This class is designed for use as a drop-in replacement for
 * <code>StringBuffer</code> in places where the string buffer was being
 * used by a single thread (as is generally the case).   Where possible,
 * it is recommended that this class be used in preference to
 * <code>StringBuffer</code> as it will be faster under most implementations.</p>
 * 
 * <p>The principal operations on a <code>StringBuilder</code> are the 
 * <code>append</code> and <code>insert</code> methods, which are 
 * overloaded so as to accept data of any type. Each effectively 
 * converts a given datum to a string and then appends or inserts the 
 * characters of that string to the string builder. The 
 * <code>append</code> method always adds these characters at the end 
 * of the builder; the <code>insert</code> method adds the characters at 
 * a specified point. </p>
 * <p>
 * For example, if <code>z</code> refers to a string builder object 
 * whose current contents are "<code>start</code>", then 
 * the method call <code>z.append("le")</code> would cause the string 
 * builder to contain "<code>startle</code>", whereas 
 * <code>z.insert(4, "le")</code> would alter the string builder to 
 * contain "<code>starlet</code>". </p>
 * <p>
 * In general, if sb refers to an instance of a <code>StringBuilder</code>, 
 * then <code>sb.append(x)</code> has the same effect as 
 * <code>sb.insert(sb.length(),&nbsp;x)</code>.</p>
 *
 * <p>Every string builder has a capacity. As long as the length of the 
 * character sequence contained in the string builder does not exceed 
 * the capacity, it is not necessary to allocate a new internal 
 * buffer. If the internal buffer overflows, it is automatically made larger.</p>
 *
 * <p>Instances of <code>StringBuilder</code> are not safe for
 * use by multiple threads. If such synchronization is required then it is
 * recommended that {@link java.lang.StringBuffer} be used.</p>
 * 
 * <p><b>Thats Bold</b><i>Thats Italic</i></p>
 *                  
 * @author      Rolf Koch
 *                  
 * @version     1.0
 *                   
 * @since       0.0
 */

public class HelloWorld4 extends Object implements test {

	static String Bla="test";
	/**
	 * Returns an Image object that can then be painted on the screen. 
	 * The url argument must specify an absolute  
	                          
	{@link URL}. The name
	 * argument is a specifier that is relative to the url argument. 
	 * <p>
	 * This method always returns immediately, whether or not the 
	 * image exists. When this applet attempts to draw the image on
	 * the screen, the data will be loaded. The graphics primitives 
	 * that draw the image will incrementally paint on the screen. 
	 *
	 *  
	                          
	@param  url  an absolute URL giving the base location of the image
	 *  
	                          
	@param  name the location of the image, relative to the url argument
	 *  
	                          
	@return      the image at the specified URL
	 *  
	                          
	@see         Image
	 */
	 public Image getImage(URL url, String name) {
	        //Lets try to do nothing here or crash the whole system
		 	try {
	            return getImage(new URL(url, name));
	        } catch (MalformedURLException e) {
	            return null;
	        }
	 }
	 
	/**
	 * Load an Image from an URL and dump it in a bin
	                          
	 * Doesn't really do something but looks kinda nice
	 *                       
	 * @param  url  Get an URL which will not be parsed                      
	 * @return      An Image with the Value Null                   
	 * @see         Image
	 */
	 private Image getImage(URL url) {
		// Here we don like nothing
		return null;
	}

	/**
	 * Shows a nice "Hello World" on the Console
	                          
	 * {@link args}. A large 
	 * amount of Hello World Programs exist. But this one is Special
	 *                       
	 * @param  args  Many Arguments can be supplied by console, but they will all be ignored            
	 * @see         Image
	 */

	public void main(String[] args) {
		// Print the Hello World Output
		Outputer n = new Outputer();
		n.printit("helloworld");
	}
	
	/**
	 * Big Nestedclass Description
	 * @author Rolf
	 *
	 */
	public class Outputer {
		/**
		 * Nested Constructor Method with parameters
		 * @param s test
		 */
		public Outputer(String s) {
		}
		/**
		 * Nested Constructor method without parameter
		 * no param
		 */
		public Outputer() {
		}
		
		/**
		 * A Nested Method
		 * @param s test
		 */
		public void printit(String s) {
			System.out.println(s);
		}
	}

	@Override
	public String printDetailedDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printDoc() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * That's a test
	 * @author Christian Dubs
	 *
	 */
	@SuppressWarnings("unused")
	private class Blub {
		public Blub() {
			//hi there
		}
	}
}
