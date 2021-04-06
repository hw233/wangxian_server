package com.xuanzhi.tools.query;

import com.xuanzhi.tools.query.net.MobileClient;

/**
 */
public interface MobileQuery {

    /**
     * query the mobile and get the props
     * @param mobile mobile phone number
     * @return the props
     * @see MobileClient.STATUS
     * @throws Exception any Exception we can meet
     */

    public MobileClient.STATUS query(String mobile) throws Exception;

    /**
     * query mobile safely
     * and if the first query is failed ,it will wait sometime and try again
     * if the second time is also failed,then the default status value will been given
     * @param mobile mobile phone number
     * @return the status
     */

    public MobileClient.STATUS querySafe(String mobile);
}
