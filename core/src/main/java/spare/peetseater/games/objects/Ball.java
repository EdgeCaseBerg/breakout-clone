package spare.peetseater.games.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private final Vector2 position;
    private final Vector2 velocity;
    private Vector2 dimensions;

    public Ball(Vector2 position, Vector2 velocity) {
        this.position = position.cpy();
        this.velocity = velocity.cpy();
        this.dimensions = new Vector2(10, 10);
    }

    public boolean willIntersect(Vector2 point, float time) {
        return false;
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public Vector2 getDimensions() {
        return dimensions.cpy();
    }
}
