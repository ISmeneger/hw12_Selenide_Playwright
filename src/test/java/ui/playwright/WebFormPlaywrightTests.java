package ui.playwright;

import com.microsoft.playwright.*;
import configs.TestPropertiesConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.support.Color;
import pages.playwright.HomePage;
import pages.playwright.WebFormPage;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("playwright")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebFormPlaywrightTests {
    TestPropertiesConfig config = ConfigFactory.create(TestPropertiesConfig.class, System.getProperties());
    private static final String WEB_FORM_URL = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
    private static final String BIG_TEXT = "Lorem ipsum dolor sit amet consectetur adipiscing elit habitant metus, " +
            "tincidunt maecenas posuere sollicitudin augue duis bibendum mauris eu, et dignissim magna ad nascetur suspendisse quis nunc. " +
            "Fames est ligula molestie aliquam pretium bibendum nullam, sociosqu maecenas mus etiam consequat ornare leo, sem mattis " +
            "varius luctus litora senectus. Parturient quis tristique erat natoque tortor nascetur, primis augue vivamus habitasse " +
            "senectus porta leo, aenean potenti ante a nam.";

    private static final String DROPDOWN_SELECT_TEXT = "Open this select menu";
    private static final String EMPTY_VALUE = "";
    private static final String CURRENT_DATE = "05/05/2025";

    static Playwright playwright;
    static Browser browser;

    BrowserContext context;
    Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate(WEB_FORM_URL);
    }

    @AfterEach
    void closeContext() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        context.close();
    }

    @Test
    @Order(1)
    @DisplayName("Check heading title")
    void headingTextTest() {
        Locator headingText = page.locator(".display-4");

        assertEquals("Hands-On Selenium WebDriver with Java", headingText.innerText());
    }

    @Test
    @Order(2)
    @DisplayName("Check title")
    void titleTextTest() {
        Locator titleText = page.locator("h5");

        assertEquals("Practice site", titleText.innerText());
    }

    @Test
    @Order(3)
    @DisplayName("Check icon")
    void imageIconTest() {
        Locator imageIcon = page.locator("xpath=//img[@src = 'img/hands-on-icon.png']");

        assertTrue(imageIcon.isVisible());

        imageIcon.click();
        assertEquals("https://github.com/bonigarcia/selenium-webdriver-java", page.url());
    }

    @Test
    @Order(4)
    @DisplayName("Check web form title")
    void webFormTitleTextTest() {
        Locator webFormTitleText = page.locator(".display-6");

        assertEquals("Web form", webFormTitleText.innerText());
    }

    @Test
    @Order(5)
    @DisplayName("Check web form field names")
    void webFormFieldNamesTest() {
        var formFields = page.locator(".form-label.w-100").allInnerTexts();
        var checkBoxFields = page.locator(".form-check-label.w-100").allInnerTexts();

        assertEquals("Text input", formFields.get(0), "Text Input Field Name");
        assertEquals("Password", formFields.get(1), "Password Input Field Name");
        assertEquals("Textarea", formFields.get(2), "Disabled Input Field Name");
        assertEquals("Disabled input", formFields.get(3), "Disabled Input Field Name");
        assertEquals("Readonly input", formFields.get(4), "Readonly Input Field Name");
        assertEquals("Dropdown (select)", formFields.get(5).split("\n")[0].trim(), "Dropdown (select) Field Name");
        assertEquals("Dropdown (datalist)", formFields.get(6), "Dropdown (datalist) Field Name");
        assertEquals("File input", formFields.get(7), "File Input Field Name");
        assertEquals("Checked checkbox", checkBoxFields.get(0), "Checked Checkbox Field Name");
        assertEquals("Default checkbox", checkBoxFields.get(1), "Default Checkbox Field Name");
        assertEquals("Checked radio", checkBoxFields.get(2), "Checked Radio Field Name");
        assertEquals("Default radio", checkBoxFields.get(3), "Default Radio Field Name");
        assertEquals("Color picker", formFields.get(8), "Color Picker Field Name");
        assertEquals("Date picker", formFields.get(9), "Date Picker Field Name");
        assertEquals("Example range ", formFields.get(10), "Range Input Field Name");
    }

    @Test
    @Order(6)
    @DisplayName("Check user field")
    void textInputTest() {
        HomePage homePage = new HomePage(page);
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();

        webFormPage.inputLogin(config.getUsername());
        String actualText = page.inputValue("#my-text-id");

        assertThat(actualText).isNotEmpty();
    }

    @Test
    @Order(7)
    @DisplayName("Check clear user field")
    void textInputClearTest() {
        HomePage homePage = new HomePage(page);
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();

        webFormPage.inputLogin(config.getUsername());
        webFormPage.clearTextValue();
        String actualText = page.inputValue("#my-text-id");

        assertThat(actualText).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("Check password field")
    void passwordInputTest() {
        HomePage homePage = new HomePage(page);
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();

        webFormPage.inputPassword(config.getPassword());
        String actualPassword = page.inputValue("css=[name='my-password']");

        assertThat(actualPassword).isNotEmpty();
    }

    @Test
    @Order(9)
    @DisplayName("Check clear password field")
    void passwordInputClearTest() {
        HomePage homePage = new HomePage(page);
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();

        webFormPage.inputPassword(config.getPassword());
        webFormPage.clearPasswordValue();
        String actualPassword = page.inputValue("css=[name='my-password']");

        assertThat(actualPassword).isEmpty();
    }

    @Test
    @Order(10)
    @DisplayName("Check textarea field")
    void textAreaFieldTest() {
        Locator textAreaInputField = page.locator("css=[name='my-textarea']");

        textAreaInputField.fill(BIG_TEXT);

        assertEquals(BIG_TEXT, textAreaInputField.inputValue());
    }

    @Test
    @Order(11)
    @DisplayName("Check clear textarea field")
    void textAreaFieldClearTest() {
        Locator textAreaInputField = page.locator("css=[name='my-textarea']");

        textAreaInputField.fill(BIG_TEXT);
        textAreaInputField.clear();

        assertEquals(EMPTY_VALUE, textAreaInputField.inputValue());
    }

    @Test
    @Order(12)
    @DisplayName("Check Disabled input field")
    void disabledInputFieldTest() {
        Locator disabledInputField = page.locator("css=[name='my-disabled']");

        assertFalse(disabledInputField.isEnabled());
        assertThat(disabledInputField.getAttribute("placeholder")).contains("Disabled input");

        System.out.println(page.locator("css=[name='my-disabled']").getAttribute("placeholder"));
    }

    @Test
    @Order(13)
    @DisplayName("Check Readonly input field")
    void readonlyInputFieldTest() {
        Locator readonlyInputField = page.locator("css=[name='my-readonly']");

        assertTrue(readonlyInputField.isEnabled());
        assertFalse(readonlyInputField.isEditable());
        assertEquals("Readonly input", readonlyInputField.inputValue());

        System.out.println(readonlyInputField.inputValue());
    }

    @ParameterizedTest(name = "Check Dropdown (select) menu by visible text")
    @Order(14)
    @ValueSource(strings = {"One", "Two", "Three"})
    public void dropdownSelectByVisibleTextTest(String option) {
        Locator dropdownSelectMenu = page.locator("css=.form-select");

        assertEquals(DROPDOWN_SELECT_TEXT, dropdownSelectMenu.inputValue());

        dropdownSelectMenu.selectOption(option);

//        assertEquals(option, dropdownSelectMenu.inputValue());
    }

    @ParameterizedTest(name = "Check Dropdown (select) menu by value")
    @Order(15)
    @ValueSource(strings = {"1", "2", "3"})
    public void dropdownSelectByValueTest(String value) {
        Locator dropdownSelectMenu = page.locator("css=[name='my-select']");

        assertEquals(DROPDOWN_SELECT_TEXT, dropdownSelectMenu.inputValue());

        dropdownSelectMenu.selectOption(value);

        assertEquals(value, dropdownSelectMenu.inputValue());
    }

    @ParameterizedTest(name = "Check Dropdown (datalist) menu")
    @Order(16)
    @ValueSource(strings = {"San Francisco", "New York", "Seattle", "Los Angeles", "Chicago"})
    void dropdownDataListTest(String city) {
        Locator dropdownDataList = page.locator("css=[name='my-datalist']");

        dropdownDataList.fill(city);

        assertEquals(city, dropdownDataList.inputValue());
    }

    @Test
    @Order(17)
    @DisplayName("Check File input field")
    void fileInputTest() {
        String selectFile = System.getProperty("user.dir") + "/src/main/resources/STE In Banner.jpg";

        page.setInputFiles("css=input[name='my-file']", Path.of(selectFile));

        Locator submit = page.locator("xpath=//button[text()='Submit']");
        submit.click();

        assertThat(page.url()).contains("STE+In+Banner.jpg");
    }

    @Test
    @Order(18)
    @DisplayName("Check checked Checkbox")
    void checkedCheckboxTest() {
        Locator checkedCheckbox = page.locator("#my-check-1");

        assertTrue(checkedCheckbox.isChecked());

        checkedCheckbox.click();

        assertFalse(checkedCheckbox.isChecked());
    }

    @Test
    @Order(19)
    @DisplayName("Check default Checkbox")
    void defaultCheckboxTest() {
        Locator checkedCheckbox = page.locator("#my-check-2");

        assertFalse(checkedCheckbox.isChecked());

        checkedCheckbox.click();

        assertTrue(checkedCheckbox.isChecked());
    }

    @Test
    @Order(20)
    @DisplayName("Check radio buttons")
    void radioButtonsTest() {
        Locator checkedRadioButton = page.locator("#my-radio-1");
        Locator defaultRadioButton = page.locator("#my-radio-2");

        assertTrue(checkedRadioButton.isChecked());
        assertFalse(defaultRadioButton.isChecked());

        defaultRadioButton.click();

        assertFalse(checkedRadioButton.isChecked());
        assertTrue(defaultRadioButton.isChecked());
    }

    @Test
    @Order(21)
    @DisplayName("Check Color picker")
    void colorPickerTest() {
        Locator colorPicker = page.locator("css=[name='my-colors']");

        String initColor = colorPicker.inputValue();
        System.out.println("The initial color is " + initColor);

        Color green = new Color(0, 255, 0, 1);
        colorPicker.fill(green.asHex());

        String finalColor = colorPicker.inputValue();
        System.out.println("The initial color is " + finalColor);
        assertThat(finalColor).isNotEqualTo(initColor);
        assertThat(Color.fromString(finalColor)).isEqualTo(green);
    }

    @Test
    @Order(22)
    @DisplayName("Check Date picker")
    void datePickerTest() {
        Locator dateBox = page.locator("css=[name='my-date']");

        dateBox.click();
        dateBox.fill(CURRENT_DATE);

        assertEquals(CURRENT_DATE, dateBox.inputValue());
    }

    @Test
    @Order(23)
    @DisplayName("Check Example range")
    void actionAPIMouseExampleRangeTest() {
        Locator rangeElement = page.locator("css=[name='my-range']");

        int width = (int) rangeElement.boundingBox().width;
        System.out.println("width = " + width);
        int x = (int) rangeElement.boundingBox().x;
        System.out.println("x = " + x);
        int y = (int) rangeElement.boundingBox().y;
        System.out.println("y = " + y);

        for (int i = 5; i <= 10; i++) {
            page.mouse().move((x + (double) width / 10 * i), y);
            page.mouse().down();
            page.mouse().up();
        }

        assertThat(rangeElement.inputValue()).isEqualTo("10");
    }

    @Test
    @Order(24)
    @DisplayName("Check Return to index link")
    void returnToIndexLinkTest() {
        Locator returnToIndexLink = page.locator("xpath=//a[@href = './index.html']");

        returnToIndexLink.click();

        assertEquals("https://bonigarcia.dev/selenium-webdriver-java/index.html", page.url());
    }

    @Test
    @Order(25)
    @DisplayName("Check Copyright link")
    void boniGarciaLinkTest() {
        Locator boniGarciaLink = page.locator("xpath=//a[@href = 'https://bonigarcia.dev/']");

        boniGarciaLink.click();

        assertEquals("https://bonigarcia.dev/", page.url());
    }

    @Test
    @Order(26)
    @DisplayName("Check Copyright text")
    void copyrightTextTest() {
        Locator copyrightText = page.locator("xpath=//span[@class = 'text-muted' and normalize-space(text()) = 'Copyright © 2021-2025']");

        assertEquals("Copyright © 2021-2025 Boni García", copyrightText.innerText());
    }

    @Test
    @Order(27)
    @DisplayName("Check submit button")
    void submitButtonTest() {
        HomePage homePage = new HomePage(page);
        homePage.open();
        WebFormPage webFormPage = homePage.openWebFormPage();
        webFormPage.submit();

        assertThat(page.url()).contains("https://bonigarcia.dev/selenium-webdriver-java/submitted-form.html");

        Locator formSubmittedText = page.locator("xpath=//h1[@class = 'display-6']");

        assertEquals("Form submitted", formSubmittedText.innerText());
    }
}
