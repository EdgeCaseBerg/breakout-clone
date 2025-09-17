package spare.peetseater.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BustOutRun extends Game {

    public SpriteBatch batch;
    AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        setScreen(new InitialLoadingScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    @Override
    public void render() {
        super.render();
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            getScreen().dispose();
            setScreen(new InitialLoadingScreen(this));
        }
    }

}
