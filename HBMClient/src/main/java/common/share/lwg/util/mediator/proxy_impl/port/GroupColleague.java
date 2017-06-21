/**
 * GroupColleague
 */
package common.share.lwg.util.mediator.proxy_impl.port;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.common.AppConstant;
import com.bonc.mobile.common.util.UIUtil;
import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.data.BusinessDao;
import com.bonc.mobile.hbmclient.data.SQLHelper;

/**
 * @author liweigao
 * 
 */
public class GroupColleague extends APortColleague {
	private List<Map<String, String>> menuGroupInfo;
	private RadioGroup radioGroup;
	private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件
	private BusinessDao dao;
	private GetNewInfo newInfo = GetNewInfo.getSingleInstance();

	/**
	 * @param activity
	 */
	public GroupColleague(Activity activity) {
		super(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.mediator.proxy_impl.port.APortColleague#create()
	 */
	@Override
	public void create() {
		dao = new BusinessDao();
		this.newInfo.getNew();
		HashSet<String> hs = this.newInfo.getAllNewMenu();
		this.menuGroupInfo = dao.getMenuGroupInfo();
		if (TextUtils.isEmpty(checkdata(menuGroupInfo))) {
			return;
		}
		this.radioGroup = (RadioGroup) this.activity
				.findViewById(R.id.radioGroup);

		this.mHorizontalScrollView = (HorizontalScrollView) radioGroup
				.getParent();

		addInRadioGroup();

		this.portMediator.setMenuGroupInfo(menuGroupInfo);

		this.radioGroup.findViewById(0).performClick();

	}

	private String checkdata(List<Map<String, String>> menuGroupInfo) {
		if (menuGroupInfo.size() == 0 || menuGroupInfo == null) {
			new AlertDialog.Builder(activity).setMessage("菜单配置数据错误")
					.setTitle(activity.getString(R.string.hint))
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							new SQLHelper().deleteDbIfExists();
							activity.finish();
						}
					}).create().show();
			return "";
		} else
			return "OK";
	}

	int partWidth;

	private void addInRadioGroup() {
		/**
		 * menuGroupInfo:[{GROUP_CHANGE=0, GROUP_KIND=A, GROUP_NAME=领导视窗,
		 * GROUP_ID=null, GROUP_ORDER=1, GROUP_STATE=null}, {GROUP_CHANGE=0,
		 * GROUP_KIND=B, GROUP_NAME=部门专题, GROUP_ID=null, GROUP_ORDER=2,
		 * GROUP_STATE=null}, {GROUP_CHANGE=1, GROUP_KIND=D, GROUP_NAME=营销专区,
		 * GROUP_ID=null, GROUP_ORDER=3, GROUP_STATE=null}, {GROUP_CHANGE=0,
		 * GROUP_KIND=C, GROUP_NAME=系统管理, GROUP_ID=null, GROUP_ORDER=4,
		 * GROUP_STATE=null}]
		 * */
		int length = menuGroupInfo.size();
		
		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		partWidth = screenWidth / 4;
		LayoutInflater inflater = LayoutInflater.from(this.activity);

		for (int i = 0; i < length; i++) {
			Map<String, String> item = menuGroupInfo.get(i);
			String groupName = item.get("GROUP_NAME");
			String groupKind = item.get("GROUP_KIND");
			View partView = inflater.inflate(R.layout.radio_group_item,
					radioGroup, false);
			TextView tv = (TextView) partView.findViewById(R.id.groupName);
			ImageView iv = (ImageView) partView.findViewById(R.id.groupChange);
			tv.setText(groupName);

			if (this.newInfo.isNew(groupKind)) {
				iv.setImageResource(R.mipmap.ic_new1);
			} else {
				iv.setImageResource(0);
			}

			partView.setTag(item);
			partView.setId(i);

			partView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = v.getId();

					focusView(index);

					portMediator.focusView(GroupColleague.this, index);
				}
			});
			LayoutParams lp = partView.getLayoutParams();
			lp.width = partWidth;
			this.radioGroup.addView(partView);
		}

//		this.radioGroup
//				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//					@Override
//					public void onCheckedChanged(RadioGroup group, int checkedId) {
//						System.out.println("AAAA:" + checkedId);
//
//					}
//				});
	}

	public void focusView(int index) {
		int length = menuGroupInfo.size();
		if (index >= 0 && index < length) {
			resetGroup();
			View partView = this.radioGroup.findViewById(index);
			TextView tv = (TextView) partView.findViewById(R.id.groupName);
			tv.setTextAppearance(activity, R.style.menuGroupNameFocus);
			ImageView line = (ImageView) partView.findViewById(R.id.line);
			line.setVisibility(View.VISIBLE);

			DisplayMetrics outMetrics = new DisplayMetrics();
			this.activity.getWindowManager().getDefaultDisplay()
					.getMetrics(outMetrics);

			// 计算滚动距离
			int distance = (int) ((index + 1) * partWidth - outMetrics.widthPixels / 2);

			
			if (prePos != index)
				this.mHorizontalScrollView.smoothScrollTo(distance, 0);
		} else {

		}
	}

	int prePos = 0;

	private void resetGroup() {
		for (int i = 0; i < menuGroupInfo.size(); i++) {
			View partView = this.radioGroup.findViewById(i);
			TextView tv = (TextView) partView.findViewById(R.id.groupName);
			tv.setTextAppearance(activity, R.style.menuGroupNameUnfocus);
			ImageView line = (ImageView) partView.findViewById(R.id.line);
			if (line.getVisibility() == View.VISIBLE) {

				prePos = i;
				line.setVisibility(View.INVISIBLE);
				return;
			}
		}
	}

	class ViewWapper {
		private View mTarget;

		public ViewWapper(View target) {
			mTarget = target;
		}

		public int getWidth() {
			return mTarget.getLayoutParams().width;
		}

		public void setWidth(int width) {
			mTarget.getLayoutParams().width = width;
			mTarget.requestLayout();
		}
	}
}
