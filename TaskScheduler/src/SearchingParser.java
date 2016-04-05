import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskDurationException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.KeywordNotEnteredException;
import Exceptions.ParserExceptions.SearchTypeNotEnteredException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;
import Exceptions.ParserExceptions.SearchNotInPairException;

public class SearchingParser {
	
	private static final String SEARCH_NAME = "name";
	private static final String SEARCH_DATE = "date";
	private static final String SEARCH_TIME = "time";
	private static final String SEARCH_DURATION = "duration";
	
	private static SearchingParser instance = null;
	
	private static final Logger logger = Logger.getLogger(SearchingParser.class.getName());
	
	public static SearchingParser getInstance() {
		if (instance == null) {
			instance = new SearchingParser();
		}
		return instance;
	}
	
	public SearchType findSearchType(String input) throws SearchTypeNotEnteredException {
		String[] tokens = Parser.divideTokens(input);
		assert(tokens!=null);
		// Get Arguments
		switch (tokens[1]) {
			case "name":
				return SearchType.NAME;
			case "date":
				return SearchType.DATE;
			default:
				throw new SearchTypeNotEnteredException();
		}
	}

	public String getNameForSearch(String input) throws KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException {
		assert(input!=null);
		String[] tokens = input.split(" ", 3);
		return tokens[2];
	}
	
	public Date getDateForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskDateException {
		assert(input!=null);
		String content = input.split(" ", 3)[2];
		return DateTime.getExactDate(content, 0);
	}
	
}
