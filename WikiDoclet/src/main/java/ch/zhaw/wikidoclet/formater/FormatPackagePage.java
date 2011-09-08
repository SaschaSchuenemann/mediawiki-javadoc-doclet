package ch.zhaw.wikidoclet.formater;

import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Tag;

/**
 * That is the formatter class for formatting the package page.
 * The package page is an overview Javadoc Wiki page that shows
 * all packages in a current Java project.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public class FormatPackagePage extends FormatPage {


	private PackageDoc pkg;
	
	public FormatPackagePage(PackageDoc pkg) {
		this.pkg = pkg;
	}
	/**
	 * Prints the Head of the Wiki Page (Short Description + TOC + Package
	 * Title)
	 * 
	 * @return Package Page Title String
	 */
	public  StringBuilder printHead() {
		// this is ch.zhaw.javadoc2mediawiki.helloworld package description, see
		// [[http://en.wikipedia.org/wiki/Java_package Java Packages]] (Limited
		// to like 100 Chars (for better view in the search result)
		// __TOC__
		//
		// = Package: ch.zhaw.javadoc2mediawiki.helloworld =
		//
		//
		StringBuilder output = new StringBuilder();
		output.append(createSmallComment(new StringBuilder(pkg.commentText()))).append(NEWLINE).append("__TOC__").append(NEWLINE).append(NEWLINE).append(h1(new StringBuilder("Package: ").append(pkg.toString()))).append(NEWLINE).append(NEWLINE).append(NEWLINE);

		return output;

	}

	/**
	 * Prints the detailed description including all Tags and See Tags
	 * 
	 * @return Detailed Description String
	 */
	public StringBuilder printDetailedDescription() {
		// == Detailed Description ==\n
		//
		// this is ch.zhaw.javadoc2mediawiki.helloworld package description, see
		// [[http://en.wikipedia.org/wiki/Java_package Java Packages]]
		//
		// '''Since:'''
		// :0.1
		// '''Version:'''
		// :0.123
		// '''Author:'''
		// :Sami Shaio, Arthur van Hoff
		// '''See also:'''
		// [[link1]], [[link2]]
		StringBuilder output = new StringBuilder();

		output.append(h2(new StringBuilder("Detailed Description"))).append(NEWLINE).append(NEWLINE);
		output.append(createComment(new StringBuilder(pkg.commentText()))).append(NEWLINE);
		output.append(NEWLINE);

		// More tags to handle: http://de.wikipedia.org/wiki/Javadoc
		for (Tag t : pkg.tags()) {

			if (t.name() != "@see") {
				output.append(bold(new StringBuilder(t.name().substring(1).substring(0, 1).toUpperCase()).append(t.name().substring(2).toLowerCase()).append(":"))).append(NEWLINE);
				output.append(indent(new StringBuilder(t.text()))).append(NEWLINE);
			}

		}
		if (pkg.seeTags().length > 0) {
			output.append(bold(new StringBuilder("See also:"))).append(NEWLINE);
			for (Tag t : pkg.seeTags()) {
				String[] s = t.text().split("\\.");
				output.append(createWikiLink(new StringBuilder(t.text()), new StringBuilder(s[s.length - 1])));
			}
		}
		output.append(NEWLINE);

		return output;

	}

	/**
	 * Prints the Package Tree part of the Page
	 * 
	 * @return Package Tree String
	 */
	public StringBuilder printPackageTree() {
		// == Package Tree ==
		// *[[Javadoc:_ch|ch]]
		// **[[Javadoc:_ch.zhaw|zhaw]]
		// ***[[Javadoc:_ch.zhaw.javadoc2mediawiki|javadoc2mediawiki]]
		// ****[[Javadoc:_ch.zhaw.javadoc2mediawiki.helloworld|helloworld]]
		//
		StringBuilder output = new StringBuilder();
		output.append(h2(new StringBuilder("Package Tree"))).append(NEWLINE);

		StringBuilder curpkg = new StringBuilder();
		String[] p = pkg.toString().split("[\\.]");
		// output+=pkg.toString();
		// output+=Arrays.toString(pkg.toString().split("[\\.]"));
		for (int i = 0; i < p.length; i++) {

			if (curpkg.length()==0)
				curpkg.append(p[i]);
			else
				curpkg.append(".").append(p[i]);

			for (int j = 0; j <= i; j++)
				output.append("*");
			output.append(createWikiLink(new StringBuilder(curpkg), new StringBuilder(p[i]))).append(NEWLINE);
		}
		output.append(NEWLINE);
		return output;

	}

	/**
	 * Prints the Interface List
	 * 
	 * @return Interface List String
	 */
	public StringBuilder printInterfaceList() {
		/*
		 * == Interface == {| class="wikitable" border="1" !
		 * style="background: #CCCCFF" | Properties !
		 * style="background: #CCCCFF" | Name ! style="background: #CCCCFF" |
		 * Description |- |
		 * <tt>public</tt>||[[Javadoc:ch.zhaw.javadoc2mediawiki.
		 * helloworld.ExampleInterface|ExampleInterface]]||ExampleInterface
		 * Description |}
		 */

		StringBuilder output = new StringBuilder();
		ClassDoc[] iface = pkg.interfaces();
		if (iface.length > 0) {
			output.append(h2(new StringBuilder("Interface"))).append(NEWLINE);
			output.append(makeClassDocList(iface));
			output.append(NEWLINE);
		}
		return output;

	}

	/**
	 * Prints the ordinary Class List
	 * 
	 * @return Class List String
	 */
	public StringBuilder printOrdinaryClassList() {
		/*
		 * == Classes ==
		 * 
		 * {| class="wikitable" border="1" ! style="background: #CCCCFF" |
		 * Properties ! style="background: #CCCCFF" | Name !
		 * style="background: #CCCCFF" | Description |- |
		 * <tt>public</tt>||[[Javadoc
		 * :ch.zhaw.javadoc2mediawiki.helloworld.HelloWorld|HelloWorld]]||This
		 * is a Very big Description about this Hello World Class and its
		 * purpose in the big Scary World. |- | ||[[Javadoc:
		 * ch.zhaw.javadoc2mediawiki.helloworld.Graphics|Graphics]]||Graphics is
		 * the abstract base class for all graphics contexts which allow an
		 * application to draw onto components realized on various devices or
		 * onto off-screen images. |- |
		 * <tt>public</tt>||[[Javadoc:ch.zhaw.javadoc2mediawiki
		 * .helloworld.Painter|Painter]]||Class informations |- | <tt>private
		 * static
		 * </tt>||[[Javadoc:ch.zhaw.javadoc2mediawiki.helloworld.ByeWorld|
		 * ByeWorld ]]||Nobody Cared about this Class |- | <tt>Protected
		 * abstract</tt>||[[
		 * Javadoc:ch.zhaw.javadoc2mediawiki.helloworld.NewWorld
		 * |NewWorld]]||With some Comments |}
		 */

		StringBuilder output = new StringBuilder();
		ClassDoc[] classes = pkg.ordinaryClasses();
		if (classes.length > 0) {
			output.append(h2(new StringBuilder("Classes"))).append(NEWLINE);
			output.append(makeClassDocList(classes));
			output.append(NEWLINE);
		}
		return output;

	}

	/**
	 * Prints the Enum List
	 * 
	 * @return Enum List String
	 */
	public StringBuilder printEnumList() {
		/*
		 * == Enum ==
		 * 
		 * {| class="wikitable" border="1" ! style="background: #CCCCFF" |
		 * Properties ! style="background: #CCCCFF" | Name !
		 * style="background: #CCCCFF" | Description |- |
		 * <tt>public</tt>||[[Javadoc
		 * :ch.zhaw.javadoc2mediawiki.helloworld.Exampleenum
		 * |Exampleenum]]||ExampleEnum Description |}
		 */

		StringBuilder output = new StringBuilder();
		ClassDoc[] enums = pkg.enums();
		if (enums.length > 0) {
			output.append(h2(new StringBuilder("Enum"))).append(NEWLINE);
			output.append(makeClassDocList(enums));
			output.append(NEWLINE);
		}
		return output;

	}

	/**
	 * prints the Errors and Exceptions List
	 * 
	 * @return Error and Exceptions List String
	 */
	public StringBuilder printErrorList() {
		/*
		 * == Errors / Exceptions ==
		 * 
		 * {| class="wikitable" border="1" ! style="background: #CCCCFF" |
		 * Properties ! style="background: #CCCCFF" | Name !
		 * style="background: #CCCCFF" | Description |- |
		 * <tt>public</tt>||[[Javadoc
		 * :ch.zhaw.javadoc2mediawiki.helloworld.ExampleException
		 * |ExampleException]]||ExampleException Description |}
		 */

		StringBuilder output = new StringBuilder();
		ClassDoc[] errors = pkg.errors();
		if (errors.length > 0) {
			output.append(h2(new StringBuilder("Errors / Exceptions"))).append(NEWLINE);
			output.append(makeClassDocList(errors));
			output.append(NEWLINE);
		}
		return output;

	}


	/**
	 * prints the page fooder including all Category tags
	 * 
	 * @return fooder String
	 */
	public StringBuilder printFooder() {

		// [[Category:Java_Package]]
		// [[Category:Project_XY|h]]
		// [[Category:Javadoc_Documentation|h]]
		// [[Category:finalTemplate|h]]

		StringBuilder output = new StringBuilder();

		output.append("[[Category:Java_Package|").append(pkg.name().charAt(0)).append("]]").append(NEWLINE);
		output.append("[[Category:Javadoc_Documentation|").append(pkg.name().charAt(0)).append("]]").append(NEWLINE);
	
		//Add the Project Category if its not ""
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		StringBuilder project = new StringBuilder(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_PROJ));
		if(project.length()>0) {
		
			output.append("[[Category:").append(project).append("|").append(pkg.name().charAt(0)).append("]]").append(NEWLINE);
		}

		return output;

	}
}