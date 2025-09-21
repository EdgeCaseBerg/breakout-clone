package spare.peetseater.games.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.BustOutRun;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.screens.Scene;
import spare.peetseater.games.screens.ScreenSignal;
import spare.peetseater.games.utilities.SceneAssetBundle;

public class FadeOut extends ScreenAdapter implements Scene {
    private final BustOutRun game;
    private final float forSeconds;
    private float accum = 0f;
    private final Texture from;

    public FadeOut(BustOutRun game, float seconds, Texture priorScreenSnapshot) {
        this.game = game;
        this.forSeconds = seconds;
        this.from = priorScreenSnapshot;
        this.game.assetManager.load(GameAssets.FADE_OUT_BUNDLE);
    }

    @Override
    public String getBundleName() {
        return GameAssets.FADE_OUT_BUNDLE.fileName;
    }

    @Override
    public Screen getScreen() {
        return this;
    }

    @Override
    public ScreenSignal update(float seconds) {
        // As we fade out, ensure that the asset manager is loading anything
        // for what we intend to load to in order to minimize wait time.
        game.assetManager.update(17);
        this.accum += seconds;
        if (accum > forSeconds) {
            return ScreenSignal.UNLOAD;
        }
        return ScreenSignal.CONTINUE;
    }

    @Override
    public void render(float delta) {
        float alpha = Math.min(accum / forSeconds, forSeconds);
        SceneAssetBundle bundle = game.assetManager.get(getBundleName());
        ScreenUtils.clear(Color.BLACK);
        game.batch.draw(
            from,
            0f, 0f,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            0, 0,
            from.getWidth(), from.getHeight(),
            false,
            true // frame buffer screenshots are upside down so flipY=true
        );
        Color batchColor = game.batch.getColor().cpy();
        Color alphaColor = batchColor.cpy();
        alphaColor.a = MathUtils.lerp(0, 1, alpha);
        game.batch.setColor(alphaColor);
        game.batch.draw(
            bundle.get(GameAssets.BLACK_SQUARE),
            0,0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight()
        );
        game.batch.setColor(batchColor);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.from.dispose();
    }
}
