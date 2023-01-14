import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javalib.impworld.WorldScene;
import javalib.worldimages.EllipseImage;
import javalib.worldimages.FontStyle;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayOffsetImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

public class FloodItTest {
  // Creates examples of Cells
  Cell red;
  Cell yellow;
  Cell green;
  Cell orange;
  Cell magenta;
  Cell cyan;
  Cell blue;
  Cell pink;
  Cell red2;

  // Creates examples of game inputs
  int exampleColors;
  ArrayList<String> exampleLoColors;
  ArrayList<String> exampleColorChoices;
  int exampleSize;
  FloodItWorld exampleFloodIt;

  // Initializes an examples Flood It game for testing
  void init() {

    // Creates a 3x3 grid of customized Cells and updates them when applicable
    red = new Cell(0, 0, "red", true,
            null, null, null, null);
    yellow = new Cell(1, 0, "yellow", false,
            red, null, null, null);
    red.right = yellow;
    green = new Cell(0, 1, "green", false,
            null, null, red, null);
    red.bottom = green;
    orange = new Cell(1, 1, "orange", false,
            green, null, yellow, null);
    yellow.bottom = orange;
    green.right = orange;
    magenta = new Cell(2, 0, "magenta", false,
            yellow, null, null, null);
    yellow.right = magenta;
    cyan = new Cell(0, 2, "cyan", false,
            null, null, green, null);
    green.bottom = cyan;
    blue = new Cell(2, 1, "blue", false,
            orange, null, magenta, null);
    orange.right = blue;
    magenta.bottom = blue;
    pink = new Cell(1, 2, "pink", false,
            cyan, null, orange, null);
    orange.bottom = pink;
    cyan.right = pink;
    red2 = new Cell(2, 2, "red", true,
            pink, null, blue, null);
    blue.bottom = red2;
    pink.right = red2;

    // Creates an example list of colors with a given colors input
    exampleColors = 3;
    exampleLoColors = new ArrayList<String>();
    exampleColorChoices = new ArrayList<String>(
            Arrays.asList("red", "yellow", "green", "orange", "magenta", "cyan", "blue", "pink"));
    for (int i = 0; i < exampleColors; i++) {
      exampleLoColors.add(exampleColorChoices.get(i));
    }

    // Creates an Example Flood It game with a given size input
    exampleSize = 3;
    exampleFloodIt = new FloodItWorld();
    exampleFloodIt.createCells(exampleSize);
    exampleFloodIt.setAdjacent();
  }

  // EFFECT: Tests the image method
  void testImage(Tester t) {
    init();
    // Checks that each Cell is converted to the correct display image
    t.checkExpect(red.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED));
    t.checkExpect(yellow.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW));
    t.checkExpect(green.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN));
    t.checkExpect(orange.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.ORANGE));
    t.checkExpect(magenta.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA));
    t.checkExpect(cyan.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN));
    t.checkExpect(blue.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE));
    t.checkExpect(pink.image(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK));
  }

  // EFFECT: Tests the adjacentFlooded method
  void testAdjacentFlooded(Tester t) {
    init();
    // Checks to see that the correct Cells are flooded after adjacentFlooded is run
    t.checkExpect(red.right.flooded, false);
    t.checkExpect(red.bottom.flooded, false);
    red.adjacentFlooded("yellow");
    t.checkExpect(magenta.left.flooded, true);
    t.checkExpect(magenta.bottom.flooded, false);
    magenta.adjacentFlooded("blue");
    t.checkExpect(red2.left.flooded, false);
    t.checkExpect(red2.top.flooded, true);
    red2.adjacentFlooded("pink");
    t.checkExpect(cyan.right.flooded, true);
    t.checkExpect(cyan.top.flooded, false);
    cyan.adjacentFlooded("green");
    // At this point, all cells surrounding the center should be flooded
    t.checkExpect(orange.left.flooded, true);
    t.checkExpect(orange.right.flooded, true);
    t.checkExpect(orange.top.flooded, true);
    t.checkExpect(orange.bottom.flooded, true);
  }

  // EFFECT: Tests createCells method
  void testCreateCells(Tester t) {
    init();
    // Checks that the correct Cells are flooded and that each is in an applicable spot
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, true);
    for (int i = 0; i < exampleFloodIt.grid.size(); i++) {
      Cell cell = exampleFloodIt.grid.get(i);
      t.checkRange(cell.x, 0, exampleSize);
      t.checkRange(cell.y, 0, exampleSize);
      t.checkExpect(exampleLoColors.contains(cell.color), true);
    }
    for (int i = 1; i < exampleSize; i++) {
      t.checkExpect(exampleFloodIt.grid.get(i).flooded, false);
    }
  }

  // EFFECT: Tests the setAdjacent method
  void testSetAdjacent(Tester t) {
    init();
    // Checks that each Cell has its adjacent Cells set correctly
    for (int i = 0; i < exampleFloodIt.grid.size(); i++) {
      Cell cell = exampleFloodIt.grid.get(i);
      if (cell.x > 0) {
        t.checkExpect(cell.left, exampleFloodIt.grid.get(i - exampleSize));
      }
      if (cell.x < exampleSize - 1) {
        t.checkExpect(cell.right, exampleFloodIt.grid.get(i + exampleSize));
      }
      if (cell.y > 0) {
        t.checkExpect(cell.top, exampleFloodIt.grid.get(i - 1));
      }
      if (cell.y < exampleSize - 1) {
        t.checkExpect(cell.bottom, exampleFloodIt.grid.get(i + 1));
      }
    }
  }

  // EFFECT: Tests the makeScene Method
  void testMakeScene(Tester t) {
    init();
    // Manually recreates each aspect of makeScene with the given inputs
    // and confirms that it mirrors the automated makeScene method
    WorldScene game = new WorldScene(600, 750);
    game.placeImageXY(new RectangleImage(600, 750, OutlineMode.SOLID, Color.GRAY), 300, 375);
    game.placeImageXY(new EllipseImage(350, 80, OutlineMode.SOLID, Color.WHITE), 300,
            (330 - 10 * exampleSize) / 2);
    game.placeImageXY(new TextImage("Flood It", 60, FontStyle.BOLD, Color.BLACK), 300,
            (330 - 10 * exampleSize) / 2);
    game.placeImageXY(
            new RectangleImage((exampleSize + 2) * 20, (exampleSize + 2) * 20, OutlineMode.SOLID,
                    Color.WHITE), 300, 350);
    game.placeImageXY(
            new RectangleImage((exampleSize + 1) * 20, (exampleSize + 1) * 20, OutlineMode.SOLID,
                    Color.BLACK), 300, 350);
    for (Cell cell : exampleFloodIt.grid) {
      game.placeImageXY(cell.image(), (310 - 10 * exampleSize) + 20 * cell.x,
              (360 - 10 * exampleSize) + 20 * cell.y);
    }
    if (exampleFloodIt.displayTime == 3000) {
      exampleFloodIt.minutes++;
      exampleFloodIt.displayTime = 0;
    }
    TextImage timeDisplay = new TextImage("", 30, Color.BLACK);
    if (exampleFloodIt.minutes == 1 && exampleFloodIt.displayTime / 50 == 1) {
      timeDisplay = new TextImage(
              "Time: " + exampleFloodIt.minutes + " minute, " + exampleFloodIt.displayTime / 50
                      + " second", 30, Color.BLACK);
    }
    else if (exampleFloodIt.minutes == 1) {
      timeDisplay = new TextImage(
              "Time: " + exampleFloodIt.minutes + " minute, " + exampleFloodIt.displayTime / 50
                      + " seconds", 30, Color.BLACK);
    }
    else if (exampleFloodIt.displayTime / 50 == 1) {
      timeDisplay = new TextImage(
              "Time: " + exampleFloodIt.minutes + " minutes, " + exampleFloodIt.displayTime / 50
                      + " second", 30, Color.BLACK);
    }
    else {
      timeDisplay = new TextImage(
              "Time: " + exampleFloodIt.minutes + " minutes, " + exampleFloodIt.displayTime / 50
                      + " seconds", 30, Color.BLACK);
    }
    WorldImage info = new OverlayOffsetImage(
            new TextImage(
                    "Turns: " + Integer.toString(exampleFloodIt.turns) + " / "
                            + Integer.toString(exampleFloodIt.limit), 30, Color.BLACK), 0, 35,
            new OverlayOffsetImage(timeDisplay, 0, -5,
                    new OverlayOffsetImage(
                            new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK),
                            0, -40, new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE))));
    game.placeImageXY(info, 300, 750 - ((750 - (370 + 10 * exampleSize)) / 2));
    WorldImage win = new OverlayOffsetImage(
            new TextImage("You Win!", 60, FontStyle.BOLD, Color.GREEN), 0, 15,
            new OverlayOffsetImage(
                    new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK),
                    0, -40, new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));
    WorldImage loss = new OverlayOffsetImage(
            new TextImage("Game Over", 60, FontStyle.BOLD, Color.RED), 0, 15,
            new OverlayOffsetImage(
                    new TextImage("(Press the 'r' key to restart the game)", 15, Color.BLACK),
                    0, -40, new RectangleImage(480, 125, OutlineMode.SOLID, Color.WHITE)));
    if (exampleFloodIt.turns <= exampleFloodIt.limit &&
            exampleFloodIt.time >= exampleFloodIt.turnEnd && exampleFloodIt.allFlooded()) {
      game.placeImageXY(win, 300, 750 - ((750 - (370 + 10 * exampleSize)) / 2));
    }
    if (exampleFloodIt.turns >= exampleFloodIt.limit &&
            exampleFloodIt.time >= exampleFloodIt.turnEnd && !exampleFloodIt.allFlooded()) {
      game.placeImageXY(loss, 300, 750 - ((750 - (370 + 10 * exampleSize)) / 2));
    }
    t.checkExpect(exampleFloodIt.makeScene(), game);
  }

  // EFFECT: Tests the allFlooded method
  void testAllFlooded(Tester t) {
    init();
    // Checks whether or not all Cells are the same color
    // Does not check binary flooded status because when floodGrid is inactive,
    // all Cells except the top left corner are registered as not flooded
    t.checkExpect(exampleFloodIt.allFlooded(), false);
    for (Cell cell : exampleFloodIt.grid) {
      cell.color = exampleFloodIt.post;
    }
    t.checkExpect(exampleFloodIt.allFlooded(), true);
  }

  // EFFECT: Tests the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    init();
    // Creates a new alternate Flood It game with its own grid
    FloodItWorld testResetFloodIt = new FloodItWorld();
    ArrayList<Cell> testResetGrid = new ArrayList<Cell>();
    testResetFloodIt.grid = testResetGrid;
    // Checks that the intended reset key correctly creates a new game with the default
    // Flood It game previously initialized
    t.checkExpect(testResetFloodIt.size == 3, true);
    t.checkExpect(testResetFloodIt.colors == 3, true);
    t.checkExpect(testResetFloodIt.grid == testResetGrid, true);
    testResetFloodIt.onKeyEvent("a");
    t.checkExpect(testResetFloodIt.size == 3, true);
    t.checkExpect(testResetFloodIt.colors == 3, true);
    t.checkExpect(testResetFloodIt.grid == testResetGrid, true);
    testResetFloodIt.onKeyEvent("r");
    t.checkExpect(testResetFloodIt.size == 3, true);
    t.checkExpect(testResetFloodIt.colors == 3, true);
    t.checkExpect(testResetFloodIt.grid == testResetGrid, false);
  }

  // EFFECT: Tests the onMouseClicked method
  void testOnMouseClicked(Tester t) {
    init();
    // Manually alters conditions in a favorable manner for testing and then
    // tests these conditions
    exampleFloodIt.grid.get(0).color = "red";
    exampleFloodIt.post = "red";
    exampleFloodIt.grid.get(1).color = "yellow";
    exampleFloodIt.prev = "yellow";
    exampleFloodIt.grid.get(4).color = "green";
    exampleFloodIt.grid.get(0).flooded = false;
    exampleFloodIt.turns = 0;
    exampleFloodIt.time = 0;
    exampleFloodIt.turnEnd = 0;
    Posn grid4Posn = new Posn(300, 350);
    t.checkExpect(exampleFloodIt.post, "red");
    t.checkExpect(exampleFloodIt.prev, "yellow");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.turns, 0);
    t.checkExpect(exampleFloodIt.turnEnd, 0);
    exampleFloodIt.grid.get(2).flooded = true;
    // Checks that the method doesn't work when a non-top-left-corner Cell is flooded
    // to replicate a null click when the waterfall animation is active
    exampleFloodIt.onMouseClicked(grid4Posn);
    t.checkExpect(exampleFloodIt.post, "red");
    t.checkExpect(exampleFloodIt.prev, "yellow");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.turns, 0);
    t.checkExpect(exampleFloodIt.turnEnd, 0);
    exampleFloodIt.grid.get(2).flooded = false;
    exampleFloodIt.grid.get(4).color = "red";
    // Checks that the method doesn't work when the clicked upon Cell is the same color
    // as the Cell currently in the top left corner
    exampleFloodIt.onMouseClicked(grid4Posn);
    t.checkExpect(exampleFloodIt.post, "red");
    t.checkExpect(exampleFloodIt.prev, "yellow");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.turns, 0);
    t.checkExpect(exampleFloodIt.turnEnd, 0);
    exampleFloodIt.grid.get(4).color = "green";
    // Checks that the method does work when the required conditions are met
    exampleFloodIt.onMouseClicked(grid4Posn);
    t.checkExpect(exampleFloodIt.post, exampleFloodIt.clickedCell(grid4Posn).color);
    t.checkExpect(exampleFloodIt.prev, "red");
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, true);
    t.checkExpect(exampleFloodIt.turns, 1);
    t.checkExpect(exampleFloodIt.turnEnd, exampleSize * 2);
  }

  // EFFECT: Tests the clickedCell method
  void testClickedCell(Tester t) {
    init();
    // Checks that the clicked position corresponds to the correct Cell or lack thereof
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(300, 350)), exampleFloodIt.grid.get(4));
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(320, 350)), exampleFloodIt.grid.get(7));
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(300, 370)), exampleFloodIt.grid.get(5));
    t.checkExpect(exampleFloodIt.clickedCell(new Posn(300, 390)), null);
  }

  // EFFECT: Tests the floodGrid method
  void testfloodGrid(Tester t) {
    init();
    // Manually alters conditions in a favorable manner for testing and then
    // tests these conditions
    exampleFloodIt.grid.get(0).color = "red";
    exampleFloodIt.post = "red";
    exampleFloodIt.grid.get(1).color = "yellow";
    exampleFloodIt.prev = "yellow";
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, true);
    t.checkExpect(exampleFloodIt.grid.get(1).flooded, false);
    t.checkExpect(exampleFloodIt.grid.get(0).color, "red");
    t.checkExpect(exampleFloodIt.grid.get(1).color, "yellow");
    // Checks that the correct flooded Cell changes color upon running the method
    exampleFloodIt.floodGrid();
    t.checkExpect(exampleFloodIt.grid.get(0).flooded, false);
    t.checkExpect(exampleFloodIt.grid.get(1).flooded, true);
    t.checkExpect(exampleFloodIt.grid.get(0).color, "red");
    t.checkExpect(exampleFloodIt.grid.get(1).color, "red");
  }

  // EFFECT: Tests the initGame method
  void testInitGame(Tester t) {
    FloodItWorld game = new FloodItWorld(22, 6);
    // Runs a game using the given inputs [size, colors] within fixed bounds
    game.initGame(22, 6);
    // Checks to make sure illegal arguments don't pass through
    t.checkException(
            new IllegalArgumentException("Grid dimensions must be at least 2x2"),
            game, "initGame", 1, 6);
    t.checkException(
            new IllegalArgumentException("Grid dimensions may not exceed 22x22"),
            game, "initGame", 23, 6);
    t.checkException(
            new IllegalArgumentException("Grid must contain at least 3 colors"),
            game, "initGame", 18, 2);
    t.checkException(
            new IllegalArgumentException("Grid may not exceed 8 colors"),
            game, "initGame", 18, 9);
    // Tests that the initial inputs are correctly interpreted
    t.checkExpect(game.size, 22);
    t.checkExpect(game.colors, 8);
    t.checkExpect(game.limit, (int) (22 * 8 * 0.3));
    t.checkExpect(game.turns, 0);
    t.checkExpect(game.prev, "");
    t.checkExpect(game.post, game.grid.get(0).color);
    t.checkExpect(game.time, 0);
    t.checkExpect(game.displayTime, 0);
    t.checkExpect(game.minutes, 0);
    t.checkExpect(game.turnEnd, 0);
  }
}
