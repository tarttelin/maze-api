package com.pyruby;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MazeTest {

    @Test
    void newMaze_createsARandomSecret() {
        Maze maze = new Maze(20, 20);
        assertThat(maze).isNotNull();
        assertThat(maze.getSecret()).isNotNull();
        assertThat(maze.getSecret().length()).isEqualTo(10);

        Maze anotherMaze = new Maze(20, 20);
        assertThat(anotherMaze.getSecret()).isNotEqualTo(maze.getSecret());
    }

    @Test
    void newMaze_distributesTheSecretAcrossCellsInTheMazeInOrder() {
        var maze = new Maze(25, 20);
        String cellSecret = IntStream.range(0, 20).mapToObj(y -> y)
                .flatMap(y ->
                        IntStream.range(0, 25).mapToObj(x -> maze.getCell(x, y))
                ).map(Maze.Cell::getSecret)
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        assertThat(cellSecret).isEqualTo(maze.getSecret());
    }

    @Test
    void newMaze_hasEveryCellAccessibleToAtLeastOneNeighbour() {
        var maze = new Maze(25, 20);
        IntStream.range(0, 20).forEach(y ->
                IntStream.range(0, 25).forEach(x -> {
                    var cell = maze.getCell(x, y);
                    assertThat(cell.getWalls()).isNotNull();
                    assertThat(cell.getWalls().size()).isLessThan(4);
                })
        );
    }

    @Test
    void newMaze_edgesHaveWalls() throws IOException {
        var maze = new Maze(25, 20);
        IntStream.range(0, 20).forEach(y -> {
            var cell = maze.getCell(0, y);
            assertThat(cell.getWalls()).contains(Wall.West);
            cell = maze.getCell(24, y);
            assertThat(cell.getWalls()).contains(Wall.East);
        });
        IntStream.range(0, 25).forEach(x -> {
            var cell = maze.getCell(x, 0);
            assertThat(cell.getWalls()).contains(Wall.North);
            cell = maze.getCell(x, 19);
            assertThat(cell.getWalls()).contains(Wall.South);
        });
        maze.drawMaze();
        System.out.println("secret = " + maze.getSecret());
    }
}