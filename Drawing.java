import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;

public class Drawing extends JPanel implements Runnable{
public static BufferedImage building; 
public static BufferedImage elevator; 
public static BufferedImage openElevator; 
public static BufferedImage whitebox; 
private javax.swing.JTextArea textArea;
int elevOffsetY = 508; //always the answer to make elevator go up one story add 42
int elevOffsetX = 42; //a french sedan  
String  personGetOn = "";
String  personGetOff = "";
String  weight = "";
String  full = "";
  
    public Drawing(){
	    super(); 
	    try 
	    {                
	      building = ImageIO.read(new File("building.png")); 
	      elevator = ImageIO.read(new File("box.png")); 
	      openElevator = ImageIO.read(new File("emptyBox.png")); 
	      whitebox = ImageIO.read(new File("whitebox.png")); 
	    } 
	    catch (IOException e) 
	    { 
	    } 
		JFrame jf = new JFrame();
		jf.setSize(building.getWidth() + 480, building.getHeight() + 30); 
		jf.add(this);
		jf.setVisible(true);

    }



    @Override
    public void paintComponent(Graphics g) {
	    super.paintComponent(g);

	    Graphics2D g2 = (Graphics2D)g; //because otherwise there is horrific alisisisng 
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
	    g.drawImage(whitebox,elevOffsetX ,0 , null); 
	    g.drawImage(building, 0, 0, null); 
	    g.drawImage(elevator,elevOffsetX , elevOffsetY, null); 
	    Dimension d = this.getPreferredSize();
	    int fontSize = 12;
	    g2.setFont(new Font("Courier", Font.PLAIN, fontSize));
	    g2.setColor(Color.black);
	    g2.drawString(weight, 125, 440);
	    g2.drawString(personGetOn, 125, 460);
	    g2.drawString(personGetOff, 125, 480);
	    g2.drawString(full, 125, 500);

	    repaint(); 
	    g.dispose();
    }


    public void moveElevator(int direction ) {

       if( direction == 1){
		elevOffsetY -= 42;
 	 }
	else{
		elevOffsetY += 42;
	}
        repaint();	
	SwingUtilities.updateComponentTreeUI(this);

	}
    

   public static void main(String[] args) {
       new Drawing();
	  }

    @Override
    public void run() {
            repaint();
	    

        }
    

}


