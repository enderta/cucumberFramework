package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

public class SpartanPages {
    public SpartanPages() {
        PageFactory.initElements(Driver.get(), this);
    }
    By form= By.id("add_spartan_btn");
    By name= By.xpath("//input[@name='name']");
    By gender=By.id("genderSelect");
    By phone=By.id("phone");
    By sumbit=By.id("submit_btn");

    public By getSumbit() {
        return sumbit;
    }

    By backHome=By.xpath("//*[.='Back to Home']");

    public By getBackHome() {
        return backHome;
    }

    By UIid=By.id("name");

    public By getForm() {
        return form;
    }

    public By getName() {
        return name;
    }

    public By getGender() {
        return gender;
    }

    public By getPhone() {
        return phone;
    }

    public By getUIid() {
        return UIid;
    }
}
