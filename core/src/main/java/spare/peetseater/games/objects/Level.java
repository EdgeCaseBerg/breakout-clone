package spare.peetseater.games.objects;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Level {
    private final Obstacle player;
    private final List<Obstacle> obstacles;
    private final Ball ball;

    public Level(Obstacle player, List<Obstacle> obstacles) {
        this.player = player;
        this.ball = new Ball(
            this.player.getPosition()
                .add(player.getDimensions().x/2f, player.getDimensions().y)
            , Vector2.Zero
        );
        this.obstacles = obstacles;
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
