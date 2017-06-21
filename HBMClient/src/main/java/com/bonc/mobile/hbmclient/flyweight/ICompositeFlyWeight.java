/**
 * 
 */
package com.bonc.mobile.hbmclient.flyweight;

import com.bonc.mobile.hbmclient.composite.IComposite;

/**
 * @author liweigao 多继承接口中尽量避免出现同名方法，虽然不会出问题，但是不容易理解
 */
public interface ICompositeFlyWeight<S, U> extends IComposite<S>,
		IFlyWeight<S, U> {

}
