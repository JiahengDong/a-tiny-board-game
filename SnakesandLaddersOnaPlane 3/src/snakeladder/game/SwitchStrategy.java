package snakeladder.game;

import snakeladder.game.GamePane;
import snakeladder.game.NavigationPane;

public interface SwitchStrategy {
    public boolean applyStrategy(NavigationPane np, GamePane gp);
}
