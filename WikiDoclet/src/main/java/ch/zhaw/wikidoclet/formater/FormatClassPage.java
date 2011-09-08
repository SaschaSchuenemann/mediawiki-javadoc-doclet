package ch.zhaw.wikidoclet.formater;

import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

/**
 * That is the class that is in charge of the page design for the Javadoc class comments
 * that are rendered to a Javadoc Wiki page.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class FormatClassPage extends FormatPage{
	private ClassDoc cs;
	
	public FormatClassPage(ClassDoc cs) {
		this.cs = cs;
	}
	/**
	 * Prints the Head of the Wiki Class Page (Short Description + TOC + Class
	 * Title)
	 * 
	 * @return Class Page Title StringBuilder
	 */
	public StringBuilder printHead() {
		// this is ch.zhaw.javadoc2mediawiki.helloworld package description, see
		// [[http://en.wikipedia.org/wiki/Java_package Java Packages]] (Limited
		// to like 100 Chars (for better view in the search result)
		// __TOC__
		//
		// = Class: ch.zhaw.javadoc2mediawiki.helloworld =
		//
		//
		StringBuilder output = new StringBuilder();

		output.append(createSmallComment(new StringBuilder(cs.commentText()))).append(NEWLINE).append("__TOC__").append(NEWLINE).append(NEWLINE).append(h1(new StringBuilder("Class:").append(cs.toString()))).append(NEWLINE).append(NEWLINE).append(NEWLINE);
		return  output;

	}

	/**
	 * Prints the Package Section with the Package Link
	 * 
	 * @return Package Section String
	 */
	public StringBuilder printPackage() {
		// == Package ==
		//
		// [[Javadoc:_ch.zhaw.javadoc2mediawiki.helloworld|ch.zhaw.javadoc2mediawiki.helloworld]]
		//
		StringBuilder output = new StringBuilder();
		output.append(h2(new StringBuilder("Package"))).append(NEWLINE).append(NEWLINE).append(createWikiLink(new StringBuilder(cs.containingPackage().toString()))).append(NEWLINE).append(NEWLINE);
		return output;
	}

	/**
	 * Prints the Detailed Description Section
	 * 
	 * @return Detailed Description Section String
	 */
	public StringBuilder printDetailedDescription() {
		// == Detailed Description ==\n
		//
		// <tt>public class Painter</tt>
		// :<tt>extends Graphics</tt>
		// ::<tt>implements java.lang.Comparable<Painter>,
		// java.awt.event.ActionListener</tt>
		// this is ch.zhaw.javadoc2mediawiki.helloworld.HelloWorld class
		// description
		//
		// '''Since:'''
		// :0.1
		// '''Version:'''
		// :0.1
		// '''Author:'''
		// :Sami Shaio, Arthur van Hoff
		// '''See also:'''
		// [[link1]], [[link2]]

		StringBuilder output = new StringBuilder();

		output.append(h2(new StringBuilder("Detailed Description"))).append(NEWLINE).append(NEWLINE);

		// Class Header
		output.append(code(new StringBuilder(cs.modifiers())).append(" ").append(cs.typeName()).append(" ").append(cs.simpleTypeName())).append(NEWLINE);
		// Superclasses
		output.append(indent(code(new StringBuilder("extends ").append(cs.superclass())))).append(NEWLINE);
		// Interfaces
		if (cs.interfaces().length > 0) {
			StringBuilder sIfaces = new StringBuilder();
			for (Type iface : cs.interfaceTypes()) {
				sIfaces.append(" ").append(iface.qualifiedTypeName());
			}
			output.append(indent(indent(code(new StringBuilder("implements").append(sIfaces))))).append(NEWLINE);
		}
		// Class Description
		output.append(NEWLINE);
		output.append(createComment(new StringBuilder(cs.commentText()))).append(NEWLINE).append(NEWLINE);

		// More tags to handle: http://de.wikipedia.org/wiki/Javadoc
		output.append(createMethodTags(cs.tags()));

		return output;

	}

	/**
	 * Prints the Nested class List Section
	 * 
	 * @return Nested class List Section String
	 */
	public StringBuilder printNestedClassList() {
		// == Nested Classes ==
		// {| class="wikitable" border="1" width="100%"
		// ! style="background: #CCCCFF" | Typ
		// ! style="background: #CCCCFF" | Name
		// ! style="background: #CCCCFF" | Description
		// |-
		// |<tt>class</tt>
		// |<tt>[[Javadoc:ch.zhaw.javadoc2mediawiki.helloworld.HelloWorld.outputer|HelloWorld.outputer]]
		// </tt>
		// |
		// Big Nestedclass Description
		// <toggledisplay>
		// {| class="wikitable" border="1" width="100%"
		// style="background: #EAEAFF"
		// |-
		// |
		// {{Javadoc:ch.zhaw.javadoc2mediawiki.helloworld.HelloWorld.outputer}}
		// |}
		// </toggledisplay> (This Show/Hidde functionality is part of a
		// Mediawiki
		// Extension called ToggleDisplay and is optional. We put in this part
		// only
		// if we can check if this Extension is available.)
		// |}
		StringBuilder output = new StringBuilder();
		ClassDoc[] innerclass = cs.innerClasses();
		if (innerclass.length > 0) {
			output.append(h2(new StringBuilder("Nested Classes"))).append(NEWLINE);
			output.append(makeClassDocList(innerclass));
			output.append(NEWLINE);
		}
		return output;

	}

	/**
	 * Prints the Field Summary Section
	 * 
	 * @return Field Summary Section String
	 */
	public StringBuilder printFieldSummary() {
		//
		// == Field Summary ==
		// {| class="wikitable" border="1" width="100%"
		// ! style="background: #CCCCFF" | Method
		// ! style="background: #CCCCFF" | Description
		// |-
		// |<tt>static short [[#MAXVAL|MAXVAL]]</tt>
		// | Contains the Max Value of XY
		// |}
		//
		StringBuilder output = new StringBuilder();
		FieldDoc[] fields = cs.fields();
		if (fields.length > 0) {
			output.append(h2(new StringBuilder("Field Summary"))).append(NEWLINE);

			WikiTable fieldtable = new WikiTable(new String[] { "Field", "Description" });

			for (int i = 0; i < fields.length; i++) {

				// Replace last "." with a "#" (ie: ch.bla.xx.hallo -->
				// ch.bla.xx#hallo) This is used for internal Document Linking
				// FIXME: Check if this works with Nested Classes!
				StringBuilder link = new StringBuilder();
				link.append(fields[i].toString().substring(0, fields[i].toString().lastIndexOf("."))).append("#").append(fields[i].toString().substring(fields[i].toString().lastIndexOf(".") + 1));

				fieldtable
						.add(new String[] { 
								code(new StringBuilder(fields[i].modifiers()).append(" ").append(fields[i].type().simpleTypeName()).append(" ").append(createWikiLink(link, new StringBuilder(fields[i].name())))).toString(), 
								createSmallComment(new StringBuilder(fields[i].commentText())).toString() });

			}
			output.append(fieldtable.print());
			output.append(NEWLINE);
		}

		return output;
	}

	/**
	 * Prints the Constructor Summary Section
	 * 
	 * @return Constructor Summary Section String
	 */
	public StringBuilder printConstructorSummary() {
		// == Constructor Summary ==
		// {| class="wikitable" border="1" width="100%"
		// ! style="background: #CCCCFF" | Method
		// ! style="background: #CCCCFF" | Description
		// |-
		// |<tt>[[#HelloWorld|HelloWorld]]()</tt>
		// | Description for the Constructor
		// |}
		//
		StringBuilder output = new StringBuilder();
		ConstructorDoc[] constructors = cs.constructors();
		if (constructors.length > 0) {
			output.append(h2(new StringBuilder("Constructor Summary"))).append(NEWLINE);

			WikiTable fieldtable = new WikiTable(new String[] { "Method", "Description" });

			for (int i = 0; i < constructors.length; i++) {

				// Replace last "." with a "#" (ie: ch.bla.xx.hallo -->
				// ch.bla.xx#hallo) This is used for internal Document Linking
				// FIXME: Check if this works with Nested Classes!
				StringBuilder link = new StringBuilder();
				link.append(constructors[i].qualifiedName().substring(0, constructors[i].qualifiedName().lastIndexOf("."))).append("#").append(constructors[i].qualifiedName().substring(constructors[i].qualifiedName().lastIndexOf(".") + 1));

				StringBuilder params = new StringBuilder();
				params.append("(");
				boolean first = false;
				for (Parameter p : constructors[i].parameters()) {
					if (first)
						params.append(", ");
					else
						first = !first;
					params.append(p.type().qualifiedTypeName()).append(" ").append(p.name());
				}
				params.append(")");

				fieldtable.add(new String[] { 
						code(new StringBuilder(constructors[i].modifiers()).append(" ").append(createWikiLink(link, new StringBuilder(constructors[i].name())).append(params))).toString(),
						createSmallComment(new StringBuilder(constructors[i].commentText())).toString() });

			}
			output.append(fieldtable.print());
			output.append(NEWLINE);
		}

		return output;
	}

	/**
	 * Prints the Method Summary Section
	 * 
	 * @return Method Summary Section String
	 */
	public StringBuilder printMethodSummary() {
		// == Method Summary==
		//
		// {| class="wikitable" border="1" width="100%"
		// ! style="background: #CCCCFF" | Type
		// ! style="background: #CCCCFF" | Symbol
		// |-
		// | <tt>java.awt.Image</tt>||<tt>[[#getImage|getImage]](java.net.URL
		// url,
		// java.lang.String name)</tt>
		// :Returns an Image object that can then be painted on the screen.
		// |-
		// | <tt>static void</tt>||<tt>[[#main|main]](java.lang.String[]
		// args)</tt>
		// :Shows a nice "Hello World" on the Console args.
		// |}
		//
		// {| class="wikitable" border="1" width="100%"
		// ! | '''Methods inherited from class java.lang.Object'''
		// |-
		// | <tt>equals, getClass, hashCode, notify, notifyAll, toString, wait,
		// wait, wait</tt>
		// |}
		//
		StringBuilder output = new StringBuilder();
		MethodDoc[] methods = cs.methods();
		if (methods.length > 0) {
			output.append(h2(new StringBuilder("Method Summary"))).append(NEWLINE);

			WikiTable fieldtable = new WikiTable(new String[] { "Type", "Symbol" });

			for (int i = 0; i < methods.length; i++) {

				// Replace last "." with a "#" (ie: ch.bla.xx.hallo -->
				// ch.bla.xx#hallo) This is used for internal Document Linking
				// FIXME: Check if this works with Nested Classes!
				StringBuilder link = new StringBuilder();
				link.append(methods[i].qualifiedName().substring(0, methods[i].qualifiedName().lastIndexOf("."))).append("#").append(createMethodTitle(methods[i]));

				StringBuilder params = new StringBuilder();
				params.append("(");
				boolean first = false;
				for (Parameter p : methods[i].parameters()) {
					if (first)
						params.append(", ");
					else
						first = !first;
					params.append(p.type().qualifiedTypeName()).append(" ").append(p.name());
				}
				params.append(")");

				fieldtable.add(new String[] { code(new StringBuilder(methods[i].modifiers())).toString(),
						code(new StringBuilder(createWikiLink(link, new StringBuilder(methods[i].name()))).append(params)).append(NEWLINE).append(indent(new StringBuilder(createSmallComment(new StringBuilder(methods[i].commentText()))))).toString() });

			}
			output.append(fieldtable.print());
			output.append(NEWLINE);
		}

		return output;
	}
	/**
	 * Prints the Field Details Section
	 * 
	 * @return Field Details Section String
	 */
	public StringBuilder printFieldDetail() {
//		==Field Details==
//		==Field1==
//		<tt>Private static final String TEST</tt>
//		:Description
//		
//		'''Parameter:'''
//		:Value
//		----
//		==Field2==
		StringBuilder output = new StringBuilder();
		FieldDoc[] fields = cs.fields();
		if (fields.length > 0) {
			output.append(h2(new StringBuilder("Field Details"))).append(NEWLINE);

			boolean first = true;

			for (int i = 0; i < fields.length; i++) {

				if(!first) {
					output.append("----").append(NEWLINE);
				} else {
					first = !first;
				}

				output.append(h3(new StringBuilder(fields[i].toString().substring(fields[i].toString().lastIndexOf(".") + 1)))).append(NEWLINE).append(NEWLINE);

				output.append(code(new StringBuilder(fields[i].modifiers()).append(" ").append(fields[i].type().simpleTypeName()).append(" ").append(fields[i].name()))).append(NEWLINE).append(NEWLINE);
				
				output.append(indent(createSmallComment(new StringBuilder(fields[i].commentText()))));
				
				output.append(createMethodTags(fields[i].tags()));

			}
			output.append(NEWLINE);
		}
		
		
		return output;
	}
	
	/**
	 * Prints the Constructor Details Section
	 * 
	 * @return Constructor Details Section String
	 */
	public StringBuilder printConstructorDetail() {
		// ==Constructor Details ==
		//
		// ===HelloWorld()===
		//
		// <tt>public HelloWorld()</tt>
		// :Some Description about this Constructor
		//
		StringBuilder output = new StringBuilder();
		ConstructorDoc[] constructors = cs.constructors();
		boolean first = true;
		if (constructors.length > 0) {
			output.append(h2(new StringBuilder("Constructor Details"))).append(NEWLINE);
			for (ConstructorDoc ct : constructors) {
				if(!first) {
					output.append("----").append(NEWLINE);
				} else {
					first = !first;
				}
				output.append(createMethodHead(ct));
				output.append(createMethodTags(ct.tags()));
			}
		}
		return output;
	}

	/**
	 * Prints the Method Details Section
	 * 
	 * @return Method Details Section String
	 */
	public StringBuilder printMethodDetails() {
		// ==Method Details==
		//
		// ===getImage===
		//
		// <tt>public java.awt.Image getImage(java.net.URL url, java.lang.String
		// name)</tt>
		//
		// :Returns an Image object that can then be painted on the screen. The
		// url
		// argument must specify an absolute URL. The name argument is a
		// specifier
		// that is relative to the url argument.
		//
		// :This method always returns immediately, whether or not the image
		// exists.
		// When this applet attempts to draw the image on the screen, the data
		// will
		// be loaded. The graphics primitives that draw the image will
		// incrementally
		// paint on the screen.
		//
		// :'''Parameters:'''
		// ::<tt>url</tt> - an absolute URL giving the base location of the
		// image
		// ::<tt>name</tt> - the location of the image, relative to the url
		// argument
		// :'''Returns:'''
		// ::the image at the specified URL
		// :'''See Also:'''
		// ::<tt>Image</tt>
		//
		// ===main===
		//
		// <tt>public static void main(java.lang.String[] args)</tt>
		//
		// :Shows a nice "Hello World" on the Console args. A large amount of
		// Hello
		// World Programs exist. But this one is Special
		//
		// :'''Parameters:'''
		// ::<tt>args</tt> - Many Arguments can be supplied by console, but they
		// will all be ignored
		// :'''See Also:'''
		// ::<tt>Image</tt>
		StringBuilder output = new StringBuilder();
		MethodDoc[] methods = cs.methods();
		boolean first = true;
		if (methods.length > 0) {
			output.append(h2(new StringBuilder("Method Details"))).append(NEWLINE);
			for (MethodDoc m : methods) {
				if(!first) {
					output.append("----").append(NEWLINE);
				} else {
					first = !first;
				}
				output.append(createMethodHead(m));
				output.append(createMethodTags(m.tags()));
			}
		}
		return output;
	}

	/**
	 * Creates the Method Tags List as a String
	 * 
	 * @param tags
	 *            Tag Array containing all Tags to be parsed
	 * @return Method Tag List String
	 */
	private StringBuilder createMethodTags(Tag[] tags) {
		StringBuilder output = new StringBuilder();
		// More tags to handle: http://de.wikipedia.org/wiki/Javadoc
		for (Tag t : tags) {
			// FIXME: Group Similar Tags and print them at only with one title

			// Unknown Tags:
			// {@inheritDoc}
			// {@link reference}
			// {@linkPlain reference}
			// {@value}
			// {@code}
			// {@literal}
			
			String x = t.name().toLowerCase();

			if (x.equals("@author") || x.equals("@version") || x.equals("@since") || x.equals("@return") || x.equals("@deprecated")) {
				// @author name
				// @version version
				// @since jdk-version

				// @return description
				// @deprecated description

				// Normal Parameter Head
				output.append(bold(new StringBuilder(t.name().substring(1).substring(0, 1).toUpperCase()).append(t.name().substring(2).toLowerCase()).append(":"))).append(NEWLINE);
				// Text
				output.append(indent(simpleText(new StringBuilder(t.text())))).append(NEWLINE);
			} else if (x.equals("@see")) {
				// @see reference

				// SKIP this for now and put it to the bottom
			} else if (x.equals("@param") || x.equals("@exception") || x.equals("@throws")) {
				// @param name description
				// @exception classname description
				// @throws classname description
				StringBuilder paramCont = new StringBuilder(t.text().replaceAll("\n+", ""));
				int firstspace = paramCont.indexOf(" ");
				// Normal Parameter Head
				output.append(bold(new StringBuilder(t.name().substring(1).substring(0, 1).toUpperCase()).append(t.name().substring(2).toLowerCase()).append(":"))).append(NEWLINE);
				if (firstspace <= 0) {
					// This could be an Error Case
					// TODO: Define if @param, @exception or @throws with only
					// one parameter should be printed as normal Text or as
					// Error
					output.append(indent(simpleText(new StringBuilder(paramCont)))).append(NEWLINE);
				} else {

					
					// Parameter + Description

					output.append(indent(code(new StringBuilder(paramCont.substring(0, firstspace))).append(" - ").append(simpleText(new StringBuilder(paramCont.substring(firstspace))))));
				}
			} else {
				// Error, Tag Unknown (Maybe needed or should it be ignored?)
				output.append("found unknown Tag: ").append(t.name()).append(" value: ").append(t.text()).append(NEWLINE);
			}
		}
		if (cs.seeTags().length > 0) {
			output.append(bold(new StringBuilder("See also:"))).append(NEWLINE);
			for (Tag t : tags) {
				String[] s = t.text().split("\\.");
				output.append(createWikiLink(new StringBuilder(t.text()), new StringBuilder(s[s.length - 1])));
			}
		}
		output.append(NEWLINE);
		return output;
	}

	/**
	 * Creates the Method Head with all Parameters (Public String Foo(String
	 * bar)
	 * 
	 * @param m
	 *            A Element of type ExecutableMemberDoc
	 * @return Method Head String
	 */
	private StringBuilder createMethodHead(ExecutableMemberDoc m) {
		StringBuilder output = new StringBuilder();


		StringBuilder params = new StringBuilder();
		params.append("(");
		boolean first = false;
		for (Parameter p : m.parameters()) {
			if (first)
				params.append(", ");
			else
				first = !first;
			params.append(p.type().qualifiedTypeName()).append(" ").append(p.name());
		}
		params.append(")");

		output.append(h3(createMethodTitle(m))).append(NEWLINE).append(NEWLINE);
		output.append(code(new StringBuilder(m.modifiers()).append(" ").append(m.name()).append(params))).append(NEWLINE);
		output.append(indent(new StringBuilder(createComment(new StringBuilder(m.commentText()))))).append(NEWLINE).append(NEWLINE);
		return output;
	}

	/**
	 * Builds the Method Title. This Method is used by the Method Details list and the Method Summary List to create the Links
	 * @param m Method ExecutableMemberDoc
	 * @return Method Title String (eg: "print(String)")
	 */
	private StringBuilder createMethodTitle(ExecutableMemberDoc m) {
		StringBuilder typparam = new StringBuilder();
		typparam.append("(");
		boolean first = false;
		for (Parameter p : m.parameters()) {
			if (first)
				typparam.append(", ");
			else
				first = !first;
			typparam.append(p.type().typeName());
		}
		typparam.append(")");
		return new StringBuilder(m.name()).append(typparam);
	}

	/**
	 * Prints the Class Page Fooder
	 * 
	 * @return Class Page Fooder String
	 */
	public StringBuilder printClassFooder() {

		// [[Category:Java_Class|H]]
		// [[Category:Project_XY|H]]
		// [[Category:Javadoc_Documentation|H]]
		StringBuilder output = new StringBuilder();
		
		//Add the Java Class Category
		output.append("[[Category:Java_Class|").append(cs.simpleTypeName().charAt(0)).append("]]").append(NEWLINE);

		//Add the Javadoc Documentation Category
		output.append("[[Category:Javadoc_Documentation|").append(cs.simpleTypeName().charAt(0)).append("]]").append(NEWLINE);
		
		//Add the Project Category if its not ""
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		StringBuilder project = new StringBuilder(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_PROJ));
		if(project.length()>0) {
			output.append("[[Category:").append(project).append("|").append(cs.simpleTypeName().charAt(0)).append("]]").append(NEWLINE);
		}
		
		return output;
	}
}
