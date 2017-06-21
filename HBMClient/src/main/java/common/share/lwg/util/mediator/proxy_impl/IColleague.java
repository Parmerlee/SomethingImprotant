/**
 * IColleague
 */
package common.share.lwg.util.mediator.proxy_impl;

import common.share.lwg.util.mediator.IMediator;

/**
 * @author liweigao
 *
 */
public interface IColleague {
	void setMediator(IMediator mediator);
	IMediator getMediator();
}
