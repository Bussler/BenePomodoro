package main.java.pomTestClass;
import java.applet.Applet;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Pomodoro extends JFrame implements KeyListener{
	
	public static int waitingTime=10;
	JTextField field;
	JLabel label;
	public static int stage=0;// 0 start, 1 idle, 2 expecting
	private static int counter=0;
	private int originWait;

    public static void main(String[] args) throws MalformedURLException, AWTException, InterruptedException {
    	Pomodoro myPom=new Pomodoro();
    }
    
    public Pomodoro() throws AWTException, MalformedURLException, InterruptedException {
    	
    	if (SystemTray.isSupported()) {
    		initPanel();
    		
    		//this.displayTray("Hello! Please type in your Waitingtime! Nothing for default 45 min.");
    		
    		
    		while(true) {//loop
    			
    			long startTime = System.currentTimeMillis();
                long elapsedTime = 0L;
                long elapsedMin=0L;
                long startMin=System.currentTimeMillis();
                
                originWait=waitingTime;

                while (true) {
                    
                    elapsedTime = (new Date()).getTime() - startTime;
                    elapsedMin=(new Date()).getTime() - startMin;
                   
                    Thread.sleep(2*1000);
                    
                    if(elapsedTime > waitingTime*60*1000) {
                    	elapsedMin=0L;
                    	break;
                    }
                    
                    if(elapsedMin>1*60*1000) {
                    	elapsedMin=0L;
                    	startMin=System.currentTimeMillis();
                    	label.setText("<html>Your learntime will be "+--originWait+" minutes.<br>Press esc to end session</html>");
                    }
                }
                counter++;
                
                stage=2;
                int breaktime=5;
                if(counter>=4) {
                	breaktime=15;
                }
                
                this.displayTray("Learnsession over! Do you want to take a "+breaktime+"min break now? y/n");
                label.setText("<html>Learnsession over!</html>");
             
                while(true) {
            	   Thread.sleep(2*1000);//antwort jede zwei sekunden checken
            	   if(stage==1) {
            		   label.setText("BreakTime: "+breaktime+" minutes");
            		  
            		   long startTime2 = System.currentTimeMillis();
                       long elapsedTime2 = 0L;
                       originWait=breaktime;
                       long startMin2=System.currentTimeMillis();
                       
                       while(true) {
                    	  
                    	   elapsedTime2 = (new Date()).getTime() - startTime2;
                           elapsedMin=(new Date()).getTime() - startMin2;
                    	   
                    	   Thread.sleep(2*1000);   
                    	   
                    	   if(elapsedTime2 > breaktime*60*1000) {
                    		   break;
                    	   }
                    	   
                    	   if(elapsedMin>1*60*1000) {
                    		   elapsedMin=0L;
                    		   startMin2=System.currentTimeMillis();
                    		   label.setText("BreakTime: "+--originWait+" minutes");
                    	   }
                    	   
                       }
            		   
            		   this.displayTray("Starting new session.");
            		   label.setText("<html>Your learntime will be "+waitingTime+" minutes.<br>Press esc to end session</html>");
            		   break;
            	   }
               }
                
    		}
            
            
        } else {
            System.err.println("System tray not supported!");
        }
        
        killIt();
    }
    
    public void displayTray(String myMessage) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage(myMessage, "Pomodoro Messenger", MessageType.INFO);
    }

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode=e.getKeyCode();
		
		if(keyCode==KeyEvent.VK_ESCAPE) {
			try {
				killIt();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
		
		if(keyCode==KeyEvent.VK_ENTER&&stage==0) {
			//System.out.println(field.getText());
			
			String input=field.getText();
			if(input.equals("")) {
				this.waitingTime=25;
			}
			else {
				this.waitingTime=Integer.parseInt(input);
			}
			
			stage=1;
			
			label.setText("<html>Your learntime will be "+waitingTime+" minutes.<br>Press esc to end session</html>");
			
			originWait=waitingTime;
			
			try {
				this.displayTray("Starting session...");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			
		}
		
		if(stage==2) {
			if(keyCode==KeyEvent.VK_Y) {
				stage=1;
			}
			if(keyCode==KeyEvent.VK_N) {
				try {
					this.displayTray("Waiting...");
					label.setText("Press y to take break");
				} catch (MalformedURLException e1) {
					
					e1.printStackTrace();
				} catch (AWTException e1) {
					
					e1.printStackTrace();
				}
			}
		}
		
		e.consume();
		if(stage!=0) {
			field.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public void initPanel() {
	    
		field=new JTextField();
    	field.addKeyListener(this);
    	this.add(field,BorderLayout.CENTER);
    	this.pack();
    	
    	label=new JLabel("<html>Hello!<br>Please type in your Sessiontime!<br>Nothing for default 25 min.</html>",JLabel.CENTER);
		label.setFont(new Font("Verdana",1,20));
		
		this.add(label);
	   
		this.setSize(400, 400);
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setLocationRelativeTo(null);
    	this.setVisible(true);
	}
	
	public void killIt() throws MalformedURLException, AWTException {
		this.displayTray("Ending Session...");
        System.exit(0);
	}

}