package ui_automations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class boardTests {
	WebDriver driver;
	WebDriverWait wait;
	 
	
	@BeforeTest
	public void launchApp() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:3000/");
		wait =new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	@Test
	public void boardCreation() {
		
		//Counting the Board count before create a new Board
		List<WebElement> boardsBefore;
		try {
		boardsBefore = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='board' and @data-cy='board-item']")));
		} catch (TimeoutException e) {
			boardsBefore = new ArrayList<>();
		}
		
		int boardCountBefore = boardsBefore.size();
		
		// Creating a New Board
		driver.findElement(By.xpath("//div[@data-cy='create-board']")).click();
		driver.findElement(By.xpath("//input[@data-cy='new-board-input']")).sendKeys("b3", Keys.ENTER);

		wait.until(ExpectedConditions.urlContains("/board/"));
		driver.navigate().back();
		//Counting the Board count after create a new Board
		List<WebElement> boardsAfter = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='board' and @data-cy='board-item']")));
		int boardCountAfter = boardsAfter.size();
		
		Assert.assertEquals(boardCountAfter, boardCountBefore +1, "New Board was not created");
	}
	@Test
	public void addingLists() {
		
		try {
			driver.findElement(By.xpath("//div/h2[text()='b3']")).click();
		} catch (NoSuchElementException e) {
			Assert.fail("b3 Board was not created");
		}
		
		//Counting the List count before create a new List
		List<WebElement> listsBefore;
		try {
		listsBefore = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-cy='list']")));
		} catch (TimeoutException e) {
			listsBefore = new ArrayList<>();
		}
		int listCountBefore = listsBefore.size();
		
		//creating two lists as List_1 and List_2
		driver.findElement(By.xpath("//input[@data-cy='add-list-input']")).sendKeys("List_1", Keys.ENTER);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@data-cy='add-list-input']"))).sendKeys("List_2", Keys.ENTER);
		
	
		try {
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[@data-cy='list']"), listCountBefore +1));
		} catch (TimeoutException e) {
			Assert.fail("Expected more than " + listCountBefore + " lists, but new lists did not appear.");
		}
		
		//Counting the List count after create a new List
		List<WebElement> listsAfter = driver.findElements(By.xpath("//div[@data-cy='list']"));
		int listCountAfter = listsAfter.size();
		System.out.println(listsAfter);
		
		Assert.assertEquals(listCountAfter, listCountBefore + 2, "Exactly Two lists are not created successfully");	
	}
	
	@Test
	public void removeList() {
		List<WebElement> listsBefore;
		try {
		listsBefore = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-cy='list']")));
		} catch (TimeoutException e) {
			listsBefore = new ArrayList<>();
		}
		
		int listCountBefore = listsBefore.size();
		
		
		//Remove the first list always.
		
		if (listCountBefore == 0) {
			Assert.fail("This is an Empty board");
		} else {
			driver.findElement(By.xpath("//button[@data-cy='list-options']")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Delete list']"))).click();
			
			List<WebElement> listsAfter = wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//div[@data-cy='list']"), listCountBefore));
			int listCountAfter = listsAfter.size();
			
			Assert.assertEquals(listCountAfter, listCountBefore - 1, "First list was not removed successfully");
		}
		
	}
	@AfterTest
	public void closingBrowser() {
		driver.quit();
	}

}
