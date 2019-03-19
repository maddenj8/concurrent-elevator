import javax.swing.*; 
import java.io.*; 
import java.awt.*; 
import java.awt.image.*; 
import javax.imageio.*; 

public class Drawing extends JPanel 
{ 
  public static BufferedImage building; 
  public static BufferedImage elevator; 
  int elevOffsetY = 508; //always the answer to make elevator go up one story add 42
  int elevOffsetX = 42; //a french sedan  
  int i = 0;
  
  public Drawing () 
  { 
    super(); 
    try 
    {                
      building = ImageIO.read(new File("building.png")); 
      elevator = ImageIO.read(new File("box.png")); 
    } 
    catch (IOException e) 
    { 
      //Not handled. 
    } 
  } 

  public void moveElevator(int direction) {
	if( direction == 1){
		elevOffsetY -= 42;
 	 }
	else{
		elevOffsetY += 42;
	}

  }
  

  public void paintComponent(Graphics g) 
  { 
    g.drawImage(building, 0, 0, null); 
    g.drawImage(elevator,elevOffsetX , elevOffsetY, null); 
    repaint(); 
	  while(i < 9 ){
		try{
		g.drawImage(elevator, elevOffsetX , elevOffsetY, null); 
		Thread.sleep(2000);
		moveElevator(1);
		//g.drawImage(building,0, 0, null); 
		//repaint(); 
		super.paintComponent(g);
		revalidate();
		i++;
		}
		catch (InterruptedException e){
		}
	    }
  } 



  public static void main(String [] args) throws InterruptedException
  { 
    JFrame f = new JFrame("Window"); 
    f.add(new Drawing()); 
    f.setSize(building.getWidth(), building.getHeight() + 30); 
    f.setVisible(true); 
  
  } 
}
