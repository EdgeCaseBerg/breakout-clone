package spare.peetseater.games.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import spare.peetseater.games.BustOutRun;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.utilities.SceneAssetBundle;

public class LevelScreen extends ScreenAdapter implements Scene {

    private final FitViewport viewport;
    private final Camera camera;
    private final BustOutRun game;

    public LevelScreen(BustOutRun game) {
        this.game = game;
        game.assetManager.load(GameAssets.LEVEL_SCREEN_BUNDLE);
        camera = new OrthographicCamera(1280f, 800f);
        viewport = new FitViewport(1280, 800);
        viewport.setCamera(camera);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        ScreenUtils.clear(Color.BLACK);
        SceneAssetBundle bundle = game.assetManager.get(GameAssets.LEVEL_SCREEN_BUNDLE);
        Texture texture = bundle.get(GameAssets.PLAYER_PADDLE);
        game.batch.draw(
            texture,
            300, 300, 30, 30
        );
        game.batch.end();
    }

    @Override
    public String getBundleName() {
        return GameAssets.LEVELSCREEN_BUNDLE_KEY;
    }

    @Override
    public Screen getScreen() {
        return this;
    }
}
