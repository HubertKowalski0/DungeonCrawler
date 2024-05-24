import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 30;
    private final int[] mazeSizes = {18, 21, 25, 29, 33};
    private Maze maze;
    private int[] playerPosition;
    private int level;
    private boolean gameWon;
    private List<int[]> pathToFollow;
    private int pathIndex;
    private List<int[]> pathHistory;

    public GamePanel() {
        this.level = 1;
        this.gameWon = false;
        this.pathToFollow = null;
        this.pathIndex = 0;
        this.pathHistory = new ArrayList<>();
        loadLevel();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameWon && pathToFollow == null) {
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / TILE_SIZE;
                int y = e.getY() / TILE_SIZE;
                if (x >= 0 && x < maze.getMaze()[0].length && y >= 0 && y < maze.getMaze().length) {
                    List<int[]> path = maze.findPath(playerPosition, new int[]{x, y});
                    if (!path.isEmpty()) {
                        pathToFollow = path;
                        pathIndex = 0;
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
        this.pathHistory.clear();
        this.pathHistory.add(playerPosition.clone());
    }

    private void movePlayer(int dx, int dy) {
        int x = playerPosition[0] + dx;
        int y = playerPosition[1] + dy;
        if (x >= 0 && x < maze.getMaze()[0].length && y >= 0 && y < maze.getMaze().length) {
            if (maze.getMaze()[y][x] == ' ') {
                playerPosition[0] = x;
                playerPosition[1] = y;
                pathHistory.add(playerPosition.clone());
            } else if (maze.getMaze()[y][x] == 'E') {
                if (level == 5) {
                    gameWon = true;
                } else {
                    level++;
                    loadLevel();
                }
            } else {
                playerPosition = maze.getStart();
                pathHistory.clear();
                pathHistory.add(playerPosition.clone());
            }
        }
    }

    private void followPath() {
        if (pathToFollow != null && pathIndex < pathToFollow.size()) {
            playerPosition = pathToFollow.get(pathIndex);
            pathHistory.add(playerPosition.clone());
            pathIndex++;
            if (pathIndex >= pathToFollow.size()) {
                pathToFollow = null;
            }
            repaint();
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

        g.setColor(Color.GREEN);
        for (int[] step : pathHistory) {
            g.fillRect(step[0] * TILE_SIZE, step[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.BLUE);
        g.fillRect(playerPosition[0] * TILE_SIZE, playerPosition[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        if (gameWon) {
            g.setColor(Color.GREEN);
            g.drawString("Gratuluję Zwycięstwa!", 50, 50);
        }

        if (pathToFollow != null) {
            g.setColor(Color.GREEN);
            for (int[] step : pathToFollow) {
                g.fillRect(step[0] * TILE_SIZE, step[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
            followPath();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int size = mazeSizes[level - 1];
        return new Dimension(size * TILE_SIZE, size * TILE_SIZE);
    }
}
