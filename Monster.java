//package finalProjectWorkLab;

/*
 * Author: Anthony Madrigal-Murillo
 * Date Started: 4/17/25
 * Project: Simple Monster Battler
 * Goal: Monsters fight in RPS fashion
 * 
 */

/*
 *  Monster class will hold the constructor for making Monster instances.
 */


public class Monster {
	
	private double hitPoints;
	private double attack;
	private double defense;
	private double speed;
	private boolean fainted;
	
	// Default Constructor
	public Monster() {
		hitPoints = -1;
		attack = -1;
		defense = -1;
		speed = -1;
		fainted = false;
	}
	
	public Monster(double newHitPoints,double newAttack, double newDefense, double newSpeed) {
		hitPoints = newHitPoints;
		attack = newAttack;
		defense = newDefense;
		speed = newSpeed;
		fainted = false;
	}
	
	// Getters
    public double getHitPoints() {
        return hitPoints;
    }
    
    public double getAttack() {
        return attack;
    }
    
    public double getDefense() {
        return defense;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public boolean getFainted() {
        return fainted;
    }
    
    // Setters
    public void setHitPoints(double newHitPoints) {
        hitPoints = newHitPoints;
    }
    
    public void setAttack(double newAttack) {
        attack = newAttack;
    }
    
    public void setDefense(double newDefense) {
        defense = newDefense;
    }
    
    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }
    
    public void setFainted(boolean newFainted) {
        fainted = newFainted;
    }

}
