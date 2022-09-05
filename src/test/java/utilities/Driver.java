package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
                        try {
//                            URL url = new URL("http://18.212.57.48:4444/wd/hub");
                            URL url = new URL("http://3.87.98.87:4444/wd/hub");
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("chrome");
                            driverPool.set(new RemoteWebDriver(url, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "remote-firefox":
                        try {
//                            URL url = new URL("http://18.212.57.48:4444/wd/hub");
                            URL url = new URL("http://localhost:4444/wd/hub");
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("firefox");
                            driverPool.set(new RemoteWebDriver(url, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("start-maximized"); // open Browser in maximized mode
                        options.addArguments("disable-infobars"); // disabling infobars
                        options.addArguments("--disable-extensions"); // disabling extensions
                        options.addArguments("--disable-gpu"); // applicable to windows os only
                        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
                        options.addArguments("--no-sandbox"); // Bypass OS security model
                        WebDriver driver = new ChromeDriver(options);
                        driverPool.set(driver);
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver());
                        break;

                    case "chrome-headless":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        WebDriverManager.chromedriver().setup();
                        chromeOptions.setHeadless(true);
                        driverPool.set(new ChromeDriver(chromeOptions));

                    case "firefox-headless":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                        break;

                }

            }
        }
        driverPool.get().manage().window().maximize();
        driverPool.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //This same driver will be returned every time we call Driver.getDriver() method
        return driverPool.get();

    }

   /* public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }*/


    public static WebDriver setDriver(String browser) {

        if (driverPool.get() == null) {

            synchronized (Driver.class) {

                switch (browser) {

                    case "remote-chrome":
                        try {
//                            URL url = new URL("http://18.212.57.48:4444/wd/hub");
                            URL url = new URL("http://localhost:4444/wd/hub");
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("chrome");
                            driverPool.set(new RemoteWebDriver(url, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "remote-firefox":
                        try {
//                            URL url = new URL("http://18.212.57.48:4444/wd/hub");
                            URL url = new URL("http://localhost:4444/wd/hub");
                            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                            desiredCapabilities.setBrowserName("firefox");
                            driverPool.set(new RemoteWebDriver(url, desiredCapabilities));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver());
                        break;
                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver());
                        break;

                    case "chrome-headless":
                        ChromeOptions chromeOptions = new ChromeOptions();
                        WebDriverManager.chromedriver().setup();
                        chromeOptions.setHeadless(true);
                        driverPool.set(new ChromeDriver(chromeOptions));

                    case "firefox-headless":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                        break;
                }

            }
        }

        //This same driver will be returned every time we call Driver.getDriver() method
        return driverPool.get();

    }
}
