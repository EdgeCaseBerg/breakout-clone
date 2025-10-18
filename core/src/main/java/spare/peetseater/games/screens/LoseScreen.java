package spare.peetseater.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.GameAssets;

import static spare.peetseater.games.GameRunner.GAME_HEIGHT;
import static spare.peetseater.games.GameRunner.GAME_WIDTH;

public class LoseScreen implements Scene {

    private final GameRunner game;

    public LoseScreen(GameRunner game) {
        this.game = game;
        game.assetManager.load(GameAssets.LEVEL_SCREEN_BUNDLE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.font.draw(
            game.batch,
            "You lose!",
            0, GAME_HEIGHT / 2f,
            GAME_WIDTH, Align.center, false
        );
    }

    @Override
    public String getBundleName() {
        return GameAssets.LEVELSCREEN_BUNDLE_KEY;
    }

    @Override
    public void update(float seconds) {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getSimpleName(), "UNLOAD: " + getBundleName());
        game.assetManager.unload(getBundleName());
    }
}

