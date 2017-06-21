/**
 * IVisitor
 */
package com.bonc.mobile.hbmclient.visitor;

/**
 * @author liweigao
 *         声明是静态类型，实例化是动态类型。方法重载根据静态类型，方法调用根据动态类型。visitor最好使用重载，节点node使用方法调用。
 *         所以node的方法调用
 *         （visitor.visit）传入的参数可以传静态类型（声明）,visitor的重载方法传入的参数必须是动态类型（实例化对象
 *         ，new后边的）
 */
public interface IVisitor {
	void visitor(DBNode node);
}
