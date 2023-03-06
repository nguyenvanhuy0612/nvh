package com.avaya.outbound.lib;

import com.avaya.outbound.lib.support.Locator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class EnvSetup {
    private static final Logger log = LogManager.getLogger(EnvSetup.class);
    public static String TESTCASE_START_TIME = "";
    //OUTBOUND URL details
    public static String OUTBOUND_SERVER = "";
    public static String OUTBOUND_PORT = "";
    public static String OUTBOUND_CAMPAIGNS_UI_BASEURL = "";
    @Deprecated
    public static String SCENARIO_NAME = "";
    @Deprecated
    public static String SCENARIO_JIRAID = "";
    public static String DEFAULT_USERNAME = "";
    public static String DEFAULT_PASSWORD = "";
    public static String BUILD_VERSION;
    public static String OUTBOUND_CONTACTLIST_SERVER = "";
    public static String OUTBOUND_CONTACTLIST_PORT = "";
    public static String OUTBOUND_CONTACTS_UI_BASEURL = "";
    // BD details
    public static String OUTBOUND_CAMPAIGNS_API_BASEURL = "";
    public static String DB_HOST = "";
    public static String DB_NAME = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";
    public static String DB_PORT = "";
    public static String DB_TYPE = "";
    public static String DBVersion = "";
    public static String DB_CONTACTLIST_SERVER = "";
    public static String DB_CONTACTLIST_PORT = "";
    public static String OUTBOUND_CONTACTS_API_BASEURL = "";
    // Browser details
    public static String BROWSER_NAME = "";
    public static String DEFAULT_BROWSER_NAME = "";
    public static String DRIVER_PATH = "";
    public static WebDriver WEBDRIVER;
    public static WebDriver WEBDRIVER1;
    public static WebDriver WEBDRIVER2;
    public static WebDriver WEBDRIVER3;
    public static volatile ThreadLocal<WebDriver> driverThreading = new ThreadLocal<>();
    public static final ConcurrentHashMap<Thread, WebDriver> driverMaps = new ConcurrentHashMap<>();
    public static int ImplicitWait = 60;
    public static int ExplicitWait = 120;
    // Suite path
    public static String SUITE_PATH = System.getProperty("user.dir") + "/";
    public final static Map<String, Properties> locatorMap = Locator.listPropFilesForFolder(SUITE_PATH + "src/test/resources/locator/");
    // Test Data path
    public static String TEST_DATA_PATH = SUITE_PATH + "src/test/resources/data/";
    public static String UTILITIES_PATH = SUITE_PATH + "src/test/java/com/avaya/outbound/utilities/";
    public static String LOCATOR_FOLDER_PATH = SUITE_PATH + "src/test/resources/locator/";
    // chrome download path
    public static String CHROME_DOWNLOAD_PATH = SUITE_PATH + "chrome_download/";
    // Log4j property file location
    public static String LOGGER_PROPERTY_FILE = SUITE_PATH + "src/test/resources/config/log4j2_configure.xml";
    // Screen Shot path details
    public static String SNAP_FOLDER_PATH = SUITE_PATH + "target/surefire-reports/html/";
    public static String SNAP_IMAGE_WIDTH = "";
    public static String SNAP_IMAGE_HEIGHT = "";
    // Log Folder Path
    public static String LOG_FOLDER_PATH = SUITE_PATH + "log/";
    public static boolean IS_ENVIRONMENT_READY = false;
    public static String USER_PROPERTY_FILE_PATH = "";
    // JIRA
    public static String TESTCYCLE;
    public static String RELEASE;
    public static String JIRAUSER;
    public static String JIRAPASSWORD;
    public static String CYCLEID;
    public static String FOLDERID;
    public static String FOLDERNAME;
    public static String RELEASEID;
    public static String PROJECTID;
    public static String UPDATEZEPHYR;
    public static String CREATEJIRA;
    public static String USEEPICCYCLE;
    public static String GRID_HUB;
    public static String SFTP_HOSTNAME;
    public static String SFTP_PASSWORD;
    public static String SFTP_USERNAME;
    public static String SFTP_REMOTE_FILEPATH;
    public static String NGM_URL;
    public static String NGM_USER;
    public static String NGM_PASSWORD;
    public static String USENGM;
    private static String REMOTEDRIVER;
    private static String ENABLENETWORKBROWSER;
    public static String TEST_CLEAN_UP = "no";
    public static String KEEP_BROWSER_ACROSS_TESTS = "no";
    public static volatile ThreadLocal<String> ssoToken = new ThreadLocal<>();

    public EnvSetup() {
        IS_ENVIRONMENT_READY = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        Date date = new Date();
        TESTCASE_START_TIME = dateFormat.format(date);
        UtilityFun utilityFun = new UtilityFun();
        String configFile = System.getProperty("CONFIG_FILE", "");
        if (configFile.isEmpty()) {
            USER_PROPERTY_FILE_PATH = SUITE_PATH + "src/test/resources/config/" + utilityFun.readPropertyValue("CONFIG_FILE",
                    SUITE_PATH + "src/test/resources/config/default.properties");
        } else {
            USER_PROPERTY_FILE_PATH = SUITE_PATH + "src/test/resources/config/" + configFile;
        }
        System.out.println("Properties File Path: " + EnvSetup.USER_PROPERTY_FILE_PATH);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Properties prop = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get(USER_PROPERTY_FILE_PATH))) {
            prop.load(input);
            prop.entrySet().removeIf(e -> e.getValue().toString().matches("[`'\"]\\s*[`'\"]|\\s+|[`'\"]\\s*|\\s*[`'\"]|^$"));
            prop.putAll(System.getProperties());
        } catch (Exception e) {
            System.out.println("Exception when load properties file");
            e.printStackTrace();
        }
        // OUTBOUND details
        OUTBOUND_CAMPAIGNS_UI_BASEURL = prop.getProperty("OUTBOUND_CAMPAIGNS_UI_BASEURL", "");
        OUTBOUND_CAMPAIGNS_UI_BASEURL = OUTBOUND_CAMPAIGNS_UI_BASEURL.matches("(?i)^https?://.*") ? OUTBOUND_CAMPAIGNS_UI_BASEURL : "https://" + OUTBOUND_CAMPAIGNS_UI_BASEURL;

        OUTBOUND_SERVER = prop.getProperty("OUTBOUND_SERVER", "");
        if (OUTBOUND_SERVER.isEmpty()) {
            OUTBOUND_SERVER = extractHostname(OUTBOUND_CAMPAIGNS_UI_BASEURL);
        }
        OUTBOUND_PORT = prop.getProperty("OUTBOUND_SERVER", "");
        if (OUTBOUND_PORT.isEmpty()) {
            OUTBOUND_PORT = extractPort(OUTBOUND_CAMPAIGNS_UI_BASEURL);
        }

        // Contact list UI
        OUTBOUND_CONTACTS_UI_BASEURL = prop.getProperty("OUTBOUND_CONTACTS_UI_BASEURL", "");
        OUTBOUND_CONTACTS_UI_BASEURL = OUTBOUND_CONTACTS_UI_BASEURL.matches("(?i)^https?://.*") ? OUTBOUND_CONTACTS_UI_BASEURL : "https://" + OUTBOUND_CONTACTS_UI_BASEURL;
        OUTBOUND_CONTACTLIST_SERVER = extractHostname(OUTBOUND_CONTACTS_UI_BASEURL);
        OUTBOUND_CONTACTLIST_PORT = extractPort(OUTBOUND_CONTACTS_UI_BASEURL);

        BUILD_VERSION = prop.getProperty("BuildVersion", "");
        DEFAULT_USERNAME = prop.getProperty("DefaultUsername", "");
        DEFAULT_PASSWORD = prop.getProperty("DefaultPassword", "");
        // Database details
        OUTBOUND_CAMPAIGNS_API_BASEURL = prop.getProperty("OUTBOUND_CAMPAIGNS_API_BASEURL", "");
        OUTBOUND_CAMPAIGNS_API_BASEURL = OUTBOUND_CAMPAIGNS_API_BASEURL.matches("(?i)^https?://.*") ? OUTBOUND_CAMPAIGNS_API_BASEURL : (OUTBOUND_CAMPAIGNS_API_BASEURL.isEmpty() ? "" : "https://" + OUTBOUND_CAMPAIGNS_API_BASEURL);

        DB_HOST = prop.getProperty("DB_HOST", "");
        if (DB_HOST.isEmpty()) {
            DB_HOST = extractHostname(OUTBOUND_CAMPAIGNS_API_BASEURL);
        }
        DB_PORT = prop.getProperty("DB_PORT", "");
        if (DB_PORT.isEmpty()) {
            DB_PORT = extractPort(OUTBOUND_CAMPAIGNS_API_BASEURL);
        }
        // Contact list DB
        OUTBOUND_CONTACTS_API_BASEURL = prop.getProperty("OUTBOUND_CONTACTS_API_BASEURL", "");
        OUTBOUND_CONTACTS_API_BASEURL = OUTBOUND_CONTACTS_API_BASEURL.matches("(?i)^https?://.*") ? OUTBOUND_CONTACTS_API_BASEURL : "https://" + OUTBOUND_CONTACTS_API_BASEURL;
        DB_CONTACTLIST_SERVER = extractHostname(OUTBOUND_CONTACTS_API_BASEURL);
        DB_CONTACTLIST_PORT = extractPort(OUTBOUND_CONTACTS_API_BASEURL);

        DB_NAME = prop.getProperty("DB_NAME", "");
        DB_USER = prop.getProperty("DB_USER", "");
        DB_PASSWORD = prop.getProperty("DB_PASSWORD", "");
        DB_TYPE = prop.getProperty("DB_TYPE", "");
        DBVersion = prop.getProperty("DBVersion", "");

        // Browser details
        BROWSER_NAME = prop.getProperty("Browser", "FIREFOX");
        DEFAULT_BROWSER_NAME = BROWSER_NAME;
        DRIVER_PATH = prop.getProperty("DriverPath", SUITE_PATH + "src/test/resources/drivers/");
        DRIVER_PATH = DRIVER_PATH.matches(".*[/\\\\]$") ? DRIVER_PATH : DRIVER_PATH + "\\";
        // Error Snapshot Details
        SNAP_IMAGE_WIDTH = prop.getProperty("ImageWidth", "");
        SNAP_IMAGE_HEIGHT = prop.getProperty("ImageHeight", "");
        // Jira details
        TESTCYCLE = prop.getProperty("TESTCYCLE", "");
        RELEASE = prop.getProperty("RELEASE", "");
        JIRAUSER = prop.getProperty("JIRAUSER", "");
        JIRAPASSWORD = prop.getProperty("JIRAPASSWORD", "");
        CYCLEID = prop.getProperty("CYCLEID", "");
        FOLDERID = prop.getProperty("FOLDERID", "");
        FOLDERNAME = prop.getProperty("FOLDERNAME", "");
        RELEASEID = prop.getProperty("RELEASEID", "");
        PROJECTID = prop.getProperty("PROJECTID", "");
        UPDATEZEPHYR = prop.getProperty("UPDATEZEPHYR", "");
        CREATEJIRA = prop.getProperty("CREATEJIRA", "no");
        GRID_HUB = prop.getProperty("GRID_HUB", "");
        USEEPICCYCLE = prop.getProperty("USEEPICCYCLE", "no");
        SFTP_HOSTNAME = prop.getProperty("SFTP_HOSTNAME", "no");
        SFTP_PASSWORD = prop.getProperty("SFTP_PASSWORD", "no");
        SFTP_USERNAME = prop.getProperty("SFTP_USERNAME", "no");
        REMOTEDRIVER = prop.getProperty("REMOTEDRIVER", "localhost:4444");
        SFTP_REMOTE_FILEPATH = prop.getProperty("SFTP_REMOTE_FILEPATH", "/");
        ENABLENETWORKBROWSER = prop.getProperty("ENABLENETWORKBROWSER", "no");
        NGM_URL = prop.getProperty("NGM_URL", "dev-3.ixcc-sandbox.avayacloud.com");
        NGM_USER = prop.getProperty("NGM_USER", "admin@uc3dev01.dev3.com");
        NGM_PASSWORD = prop.getProperty("NGM_PASSWORD", "Avaya@123$");
        USENGM = prop.getProperty("USENGM", "no");
        TEST_CLEAN_UP = prop.getProperty("TEST_CLEAN_UP", "no");
        KEEP_BROWSER_ACROSS_TESTS = prop.getProperty("KEEP_BROWSER_ACROSS_TESTS", "no");
    }

    public static String extractHostname(String serverURL) {
        return serverURL.replaceAll("(?i)^(?:https?://)?(?:www\\.)?([^:\\\\/\\s]+)(?::(\\d{1,8}))?.*", "$1");
    }

    public static String extractPort(String serverURL) {
        return serverURL.replaceAll("(?i)^(?:https?://)?(?:www\\.)?([^:\\\\/\\s]+)(?::(\\d{1,8}))?.*", "$2");
    }

    public static WebDriver createSafariDriver() {
        return new SafariDriver();
    }


    public static WebDriver createChromeDriver() {
        try {
            return createChromeDriver("nonDriver");
        } catch (Exception e) {
            System.out.println("There is an exception when initializing the driver by automatically downloading the browser driver. " +
                    "The problem may be no network connection or network connection by proxy but auto downloader doesn't support proxy.");
            System.out.println("Try initializing the browser with the available driver.");
            System.out.println(e.getMessage());
            return createChromeDriver("default");
        }
    }

    public static WebDriver createChromeDriver(String driverPath) {
        Objects.requireNonNull(driverPath);
        if (driverPath.equalsIgnoreCase("default")) {
            System.setProperty("webdriver.chrome.driver", new File(EnvSetup.DRIVER_PATH + "chromedriver.exe").getAbsolutePath());
        } else if (driverPath.equalsIgnoreCase("remove") || driverPath.equalsIgnoreCase("nonDriver")) {
            System.clearProperty("webdriver.chrome.driver");
        } else if (!driverPath.isEmpty() && new File(driverPath).exists()) {
            System.setProperty("webdriver.chrome.driver", new File(driverPath).getAbsolutePath());
        }
        Map<String, String> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", "true");
        prefs.put("download.default_directory", CHROME_DOWNLOAD_PATH);
        prefs.put("download.extensions_to_open", "pdf");
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("test-type");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("use-fake-device-for-media-stream");
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-features=WebRtcHideLocalIpsWithMdns");
        // disable Chrome popups
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("excludeSwitches");
        //enable network in chrome browser to get api info
        if (ENABLENETWORKBROWSER.equalsIgnoreCase("yes")) {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability(ChromeOptions.CAPABILITY, options);
            // this sends Network.enable to chromedriver
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("goog:loggingPrefs", logPrefs);
        }
        return new ChromeDriver(options);
    }


    public static WebDriver createFirefoxDriver() {
        try {
            return createFirefoxDriver("nonDriver");
        } catch (Exception e) {
            System.out.println("There is an exception when initializing the driver by automatically downloading the browser driver. " +
                    "The problem may be no network connection or network connection by proxy but auto downloader doesn't support proxy.");
            System.out.println("Try initializing the browser with the available driver.");
            System.out.println(e.getMessage());
            return createFirefoxDriver("default");
        }
    }

    public static WebDriver createFirefoxDriver(String driverPath) {
        Objects.requireNonNull(driverPath);
        if (driverPath.equalsIgnoreCase("default")) {
            System.setProperty("webdriver.gecko.driver", new File(EnvSetup.DRIVER_PATH + "geckodriver.exe").getAbsolutePath());
        } else if (driverPath.equalsIgnoreCase("remove") || driverPath.equalsIgnoreCase("nonDriver")) {
            System.clearProperty("webdriver.gecko.driver");
        } else if (!driverPath.isEmpty() && new File(driverPath).exists()) {
            System.setProperty("webdriver.gecko.driver", new File(driverPath).getAbsolutePath());
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("marionette", true);
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        FirefoxOptions options = new FirefoxOptions();
        options.merge(capabilities);
        options.addPreference("permissions.default.microphone", 1);
        options.addPreference("permissions.default.camera", 1);
        options.setProfile(profile);
        return new FirefoxDriver(options);
    }
    public static WebDriver createIEDriver() {
        try {
            return createIEDriver("nonDriver");
        } catch (Exception e) {
            System.out.println("There is an exception when initializing the driver by automatically downloading the browser driver. " +
                    "The problem may be no network connection or network connection by proxy but auto downloader doesn't support proxy.");
            System.out.println("Try initializing the browser with the available driver.");
            System.out.println(e.getMessage());
            return createIEDriver("default");
        }
    }

    public static WebDriver createIEDriver(String driverPath) {
        Objects.requireNonNull(driverPath);
        if (driverPath.equalsIgnoreCase("default")) {
            System.setProperty("webdriver.ie.driver", new File(EnvSetup.DRIVER_PATH + "IEDriverServer.exe").getAbsolutePath());
        } else if (driverPath.equalsIgnoreCase("remove") || driverPath.equalsIgnoreCase("nonDriver")) {
            System.clearProperty("webdriver.ie.driver");
        } else if (!driverPath.isEmpty() && new File(driverPath).exists()) {
            System.setProperty("webdriver.ie.driver", new File(driverPath).getAbsolutePath());
        }
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        options.setCapability("acceptSslCerts", true);
        options.setAcceptInsecureCerts(true);
        return new InternetExplorerDriver(options);
    }


    public static void addAllBrowserSetup(WebDriver driver) {
        //Set implicit time for Web Elements to be available
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ImplicitWait));
        // Set script execution timeout
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(ImplicitWait));
        if (!BROWSER_NAME.equalsIgnoreCase("safari")) {
            // Set page load timeout
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ExplicitWait));
        }
        // Maximize the browser window
        driver.manage().window().maximize();
    }


    public static WebDriver createFirefoxGrid() {
        // pending
        System.out.println("Instantiate Firefox GRID");
        String nodeUrl = "http://" + EnvSetup.REMOTEDRIVER + "/wd/hub";
        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setBrowserName("firefox");
        capability.setCapability("idleTimeout", 500);
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        FirefoxOptions options = new FirefoxOptions().merge(capability);
        options.setProfile(profile);
        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(nodeUrl), options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return driver;
    }

    public static WebDriver createChromeGrid() {
        String nodeUrl = "http://" + EnvSetup.REMOTEDRIVER + "/wd/hub";
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setBrowserName("chrome");
        ChromeOptions options = new ChromeOptions().merge(cap);
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("test-type");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("use-fake-device-for-media-stream");
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-features=WebRtcHideLocalIpsWithMdns");
        RemoteWebDriver driver;
        log.info("Instantiate Chrome GRID (" + nodeUrl + "," + options.toString());
        try {
            driver = new RemoteWebDriver(new URL(nodeUrl), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return driver;
    }

    public static WebDriver createEdgeDriver() {
        try {
            return createEdgeDriver("nonDriver");
        } catch (Exception e) {
            System.out.println("There is an exception when initializing the driver by automatically downloading the browser driver. " +
                    "The problem may be no network connection or network connection by proxy but auto downloader doesn't support proxy.");
            System.out.println("Try initializing the browser with the available driver.");
            System.out.println(e.getMessage());
            return createEdgeDriver("default");
        }
    }

    public static WebDriver createEdgeDriver(String driverPath) {
        Objects.requireNonNull(driverPath);
        if (driverPath.equalsIgnoreCase("default")) {
            System.setProperty("webdriver.edge.driver", new File(EnvSetup.DRIVER_PATH + "msedgedriver.exe").getAbsolutePath());
        } else if (driverPath.equalsIgnoreCase("remove") || driverPath.equalsIgnoreCase("nonDriver")) {
            System.clearProperty("webdriver.edge.driver");
        } else if (!driverPath.isEmpty() && new File(driverPath).exists()) {
            System.setProperty("webdriver.edge.driver", new File(driverPath).getAbsolutePath());
        }
        EdgeOptions options = new EdgeOptions();
        options.setCapability(org.openqa.selenium.remote.CapabilityType.ACCEPT_INSECURE_CERTS, true);
        List<String> args = Arrays.asList("use-fake-ui-for-media-stream", "use-fake-device-for-media-stream",
                "test-type", "--disable-web-security", "--allow-running-insecure-content",
                "disable-features=WebRtcHideLocalIpsWithMdns", "disable-infobars",
                "disable-extensions", "disable-notifications", "excludeSwitches");
        Map<String, Object> map = new HashMap<>();
        map.put("args", args);
        options.setCapability("ms:edgeOptions", map);
        return new EdgeDriver(options);
    }

    public static WebDriver createChromeHeadless() {
        try {
            return createChromeHeadless("nonDriver");
        } catch (Exception e) {
            System.out.println("There is an exception when initializing the driver by automatically downloading the browser driver. " +
                    "The problem may be no network connection or network connection by proxy but auto downloader doesn't support proxy.");
            System.out.println("Try initializing the browser with the available driver.");
            System.out.println(e.getMessage());
            return createChromeHeadless("default");
        }
    }

    public static WebDriver createChromeHeadless(String driverPath) {
        Objects.requireNonNull(driverPath);
        if (driverPath.equalsIgnoreCase("default")) {
            System.setProperty("webdriver.chrome.driver", new File(EnvSetup.DRIVER_PATH + "chromedriver.exe").getAbsolutePath());
        } else if (driverPath.equalsIgnoreCase("remove") || driverPath.equalsIgnoreCase("nonDriver")) {
            System.clearProperty("webdriver.chrome.driver");
        } else if (!driverPath.isEmpty() && new File(driverPath).exists()) {
            System.setProperty("webdriver.chrome.driver", new File(driverPath).getAbsolutePath());
        }
        Map<String, String> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", "true");
        prefs.put("download.default_directory", CHROME_DOWNLOAD_PATH);
        prefs.put("download.extensions_to_open", "pdf");
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("test-type");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("use-fake-device-for-media-stream");
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("disable-features=WebRtcHideLocalIpsWithMdns");
        // disable Chrome popups
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("excludeSwitches");
        options.addArguments("window-size=1920,1080");
        // headless
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    public static WebDriver setupDriverInstance() {
        if (!KEEP_BROWSER_ACROSS_TESTS.equalsIgnoreCase("yes")) {
            return setupDriverInstance(BROWSER_NAME);
        }
        WebDriver webDriver = driverThreading.get();
        if (webDriver != null && !webDriver.toString().contains("null")) {
            return webDriver;
        }
        webDriver = setupDriverInstance(BROWSER_NAME);
        driverThreading.remove();
        driverThreading.set(webDriver);
        driverMaps.put(Thread.currentThread(), webDriver);
        return webDriver;
    }

    public static WebDriver setupDriverInstance(String browserName) {
        WebDriver driver;
        Browsers browser = Browsers.browserForName(browserName);
        switch (browser) {
            case FIREFOX:
                driver = createFirefoxDriver();
                break;
            case IE:
                driver = createIEDriver();
                break;
            case CHROME:
                driver = createChromeDriver();
                break;
            case FIREFOXGRID:
                driver = createFirefoxGrid();
                break;
            case CHROMEGRID:
                driver = createChromeGrid();
                break;
            case CHROMEHEADLESS:
                driver = createChromeHeadless();
                break;
            case EDGE:
                driver = createEdgeDriver();
                break;
            default:
                BROWSER_NAME = "FIREFOX";
                driver = createFirefoxDriver();
                break;
        }
        addAllBrowserSetup(driver);
        return driver;
    }

    public enum Browsers {
        SAFARI,
        FIREFOX,
        CHROME,
        CHROMEHEADLESS,
        EDGE,
        IE,
        NOBROWSER,
        FIREFOXGRID,
        CHROMEGRID,
        IEGRID,
        GRID;

        public static Browsers browserForName(String browser) throws IllegalArgumentException {
            for (Browsers b : values()) {
                if (b.toString().equalsIgnoreCase(browser)) {
                    return b;
                }
            }
            return NOBROWSER;
        }
    }

}
