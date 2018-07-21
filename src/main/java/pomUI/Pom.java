package main.java.pomUI;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class Pom {//class to handle pomodoro management

	enum Stage{
		Setup,
		Session,
		Idle,
		Pause
	}
	
	//Attributes
	Toastie myFavToast;
	UISys myUI;
	
	Stage currentStage;
	
	int SessionTime;
	int PauseTime;
	
	//Constructors
	public Pom() {
		
		currentStage=Stage.Setup;
		
		myFavToast=new Toastie("Pomodoro Messenger");
		myUI=new UISys(this);
		
		try {
			mainLoop();
		} catch (InterruptedException | MalformedURLException | AWTException e) {
			e.printStackTrace();
		}
	}
	
	//Methods
	
	private void mainLoop() throws InterruptedException, MalformedURLException, AWTException {
		
		if(SystemTray.isSupported()) {
			while(currentStage==Stage.Setup) {
				Thread.sleep(1000);
			}
			
			myUI.label.setText("<html>Your learntime will be "+SessionTime+" minutes.<br>Press esc to end session</html>");
			myFavToast.displayTray("Starting Session...");
			
			int Sessiontimer=0;
			
			TimeWatch watch=TimeWatch.start();
			watch.reset();
			
			while(true) {//inner loop: session-takepause-pause-session...
			
				long originWait=SessionTime;
				
				while(currentStage==Stage.Session) {//Sessiontime
					
					originWait=SessionTime-watch.time(TimeUnit.MINUTES);
					myUI.label.setText("<html>Your learntime will be "+originWait+" minutes.<br>Press esc to end session</html>");
					
					if(originWait<=0) {//Sessiontime over
						currentStage=Stage.Idle;
					}
					else {
						Thread.sleep(1000);
					}
					
				}
				
				Sessiontimer++;
				PauseTime=5;
				if(Sessiontimer>=4) {
					PauseTime+=10;
					Sessiontimer=0;
				}
				myFavToast.displayTray("Learnsession over! Do you want to take a "+PauseTime+"min break now? y/n");
				myUI.label.setText("<html>"+PauseTime+" min break ahead!<br>Press y to take break</html>");
				
				while(currentStage==Stage.Idle) {//waiting for player to take the pausetime
					Thread.sleep(1000);
				}
				
				watch.reset();//resetting stopwatch for breaktime
				long originPause=PauseTime;
				
				while(currentStage==Stage.Pause) {
					
					originPause=PauseTime-watch.time(TimeUnit.MINUTES);
					myUI.label.setText("<html>Breaktime! "+originPause+" minutes left.</html>");
					
					if(originPause<=0) {//Sessiontime over
						currentStage=Stage.Session;
					}
					else {
						Thread.sleep(1000);
					}
			
				}
				
				myFavToast.displayTray("Breaktime over! Starting new Session!");
				watch.reset();
				
			}
			
		}
	}
	
	public void killIt() throws MalformedURLException, AWTException {
		myFavToast.displayTray("Ending Session...");
        System.exit(0);
	}
	
}
