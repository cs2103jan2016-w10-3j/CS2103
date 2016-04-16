package snaptask.parser.children;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskDurationException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.KeywordNotEnteredException;
import Exceptions.ParserExceptions.SearchNotInPairException;
import Exceptions.ParserExceptions.SearchTypeNotEnteredException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;
import snaptask.parser.DateTime;

//@@author A0148926R
public class SearchingParser {
	
	// Keyword string defined to search for name
	private static final String SEARCH_NAME = "name";
	
	// Keyword string defined to search for date
	private static final String SEARCH_DATE = "date";
	
	// Keyword string defined to search for time
	private static final String SEARCH_TIME = "time";
	
	// Keyword string defined to search for duration
	private static final String SEARCH_DURATION = "duration";
	
	// Parser instance
	private static SearchingParser instance = null;
	
	// Logger of logging
	private static final Logger logger = Logger.getLogger(SearchingParser.class.getName());
	
	public static SearchingParser getInstance() {
		if (instance == null) {
			instance = new SearchingParser();
		}
		return instance;
	}
	
	/**
	 * Get an ArrayList with all search types from input. 
	 * @param input Input entered.
	 * @return An ArrayList.
	 */
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
	
	/**
	 * Get string pairs with first one being type and the second one being content from input for searching.
	 * @param input Input entered.
	 * @return A String array with two elements.
	 * @throws SearchTypeNotEnteredException Search type is not entered.
	 * @throws SearchNotInPairException Either Keyword or content or neither are not entered.
	 */
	private String[] getPairsForSearching(String input) throws SearchTypeNotEnteredException, SearchNotInPairException {
		String substring;
		try {
			substring = input.split(" ", 2)[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new SearchTypeNotEnteredException();
		}
		// Split inputs into with two-word strings with first word being name, date, time or duration
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
	
	/**
	 * Get the content from input with given searching type.
	 * @param input Input entered.
	 * @param type Type to search for.
	 * @return Content for searching.
	 * @throws SearchTypeNotEnteredException Search type is not entered.
	 * @throws SearchNotInPairException Either Keyword or content or neither are not entered.
	 */
	private String getCorrespondingSearchContent(String input, String type) throws SearchTypeNotEnteredException, SearchNotInPairException {
		String[] pairs = getPairsForSearching(input);
		for (String pair: pairs) {
			if (pair.split(" ", 2)[0].equalsIgnoreCase(type)) {
				return pair.split(" ", 2)[1];
			}
		}
		return null;
	}
	
	/**
	 * Get the name for searching.
	 * @param input Input entered.
	 * @return Name keyword.
	 * @throws KeywordNotEnteredException Keyword is not entered.
	 * @throws SearchTypeNotEnteredException Search type is not entered.
	 * @throws SearchNotInPairException Either Keyword or type or neither are not entered.
	 */
	public String getNameForSearch(String input) throws KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException {
		assert(input!=null);
		return getCorrespondingSearchContent(input, SEARCH_NAME);
	}
	
	/**
	 * Get the time for searching.
	 * @param input Input entered.
	 * @return An integer array with hour and minute.
	 * @throws InvalidTaskTimeException Time with invalid time format is entered.
	 * @throws SearchTypeNotEnteredException Search type is not entered.
	 * @throws SearchNotInPairException Either Keyword or content or neither are not entered.
	 * @throws TaskTimeOutOfBoundException Time out of bound is entered.
	 */
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
	
	/**
	 * Get Date object for searching.
	 * @param input Input entered.
	 * @return Date for searching.
	 * @throws SearchTypeNotEnteredException Search type is not entered.
	 * @throws SearchNotInPairException Either Keyword or type or neither are not entered.
	 * @throws InvalidTaskDateException Time with invalid time format is entered.
	 */
	public Date getDateForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskDateException {
		assert(input!=null);
		String content = getCorrespondingSearchContent(input, SEARCH_DATE);
		return DateTime.getExactDate(content, 0);
	}
	
	/**
	 * Get the duration for searching.
	 * @param input
	 * @return Duration in minutes.
	 * @throws SearchTypeNotEnteredException Search type is not entered.
	 * @throws SearchNotInPairException Either Keyword or type or neither are not entered.
	 * @throws InvalidTaskTimeException Time with invalid time format is entered.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 */
	public int getDurationForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskTimeException, InvalidTaskDurationException {
		assert(input!=null);
		String content = getCorrespondingSearchContent(input, SEARCH_DURATION);
		return DateTime.getTotalMin(content);
	}
	
	
}
