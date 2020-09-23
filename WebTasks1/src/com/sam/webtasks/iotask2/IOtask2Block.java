package com.sam.webtasks.iotask2;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.sam.webtasks.basictools.Counterbalance;
import com.sam.webtasks.basictools.Names;
import com.sam.webtasks.client.Params;
import com.sam.webtasks.client.SequenceHandler;

public class IOtask2Block {
	// set defaults for the parameters

	// number of trials to run
	public int nTrials = 1;

	// run a standard 17-trial block?
	public boolean standard17block = false;
	
	// run a standard 13-trial block?
	public boolean standard13block = false;
		
	// run a standard 9-trial block?
	public boolean standard9block = false;
	
	// reverse the order of the forced internal/external trials?
	public boolean reverseForcedOrder = false;
	
	// run just the choice trials without forced internal/external?
	public boolean onlyChoiceTrials = false;

	// number of circles visible on screen
	public int nCircles = 6;

	// total number of circles in one trial
	public int totalCircles = 25;
	
	// update the progress bar with progress through the block? 
	public boolean updateProgress = true;

	// allow any circle to be moved? NB this will be overridden by participants'
	// choice to use reminders or not, if this choice is presented
	public int offloadCondition = Names.REMINDERS_OPTIONAL;
	
	// if offloadCondition is set to REMINDERS_VARIABLE, separately set whether
	// circles that should go to bottom, left, right, and top are moveable
	public boolean[] moveableSides = new boolean[4];

	// what block number is this? useful to store alongside performance data
	public int blockNum = 1;

	// which trial number is this?
	public int currentTrial = 0;

	// should target feedback be shown, i.e. green for correct target response, red
	// for incorrect
	public boolean showTargetFeedback = true;

	// should post-trial feedback be shown?
	public boolean showPostTrialFeedback = false;
	
	// include a countdowntimer?
	public boolean countdownTimer = false;
	public int countdownTime = Params.countdownTime; //number of seconds to start clock at

	// total number of targets to include
	public int nTargets = 10;
	
	public boolean nTargetsVariable = false;
	public boolean nTargetsShuffle = false;
	public ArrayList<Integer> nTargetsList = new ArrayList<Integer>();

	// target locations for each circle
	public int[] targetSide = new int[nTargets];
	
	// are targets set manually?
	public boolean specifyTargets = false;

	// target values
	public ArrayList<Integer> targetValues = new ArrayList<Integer>();
	
	// do different target directions have variable numbers of points?
	public boolean variablePoints = false;
	
	public int[] pointValues = new int[4];
	
	// how many circles can be dragged to each side during a surprise memory test?
	public int[] surpriseDrags = new int[4];

	// maximum number of points per target (i.e. in the no reminder condition)
	public int maxPoints = 10;

	// how many points per target are actually scored?
	public int actualPoints = 0;

	// total number of points scored so far
	public int totalPoints = 0;
	
	// are points being scored in this block? can set to false for prac blocks
	public boolean scorePoints = true;

	// tell participants how many points they've scored at each PreTrial screen?
	public boolean showPoints = true;
	
	//show point values on either side
	public boolean showPointLabels = false;
	
	// tell points how many points they've scored as they perform the task?
	public boolean showLivePoints = false;
	
	// how should points be displayed?
	public int pointDisplay = Names.POINT_STANDARD;

	// check whether the first circle has been double-clicked, if so a double-click
	// on the final circle exits the task, to help speed up debugging
	public boolean doubleClickFlag = false;
	
	// log drag end data?
	public boolean logDragData = false;
	
	// are we in gain or loss freaming?
	public int rewardFrame = Names.GAIN_FRAME;
	
	// special algorithm for adjusting points in a gain/loss experiment
	public boolean gainLossExp = false;
	public int gainLossRememberPoints = 0;
	public int gainLossForgetPoints = 0;
	public int gainLossReminderCost = 0; // how much to increment points each time a target is presented

	// if a circle has been dragged out of the box, which side has it been dragged
	// to?
	public int exitFlag = 0;

	// use these variables to check (when necessary) whether reminders have been set
	public int reminderFlag = -1;
	public int backupReminderFlag = -1;

	// task lockout as a penalty for reminders?
	public boolean reminderLockout = false;
	public int reminderLockoutTime = 2000; // duration of lockout in ms
	
	//what type of stimuli are inside the circles?
	public int ongoingStimType = Names.ONGOING_STIM_NUMBERS;

	// this method actually runs the block
	public void Run() { 
		//shuffle the various difficulty levels?
		if (nTargetsShuffle) {
			for (int i = 0; i < nTargetsList.size(); i++) {
				Collections.swap(nTargetsList, i, Random.nextInt(nTargetsList.size()));
			}
		}
		
		if (standard9block) {
			nTrials=9;
			
			// 9 trials is the standard number of trials for 2 of each forced condition
			// plus 5 choice trials (2, 4, 5, 6, 8)

			// first set up and shuffle the 9 choice values
			ArrayList<Integer> choiceValues = new ArrayList<Integer>();

			// add choice numbers
			choiceValues.add(2);
			choiceValues.add(4);
			choiceValues.add(5);
			choiceValues.add(6);
			choiceValues.add(8);

			// now shuffle
			for (int i = 0; i < choiceValues.size(); i++) {
				Collections.swap(choiceValues, i, Random.nextInt(choiceValues.size()));
			}

			// now set up the values corresponding to the forced internal / forced external
			// conditions
			int forcedA = 0; // begin with forced internal
			int forcedB = 10; // then have forced external

			if (Counterbalance.getFactorLevel("forcedOrder") == 1) { // depending on counterbalancing version,
																		// switch these around
				forcedA = 10;
				forcedB = 0;
			}
			
			if (reverseForcedOrder) {
				if (forcedA == 0) {
					forcedA = 10;
				} else {
					forcedA = 0;
				}
				
				if (forcedB == 0) {
					forcedB = 10;
				} else {
					forcedB = 0;
				}
			}

			// now put the whole list of target values together
			targetValues.add(choiceValues.get(0));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(1));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(2));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(3));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(4));
		}
		
		if (standard13block) {
			nTrials=13;
			
			// 17 trials is the standard number of trials for 4 of each forced condition
			// plus 9 choice trials

			// first set up and shuffle the 9 choice values
			ArrayList<Integer> choiceValues = new ArrayList<Integer>();

			// add numbers 2-8
			for (int i = 2; i < 9; i++) {
				choiceValues.add(i);
			}

			// now shuffle
			for (int i = 0; i < choiceValues.size(); i++) {
				Collections.swap(choiceValues, i, Random.nextInt(choiceValues.size()));
			}

			// now set up the values corresponding to the forced internal / forced external
			// conditions
			int forcedA = 0; // begin with forced internal
			int forcedB = 10; // then have forced external

			if (Counterbalance.getFactorLevel("forcedOrder") == 1) { // depending on counterbalancing version,
																		// switch these around
				forcedA = 10;
				forcedB = 0;
			}
			
			if (reverseForcedOrder) {
				if (forcedA == 0) {
					forcedA = 10;
				} else {
					forcedA = 0;
				}
				
				if (forcedB == 0) {
					forcedB = 10;
				} else {
					forcedB = 0;
				}
			}

			// now put the whole list of target values together
			targetValues.add(choiceValues.get(0));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(1));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(2));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(3));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(4));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(5));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(6));
		}
		
		if (standard17block) {
			nTrials=17;
			
			// 17 trials is the standard number of trials for 4 of each forced condition
			// plus 9 choice trials

			// first set up and shuffle the 9 choice values
			ArrayList<Integer> choiceValues = new ArrayList<Integer>();

			// add numbers 1-9
			for (int i = 1; i < 10; i++) {
				choiceValues.add(i);
			}

			// now shuffle
			for (int i = 0; i < choiceValues.size(); i++) {
				Collections.swap(choiceValues, i, Random.nextInt(choiceValues.size()));
			}

			// now set up the values corresponding to the forced internal / forced external
			// conditions
			int forcedA = 0; // begin with forced internal
			int forcedB = 10; // then have forced external

			if (Counterbalance.getFactorLevel("forcedOrder") == 1) { // depending on counterbalancing version,
																		// switch these around
				forcedA = 10;
				forcedB = 0;
			}
			
			if (reverseForcedOrder) {
				if (forcedA == 0) {
					forcedA = 10;
				} else {
					forcedA = 0;
				}
				
				if (forcedB == 0) {
					forcedB = 10;
				} else {
					forcedB = 0;
				}
			}

			// now put the whole list of target values together
			targetValues.add(choiceValues.get(0));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(1));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(2));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(3));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(4));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(5));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(6));
			targetValues.add(forcedA);
			targetValues.add(choiceValues.get(7));
			targetValues.add(forcedB);
			targetValues.add(choiceValues.get(8));
		}
		
		if (onlyChoiceTrials) {
			nTrials=9;
			
			//first set up and shuffle the 9 choice values
			ArrayList<Integer> choiceValues = new ArrayList<Integer>();

			//add numbers 1-9
			for (int i = 1; i < 10; i++) {
				choiceValues.add(i);
			}

			//now shuffle
			for (int i = 0; i < choiceValues.size(); i++) {
				Collections.swap(choiceValues, i, Random.nextInt(choiceValues.size()));
			}	
			
			//now add to list
			for (int i = 0; i < choiceValues.size(); i++) {
				targetValues.add(choiceValues.get(i));
			}
		}
		
		while (targetValues.size() < nTrials) { //if not enough target values have been set up
			targetValues.add(-1);
		}

		/*
		 * now save all the context about this block to the IOtask1BlockContext class*
		 */
		IOtask2BlockContext.setContext(this);

		/*
		 * now start subloop 3 of the sequence handler, which runs a trial of the task
		 */
		SequenceHandler.SetLoop(3, true);
		SequenceHandler.Next();
	}

	// the variables below are used during trials of the task. they don't need to be
	// set up

	// which circle should next be clicked in the sequence?
	public int nextCircle = 0;

	// which circle has actually been clicked?
	public int clickedCircle = 0;

	// use this variable for the conversion between the number of the circle in the
	// sequence (can go infinitely high) and the number of circles on screen
	// (default 6)
	public int circleAdjust = 0;

	// how many hits (just considering the most recent trial)
	public int nHits = 0;

	// did the participant choose to set reminders (1) or not (0)?
	public int reminderChoice = -1;

	// how long should target circles appear in their instruction colour before
	// fading to yellow
	public int instructionTime = 2000;

	// flash flag. use this for timing the red flash if wrong circle is dragged to
	// the bottom
	public boolean flashFlag = false;
	
	// run a surprise memory test? set to the circle number after which the test will be given. set to a big number to prevent it happening
	public int surpriseTest = 999;
	
	// use this to specify surprise tests across a block
	public ArrayList<Integer> surpriseTests = new ArrayList<Integer>();

	// ???????
	public int completedCircles = 0;
	public int backupCompletedCircles = 0;
	public int reminderCompletedCircles = -999;

	public int checkExitFlag = 1;

	// TODO: forced offloading
	// TODO: drag-by-drag data?

}
