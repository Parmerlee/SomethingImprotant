/**
 * LinearDirector
 */
package common.share.lwg.util.builder.mainkpi;

import java.util.List;
import java.util.Map;

/**
 * @author liweigao
 *
 */
public class LinearDirector {
	
	public void buildTitle(LTitleBuilder builder,List<Map<String,String>> columnInfo) {
		builder.buildAppearance();
		for(int i=0;i<columnInfo.size();i++) {
			Map<String,String> item = columnInfo.get(i);
			String columnType = item.get("COLUMNTYPE");
			if("0".equals(columnType)) {
				builder.buildColumnType0(item);
			} else if("1".equals(columnType)) {
				builder.buildColumnType1(item);
			} else if("2".equals(columnType)) {
				builder.buildColumnType2(item);
			} else {
				builder.buildColumnType(item);
			}
		}
	}
	
	public void buildKpiRow(LKpiRowBuilder builder,List<Map<String,String>> columnInfo) {
		for(int i=0;i<columnInfo.size();i++) {
			Map<String,String> item = columnInfo.get(i);
			
			String columnType = item.get("COLUMNTYPE");
			
			if("0".equals(columnType)) {
				builder.buildColumnType0(item);
			} else if("1".equals(columnType)) {
				builder.buildColumnType1(item);
			} else if("2".equals(columnType)) {
				builder.buildColumnType2(item);
			} else {
				builder.buildColumnType(item);
			}
		}
	}
}
