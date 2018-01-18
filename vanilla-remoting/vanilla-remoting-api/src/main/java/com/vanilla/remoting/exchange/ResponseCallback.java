package com.vanilla.remoting.exchange;

public interface ResponseCallback {

	/**
     * done.
     *
     * @param response
     */
    void done(Object response);

    /**
     * caught exception.
     *
     * @param exception
     */
    void caught(Throwable exception);
}
