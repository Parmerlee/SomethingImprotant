/**
 * AMainKpiColleague
 */
package common.share.lwg.util.mediator.proxy_impl.mainkpi;

import common.share.lwg.util.mediator.IMediator;

/**
 * @author liweigao
 *
 */
public abstract class AMainKpiColleague implements IMainKpiColleague {
	protected MainKpiMediator mainKpiMediator;

	/* (non-Javadoc)
	 * @see common.share.lwg.util.mediator.proxy_impl.IColleague#setMediator(common.share.lwg.util.mediator.IMediator)
	 */
	@Override
	public void setMediator(IMediator mediator) {
		this.mainKpiMediator = (MainKpiMediator) mediator;
	}

	/* (non-Javadoc)
	 * @see common.share.lwg.util.mediator.proxy_impl.IColleague#getMediator()
	 */
	@Override
	public IMediator getMediator() {
		return this.mainKpiMediator;
	}

}
