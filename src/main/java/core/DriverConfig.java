package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DriverConfig {
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static String separator = File.separator;

    private DriverConfig() {
    }

    static String getPropertyValue(String property) {
        Properties prop = new Properties();
        InputStream input = null;
        String resources = "src" + separator + "main" + separator + "resources" + separator;
        try {
            String propFileName = "config.properties";
            input = new FileInputStream(resources + propFileName);
            prop.load(input);
            String propValue = prop.getProperty(property);
            propValue = removeExeForChromeDriver(propValue);
            //Replace win separator to system separator
            return propValue.replace("\\", separator);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isMac() {
        return (OS.contains("mac"));
    }

    private static String removeExeForChromeDriver(String prop) {
        if (isMac() && prop.contains("chrome")) {
            return prop.replace(".exe", "");
        }
        return prop;
    }
}
