//package finalProjectWorkLab;

/*
 * Author: Anthony Madrigal-Murillo
 * Date Started: 4/17/25
 * Project: Simple Monster Battler
 * Goal: Monsters fight in RPS fashion
 * 
 */

import java.util.Scanner;

public class StartingFrameWork {
	
	public static void main(String[] args) {
		
		//Creates scanner for main
		Scanner scnr = new Scanner(System.in);
		
		//Creates playerOne monster
		Monster playerOne = new Monster(100,20,30,30);
		
		//Creates playerTwo monster
		Monster playerTwo = new Monster(120,10,40,20);
		
		//Variable to facilitate battling
		FightSequence battle = new FightSequence();
		
		//Fight Sequence Initiates
		battle.sequenceHealthStep(playerOne,playerTwo);
		//Step Loops
		while(playerOne.getFainted() != true && playerTwo.getFainted() != true) {
			System.out.print("What would you like to do? Attacks: 1 = Tackle, 2 = Counter Hit and 3 = Charge Punch: ");
			int userChoice = scnr.nextInt();
			
			battle.battleStep(userChoice,playerOne,playerTwo);
			battle.sequenceHealthStep(playerOne,playerTwo);
		}
		
		if(playerOne.getFainted()) {
		    System.out.println("You lost better luck next time!");
		}

		if(playerTwo.getFainted()) {
		    System.out.println("Congratulations! You won!");
		}
		
		scnr.close();
	}

}
