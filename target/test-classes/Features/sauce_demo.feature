Feature: SauceDemo E2E Testing
  Scenario: Full end-to-end test for the SauceDemo site
    Given User opens the browser and navigates to the SauceDemo site
    When User logs in with valid credentials "standard_user" and "secret_sauce"
    Then User should be on the products page

    # Add two products and verify
    When User adds products to the cart
    Then The cart should show 2 item(s)

    # Remove the two products and verify
    When User removes the products from the cart
    Then The cart should show no items

    # Add two different products to the cart
    When User adds products to the cart
    Then The cart should show 2 item(s)
 
    # Verify products in the cart
    When User navigates to the cart
    Then The cart should have 2 item(s)
    And The cart should display the correct products "Sauce Labs Backpack" and "Sauce Labs Bike Light"

    # Attempt to submit an empty form during checkout
    When User proceeds to checkout
    And User tries to submit the form with missing fields
    Then User should see an error message "Error: First Name is required"

    # Fill out the form correctly and continue
    When User fills in checkout information with "Johny", "Bravo", and "1212"
    And User clicks on the continue button
    Then User should be on the order summary page with title "Checkout: Overview"

    # Verify order summary details
    Then Verify that the information on the last preview screen is shown properly
    And Verify the total price is correct based on selected items

    # Complete the purchase
    When User completes the purchase
    Then User should see a success message "Thank you for your order!"

    # Logout after successful purchase
    When User logs out
    Then User should be redirected to the login page


