package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.bonigarcia.wdm.WebDriverManager.*;

public class Driver {

private Driver() {
}

private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

public static WebDriver get() {

    if (driverPool.get() == null) {

        synchronized (Driver.class) {

            String browser = ConfigurationReader.get("browser");

            switch (browser) {

                case "remote-chrome":
                    driverPool.set(createRemoteWebDriver("chrome"));
                    break;

                case "remote-firefox":
                    driverPool.set(createRemoteWebDriver("firefox"));
                    break;

                case "chrome":
                    driverPool.set(createChromeDriver());
                    break;

                case "firefox":
                    driverPool.set(createFirefoxDriver());
                    break;

                case "chrome-headless":
                    driverPool.set(createHeadlessChromeDriver());
                    break;

                case "firefox-headless":
                    driverPool.set(createHeadlessFirefoxDriver());
                    break;

            }

        }
    }
    return driverPool.get();

}

public static void closeDriver() {
    if (driverPool.get() != null) {
        driverPool.get().quit();
        driverPool.remove();
    }
}

private static WebDriver createRemoteWebDriver(String browser) {
    URL url;
    if (browser.equals("chrome")) {
        url = getRemoteWebDriverUrl("http://3.87.98.87:4444/wd/hub");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName("chrome");
        return new RemoteWebDriver(url, desiredCapabilities);
    } else if (browser.equals("firefox")) {
        url = getRemoteWebDriverUrl("http://localhost:4444/wd/hub");
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName("firefox");
        return new RemoteWebDriver(url, desiredCapabilities);
    }
    return null;
}

private static URL getRemoteWebDriverUrl(String url) {
    try {
        return new URL(url);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    return null;
}

private static WebDriver createChromeDriver() {
    chromedriver().driverVersion("111.0.5563.64").setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("start-maximized"); // open Browser in maximized mode
    return new ChromeDriver(options);
}

private static WebDriver createFirefoxDriver() {
    firefoxdriver().setup();
    return new FirefoxDriver();
}

private static WebDriver createHeadlessChromeDriver() {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromedriver().setup();
    chromeOptions.setHeadless(true);
    return new ChromeDriver(chromeOptions);
}

private static WebDriver createHeadlessFirefoxDriver() {
    firefoxdriver().setup();
    return new FirefoxDriver(new FirefoxOptions().setHeadless(true));
}
}