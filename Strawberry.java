import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Strawberry {
    private int startX;
    private int startY;

    private boolean collected;
    private boolean pastColleted;

    private static int width = 40;
    private static int height = 52;

    private boolean attatched;

    private Image sprite;
    private int sx;
    private static int sy = 0;

    private final static Image[] strawberryCollectArray = new Image[20];
    
    private Rectangle bounds;

    private int ticks;

    private int respawnTicks; //counts how may ticks between it going off screen and popping up again, max respawn tick lies in Canvas
    private final static int RESPAWNMAXTICKS = 100;
    
    public Strawberry(ArrayList<Blocks> blockList){ //startX and startY in blocks of 64, strawberries center themselves based off of the 64-grid
        for(int i = 0; i < strawberryCollectArray.length; i++){
            String str = "";
            if(i <= 9){
                str = "sprite_strawberryCollect0" + i + ".png";
            } else {
                str = "sprite_strawberryCollect" + i + ".png";
            }
            strawberryCollectArray[i] = new ImageIcon(str).getImage();
        }
        attatched = false;
        sprite = new ImageIcon("strawberry.gif").getImage();
        ticks = 0;
        collected = false;
        respawnTicks = 0;

        randomLocation();

        for(int i = 0; i < blockList.size(); i++){
            while(this.bounds.intersects(blockList.get(i).getBounds())){ //if strawbery spawns in a block, change spawn
                randomLocation();
            } 
        }
    }

    public boolean update(Madeline madeline){ //returns if the strawberry has been collected or not, update method for strawberry
        pastColleted = collected;
        if(!attatched){
            touching(madeline.getBounds());
        }

        if(attatched && !collected){
            startX = (int) (madeline.getX() - madeline.getXVelo());
            startY = (int) (madeline.getY() - madeline.getYVelo());
            if(madeline.getGroundContact()){
                collected = true;
            }
        }
        if(collected){
            //ticks
            //if the # fsprites is 10 and wanted to do every 8 ticks, do sprite = list[tick/ 8%10]
            
            if(ticks < 80){
                sprite = strawberryCollectArray[ticks / 4 % 20];
                ticks++;
            } else {
                sprite = new ImageIcon("").getImage();
                respawnTicks++;
            }
        }
        return respawnTicks >= RESPAWNMAXTICKS;
    }

    public void touching (Rectangle rectangle){
        attatched = this.bounds.intersects(rectangle);
    }

    public void randomLocation(){
        this.startX = (int)(Math.random() * 15) * 64 + 12; //change bounds ===========================================================================
        this.startY = (int)(Math.random() * 12) * 64 + 2;

        bounds = new Rectangle(startX, startY, width, height);
    }

    public boolean getSingleCollect(){
        return pastColleted != collected;
    }

    public int getStartX(){
        return startX;
    }
    public int getStartY(){
        return startY;
    }
    public Image getSprite(){
        return sprite;
    }
    public Rectangle getBounds(){
        return bounds;
    }
    public boolean getCollected(){
        return collected;
    }
    public int getSX(){
        return sx;
    }
    public int getSY(){
        return sy;
    }
}
