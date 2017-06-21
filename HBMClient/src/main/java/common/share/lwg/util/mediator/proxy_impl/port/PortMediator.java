/**
 * PortMediator
 */
package common.share.lwg.util.mediator.proxy_impl.port;

import java.util.List;
import java.util.Map;

import android.app.Activity;

import common.share.lwg.util.life.ILife;
import common.share.lwg.util.mediator.IMediator;
import common.share.lwg.util.mediator.proxy_impl.IColleague;

/**
 * @author liweigao
 * 
 */
public class PortMediator implements IMediator, ILife {
	private GroupColleague groupColleague;
	private PagerColleague pagerColleague;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * common.share.lwg.util.mediator.IMediator#createColleagues(java.lang.Object
	 * [])
	 */
	@Override
	public void createColleagues(Object... args) {
		Activity a = (Activity) args[0];
		this.groupColleague = new GroupColleague(a);
		this.pagerColleague = new PagerColleague(a);
		this.groupColleague.setMediator(this);
		this.pagerColleague.setMediator(this);
	}

	public void focusView(IColleague colleague, int index) {
		if (colleague instanceof GroupColleague) {
			this.pagerColleague.focusView(index);
		} else if (colleague instanceof PagerColleague) {
			this.groupColleague.focusView(index);
		}

//		// if (colleague instanceof GroupColleague) {
//		this.pagerColleague.focusView(index);
//		// } else if (colleague instanceof PagerColleague) {
//		this.groupColleague.focusView(index);
////		if (this.groupColleague.radioGroup != null)
////			this.groupColleague.radioGroup.findViewById(index).performClick();
//		// }
	}

	public void showBoard(boolean flag) {
		this.pagerColleague.showBoard(flag);
	}

	/**
	 * 
	 * @see common.share.lwg.util.mediator.proxy_impl.port.PagerColleague#actionPassword()
	 */
	public void actionPassword() {
		pagerColleague.actionPassword();
	}

	/**
	 * 
	 * @see common.share.lwg.util.mediator.proxy_impl.port.PagerColleague#actionShowBoard()
	 */
	public void actionShowBoard() {
		pagerColleague.actionShowBoard();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.life.ILife#create()
	 */
	@Override
	public void create() {
		this.groupColleague.create();
		this.pagerColleague.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.life.ILife#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.life.ILife#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.life.ILife#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.life.ILife#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.share.lwg.util.life.ILife#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param menuGroupInfo
	 * @see common.share.lwg.util.mediator.proxy_impl.port.PagerColleague#setMenuGroupInfo(java.util.List)
	 */
	public void setMenuGroupInfo(List<Map<String, String>> menuGroupInfo) {
		pagerColleague.setMenuGroupInfo(menuGroupInfo);
	}

}
