package com.sam.webtasks.timeBasedOffloading;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TimeDisplay {
	//we can use this to check whether the display has been initialised, if not it needs to be done
	public static boolean isInitialised = false;
	
	//use this to wait specifically for a space-bar response
	public static boolean waitForSpacebar = false;
	
	//is it time to show the instruction for the next target?
	public static boolean timeForInstruction = false;
	public static String instructionString = "";
	
	//are we waiting for a target response?
	public static boolean awaitingPMresponse = false;
	
	//should a reminder be displayed for the next target?
	public static boolean showReminder = false;
	
	/*------------display parameters------------*/
	public static String displayHeight="50%";
	public static String displayWidth="80%";
	
	/*------------stimulus------------*/
	public static int stimulus = -1, stimulus_1back = -1, stimulus_2back = -1;
	
	/*------------GWT display objects------------*/
	
	public static final HorizontalPanel wrapper = new HorizontalPanel();
	public static final FocusPanel focusPanel = new FocusPanel();
	public static final VerticalPanel displayPanel = new VerticalPanel();
	public static final HorizontalPanel clockPanel = new HorizontalPanel();
	public static final HorizontalPanel stimulusPanel = new HorizontalPanel();
	public static final HorizontalPanel offloadPanel = new HorizontalPanel();
	
	public static final HTML clockDisplay = new HTML("0:00");
	public static final HTML stimulusDisplay = new HTML("Press spacebar to start");
	public static final Button offloadButton = new Button("Remind me");
	
	
	/*------------set up display------------*/
	public static void Init() {
		isInitialised = true;
		
		wrapper.setWidth(Window.getClientWidth() + "px");
		wrapper.setHeight(Window.getClientHeight() + "px");
		wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		displayPanel.setWidth(displayWidth);
		displayPanel.setHeight(displayHeight);
		displayPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		//clock panel
		clockPanel.add(clockDisplay);
		
		clockDisplay.addStyleName("clockText");
		
		displayPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		displayPanel.add(clockPanel);
		
		//stimulus panel (we wrap it inside a focus panel so that it can receive keypresses)
		stimulusDisplay.addStyleName("timeText");
		
		focusPanel.add(stimulusDisplay);
		
		stimulusPanel.add(focusPanel);
		
		displayPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		displayPanel.add(stimulusPanel);
		
		//offloading panel
		offloadButton.setEnabled(false);
		
		offloadPanel.add(offloadButton);
		
		displayPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		displayPanel.add(offloadPanel);
		
		//we wrap the display panel inside a focus panel so that it can receive keypress responses
		wrapper.add(displayPanel);
		
		//set up focus panel to receive keypress responses
		focusPanel.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown (KeyDownEvent event) {
				TimeResponse.Run(event.getNativeKeyCode());
			}
		});
		
		focusPanel.setFocus(true);
		
		//set up the offload button
		offloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showReminder=true;
				offloadButton.setEnabled(false);
				focusPanel.setFocus(true);
			}
		});
	}

	/*------------clock functions------------*/
	public static final Timer clockTimer = new Timer() {
		public void run() {
			if (++TimeBlock.currentTime == 600) {
				TimeBlock.currentTime = 0;
			}
			
			clockDisplay.setHTML(timeString(TimeBlock.currentTime));
			
			//start reminding for target?
			if (TimeBlock.lastTarget - TimeBlock.currentTime == TimeBlock.PMwindow) {
				if (showReminder) {
					reminder.scheduleRepeating(200);
				}
			}
			
			//stop reminding for target?
			if (TimeBlock.currentTime - TimeBlock.lastTarget == TimeBlock.PMwindow) {
				reminder.cancel();
				TimeDisplay.showReminder = false;
				TimeDisplay.offloadButton.setEnabled(false);
			}
			
			//instruction for next target?
			if (TimeBlock.currentTime == TimeBlock.nextInstruction) {
				awaitingPMresponse=true;
				
				instructionString = "Hit the spacebar at " + timeString(TimeBlock.nextTarget);

				timeForInstruction=true;
				
				TimeBlock.nextInstruction = TimeBlock.nextTarget+generateDelay();
				TimeBlock.lastTarget = TimeBlock.nextTarget; //save this, to check against PM response
				TimeBlock.nextTarget = TimeBlock.nextInstruction+generateDelay();
				
				cancel();
			}
		}
	};
	
	public static final Timer reminder = new Timer() {
		public void run() {
			clockDisplay.addStyleName("red");
			
			new Timer() {
				public void run() {
					clockDisplay.removeStyleName("red");
				}
			}.schedule(100);
		}
	};
	
	public static int generateDelay() {
		int delay=0;
		
		switch(Random.nextInt(3)) {
		case 0:
			delay=10;
			break;
			
		case 1:
			delay=20;
			break;
			
		case 2:
			delay=30;
			break;
		}
		
		return(delay);
	}
	
	public static void startClock() {
		clockTimer.scheduleRepeating(TimeBlock.tickTime);
	}
	
	public static void stopClock() {
		clockTimer.cancel();
	}
	
	public static final String timeString(int timeInt) {
		int mins = timeInt/60;
		int secs = (timeInt % 60);
		
		String s="";
		
		if (secs<10) {
			s=mins + ":0" + secs;
		} else {
			s=mins + ":" + secs;
		}
		
		return(s);
	}

	/*------------stimulus generation------------*/
	public static String generateStimulus() {
		boolean notvalid=true;
		
		//initialise stimuli if not done already
		if (stimulus==-1) {
			while (notvalid) {
				notvalid=false;
				
				stimulus=Random.nextInt(26);
				stimulus_1back=Random.nextInt(26);
				stimulus_2back=Random.nextInt(26);
				
				if (stimulus==stimulus_1back) {
					notvalid=true;
				}
				
				if (stimulus_1back==stimulus_2back) {
					notvalid=true;
				}
			}
		}

		int newStimulus = Random.nextInt(26);
		
		notvalid=true;
		
		if (Math.random() > TimeBlock.nBackTargetProb) { //2-back nontarget
			while (notvalid) {
				notvalid=false;
				
				newStimulus = Random.nextInt(26);
				
				if (newStimulus == stimulus) {
					notvalid=true;
				}
				
				if (newStimulus == stimulus_1back) {
					notvalid=true;
				}
				
				if (newStimulus == stimulus_2back) {
					notvalid=true;
				}
			}
		} else { //2-back target
			newStimulus = stimulus_1back;
		}
		
		stimulus_2back=stimulus_1back;
		stimulus_1back=stimulus;
		stimulus=newStimulus;

		return(Character.toString((char) (stimulus + 'A')));
	}
}
