/**
 * 
 */
package com.bonc.mobile.hbmclient.enum_type;

/**
 * @author liweigao
 *
 */
public enum ProcedureEnum {
	START("流程开始"), END("流程结束"), EXCEPTION("流程中异常"), STATIC_END(
			"静态数据初始化完毕（例如：配置信息）");

	private final String description;

	private ProcedureEnum(String des) {
		this.description = des;
	}
}
