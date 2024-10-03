package demo;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
    @Test(enabled=true)
    public void testCase01() throws InterruptedException{
        System.out.println("StartTestcase 01" );    
        long start = System.currentTimeMillis();
        driver.get("https://www.google.com/");
        driver.navigate().to("https://www.scrapethissite.com/pages/");
        Thread.sleep(3000);
        List<WebElement> pageTitle = driver.findElements(By.xpath("//div[@class='container']/..//div[@class='page']/h3/a"));
         for(int i=1;i<=1;i++){
           String txt =pageTitle.get(1).getText();
           System.out.println(txt);
           pageTitle.get(1).click();
          }
          Thread.sleep(3000) ;
          JavascriptExecutor js = (JavascriptExecutor)driver;
          js.executeScript("window.scrollBy(0,350)", "");
                   
         ArrayList<HashMap<String, Object>> Hockeyteamdata = new ArrayList<>();
         WebElement clickonpage = driver.findElement(By.xpath("//ul[@class='pagination']/li/a[1]"));
         Wrappers.clickonElement(driver, clickonpage);
         Thread.sleep(2000);
         for(int page=1;page<=4;page++) {
          WebElement table = driver.findElement(By.xpath("//table"));
          List<WebElement> rows1 = table.findElements(By.xpath(".//tr[position() > 1]"));
          for(WebElement row:rows1){
            String Teamname = row.findElement(By.xpath("td[contains(@class,'name')]")).getText();
            String year = row.findElement(By.xpath("td[contains(@class,'year')]")).getText();
            String Wins = row.findElement(By.xpath("td[contains(@class,'wins')]")).getText();
            String winPercentageString = row.findElement(By.xpath("td[contains(@class,'pct')]")).getText();
           
            double winPercentage = Double.parseDouble(winPercentageString);
            if (winPercentage < 0.40) {
             HashMap<String, Object> hockeyTeam = new HashMap<>();
             hockeyTeam.put("Epoch Time",Wrappers.getEpochTimeasString());  
             hockeyTeam.put("team",Teamname);
             hockeyTeam.put("Year",year);
             hockeyTeam.put("wins",Wins);
             hockeyTeam.put("winPercentage",winPercentageString);
             Hockeyteamdata.add(hockeyTeam);
             }     

         }
             if(page<4) {
              WebElement pagElement = driver.findElement(By.xpath("//a[@aria-label='Next']"));
            //   pagElement.click(); 
            Wrappers.clickonElement(driver, pagElement);
              Thread.sleep(3000);
          }
          
         } 
         ObjectMapper objectMapper = new ObjectMapper();
         try {
            String userDir = System.getProperty("user.dir");
            File jsonFile = new File(userDir+"/src/test/resources/hockey-team-data.json");
            objectMapper.writeValue(jsonFile, Hockeyteamdata);
            System.out.println("JSON data written to: " + jsonFile.getAbsolutePath());
            SoftAssert sa = new SoftAssert();
            sa.assertTrue(jsonFile.length() != 0);
         } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("Testcase 01 took: " + (long) elapsedTime / 1000 + "seconds");
        System.out.println("EndTestcase 01" );
 }

    @Test(enabled=true)
    public void testCase02() throws InterruptedException, IOException {
        System.out.println("TestCase 02 Starts");
        long start = System.currentTimeMillis();
        driver.get("https://www.scrapethissite.com/pages/");
        WebElement pageTitle = driver.findElement(By.xpath("//a[contains(text(),'Oscar Winning Films')]"));
        Wrappers.clickonElement(driver,pageTitle);
        
        ArrayList<HashMap<String, Object>> Ocsarwinningfilmsdata = new ArrayList<>();
        
        List<WebElement> yearLinks = driver.findElements(By.xpath("//div[contains(@class, 'text-center')]/..//a[@class='year-link']"));    

        for (WebElement yearlink : yearLinks) {
            String year = yearlink.getText();
            yearlink.click();
            System.out.println("The year clicked is: " + year);
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='table']")));
      
            List<WebElement> rows = driver.findElements(By.xpath("//tr[@class='film']"));
            for(int i=0;i<5;i++) {
                int count =1;   
                for (WebElement row: rows) {    
                String movietitle = row.findElement(By.xpath(".//td[contains(@class,'title')]")).getText();
                String nomination = row.findElement(By.xpath(".//td[contains(@class,'nominations')]")).getText();
                String awards = row.findElement(By.xpath(".//td[contains(@class,'awards')]")).getText();
    
                boolean isWinnerflag = count==1;
                String isWinner = String.valueOf(isWinnerflag);
                Wrappers.getEpochTimeasString();

                HashMap<String, Object> Oscarmovies = new HashMap<>();
                Oscarmovies.put("Epoch time", Wrappers.getEpochTimeasString());
                Oscarmovies.put("Year", year);
                Oscarmovies.put("Movie Name", movietitle);
                Oscarmovies.put("Nomaination", nomination);
                Oscarmovies.put("Awards", awards);
                Oscarmovies.put("IsWinner", isWinner);

                Ocsarwinningfilmsdata.add(Oscarmovies);
                count++;
            }
        }
            
        ObjectMapper objectMapper = new ObjectMapper();
        try {
           String userDir = System.getProperty("user.dir");
           File jsonFile = new File(userDir+"/src/test/resources/oscar-winner-data.json");
           objectMapper.writeValue(jsonFile, Ocsarwinningfilmsdata);
           System.out.println("JSON data written to: " + jsonFile.getAbsolutePath());
           SoftAssert sa = new SoftAssert();
           sa.assertTrue(jsonFile.length() != 0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
       }

       long end = System.currentTimeMillis();
       long elapsedTime = end - start;
       System.out.println("Testcase 02 took: " + (long) elapsedTime / 1000 + "seconds");             
       System.out.println("TestCase02 Ends");
         
        }
    }
}   

     