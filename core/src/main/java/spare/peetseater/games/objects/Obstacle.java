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

    public float getBounceAngle(Ball ball) {
        // use center point for bounce determination!
        float xTouchPoint = ball.getPosition().x + ball.getDimensions().x/2f;
        float normalizedX = Math.max(0, xTouchPoint - position.x); // this shouldn't go negative, but just in case.

        float leftAngle = MathUtils.degreesToRadians * (180 - 15);
        float rightAngle = MathUtils.degreesToRadians * (0 + 15);
        return MathUtils.lerpAngle(leftAngle, rightAngle, normalizedX / ball.getDimensions().y);
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getDimensions() {
        return dimensions.cpy();
    }
}
