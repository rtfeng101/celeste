import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Canvas extends JComponent{
  private String inputs;

  public static final String ANSI_CYAN = "\u001B[36m";
  private final int WIDTH;
  private final int HEIGHT;

  private Madeline madeline;

  private Blocks platform;
  private Blocks block1;
  private Blocks block2;
  private Blocks block3;
  private Blocks block4;
  private Blocks block5;
  private Blocks block6;
  private Blocks block7;
  private Blocks block8;
  private Blocks block9;

  private ArrayList<Blocks> blockList;

  private Strawberry straw1;
  private Strawberry straw2;
  private Strawberry straw3;
  private ArrayList<Strawberry> strawberryList;
  private int strawCount;
  

  private ArrayList<String> directions;
  private int directionsIndex; //turns madeline
  private boolean directionsRun;

  private int ticks;
  private static int maxTicks = 5000;
  
  private Timer t;

  public Canvas(int width, int height){ //start of constructor
    WIDTH = width;
    HEIGHT = height;
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    directionsRun = true;
    madeline =  new Madeline();
    
    //blocks
    platform = new Blocks(0, 11, 15, 12, "snowBlockLargePlatformMod.png");
    block1 = new Blocks(10, 10, 11, 11, "snowBlock1x1.png");
    block2 = new Blocks( 6, 8, 7, 9, "snowBlock1x1.png");
    block3 = new Blocks( 12, 3, 14, 5, "snowBlock2x2.png");

    block4 = new Blocks(0, 6, 1, 7, "snowBlock1x1.png");
    block6 = new Blocks(1, 6, 2, 7, "snowBlock1x1.png");

    block5 = new Blocks(4, 4, 6, 6, "snowBlock2x2.png");

    block7 = new Blocks(8, 2, 9, 3, "snowBlock1x1.png");
    block9 = new Blocks(9, 2, 10, 3, "snowBlock1x1.png");

    block8 = new Blocks(14, 10, 15, 11, "snowBlock1x1.png");

    blockList = new ArrayList<Blocks>();
    blockList.add(platform);
    blockList.add(block1);
    blockList.add(block2);
    blockList.add(block3);
    blockList.add(block4);
    blockList.add(block5);
    blockList.add(block6);
    blockList.add(block7);
    blockList.add(block8);
    blockList.add(block9);

    //strawberries
    straw1 = new Strawberry(blockList);
    straw2 = new Strawberry(blockList);
    straw3 = new Strawberry(blockList);

    strawberryList = new ArrayList<Strawberry>();
    strawberryList.add(straw1);
    strawberryList.add(straw2);
    strawberryList.add(straw3);

    directions = new ArrayList<String>();
    directionsIndex = 0;

    //Directions

    // directions.add("Welcome to Celeste! (Press Space to continue)");
    // directions.add("The goal is to help Madeline collect as many strawberries as possible.");
    // directions.add("You will help Madeline manuever throughout the map.");
    // directions.add("Use the 'a' and 'd' keys to walk left and right.");
    // directions.add("On the ground, use 'j' to jump.");
    // directions.add("Use 'k' to boost. Your 'wasd' keys become directional keys.");
    // directions.add("disclaimer: block physics go wack if you hold down boost def not my fault");
    // directions.add("Once you help Madeline find a strawberry, it will respawn shortly.");
    // directions.add("There will be a maximum of three strawberries on screen.");
    // directions.add("Madeline's strawberries are displayed in the top left.");
    // directions.add("A timer will be displayed below the strawberries.");
    // directions.add("Once the timer hits 0, the game is over.");
    // directions.add("Good luck!");
    // directions.add("Press Space twice to begin. Press Space during a game to reset.");

    ticks = maxTicks + 1;

    class TimerListener implements ActionListener{ //timer, contains the important update() functions, runs game
      public void actionPerformed(ActionEvent e){

        if(ticks <= maxTicks && !directionsRun){
          madeline.update(blockList); //updates Madeline all in 1 function

          for(int i = 0 ; i < strawberryList.size(); i++){

            if(strawberryList.get(i).getSingleCollect()){
              strawCount++;
            }

            if(strawberryList.get(i).update(madeline)){ //checks if respawn timer is over
              strawberryList.set(i, new Strawberry(blockList));// if it is over, make a new berry
            }
            ticks++;
          }
        }
        //System.out.println(ticks);
        repaint();
      }
    }
    t = new Timer(20, new TimerListener()); //1 second = 50 ticks
    
    class CanvasKeyListener implements KeyListener{
      
      public void keyPressed(KeyEvent e){
        if(e.getKeyChar() == 'd'){ //right
          madeline.setXVelo(12);
          inputs += "d";
        }
        if(e.getKeyChar() == 'a'){ //left
          madeline.setXVelo(-12);
          inputs += "a";
        }
        if(e.getKeyChar() == 'w'){
          madeline.changeVertDirection(1);
          inputs += "w";
        }
        if(e.getKeyChar() == 's'){ //down
          madeline.changeVertDirection(-1);
          inputs += "s";
        }
        if(e.getKeyChar() == 'j'){ //jump
          madeline.jump();
          inputs += "j";
        }
        if(e.getKeyChar() == 'k'){ //boost
          madeline.boost();
          inputs += "k";
        }


        if(e.getKeyChar() == ' '){ //resets madeline?
          if(directionsIndex < directions.size())
          directionsIndex++;
          else {
            reset();
          }
        }
        repaint();
      }

      public void keyReleased(KeyEvent e){
        
        if(e.getKeyChar() == 'w' || e.getKeyChar() == 's'){
          madeline.resetVertDirection();
          madeline.setXVelo(0);
          
        }
        if(e.getKeyChar() == 'a' || e.getKeyChar() == 'd'){
          madeline.resetHorizDirection();
          madeline.setXVelo(0);
        }
      }
      public void keyTyped(KeyEvent e){}
    }
    addKeyListener(new CanvasKeyListener());

    setFocusable(true);
    requestFocus();
    
    t.start();
  }

  public void paintComponent(Graphics gr){
    Graphics2D g = (Graphics2D) gr;
    g.setFont( new Font("TimesRoman", Font.PLAIN, 30));

    g.drawImage(new ImageIcon("background.jpg").getImage(), -150, 0, this);
    
    g.setColor(Color.RED);
    g.drawString(" " + strawCount, 30, 40);

    g.setColor(Color.MAGENTA);
    if(ticks <= maxTicks + 10){
      g.drawString("Time: " + (maxTicks/100 - ticks / 100), 10, 80);
    }

    if(madeline.getFacingDirection()){ //flips madeline based on the direction she is facing
      g.drawImage(madeline.getSprite(), madeline.getX(), madeline.getY(), madeline.getWidth(), madeline.getHeight(), this);
    } else {
      g.drawImage(madeline.getSprite(), madeline.getX() + 46, madeline.getY(), - madeline.getWidth(), madeline.getHeight(), this);
    }

    for(Blocks block: blockList){ //prints out all blocks
      g.drawImage(block.getTexture(), block.getStartX(), block.getStartY(), this);
    }
    for(Strawberry straw : strawberryList){ //prints out all strawberries
      g.drawImage(straw.getSprite(), straw.getStartX(), straw.getStartY(), this);
      g.drawImage(new ImageIcon("strawberryIcon.gif").getImage(), 10, 10, 20, 26, this);
    }

    g.setColor(Color.CYAN); //prints directions
    if(directionsIndex < directions.size() && directionsRun){
      drawCenteredString(g, directions.get(directionsIndex), new Rectangle (0, 200, WIDTH, HEIGHT), new Font("TimesRoman", Font.PLAIN, 30));
    } else {
      directionsRun = false;
    }
  } //end of paint

  public int getCanvasWidth(){
    return WIDTH;
  }
  public int getCanvasHeight(){
    return HEIGHT;
  }

  //yoinked off the internet on stack overflow
  public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
    // Get the FontMetrics
    FontMetrics metrics = g.getFontMetrics(font);
    // Determine the X coordinate for the text
    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
    // Set the font
    g.setFont(font);
    // Draw the String
    g.drawString(text, x, y);
  }

  public void reset(){
    strawCount = 0;
    ticks = 0;
    madeline = new Madeline();
    System.out.println("reset");
  }

  
  
}