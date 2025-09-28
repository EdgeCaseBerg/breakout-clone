package spare.peetseater.games.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
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

    /*
     * https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    private boolean counterclockwise(Vector2 a, Vector2 b, Vector2 c) {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
    }

    private boolean intersect(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
        return counterclockwise(a,c,d) != counterclockwise(b,c,d) && counterclockwise(a,b,c) != counterclockwise(a,b,d);
    }

    public boolean willIntersect(Obstacle obstacle, float time) {
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

        for (int i = 0; i < corners.length; i++) {
            Vector2 ballStart = corners[i];
            float endX = ballStart.x + velocity.x * time;
            float endY = ballStart.y + velocity.y * time;
            Vector2 ballEnd = new Vector2(endX, endY);
            // Ball XY to End XY is now a line segment that we need to check if it
            // passes through any of the walls of the obstacle.
            float ow = obstacle.getDimensions().x;
            float oh = obstacle.getDimensions().y;

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
}
