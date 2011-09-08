package ch.zhaw.wikidoclet.page;

import ch.zhaw.wikidoclet.formater.FormatPackagePage;
import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.PackageDoc;

/**
 * Contains all information regarding a package. The main purpose is to print
 * the Package Wiki Page with the toString() method.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 * @see ch.zhaw.wikidoclet.page.Page
 * 
 */
public class PackagePage extends Page {


	private FormatPackagePage ppf;

	/**
	 * Standard constructor to create a PackagePage
	 * 
	 * @param pkg
	 *            A single PackageDoc Object from Javadoc
	 */
	// Default Constructor
	public PackagePage(PackageDoc pkg) {
		
		this.ppf = new FormatPackagePage(pkg);

	
		//Create Pagetitle
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		StringBuilder pageTitle = new StringBuilder();
		//Project Namespace
		pageTitle.append(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_NSPACE));
		//Project Title
		String project = cfg.getConfigValue(WikiDocletCfg.CFG_PAR_PROJ);
		if(project.length()>0) {
			pageTitle.append(project).append(" ");
		}
		//Page Title
		pageTitle.append(pkg.toString());
		//Set the Title
		setPageTitle(pageTitle);

	}

	/**
	 * Returns the PackagePage in a Wiki Page Syntax as String. This Package
	 * Page can be uploaded to any MediaWiki
	 * 
	 * @see ch.zhaw.wikidoclet.page.Page#toString()
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		
		output.append(ppf.printHead());
		output.append(ppf.printDetailedDescription());
		output.append(ppf.printPackageTree());
		output.append(ppf.printInterfaceList());
		output.append(ppf.printOrdinaryClassList());
		output.append(ppf.printEnumList());
		output.append(ppf.printErrorList());
		output.append(ppf.printFooder());

		return output.toString();
	}
}
