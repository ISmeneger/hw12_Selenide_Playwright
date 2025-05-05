package pages.selenide;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class WebFormPage {
    public static final String VALUE_NAME = "value";

    public void submit() {
        $(By.xpath("//button[text() = 'Submit']")).click();
    }

    @Step("Input login")
    public void inputLogin(String login) {
        $(By.id("my-text-id")).sendKeys(login);;
    }

    @Step("Input password")
    public void inputPassword(String password) {
        $(By.name("my-password")).sendKeys(password);;
    }

    @Step("Assert value text")
    public String getTextValue() {
        return $(By.id("my-text-id")).getDomProperty(VALUE_NAME);
    }

    @Step("Assert value password")
    public String getTextPassword() {
        return $(By.name("my-password")).getDomProperty(VALUE_NAME);
    }

    @Step("Clear text in user field")
    public void clearTextValue() {
        $(By.id("my-text-id")).clear();
    }

    @Step("Clear password")
    public void clearPasswordValue() {
        $(By.name("my-password")).clear();
    }

}
