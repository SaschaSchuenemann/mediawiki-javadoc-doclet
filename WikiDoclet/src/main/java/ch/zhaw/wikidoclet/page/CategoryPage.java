package ch.zhaw.wikidoclet.page;

import ch.zhaw.wikidoclet.formater.FormatCategoryPage;

/**
 * Represents a Category Page
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 * @see ch.zhaw.wikidoclet.page.Page
 * 
 */
public class CategoryPage extends Page {


	private FormatCategoryPage cpf;
	/**
	 * Default Constructor which only sets the Page Title.
	 */
	public CategoryPage(String title) {
		cpf = new FormatCategoryPage();
		
		StringBuilder pageTitle = new StringBuilder();
		pageTitle.append("Category:").append(title);
		setPageTitle(pageTitle);
		
	}

	/**
	 * Returns the Category Page Content
	 * 
	 * @see ch.zhaw.wikidoclet.page.Page#toString()
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(cpf.printHead());
		return output.toString();
	}

}
