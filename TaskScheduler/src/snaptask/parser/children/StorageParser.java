package snaptask.parser.children;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.FileTypeInvalidException;
import snaptask.parser.Parser;
import Exceptions.ParserExceptions.FilePathInvalidException;

//@@author A0111720B
public class StorageParser {
	
	private static StorageParser instance = null;
	
	private static final Logger logger = Logger.getLogger(StorageParser.class.getName());
	
	public static StorageParser getInstance() {
		if (instance == null) {
			instance = new StorageParser();
		}
		return instance;
	}
	
	public StorageParserType findStorageParserType(String input) throws FileTypeInvalidException {
		String[] tokens = Parser.divideTokens(input);
		String commandType = tokens[1];
		switch(commandType) {
			case "changepath":
				return StorageParserType.CHANGEPATH;
			case "changename":
				return StorageParserType.CHANGENAME;
			case "readpath":
				return StorageParserType.READPATH;
			default:
				throw new FileTypeInvalidException();
		}
	}
	
	public String getPath(String input) throws FilePathInvalidException {
		String[] tokens = Parser.divideTokens(input);
		boolean test = canWrite(tokens[2]);
		if (test == false) {
			throw new FilePathInvalidException();
		}
		else {
			return tokens[2];
		}
	}
	
	public String getName(String input) {
		String[] tokens = Parser.divideTokens(input);
		return tokens[2];
	}
	
	public static boolean canWrite(String path) {
	    File file = new File(path);
	    if (!file.isDirectory()) {
	        return false;
	    }
	    return true;
	}
	
}