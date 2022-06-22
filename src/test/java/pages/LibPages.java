package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class LibPages {
    public LibPages() {
        PageFactory.initElements(Driver.get(), this);
    }
    @FindBy(id = "inputEmail")
    public WebElement email;
    @FindBy(id = "inputPassword")
    public WebElement password;
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginBtn;


    public void login(String email, String password) {
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        loginBtn.click();
    }
}
