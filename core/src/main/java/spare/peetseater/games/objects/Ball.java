package spare.peetseater.games.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private final Vector2 position;
    private Vector2 velocity;
    private Vector2 dimensions;

    public Ball(Vector2 position, Vector2 velocity, Vector2 dimensions) {
        this.position = position.cpy();
        this.velocity = velocity.cpy();
        this.dimensions = dimensions.cpy();
    }

    public Ball(Vector2 position, Vector2 velocity) {
        this(position, velocity, new Vector2(10, 10));
    }

    public Ball projectedAt(float time) {
        Vector2 p = getPosition().add(getVelocity().scl(time));
        return new Ball(p, getVelocity());
    }

    public void update(float time) {
        position.x += velocity.x * time;
        position.y += velocity.y * time;
    }

    /*
     * https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    private boolean counterclockwise(Vector2 a, Vector2 b, Vector2 c) {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
    }

    private boolean intersect(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
        return counterclockwise(a,c,d) != counterclockwise(b,c,d) && counterclockwise(a,b,c) != counterclockwise(a,b,d);
    }

    public boolean intersects(Obstacle obstacle) {
        // For the time being, proper square check
        float pLeftX = obstacle.getPosition().x;
        float pLeftY = obstacle.getPosition().y;
        float pRightX = pLeftX + obstacle.getDimensions().x;
        float pRightY = pLeftY + obstacle.getDimensions().y;

        boolean overlapsXProjection = pRightX >= position.x && (position.x + dimensions.x) >= pLeftX;
        boolean overlapsYProjection = pRightY >= position.y && (position.y + dimensions.y) >= pLeftY;
        return overlapsXProjection && overlapsYProjection;
    }

    public boolean willIntersect(Obstacle obstacle, float time) {
        // Fast check on if we are currently bonking something
        if (intersects(obstacle)) {
            return true;
        }

        // Fast check again on expected position
        Ball nextPosition = this.projectedAt(time);
        if (nextPosition.intersects(obstacle)) {
            return true;
        }

        // If we are moving horizontally or vertically we can extend the box and do a basic intersect.
        // this will be faster than the counterclosewise check we have to do otherwise.
        if (velocity.x == 0 || velocity.y == 0) {
            float x = Math.min(position.x, nextPosition.position.x);
            float y = Math.min(position.y, nextPosition.position.y);
            float w = Math.abs(position.x - nextPosition.position.x) + dimensions.x;
            float h = Math.abs(position.y - nextPosition.position.y) + dimensions.y;
            Ball spaghettification = new Ball(new Vector2(x, y), Vector2.Zero, new Vector2(w, h));
            if (spaghettification.intersects(obstacle)) return true;
        }

        // And finally, the hard part. Dealing with overshooting
        // and landing on a position that is no longer intersecting
        // with the obstacle itself.
        // Our expected line segment from the ball's anchor to the anchor at time t.
        // we compute this from each corner through the walls of the obstacle
        float bw = getDimensions().x;
        float bh = getDimensions().y;
        Vector2[] corners = {
          getPosition(),
          getPosition().add(0, bh),
          getPosition().add(bw, 0 ),
          getPosition().add(getDimensions())
        };
        float ow = obstacle.getDimensions().x;
        float oh = obstacle.getDimensions().y;

        for (int i = 0; i < corners.length; i++) {
            Vector2 ballStart = corners[i];
            float endX = ballStart.x + velocity.x * time;
            float endY = ballStart.y + velocity.y * time;
            Vector2 ballEnd = new Vector2(endX, endY);
            // Ball XY to End XY is now a line segment that we need to check if it
            // passes through any of the walls of the obstacle.
            boolean intersectsBottom = intersect(ballStart, ballEnd, obstacle.getPosition(), obstacle.getPosition().add(ow, 0));
            boolean intersectsLeftWall = intersect(ballStart, ballEnd, obstacle.getPosition(), obstacle.getPosition().add(0, oh));
            boolean intersectsTop = intersect(ballStart, ballEnd, obstacle.getPosition().add(0, oh), obstacle.getPosition().add(ow, oh));
            boolean intersectsRightWall = intersect(ballStart, ballEnd, obstacle.getPosition().add(ow, 0), obstacle.getPosition().add(ow, oh));
            if ( intersectsBottom || intersectsLeftWall || intersectsTop || intersectsRightWall) {
                return true;
            }
        }
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

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity.cpy();
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + dimensions.x/2f, position.y + dimensions.y/2f);
    }

    public void clamp(Vector2 levelSize) {
        if (position.x < 0 ) velocity.x = velocity.x * -1;
        if (position.x > levelSize.x - dimensions.x) velocity.x = velocity.x * -1;
        if (position.y < 0 ) velocity.y = velocity.y * -1;
        if (position.y > levelSize.y - dimensions.y) velocity.y = velocity.y * -1;
        position.x = MathUtils.clamp(position.x, 0, levelSize.x - dimensions.x);
        position.y = MathUtils.clamp(position.y, 0, levelSize.y - dimensions.y);
    }
}
