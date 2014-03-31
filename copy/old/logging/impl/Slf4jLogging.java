package org.ubiquity.copy.logging.impl;

import org.ubiquity.copy.logging.Logger;
import org.ubiquity.copy.logging.LoggerFactory;

/**
* Logging configuration for slf4j.
* <br />
* Date: 05/06/12
* Time: 11:05
*
* @author françois LAROCHE
*/
public class Slf4jLogging {

    public static LoggerFactory getSlf4jFactory() {
        return Factory.INSTANCE;
    }

    private enum Factory implements LoggerFactory {
        INSTANCE;

        @Override
        public Logger getLogger(Class<?> c) {
            return new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(c));
        }
    }

    private static class Slf4jLogger implements Logger {
        private final org.slf4j.Logger logger;

        Slf4jLogger(org.slf4j.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void trace(String message) {
            this.logger.trace(message);
        }

        @Override
        public void debug(String message) {
            this.logger.debug(message);
        }

        @Override
        public void info(String message) {
            this.logger.info(message);
        }

        @Override
        public void warn(String message) {
            this.logger.warn(message);
        }

        @Override
        public void error(String message) {
            this.logger.error(message);
        }

        @Override
        public void error(String message, Throwable e) {
            this.logger.error(message, e);
        }
    }
}
