package spare.peetseater.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import spare.peetseater.games.screens.LevelScreen;
import spare.peetseater.games.screens.Scene;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BustOutRun extends Game {

    public SpriteBatch batch;
    public AssetManager assetManager;
    public Scene loadingScreen;
    public Scene desiredScreen;

    @Override
    public void create() {
        assetManager = new AssetManager();
        GameAssets.configure(assetManager);
        batch = new SpriteBatch();
        loadingScreen = new InitialLoadingScreen(this);
        desiredScreen = new LevelScreen(this);
        setScreen(loadingScreen.getScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        loadingScreen.getScreen().dispose();
        desiredScreen.getScreen().dispose();
        assetManager.dispose();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        if (!assetManager.isFinished()) {
            if (!getScreen().equals(loadingScreen.getScreen())) {
                setScreen(loadingScreen.getScreen());
            }
        }

        if (assetManager.isLoaded(desiredScreen.getBundleName())) {
            if (getScreen().equals(loadingScreen.getScreen())) {
                setScreen(desiredScreen.getScreen());
            }
        }
        super.render();
    }

}
