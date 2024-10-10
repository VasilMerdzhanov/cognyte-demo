package steps;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SauceDemoSteps {

    private WebDriver driver;

    @Before
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Given("User opens the browser and navigates to the SauceDemo site")
    public void open_browser_and_navigate() {
        driver.get("https://www.saucedemo.com/");
    }

    @When("User logs in with valid credentials {string} and {string}")
    public void login_with_valid_credentials(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @Then("User should be on the products page")
    public void verify_products_page() {
        String pageTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(pageTitle, "Products");
    }

    @When("User adds products to the cart")
    public void add_two_products_to_cart() {
        // Add first product
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // Verify first product added
        String firstProductButtonText = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
        Assert.assertEquals(firstProductButtonText, "Remove");

        // Add second product
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();

        // Verify second product added
        String secondProductButtonText = driver.findElement(By.id("remove-sauce-labs-bike-light")).getText();
        Assert.assertEquals(secondProductButtonText, "Remove");
        
    }

    @Then("The cart should show {int} item\\(s)")
    public void verify_cart_item_count(int expectedItemCount) {
        List<WebElement> cartBadge = driver.findElements(By.className("shopping_cart_badge"));
        if (expectedItemCount == 0) {
            Assert.assertTrue(cartBadge.isEmpty(), "Cart should be empty");
        } else {
            Assert.assertEquals(cartBadge.get(0).getText(), String.valueOf(expectedItemCount));
        }
        
    }

    @When("User removes the products from the cart")
    public void remove_two_products_from_cart() {
        // Remove first product
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        // Verify first product removed
        String firstProductButtonText = driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).getText();
        Assert.assertEquals(firstProductButtonText, "Add to cart");

        // Remove second product
        driver.findElement(By.id("remove-sauce-labs-bike-light")).click();

        // Verify second product removed
        String secondProductButtonText = driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).getText();
        Assert.assertEquals(secondProductButtonText, "Add to cart");
    }

    @Then("The cart should show no items")
    public void verify_cart_is_empty() {
        List<WebElement> cartBadge = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(cartBadge.isEmpty(), "Cart should be empty");
    }
    

    @When("User navigates to the cart")
    public void navigate_to_cart() {
        driver.findElement(By.id("shopping_cart_container")).click();
    }

    @Then("The cart should have {int} item\\(s)")
    public void verify_items_in_cart(int expectedCount) {
        int actualItemCount = driver.findElements(By.className("cart_item")).size();
        Assert.assertEquals(actualItemCount, expectedCount);
    }

    @Then("The cart should display the correct products {string} and {string}")
    public void verify_correct_products_in_cart(String product1, String product2)   {
    	
    	List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
    	Assert.assertEquals(cartItems.size(), 2);
        Assert.assertTrue(cartItems.get(0).findElement(By.className("inventory_item_name")).getText().contains(product1));
        Assert.assertTrue(cartItems.get(1).findElement(By.className("inventory_item_name")).getText().contains(product2));
    }

    @When("User proceeds to checkout")
    public void proceed_to_checkout() {
        driver.findElement(By.id("checkout")).click();
    }

    @When("User tries to submit the form with missing fields")
    public void submit_empty_form() {
        driver.findElement(By.id("continue")).click();
    }

    @Then("User should see an error message {string}")
    public void verify_error_message(String expectedMessage) {
        String actualMessage = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @When("User fills in checkout information with {string}, {string}, and {string}")
    public void fill_checkout_information(String firstName, String lastName, String zipCode) {
        driver.findElement(By.id("first-name")).sendKeys(firstName);
        driver.findElement(By.id("last-name")).sendKeys(lastName);
        driver.findElement(By.id("postal-code")).sendKeys(zipCode);
    }

    @When("User clicks on the continue button")
    public void click_continue_button() {
        driver.findElement(By.id("continue")).click();
    }

    @Then("User should be on the order summary page with title {string}")
    public void verify_order_summary_page(String expectedTitle) {
        String pageTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(pageTitle, expectedTitle);
    }
    
    @Then("Verify that the information on the last preview screen is shown properly")
    public void verify_last_preview_screen() {
        // Verify the total price is displayed
        WebElement totalPrice = driver.findElement(By.className("summary_total_label"));
        Assert.assertTrue(totalPrice.isDisplayed(), "Total price is not displayed");

        // Verify that the items are displayed correctly
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertTrue(cartItems.size() > 0, "No items are displayed in the cart");

        // Verify the finish and cancel buttons are displayed
        WebElement finishButton = driver.findElement(By.id("finish"));
        WebElement cancelButton = driver.findElement(By.id("cancel"));
        Assert.assertTrue(finishButton.isDisplayed(), "Finish button is not displayed");
        Assert.assertTrue(cancelButton.isDisplayed(), "Cancel button is not displayed");
    }
    
    @And("Verify the total price is correct based on selected items")
    public void verify_total_price() {
        // Calculate the expected total price
        double expectedTotalPrice = calculateExpectedTotalPrice();

        // Get the displayed total price
        String totalPriceText = driver.findElement(By.className("summary_total_label")).getText();
        String totalPriceValue = totalPriceText.replace("Total: $", "").trim();
        double actualTotalPrice = Double.parseDouble(totalPriceValue);

        // Compare the actual total price with the expected total price
        Assert.assertEquals(actualTotalPrice, expectedTotalPrice, "Total price is incorrect");
    }

    // A method to calculate the expected total price based on selected products
    private double calculateExpectedTotalPrice() {
        
        double item1Price = 29.99;  // price for item 1
        double item2Price = 9.99;   // price for item 2
        double tax = 3.20;          // tax value

        return item1Price + item2Price + tax;
    }
    
    @When("User completes the purchase")
    public void complete_the_purchase() {
        // Click on the finish button to complete the purchase
        driver.findElement(By.id("finish")).click();
    }

    @Then("User should see a success message {string}")
    public void verify_success_message(String expectedMessage) {
        WebElement successMessageElement = driver.findElement(By.className("complete-header"));
        String actualMessage = successMessageElement.getText();
        Assert.assertEquals(actualMessage, expectedMessage, "The success message is not as expected");
    }

    @When("User logs out")
    public void user_logs_out() {
        // Open the side menu
        driver.findElement(By.id("react-burger-menu-btn")).click();
        
        // Wait for the logout button to be visible
        WebElement logoutButton = driver.findElement(By.id("logout_sidebar_link"));
        
        // Click on logout
        logoutButton.click();
    }

    @Then("User should be redirected to the login page")
    public void verify_redirection_to_login_page() {
        // Verify that user is redirected to the login page by checking the presence of the login button
        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isDisplayed(), "The user is not redirected to the login page");
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
