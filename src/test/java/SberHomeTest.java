import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.opera.OperaDriver;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SberHomeTest extends AbstractTester{
    private Properties properties = new Properties();
    @Before
    public void init() throws IOException {
        File file = new File("src\\test\\resources\\application.properties");
        System.out.println(file.getAbsolutePath());
        properties.load(new FileInputStream(file));
        String webDrForm = "webdriver.%s.driver";
        switch (properties.getProperty("browser")){
            case("chrome"):
                webDrForm = String.format(webDrForm, "chrome");
                System.setProperty(webDrForm, properties.getProperty(webDrForm));
                driver = new ChromeDriver();
                break;
            case("opera"):
                System.setProperty(String.format(webDrForm, "opera"), properties.getProperty(String.format(webDrForm, "opera")));
                driver = new OperaDriver();
                break;
            case("edge"):
                webDrForm = String.format(webDrForm, "edge");
                System.setProperty(webDrForm, properties.getProperty(webDrForm));
                driver = new EdgeDriver();
                break;
                default:
                    System.exit(-1);

        }
        /*System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://www.sberbank.ru/ru/person");*/
        driver.get(properties.getProperty("url"));
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    @After
    public void destr(){
        driver.close();
        driver.quit();
    }

    @Test
    public void SberTest() {

        WebElement elem = findAndActivateElem("//span[contains(text(), 'Страхование')]/parent::*");

        /*Point coordinates = elem.getLocation();
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.mouseMove(coordinates.getX(),coordinates.getY()+200);
        robot.mousePress(InputEvent.BUTTON1_MASK);*/

        findAndActivateElem("//span[contains(text(), 'Страхование')]/parent::*/following::*[contains(text(), 'Путешествия и покупки')][1]");
        //robot.mouseRelease(InputEvent.BUTTON1_MASK);
        String res1 = "Страхование путешественников";
        Assert.assertEquals("\nНет надписи "+res1, res1, findAndActivateElem("//*[contains(text(),'Страхование путешественников')]").getText());

        String ref = findAndActivateElem("//a[contains(text(), 'Оформить онлайн')]").getAttribute("href");

        changePage(ref);

        findAndActivateElem("//div[contains(text(), 'Минимальная')]/..");
        findAndActivateElem("//span[contains(text(), 'Оформить')]/../..");

        List<String> inputData = new ArrayList<String>();
        Collections.addAll(inputData, "Ivan", "Ivanov", "14.05.1994", "Шультц", "Андрей", "Петрович", "12.09.1999", "1234", "567890", "12.10.2013");

        assertingValues(inputData, fillingFillTab());

        findAndActivateElem("//*[@ng-click='save()']");
        String res2 = "Заполнены не все обязательные поля";
        Assert.assertEquals("\nНет надписи " + res2, res2, findAndActivateElem("//div[@ng-show='tryNext && myForm.$invalid']").getText());

    }
    private List<WebElement> fillingFillTab(){
        List<WebElement> elements = new ArrayList<WebElement>();

        elements.add(fillData("//input[@name='insured0_surname']", "Ivan"));
        elements.add(fillData("//input[@name='insured0_name']", "Ivanov"));
        elements.add(fillData("//input[@name='insured0_birthDate']", "14.05.1994"));
        elements.add(fillData("//input[@name='surname']", "Шультц"));
        elements.add(fillData("//input[@name='name']", "Андрей"));
        elements.add(fillData("//input[@name='middlename']", "Петрович"));
        elements.add(fillData("//input[@name='birthDate']", "12.09.1999"));
        findAndActivateElem("//input[@name='male']");
        elements.add(fillData("//input[@placeholder='Серия']", "1234"));
        elements.add(fillData("//input[@placeholder='Номер']", "567890"));
        elements.add(fillData("//input[@name='issueDate']", "12.10.2013"));

        return elements;
    }

}
