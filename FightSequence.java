//package finalProjectWorkLab;

/*
 * Author: Anthony Madrigal-Murillo
 * Date Started: 4/17/25
 * Project: Simple Monster Battler
 * Goal: Monsters fight in RPS fashion
 * 
 */

/*
 *  FightSequence will hold the actions for battle to make the code read cleanly.
 */

public class FightSequence {
	
	//Method prints out both monster hit points
	public void sequenceHealthStep(Monster activeMonsterPOne, Monster activeMonsterPTwo) {
		System.out.println("Your monster has " + activeMonsterPOne.getHitPoints() + " HP.");
		System.out.println("Your opponent's monster has " + activeMonsterPTwo.getHitPoints() + " HP.");
	}
	
	//Method gives damage from first monster to second monster
	public void takeDamage(Monster attackingMonster,Monster defendingMonster) {
		defendingMonster.setHitPoints(defendingMonster.getHitPoints()-attackingMonster.getAttack());
	}
	
	//1 = tackle,2 == counter hit, 3 == charge punch
	//tackle beats charge punch, counter hit beats tackle, charge punch beats counter hit
	public void battleStep(int userAction,Monster monsterOne, Monster monsterTwo) {
		//random computer action
		int computerAction = (int) (Math.random() * (3 - 1 + 1)) + 1;
		if(userAction == 1) {													//user picks tackle
			System.out.print("You picked tackle ");
			switch(computerAction) {
			case 1://computer picks tackle
				System.out.println("vs the opponent's tackle.");
				System.out.println("You lost " + monsterTwo.getAttack() + " HP");
				System.out.println("Your opponent lost " + monsterOne.getAttack() + " HP");
				takeDamage(monsterOne,monsterTwo);
				takeDamage(monsterTwo,monsterOne);
				break;
			case 2://computer picks counter hit
				System.out.println("vs the opponent's counter hit.");
				System.out.println("You lost " + monsterTwo.getAttack() + " HP");
				takeDamage(monsterTwo,monsterOne);
				break;
			case 3://computer picks charge punch
				System.out.println("vs the opponent's charge punch.");
				System.out.println("Your opponent lost "+ monsterOne.getAttack() + " HP");
				takeDamage(monsterOne,monsterTwo);
				break;
			}
		}
		if(userAction == 2) {													//user picks counter hit
			System.out.print("You did counter hit ");
			switch(computerAction) {
			case 1://computer picks tackle
				System.out.println("vs the opponent's tackle.");
				System.out.println("Your opponent lost " + monsterOne.getAttack() + " HP");
				takeDamage(monsterTwo,monsterOne);
				break;
			case 2://computer picks counter hit
				System.out.println("vs the opponent's counter hit.");
				System.out.println("You lost " + monsterTwo.getAttack() + " HP");
				System.out.println("Your opponent lost " + monsterOne.getAttack() + " HP");
				takeDamage(monsterOne,monsterTwo);
				takeDamage(monsterTwo,monsterOne);
				break;
				
			case 3://computer picks charge punch
				System.out.println("vs the opponent's charge punch.");
				System.out.println("You lost "+ monsterOne.getAttack() + " HP");
				takeDamage(monsterTwo,monsterOne);
				break;
			}
		}
		if(userAction == 3) {															//user picks charge punch
			System.out.print("You did charge punch ");
			switch(computerAction) {
			case 1://computer picks tackle
				System.out.println("vs the opponent's tackle.");
				System.out.println("You lost "+ monsterOne.getAttack() + " HP");
				takeDamage(monsterTwo,monsterOne);
				break;
				
			case 2://computer picks counter hit
				System.out.println("vs the opponent's counter hit.");
				System.out.println("You lost " + monsterTwo.getAttack() + " HP");
				takeDamage(monsterOne,monsterTwo);
				break;
				
			case 3://computer picks charge punch
				System.out.println("vs the opponent's charge punch.");
				System.out.println("You lost " + monsterTwo.getAttack() + " HP");
				System.out.println("Your opponent lost " + monsterOne.getAttack() + " HP");
				takeDamage(monsterOne,monsterTwo);
				takeDamage(monsterTwo,monsterOne);
				break;
			}
		}
		
		//checks if either monster faints and sets them to fainted
		if(monsterTwo.getHitPoints() <= 0.0) {
			monsterTwo.setFainted(true);
		}
		if(monsterOne.getHitPoints() <= 0.0) {
			monsterOne.setFainted(true);
		}
	}
	
}
