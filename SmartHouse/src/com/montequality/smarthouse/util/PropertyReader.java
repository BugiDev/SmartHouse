package com.montequality.smarthouse.util;

import android.annotation.TargetApi;
import android.os.Build;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Class PropertyReader that is used to read the preferences from the specified
 * .properties file. It is coded like a singleton to avoid unnecessary
 * duplication of objects and it helps with keeping the configuration at one
 * place.
 *
 * Date 6/4/2013
 *
 * @author Bogdan Begovic
 */
public class PropertyReader {

    private static final PropertyReader propertyReader = new PropertyReader();

    /*
     * Empty constructor
     */
    private PropertyReader() {
    }

    /**
     * Method that is returning the instance of this class. It is synchronized
     * to avoid thread safety issues.
     *
     * @return propertyReader of type PropertyReader
     */
    public static synchronized PropertyReader getInstance() {
        // return the single instance of PropertyReader
        // each time this method is called             
        return propertyReader;
    }

    /**
     * Method that reads the specified property from the specified property
     * file.
     *
     * @param pathToPropertiesFile type of String
     * @param propertyFieldToRead type of String
     * @return String representing the value of the property
     */
    public String readProperty(String pathToPropertiesFile, String propertyFieldToRead) throws IOException {
        Properties prop = new Properties();

        try {
            //load a properties file
            prop.load(new FileInputStream(pathToPropertiesFile));
            return prop.getProperty(propertyFieldToRead);

        } catch (IOException e) {

        }
        return null;
    }

    /**
     * Method that reads the all configuration properties file.
     *
     * @param inputStream - InputStream to load properties from
     *
     * @return Loaded Properties object
     */
    public Properties readConfigProperties(InputStream inputStream) throws IOException {

        final Properties prop = new Properties();

        prop.load(inputStream);

        // We're done here, but let's just log loaded props

        printOutProperties(prop);

        return prop;
    }

    /**
     * Logs property key-value pairs of provided Properties object.
     *
     * @param prop Properties object to log property key-value pairs of.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void printOutProperties(final Properties prop) {

        final StringBuilder sb = new StringBuilder("Read from properties: ");

        final Object [] values = new Object[ prop.size() ];
        int i = 0;

        for (final String key : prop.stringPropertyNames()) {

            sb.append( key ).append("\n = {}");
            values[ i++ ] = prop.getProperty(key);
        }

    }

    /*
     * Method that forbids cloning
     */
    @Override
    public Object clone() throws CloneNotSupportedException {

        throw new CloneNotSupportedException();
    }
}
