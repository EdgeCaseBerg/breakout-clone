package spare.peetseater.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.screens.Scene;
import spare.peetseater.games.screens.ScreenSignal;

public class InitialLoadingScreen implements Scene {
    private final BitmapFont bitmapFont;
    private final GameRunner game;
    private float elapsedSeconds;
    private int pulse;

    public InitialLoadingScreen(GameRunner game) {
        this.bitmapFont = new BitmapFont(false);
        this.game = game;
        this.game.assetManager.load(GameAssets.INITIAL_LOADING_SCREEN_BUNDLE);
        this.elapsedSeconds = 0;
        this.pulse = 1;
    }

    @Override
    public ScreenSignal update(float seconds) {
        // unless we want to display the loading screen for a while, always return load next
        // to find out if we can shift an item off the queue
        if (this.game.assetManager.update(17)) {
            return ScreenSignal.UNLOAD;
        } else {
            return ScreenSignal.CONTINUE;
        }
    }

    @Override
    public void render(float delta) {
        float alpha = MathUtils.lerp(0, 100, elapsedSeconds);
        int loaded = (int)(this.game.assetManager.getProgress() * 100);
        ScreenUtils.clear(Color.BLACK);
        Color fontcolor = new Color(0,1,0,alpha/100f);
        bitmapFont.setColor(fontcolor);
        bitmapFont.draw(this.game.batch, String.format("Loading %d%%", loaded), 0, Gdx.graphics.getHeight()/2f, Gdx.graphics.getWidth(), Align.center, false);
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
        this.game.assetManager.unload(getBundleName());
    }

    @Override
    public String getBundleName() {
        return GameAssets.INITIAL_LOADING_SCREEN_BUNDLE_KEY;
    }

}
