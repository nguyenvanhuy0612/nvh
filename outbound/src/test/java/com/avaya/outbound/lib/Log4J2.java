package com.avaya.outbound.lib;

import org.apache.logging.log4j.core.config.Configurator;

public class Log4J2 {

    public Log4J2() {
    }

    public static void init() {
        Configurator.initialize(null, EnvSetup.LOGGER_PROPERTY_FILE);
    }
}
