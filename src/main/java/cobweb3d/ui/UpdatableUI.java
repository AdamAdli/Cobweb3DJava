package cobweb3d.ui;


/**
 * UI component that can be updated
 */
public interface UpdatableUI {

	/**
	 * Updates UI.
	 * @param synchronous whether to wait for component to complete update before returning
	 */
	void update(boolean synchronous);

	/**
	 * Checks if the UI is free to update right now.
	 * @return true when ready
	 */
	boolean isReadyToUpdate();

	void onStopped();

	void onStarted();
}
