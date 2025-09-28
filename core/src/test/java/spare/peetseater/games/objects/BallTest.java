package spare.peetseater.games.objects;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {

    @org.junit.jupiter.api.Test
    void willNotIntersectWith0SpeedNotTouching() {
        Vector2 bp = new Vector2(1, 1);
        Vector2 bv = Vector2.Zero;
        Ball ball = new Ball(bp, bv); // Balls are 10x10 units big

        Vector2 op = new Vector2(12, 12);
        Vector2 od = new Vector2(2, 2);
        Obstacle obstacle = new Obstacle(op, od);

        assertFalse(ball.willIntersect(obstacle, 10));
    }

    @org.junit.jupiter.api.Test
    void willIntersectWith0SpeedTouching() {
        Vector2 bp = new Vector2(1, 1);
        Vector2 bv = Vector2.Zero;
        Ball ball = new Ball(bp, bv);

        Vector2 op = new Vector2(1, 11);
        Vector2 od = new Vector2(2, 3);
        Obstacle obstacle = new Obstacle(op, od);

        assertTrue(ball.willIntersect(obstacle, 0));
    }

    @ParameterizedTest
    @MethodSource("intersectCases")
    void willIntersect(TestCase testCase) {
        boolean willIntersect = testCase.ball.willIntersect(testCase.obstacle, 2f);
        assertTrue(willIntersect, testCase);
    }

    // Ball is at 10,10 and dimensions are 10x10
    // Velocity is settable and there is an obstacle with dimenions 5x5 at the given position.
    static class TestCase implements Supplier<String> {
        private final Ball ball;
        private final Obstacle obstacle;
        Vector2 velocity;
        public TestCase(Vector2 ballVelocity, Vector2 obstaclePosition) {
            this.velocity = ballVelocity;
            this.ball = new Ball(new Vector2(10, 10), ballVelocity.cpy());
            this.obstacle = new Obstacle(obstaclePosition, new Vector2(5, 5));
        }

        public static TestCase setup(float vx, float vy, float ox, float oy) {
            return new TestCase(new Vector2(vx, vy), new Vector2(ox, oy));
        }

        public Ball getBall() {
            return this.ball;
        }

        public Obstacle getObstacle() {
            return this.obstacle;
        }

        @Override
        public String get() {
            return String.format("10x10 Ball at %f,%f, 5x5 Obstacle at %f, %f, velocity of %f,%f",
                ball.getPosition().x, ball.getPosition().y,
                obstacle.getPosition().x, obstacle.getPosition().y,
                velocity.x, velocity.y
            );
        }
    }
    /* Cases where the ball, moving along a vector, will hit the obstacle.
     *        (bx, by)
     *         \
     *          \
     *       (ox, oy)
     *            \
     * */
    static Stream<TestCase> intersectCases() {
        // Ball is at 10,10
        // velocity bx,by and obstacle position ox, oy are set up.
        // velocities are configured to go past the obstacle in 1 time unit
        return Stream.of(
            TestCase.setup(6, 0, 22, 10), // to the right
            TestCase.setup(-6, 0, 6, 10), // to the left
            TestCase.setup(0, 7, 10, 22), // directly above
            TestCase.setup(0, -7, 15, 4), // directly below
            TestCase.setup(6, 6, 21, 21), // diagonally to the up right
            TestCase.setup(-6, 6, 4, 25), // diagonally to the up left
            TestCase.setup(6, -6, 22, 7), // diagonally to the down right
            TestCase.setup(-6, -6, 4, 7)  // diagonally to the down left
        );
    }
}
