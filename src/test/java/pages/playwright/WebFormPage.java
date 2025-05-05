package pages.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class WebFormPage {
    private final Page page;
    private final Locator submitButton;
    private final Locator inputLoginField;
    private final Locator inputPasswordField;

    public WebFormPage(Page page) {
        this.page = page;
        this.submitButton = page.getByText("Submit");
        this.inputLoginField = page.locator("#my-text-id");
        this.inputPasswordField = page.locator("css=[name='my-password']");
    }

    @Step("Click to submit")
    public void submit() {
        submitButton.click();
    }

    @Step("Input login")
    public void inputLogin(String login) {
        inputLoginField.fill(login);;
    }

    @Step("Clear text in user field")
    public void clearTextValue() {
        inputLoginField.clear();
    }

    @Step("Input password")
    public void inputPassword(String password) {
        inputPasswordField.fill(password);;
    }

    @Step("Clear password")
    public void clearPasswordValue() {
        inputPasswordField.clear();
    }
}
