package org.ubiquity.logging.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ubiquity.logging.Logger;
import org.ubiquity.logging.LoggerFactory;

/**
 * Logging configuration for commons-logging.
 * <br />
 *
 * Date: 05/06/12
 * Time: 11:14
 *
 * @author françois LAROCHE
 */
public class CommonsLoggingLogging {

    public static LoggerFactory getCommonsLoggingFactory() {
        return Factory.INSTANCE;
    }

    private enum Factory implements LoggerFactory {
        INSTANCE;


        @Override
        public Logger getLogger(Class<?> c) {
            return new CommonsLoggingLogger(LogFactory.getLog(c));
        }
    }

    private static class CommonsLoggingLogger implements Logger {
        private final Log logger;

        CommonsLoggingLogger(Log logger) {
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
