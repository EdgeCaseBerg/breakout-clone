package spare.peetseater.games.objects;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Level {
    private final Obstacle player;
    private final List<Obstacle> obstacles;
    private final float ballSpeed;
    private final Ball ball;
    private int score;

    public Level(Obstacle player, List<Obstacle> obstacles, float ballSpeed) {
        this.score = 0;
        this.player = player;
        this.ball = new Ball(
            this.player.getPosition()
                .add(player.getDimensions().x/2f, player.getDimensions().y)
            , Vector2.Zero.cpy()
        );
        this.obstacles = obstacles;
        this.ballSpeed = ballSpeed;
    }

    public void launchBall(Vector2 velocity) {
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
        Vector2 v = bv.scl(reflection).add(ov);
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
                score += toRemove.getWorth();
                toRemove = obstacle;
                ballHitSomething = true;
                break;
            }
        }
        if (toRemove != null) {
            obstacles.remove(toRemove);
        }

        // Paddle collisions
        if (this.ball.willIntersect(player, delta)) {
            collideBallWithObstacle(player, delta);
            ballHitSomething = true;
        }
        if (!ballHitSomething) {
            this.ball.update(delta);
        }
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
}
