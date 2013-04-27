package org.ubiquity.logging;

/**
 * Interface defining a logger factory.
 * These are required so that Ubiquity doesn't become bound to a specific technology,
 * and can use the logger usually used in the project.
 *
 * <br />
 * Date: 05/06/12
 * Time: 10:52
 *
 * @author françois LAROCHE
 */
public interface LoggerFactory {

    public Logger getLogger(Class<?> c);

}
