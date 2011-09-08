package ch.zhaw.wikidoclet.page;

import ch.zhaw.wikidoclet.formater.FormatClassPage;
import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.ClassDoc;

/**
 * Contains all information regarding a Class. The main purpose is to print the
 * Class Wiki Page with the toString() method.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 * @see ch.zhaw.wikidoclet.page.Page
 * 
 */
public class ClassPage extends Page {

	
	private FormatClassPage cpf;
	/**
	 * Standard constructor to create a ClassPage
	 * 
	 * @param c
	 *            A single ClassDoc Object from Javadoc
	 */
	public ClassPage(ClassDoc c) {
		
		cpf = new FormatClassPage(c);
		
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
		pageTitle.append(c.toString());
		setPageTitle(pageTitle);

	}

	/**
	 * Returns the ClassPage in a Wiki Page Syntax as String. This Class Page
	 * can be uploaded to any Mediawiki
	 * 
	 * @see ch.zhaw.wikidoclet.page.Page#toString()
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		output.append(cpf.printHead());
		output.append(cpf.printPackage());
		output.append(cpf.printDetailedDescription());
		output.append(cpf.printNestedClassList());
		output.append(cpf.printFieldSummary());
		output.append(cpf.printConstructorSummary());
		output.append(cpf.printMethodSummary());
		output.append(cpf.printFieldDetail());
		output.append(cpf.printConstructorDetail());
		output.append(cpf.printMethodDetails());
		output.append(cpf.printClassFooder());

		return output.toString();
	}

	

}
