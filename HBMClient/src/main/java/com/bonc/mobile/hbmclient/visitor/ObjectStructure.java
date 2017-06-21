/**
 * ObjectStructure
 */
package com.bonc.mobile.hbmclient.visitor;

import java.util.ArrayList;
import java.util.List;

import com.bonc.mobile.hbmclient.composite.IComposite;

/**
 * @author liweigao
 *
 */
public class ObjectStructure implements INode, IComposite<INode> {
	private List<INode> nodes = new ArrayList<INode>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.visitor.INode#accept(com.bonc.mobile.hbmclient
	 * .visitor.IVisitor)
	 */
	@Override
	public void accept(IVisitor visitor) {
		// TODO Auto-generated method stub
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).accept(visitor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.IComposite#add(java.lang.Object)
	 */
	@Override
	public void add(INode node) {
		this.nodes.add(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.IComposite#remove(java.lang.Object)
	 */
	@Override
	public void remove(INode node) {
		this.nodes.remove(node);
	}

}
