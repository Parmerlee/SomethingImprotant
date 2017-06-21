/**
 * APortColleague
 */
package common.share.lwg.util.mediator.proxy_impl.port;

import android.app.Activity;

import common.share.lwg.util.life.ILife;
import common.share.lwg.util.mediator.IMediator;
import common.share.lwg.util.mediator.proxy_impl.IColleague;

/**
 * @author liweigao
 *
 */
public abstract class APortColleague implements IColleague,ILife {
	protected PortMediator portMediator;
	protected Activity activity;
	
	public APortColleague(Activity activity) {
		this.activity = activity;
	}
	

	/* (non-Javadoc)
	 * @see common.share.lwg.util.mediator.proxy_impl.IColleague#setMediator(common.share.lwg.util.mediator.IMediator)
	 */
	@Override
	public void setMediator(IMediator mediator) {
		this.portMediator = (PortMediator) mediator;
	}
	

	/* (non-Javadoc)
	 * @see common.share.lwg.util.mediator.proxy_impl.IColleague#getMediator()
	 */
	@Override
	public IMediator getMediator() {
		return this.portMediator;
	}


	/* (non-Javadoc)
	 * @see common.share.lwg.util.life.ILife#create()
	 */
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.life.ILife#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.life.ILife#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.life.ILife#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.life.ILife#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.life.ILife#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
