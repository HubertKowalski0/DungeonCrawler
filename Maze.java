import java.util.*;

public class Maze {
    private final int width, height;
    private final char[][] maze;
    private final Random random = new Random();
    private int[] start;
    private int[] end;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new char[height][width];
        generateMaze();
    }

    private void generateMaze() {
        for (int y = 0; y < height; y++) {
            Arrays.fill(maze[y], '#');
        }

        Deque<int[]> stack = new ArrayDeque<>();
        int startX = random.nextInt(width / 2) * 2 + 1;
        int startY = random.nextInt(height / 2) * 2 + 1;
        stack.push(new int[]{startX, startY});
        maze[startY][startX] = ' ';
        start = new int[]{startX, startY};

        int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}};
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            List<int[]> neighbors = new ArrayList<>();
            for (int[] direction : directions) {
                int nx = current[0] + direction[0];
                int ny = current[1] + direction[1];
                if (nx > 0 && ny > 0 && nx < width - 1 && ny < height - 1 && maze[ny][nx] == '#') {
                    neighbors.add(new int[]{nx, ny});
                }
            }

            if (!neighbors.isEmpty()) {
                int[] next = neighbors.get(random.nextInt(neighbors.size()));
                int wx = (current[0] + next[0]) / 2;
                int wy = (current[1] + next[1]) / 2;
                maze[wy][wx] = ' ';
                maze[next[1]][next[0]] = ' ';
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        int endX = random.nextInt(width / 2) * 2 + 1;
        int endY = random.nextInt(height / 2) * 2 + 1;
        maze[endY][endX] = 'E';
        end = new int[]{endX, endY};
    }

    public char[][] getMaze() {
        return maze;
    }

    public int[] getStart() {
        return start;
    }

    public int[] getEnd() {
        return end;
    }
}



