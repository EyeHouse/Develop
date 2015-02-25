import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.awt.Window;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageHandler extends JPanel {
	
	//String sourceFile;
	float xStart;
	float yStart;
	float scale;
	float duration;
	float startTime;
	BufferedImage image;
	
	public ImageHandler() {
		
	    setBackground(Color.white);
	    
		try {                
	          image = ImageIO.read(new File("crazy.png"));
	       } catch (IOException ex) {
	    	   System.out.println("there has been an error");
	       } 
		
		repaint();
		makeFrame();
	}

	public void paintComponent(Graphics g){
		int x = 100;
		int y = 100; 	
		g.drawImage(image, x, y, null); // see javadoc for more info on the parameters  
	}
	
	 public void makeFrame() {
	        
        // Instantiate a window frame using Swing class JFrame
        JFrame frame = new JFrame("ShapeManipulation");

        // When the window is closed terminate the application
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Set initial size of window and prevent resizing
        frame.setSize(1000, 700);
        frame.setResizable(false);
        
        // Add the current object to the centre of the frame and make visible
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setBackground(Color.WHITE);
        frame.setVisible(true);
	}   	 
	 
	public static void main(String[] properties) {
		new ImageHandler();
	}	
}
