import java.awt.*;
import java.io.Serializable;

class Shape implements Serializable{
    String type;
    int x1, y1, x2, y2;
    Color color;

    Shape(String type, int x1, int y1, int x2, int y2, Color color) {
        this.type = type;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "type='" + type + '\'' +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", color=" + color +
                '}';
    }

}