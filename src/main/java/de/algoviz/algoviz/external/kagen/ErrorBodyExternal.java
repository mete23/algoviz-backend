package de.algoviz.algoviz.external.kagen;

/**
 * This class represents an error body in web client repsonse
 *
 * @author David
 * @version 1.0
 */
public class ErrorBodyExternal {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    /**
     * Constructor for the ErrorBodyExternal class.
     *
     * @param timestamp the timestamp of the error
     * @param status    the status of the error
     * @param error     the error
     * @param message   the message of the error
     * @param path      the path of the error
     */
    public ErrorBodyExternal(String timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Default constructor for the ErrorBodyExternal class.
     */
    public ErrorBodyExternal() {
    }

    /**
     * Returns the timestamp of the error.
     *
     * @return the timestamp of the error
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the error.
     *
     * @param timestamp the timestamp of the error
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the status of the error.
     *
     * @return the status of the error
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status of the error.
     *
     * @param status the status of the error
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns the error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Returns the message of the error.
     *
     * @return the message of the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the error.
     *
     * @param message the message of the error
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the path of the error.
     *
     * @return the path of the error
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of the error.
     *
     * @param path the path of the error
     */
    public void setPath(String path) {
        this.path = path;
    }
}
