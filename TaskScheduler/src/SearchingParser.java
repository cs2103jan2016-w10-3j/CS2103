import java.util.ArrayList;
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

//@@author JunWei
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
	
	public ArrayList<SearchType> findSearchTaskType(String input) {
		ArrayList<SearchType> toReturn = new ArrayList<SearchType>();
		if (input.contains(SEARCH_NAME)) {
			toReturn.add(SearchType.NAME);
		}
		if (input.contains(SEARCH_DATE)) {
			toReturn.add(SearchType.DATE);
		}
		if (input.contains(SEARCH_TIME)) {
			toReturn.add(SearchType.TIME);
		}
		if (input.contains(SEARCH_DURATION)) {
			toReturn.add(SearchType.DURATION);
		}
		return toReturn;
		
	}
	
	private String[] getPairsForSearching(String input) throws SearchTypeNotEnteredException, SearchNotInPairException {
		String substring;
		try {
			substring = input.split(" ", 2)[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new SearchTypeNotEnteredException();
		}
		String[] tokens = substring.split("(?= name| date| time| duration )");
		for (int i=0;i<tokens.length;i++) {
			tokens[i] = tokens[i].trim();
		}
		for (String token: tokens) {
			if (token.split(" ").length <= 1) {
				throw new SearchNotInPairException();
			}
		}
		return tokens;
	}
	
	private String getCorrespondingSearchContent(String input, String type) throws SearchTypeNotEnteredException, SearchNotInPairException {
		String[] pairs = getPairsForSearching(input);
		for (String pair: pairs) {
			if (pair.split(" ", 2)[0].equalsIgnoreCase(type)) {
				return pair.split(" ", 2)[1];
			}
		}
		return null;
	}
	
	public String getNameForSearch(String input) throws KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException {
		assert(input!=null);
		return getCorrespondingSearchContent(input, SEARCH_NAME);
	}
	
	public int[] getTimeForSearch(String input) throws InvalidTaskTimeException, SearchTypeNotEnteredException, SearchNotInPairException, TaskTimeOutOfBoundException {
		assert(input!=null);
		String timeString = getCorrespondingSearchContent(input, SEARCH_TIME);
		String[] timeTokens = DateTime.getTimeStringToken(timeString);
		int[] result = new int[2];
		int hr = DateTime.getTimeElement(timeTokens[0]);
		int min = DateTime.getTimeElement(timeTokens[1]);
		if (DateTime.hrOutOfBound(hr) || DateTime.minOutOfBound(min)) {
			TaskTimeOutOfBoundException e = new TaskTimeOutOfBoundException();
			logger.log(Level.SEVERE, e.toString(), e);
			throw e;
		}
		result[0] = hr;
		result[1] = min;
		return result;
	}
	
	public Date getDateForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskDateException {
		assert(input!=null);
		String content = getCorrespondingSearchContent(input, SEARCH_DATE);
		return DateTime.getExactDate(content, 0);
	}
	
	public int getDurationForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskTimeException, InvalidTaskDurationException {
		assert(input!=null);
		String content = getCorrespondingSearchContent(input, SEARCH_DURATION);
		return DateTime.getTotalMin(content);
	}
	
	
}
