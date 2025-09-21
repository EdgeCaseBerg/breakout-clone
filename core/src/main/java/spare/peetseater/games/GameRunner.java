package spare.peetseater.games;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
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
import spare.peetseater.games.utilities.DelayedScreenshot;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameRunner implements ApplicationListener {

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
        currentScreen = new InitialLoadingScreen(this);;
        requestSceneChangeTo(new LevelScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
    }

    @Override
    public void dispose() {
        while(!toLoad.isEmpty()) {
            toLoad.poll().dispose();
        }
        currentScreen.dispose();
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
            currentScreen.dispose();
            currentScreen = toLoad.poll();
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
            toLoad.add(currentScreen);     // add to tail
            currentScreen = toLoad.poll(); // pull from head
        } else {
            assetManager.update(17);
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    public void requestSceneChangeTo(Scene scene) {
        // When we do screen transitions, this is where we can
        // queue up a FadeOut, Load, FadeIn, Desired Scene, list
        // of values once we make those.
        toLoad.add(new FadeOut(this, 2f, new DelayedScreenshot(batch, currentScreen) ));
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
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
