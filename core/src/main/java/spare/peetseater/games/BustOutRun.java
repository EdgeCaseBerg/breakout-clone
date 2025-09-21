package spare.peetseater.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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
    public Scene loadingScreen;
    public Scene currentScreen;
    private Queue<Scene> toLoad;

    @Override
    public void create() {
        toLoad = new ConcurrentLinkedQueue<>();
        assetManager = new AssetManager();
        GameAssets.configure(assetManager);
        batch = new SpriteBatch();
        loadingScreen = new InitialLoadingScreen(this);
        currentScreen = loadingScreen;
        setScreen(loadingScreen.getScreen());
        requestSceneChangeTo(new LevelScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        loadingScreen.getScreen().dispose();
        currentScreen.getScreen().dispose();
        assetManager.dispose();
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        if (!assetManager.isFinished()) {
            if (!getScreen().equals(loadingScreen.getScreen())) {
                setScreen(loadingScreen.getScreen());
            }
        }

        float delta = Gdx.graphics.getDeltaTime();
        ScreenSignal signal = currentScreen.update(delta);
        switch (signal) {
            case OVERLAY_SCENE:
                loadSceneOnSignal();
                break;
            case UNLOAD:
                if (toLoad.isEmpty()) {
                    Gdx.app.log("SCENES", "asked to unload current scene but there is no scene to return to");
                    throw new RuntimeException("...");
                }
                Scene nextScene = toLoad.peek();
                if (assetManager.isLoaded(nextScene.getBundleName())) {
                    if (loadingScreen != currentScreen) {
                        assetManager.unload(currentScreen.getBundleName());
                        currentScreen.getScreen().dispose();
                    }
                    currentScreen = toLoad.poll();
                    setScreen(currentScreen.getScreen());
                } else {
                    currentScreen = loadingScreen;
                    if (!getScreen().equals(loadingScreen.getScreen())) {
                        setScreen(loadingScreen.getScreen());
                    }
                }

                break;
            case CONTINUE:
            default:
                if (screen != null) screen.render(Gdx.graphics.getDeltaTime());
        }

    }

    private void loadSceneOnSignal() {
        if (toLoad.isEmpty()) {
            Gdx.app.log("SCENES", "asked to load next scene but there is none queued");
            throw new RuntimeException("...");
        }

        Scene nextScene = toLoad.peek();
        if (assetManager.isLoaded(nextScene.getBundleName())) {
            currentScreen = toLoad.poll();
            setScreen(currentScreen.getScreen());
        } else {
            currentScreen = loadingScreen;
            if (!getScreen().equals(loadingScreen.getScreen())) {
                setScreen(loadingScreen.getScreen());
            }
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
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            false
        );
        batch.flush();;
        frameBuffer.begin();
        currentScreen.getScreen().render(0);
        frameBuffer.end();
        Texture screenshot = frameBuffer.getColorBufferTexture();
        return screenshot;
    }
}
