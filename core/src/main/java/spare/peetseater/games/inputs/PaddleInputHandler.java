package spare.peetseater.games.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import spare.peetseater.games.objects.Obstacle;

public class PaddleInputHandler extends InputAdapter {

    private final Obstacle player;

    public PaddleInputHandler(Obstacle player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                setPlayerVelocityLeft();
                return true;
            case Input.Keys.RIGHT:
                setPlayerVelocityRight();
                return true;
            default:
                setPlayerVelocityZero();
                return super.keyDown(keycode);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT: setPlayerVelocityZero();
            case Input.Keys.RIGHT: setPlayerVelocityZero();
                return true;
            default:
                return super.keyDown(keycode);
        }
    }

    private void setPlayerVelocityZero() {
        this.player.setVelocity(Vector2.Zero.cpy());
    }

    private void setPlayerVelocityRight() {
        this.player.setVelocity(Vector2.X.cpy());
    }

    private void setPlayerVelocityLeft() {
        this.player.setVelocity(Vector2.X.cpy().scl(-1f));
    }
}
