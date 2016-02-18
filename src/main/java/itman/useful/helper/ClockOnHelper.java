package itman.useful.helper;

import itman.useful.helper.util.Config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClockOnHelper {
	public static void main(String[] args) throws Exception {
		try {
			Config config = Config.getInstance();
			String url = config.getUrl();
			String name = config.getName();
			String password = config.getPassword();

			new ClockOnHelper().doClockOn(url, name, password);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	WebDriver driver;

	public ClockOnHelper() throws ConfigurationException {
		driver = new FirefoxDriver();
		// driver = new HtmlUnitDriver();
	}

	private void clickAlert() {
		if (isAlertPresent()) {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		}
	}

	public void doClockOn(String url, String name, String password) throws Exception {
		final int RETRY = 3;

		// wait 10 seconds at most for element finding.
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// open the url.
		for (int i = 0; i <= RETRY; i++) {
			try {
				driver.get(url);
				(new WebDriverWait(driver, 10 * i)).until(ExpectedConditions.visibilityOfElementLocated(By.id("userid_input")));
				break;
			} catch (TimeoutException e) {
				System.out.println("Connect failed after waiting " + 10 * i + " seconds. Retry " + (i + 1));
				if (i == RETRY) {
					throw e;
				}
			}
		}

		login(name, password);

		// dismiss the "password is expired" alert.
		clickAlert();

		// open the clock-on page.
		openClockOnPage();

		// switch to the clock on window and wait 10 seconds at most for element finding.
		WebDriver clockOnWindow = driver.switchTo().window("_win68");
		clockOnWindow.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// select the option 3 and submit.
		clockOnWindow.findElement(By.xpath("//div[@id='data']/div[3]/label")).click();
		clockOnWindow.findElement(By.xpath("//div[@id='my_button']/div/div/input")).click();

		// close the browser.
		driver.quit();

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
	};

	private void openClockOnPage() throws Exception {
		WebElement targetItem = null;
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='ditch-tab-pane-wrap']/div/table/tbody/tr/td[2]/a"));

		for (WebElement element : list) {
			if (element.getText().equals("今日出勤班別")) {
				targetItem = element;
			}
		}

		if (targetItem == null) {
			throw new Exception("Can't find target element.");
		} else {
			targetItem.click();
		}
	}
}
