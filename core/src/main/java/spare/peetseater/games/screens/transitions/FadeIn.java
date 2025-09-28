package spare.peetseater.games.screens.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.screens.Scene;
import spare.peetseater.games.utilities.SceneAssetBundle;


public class FadeIn implements Scene {
    private final GameRunner game;
    private final float forSeconds;
    private float accum = 0f;
    private final Scene to;

    public FadeIn(GameRunner game, float seconds, Scene to) {
        this.game = game;
        this.forSeconds = seconds;
        this.to = to;
        Gdx.app.log(getClass().getSimpleName(), "LOAD: " + getBundleName());
        this.game.assetManager.load(GameAssets.FADE_OUT_BUNDLE);
        this.game.assetManager.load(to.getBundleName(), SceneAssetBundle.class);
    }

    @Override
    public String getBundleName() {
        return GameAssets.FADE_OUT_BUNDLE.fileName;
    }

    @Override
    public void update(float seconds) {
        // As we fade out, ensure that the asset manager is loading anything
        // for what we intend to load to in order to minimize wait time.
        game.assetManager.update(17);
        this.accum = Math.min(this.accum + seconds, forSeconds);
    }

    @Override
    public void render(float delta) {
        float alpha = Math.min(accum / forSeconds, forSeconds);
        SceneAssetBundle bundle = game.assetManager.get(getBundleName());
        ScreenUtils.clear(Color.BLACK);
        to.render(0);
        Color batchColor = game.batch.getColor().cpy();
        Color alphaColor = batchColor.cpy();
        alphaColor.a = MathUtils.lerp(0, 1, 1 - alpha);
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
        Gdx.app.log(getClass().getSimpleName(), "UNLOAD: " + getBundleName());
        this.game.assetManager.unload(getBundleName());
        // This unload _shouldn't_ be a problem because the reference counting
        // by the asset manager should be at +2 since new TO() will have requested
        // load as well.
        this.game.assetManager.unload(to.getBundleName());
    }
}

