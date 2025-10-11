package spare.peetseater.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.inputs.PaddleInputHandler;
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
        // 700 to 1200, nee 8 rows. 500/8 = 62.5
        Vector2 brickSize = new Vector2(150, 30);
        for (float x = 50; x < GAME_WIDTH - 50; x += brickSize.x + 25) {
            float padding = 15 + brickSize.y ;
            obstacles.add(Obstacle.RedBrick(new Vector2(x, 800 - padding), brickSize));
            obstacles.add(Obstacle.RedBrick(new Vector2(x, 750 - padding), brickSize));
            obstacles.add(Obstacle.OrangeBrick(new Vector2(x, 700 - padding), brickSize));
            obstacles.add(Obstacle.OrangeBrick(new Vector2(x, 650 - padding), brickSize));
            obstacles.add(Obstacle.GreenBrick(new Vector2(x, 600 - padding), brickSize));
            obstacles.add(Obstacle.GreenBrick(new Vector2(x, 550 - padding), brickSize));
            obstacles.add(Obstacle.YellowBrick(new Vector2(x, 500 - padding), brickSize));
            obstacles.add(Obstacle.YellowBrick(new Vector2(x, 450 - padding), brickSize));
        }
        Vector2 playerPosition = new Vector2(GAME_WIDTH / 2f - 80, 25);
        Vector2 playerSize = new Vector2(80, 20);
        Obstacle player = new Obstacle(playerPosition, playerSize);
        Vector2 levelSize = new Vector2(GAME_WIDTH, GAME_HEIGHT);
        this.level = new Level(player, obstacles, 50, levelSize);
        Gdx.app.log(getClass().getSimpleName(), "LOAD: " + getBundleName());
        game.assetManager.load(GameAssets.LEVEL_SCREEN_BUNDLE);
        PaddleInputHandler paddleInputHandler = new PaddleInputHandler(player);
        Gdx.input.setInputProcessor(paddleInputHandler);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        SceneAssetBundle bundle = game.assetManager.get(GameAssets.LEVEL_SCREEN_BUNDLE);
        Texture playerTexture = bundle.get(GameAssets.PLAYER_PADDLE);
        Texture ballTexture = bundle.get(GameAssets.YELLOW_SQUARE);
        Texture greenBrickTexture = bundle.get(GameAssets.GREEN_SQUARE);
        Texture redBrickTexture   = bundle.get(GameAssets.RED_SQUARE);
        Texture orangeBrickTexture = bundle.get(GameAssets.ORANGE_SQUARE);
        Texture yellowBrickTexture = bundle.get(GameAssets.YELLOW_SQUARE);

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
            Texture obstacleTexture;
            switch (obstacle.getWorth()) {
                case 7:  obstacleTexture = redBrickTexture; break;
                case 5:  obstacleTexture = orangeBrickTexture; break;
                case 3:  obstacleTexture = greenBrickTexture; break;
                default: obstacleTexture = yellowBrickTexture; break;
            }
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
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            this.level.getBall().tmpReset().setVelocity(new Vector2(MathUtils.random(), MathUtils.random()));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.level.launchBall(new Vector2(50, 50));
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.level.getBall().tmpReset(Gdx.input.getX(), Gdx.input.getY()).setVelocity(new Vector2(MathUtils.random(), MathUtils.random()));
        }
        this.level.update(seconds);
    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getSimpleName(), "UNLOAD: " + getBundleName());
        game.assetManager.unload(getBundleName());
    }
}
