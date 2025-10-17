package spare.peetseater.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import spare.peetseater.games.GameRunner;
import spare.peetseater.games.GameAssets;
import spare.peetseater.games.inputs.LaunchBallInputHandler;
import spare.peetseater.games.inputs.PaddleInputHandler;
import spare.peetseater.games.objects.Level;
import spare.peetseater.games.objects.LevelEventConsumer;
import spare.peetseater.games.objects.Obstacle;
import spare.peetseater.games.screens.transitions.TimedAction;
import spare.peetseater.games.utilities.SceneAssetBundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static spare.peetseater.games.GameRunner.GAME_HEIGHT;
import static spare.peetseater.games.GameRunner.GAME_WIDTH;

public class LevelScreen implements Scene {
    private final GameRunner game;
    private final Level level;
    private final LaunchBallInputHandler launchBallInputHandler;
    private final LinkedList<Obstacle> obstaclesToDisappear;
    private final LinkedList<TimedAction> removeActions;
    private int lives;

    public LevelScreen(GameRunner game) {
        this.game = game;
        List<Obstacle> obstacles = new LinkedList<>();
        // 700 to 1200, nee 8 rows. 500/8 = 62.5
        Vector2 brickSize = new Vector2(150, 30);
        float padding = 15 + brickSize.y ;
        float offset = 50;
        for (float x = 40; x < GAME_WIDTH - 50; x += brickSize.x + 25) {
            obstacles.add(Obstacle.RedBrick(new Vector2(x, 800 - padding - offset), brickSize));
            obstacles.add(Obstacle.RedBrick(new Vector2(x, 760 - padding - offset), brickSize));
            obstacles.add(Obstacle.OrangeBrick(new Vector2(x, 720 - padding - offset), brickSize));
            obstacles.add(Obstacle.OrangeBrick(new Vector2(x, 680 - padding - offset), brickSize));
            obstacles.add(Obstacle.GreenBrick(new Vector2(x, 640 - padding - offset), brickSize));
            obstacles.add(Obstacle.GreenBrick(new Vector2(x, 600 - padding - offset), brickSize));
            obstacles.add(Obstacle.YellowBrick(new Vector2(x, 560 - padding - offset), brickSize));
            obstacles.add(Obstacle.YellowBrick(new Vector2(x, 520 - padding - offset), brickSize));
        }
        Vector2 playerPosition = new Vector2(GAME_WIDTH / 2f - 80, 25);
        Vector2 playerSize = new Vector2(80, 20);
        Obstacle player = new Obstacle(playerPosition, playerSize);
        Vector2 levelSize = new Vector2(GAME_WIDTH, GAME_HEIGHT);
        lives = 3;
        obstaclesToDisappear = new LinkedList<>();
        removeActions = new LinkedList<>();
        this.level = new Level(player, obstacles, 50, levelSize);
        this.level.addConsumer(new LevelEventConsumer() {
            @Override
            public void onBallOutOfBounds(Level level) {
                lives--;
                launchBallInputHandler.resetLaunched();
                level.resetBall();
            }

            @Override
            public void onObstacleRemove(Level level, Obstacle obstacle) {
                // We could play a sound here or similar,
                // but for now we'll just add them to a list we'll fade out or similar.
                obstaclesToDisappear.add(obstacle);
                removeActions.add(new TimedAction(0.100f, () -> {
                        float y = obstacle.getDimensions().y - 1;
                        obstacle.setDimensions(obstacle.getDimensions().x, y);
                        if (y <= 0) {
                            obstaclesToDisappear.remove(obstacle);
                            return true;
                        }
                        return false;
                }));
            }

            @Override
            public void onAllObstaclesRemoved(Level level) {
                game.swapSceneTo(new WinScreen(game));
            }
        });

        Gdx.app.log(getClass().getSimpleName(), "LOAD: " + getBundleName());
        game.assetManager.load(GameAssets.LEVEL_SCREEN_BUNDLE);
        PaddleInputHandler paddleInputHandler = new PaddleInputHandler(player);
        launchBallInputHandler = new LaunchBallInputHandler(this.level);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(launchBallInputHandler);
        inputMultiplexer.addProcessor(paddleInputHandler);
        Gdx.input.setInputProcessor(inputMultiplexer);
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

        if (!launchBallInputHandler.isLaunched()) {
            Vector2 launchVector = launchBallInputHandler.getLaunchVector();
            float lvx = MathUtils.lerp(px, px + pw, launchVector.x + 0.5f);
            game.batch.draw(
                playerTexture,
                lvx,
                py + ph + 15,
                10, 10
            );
        }

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

        for (Obstacle obstacle : obstaclesToDisappear) {
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

        game.font.draw(
            game.batch,
            "Score: " + level.getScore(),
            0, 780,
            GAME_WIDTH, Align.center, false
        );

        game.font.draw(
            game.batch,
            "Balls: " + lives,
            0, 780,
            GAME_WIDTH / 3f, Align.center, false
        );

    }

    @Override
    public String getBundleName() {
        return GameAssets.LEVELSCREEN_BUNDLE_KEY;
    }

    @Override
    public void update(float seconds) {
        this.level.update(seconds);
        List<TimedAction> toRemove = new ArrayList<>(2);
        for (TimedAction timedAction : removeActions) {
            timedAction.update(seconds);
            if (timedAction.isDone()) {
                toRemove.add(timedAction);
            }
        }
        removeActions.removeAll(toRemove);
    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getSimpleName(), "UNLOAD: " + getBundleName());
        game.assetManager.unload(getBundleName());
    }
}
