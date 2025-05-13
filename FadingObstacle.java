//All Code made by me -setters and getters

/*
 * 
 * Author: Anthony Madrigal-Murillo
 * Started: 4/17/25
 * Program: Simple graphics arcade shooter game similar to retro game Galaga.
 * The player is at the bottom of the screen and must avoid obstacles while shooting an enemy to earn as much points as possible.
 * Obstacles fall from the top of the screen. The enemy moves left and right in a predictable pattern but the obstacles speed up and
 * slow down as time goes on. Once the player accumulates 80 points a danger will appear which the player must avoid.
 * The player can only shoot in a straight line above themselves. A leader board holds the top 6 scores.
 * 
 */

import java.awt.*;

public class FadingObstacle {//Object used to build the dangerZone attack

	private int obstacleWidth;
	private int obstacleHeight;
	private int obstacleX;
	private int obstacleY;
	private int obstacleSpeed;
	private int attackTimer;
	private Color obstacleColor;
	
	//Default constructor
	public FadingObstacle() {
		obstacleWidth = 200;
		obstacleHeight = 200;
		obstacleX = 200;
		obstacleY = 200;
		obstacleSpeed = 10;
		attackTimer = 0;
		obstacleColor = Color.red;
	}
	
	//Common constructor
	public FadingObstacle(int obstacleWidth, int obstacleHeight, int xPosition,int yPosition,int speed,Color obstacleColor,String shape) {
		this.obstacleWidth = obstacleWidth;
		this.obstacleHeight = obstacleHeight;
		obstacleX = xPosition;
		obstacleY = yPosition;
		obstacleSpeed = speed;
		this.obstacleColor = obstacleColor;
	}
	
	// Getters (Provided by Copilot)
	public int getObstacleWidth() { return obstacleWidth; }
	public int getObstacleHeight() { return obstacleHeight; }
	public int getObstacleX() { return obstacleX; }
	public int getObstacleY() { return obstacleY; }
	public int getObstacleSpeed() { return obstacleSpeed; }
	public int getAttackTimer() { return attackTimer; }
	public Color getObstacleColor() { return obstacleColor; }

	// Setters (Provided by Copilot)
	public void setObstacleWidth(int obstacleWidth) { this.obstacleWidth = obstacleWidth; }
	public void setObstacleHeight(int obstacleHeight) { this.obstacleHeight = obstacleHeight; }
	public void setObstacleX(int obstacleX) { this.obstacleX = obstacleX; }
	public void setObstacleY(int obstacleY) { this.obstacleY = obstacleY; }
	public void setObstacleSpeed(int obstacleSpeed) { this.obstacleSpeed = obstacleSpeed; }
	public void setAttackTimer(int attackTimer) { this.attackTimer = attackTimer;}
	public void setObstacleColor(Color obstacleColor) {this.obstacleColor = obstacleColor;}
	
	//Paint Obstacle Method
	public void paintObstacle(Graphics g) {
			g.setColor(obstacleColor);
			g.fillRect(getObstacleX(),getObstacleY(),getObstacleWidth(),getObstacleHeight());
	}
	
	public void randomizeLocation() {
		obstacleX = (int) (Math.random() * 601);
		obstacleY = (int) ((Math.random() * 201)+200);
	}
}
