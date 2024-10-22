package com.fy.gamegateway.thirdpartner.qq.mime.qweibo;

public interface QAsyncHandler {

	/**
	 * Invoked when an unexpected exception occurs during the processing of the
	 * response
	 * 
	 * @param t
	 *            a {@link Throwable}
	 */
	public void onThrowable(Throwable t, Object cookie);

	/**
	 * Invoked once the HTTP response processing is finished.
	 * <p/>
	 * 
	 * Gets always invoked as last callback method.
	 * 
	 */
	public void onCompleted(int statusCode, String Content, Object cookie);
}
