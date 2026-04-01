package BTTH2;

import java.awt.Image;

// 2. Them doi tuong bird
public class Bird {
    int x;
    int y;
    int width;
    int height;
    Image img;

    public Bird(Image img, int x, int y, int width, int height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}