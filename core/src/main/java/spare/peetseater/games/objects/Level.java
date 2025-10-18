package spare.peetseater.games.objects;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;

public class Level {
    private final Obstacle player;
    private final List<Obstacle> obstacles;
    private final float ballSpeed;
    private final Ball ball;
    private final Vector2 levelSize;
    private boolean ballLaunched;
    private int score;
    private final List<LevelEventConsumer> consumers;

    public Level(
        Obstacle player,
        List<Obstacle> obstacles,
        float ballSpeed,
        Vector2 levelSize
    ) {
        this.score = 0;
        this.player = player;
        this.ballLaunched = false;
        this.ball = new Ball(
            this.player.getPosition()
                .add(player.getDimensions().x/2f, player.getDimensions().y)
            , Vector2.Zero.cpy()
        );
        this.obstacles = obstacles;
        this.ballSpeed = ballSpeed;
        this.levelSize = levelSize;
        this.consumers = new LinkedList<>();
    }

    public void addConsumer(LevelEventConsumer levelEventConsumer) {
        this.consumers.add(levelEventConsumer);
    }

    public void launchBall(Vector2 velocity) {
        this.ballLaunched = true;
        this.ball.setVelocity(velocity);
    }

    private void collideBallWithObstacle(Obstacle obstacle, float delta) {
        Vector2 ov = obstacle.getVelocity();
        Vector2 reflection = ball.getCenter().sub(obstacle.getCenter());
        reflection.x = Math.signum(reflection.x);
        reflection.y = Math.signum(reflection.y);
        Vector2 bv = this.ball.getVelocity();
        bv.x = Math.abs(bv.x);
        bv.y = Math.abs(bv.y);
        Vector2 v = bv.scl(reflection).add(ov).scl(1.1f);
        v.clamp(-250f, 250f);
        this.ball.setVelocity(v);
        this.ball.update(delta);
    }

    public void update(float delta) {
        this.player.update(delta);

        boolean ballHitSomething = false;
        // Obstacle collisions
        Obstacle toRemove = null;
        for (Obstacle obstacle : obstacles) {
            if (this.ball.willIntersect(obstacle, delta)) {
                collideBallWithObstacle(obstacle, delta);
                score += obstacle.getWorth();
                toRemove = obstacle;
                ballHitSomething = true;
                break;
            }
        }
        if (toRemove != null) {
            for (LevelEventConsumer consumer : consumers) {
                consumer.onObstacleRemove(this, toRemove);
            }
            obstacles.remove(toRemove);
            if (obstacles.isEmpty()) {
                for (LevelEventConsumer consumer : consumers) {
                    consumer.onAllObstaclesRemoved(this);
                }
            }
        }

        // Paddle collisions
        if (this.ball.willIntersect(player, delta)) {
            collideBallWithObstacle(player, delta);
            ballHitSomething = true;
        }
        if (!ballHitSomething) {
            this.ball.update(delta);
        }
        if (!ballLaunched) {
            this.ball.centeredOnTopOf(this.player);
        }
        if (this.ball.getPosition().y <= 0) {
            for (LevelEventConsumer consumer : consumers) {
                consumer.onBallOutOfBounds(this);
            }
        }
        this.ball.clamp(levelSize);
        this.player.clamp(levelSize);
    }

    public Ball getBall() {
        return ball;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Obstacle getPlayer() {
        return player;
    }

    public int getScore() {
        return this.score;
    }

    public void resetBall() {
        this.ballLaunched = false;
        this.ball.setVelocity(Vector2.Zero.cpy());
        this.ball.setPosition(
            this.player.getPosition()
                .add(player.getDimensions().x/2f, player.getDimensions().y)
        );
    }

    public float getBallSpeed() {
        return ballSpeed;
    }
}
