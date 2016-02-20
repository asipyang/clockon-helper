package itman.useful.helper.calendar;

import itman.useful.helper.util.LoggerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class HolidayHelper {
	final String HOLIDAY_CALENDAR_API = "http://data.ntpc.gov.tw/od/data/api/7B7A8FD9-2722-4F17-B515-849E00073865?$format=json&$skip=360";
	Logger holidayLogger = LoggerUtil.getHolidayLogger();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object doApiCall(String url, Class returnClass) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		for (int retry = 0; retry < 3; retry++) {
			try {
				return restTemplate.getForObject(url, returnClass);
			} catch (HttpClientErrorException e) {
				holidayLogger.info(e.getMessage());
				break;
			} catch (RestClientException e) {
				holidayLogger.info(e.getMessage());
				return null;
			} catch (Exception e) {
				holidayLogger.info(e.getMessage());

				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e1) {
					holidayLogger.debug("The thread is interrupted.", e1);
				}
			}
		}
		throw new Exception("Calling internal API failed!");
	}

	public boolean isHoliday() throws Exception {
		HolidayRecord[] holidays = (HolidayRecord[]) doApiCall(HOLIDAY_CALENDAR_API, HolidayRecord[].class);
		SimpleDateFormat sdt = new SimpleDateFormat("yyyy/M/d");
		String today = sdt.format(new Date());

		for (HolidayRecord holiday : holidays) {
			if (today.equals(holiday.getDate()) && holiday.isHoliday()) {
				return true;
			}
		}

		return false;
	}
}
