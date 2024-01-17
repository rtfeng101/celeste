import javax.swing.*;

public class Main{

  public static void main(String[] args){
    JFrame frame = new JFrame("Celeste");

    JLabel output = new JLabel("poggers");
    
    
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Canvas canvas = new Canvas(960, 768);
    
    frame.add(output);
    frame.add(canvas);
    
    
    frame.pack();
    frame.setVisible(true);


    SwingUtilities.updateComponentTreeUI(frame);
    
  }

}
