import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Madeline{
  private Image sprite;
  private Rectangle bounds;

  private int spriteX;
  private int spriteY;
  
  private double yVelocity;
  private double xVelocity;
  
  private boolean groundContact;
  private boolean rightContact;
  private boolean leftContact;

  private boolean canBoost;
  private int boostTicks;
  private static int boostTicksReset = 5;
  private boolean inAir; //air drift is not as fast as moving on ground
  
  private int horizDirection; //-1 for left, 0 for neutral, 1 for right
  private int lastHorizDirection;
  private int vertDirection; //-1 is down, 0 is neutral, 1 is up

  private static int inAirTicks;

  private final static int width = 46; //amount of pixels from left of sprite to right of sprite
  private final static int height = 64;

  private final int CANVASWIDTH = 960; //manually set variables
  private final int CANVASHEIGHT = 768;

  public Madeline(){
    spriteX = 64;
    spriteY = 640;
    xVelocity = 0;
    yVelocity = 0;

    bounds = new Rectangle(spriteX, spriteY, width, height);

    sprite = new ImageIcon("madeline.png").getImage();
    
    groundContact = false;
    rightContact = false;
    leftContact = false;

    canBoost = true;
    
    horizDirection = 1;
    lastHorizDirection = 1;
    vertDirection = 0;
    

  } //end of constructor

  

  public void update(ArrayList<Blocks> blockList){

    if(inAir) inAirTicks++;
    if(inAir && xVelocity != 0 && inAirTicks % 2 == 0){
      if(xVelocity > 0){
        xVelocity -= 1;
      } else {
        xVelocity += 1;
      }
    }
    
    if(!groundContact){ //determines gravity and goost resets
      gravity();
      inAir = true;
    } else if(boostTicks >= 5){ //on ground, after 5 ticks, boost reset
      canBoost = true;
      changeSprite("madeline.png");
      inAir = false;
    }
    if(!canBoost) { //madeline doesn't have boost
      changeSprite("madelineNoBoost.png"); //blue hair
    }
    
    if(xVelocity > 0){
      horizDirection = 1;
    } else if(xVelocity < 0){
      horizDirection = -1;
    } else{
      horizDirection = 0;
    }

    if(lastHorizDirection != horizDirection && horizDirection != 0){
      lastHorizDirection = horizDirection;
    }

    changePosition(); //changes spriteX and spriteY

    groundContact = false; //resets contacts to recheck
    leftContact = false;

    boolean tempGroundContact = false;
    boolean tempRightContact = false;

    for(Blocks block : blockList){
      checkCollision(block);
      if(groundContact){
        tempGroundContact = true;
      }
      if(rightContact){ //unused
        tempRightContact = true;
      }
    }
    groundContact = tempGroundContact;
    inAir = !tempGroundContact;

    rightContact = tempRightContact;
    
    boostTicks++;
    
  } //end of update()

  public void changePosition(){ //updates postions using velocities, bounds control
    spriteX += xVelocity;
    spriteY += yVelocity;
    
    if(spriteX >= CANVASWIDTH - width){ //bounds control
      spriteX = CANVASWIDTH - width;
    } else if(spriteX <= 0){
      spriteX = 0;
    }
    if(spriteY >= CANVASHEIGHT - height){
      spriteY = CANVASHEIGHT - height;
    }
    bounds = new Rectangle(spriteX, spriteY, width, height + 1);
  }

  public void gravity(){
    if(!(yVelocity >= 16)){
      yVelocity += 2;
    } else {
      yVelocity = 32;
    }
  }

  //movement methods
  public void jump(){
    if(groundContact){
      yVelocity -= 24;
      groundContact = false;
    }
    
  }


  public void checkCollision(Blocks block){ //checks vertical collision
    
    //do small editing/adjusting to bounds =========================================================================================================

    if(this.bounds.intersects(block.getBounds())){ 
      //horizontal check
      if((spriteX + 0.75 * width >= block.getStartX() && spriteX + 0.75 * width <= block.getEndX()) && (spriteY + height/2  >= block.getStartY() && spriteY + height/2  <= block.getEndY())){
        rightContact = true;     
        xVelocity = 0;                                         
        
        spriteX = block.getStartX() - width;
      }
      else if((spriteX <= block.getEndX() && spriteX >= block.getStartX()) && (spriteY + height/2  >= block.getStartY() && spriteY + height/2  <= block.getEndY())){
        leftContact = true;
        xVelocity = 0;
        spriteX = block.getEndX();
      }

      //vertical check
      if((spriteY + height  >= block.getStartY() && spriteY + height  <= block.getEndY()) && (spriteX + 0.75 * width >= block.getStartX() && spriteX + 0.25 * width<= block.getEndX())){ //ground detection(down)
        groundContact = true;
        spriteY = block.getStartY() - height;
        yVelocity = 0;
       
        
      } 
      else if((spriteY <= block.getEndY() && spriteY >= block.getStartY()) && (spriteX + 0.75 * width >= block.getStartX() && spriteX + 0.25 * width <= block.getEndX())){
        yVelocity = 0;
        spriteY = block.getEndY();

      } 
    }
  }

  public void boost(){
    if(canBoost){
      System.out.println("boosted");
      yVelocity = 0;
      xVelocity = 0;
      
      
      if(horizDirection == 0 && vertDirection == 1){ //if only 'w' is pressed it doesn't boost in a horizontal direction
        yVelocity -= 24;
      } else if(horizDirection == 0 && vertDirection == -1){// 'd' only
        yVelocity += 24;
      } else {
        if(lastHorizDirection == -1){ 
          xVelocity -= 24;
        } else if(lastHorizDirection == 1){
          xVelocity += 24;
        }
        if(vertDirection == -1){
          yVelocity += 24;
        } else if (vertDirection == 1){
          yVelocity -= 24;
        }
        
      }
      
      System.out.println(vertDirection);
      

      canBoost = false;
    }
  }

  //mutators---------------------------------------------------------------------
    //changing states
  public void changeGroundContact(boolean tof){
    groundContact = tof;
  }
  public void changeCanBoost(boolean tof){
    canBoost = tof;
  }
  public void changeVertDirection(int num){
    vertDirection = num;
  }
  public void changeHorizDirection(int num){
    horizDirection = num;
  }
  public void resetVertDirection(){
    vertDirection = 0;
  }
  public void resetHorizDirection(){
    horizDirection = 0;
  }
  
    //changing positions
  public void setY(int num){
    spriteY = num;
  }
  
    //changing velocity
  public void addXVelo(int num){
    xVelocity += num;
  }

  public void setXVelo(int num){
    if(xVelocity != num){
      
    }
    xVelocity = num;
  }

  public void addXVeloMod(int num){ //change ================================================
    if(inAir){
      xVelocity += num/2;
    } else {
      xVelocity += num;
    }
  }

  public void addYVelo(int num){
    yVelocity += num;
    
  }
  public void setYVelo(int num){
    yVelocity = num;
  }
  
    //changing sprite
  public void changeSprite (String str){
    sprite = new ImageIcon(str).getImage();
  }

  //accessors ----------------------------------------------------------------------
  public int getX(){
    return spriteX;
  }
  public int getY(){
    return spriteY;
  }
  public double getXVelo(){
    return xVelocity;
  }
  public double getYVelo(){
    return yVelocity;
  }
  public Image getSprite(){
    return sprite;
  }
  public boolean getGroundContact(){
    return groundContact;
  }
  public boolean getCanBoost(){
    return canBoost;
  }
  public boolean getFacingDirection(){ //returns false for left true for right
    if(lastHorizDirection == -1){
      return false;
    } else {
      return true;
    } 
  }
  public int getVertDirection(){
    return vertDirection;
  }
  public int getWidth(){
    return width;
  }
  public int getHeight(){
    return height;
  }
  public Rectangle getBounds(){
    return bounds;
  }
  
  
}

