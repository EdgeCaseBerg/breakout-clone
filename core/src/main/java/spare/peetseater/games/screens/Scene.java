package spare.peetseater.games.screens;

public interface Scene {
    public String getBundleName();
    public void update(float seconds);
    public void render(float delta);
    public void dispose();
}
