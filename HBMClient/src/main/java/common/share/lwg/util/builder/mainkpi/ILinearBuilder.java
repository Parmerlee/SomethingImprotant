/**
 * ILinearProduct
 */
package common.share.lwg.util.builder.mainkpi;

import common.share.lwg.util.builder.IBuilder;

/**
 * @author liweigao
 * 
 */
public interface ILinearBuilder<T> extends IBuilder {
	T getProduct();
	
	void buildColumnType(Object... obj);
	void buildColumnType0(Object... obj);
	void buildColumnType1(Object... obj);
	void buildColumnType2(Object... obj);
	void buildAppearance(Object... obj);
}
