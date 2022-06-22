package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class BritPAge {
    public BritPAge() {
        PageFactory.initElements(Driver.get(), this);
    }

    @FindBy(id = "login")
    public WebElement username;
    @FindBy(id = "password")
    public WebElement password;
    @FindBy(xpath = "//*[@class='btn btn-primary']")
    public WebElement loginButton;

    public void login(String usr, String pass) {
        Driver.get().get("https://app.briteerp.com/web/login");
        this.username.sendKeys(usr);
        this.password.sendKeys(pass);
        this.loginButton.click();

    }
}
