package spare.peetseater.games.objects;

import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private final Vector2 position;
    private final Vector2 velocity;
    private final Vector2 dimensions;

    public Obstacle(Vector2 position, Vector2 dimensions) {
        this(position, dimensions, Vector2.Zero.cpy());
    }

    public Obstacle(Vector2 position, Vector2 dimensions, Vector2 velocity) {
        this.position = position;
        this.dimensions = dimensions;
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public Vector2 getDimensions() {
        return dimensions.cpy();
    }

    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    public void setVelocity(Vector2 newV) {
        velocity.x = newV.x;
        velocity.y = newV.y;
    }

    public void update(float delta) {
        this.position.set(position.add(getVelocity().scl(100 * delta)));
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + dimensions.x /2f, position.y + dimensions.y/2f);
    }
}
