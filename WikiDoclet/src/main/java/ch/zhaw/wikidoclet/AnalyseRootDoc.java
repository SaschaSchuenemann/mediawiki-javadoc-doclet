package ch.zhaw.wikidoclet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikidoclet.page.CategoryPage;
import ch.zhaw.wikidoclet.page.ClassPage;
import ch.zhaw.wikidoclet.page.IndexPage;
import ch.zhaw.wikidoclet.page.PackagePage;
import ch.zhaw.wikidoclet.page.Page;
import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

/**
 * The AnalyseRootDoc class is used to analyse a RootDoc Object from Javadoc. It
 * iterates over all Packages and Classes in the RootDoc Object and creates a
 * PackagePage for each PackageDoc Element and a ClassPage for each ClassDoc.
 * The iteration is happening Recursively
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 * @version 1.0
 */
class AnalyseRootDoc {

	// The list containing all pages
	private List<Page> pageList;

	/**
	 * Logger Variable
	 */
	private static final Logger logger = LoggerFactory.getLogger(WikiDoclet.class);

	/**
	 * The Default Constructor creates the PageSet and stores the RootDoc Object
	 * locally
	 * 
	 * @param rootDoc
	 *            RootDoc from Javadoc
	 */
	public AnalyseRootDoc(RootDoc rootDoc) {
		this.pageList = new ArrayList<Page>();
		analyseClassDocSet(rootDoc.specifiedClasses());
		analysePackageDocSet(rootDoc.specifiedPackages());
	
		IndexPage indexPage = new IndexPage();
		logger.debug("Created Index Page " + indexPage.getPageTitle());
		this.pageList.add(indexPage);
	
		//Class Category
		CategoryPage classCategoryPage = new CategoryPage("Java Class");
		logger.debug("Created Category Page " + classCategoryPage.getPageTitle());
		this.pageList.add(classCategoryPage);
		
		//Package Category
		CategoryPage packageCategoryPage = new CategoryPage("Java Package");
		logger.debug("Created Category Page " + packageCategoryPage.getPageTitle());
		this.pageList.add(packageCategoryPage);
		
		//Javadoc Category
		CategoryPage javadocCategoryPage = new CategoryPage("Javadoc Documentation");
		logger.debug("Created Category Page " + javadocCategoryPage.getPageTitle());
		this.pageList.add(javadocCategoryPage);
		
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		String project = cfg.getConfigValue(WikiDocletCfg.CFG_PAR_PROJ);
		if(!project.equals("")) {
			//Project Category
			CategoryPage projectCategoryPage = new CategoryPage(project);
			logger.debug("Created Category Page " + projectCategoryPage.getPageTitle());
			this.pageList.add(projectCategoryPage);
		}
		
		
	
	}

	/**
	 * Analyses an Array of PackageDoc Elements. For each PackageDoc Element a
	 * PackagePage is created and added to the PageSet HashMap. If a Class is
	 * found it will be analysed by AnalyseClassDocSet @see #AnalyseClassDocSet
	 * 
	 * @param pkg
	 *            PackageDoc Array
	 */
	public void analysePackageDocSet(PackageDoc[] pkg) {
		for (int i = 0; i < pkg.length; i++) {
			Page pkgpage = new PackagePage(pkg[i]);
			logger.debug("Created Packages Page " + pkgpage.getPageTitle());
			pageList.add(pkgpage);
			if (pkg[i].allClasses().length > 0) {
				analyseClassDocSet(pkg[i].allClasses());
			}
		}
	}
	
	/**
	 * Analyses an Array of ClassDoc Elements. For each ClassDoc Element a
	 * ClassPage is created and added to the PageSet HashMap. If a Nested Class
	 * is found it will be analysed again by AnalyseClassDocSet
	 * 
	 * @param cs
	 *            ClassDoc Array
	 */
	public void analyseClassDocSet(ClassDoc[] cs) {
		for (int i = 0; i < cs.length; i++) {
			ClassPage classpage = new ClassPage(cs[i]);
			logger.debug("Created Class Page " + classpage.getPageTitle());
			pageList.add(classpage);
		}
	}

	/**
	 * Getter for the pageList.
	 * 
	 * @return pageList the page list containing all WikiDoc pages.
	 */
	public List<Page> getPageList() {
		return pageList;
	}

}
