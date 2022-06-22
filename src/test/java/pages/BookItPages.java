package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class BookItPages {
    public BookItPages() {
        PageFactory.initElements(Driver.get(), this);
    }
    @FindBy(xpath = "(//div[@class='media-content']//p[1])[1]")
    public WebElement nameUI;

    By email = By.xpath("//input[@name='email']");
    By password = By.xpath("//input[@name='password']");
    By loginButton = By.xpath("//button[@type='submit']");
    By my = By.xpath("(//a[@class='navbar-link'])[2]");
    By self = By.xpath("//a[.='self']");
    By logout = By.xpath("//a[.='sign out']");
    Actions actions = new Actions(Driver.get());

    public void login(String email, String password) {
        Driver.get().findElement(this.email).sendKeys(email);
        Driver.get().findElement(this.password).sendKeys(password);
        Driver.get().findElement(this.loginButton).click();
    }

    public void mySelf() {
        actions.moveToElement(Driver.get().findElement(this.my)).click(Driver.get().findElement(this.self)).build().perform();
    }

    public void logout() {
        actions.moveToElement(Driver.get().findElement(this.my)).click(Driver.get().findElement(this.logout)).build().perform();
    }
}


