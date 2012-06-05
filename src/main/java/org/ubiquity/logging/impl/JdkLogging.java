package org.ubiquity.logging.impl;

import org.ubiquity.logging.Logger;
import org.ubiquity.logging.LoggerFactory;

import java.util.logging.Level;

/**
 * Logging configuration for Jdk loggers.
 * <br />
 * Date: 05/06/12
 * Time: 11:40
 *
 * @author françois LAROCHE
 */
public class JdkLogging {

    public LoggerFactory getJdkLoggerFactory() {
        return Factory.INSTANCE;
    }

    private enum Factory implements LoggerFactory {
        INSTANCE;


        @Override
        public Logger getLogger(Class<?> c) {
            return new JdkLogger(java.util.logging.Logger.getLogger(c.getName()));
        }
    }

    private static class JdkLogger implements Logger {
        private final java.util.logging.Logger logger;

        JdkLogger(java.util.logging.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void trace(String message) {
            logger.finest(message);
        }

        @Override
        public void debug(String message) {
            logger.finer(message);
        }

        @Override
        public void info(String message) {
            logger.info(message);
        }

        @Override
        public void warn(String message) {
            logger.warning(message);
        }

        @Override
        public void error(String message) {
            logger.severe(message);
        }

        @Override
        public void error(String message, Throwable e) {
            logger.severe(message + " : " + e.getMessage());
            if(logger.isLoggable(Level.SEVERE)) {
                e.printStackTrace();
            }
        }
    }
}
