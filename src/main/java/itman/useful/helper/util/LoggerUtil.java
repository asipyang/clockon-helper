package itman.useful.helper.util;

import org.apache.log4j.Logger;

public class LoggerUtil {
	private static final String CLOCKON_LOGGER = "helper.clockon";
	private static final String HOLIDAY_LOGGER = "helper.clockon";

	private static final Logger clockonLogger = Logger.getLogger(CLOCKON_LOGGER);
	private static final Logger holidayLogger = Logger.getLogger(HOLIDAY_LOGGER);

	public static Logger getClockonLogger() {
		return clockonLogger;
	}

	public static Logger getHolidayLogger() {
		return holidayLogger;
	}

}
