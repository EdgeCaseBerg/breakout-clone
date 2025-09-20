package spare.peetseater.games.screens;

import com.badlogic.gdx.Screen;
import spare.peetseater.games.utilities.SceneAssetBundle;
import spare.peetseater.games.utilities.SceneAssetBundleLoader;

public interface Scene {
    public String getBundleName();
    public Screen getScreen();
}
