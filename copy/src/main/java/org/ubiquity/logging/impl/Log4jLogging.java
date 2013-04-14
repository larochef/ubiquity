package org.ubiquity.logging.impl;

import org.ubiquity.logging.Logger;
import org.ubiquity.logging.LoggerFactory;

/**
 * Logging configuration for Log4j.
 * <br />
 * Date: 05/06/12
 * Time: 10:57
 *
 * @author françois LAROCHE
 */
public class Log4jLogging {

    public static LoggerFactory getLog4jFactory() {
        return Factory.INSTANCE;
    }

    private enum Factory implements LoggerFactory {
        INSTANCE;

        @Override
        public Logger getLogger(Class<?> c) {
            return new Log4jLogger(org.apache.log4j.Logger.getLogger(c));
        }
    }

    private static class Log4jLogger implements Logger {
        private final org.apache.log4j.Logger logger;

        Log4jLogger(org.apache.log4j.Logger logger) {
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
