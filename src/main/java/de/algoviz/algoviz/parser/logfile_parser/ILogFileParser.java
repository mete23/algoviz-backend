package de.algoviz.algoviz.parser.logfile_parser;

import de.algoviz.algoviz.model.log_file.LogFile;
import de.algoviz.algoviz.parser.ParseException;

/**
 * This interface provides methods to parse a log file in string format to an instance of {@link LogFile}.
 *
 * @author Tim
 * @version 1.0
 */
public interface ILogFileParser {
    /**
     * This method parses a log file in string format to an instance of {@link LogFile}.
     *
     * @param logFileString the log file in string format
     * @return the log file in instance of {@link LogFile}
     * @throws ParseException the log file could not be parsed
     */
    LogFile parse(String logFileString) throws ParseException;
}
