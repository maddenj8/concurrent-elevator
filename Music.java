
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music implements Runnable {
		  public  void run(){
				try {
					 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("music.wav"));
					 Clip clip = AudioSystem.getClip();
					 clip.open(audioInputStream);
					 clip.start();
					 clip.loop(Clip.LOOP_CONTINUOUSLY);
				} 
				catch (Exception ex) {
					 ex.printStackTrace();
				}
		 }	
		  public  void ding(){
				try {
					 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("ding.wav"));
					 Clip clip = AudioSystem.getClip();
					 clip.open(audioInputStream);
					 clip.start();
				} 
				catch (Exception ex) {
					 ex.printStackTrace();
				}
		 }	
}
