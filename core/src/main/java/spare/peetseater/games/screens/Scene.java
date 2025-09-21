package spare.peetseater.games.screens;

import com.badlogic.gdx.Screen;

public interface Scene {
    public String getBundleName();
    public Screen getScreen();
    public ScreenSignal update(float seconds);
}
