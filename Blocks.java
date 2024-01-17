import javax.swing.*;
import java.awt.*;

public class Blocks {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Image texture;
    
    private Rectangle bounds;

    public Blocks(int startX, int startY, int endX, int endY, String image){
        this.startX = startX * 64;
        this.startY = startY * 64;
        this.endX = endX * 64;
        this.endY = endY * 64;
        texture = new ImageIcon(image).getImage();

        bounds = new Rectangle(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY);
    }

    public int getStartX(){
        return startX;
    }
    public int getStartY(){
        return startY;
    }
    public int getEndX(){
        return endX;
    }
    public int getEndY(){
        return endY;
    }
    public Image getTexture(){
        return texture;
    }
    public Rectangle getBounds(){
        return bounds;
    }

    //customs
    public int getWidth(){
        return endX - startX;
    }
    public int getHeight(){
        return endY - startY;
    }
}
