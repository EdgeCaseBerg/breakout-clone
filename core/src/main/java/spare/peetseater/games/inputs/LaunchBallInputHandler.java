package spare.peetseater.games.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import spare.peetseater.games.objects.Level;

public class LaunchBallInputHandler extends PaddleInputHandler {

    final Level level;
    final Vector2 launchVector;
    private boolean isLaunched;

    public LaunchBallInputHandler(Level level) {
        super(level.getPlayer());
        this.level = level;
        this.launchVector = new Vector2(1, 1);
        this.isLaunched = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            this.isLaunched = true;
            level.launchBall(launchVector.cpy().scl(50));
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 center = this.level.getPlayer().getCenter();
        float width = this.level.getPlayer().getDimensions().x;
        float clampedMx = MathUtils.clamp(screenX, center.x - width/2f, center.x + width/2f) - center.x;
        float unitX = clampedMx / width;
        launchVector.x = unitX;
        launchVector.y = 1;
        return super.mouseMoved(screenX, screenY);
    }

    public Vector2 getLaunchVector() {
        return launchVector.cpy();
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void resetLaunched() {
        isLaunched = false;
    }
}
