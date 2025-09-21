package spare.peetseater.games.screens;

import spare.peetseater.games.GameRunner;

public enum ScreenSignal {
    /** Signal to the game runner that a queued scene should be loaded
     * The current screen will _not_ be disposed of and will remain in
     * memory with the expectation that it will be used again soon.
     * Such as a pause menu or similar.
     **/
    OVERLAY_SCENE,
    /** Signal to the game runner to unload the current scene and display the next in queue
     * It is the responsibility of the current scene to ensure that there is a queued scene
     * to be transitioned to. Use {@link GameRunner}'s requestSceneChangeTo method to do so.
     * */
    UNLOAD,
    /** Continue rendering the current scene */
    CONTINUE,
}
