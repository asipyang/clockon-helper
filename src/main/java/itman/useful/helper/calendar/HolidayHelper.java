package itman.useful.helper.calendar;

import itman.useful.helper.exception.ConnectionFailedException;
import itman.useful.helper.exception.EndEarlyException;
import itman.useful.helper.exception.UnexpectedException;
import itman.useful.helper.util.LoggerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class HolidayHelper {
	final String HOLIDAY_CALENDAR_API = "http://data.ntpc.gov.tw/od/data/api/7B7A8FD9-2722-4F17-B515-849E00073865?$format=json&$skip=360";
	Logger holidayLogger = LoggerUtil.getHolidayLogger();

	public void checkHoliday() throws UnexpectedException, ConnectionFailedException, EndEarlyException {
		HolidayRecord[] holidays = (HolidayRecord[]) doApiCall(HOLIDAY_CALENDAR_API, HolidayRecord[].class);
		SimpleDateFormat sdt = new SimpleDateFormat("yyyy/M/d");
		String today = sdt.format(new Date());

		for (HolidayRecord holiday : holidays) {
			if (today.equals(holiday.getDate()) && holiday.isHoliday()) {
				// holidayLogger.info(holiday.getHolidayCategory() + "; " + holiday.getDescription());
				throw new EndEarlyException(holiday.getHolidayCategory() + "; " + holiday.getDescription());
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object doApiCall(String url, Class returnClass) throws UnexpectedException, ConnectionFailedException {
		RestTemplate restTemplate = new RestTemplate();
		final int RETRY = 3;

		for (int i = 0; i <= RETRY; i++) {
			try {
				return restTemplate.getForObject(url, returnClass);
			} catch (Exception e) {
				holidayLogger.info("Calling holiday API failed after waiting " + 10 * i + " seconds. Retry " + (i + 1));
				holidayLogger.debug(e.getMessage());

				try {
					Thread.sleep(i * 10 * 1000);
				} catch (InterruptedException e1) {
					throw new UnexpectedException("Unexpected error when waiting for calling holiday API.", e);
				}
			}
		}
		throw new ConnectionFailedException("Failed to calling holiday API " + url);
	}
}
