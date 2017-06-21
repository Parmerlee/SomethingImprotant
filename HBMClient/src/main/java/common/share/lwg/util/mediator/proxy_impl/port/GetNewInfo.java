/**
 * GetNewMenu
 */
package common.share.lwg.util.mediator.proxy_impl.port;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.SQLHelper;
import com.bonc.mobile.hbmclient.enum_type.DateRangeEnum;
import com.bonc.mobile.hbmclient.util.DateUtil;

/**
 * @author liweigao
 *
 */
public class GetNewInfo {
	private volatile static GetNewInfo instance;
	private HashSet<String> allNewMenu;
	private BusinessDao dao = new BusinessDao();

	private GetNewInfo() {
	}

	public static GetNewInfo getSingleInstance() {
		if (instance == null) {
			synchronized (GetNewInfo.class) {
				if (instance == null) {
					instance = new GetNewInfo();
				}
			}
		}
		return instance;
	}

	public HashSet<String> getAllNewMenu() {
		return this.allNewMenu;
	}

	public void insertScan(String menuCode) {
		String clickDate = DateUtil.formatter(new Date(),
				DateRangeEnum.DAY.getDateServerPattern());
		ContentValues cv = new ContentValues();
		cv.put("MENU_CODE", menuCode);
		cv.put("newestTime", clickDate);
		new SQLHelper().insert("ScanStatistics", cv);
	}

	public boolean isNew(String flag) {
		if (this.allNewMenu != null) {
			return this.allNewMenu.contains(flag);
		} else {
			return false;
		}
	}

	public void getNew() {
		List<Map<String, String>> bottomNewMenu = dao.getNewMenu();
		if (bottomNewMenu == null)
			return;
		if (bottomNewMenu.size() <= 0)
			return;
		this.allNewMenu = new HashSet<String>();
		Iterator it = bottomNewMenu.iterator();
		while (it.hasNext()) {
			Map<String, String> m = (Map<String, String>) it.next();
			String menuCode = m.get("MENU_CODE");
			upCheck(menuCode);
		}
	}

	private void upCheck(String menuCode) {
		this.allNewMenu.add(menuCode);

		String parentMenuCode = dao.getParentMenuCode(menuCode);
		if (parentMenuCode == null) {
			return;
		}
		if ("".equals(parentMenuCode.trim())) {
			return;
		}

		if ("0".equals(parentMenuCode)) {
			String menuKind = dao.getMenuKind(menuCode);
			this.allNewMenu.add(menuKind);
		} else {
			upCheck(parentMenuCode);
		}
	}
}
