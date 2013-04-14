package org.ubiquity.logging;

/**
 * Interface defining a Logger.
 * These are required so that Ubiquity doesn't become bound to a specific technology,
 * and can use the logger usually used in the project.
 * <br />
 *
 * Date: 05/06/12
 * Time: 10:52
 *
 * @author françois LAROCHE
 */
public interface Logger {

    void trace(String message);
    void debug(String message);
    void info(String message);
    void warn(String message);
    void error(String message);
    void error(String message, Throwable e);
}
