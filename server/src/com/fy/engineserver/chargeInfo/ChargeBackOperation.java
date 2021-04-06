/**
 * 
 */
package com.fy.engineserver.chargeInfo;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public abstract class ChargeBackOperation implements Serializable{
	
	long playerId;
	
	/**
	 * 
	 */
	public ChargeBackOperation(long playerId) {
		// TODO Auto-generated constructor stub
		this.playerId=playerId;
	}
	
	public abstract void doOperation();
	
	public abstract boolean isDisabled();

}
