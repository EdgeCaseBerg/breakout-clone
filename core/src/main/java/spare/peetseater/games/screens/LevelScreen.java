package spare.peetseater.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.objects.Level;
import spare.peetseater.games.objects.Obstacle;
import spare.peetseater.games.utilities.SceneAssetBundle;

import java.util.LinkedList;
import java.util.List;

import static spare.peetseater.games.GameRunner.GAME_HEIGHT;
import static spare.peetseater.games.GameRunner.GAME_WIDTH;

public class LevelScreen implements Scene {
    private final GameRunner game;
    private final Level level;

    public LevelScreen(GameRunner game) {
        this.game = game;
        List<Obstacle> obstacles = new LinkedList<>();
        Vector2 brickSize = new Vector2(150, 50);
        for (float x = 50; x < GAME_WIDTH - 50; x += brickSize.x + 25) {
            for (float y = GAME_HEIGHT - 400; y < GAME_HEIGHT - brickSize.y + 10; y += brickSize.y + 10) {
                obstacles.add(new Obstacle(new Vector2(x, y), brickSize));
            }
        }
        Vector2 playerPosition = new Vector2(GAME_WIDTH / 2f - 80, 25);
        Vector2 playerSize = new Vector2(160, 45);
        Obstacle player = new Obstacle(playerPosition, playerSize);
        this.level = new Level(player, obstacles, 50);
        Gdx.app.log(getClass().getSimpleName(), "LOAD: " + getBundleName());
        game.assetManager.load(GameAssets.LEVEL_SCREEN_BUNDLE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        SceneAssetBundle bundle = game.assetManager.get(GameAssets.LEVEL_SCREEN_BUNDLE);
        Texture playerTexture = bundle.get(GameAssets.PLAYER_PADDLE);
        Texture ballTexture = bundle.get(GameAssets.YELLOW_SQUARE);
        Texture obstacleTexture = bundle.get(GameAssets.GREEN_SQUARE);

        float px = level.getPlayer().getPosition().x;
        float py = level.getPlayer().getPosition().y;
        float pw = level.getPlayer().getDimensions().x;
        float ph = level.getPlayer().getDimensions().y;
        game.batch.draw(
            playerTexture,
            px, py, pw, ph
        );

        float bx = level.getBall().getPosition().x;
        float by = level.getBall().getPosition().y;
        float bw = level.getBall().getDimensions().x;
        float bh = level.getBall().getDimensions().y;
        game.batch.draw(
            ballTexture,
            bx, by, bw, bh
        );

        for (Obstacle obstacle : level.getObstacles()) {
            float ox = obstacle.getPosition().x;
            float oy = obstacle.getPosition().y;
            float ow = obstacle.getDimensions().x;
            float oh = obstacle.getDimensions().y;
            game.batch.draw(
                obstacleTexture,
                ox, oy, ow, oh
            );
        }
    }

    @Override
    public String getBundleName() {
        return GameAssets.LEVELSCREEN_BUNDLE_KEY;
    }

    @Override
    public void update(float seconds) {

    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getSimpleName(), "UNLOAD: " + getBundleName());
        game.assetManager.unload(getBundleName());
    }
}
