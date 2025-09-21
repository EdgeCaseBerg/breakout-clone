package spare.peetseater.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.utilities.SceneAssetBundle;

public class LevelScreen implements Scene {
    private final GameRunner game;

    public LevelScreen(GameRunner game) {
        this.game = game;
        game.assetManager.load(GameAssets.LEVEL_SCREEN_BUNDLE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.YELLOW);
        SceneAssetBundle bundle = game.assetManager.get(GameAssets.LEVEL_SCREEN_BUNDLE);
        Texture texture = bundle.get(GameAssets.PLAYER_PADDLE);
        game.batch.draw(
            texture,
            300, 300, 300, 300
        );
    }

    @Override
    public String getBundleName() {
        return GameAssets.LEVELSCREEN_BUNDLE_KEY;
    }

    @Override
    public ScreenSignal update(float seconds) {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.requestSceneChangeTo(new LevelScreen(game));
            return ScreenSignal.UNLOAD;
        }
        return ScreenSignal.CONTINUE;
    }

    @Override
    public void dispose() {
        game.assetManager.unload(getBundleName());
    }
}
