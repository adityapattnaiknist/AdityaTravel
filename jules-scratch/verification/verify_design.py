from playwright.sync_api import Page, expect

def test_new_design(page: Page):
    """
    This test verifies that the new design is applied to the login page.
    """
    # 1. Arrange: Go to the login page.
    page.goto("http://localhost:3000/login")

    # 2. Assert: Check for the new brand name in the navbar.
    navbar_brand = page.locator(".navbar-brand")
    expect(navbar_brand).to_have_text("ADITYA TOUR AND TRAVELS")

    # 3. Screenshot: Capture the final result for visual verification.
    page.screenshot(path="jules-scratch/verification/verification.png")
