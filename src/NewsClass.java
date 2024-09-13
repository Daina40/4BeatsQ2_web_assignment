import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException; 
import org.junit.Assert;

import java.time.Duration;
import java.util.List;

public class NewsClass {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.news24bd.tv/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("nav a")));
        } catch (TimeoutException e) {
            System.out.println("Navigation links not found.");
            driver.quit();
            return;
        }

        List<WebElement> navLinks = driver.findElements(By.cssSelector("nav a"));

        if (navLinks.isEmpty()) {
            System.out.println("No navigation links found.");
            driver.quit();
            return;
        }

        // Iterate through each navigation link
        for (int i = 0; i < navLinks.size(); i++) {
            // Re-find the navigation links to prevent StaleElementReferenceException
            navLinks = driver.findElements(By.cssSelector("nav a"));

            // Check if the list is still valid
            if (navLinks.isEmpty()) {
                System.out.println("No navigation links found during iteration.");
                break; // Exit the loop if no links are found
            }

            // Get the link text for logging purposes
            String linkText = navLinks.get(i).getText();
            System.out.println("Checking link: " + linkText);

            try {
                // Scroll to the element to ensure it is in view
                WebElement link = navLinks.get(i);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", link);

                // Wait until the element is clickable
                wait.until(ExpectedConditions.elementToBeClickable(link));

                // Try clicking the link
                link.click();

                // Optionally, wait for the page to load or perform additional actions
                Thread.sleep(1000); // wait for 1 second

                // Verify if the page loaded correctly
                String currentUrl = driver.getCurrentUrl();
                System.out.println("Current URL: " + currentUrl);

                // Modify expected URL check according to the actual URL format
                Assert.assertTrue("URL does not match expected content", currentUrl.contains(linkText.toLowerCase()));

                // Go back to the main page
                driver.navigate().back();

                // Wait for the main page to be fully loaded again
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("nav a")));

            } catch (Exception e) {
                System.out.println("Exception occurred while clicking link: " + linkText);
                e.printStackTrace();
            }
        }

        // Close the browser
        driver.quit();
    }
}
