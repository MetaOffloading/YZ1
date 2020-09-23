package com.sam.webtasks.iotask2;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sam.webtasks.basictools.Counterbalance;
import com.sam.webtasks.basictools.Names;
import com.sam.webtasks.basictools.PHP;
import com.sam.webtasks.client.SequenceHandler;

public class IOtask2PreTrial {
	public static void Run() {
		
		final Date instructionStart = new Date();
		
		IOtask2BlockContext.setReminderFlag(-1);
		IOtask2BlockContext.setBackupReminderFlag(-1);
		IOtask2BlockContext.setReminderCompletedCircles(-999);

		final HTML displayText = new HTML();
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		final VerticalPanel verticalPanel = new VerticalPanel();
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		String displayString = "";

		if (IOtask2BlockContext.showPoints()) {
			if (IOtask2BlockContext.getPointDisplay() == Names.POINT_GAINLOSS) {
			displayString = displayString + "You have " + IOtask2BlockContext.getTotalPoints()
					+ " points (" + IOtask2BlockContext.getMoneyString() + ").<br><br>";
			} else {
				displayString = displayString + "You have scored " + IOtask2BlockContext.getTotalPoints()
				+ " points.<br><br>";
			}
		}

		int points = IOtask2BlockContext.currentTargetValue();

		if (points == 0) {
			displayString = displayString + "This time you must do the task <b>without</b> setting any reminders.<br><br>";
			
			if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
				displayString = displayString + "You will gain " + IOtask2BlockContext.maxPoints() 
				+ " points for every special circle that you remember.";
			} else {
				displayString = displayString + "You will lose " + IOtask2BlockContext.maxPoints()
				+ " points for every special circle that you forget.";
			}
			
			displayString = displayString + "<br><br>Please click the button below to start.";
		} else if (points == IOtask2BlockContext.maxPoints()) {
			displayString = displayString + "This time you <b>must</b> set a reminder for every special circle.<br><br>";
					
			if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
				displayString = displayString + "You will gain " + IOtask2BlockContext.maxPoints() 
				+ " points for every special circle that you remember.";
			} else {
				displayString = displayString + "You will lose " + + IOtask2BlockContext.maxPoints() 
				+ " points for every special circle that you forget.";
			}
			
			displayString = displayString + "<br><br>Please click the button below to start.";
		} else {
			displayString = displayString + "This time you must make a decision.<br><br>";
					
			if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
				displayString = displayString + "If you forget a special circle, you will not score any points.";
			} else {
				displayString = displayString + "If you forget a special circle, you will lose " 
			    + IOtask2BlockContext.maxPoints() + " points.";
			}
			
			displayString = displayString + "<br><br>You have a choice for what happens when you remember. "
					+ "Please click the option that you prefer.";
		}

		displayText.setHTML(displayString);

		//reminder button
		if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
			displayString = "Special circles worth<br><b>" + points + " </b>points<br><br>" + "Reminders allowed";	
		} else {
			displayString = "Lose " + (IOtask2BlockContext.maxPoints() - points)
					        + " points<br>each time you remember<br><br>Reminders allowed";
		}
		
		if (points == IOtask2BlockContext.maxPoints()) {
			displayString = "You <b>must</b> set reminders";
		}
		
		final Button reminderButton = new Button(displayString);

		//no-reminder button
		if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
			displayString = "Special circles worth<br><b>" + IOtask2BlockContext.maxPoints() + " </b>points<br><br>" + "Reminders <b>not</b> allowed";		
		} else {
			displayString = "Lose 0 points<br>each time you remember<br><br>Reminders not allowed";
		}
		
		if (points == 0) {
			displayString = "Reminders <b>not</b> allowed";
		}
		
		final Button noReminderButton = new Button(displayString);

		if (Counterbalance.getFactorLevel("buttonColours") == 0) {
			reminderButton.setStyleName("pinkButton");
			noReminderButton.setStyleName("greenButton");
		} else {
			reminderButton.setStyleName("greenButton");
			noReminderButton.setStyleName("pinkButton");
		}

		if (Counterbalance.getFactorLevel("buttonPositions") == 0) {
			if (points > 0) {
				buttonPanel.add(reminderButton);
			}

			if (points < IOtask2BlockContext.maxPoints()) {
				buttonPanel.add(noReminderButton);
			}
		} else {
			if (points < IOtask2BlockContext.maxPoints()) {
				buttonPanel.add(noReminderButton);
			}

			if (points > 0) {
				buttonPanel.add(reminderButton);
			}
		}

		// set up vertical panel
		verticalPanel.setWidth("75%");
		// verticalPanel.setHeight(Window.getClientHeight() + "px");
		verticalPanel.setHeight("300px");

		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		// add elements to panel
		displayText.setStyleName("instructionText");
		verticalPanel.add(displayText);
		verticalPanel.add(buttonPanel);

		// place vertical panel inside horizontal panel, so it can be centred
		horizontalPanel.setWidth(Window.getClientWidth() + "px");
		horizontalPanel.setHeight(Window.getClientHeight() + "px");

		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		horizontalPanel.add(verticalPanel);

		// add panel to root
		RootPanel.get().add(horizontalPanel);

		// equalise the dimensions of the buttons
		if ((points > 0) & (points < IOtask2BlockContext.maxPoints())) {
			reminderButton.setWidth(noReminderButton.getOffsetWidth() + "px");
			noReminderButton.setHeight(reminderButton.getOffsetHeight() + "px");
		}

		reminderButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Date responseTime = new Date();
				
				IOtask2BlockContext.setReminderChoice(1);
				IOtask2BlockContext.setReminderCost(IOtask2BlockContext.currentTargetValue() -
												    IOtask2BlockContext.maxPoints());
				
				//,1, below indicates the output that reminders have been selected
				
				final String data = IOtask2BlockContext.getTrialNum() + "," + IOtask2BlockContext.currentTargetValue() + ",1," + (int) (responseTime.getTime() - instructionStart.getTime()); 

				RootPanel.get().remove(horizontalPanel);

				IOtask2BlockContext.setOffloadCondition(Names.REMINDERS_MANDATORY_TARGETONLY);
				
				IOtask2BlockContext.setActualPoints(IOtask2BlockContext.currentTargetValue());

				new Timer() {
					public void run() {
						PHP.logData("preTrial", data, true);
					}
				}.schedule(500);
			}
		});

		noReminderButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Date responseTime = new Date();
				
				IOtask2BlockContext.setReminderChoice(0);
				IOtask2BlockContext.setReminderCost(0);
				
				//,0, below indicates that reminders have not been selected
				final String data = IOtask2BlockContext.getTrialNum() + "," + IOtask2BlockContext.currentTargetValue() + ",0," + (int) (responseTime.getTime() - instructionStart.getTime()); 

				RootPanel.get().remove(horizontalPanel);

				IOtask2BlockContext.setOffloadCondition(Names.REMINDERS_NOTALLOWED);
				
				IOtask2BlockContext.setActualPoints(IOtask2BlockContext.maxPoints());

				new Timer() {
					public void run() {
						PHP.logData("preTrial", data, true);
					}
				}.schedule(500);
			}
		});
	}
}
