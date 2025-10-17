package spare.peetseater.games;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import spare.peetseater.games.screens.LevelScreen;
import spare.peetseater.games.screens.Scene;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameRunner implements ApplicationListener {

    public static final float GAME_WIDTH = 1280f;
    public static final float GAME_HEIGHT = 800f;
    public SpriteBatch batch;
    public AssetManager assetManager;
    public BitmapFont font;
    private Scene loadingScene;
    private FitViewport viewport;
    private OrthographicCamera camera;
    public Scene currentScene;

    @Override
    public void create() {
        assetManager = new AssetManager();
        GameAssets.configure(assetManager);
        assetManager.load(GameAssets.scoreFont);
        assetManager.finishLoading();
        font = GameAssets.getScaledFont(GameAssets.scoreFont, assetManager);

        batch = new SpriteBatch();
        camera = new OrthographicCamera(GAME_WIDTH, GAME_HEIGHT);
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
        viewport.setCamera(camera);

        loadingScene = new InitialLoadingScreen(this);
        currentScene = new LevelScreen(this);;
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
    }

    @Override
    public void dispose() {
        if (loadingScene != currentScene) loadingScene.dispose();
        currentScene.dispose();
        assetManager.dispose();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();
        Scene scene;
        if (assetManager.isFinished()) {
            scene = currentScene;
        } else {
            scene = loadingScene;
        }

        scene.update(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        scene.render(delta);
        batch.end();
    }

    public void swapSceneTo(Scene scene) {
        this.currentScene = scene;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        this.assetManager.finishLoading();
    }
}
