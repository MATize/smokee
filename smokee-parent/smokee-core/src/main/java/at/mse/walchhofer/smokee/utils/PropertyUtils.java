package at.mse.walchhofer.smokee.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import at.mse.walchhofer.smokee.tests.boundary.SmokeResource;
import at.mse.walchhofer.smokee.tests.control.ConfigurationException;

public class PropertyUtils {

    static Properties properties;
    public static final String PROPS_FILE_NAME = "smoker.properties";
    public static final String PROP_NAME_JNDI_PREFIX = "project.jndi.prefix";
    public static final String PROP_JNDI_DEF_PREFIX = "java:global/";
    public static final String PROP_NAME_PACKAGE2SCAN = "package2scan";

    public String getJndiPrefixFromProperties() throws ConfigurationException {
        check();
        String namespace = properties.getProperty("jndi.namespace");
        if (namespace == null)
            namespace = PROP_JNDI_DEF_PREFIX;
        if (!"".equals(namespace))
            namespace += "/";
        String jndiPrefix = String.format("%s%s", namespace,
                properties.getProperty(PROP_NAME_JNDI_PREFIX));
        return jndiPrefix;

    }

    public String getPackage2Scan() throws ConfigurationException {
        check();
        String pkg2scan = properties.getProperty(PROP_NAME_PACKAGE2SCAN);
        return pkg2scan;
    }

    private void check() throws ConfigurationException {
        if (properties == null) {
            throw new ConfigurationException(String.format(
                    "Configuration %s with property %s required!",
                    PROPS_FILE_NAME, PROP_NAME_PACKAGE2SCAN));
        }
    }

    static {
        try {
            InputStream inputStream = SmokeResource.class.getClassLoader()
                    .getResourceAsStream("META-INF" + File.separator + PROPS_FILE_NAME);
            properties = new Properties();
            properties.load(inputStream);
        } catch (NullPointerException | IOException e) {
            properties = null;
        }
    }
}
