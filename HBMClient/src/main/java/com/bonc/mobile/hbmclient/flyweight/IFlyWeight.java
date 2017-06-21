/**
 * 
 */
package com.bonc.mobile.hbmclient.flyweight;

/**
 * @author liweigao
 *
 */
public interface IFlyWeight<S, U> {
	public void operation(U extrinsicState);// 经常变化的

	public S getIntrinsicState();// 不变化的
}
