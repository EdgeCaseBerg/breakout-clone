package spare.peetseater.games.objects;

public interface LevelEventConsumer {
    public void onBallOutOfBounds(Level level);
    public void onObstacleRemove(Level level, Obstacle obstacle);
    public void onAllObstaclesRemoved(Level level);
}
