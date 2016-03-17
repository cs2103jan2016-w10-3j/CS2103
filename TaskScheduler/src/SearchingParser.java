import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.KeywordNotEnteredException;

public class SearchingParser {
	
	private static SearchingParser instance = null;
	
	private static final Logger logger = Logger.getLogger(SearchingParser.class.getName());
	
	public static SearchingParser getInstance() {
		if (instance == null) {
			instance = new SearchingParser();
		}
		return instance;
	}
	
	public String getKeywordForSearch(String input) throws KeywordNotEnteredException {
		assert(input!=null);
		try {
		String[] tokens = input.split(" ", 2);
		return tokens[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new KeywordNotEnteredException();
		}
	}
}
