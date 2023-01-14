import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

// Represents a single Cell on the game grid
public class Cell {
  int x;
  int y;
  String color;
  Cell left;
  Cell right;
  Cell top;
  Cell bottom;
  boolean flooded;
  ArrayList<String> loColors = new ArrayList<String>();
  ArrayList<String> colorChoices = new ArrayList<String>(
          Arrays.asList("red", "yellow", "green", "orange", "magenta", "cyan", "blue", "pink"));

  // Standard constructor for starting a real game
  Cell(int x, int y, int colors, boolean flooded) {
    for (int i = 0; i < colors; i++) {
      loColors.add(colorChoices.get(i));
    }
    this.x = x;
    this.y = y;
    Random random = new Random();
    int randomColor = random.nextInt(loColors.size());
    this.color = loColors.get(randomColor);
    this.flooded = flooded;
  }

  // Testing constructor for a test example
  Cell(int x, int y, String color, boolean flooded,
       Cell left, Cell right, Cell top, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
  }

  // Creates the Cell image that is displayed on the game grid for each Cell color
  WorldImage image() {
    if (color.equals("red")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED);
    }
    else if (color.equals("yellow")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW);
    }
    else if (color.equals("green")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN);
    }
    else if (color.equals("orange")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.ORANGE);
    }
    else if (color.equals("magenta")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA);
    }
    else if (color.equals("cyan")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.CYAN);
    }
    else if (color.equals("blue")) {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE);
    }
    else {
      return new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK);
    }
  }

  // EFFECT: Mutates the flooded status of adjacent Cells based on their color
  void adjacentFlooded(String color) {
    if (left != null && left.color.equals(color)) {
      left.flooded = true;
    }
    if (right != null && right.color.equals(color)) {
      right.flooded = true;
    }
    if (top != null && top.color.equals(color)) {
      top.flooded = true;
    }
    if (bottom != null && bottom.color.equals(color)) {
      bottom.flooded = true;
    }
  }
}
