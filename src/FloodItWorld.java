import java.awt.*;
import java.util.ArrayList;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.EllipseImage;
import javalib.worldimages.FontStyle;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayOffsetImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

//Represents the game grid
public class FloodItWorld extends World {
  ArrayList<Cell> grid;
  int size;
  int colors;
  int limit;
  int turns = 0;
  String prev = "";
  String post = "";
  // Raw time for game-end scenario display
  int time = 0;
  // Minute-based time for info display
  int displayTime = 0;
  int minutes = 0;
  // Time when flooding ends for game-end scenario display
  int turnEnd = 0;

  // Standard constructor for starting a real game
  FloodItWorld(int size, int colors) {
    this.size = size;
    this.colors = colors;
    this.limit = (int) (colors * size * 0.3);
    createCells(size);
    setAdjacent();
  }

  // Testing constructor for a test example
  FloodItWorld() {
    size = 3;
    colors = 3;
    limit = 3;
  }

  // EFFECT: Creates new Cells with i and j coordinates, with only the corner Cell
  // flooded to begin
  public void createCells(int size) {
    grid = new ArrayList<Cell>();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == 0 && j == 0) {
          grid.add(new Cell(0, 0, colors, true));
          post = grid.get(0).color;
        }
        else {
          grid.add(new Cell(i, j, colors, false));
        }
      }
    }
  }

  // EFFECT: Sets the adjacent cell for every cell on the grid
  public void setAdjacent() {
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      if (cell.x > 0) {
        cell.left = grid.get(i - size);
      }
      if (cell.x < size - 1) {
        cell.right = grid.get(i + size);
      }
      if (cell.y > 0) {
        cell.top = grid.get(i - 1);
      }
      if (cell.y < size - 1) {
        cell.bottom = grid.get(i + 1);
      }
    }
  }

  // Creates the display that the user sees on every tick
  public WorldScene makeScene() {
    WorldScene game = new WorldScene(600, 750);

    // Displays the background
    game.placeImageXY(new RectangleImage(600, 750, OutlineMode.SOLID, Color.GRAY), 300, 375);

    // Displays the title backdrop
    game.placeImageXY(new EllipseImage(350, 80, OutlineMode.SOLID, Color.WHITE), 300,
            (330 - 10 * size) / 2);

    // Displays the title
    game.placeImageXY(new TextImage("Flood It", 60, FontStyle.BOLD, Color.BLACK), 300,
            (330 - 10 * size) / 2);

    // Displays the border
    game.placeImageXY(
            new RectangleImage((size + 2) * 20, (size + 2) * 20, OutlineMode.SOLID, Color.WHITE), 300,
            350);
    game.placeImageXY(
            new RectangleImage((size + 1) * 20, (size + 1) * 20, OutlineMode.SOLID, Color.BLACK), 300,
            350);

    // Displays the Cells inside the border
    for (Cell cell : grid) {
      game.placeImageXY(cell.image(), (310 - 10 * size) + 20 * cell.x,
              (360 - 10 * size) + 20 * cell.y);
    }

    // Handles the time when a minute has passed
    if (displayTime == 3000) {
      minutes++;
      displayTime = 0;
    }

    // Accurately converts the time to a string
    TextImage timeDisplay = new TextImage("", 30, Color.BLACK);
    if (minutes == 1 && displayTime / 50 == 1) {
      timeDisplay = new TextImage(
              "Time: " + minutes + " minute, " + displayTime / 50 + " second", 30, Color.BLACK);
    }
    else if (minutes == 1) {
      timeDisplay = new TextImage(
              "Time: " + minutes + " minute, " + displayTime / 50 + " seconds", 30, Color.BLACK);
    }
    else if (displayTime / 50 == 1) {
      timeDisplay = new TextImage(
              "Time: " + minutes + " minutes, " + displayTime / 50 + " second", 30, Color.BLACK);
    }
    else {
      timeDisplay = new TextImage(
              "Time: " + minutes + " minutes, " + displayTime / 50 + " seconds", 30, Color.BLACK);
    }

    // Creates a panel with game information
    WorldImage info = new OverlayOffsetImage(
            new TextImage(
                    "Turns: " + Integer.toString(turns) + " / " + Integer.toString(limit), 30, Color.BLACK),
            0, 35,
            new OverlayOffsetImage(timeDisplay, 0, -5,
                    new OverlayOffsetImage(
                            new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK), 0, -40,
                            new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE))));

    // Displays the panel
    game.placeImageXY(info, 300, 750 - ((750 - (370 + 10 * size)) / 2));

    // Creates a new info panel for when the player wins
    WorldImage win = new OverlayOffsetImage(
            new TextImage("You Win!", 60, FontStyle.BOLD, Color.GREEN), 0, 15,
            new OverlayOffsetImage(
                    new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK), 0, -40,
                    new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));

    // Creates a new info panel for when the player loses
    WorldImage loss = new OverlayOffsetImage(
            new TextImage("Game Over", 60, FontStyle.BOLD, Color.RED), 0, 15,
            new OverlayOffsetImage(
                    new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK), 0, -40,
                    new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));

    // Displays a win or loss message when the turn limit is reached
    if (turns <= limit && time >= turnEnd && allFlooded()) {
      game.placeImageXY(win, 300, 750 - ((750 - (370 + 10 * size)) / 2));
    }
    if (turns >= limit && time >= turnEnd && !allFlooded()) {
      game.placeImageXY(loss, 300, 750 - ((750 - (370 + 10 * size)) / 2));
    }

    return game;
  }

  // Checks if all the Cells in the grid are flooded by testing if they are all the same color,
  // given that not all Cells are flooded even if the win condition is met given the nature of
  // floodGrid and the waterfall effect
  public boolean allFlooded() {
    boolean result = true;
    for (Cell cell : grid) {
      result = result && cell.color == post;
    }
    if (result) {
      for (Cell cell : grid) {
        cell.flooded = true;
      }
      return result;
    }
    return result;
  }

  // EFFECT: Restarts a the game upon pressing the "r" key
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      createCells(size);
      setAdjacent();
      turns = 0;
      time = 0;
      displayTime = 0;
      minutes = 0;
    }
  }

  // EFFECT: Updates the game grid when the mouse is clicked
  public void onMouseClicked(Posn pos) {
    boolean anyFlooded = false;
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      anyFlooded = anyFlooded || cell.flooded;
    }
    // Checks to make sure the click was in bounds, the turns taken are below the turn limit,
    // the clicked color isn't the same as the current corner, and that the waterfall animation
    // is not currently occurring
    if (((pos.x < (300 - 10 * size) || pos.x > (300 - 10 * size) + 20 * size)
            || (pos.y < (350 - 10 * size) || pos.y > (350 - 10 * size) + 20 * size))
            || turns >= limit || (clickedCell(pos)).color == post || anyFlooded) {
      return;
    }
    Cell corner = grid.get(0);
    prev = corner.color;
    post = (clickedCell(pos)).color;
    corner.color = post;
    corner.flooded = true;
    turns++;
    turnEnd = time + (size * 2);
  }

  // Determines which Cell was clicked on by the mouse
  public Cell clickedCell(Posn pos) {
    Cell clicked = null;
    for (Cell cell : grid) {
      if ((cell.x <= (pos.x - (300 - 10 * size)) / 20 && (pos.x - (300 - 10 * size)) / 20 <= cell.x)
              && (cell.y <= (pos.y - (350 - 10 * size)) / 20
              && (pos.y - (350 - 10 * size)) / 20 <= cell.y)) {
        clicked = cell;
      }
    }
    return clicked;
  }

  // EFFECT: Updates the game every tick
  public void onTick() {
    // Updates the grid display in order to continue flooding if it is occurring
    floodGrid();
    time++;
    displayTime++;
  }

  // EFFECT: Floods the grid starting from the top left corner Cell, achieving the waterfall
  // effect by flooding in so called "waves"
  public void floodGrid() {
    // Floods the Cells adjacent to those that are already flooded
    // while unflooding those that currently are, effectively flooding the next "wave" while
    // unflooding the current one
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      if (cell.flooded && cell.color == post) {
        cell.adjacentFlooded(prev);
        cell.flooded = false;
      }
    }
    // Changes the color of the current flooded Cells, or the new "wave"
    for (int i = 0; i < grid.size(); i++) {
      Cell cell = grid.get(i);
      if (cell.flooded && cell.color == prev) {
        cell.color = post;
      }
    }
    // Updates the display
    makeScene();
  }

  // EFFECT: Initializes the game and restricts unmanageable inputs with exceptions
  public void initGame(int size, int colors) {
    if (size < 2) {
      throw new IllegalArgumentException("Grid dimensions must be at least 2x2");
    }
    else if (size > 22) {
      throw new IllegalArgumentException("Grid dimensions may not exceed 22x22");
    }
    else if (colors < 3) {
      throw new IllegalArgumentException("Grid must contain at least 3 colors");
    }
    else if (colors > 8) {
      throw new IllegalArgumentException("Grid may not exceed 8 colors");
    }
    FloodItWorld game = new FloodItWorld(size, colors);
    game.bigBang(600, 750, 0.02);
  }
}
