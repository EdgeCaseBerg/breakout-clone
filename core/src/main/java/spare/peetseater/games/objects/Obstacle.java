package spare.peetseater.games.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Obstacle {
    private final Vector2 position;
    private final Vector2 velocity;
    private final Vector2 dimensions;
    private final int worth;

    public Obstacle(Vector2 position, Vector2 dimensions) {
        this(position, dimensions, Vector2.Zero);
    }

    public Obstacle(Vector2 position, Vector2 dimensions, Vector2 velocity) {
        this(position, dimensions, velocity, 1);
    }

    public Obstacle(Vector2 position, Vector2 dimensions, Vector2 velocity, int worth) {
        this.position = position.cpy();
        this.dimensions = dimensions.cpy();
        this.velocity = velocity.cpy();
        this.worth = worth;
    }

    public static Obstacle RedBrick(Vector2 position, Vector2 dimensions) {
        return new Obstacle(position, dimensions, Vector2.Zero, 7);
    }

    public static Obstacle OrangeBrick(Vector2 position, Vector2 dimensions) {
        return new Obstacle(position, dimensions, Vector2.Zero, 5);
    }

    public static Obstacle GreenBrick(Vector2 position, Vector2 dimensions) {
        return new Obstacle(position, dimensions, Vector2.Zero, 3);
    }

    public static Obstacle YellowBrick(Vector2 position, Vector2 dimensions) {
        return new Obstacle(position, dimensions, Vector2.Zero, 1);
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

    public int getWorth() {
        return worth;
    }

    public void clamp(Vector2 levelSize) {
        position.x = MathUtils.clamp(position.x, 0, levelSize.x);
        position.y = MathUtils.clamp(position.y, 0, levelSize.y);
    }
}
