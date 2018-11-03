package bs.jnks;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTestNgTest extends TestNgTestBase {

    @Test
    public void testHomePageHasAHeader() {
        driver.get(baseUrl);
        Assert.assertFalse("".equals(driver.findElement(By.xpath("//h1")).getText()));
    }
}
