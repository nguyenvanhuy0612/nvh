package com.avaya.outbound.lib;


import com.avaya.outbound.frame.RestMethods;
import com.avaya.outbound.lib.support.TestData;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.jcraft.jsch.*;
import com.opencsv.CSVWriter;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;


public class UtilityFun {
    private static final Logger log = LogManager.getLogger(UtilityFun.class);
    public static Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

    /**
     * Method to verify time fomater basic is within last 5 mins
     *
     * @param createdOn
     */
    public static void compareTimeBasicFomatter(String createdOn) {
        log.info("----------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Instant currentTimeInUTC = Instant.now().plus(2, ChronoUnit.MINUTES);
        Instant currentTimeInUTCPrv = Instant.now().minus(5, ChronoUnit.MINUTES);
        Date actualdate;
        SimpleDateFormat dateformatter;
        try {
            dateformatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            dateformatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            actualdate = dateformatter.parse(createdOn);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        log.info("Current Time in UTC " + currentTimeInUTC);
        log.info("Current Time - 5 min in UTC " + currentTimeInUTCPrv);
        log.info("Actual last updated Time in UTC " + actualdate.toInstant());
        Assert.assertTrue(currentTimeInUTC.isAfter(actualdate.toInstant()));
        Assert.assertTrue(currentTimeInUTCPrv.isBefore(actualdate.toInstant()));
        log.info("----------------------------------------------------------");
    }

    /**
     * @param scenario
     */
    public static synchronized void writeTestResult(Scenario scenario) {
        log.info("----------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            String sEdate = dateFormat.format(date);
            Date d1 = dateFormat.parse(EnvSetup.TESTCASE_START_TIME);
            Date d2 = dateFormat.parse(sEdate);
            long diff = Math.abs(d1.getTime() - d2.getTime());
            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            df.setTimeZone(tz);
            String diffEtime = df.format(new Date(diff));
            String sData = scenario.getName() + "," + EnvSetup.TESTCASE_START_TIME + "," + dateFormat.format(date) + ","
                    + diffEtime + "," + scenario.getStatus();
            File filename = new File(EnvSetup.SUITE_PATH + "target/logs/Result.csv");
            log.info("FileName -" + filename);
            FileWriter fw = new FileWriter(filename, true);
            fw.write(sData + "\n");// appends the string to the file
            fw.close();
        } catch (Exception e) {
            System.out.println("Exception when write resut to csv file " + e.getMessage());
        }
    }

    public void findJsonElement(JsonElement jsonElement, String key, List<JsonElement> result) {
        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                findJsonElement(jsonElement1, key, result);
            }
        } else if (jsonElement.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                if (entry.getKey().equals(key)) {
                    result.add(entry.getValue());
                }
                findJsonElement(entry.getValue(), key, result);
            }
        } else {
            if (jsonElement.toString().equals(key)) {
                result.add(jsonElement);
            }
        }
    }

    //////////////////////////////////////////////  WRITE  /////////////////////////////////////////////////////////////
    public void writeObjectToFile(Object obj, String fileNamePath) {
        try (Writer writer = new FileWriter(fileNamePath)) {
            // 1. Java object to JSON file
            gson.toJson(obj, writer);
            // 2. Java object to JSON string
            // String json = gson.toJson(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJsonElementToFile(JsonElement jsonElement, String fileNamePath) {
        try (Writer writer = new FileWriter(fileNamePath)) {
            // write json element to file fileNamePath
            gson.toJson(jsonElement, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJsonObjectToFile(JsonObject jsonObject, String fileNamePath) {
        try (Writer writer = new FileWriter(fileNamePath)) {
            // write json object to file fileNamePath
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////  READ  /////////////////////////////////////////////////////////////
    public JsonElement readFileToJsonElement(String fileNamePath) {
        try {
            return gson.fromJson(new FileReader(fileNamePath), JsonElement.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonElement parseFileToJson(String fileNamePath) {
        try {
            return JsonParser.parseReader(new FileReader(fileNamePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JsonObject parseFileToJsonObject(String fileNamePath) {
        log.info("fileNamePath: " + fileNamePath);
        try {
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(fileNamePath));
            if (jsonElement.isJsonObject())
                return jsonElement.getAsJsonObject();
            else if (jsonElement.isJsonArray() | jsonElement.isJsonPrimitive()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("data", jsonElement);
                return jsonObject;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Object> jsonObjectToMap(JsonObject jsonObject) {
        return gson.fromJson(jsonObject, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public HashMap<String, Object> jsonStringToMap(String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public JsonElement parseStrToJson(String jsonData) {
        return JsonParser.parseString(jsonData);
    }

    public JsonElement stringToJsonElement(String jsonString) {
        return gson.fromJson(jsonString, JsonElement.class);
    }

    public HashMap<String, String> readJsonToHashMap(String dataFile, String data) {
        dataFile = EnvSetup.TEST_DATA_PATH + "testcasedata/" + dataFile + ".json";
        log.info("dataFile: " + dataFile);
        JsonObject jsonObject = parseFileToJsonObject(dataFile);
        jsonObject = jsonObject.get(data).getAsJsonObject();
        return gson.fromJson(jsonObject, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    public void readJsonToHashMap(Map<String, String> testData, String dataFile, String data) {
        if (testData == null)
            testData = new HashMap<>();
        testData.clear();
        testData.putAll(readJsonToHashMap(dataFile, data));
    }

    public void updateJIRAResults(Scenario scenario) {
        log.info("-----------------------------------------------------------------------------------------------------");
        log.info("Entering method: " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("EnvSetup.UPDATEZEPHYR: " + EnvSetup.UPDATEZEPHYR);
        log.info("Updating result with status: " + scenario.getStatus());
    }

    /**
     * @param exception
     * @param object
     * @param <T>
     * @throws T
     */
    @SuppressWarnings("unchecked")
    private <T extends Throwable> void throwException(Throwable exception, Object object) throws T {
        throw (T) exception;
    }

    public void throwException(Throwable exception) {
        this.throwException(exception, null);
    }

    public void quitBrowser(WebDriver driver, Scenario scenario) {
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        if (!EnvSetup.KEEP_BROWSER_ACROSS_TESTS.equalsIgnoreCase("yes") || scenario.isFailed()) {
            if (driver != null && !driver.toString().contains("null")) {
                try {
                    driver.close();
                    driver.quit();
                } catch (Exception e) {
                    log.info("No need to quit driver");
                }
            }
            EnvSetup.driverThreading.remove();
        }
    }

    public boolean compareResults(List<String> l1, List<String> l2) {
        try {
            if (l1.equals(l2)) {
                return true;
            }
            String e0L1 = l1.get(0);
            int startIndex = -1;
            for (int i = 0; i < l2.size(); i++) {
                if (e0L1.contentEquals(l2.get(i))) {
                    startIndex = i;
                    break;
                }
            }
            l2 = l2.subList(startIndex, l2.size());
            return l1.subList(0, l2.size()).equals(l2);
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    /**
     * Method to verify last updated time is within last 5 mins
     *
     * @param lastupdated
     */
    public void compareLastUpdatedTime(String lastupdated) {
        log.info("----------------------------------------------------------");
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        Instant currentTimeInUTC = Instant.now().plus(2, ChronoUnit.MINUTES);
        Instant currentTimeInUTCPrv = Instant.now().minus(5, ChronoUnit.MINUTES);
        Date actualdate;
        SimpleDateFormat dateformatter;
        lastupdated = lastupdated.replaceAll(" "," ");
        try {
            dateformatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            // dateformatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            actualdate = dateformatter.parse(lastupdated);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        log.info("Current Time in UTC " + currentTimeInUTC);
        log.info("Current Time - 5 min in UTC " + currentTimeInUTCPrv);
        log.info("Actual last updated Time in UTC " + actualdate.toInstant());
        Assert.assertTrue(currentTimeInUTC.isAfter(actualdate.toInstant()));
        Assert.assertTrue(currentTimeInUTCPrv.isBefore(actualdate.toInstant()));
        log.info("----------------------------------------------------------");
    }

    /**
     * @param sec
     */
    public void wait(float sec) {
        try {
            log.info("Wait for " + sec + " second(s)");
            Thread.sleep((long) (sec * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void wait(int sec) {
        try {
            log.info("Wait for " + sec + " second(s)");
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String readPropertyValue(String sKey, String sPropertyFileName) {
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try (InputStream input = Files.newInputStream(Paths.get(sPropertyFileName))) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(sKey, "");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String readPropertyValue(String sKey) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        try (InputStream input = Files.newInputStream(Paths.get(EnvSetup.USER_PROPERTY_FILE_PATH))) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty(sKey, "");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * Method to scp file to remove server
     *
     * @param sHostNameOrIP
     * @param sUserName
     * @param sPassword
     * @param method
     * @param sSourceFileName
     * @param sDestinationPath
     * @return
     * @throws JSchException
     * @throws SftpException
     */
    public boolean scp(String sHostNameOrIP,
                       String sUserName,
                       String sPassword,
                       String method,
                       String sSourceFileName,
                       String sDestinationPath) throws
            JSchException,
            SftpException {
        log.info("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        JSch jsch = new JSch();
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        Session session = jsch.getSession(sUserName, sHostNameOrIP); // port is usually 22
        session.setConfig(config);
        session.setPassword(sPassword);
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp cFTP = (ChannelSftp) channel;
        if (method.toLowerCase().contains("put")) {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                sSourceFileName = sSourceFileName.replace("/", "\\");
            }
            cFTP.put(sSourceFileName, sDestinationPath);
            log.info("file copied successfully Source: " + sSourceFileName + " DestinationPath" + sDestinationPath);
        } else if (method.toLowerCase().contains("get")) {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                sDestinationPath = sDestinationPath.replace("/", "\\");
            }
            cFTP.get(sSourceFileName, sDestinationPath);
            log.info("file copied successfully Source: " + sSourceFileName + " DestinationPath" + sDestinationPath);
        } else if (method.toLowerCase().contains("delete")) {
            cFTP.rm(sDestinationPath);
            log.info("file deleted successfully DestinationPath : " + sDestinationPath);
        }
        channel.disconnect();
        session.disconnect();
        return true;
    }

    /**
     * This method is used to Select/check Checkbox button.
     *
     * @param sHostNameOrIP    Linux machine Host Name or IP
     * @param sUserName        User name of machine
     * @param sPassword        Password of machine
     * @param sSourceFileName  Windows File name to copy with full path
     * @param sDestinationPath Linux destination path to copy file.
     * @return boolean Return true if success otherwise false.
     */
    public boolean copyFile(String sHostNameOrIP,
                            String sUserName,
                            String sPassword,
                            String sSourceFileName,
                            String sDestinationPath) {
        try {
            if (sHostNameOrIP.equalsIgnoreCase("usecommon")) {
                sHostNameOrIP = EnvSetup.SFTP_HOSTNAME;
                sUserName = EnvSetup.SFTP_USERNAME;
                sPassword = EnvSetup.SFTP_PASSWORD;
            }
            this.scp(sHostNameOrIP, sUserName, sPassword, "PUT", sSourceFileName, sDestinationPath);
            return true;
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method o delete file on remote server
     *
     * @param sHostNameOrIP
     * @param sUserName
     * @param sPassword
     * @param sDestinationPath
     * @return
     */
    public boolean deletefile(String sHostNameOrIP,
                              String sUserName,
                              String sPassword,
                              String sDestinationPath) {
        try {
            if (sHostNameOrIP.equalsIgnoreCase("usecommon")) {
                sHostNameOrIP = EnvSetup.SFTP_HOSTNAME;
                sUserName = EnvSetup.SFTP_USERNAME;
                sPassword = EnvSetup.SFTP_PASSWORD;
            }
            scp(sHostNameOrIP, sUserName, sPassword, "DELETE", "", sDestinationPath);
        } catch (JSchException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SftpException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return true;
    }

    public HashMap<String, Object> readJsonToHashMaps(String dataFile, String data) {
        dataFile = EnvSetup.TEST_DATA_PATH + "testcasedata/" + dataFile + ".json";
        log.info("dataFile: " + dataFile);
        JsonObject jsonObject = parseFileToJsonObject(dataFile);
        jsonObject = jsonObject.get(data).getAsJsonObject();
        return gson.fromJson(jsonObject, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }

    /**
     * Method to write data in csv
     *
     * @param filename
     * @param data
     */
    public void writecsv(String filename, List data) {
        log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
        log.debug(filename);
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            CSVWriter csvOutput = new CSVWriter(osw);
            List<String> row = new ArrayList<>(data);
            csvOutput.writeNext(row.toArray(String[]::new));
            csvOutput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("exception while closing file-" + e.getMessage());
        }
    }

    /**
     * Method to sort Json Array
     *
     * @param jsonArray
     * @param sortBy
     * @return
     */
    public JsonArray JsonObjectSort(final JsonArray jsonArray, final String sortBy) {
        final JsonArray sortedArray = new JsonArray();
        final ArrayList<JsonObject> listJsonObj = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            listJsonObj.add((JsonObject) jsonArray.get(i));
        }
        Collections.sort(listJsonObj,
                (o1, o2) -> o1.get(sortBy).getAsString().compareToIgnoreCase(o2.get(sortBy).getAsString()));

        for (int i = 0; i < jsonArray.size(); i++) {
            sortedArray.add(listJsonObj.get(i));
        }
        return sortedArray;
    }

    /**
     * Verify Duplicate values in json arrary
     *
     * @param contacts
     * @param key
     * @return
     */
    public boolean verifyDuplicateElementinResult(JsonArray contacts, String key) {
        ArrayList<String> listJsonObj = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++) {
            JsonObject contact = contacts.get(i).getAsJsonObject();
            listJsonObj.add(contact.get(key).toString().replaceAll("\"", ""));

        }
        for (int i = 0; i < contacts.size(); i++) {
            JsonObject contact = contacts.get(i).getAsJsonObject();
            Assert.assertEquals(1, Collections.frequency(listJsonObj, contact.get(key).toString().replaceAll("\"", "")));
        }

        return true;
    }

    /**
     * Method to compate out and exp text files
     *
     * @param filename1
     * @param filename2
     * @return
     */
    public boolean compareResultFile(String filename1, String filename2) {
        // Reading the contents of the files
        String s1 = "";
        String s2 = "", s3 = "", s4 = "";
        String y = "", z = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename1));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br1 = null;
        try {
            br1 = new BufferedReader(new FileReader(filename2));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                if ((z = br1.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            s3 += z;
        }
        while (true) {
            try {
                if ((y = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            s1 += y;
        }
        System.out.println();
        // String tokenizing
        int numTokens = 0;
        StringTokenizer st = new StringTokenizer(s1);
        String[] a = new String[10001];
        for (int l = 0; l < 10001; l++) {
            a[l] = "";
        }
        int i = 0;
        while (st.hasMoreTokens()) {
            s2 = st.nextToken();
            a[i] = s2;
            i++;
            numTokens++;
        }
        int numTokens1 = 0;
        StringTokenizer st1 = new StringTokenizer(s3);
        String[] b = new String[10001];
        for (int k = 0; k < 10001; k++) {
            b[k] = "";
        }
        int j = 0;
        while (st1.hasMoreTokens()) {
            s4 = st1.nextToken();
            b[j] = s4;
            j++;
            numTokens1++;
        }
        // comparing the contents of the files and printing the differences, if any.
        int x = 0;
        for (int m = 0; m < a.length; m++) {
            if (a[m].equals(b[m])) {
            } else {
                x++;
                log.info(a[m] + " -- " + b[m]);
                System.out.println();
            }
        }
        log.info("No. of differences : " + x);
        if (x > 0) {
            log.info("Files are not equal " + filename1 + " Vs " + filename2);
            return false;
        } else {
            log.info("Files are equal. No difference found " + filename1 + " Vs " + filename2);
        }
        return true;
    }

    public void tryToLoadData(TestData<String, String> testData, Scenario scenario) {
        String dataFile = "";
        String data = "";
        for (String tag : scenario.getSourceTagNames()) {
            if (tag.matches("(?i)@DataFile=.*")) {
                dataFile = tag.replaceFirst("(?i)@DataFile=\\s*", "");
            } else if (tag.matches("(?i)@Data=.*")) {
                data = tag.replaceFirst("(?i)@Data=\\s*", "");
            }
        }
        if (!dataFile.isEmpty()) {
            if (!data.isEmpty())
                testData.load(dataFile, data);
            else
                testData.load(dataFile);
        }
    }

    public static void cleanUpDataForTesting() {
        if (!EnvSetup.TEST_CLEAN_UP.equalsIgnoreCase("yes")) {
            log.info("Do not perform all data cleanup.");
            return;
        }
        log.info("Try to clean all campaigns, strategies, contact lists, data sources");
        try {
            Instant start = Clock.systemDefaultZone().instant();
            RestMethods restMethods = new RestMethods();
            RestMethods restMethodsClist = new RestMethods(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL, EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
            ConcurrentLinkedQueue<String> camps = new ConcurrentLinkedQueue<>(
                    restMethods.getAll(RestMethods.Page.campaign).asList()
                            .stream().map(json -> json.getAsJsonObject().get("id").getAsString()).toList()
            );
            cleanUp(restMethods, camps, (rest, id) -> rest.deleteCampaign("", id));

            ConcurrentLinkedQueue<String> strategies = new ConcurrentLinkedQueue<>(
                    restMethods.getAll(RestMethods.Page.strategy).asList()
                            .stream().map(json -> json.getAsJsonObject().get("id").getAsString()).toList()
            );
            cleanUp(restMethods, strategies, (rest, id) -> rest.deleteStrategy("", id));

            ConcurrentLinkedQueue<String> clists = new ConcurrentLinkedQueue<>(
                    restMethodsClist.getAll(RestMethods.Page.contactlist).asList()
                            .stream().map(json -> json.getAsJsonObject().get("id").getAsString()).toList()
            );
            cleanUp(restMethodsClist, clists, (rest, id) -> rest.deleteContactList("", id));

            ConcurrentLinkedQueue<String> dsSources = new ConcurrentLinkedQueue<>(
                    restMethodsClist.getAll(RestMethods.Page.datasource).asList()
                            .stream().map(json -> json.getAsJsonObject().get("id").getAsString()).toList()
            );
            cleanUp(restMethodsClist, dsSources, (rest, id) -> rest.deleteDataSource("", id));
            log.info("Total time to get and delete all campaigns, strategies, contact lists and data sources is "
                    + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " second(s)");
            log.info("camps: " + camps);
            log.info("strategies: " + strategies);
            log.info("clists: " + clists);
            log.info("dsSources: " + dsSources);
        } catch (Exception e) {
        }
    }

    private static void cleanUp(RestMethods restMethods, ConcurrentLinkedQueue<String> listIDs, BiConsumer<RestMethods, String> actions) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(15);
            List<Future<String>> futures = new ArrayList<>();
            Callable<String> task = () -> {
                String id = listIDs.poll();
                if (id != null && !id.isEmpty()) {
                    actions.accept(restMethods, id);
                }
                return "Complete deleting: " + id;
            };
            Instant start = Clock.systemDefaultZone().instant();
            Instant end = start.plus(Duration.ofMinutes(10));
            int size = listIDs.size();
            for (int i = 0; i < size; i++) {
                futures.add(executor.submit(task));
            }
            for (Future<String> future : futures) {
                try {
                    future.get(15, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                if (end.isBefore(Clock.systemDefaultZone().instant())) {
                    futures.stream().filter(f -> !f.isDone()).forEach(f -> {
                        try {
                            f.cancel(true);
                        } catch (Exception e) {
                        }
                    });
                    break;
                }
            }
            executor.shutdown();
            log.info("This list size after deleting: " + listIDs.size());
            log.info("Total time to delete: " + Duration.between(start, Clock.systemDefaultZone().instant()).toSeconds() + " second(s)");
        } catch (Exception e) {
        }
    }

    public void updateSSOToken(com.avaya.outbound.steps.Context context) {
        try {
            log.info("Getting access token...");
            String accessToken = context.driver.manage().getCookieNamed("sso-access").getValue();
            log.info("Access token: " + accessToken);
            EnvSetup.ssoToken.set(accessToken);
            context.RestMethodsObj = new RestMethods();
            context.RestMethodsContactListObj = new RestMethods(EnvSetup.OUTBOUND_CONTACTS_API_BASEURL, EnvSetup.DB_USER, EnvSetup.DB_PASSWORD);
        }catch (Exception e) {
            log.info("Exception: ", e);
        }
    }
}
