
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Music implements Runnable {
    public  void run(){
		  try{
					 File file = new File("music.wav");
					 URL url = null;
					 if (file.canRead()) {url = file.toURI().toURL();}
					 System.out.println(url);
					 AudioClip clip = Applet.newAudioClip(url);
					 clip.play();
					 System.out.println("should've played by now");
	    	 }
			catch(Exception e ){
				
			}
	}
}
