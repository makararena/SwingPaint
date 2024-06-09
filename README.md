# Simple Draw Editor

This is a simple drawing application built in Java Swing. It allows users to draw various shapes such as lines, circles, and squares on a canvas. The application supports features like saving and opening drawings, selecting colors, and clearing the canvas.

## Features

- **Drawing Tools**: Users can draw lines, circles, and squares on the canvas.
- **Color Selection**: Choose from a variety of colors to draw with.
- **Save and Open**: Save drawings to files and open previously saved drawings.
- **Clear Canvas**: Clear the canvas to start a new drawing.
- **Simple Interface**: Easy-to-use interface for drawing and interacting with the application.

## How to Run

1. Make sure you have Java installed on your system.
2. Compile the source files using `javac Main.java`.
3. Run the compiled program using `java Main`.

## Usage

- **Drawing**: Select the desired shape from the "Draw" menu and start drawing on the canvas.
- **Color Selection**: Choose a color from the "Draw" menu to change the drawing color.
- **Saving and Opening**: Use the "File" menu to save or open drawings.
- **Clear Canvas**: Click on "Clear" in the "Draw" menu to clear the canvas.

## Example

<img alt="Drawing Example" height="500" src="media/Screenshot 2024-06-09 at 14.49.47.png" width="800"/>

## Code Overview

### `SimpleDrawEditor.java`

This class is the main class of the application. It contains the GUI elements and handles user interactions. Features include drawing shapes, selecting colors, saving/opening drawings, and clearing the canvas.

### `DrawArea.java`

This class represents the drawing area where shapes are drawn. It handles mouse events for drawing shapes, clearing the canvas, and selecting and deleting shapes.

### `Shape.java`, `Line.java`, `Circle.java`, `Rectangle.java`

These classes represent the shapes that can be drawn on the canvas. They contain information about the shape's type, position, color, and methods for checking if a point is contained within the shape.

## Dependencies

This project has no external dependencies other than Java Swing, which is included in the Java Development Kit (JDK).
