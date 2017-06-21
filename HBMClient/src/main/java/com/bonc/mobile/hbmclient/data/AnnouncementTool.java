package com.bonc.mobile.hbmclient.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.bonc.mobile.hbmclient.common.ActionConstant;
import com.bonc.mobile.hbmclient.util.DateUtil;
import com.bonc.mobile.hbmclient.util.HttpUtil;

/**
 * 此类封装了公告牌与后台交互信息
 *
 */
public class AnnouncementTool {
	private SQLHelper sqlHelper;

	public AnnouncementTool() {
		this.sqlHelper = new SQLHelper();
	}

	// 向服务器端发请求，获得未读信息并插入数据库
	public void getAnnouncement() {
		int flow_id = getMaxID();
		List<Map<String, Object>> resultList = getAnnouncement(flow_id);
		insertDB(resultList);
	}

	// 获得每个用户未读的公告信息的数量
	public int querycountMessage() {
		String sql = "select count(*) as count from Message";

		Map<String, String> map = new SQLHelper().queryForMap(sql, null);

		int count = 0;

		try {
			count = Integer.parseInt(map.get("count"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	// 获得本地数据库的最大Message_ID
	public int getMaxID() {
		String sql = "select MESSAGEID as maxId from Message order by MESSAGEID desc limit 1";
		Map<String, String> map = new SQLHelper().queryForMap(sql, null);
		int latestDate = -1;
		try {
			String maxId = map.get("maxId");
			if (maxId != null && maxId.length() > 0) {
				latestDate = Integer.parseInt(maxId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latestDate;
	}

	// 向本地数据库中插入数据
	private void insertDB(List<Map<String, Object>> resultList) {

		if (resultList == null || resultList.size() == 0) {
			return;
		}

		List<ContentValues> valueList = new ArrayList<ContentValues>();
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			ContentValues cv = new ContentValues();
			cv.put("MessageId", (String) resultList.get(i).get("MessageId"));
			cv.put("Create_date", (String) resultList.get(i).get("Create_date"));
			cv.put("Message", (String) resultList.get(i).get("Message"));
			cv.put("Send_ID", (String) resultList.get(i).get("Send_ID"));
			cv.put("Send_Name", (String) resultList.get(i).get("Send_Name"));
			cv.put("Out_date", (String) resultList.get(i).get("Out_date"));
			cv.put("Statue", 0);
			valueList.add(cv);
		}
		new SQLHelper().batchInsert("Message", valueList);
	}

	// 当此信息已经阅读之后，将读取标志修改成已读
	public void updateDB(Integer MessageId) {
		String sql = "update Message set Statue=1 where MessageId=? ";
		new SQLHelper().update(sql, new Integer[] { MessageId });

	}

	private void updateAllAnnouncementAsReaded() {
		String sql = "update Message set Statue=1 ";
		this.sqlHelper.execute(sql);
	}

	private void deleteAllAnnouncement() {
		String sql = "delete from Message ";
		this.sqlHelper.execute(sql);
	}

	public void reloadAllAnnouncement(boolean asRead) {
		this.deleteAllAnnouncement();
		this.getAnnouncement();
		if (asRead) {
			this.updateAllAnnouncementAsReaded();
		}
	}

	// 查询本地数据库，将本地数据库中的所有数据信息显示在页面上
	public List<Map<String, String>> queryMessage() {
		String sql = "select MessageId as MessageId ,Message as Message,Create_date as Create_date,STATUE as MessageStatus from Message order by Create_date desc ";

		return new SQLHelper().queryForList(sql, null);
	}

	// 查询本地数据库，将本地数据库中的所有未过期信息提取出来
	public List<Map<String, String>> queryNotOutDateMessage() {
		String sql = "select Messageid as MessageId,Message as Message,Create_date as Create_date,STATUE as MessageStatus from Message where out_date>=? order by create_date desc";
		String currentdate = DateUtil.formatter(new Date(), DateUtil.PATTERN_8);
		return new SQLHelper().queryForList(sql, new String[] { currentdate });
	}

	// 将本地数据库中的未读未过期数据信息提取出来
	public List<Map<String, String>> queryUnreadMessage() {
		String sql = "select MessageId as MessageId,Message as Message,Create_date as Create_date,statue as MessageStatus from Message where out_date >=? and statue=0 order by Create_date desc ";
		String currentdate = DateUtil.formatter(new Date(), DateUtil.PATTERN_8);
		return new SQLHelper().queryForList(sql, new String[] { currentdate });
	}

	// 查询本地数据库，将本地数据库中的已读数据信息显示在页面上
	public List<Map<String, String>> queryReadMessage(int start, int length) {
		String sql = "select MessageId as MessageId,Message as Message,Create_date as Create_date  from Message where Statue=1 and "
				+ "Out_date >=? order by Create_date desc limit ?,?";

		List<Map<String, String>> resuList = new SQLHelper().queryForList(sql,
				new String[] { System.currentTimeMillis() + "", start + "",
						length + "" });

		if (resuList == null) {
			return new ArrayList<Map<String, String>>(0);
		}

		int len = resuList.size();
		SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		t.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		for (int i = 0; i < len; i++) {
			try {
				Map<String, String> map = resuList.get(i);
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(Long.parseLong(map.get("CREATE_DATE")));
				map.put("Create_date", t.format(c.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resuList;
	}

	/**
	 * @param methodName
	 *            调用服务器方法名
	 * @param map
	 *            方法参数
	 * @return 包含有广告牌消息的Map对象
	 */
	public List<Map<String, Object>> getAnnouncement(int flowId) {
		String reply_json;
		JSONArray array;
		JSONObject item;
		List<Map<String, Object>> reply_list = new ArrayList<Map<String, Object>>();

		Map<String, String> param = new HashMap<String, String>();
		param.put("flowid", "" + flowId);

		reply_json = HttpUtil.sendRequest(ActionConstant.NOTICE_INFO, param);
		if (reply_json != null && reply_json.length() > 1) {
			try {
				array = new JSONArray(reply_json);
				for (int i = 0; i < array.length(); i++) {
					item = array.getJSONObject(i);
					Map<String, Object> reply_map = new HashMap<String, Object>();
					reply_map.put("MessageId", item.get("flow_id"));
					reply_map.put("Message", item.get("notice_msg"));
					reply_map.put("Create_date",
							item.get("notice_publish_date"));

					if (item.get("notice_expire_date") != null) {
						reply_map.put("Out_date",
								item.get("notice_expire_date"));
					} else {
						reply_map.put("Out_date", null);
					}

					reply_map.put("Send_ID", item.get("notice_user_code"));
					reply_map.put("Send_Name", item.get("notice_user_name"));
					reply_list.add(reply_map);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return reply_list;
	}
}
