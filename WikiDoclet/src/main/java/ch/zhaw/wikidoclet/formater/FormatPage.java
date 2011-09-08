package ch.zhaw.wikidoclet.formater;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.wikidoclet.util.WikiDocletCfg;

import com.sun.javadoc.ClassDoc;

/**
 * The general FormatPage yields all information that has to be available for all
 * types of page design.
 * 
 * @author Rolf Koch (kochrol@students.zhaw.ch), Christian Dubs (dubschr@students.zhaw.ch)
 *
 */
public abstract class FormatPage {
	/**
	 * Default Newline Charakter to be used
	 */
	static final String NEWLINE = "\n";

	/**
	 * Default Marker for cutted text to be used
	 */
	static final String POINTS = " ... ";

	/**
	 * Standard Index Page String Delimiter
	 */
	static String DELIMITER = ",";

	/**
	 * Logger Variable
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatPage.class);


	/**
	 * Wrapper Method for all Comment Strings to filter the Text before Posting
	 * it to the Page
	 * 
	 * @param rawComment
	 *            The whole Comment to be printed
	 * @return Filtered Comment String
	 */
	protected StringBuilder createComment(StringBuilder rawComment) {
	
		StringBuilder comment = new StringBuilder(rawComment.toString().replaceAll("\n +", " "));
	
		//Filter Allowed HTML Tags and Translate them to Wiki Markup
		comment = filter(comment, "<a (.*?)href=\"([^\"]*)\"(.*?)>(.*?)</a>", FilterType.A);
		comment = filter(comment, "<p(\\s.+?)?>(.*?)</p>", FilterType.P);
		comment = filter(comment, "<code(\\s.+?)?>(.*?)</code>", FilterType.CODE);
		comment = filter(comment, "<ul(\\s.+?)?>(.*?)</ul>", FilterType.UL);
		comment = filter(comment, "<i(\\s.+?)?>(.*?)</i>", FilterType.I);
		comment = filter(comment, "<b(\\s.+?)?>(.*?)</b>", FilterType.B);
		
		//Filter all remaning HTML Tags
		comment = replaceHtmlTags(comment);
		
		//Restore the Code Tag
		comment = filter(comment, "\\{\\{\\{(.*?)\\}\\}\\}", FilterType.RESTORE_CODE);
	
		
		return comment;
	
	}
	
	/**
	 * Filters all recognised HTML tags to be compatible with Wiki syntax.
	 * 
	 * @param rawComment the comment to be filtered.
	 * @param regex the regex to filter the raw comment with.
	 * @param filterType the type of the filter to divide between different tags.
	 * 
	 * @return outputs the filtered comment.
	 */
	protected StringBuilder filter(StringBuilder rawComment, String regex, FilterType filterType) {
		CharSequence output = rawComment;
		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(output);
		
		
		while (matcher.find()) {
			StringBuilder filtered = null;
			switch(filterType) {
				case P:
					LOGGER.trace("Found P : '{}'", matcher.group(2));
					filtered = createParagraph(new StringBuilder(matcher.group(2).trim()));		
					break;
				case B:
					LOGGER.trace("Found B : '{}'", matcher.group(2));
					filtered = bold(new StringBuilder(matcher.group(2).trim()));
					break;
				case I:
					LOGGER.trace("Found I : '{}'", matcher.group(2));
					//logger.trace("Found I : '{}'", matcher.group(0));
					filtered = createItalic(new StringBuilder(matcher.group(2).trim()));
					//logger.trace("filtered I : '{}'",filtered);
					break;
				case CODE:
					LOGGER.trace("Found CODE : '{}'", matcher.group(2));
					filtered = createCode(new StringBuilder(matcher.group(2).trim()));
					break;
				case RESTORE_CODE:
					LOGGER.trace("RESTORE CODE : '{}'", matcher.group(1));
					filtered = restoreCode(new StringBuilder(matcher.group(1).trim()));
					break;
					
				case UL:
					LOGGER.trace("Found UL : '{}'", matcher.group(2));
					filtered = filter(new StringBuilder(matcher.group(2).trim()), "<li(\\s.+?)?>(.*?)</li>", FilterType.LI);
					break;
				case LI:
					LOGGER.trace("Found LI : '{}'", matcher.group(2));
					filtered = createListItem(new StringBuilder(matcher.group(2).trim()));
					break;
				case A:
					LOGGER.trace("Found A : '{}'", matcher.group(2));
					filtered = createExternalWikiLink(new StringBuilder(matcher.group(2).trim()));
					break;
					
			}
			
			CharSequence before = output.subSequence(0, matcher.start());
			CharSequence after = output.subSequence(matcher.end(), output.length());
			//logger.trace("BEFORE: '{}'", before);
			//logger.trace("AFTER: '{}'", after);
			
			output = new StringBuilder(before).append(new StringBuilder(filtered)).append(new StringBuilder(after));
			matcher = pattern.matcher(output);
		}

		switch(filterType) {
			case LI:
				StringBuilder out = new StringBuilder();
				LOGGER.trace("LI: {}",output);
				out.append(NEWLINE).append(new StringBuilder(output.toString().replaceAll("\n +", "\n")));
				return out;
			case UL:
				return new StringBuilder(output.toString().replaceAll("\n +", "\n"));
			default:
				return new StringBuilder(output);
		}
		
	}
	
	private StringBuilder createParagraph(StringBuilder stringBuilder) {
		StringBuilder output = new StringBuilder();
		output.append(stringBuilder).append(NEWLINE).append(NEWLINE);
		return output;
	}
	
	private StringBuilder createItalic(StringBuilder stringBuilder) {
		StringBuilder output = new StringBuilder();
		output.append("''").append(stringBuilder).append("''");
		return output;
	}
	
	private StringBuilder createCode(StringBuilder stringBuilder) {
		StringBuilder output = new StringBuilder();
		output.append("{{{").append(stringBuilder).append("}}}");
		return output;
	}
	
	private StringBuilder restoreCode(StringBuilder stringBuilder) {
		StringBuilder output = new StringBuilder();
		output.append("<code>").append(stringBuilder).append("</code>");
		return output;
	}
	
	private StringBuilder createListItem(StringBuilder stringBuilder) {
		StringBuilder output = new StringBuilder();
		output.append("*").append(stringBuilder).append(NEWLINE);
		return output;
	}
	
	/**
	 * Creates a external Wiki Link
	 * 
	 * @param externalLink
	 * 
	 * @return External Link String in Wiki Syntax
	 */
	protected StringBuilder createExternalWikiLink(StringBuilder Link) {
		StringBuilder output = new StringBuilder();
		output.append("[").append(Link).append(" ").append(Link).append("]");
		return output;
	}
	
	/**
	 * Replaces all Tags in JavaCode by nothing
	 * @param rawComment
	 * @return
	 */
	protected StringBuilder replaceHtmlTags(StringBuilder rawComment) {
		String str = rawComment.toString();
		str.replaceAll("\\<.*?\\>", "");
		StringBuilder output = new StringBuilder(str);
		return output;

	}

	/**
	 * Creates a Wiki Link with the schema: [[WIKI_NAMESPACE:Link|Linktext]
	 * 
	 * @param Link
	 *            The whole Page Name or Link to be used in this Link
	 * @param Linktext
	 *            The Text to be shown on the Website as Link
	 * @return Link String in Wiki Syntax
	 */

	protected StringBuilder createWikiLink(StringBuilder Link, StringBuilder Linktext) {
		WikiDocletCfg cfg = WikiDocletCfg.getInstance();
		StringBuilder output = new StringBuilder();
		output.append("[[").append(cfg.getConfigValue(WikiDocletCfg.CFG_PAR_NSPACE));
		String project = cfg.getConfigValue(WikiDocletCfg.CFG_PAR_PROJ);
		if (project.length() > 0) {
			output.append(project).append(" ");
		}
		output.append(Link).append("|").append(Linktext).append("]]");
		return output;
	}

	/**
	 * Creates a Wiki Link with the same Link and Text with the schema:
	 * [[WIKI_NAMESPACE:Link|Link]]
	 * 
	 * @param Link
	 *            The whole PageName or Link to be used in this Link for Link
	 *            and Text
	 * @return Link String in Wiki Syntax
	 */
	protected StringBuilder createWikiLink(StringBuilder Link) {
		return createWikiLink(Link, Link);
	}
	
	/**
	 * Wrapper Method to cut down the Comment Strings to 300 Chars length
	 * maximal. If there is a .\n or \n\n within the first 200 Chars or a \n
	 * within the first 300 chars, it will be cutted at this location instead
	 * 
	 * @param rawComment
	 *            The whole Comment to be printed
	 * @return Cutted Comment String with 300 chars lengt maximal
	 */
	protected StringBuilder createSmallComment(StringBuilder rawComment) {
		StringBuilder output = new StringBuilder();
		rawComment = createComment(rawComment);

		if (rawComment.length() > 200) {

			int cutpos = 200;
			boolean addpoints = true;

			// Check first .\n string
			if (0 < rawComment.indexOf("." + NEWLINE) && rawComment.indexOf("." + NEWLINE) < 200) {
				cutpos = rawComment.indexOf("." + NEWLINE) + 1;
				addpoints = false;

			}
			// Check first \n\n string
			else if (0 < rawComment.indexOf(NEWLINE + NEWLINE) && rawComment.indexOf(NEWLINE + NEWLINE) < 200) {
				cutpos = rawComment.indexOf(NEWLINE + NEWLINE);
				addpoints = false;

			}
			// Check first \n string
			else if (0 < rawComment.indexOf(NEWLINE) && rawComment.indexOf(NEWLINE) < 300) {
				cutpos = rawComment.indexOf(NEWLINE);
				addpoints = true;

			}
			output.append(rawComment.substring(0, cutpos));
			if (addpoints)
				output.append(POINTS);

		} else {

			output.append(rawComment);

		}
		// TODO: Remove doubled Spaces and check if NEWLINE Chars are
		// Handled corrctly
		output = new StringBuilder(output.toString().replace(NEWLINE, " "));
		output = new StringBuilder(output.toString().replace("  ", " "));
		output = new StringBuilder(output.toString().replace("  ", " "));

		return output;
	}

	/**
	 * Inner Class to create a simple dynamic Wiki Table with a Header with a
	 * configurable color and dynamic content
	 * 
	 * @author Rolf
	 * @version 1.0
	 * 
	 */
	public class WikiTable {
		String[] names;
		int fieldcount;
		String titlecolor;
		ArrayList<String[]> elements;

		/**
		 * Constructor with only the Fieldnames Array as Parameter sets the
		 * titlebackgroundcolor to #CCCCFF by default. This constructor can be
		 * used if no customised title background color is required
		 * 
		 * @param fieldnames
		 *            A String Array containing the Title Fields. This Array
		 *            also defines the amount of collumns
		 */
		public WikiTable(String[] fieldnames) {
			this(fieldnames, "#CCCCFF");
		}

		/**
		 * Constructor to define fieldnames and titlebackgroundcolor
		 * 
		 * @param fieldnames
		 *            A String Array containing the Title Fields. This Array
		 *            also defines the amount of collumns
		 * @param titlebackgroundcolor
		 *            A HTML Color for the Title row Background. {@link http
		 *            ://www.w3.org/TR/html4/types.html#h-6.5} @
		 */
		public WikiTable(String[] fieldnames, String titlebackgroundcolor) {
			elements = new ArrayList<String[]>();
			names = fieldnames;
			fieldcount = names.length;
			titlecolor = titlebackgroundcolor;
		}

		/**
		 * Add a new row
		 * 
		 * @param content
		 *            A String array containing the Content of a table row. The
		 *            length of the Array must be the same as the length of the
		 *            fieldnames array used in the constructor
		 */
		public void add(String[] content) {
			if (content.length == fieldcount)
				elements.add(content);
		}

		/**
		 * Print the whole Wiki table code as string
		 * 
		 * @return Wiki table string
		 */
		public StringBuilder print() {

			StringBuilder output = new StringBuilder();
			output.append("{| class=\"wikitable\"  border=\"1\"").append(NEWLINE);
			for (int i = 0; i < names.length; i++)
				output.append("! style=\"background: ").append(titlecolor).append("\" | ").append(names[i]).append(NEWLINE);

			for (String[] row : elements) {
				output.append("|-").append(NEWLINE);
				output.append("| ");
				boolean first = false;
				for (String elem : row) {
					if (first)
						output.append("||");
					else
						first = !first;
					output.append(elem);

				}
				output.append(NEWLINE);
			}
			output.append("|}").append(NEWLINE);
			return output;
		}

	}

	/**
	 * Creates a new Wiki List containing the class descriptions supplied with
	 * three rows called Properties, Name and Description
	 * 
	 * @param Classdoc
	 *            Description Array
	 * @return Wiki Class Table String
	 */
	protected StringBuilder makeClassDocList(ClassDoc[] classes) {
		WikiTable classestable = new WikiTable(new String[] { "Properties", "Name", "Description" });

		for (int i = 0; i < classes.length; i++) {
			classestable.add(new String[] { code(new StringBuilder(classes[i].modifiers())).toString(),
					createWikiLink(new StringBuilder(classes[i].toString()), new StringBuilder(classes[i].typeName().toString())).toString(),
					createSmallComment(new StringBuilder(classes[i].commentText())).toString() });

		}
		return classestable.print();
	}

	/**
	 * Create a Title h1 (=Title=)
	 * 
	 * @param title
	 *            Title String
	 * @return H1 Title String in Wiki Syntax
	 */
	protected StringBuilder h1(StringBuilder title) {
		StringBuilder output = new StringBuilder();
		output.append("=").append(title).append("=");
		return output;
	}

	/**
	 * Create a Title h2 (==Title==)
	 * 
	 * @param title
	 *            Title String
	 * @return H2 Title String in Wiki Syntax
	 */
	protected StringBuilder h2(StringBuilder title) {
		return h1(h1(title));
	}

	/**
	 * Create a Title h3 (===Title===)
	 * 
	 * @param title
	 *            Title String
	 * @return H3 Title String in Wiki Syntax
	 */
	protected StringBuilder h3(StringBuilder title) {
		return h1(h1(h1(title)));
	}

	/**
	 * Code style Text (<tt>Code text</tt>
	 * 
	 * @param code
	 *            Text to be put in <tt> tags
	 * @return Code String in Wiki Syntax
	 */
	protected StringBuilder code(StringBuilder code) {
		StringBuilder output = new StringBuilder();
		output.append("<tt>").append(code).append("</tt>");
		return output;
	}

	/**
	 * Indent Text with Wiki Syntax. ( : text ) This Method will also do the
	 * same for all additional paragraphs in the text
	 * 
	 * @param text
	 *            Text to indent
	 * @return Code Text String in Wiki Syntax
	 */
	protected StringBuilder indent(StringBuilder text) {
		StringBuilder output = new StringBuilder();
		output.append(":").append(text.toString().replace("\n", "\n:")).append("\n");
		return output;
	}

	/**
	 * Mark Text as Bold ('''Text''')
	 * 
	 * @param text
	 *            Text to mark Bold
	 * @return Bold Text String in Wiki Syntax
	 */

	protected StringBuilder bold(StringBuilder text) {
		StringBuilder output = new StringBuilder();
		output.append("'''").append(text).append("'''");
		return output;
	}

	/**
	 * Filter Text as a simple one line text. Removes all \n and replaces all
	 * multiple spaces with single spaces
	 * 
	 * @param text
	 *            Text to be filtered
	 * @return Simple Text String
	 */
	protected StringBuilder simpleText(StringBuilder text) {
		StringBuilder output = new StringBuilder();
		output.append(text.toString().trim().replace("\n", "").replaceAll(" +", " "));
		return output;
	}
}
