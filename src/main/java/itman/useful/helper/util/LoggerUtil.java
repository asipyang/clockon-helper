package itman.useful.helper.util;

import org.apache.log4j.Logger;

public class LoggerUtil {
	private static final String CLOCKON_LOGGER = "helper.clockon";
	private static final String HOLIDAY_LOGGER = "helper.holiday";
	private static final String MAIL_LOGGER = "helper.mail";

	private static final Logger clockonLogger = Logger.getLogger(CLOCKON_LOGGER);
	private static final Logger holidayLogger = Logger.getLogger(HOLIDAY_LOGGER);
	private static final Logger mailLogger = Logger.getLogger(MAIL_LOGGER);

	public static Logger getClockonLogger() {
		return clockonLogger;
	}

	public static Logger getHolidayLogger() {
		return holidayLogger;
	}

	public static Logger getMailLogger() {
		return mailLogger;
	}
}
