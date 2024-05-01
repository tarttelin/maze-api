package com.pyruby;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Maze {
    private static final Random rand = new Random();
    private final int width;
    private final int height;
    private List<Cell> cells;
    private String secret = "";

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        generateMaze();
    }

    public String getSecret() {
        return secret;
    }

    public Cell getCell(int x, int y) {
        int idx = x + y * width;
        return cells.get(idx);
    }

    private void generateMaze() {
        cells = IntStream.range(0, width * height).mapToObj(idx ->
                new Cell(idx % width, idx / width)).toList();
        hideSecret();
        walkMaze(this, getCell(rand.nextInt(width - 1), rand.nextInt(height -1)), new LinkedList<>(), 1);
    }

    private void hideSecret() {
        int codeLength = 10;
        IntStream.range(0, codeLength).forEach(i -> secret += (char) rand.nextInt(65, 90));
        int[] secretPos = rand.ints(1, width * height).distinct().limit(codeLength).sorted().toArray();

        List<String> codes = new ArrayList<>(IntStream.range(0, codeLength).mapToObj(i -> "" + secret.charAt(i)).toList());
        Arrays.stream(secretPos).forEach(pos -> getCell(pos % width, pos / width).setSecret(codes.removeFirst()));
    }

    private static Maze walkMaze(Maze maze, Cell current, List<Cell> stack, int count) {
        current.visited = true;
        if (count == maze.height * maze.width) {
            return maze;
        }
        Map<Wall, Cell> unvisited = getUnvisitedNeighbours(maze, current);
        if (unvisited.isEmpty()) {
            Cell cell = stack.removeLast();
            return walkMaze(maze, cell, stack, count);
        } else {
            stack.add(current);
            int neighbourId = rand.nextInt(unvisited.size());
            Wall heading = unvisited.keySet().stream().limit(neighbourId + 1).toList().getLast();
            Wall comingFrom = Wall.values()[(heading.ordinal() + 2) % Wall.values().length];
            Cell cell = unvisited.get(heading);
            current.walls.remove(heading);
            cell.walls.remove(comingFrom);
            return walkMaze(maze, cell, stack, count + 1);
        }
    }

    private static Map<Wall, Cell> getUnvisitedNeighbours(Maze maze, Cell current) {
        Map<Wall, Cell> unvisited = new HashMap<>();
        if (current.y > 0) {
            Cell cell = maze.getCell(current.x, current.y - 1);
            if (!cell.visited) {
                unvisited.put(Wall.North, cell);
            }
        }
        if (current.x < maze.width - 1) {
            Cell cell = maze.getCell(current.x + 1, current.y);
            if (!cell.visited) {
                unvisited.put(Wall.East, cell);
            }
        }
        if (current.y < maze.height - 1) {
            Cell cell = maze.getCell(current.x, current.y + 1);
            if (!cell.visited) {
                unvisited.put(Wall.South, cell);
            }
        }
        if (current.x > 0) {
            Cell cell = maze.getCell(current.x - 1, current.y);
            if (!cell.visited) {
                unvisited.put(Wall.West, cell);
            }
        }
        return unvisited;
    }

    public static class Cell {
        private final int x;
        private final int y;
        private final List<Wall> walls = new ArrayList<>(List.of(Wall.values()));
        protected boolean visited;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private String secret;

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getSecret() {
            return secret;
        }

        public List<Wall> getWalls() {
            return Collections.unmodifiableList(walls);
        }
    }

    protected void drawMaze() throws IOException {
        int lineLength = 25;
        BufferedImage image = new BufferedImage(width * lineLength + 1, height * lineLength + 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        for (var cell : cells) {
            int x = cell.x * lineLength;
            int y = cell.y * lineLength;
            if (cell.walls.contains(Wall.North)) {
                g.drawLine(x, y, x + lineLength, y);
            }
            if (cell.walls.contains(Wall.East)) {
                g.drawLine(x + lineLength, y, x + lineLength, y + lineLength);
            }
            if (cell.walls.contains(Wall.South)) {
                g.drawLine(x, y + lineLength, x + lineLength, y + lineLength);
            }
            if (cell.walls.contains(Wall.West)) {
                g.drawLine(x, y, x, y + lineLength);
            }
            if (cell.getSecret() != null) {
                g.drawString(cell.getSecret(), x + 8, y + 18);
            }
        }
        ImageIO.write(image, "PNG", new File("randomMaze.PNG"));
    }
}
