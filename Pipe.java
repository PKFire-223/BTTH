package BTTH2;

import java.awt.Image;

// 3. Them doi tuong Pipe
public class Pipe {
    int x;
    int y;
    int width;
    int height;
    Image img;
    boolean passed = false;

    public Pipe(Image img, int x, int y, int width, int height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}