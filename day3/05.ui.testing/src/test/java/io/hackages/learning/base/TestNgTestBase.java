package io.hackages.learning.base;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Capabilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import ru.stqa.selenium.factory.WebDriverPool;

/**
 * Base class for TestNG-based test classes
 */
public class TestNgTestBase {

  protected static URL gridHubUrl = null;
  protected static String baseUrl;
  protected static Capabilities capabilities;
  protected static Logger LOG;

  protected WebDriver driver;

  @BeforeSuite
  public void initTestSuite() throws IOException {
    LOG = LoggerFactory.getLogger("ui.testing");
    SuiteConfiguration config = new SuiteConfiguration();
    baseUrl = config.getProperty("site.url");
    if (config.hasProperty("grid.url") && !"".equals(config.getProperty("grid.url"))) {
      gridHubUrl = new URL(config.getProperty("grid.url"));
    }
    capabilities = config.getCapabilities();
  }

  @BeforeMethod
  public void initWebDriver() {
    switch (capabilities.getBrowserName()) {
      case "chrome" :
        WebDriverManager.chromedriver().setup();
        break;
      case "firefox" :
        WebDriverManager.firefoxdriver().setup();
        break;
    }
    driver = WebDriverPool.DEFAULT.getDriver(gridHubUrl, capabilities);
    driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MINUTES);
    driver.get(baseUrl);
  }

  @AfterSuite(alwaysRun = true)
  public void tearDown() {
    WebDriverPool.DEFAULT.dismissAll();
  }
}
