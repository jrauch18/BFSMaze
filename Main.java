		 /* COP 3503C Assignment 4
		 This program is written by: Jack Rauch*/

import java.util.*;

public class Main {
    //define a Point class to keep track of the coordinates and the number of moves made.
    static class Point {
        int x, y, moves;

        Point(int x, int y, int moves) {
            this.x = x;
            this.y = y;
            this.moves = moves; //number of moves to reach this point
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt();
        int c = sc.nextInt();
        sc.nextLine();

        char[][] maze = new char[r][c];
        Map<Character, List<Point>> teleportationPoints = new HashMap<>();
        Point start = null, end = null;

        //reading the maze and identifying special points (start, end, and teleportation points)
        for (int i = 0; i < r; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < c; j++) {
                maze[i][j] = line.charAt(j);
                if (maze[i][j] == '*') {
                    start = new Point(i, j, 0); //setting the start point
                } else if (maze[i][j] == '$') {
                    end = new Point(i, j, 0); //setting the end point
                } else if (Character.isUpperCase(maze[i][j])) {
                    //recording teleportation points
                    teleportationPoints.computeIfAbsent(maze[i][j], k -> new ArrayList<>()).add(new Point(i, j, 0));
                }
            }
        }
        sc.close(); //closing the scanner

        //calculate and print the minimum number of moves
        int result = minMoves(maze, start, end, teleportationPoints);
        System.out.println(result == -1 ? "Call 911" : result);
    }

    private static int minMoves(char[][] maze, Point start, Point end, Map<Character, List<Point>> teleportationPoints) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}}; //possible directions to move
        Queue<Point> queue = new LinkedList<>();
        queue.offer(start);

        boolean[][] visited = new boolean[maze.length][maze[0].length];
        visited[start.x][start.y] = true;

        //performing BFS
        while (!queue.isEmpty()) {
            Point current = queue.poll();

            //if the end is reached, return the number of moves
            if (current.x == end.x && current.y == end.y) {
                return current.moves;
            }

            char cell = maze[current.x][current.y];
            //handling teleportation
            if (Character.isUpperCase(cell)) {
                for (Point tp : teleportationPoints.get(cell)) {
                    if (!visited[tp.x][tp.y]) {
                        visited[tp.x][tp.y] = true;
                        queue.offer(new Point(tp.x, tp.y, current.moves + 1));
                    }
                }
            }

            //exploring neighboring cells
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                //check the validity of the new position and whether it's visited or not
                if (newX >= 0 && newY >= 0 && newX < maze.length && newY < maze[0].length && !visited[newX][newY]
                        && maze[newX][newY] != '!') {
                    visited[newX][newY] = true;
                    queue.offer(new Point(newX, newY, current.moves + 1)); // Offer the new position to the queue with the updated moves
                }
            }
        }

        //if the end is unreachable, return -1, and call 911!
        return -1;
    }
}
