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
				displayString = displayString + "你现在的累计积分是" + IOtask2BlockContext.getTotalPoints()
				+ "分<br><br>";
			}
		}

		int points = IOtask2BlockContext.currentTargetValue();

		if (points == 0) {
			displayString = displayString + "这次你<b>不能</b>通过设置标记提醒完成任务。<br><br>";
			
			if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
				displayString = displayString + "每移出一个特殊的圆圈，你将获得" + IOtask2BlockContext.maxPoints()
				+ "分";
			} else {
				displayString = displayString + "You will lose " + IOtask2BlockContext.maxPoints()
				+ " points for every special circle that you forget.";
			}
			
			displayString = displayString + "<br><br>请点击下方按钮开始任务";
		} else if (points == IOtask2BlockContext.maxPoints()) {
			displayString = displayString + "这次你 <b>必须</b> 通过对每一个特殊圆圈设置标记提醒来完成任务<br><br>";
					
			if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
				displayString = displayString + "每移出一个特殊的圆圈，你将获得" + IOtask2BlockContext.maxPoints() + "分";
			} else {
				displayString = displayString + "You will lose " + + IOtask2BlockContext.maxPoints() 
				+ " points for every special circle that you forget.";
			}
			
			displayString = displayString + "<br><br>请点击下方按钮开始任务";
		} else {
			displayString = displayString + "这次你需要对任务策略进行一个选择。<br><br>";
					
			if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
				displayString = displayString + "如果你忘记将一个特殊圆圈从对应边框移出，你将不会得分。";
			} else {
				displayString = displayString + "如果你忘记将一个特殊圆圈从对应边框移出，你将会失去 " + IOtask2BlockContext.maxPoints() + " 分";
			}
			
			displayString = displayString + "当选择设置标记提醒的时候，你也可不必对每一个特殊圆圈进行标记提醒。<br><br>"
					+ "请通过点击下方按钮选择您的任务策略，并开始本次任务";
		}

		displayText.setHTML(displayString);

		//reminder button
		if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
			displayString = "每个特殊圆圈值<br><b>" + points + " </b>分<br><br>" + "设置标记提醒";
		} else {
			displayString = "Lose " + (IOtask2BlockContext.maxPoints() - points)
					        + " points<br>each time you remember<br><br>Reminders allowed";
		}
		
		if (points == IOtask2BlockContext.maxPoints()) {
			displayString = "对每一个特殊圆圈<b>设置</b>标记提醒";
		}
		
		final Button reminderButton = new Button(displayString);

		//no-reminder button
		if (IOtask2BlockContext.getRewardFrame() == Names.GAIN_FRAME) {
			displayString = "每个特殊圆圈值<br><b>" + IOtask2BlockContext.maxPoints() + " </b>分<br><br>" + " <b>不设置</b> 标记提醒";
		} else {
			displayString = "Lose 0 points<br>each time you remember<br><br>Reminders not allowed";
		}
		
		if (points == 0) {
			displayString = "<b>不设置</b>标记提醒";
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
