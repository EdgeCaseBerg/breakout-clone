package spare.peetseater.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import spare.peetseater.games.screens.LevelScreen;
import spare.peetseater.games.screens.Scene;
import spare.peetseater.games.screens.ScreenSignal;
import spare.peetseater.games.screens.transitions.FadeOut;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BustOutRun extends Game {

    public SpriteBatch batch;
    public AssetManager assetManager;
    private Queue<Scene> toLoad;
    private FitViewport viewport;
    private OrthographicCamera camera;

    public Scene currentScreen;

    @Override
    public void create() {
        assetManager = new AssetManager();
        GameAssets.configure(assetManager);

        batch = new SpriteBatch();
        camera = new OrthographicCamera(1280f, 800f);
        camera.setToOrtho(false, 1280f, 800f);
        viewport = new FitViewport(1280f, 800f);
        viewport.setCamera(camera);

        toLoad = new ConcurrentLinkedQueue<>();
        Scene loadingScreen = new InitialLoadingScreen(this);
        currentScreen = loadingScreen;
        setScreen(loadingScreen.getScreen());
        requestSceneChangeTo(new LevelScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        while(!toLoad.isEmpty()) {
            toLoad.poll().getScreen().dispose();
        }
        currentScreen.getScreen().dispose();
        assetManager.dispose();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();
        ScreenSignal signal = currentScreen.update(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        switch (signal) {
            case OVERLAY_SCENE:
                Gdx.app.log(getClass().getSimpleName(), "OVERLAY");
                overlayScene();
                break;
            case UNLOAD:
                Gdx.app.log(getClass().getSimpleName(), "UNLOAD");
                unloadScene();
                break;
            case CONTINUE:
            default:
                currentScreen.render(delta);
        }
        batch.end();
    }

    private void unloadScene() {
        if (toLoad.isEmpty()) {
            Gdx.app.log("SCENES", "asked to unload current scene but there is no scene to return to");
            throw new RuntimeException("...");
        }
        Scene nextScene = toLoad.peek();
        if (assetManager.isLoaded(nextScene.getBundleName())) {
            assetManager.unload(currentScreen.getBundleName());
            currentScreen.getScreen().dispose();
            currentScreen = toLoad.poll();
            setScreen(currentScreen.getScreen());
        } else {
            assetManager.update(17);
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    private void overlayScene() {
        if (toLoad.isEmpty()) {
            String message = "asked to load next scene but there is none queued";
            Gdx.app.log(getClass().getSimpleName(), message);
            throw new RuntimeException(message);
        }

        Scene nextScene = toLoad.peek();
        if (assetManager.isLoaded(nextScene.getBundleName())) {
            // Place the scene we're coming from onto the queue so
            // we can return to it when the next screen signals UNLOAD
            toLoad.add(currentScreen);
            currentScreen = toLoad.poll();
            setScreen(currentScreen.getScreen());
        } else {
            assetManager.update(17);
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    public void requestSceneChangeTo(Scene scene) {
        // When we do screen transitions, this is where we can
        // queue up a FadeOut, Load, FadeIn, Desired Scene, list
        // of values once we make those.
        toLoad.add(new FadeOut(this, 2f, screenshot()));
        toLoad.add(scene);
    }

    private Texture screenshot() {
        FrameBuffer frameBuffer = new FrameBuffer(
            Pixmap.Format.RGBA8888,
            1280,
            800,
            false
        );
        batch.flush();
        frameBuffer.begin();
        batch.begin();
        currentScreen.render(0);
        batch.end();
        frameBuffer.end();
        return frameBuffer.getColorBufferTexture();
    }
}
