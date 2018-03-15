package com.vanilla.remoting.exchange;

import com.vanilla.remoting.RemotingException;

public interface ResponseFuture {

    /**
     * get result.
     *
     * @return result.
     */
    Object get() throws RemotingException;

    /**
     * get result with the specified timeout.
     *
     * @param timeoutInMillis timeout.
     * @return result.
     */
    Object get(long timeoutInMillis) throws RemotingException;

    /**
     * check is done.
     *
     * @return done or not.
     */
    boolean isDone();
    
}
