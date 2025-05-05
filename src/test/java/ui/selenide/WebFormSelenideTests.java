package ui.selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.support.Color;
import pages.selenide.HomePage;
import pages.selenide.WebFormPage;

import java.io.File;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.domProperty;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("selenide")
class WebFormSelenideTests {
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());

    private static final String WEB_FORM_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    private static final String BIG_TEXT = "Lorem ipsum dolor sit amet consectetur adipiscing elit habitant metus, " +
            "tincidunt maecenas posuere sollicitudin augue duis bibendum mauris eu, et dignissim magna ad nascetur suspendisse quis nunc. " +
            "Fames est ligula molestie aliquam pretium bibendum nullam, sociosqu maecenas mus etiam consequat ornare leo, sem mattis " +
            "varius luctus litora senectus. Parturient quis tristique erat natoque tortor nascetur, primis augue vivamus habitasse " +
            "senectus porta leo, aenean potenti ante a nam.";

    private static final String DROPDOWN_SELECT_TEXT = "Open this select menu";
    private static final String VALUE_NAME = "value";

    @BeforeEach
    void start() {
        open(WEB_FORM_URL);
    }

    @Test
    @Order(1)
    @DisplayName("Check heading title")
    void headingTextTest() {
        WebElement headingText = $(By.className("display-4"));

        assertEquals("Hands-On Selenium WebDriver with Java", headingText.getText());
    }

    @Test
    @Order(2)
    @DisplayName("Check title")
    void titleTextTest() {
        WebElement titleText = $(By.tagName("h5"));

        assertEquals("Practice site", titleText.getText());
    }

    @Test
    @Order(3)
    @DisplayName("Check icon")
    void imageIconTest() {
        WebElement imageIcon = $(By.xpath("//img[@src = 'img/hands-on-icon.png']"));

        int width = imageIcon.getSize().getWidth();
        int hight = imageIcon.getSize().getHeight();

        System.out.println(width + ">>>" + hight);

        assertTrue(imageIcon.isDisplayed());
        assertEquals(80, width);

        imageIcon.click();
        assertEquals("https://github.com/bonigarcia/selenium-webdriver-java", url());
    }

    @Test
    @Order(4)
    @DisplayName("Check web form title")
    void webFormTitleTextTest() {
        WebElement webFormTitleText = $(By.className("display-6"));

        assertEquals("Web form", webFormTitleText.getText());
    }

    @Test
    @Order(5)
    @DisplayName("Check web form field names")
    void webFormFieldNamesTest() {
        ElementsCollection formFields = $$(By.xpath("//label[@class='form-label w-100']"));
        ElementsCollection checkBoxFields = $$(By.xpath("//label[@class='form-check-label w-100']"));

        assertAll (
                () -> assertEquals("Text input", formFields.get(0).getText(), "Text Input Field Name"),
                () -> assertEquals("Password", formFields.get(1).getText(), "Password Input Field Name"),
                () -> assertEquals("Textarea", formFields.get(2).getText(), "Disabled Input Field Name"),
                () -> assertEquals("Disabled input", formFields.get(3).getText(), "Disabled Input Field Name"),
                () -> assertEquals("Readonly input", formFields.get(4).getText(), "Readonly Input Field Name"),
                () -> assertEquals("Dropdown (select)", formFields.get(5).getText().split("\n")[0].trim(),
                        "Dropdown (select) Field Name"),
                () -> assertEquals("Dropdown (datalist)", formFields.get(6).getText(), "Dropdown (datalist) Field Name"),
                () -> assertEquals("File input", formFields.get(7).getText(), "File Input Field Name"),
                () -> assertEquals("Checked checkbox", checkBoxFields.get(0).getText(), "Checked Checkbox Field Name"),
                () -> assertEquals("Default checkbox", checkBoxFields.get(1).getText(), "Default Checkbox Field Name"),
                () -> assertEquals("Checked radio", checkBoxFields.get(2).getText(), "Checked Radio Field Name"),
                () -> assertEquals("Default radio", checkBoxFields.get(3).getText(), "Default Radio Field Name"),
                () -> assertEquals("Color picker", formFields.get(8).getText(), "Color Picker Field Name"),
                () -> assertEquals("Date picker", formFields.get(9).getText(), "Date Picker Field Name"),
                () -> assertEquals("Example range", formFields.get(10).getText(), "Range Input Field Name")
        );
    }

    @Test
    @Order(6)
    @DisplayName("Check user field")
    void textInputTest() {
        HomePage homePage = new HomePage();
        homePage.open();
        WebFormPage webFormPage = homePage.openWebForm();

        webFormPage.inputLogin(config.getUsername());
        String actualText = webFormPage.getTextValue();

        assertThat(actualText).isNotEmpty();
    }

    @Test
    @Order(7)
    @DisplayName("Check clear user field")
    void textInputClearTest() {
        HomePage homePage = new HomePage();
        homePage.open();
        WebFormPage webFormPage = homePage.openWebForm();

        webFormPage.inputLogin(config.getUsername());
        webFormPage.clearTextValue();
        String actualText = webFormPage.getTextValue();

        assertThat(actualText).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("Check password field")
    void passwordInputTest() {
        HomePage homePage = new HomePage();
        homePage.open();
        WebFormPage webFormPage = homePage.openWebForm();

        webFormPage.inputPassword(config.getPassword());
        String actualPassword = webFormPage.getTextPassword();

        assertThat(actualPassword).isNotEmpty();
    }

    @Test
    @Order(9)
    @DisplayName("Check clear password field")
    void passwordInputClearTest() {
        HomePage homePage = new HomePage();
        homePage.open();
        WebFormPage webFormPage = homePage.openWebForm();

        webFormPage.inputPassword(config.getPassword());
        webFormPage.clearPasswordValue();
        String actualPassword = webFormPage.getTextPassword();

        assertThat(actualPassword).isEmpty();
    }

    @Test
    @Order(10)
    @DisplayName("Check textarea field")
    void textAreaFieldTest() {
        SelenideElement textAreaInputField = $(By.name("my-textarea"));

        textAreaInputField.sendKeys(BIG_TEXT);

        textAreaInputField.shouldHave(domProperty(VALUE_NAME));
    }

    @Test
    @Order(11)
    @DisplayName("Check clear textarea field")
    void textAreaFieldClearTest() {
        SelenideElement textAreaInputField = $(By.name("my-textarea"));
        textAreaInputField.sendKeys(BIG_TEXT);

        textAreaInputField.clear();

        textAreaInputField.shouldHave(domProperty(VALUE_NAME));
    }

    @Test
    @Order(12)
    @DisplayName("Check Disabled input field")
    void disabledInputFieldTest() {
        WebElement disabledInputField = $(By.name("my-disabled"));

        assertFalse(disabledInputField.isEnabled());
        assertEquals("Disabled input", disabledInputField.findElement(By.xpath("..")).getText());
        assertEquals("Disabled input", disabledInputField.getDomAttribute("placeholder"));

        Rectangle rec = disabledInputField.getRect();
        System.out.printf("Dimension %s, Height %s, Width %s, Point %s, X: %s, Y: %s\n", rec.getDimension(), rec.getHeight(),
                rec.getWidth(), rec.getPoint(), rec.getX(), rec.getY());

        //get css values
        System.out.println(disabledInputField.getCssValue("background-color"));
        System.out.println(disabledInputField.getCssValue("opacity"));
        System.out.println(disabledInputField.getCssValue("font-size"));
        System.out.println(disabledInputField.getCssValue("color"));

        System.out.println(disabledInputField.getText());
        System.out.println(disabledInputField.findElement(By.xpath("..")).getText());
        System.out.println(disabledInputField.findElement(By.xpath("..")).getDomProperty("innerText"));
        System.out.println(disabledInputField.findElement(By.xpath("..")).getDomProperty("textContent"));
    }

    @Test
    @Order(13)
    @DisplayName("Check Readonly input field")
    void readonlyInputFieldTest() {
        WebElement readonlyInputField = $(By.name("my-readonly"));

        assertTrue(readonlyInputField.isEnabled());
        assertEquals("Readonly input", readonlyInputField.findElement(By.xpath("..")).getText());
        assertEquals("Readonly input", readonlyInputField.getDomAttribute(VALUE_NAME));
        readonlyInputField.sendKeys("test string");
        assertNotEquals("test string", readonlyInputField.findElement(By.xpath("..")).getText());

        System.out.println(readonlyInputField.getText());
        System.out.println(readonlyInputField.findElement(By.xpath("..")).getText());
        System.out.println(readonlyInputField.findElement(By.xpath("..")).getDomProperty("innerText"));
        System.out.println(readonlyInputField.findElement(By.xpath("..")).getDomProperty("textContent"));
    }

    @ParameterizedTest(name = "Check Dropdown (select) menu by visible text")
    @Order(14)
    @ValueSource(strings = {"One", "Two", "Three"})
    public void dropdownSelectByVisibleTextTest(String option) {
        SelenideElement dropdownSelectMenu = $(By.name("my-select"));
        dropdownSelectMenu.shouldHave(text(DROPDOWN_SELECT_TEXT));

        dropdownSelectMenu.selectOption(option);

        dropdownSelectMenu.shouldHave(text(option));
    }

    @ParameterizedTest(name = "Check Dropdown (select) menu by value")
    @Order(15)
    @ValueSource(strings = {"1", "2", "3"})
    public void dropdownSelectByValueTest(String value) {
        SelenideElement dropdownSelectMenu = $(By.name("my-select"));
        dropdownSelectMenu.shouldHave(text(DROPDOWN_SELECT_TEXT));

        dropdownSelectMenu.selectOptionByValue(value);

        dropdownSelectMenu.shouldHave(exactValue(value));
    }

    @ParameterizedTest(name = "Check Dropdown (datalist) menu")
    @Order(16)
    @ValueSource(strings = {"San Francisco", "New York", "Seattle", "Los Angeles", "Chicago"})
    void dropdownDataListTest(String city) {
        SelenideElement dropdownDataList = $(By.name("my-datalist"));
        dropdownDataList.sendKeys(city);

        dropdownDataList.shouldHave(domProperty(VALUE_NAME));
    }

    @Test
    @Order(17)
    @DisplayName("Check File input field")
    void fileInputTest() {
        String selectFile = System.getProperty("user.dir") + "/src/main/resources/STE In Banner.jpg";

        SelenideElement fileInput = $("input[name='my-file']");
        fileInput.uploadFile(new File(selectFile));

        WebElement submit = $(By.xpath("//button[text()='Submit']"));
        submit.click();

        assertThat(url()).contains("STE+In+Banner.jpg");
    }

    @Test
    @Order(18)
    @DisplayName("Check checked Checkbox")
    void checkedCheckboxTest() {
        SelenideElement checkedCheckbox = $(By.id("my-check-1"));

        checkedCheckbox.shouldBe(selected);

        checkedCheckbox.click();

        checkedCheckbox.shouldNotBe(selected);
    }

    @Test
    @Order(19)
    @DisplayName("Check default Checkbox")
    void defaultCheckboxTest() {
        SelenideElement checkedCheckbox = $(By.id("my-check-2"));

        checkedCheckbox.shouldNotBe(selected);

        checkedCheckbox.click();

        checkedCheckbox.shouldBe(selected);
    }

    @Test
    @Order(20)
    @DisplayName("Check radio buttons")
    void radioButtonsTest() {
        SelenideElement checkedRadioButton = $(By.id("my-radio-1"));
        SelenideElement defaultRadioButton = $(By.id("my-radio-2"));

        checkedRadioButton.shouldBe(selected);
        defaultRadioButton.shouldNotBe(selected);

        defaultRadioButton.click();

        checkedRadioButton.shouldNotBe(selected);
        defaultRadioButton.shouldBe(selected);
    }

    @Test
    @Order(21)
    @DisplayName("Check Color picker")
    void colorPickerTest() {
        SelenideElement colorPicker = $(By.name("my-colors"));

        String initColor = colorPicker.getAttribute(VALUE_NAME);
        System.out.println("The initial color is " + initColor);

        Color green = new Color(0, 255, 0, 1);

        String script = String.format("arguments[0].setAttribute('value', '%s');", green.asHex());
        executeJavaScript(script, colorPicker);

        String finalColor = colorPicker.getAttribute(VALUE_NAME);
        System.out.println("The initial color is " + finalColor);
        assertThat(finalColor).isNotEqualTo(initColor);
        assertThat(Color.fromString(finalColor)).isEqualTo(green);
    }

    @Test
    @Order(22)
    @DisplayName("Check Date picker")
    void datePickerTest() {
        SelenideElement dateBox = $(By.name("my-date"));

        dateBox.click();
        dateBox.sendKeys("05/05/2025");

        dateBox.shouldHave(domProperty(VALUE_NAME));
    }

    @Test
    @Order(23)
    @DisplayName("Check Example range")
    void actionAPIMouseExampleRangeTest() {
        SelenideElement rangeElement = $(By.name("my-range"));

        int width = rangeElement.getSize().getWidth();
        System.out.println("width = " + width);
        int x = rangeElement.getLocation().getX();
        System.out.println("x = " + x);
        int y = rangeElement.getLocation().getY();
        System.out.println("y = " + y);
        int i;
        for (i = 5; i <= 10; i++) {
            actions()
                    .moveToElement(rangeElement)
                    .clickAndHold()
                    .moveToLocation(x + width / 10 * i, y)
                    .release()
                    .perform();

        }
        assertThat(rangeElement.getAttribute("value")).isEqualTo(String.valueOf(10));
    }

    @Test
    @Order(24)
    @DisplayName("Check Return to index link")
    void returnToIndexLinkTest() {
        SelenideElement returnToIndexLink = $(By.xpath("//a[@href = './index.html']"));
        returnToIndexLink.click();

        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/index.html", url());
    }

    @Test
    @Order(25)
    @DisplayName("Check Copyright link")
    void boniGarciaLinkTest() {
        SelenideElement boniGarciaLink = $(By.xpath("//a[@href = 'https://bonigarcia.dev/']"));
        boniGarciaLink.click();

        assertEquals("https://bonigarcia.dev/", url());
    }

    @Test
    @Order(26)
    @DisplayName("Check Copyright text")
    void copyrightTextTest() {
        SelenideElement copyrightText = $(By.xpath("//span[@class = 'text-muted' and normalize-space(text()) = 'Copyright © 2021-2025']"));

        copyrightText.shouldHave(text("Copyright © 2021-2025 Boni García"));
    }

    @Test
    @Order(27)
    @DisplayName("Check submit button")
    void submitButtonTest() {
        HomePage homePage = new HomePage();
        homePage.open();
        WebFormPage webFormPage = homePage.openWebForm();
        webFormPage.submit();

        assertThat(url()).contains("https://bonigarcia.dev/selenium-webdriver-java/submitted-form.html");

        WebElement formSubmittedText = $(By.xpath("//h1[@class = 'display-6']"));

        assertEquals("Form submitted", formSubmittedText.getText());
    }
}
