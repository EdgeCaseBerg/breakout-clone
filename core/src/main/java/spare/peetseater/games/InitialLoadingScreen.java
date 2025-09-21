package spare.peetseater.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.screens.Scene;
import spare.peetseater.games.screens.ScreenSignal;

public class InitialLoadingScreen extends ScreenAdapter implements Scene {
    private final AssetManager assetManager;
    private final SpriteBatch batch;
    private final BitmapFont bitmapFont;
    private float elapsedSeconds;
    private int pulse;

    public InitialLoadingScreen(BustOutRun game) {
        this.bitmapFont = new BitmapFont(false);
        this.assetManager = game.assetManager;
        this.assetManager.load(GameAssets.INITIAL_LOADING_SCREEN_BUNDLE);
        this.batch = game.batch;
        this.elapsedSeconds = 0;
        this.pulse = 1;
    }

    @Override
    public ScreenSignal update(float seconds) {
        // unless we want to display the loading screen for a while, always return load next
        // to find out if we can shift an item off the queue
        if (assetManager.update(17)) {
            return ScreenSignal.OVERLAY_SCENE;
        } else {
            return ScreenSignal.CONTINUE;
        }
    }

    @Override
    public void render(float delta) {
        float alpha = MathUtils.lerp(0, 100, elapsedSeconds);
        int loaded = (int)(assetManager.getProgress() * 100);
        ScreenUtils.clear(Color.GRAY);
        Color fontcolor = new Color(0,1,0,alpha/100f);
        bitmapFont.setColor(fontcolor);
        bitmapFont.draw(batch, String.format("Loading %d%%", loaded), 0, Gdx.graphics.getHeight()/2f, Gdx.graphics.getWidth(), Align.center, false);
        elapsedSeconds += delta * pulse;
        if (elapsedSeconds > 1) {
            pulse = -1;
        } else if (elapsedSeconds < 0) {
            pulse = 1;
        }
    }

    @Override
    public void dispose() {
        this.bitmapFont.dispose();
    }

    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        // we dont need to resize because the loading screen isn't using a camera.
    }

    @Override
    public void pause() {
        elapsedSeconds = 0;
    }

    @Override
    public void resume() {
        elapsedSeconds = 0;
    }

    @Override
    public void hide() {
        elapsedSeconds = 0;
    }

    @Override
    public String getBundleName() {
        return GameAssets.INITIAL_LOADING_SCREEN_BUNDLE_KEY;
    }

    @Override
    public Screen getScreen() {
        return this;
    }
}
