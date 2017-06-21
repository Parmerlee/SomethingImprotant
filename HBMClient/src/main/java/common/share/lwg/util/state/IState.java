/**
 * IState
 */
package common.share.lwg.util.state;

/**
 * @author liweigao
 *
 */
public interface IState<T> {
	void enter();
	void changeState();
	T getStateFlag();
}
