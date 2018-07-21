package main.java.pomUI;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.java.pomUI.Pom.Stage;

public class UISys extends JFrame implements KeyListener{//class to handle all the UI elements, and also functions as keylistener

	JTextField field;
	JLabel label;
	
	Pom myPom;
	
	public UISys(Pom pom) {
		initPanel();
		myPom=pom;
	}
	
	private void initPanel() {
	    
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
	
	public JTextField getField() {
		return field;
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	
	//KeyEvents
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {//handler for keyEvents
	
		int keyCode=e.getKeyCode();
		
		if(keyCode==KeyEvent.VK_ESCAPE) {//stopping program
			try {
				myPom.killIt();;
			} catch (MalformedURLException | AWTException e1) {
				e1.printStackTrace();
			} 
		}
		
		if(keyCode==KeyEvent.VK_ENTER && myPom.currentStage==Stage.Setup) {//read the input and set it as sessiontime
			
			String input=field.getText();
			if(!isNumber(input)||input.charAt(0)=='0') {//illegal input
				field.setText("");
			}
			else {
				
				if(input.equals("")) {
					myPom.SessionTime=25;
				}
				else {
					myPom.SessionTime=Integer.parseInt(input);
				}
				
				myPom.currentStage=Stage.Session;
				
			}
		
		}
		
		if(keyCode==KeyEvent.VK_Y && myPom.currentStage==Stage.Idle) {//accepting break
			myPom.currentStage=Stage.Pause;
		}
		
		e.consume();
		if(myPom.currentStage!=Stage.Setup) {
			field.setText("");
		}
	}
	
	private boolean isNumber(String number) {
		
		if(number.equals("")) {
			return true;
		}
		
		for(int i=0;i<number.length();i++) {
			if(number.charAt(i)<48||number.charAt(i)>57) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	
	}

}
