package com.cielyang.android.login.common.http;

import android.support.annotation.NonNull;

/** The universal interface of Http client. */
public interface HttpClient {

    /**
     * Get method.
     *
     * @param request    the request
     * @param forceCache whether to use cache
     * @return the response
     */
    Response get(@NonNull Request request, boolean forceCache);

    /**
     * Post method.
     *
     * @param request    the request
     * @param forceCache whether to use cache
     * @return the response
     */
    Response post(@NonNull Request request, boolean forceCache);

    /**
     * Put method.
     *
     * @param request    the request
     * @param forceCache whether to use cache
     * @return the response
     */
    Response put(@NonNull Request request, boolean forceCache);

    /**
     * Delete method.
     *
     * @param request    the request
     * @param forceCache whether to use cache
     * @return the response
     */
    Response delete(@NonNull Request request, boolean forceCache);
}
