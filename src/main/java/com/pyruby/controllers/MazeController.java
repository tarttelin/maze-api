package com.pyruby.controllers;

import com.pyruby.maze.model.Solution;
import org.springframework.http.ResponseEntity;

public class MazeController implements com.pyruby.maze.api.MazeApi {

    @Override
    public ResponseEntity<com.pyruby.maze.model.Cell> appApiMazeMoveCell(String id, Integer x, Integer y, String move) {
        return null;
    }

    @Override
    public ResponseEntity<Void> appApiMazeSolve(String id, Solution solution) {
        return null;
    }

    @Override
    public ResponseEntity<com.pyruby.maze.model.MazeResult> mazeGet() {
        return null;
    }

    @Override
    public ResponseEntity<com.pyruby.maze.model.Maze> mazeIdGet(String id) {
        return null;
    }

    @Override
    public ResponseEntity<com.pyruby.maze.model.Maze> mazePost(Boolean assessment) {
        return null;
    }
}
