import java.awt.*;
import java.io.Serializable;


// https://stackoverflow.com/questions/4548816/when-should-we-implement-serializable-interface
public abstract class Shape implements Serializable {
    String type;
    int curX, curY;
    Color color;

    public Shape(String type, int curX, int curY, Color color) {
        this.type = type;
        this.curX = curX;
        this.curY = curY;
        this.color = color;
    }
    public abstract boolean contains(int x, int y);

    public int getCurY() {
        return curY;
    }

    public int getCurX() {
        return curX;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}