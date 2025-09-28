package spare.peetseater.games.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private final Vector2 position;
    private final Vector2 dimensions;

    public Obstacle(Vector2 position, Vector2 dimensions) {
        this.position = position.cpy();
        this.dimensions = dimensions.cpy();
    }

    /** It is assumed that intersection has been computed if we are using this */
    public float getBounceAngle(Ball ball) {
        // use center point for bounce determination!
        float bx = ball.getPosition().x + ball.getDimensions().x/2f;
        float by = ball.getPosition().y + ball.getDimensions().y/2f;

        float ox = position.x + dimensions.x /2f;
        float oy = position.y + dimensions.y /2f;

        boolean ballToTheLeft   = bx < ox;
        boolean ballUnderneath  = by < oy;
        boolean ballToTheRight = !ballToTheLeft;

        // Off to the quadrant you go
        float angle1;
        float angle2;
        if (ballToTheLeft && ballUnderneath) {
            // 180 - 270 (but nix the edge, we don't want purely horizontal/vertical movement ever.
            angle1 = 181;
            angle2 = 269;
        } else if (ballToTheLeft) {
            // 90 - 180
            angle1 = 91;
            angle2 = 179;
        } else if (ballToTheRight && ballUnderneath) {
            // 270 - 360
            angle1 = 271;
            angle2 = 359;
        } else {
            // 0 - 90
            angle1 = 1;
            angle2 = 89;
        }

        float radA = MathUtils.degreesToRadians * angle1;
        float radB = MathUtils.degreesToRadians * angle2;
        return MathUtils.lerpAngle(radA, radB, MathUtils.random());
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getDimensions() {
        return dimensions.cpy();
    }
}
