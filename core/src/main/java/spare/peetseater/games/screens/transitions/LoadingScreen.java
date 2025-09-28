package spare.peetseater.games.screens.transitions;

import spare.peetseater.games.GameAssets;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.screens.Scene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.utilities.SceneAssetBundle;

public class LoadingScreen implements Scene {

    private final GameRunner game;
    private float elapsedSeconds;
    private float minLength;
    private boolean shownOnce = false;

    public LoadingScreen(GameRunner game) {
        this.game = game;
        Gdx.app.log(getClass().getSimpleName(), "LOAD: " + getBundleName());
        this.game.assetManager.load(GameAssets.LOADING_SCREEN_BUNDLE);
        this.elapsedSeconds = 0;
        this.minLength = 1;
    }

    @Override
    public void update(float seconds) {
        if (this.game.assetManager.update(17) && shownOnce) {
        } else {
        }
    }

    @Override
    public void render(float delta) {
        SceneAssetBundle bundle = game.assetManager.get(GameAssets.LOADING_SCREEN_BUNDLE);
        elapsedSeconds += delta;
        ScreenUtils.clear(Color.BLACK);

        game.batch.draw(
            bundle.get(GameAssets.RED_SQUARE),
            1280f / 2 - 60, 400,
            30, 30
        );

        if (elapsedSeconds > 0.4 || elapsedSeconds == 0) {
            game.batch.draw(
                bundle.get(GameAssets.YELLOW_SQUARE),
                1280f / 2f - 15, 400,
                30, 30
            );
        }
        if (elapsedSeconds > 0.8 || elapsedSeconds == 0) {
            game.batch.draw(
                bundle.get(GameAssets.GREEN_SQUARE),
                1280f / 2f + 30, 400,
                30, 30
            );
        }
        if (elapsedSeconds > minLength) {
            shownOnce = true;
            elapsedSeconds = 0;
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getSimpleName(), "UNLOAD: " + getBundleName());
        this.game.assetManager.unload(getBundleName());
    }

    @Override
    public String getBundleName() {
        return GameAssets.LOADING_SCREEN_BUNDLE_KEY;
    }

}

