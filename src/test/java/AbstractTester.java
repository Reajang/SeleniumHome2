import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTester {
    static WebDriver driver;


    protected void changePage(String ref){
        //driver.get(ref);
        driver.navigate().to(ref);
        driver.switchTo().window(driver.getWindowHandle());
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }

    protected WebElement findAndActivateElem(String xpath){
        List<WebElement> list = driver.findElements(By.xpath(xpath));
        list.get(0).click();
        return list.get(0);
    }

    protected WebElement fillData(String xpath, String text){
        WebElement element = driver.findElement(By.xpath(xpath));
        element.sendKeys(text);
        return element;
    }
    protected void assertingValues(List<String> input, List<WebElement> actualList){
        for(int i = 0; i < input.size(); i++){
            String error = "\nНесовпадение в поле: " + actualList.get(i).findElement(By.xpath("./../../h4")).getText();
            String actual = actualList.get(i).getAttribute("value");
            Assert.assertEquals(error, input.get(i), actual);
        }
    }
}
