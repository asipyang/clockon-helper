package itman.useful.helper;

import itman.useful.helper.calendar.HolidayHelper;
import itman.useful.helper.common.BrowserUsed;
import itman.useful.helper.common.ClockonState;
import itman.useful.helper.util.Config;
import itman.useful.helper.util.LoggerUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class ClockOnHelper {
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("./clock-on.properties");
		try {
			Config config = Config.getInstance();
			BrowserUsed browserUsed = config.getBrowserUsed();
			String url = config.getUrl();
			String name = config.getName();
			String password = config.getPassword();

			if (config.useHolidayCalendar()) {
				HolidayHelper holidayHelper = new HolidayHelper();
				if (holidayHelper.isHoliday()) {
					LoggerUtil.getHolidayLogger().info("Today is holiday.");
				} else {
					ClockOnHelper helper = new ClockOnHelper(browserUsed);
					helper.doClockOn(url, name, password);
				}
			} else {
				ClockOnHelper helper = new ClockOnHelper(browserUsed);
				helper.doClockOn(url, name, password);
			}

		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	final String STATE_KEY = "state";
	final String MSG_KEY = "msg";
	Logger clockonLogger = LoggerUtil.getClockonLogger();
	WebDriver driver;

	public ClockOnHelper() throws ConfigurationException {
		this(BrowserUsed.HtmlUnit);
	}

	public ClockOnHelper(BrowserUsed browserUsed) throws ConfigurationException {
		if (browserUsed == BrowserUsed.FireFox) {
			driver = new FirefoxDriver();
		} else {
			driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38, true);
		}
	}

	private Map<String, Object> checkClockonState(WebDriver clockOnWindowDriver) {
		Map<String, Object> result = new HashMap<String, Object>();

		WebDriverWait wait = new WebDriverWait(clockOnWindowDriver, 60);
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("my_info")));
		String style = element.getAttribute("style");
		// the "has done" message is displayed
		if (style.indexOf("none") < 0) {
			result.put(STATE_KEY, ClockonState.HAS_DONE);
			result.put(MSG_KEY, element.getText());
			return result;
		}

		// other error message is displayed
		List<WebElement> labels = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@id='my_msg']/label")));
		for (WebElement label : labels) {
			style = label.getAttribute("style");
			if (style.indexOf("none") < 0) {
				result.put(STATE_KEY, ClockonState.OTHER_ERROR);
				result.put(MSG_KEY, label.getText());
				return result;
			}
		}

		result.put(STATE_KEY, ClockonState.NO_PROCESS);
		result.put(MSG_KEY, "");
		return result;
	}

	private void clickAlert() {
		if (isAlertPresent()) {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}
	}

	public void doClockOn(String url, String name, String password) throws Exception {
		// wait 10 seconds at most for element finding.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		openUrl(url);

		login(name, password);

		// dismiss the "password is expired" alert.
		clickAlert();

		openClockOnPage();

		// switch to the clock on window and wait 10 seconds at most for element finding.
		WebDriver clockOnWindowDriver = driver.switchTo().window("_win68");
		clockOnWindowDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// select the clock-on option and submit.
		selectClockonOption(clockOnWindowDriver);

		// close the browser.
		driver.close();
	}

	private boolean isAlertPresent() {
		try {
			new WebDriverWait(driver, 15).until(ExpectedConditions.alertIsPresent());
		} catch (RuntimeException e) {
			return false;
		}
		return true;
	}

	private void login(String name, String password) {
		// type the name, password and click the submit button.
		driver.findElement(By.id("userid_input")).sendKeys(name);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("/html/body/form[@id='loginform']/table[2]/tbody/tr/td[2]/table/tbody/tr[3]/td[2]/div/a")).click();
	}

	private void openClockOnPage() throws Exception {
		WebElement targetItem = null;
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='ditch-tab-pane-wrap']/div/table/tbody/tr/td[2]/a"));

		for (WebElement element : list) {
			if (element.getText().equals("今日出勤班別")) {
				targetItem = element;
				break;
			}
		}

		if (targetItem == null) {
			throw new Exception("Can't find target element.");
		} else {
			if (driver instanceof HtmlUnitDriver) {
				Thread.sleep(2000); // this is waiting for the javascript is ready.
			}
			targetItem.click();
		}
	}

	private void openUrl(String url) {
		final int RETRY = 3;

		for (int i = 0; i <= RETRY; i++) {
			try {
				driver.get(url);
				(new WebDriverWait(driver, 10 * i)).until(ExpectedConditions.visibilityOfElementLocated(By.id("userid_input")));
				break;
			} catch (TimeoutException e) {
				clockonLogger.info("Connect failed after waiting " + 10 * i + " seconds. Retry " + (i + 1));
				if (i == RETRY) {
					throw e;
				}
			}
		}
	}

	private void selectClockonOption(WebDriver clockOnWindowDriver) throws InterruptedException {
		// check the current clock on state
		Map<String, Object> result = checkClockonState(clockOnWindowDriver);
		Integer currentState = (Integer) result.get(STATE_KEY);

		if (currentState.equals(ClockonState.NO_PROCESS)) {
			try {
				WebDriverWait wait = new WebDriverWait(clockOnWindowDriver, 60);
				WebElement selectedOption = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='data']/div[3]/label")));

				// select the option 3 and submit.
				selectedOption.click();
				clockOnWindowDriver.findElement(By.xpath("//div[@id='my_button']/div/div/input")).click();
			} catch (NoSuchElementException e) {
				clockonLogger.info(e.getMessage());
			}
		} else {
			clockonLogger.info(result.get(MSG_KEY));
		}
	}
}
