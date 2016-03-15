import Exceptions.ParserExceptions.KeywordNotEnteredException;

public class SearchingParser {
	
	private static SearchingParser instance = null;
	
	public static SearchingParser getInstance() {
		if (instance == null) {
			instance = new SearchingParser();
		}
		return instance;
	}
	
	public String getKeywordForSearch(String input) throws KeywordNotEnteredException {
		try {
		String[] tokens = input.split(" ", 2);
		return tokens[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new KeywordNotEnteredException();
		}
	}
}
