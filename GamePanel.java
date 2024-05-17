import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 30;
    private final int[] mazeSizes = {18, 21, 25, 29, 33}; // Sizes for each level
    private Maze maze;
    private int[] playerPosition;
    private int level;
    private boolean gameWon;

    public GamePanel() {
        this.level = 1;
        this.gameWon = false;
        loadLevel();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameWon) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            movePlayer(0, -1);
                            break;
                        case KeyEvent.VK_DOWN:
                            movePlayer(0, 1);
                            break;
                        case KeyEvent.VK_LEFT:
                            movePlayer(-1, 0);
                            break;
                        case KeyEvent.VK_RIGHT:
                            movePlayer(1, 0);
                            break;
                    }
                }
                repaint();
            }
        });
    }

    private void loadLevel() {
        int size = mazeSizes[level - 1];
        this.maze = new Maze(size, size);
        this.playerPosition = maze.getStart();
    }

    private void movePlayer(int dx, int dy) {
        int x = playerPosition[0] + dx;
        int y = playerPosition[1] + dy;
        if (x >= 0 && x < maze.getMaze()[0].length && y >= 0 && y < maze.getMaze().length) {
            if (maze.getMaze()[y][x] == ' ') {
                playerPosition[0] = x;
                playerPosition[1] = y;
            } else if (maze.getMaze()[y][x] == 'E') {
                if (level == 5) {
                    gameWon = true;
                } else {
                    level++;
                    loadLevel();
                }
            } else {
                // Uderzenie w ścianę - powrót na początek poziomu
                playerPosition = maze.getStart();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < maze.getMaze().length; y++) {
            for (int x = 0; x < maze.getMaze()[y].length; x++) {
                if (maze.getMaze()[y][x] == '#') {
                    g.setColor(Color.BLACK);
                } else if (maze.getMaze()[y][x] == 'E') {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        g.setColor(Color.BLUE);
        g.fillRect(playerPosition[0] * TILE_SIZE, playerPosition[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        if (gameWon) {
            g.setColor(Color.GREEN);
            g.drawString("Congratulations! You've won the game!", 50, 50);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int size = mazeSizes[level - 1];
        return new Dimension(size * TILE_SIZE, size * TILE_SIZE);
    }
}
