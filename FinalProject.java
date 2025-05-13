import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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

public class FinalProject extends JPanel implements ActionListener, KeyListener {

	// Box position																			//Added by me
	private int boxX = 350;
	private int boxY = 400;
	
	// Falling Obstacles position and speed
	private int obstacleYDirection = 0;
	private int obstacleXDirection = 200;
	private int obstacleSpeed = 10;
	
	//enemy position, speed, and color
	private int enemyX = 350;
	private int enemyY = 50;
	private int enemySpeed = 10;
	private Color enemyColor = Color.cyan;
	
	//player score, size, color, speed, and health
	private int playerScore= 0;
	private final int BOX_SIZE = 50;
	private Color playerColor = Color.green;
	private final int SPEED = 15;
	private int playerHealth = 40;
	
	//file name for leaderboard text file
	private String filename = "NewLeaderBoard.txt";
	
	//trackers for recent hits
	private int hitFrames = 0; // Tracks how long ago the player was hit
	private boolean hitRecently = false; // Will prevent instant color reset
	
	private FadingObstacle squareAttack = new FadingObstacle(100,100,200,200,10,Color.darkGray,"square"); //danger zone that appears later in game
	private static Queue<Color> colorQueue = new LinkedList<>(); //global colorQueue
	private static Stack<Color> colorStack = new Stack<>();      //global colorStack

	//Player's bullet Variables
	private int attackX = boxX+12;
	private int attackY = boxY-25;
	private Color attackColor = Color.black;

	// Which arrow keys are currently held down                               //From CPSC 06 Template
	private boolean up, down, left, right, space;
	
	// major timer for game's frame-rate
	private final Timer timer;

	//Insures leader-board is updated once									//From me
	private boolean gameOverLogged = false;
	
	//Insures game doesn't start until a button is pressed
	private boolean gameStarted = false;


	public FinalProject() {												//From CPSC 06 template
		setPreferredSize(new Dimension(800, 600)); //Sets window size
		setBackground(Color.BLACK);                //Sets background color
		setFocusable(true);                        //Sends keyboard input to game window
		addKeyListener(this);					   //Interprets keyboard input for this program's code

		// ≃1000ms/60 ≃16ms per frame for ~60 FPS
		timer = new Timer(16, this);               //timer events trigger every 16ms
		timer.start();							   //starts timer 

	}

	@Override
	protected void paintComponent(Graphics g) {     //From CPSC 06 Template
								//Graphics g handles all the "painting" that happens on the screen
		
		// Draw Game Over screen										//Added by CoPilot with edits by me
		if (playerHealth <= 0) {
			g.setColor(Color.MAGENTA); 						//selects color
			g.setFont(new Font("Arial", Font.BOLD, 40));	//selects font
			g.drawString("GAME OVER", 300, 200);			//draws string in quotes

			// **Step 1: Read leaderboard from file**
			List<String> leaderboard = new ArrayList<>();                                //ArrayList for holding strings with indexes
			try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { //buffered reader holds strings from file
				String line;
				while ((line = reader.readLine()) != null) {
					leaderboard.add(line); // Store each score entry
				}
			} catch (IOException e) {
				System.out.println("Error reading leaderboard file.");
			}

			// **Step 2: Draw leaderboard on screen**
			g.setColor(Color.pink);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Top 6 Leaderboard:", 300, 250);

			int yOffset = 280; // Start printing scores below "Top 6 Leaderboard"
			for (String score : leaderboard) {			//Uses indexes from before to output every score on leaderboard
				g.drawString(score, 300, yOffset);
				yOffset += 30; // Move down for each entry
			}
			return; // Stop rendering other game objects
		}

		super.paintComponent(g); //clears previous frames from window to prevent overlapping images
																								//Added by me
        
		//Painted Player
		g.setColor(playerColor);
		g.fillRect(boxX, boxY, BOX_SIZE, BOX_SIZE);

		//Painted PlayerAttack
		g.setColor(attackColor);
		g.fillRect(attackX, attackY, BOX_SIZE/2, BOX_SIZE/2);


		//Painted Obstacle
		g.setColor(Color.RED);
		g.fillRect(obstacleXDirection,obstacleYDirection, BOX_SIZE*8,BOX_SIZE);

		//Painted Enemy
		g.setColor(enemyColor);
		g.fillRect(enemyX, enemyY, BOX_SIZE, BOX_SIZE);

		//Added by me
		//Paint squareFadeObstacle
		if(playerScore >= 80) {
			squareAttack.paintObstacle(g);
		}

		// Draw the player's health on screen                   //Added by Copilot
		g.setColor(Color.WHITE); // Set text color
		g.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size and style
		g.drawString("Health: " + playerHealth, 20, 30); // Draw at position (20,30)

		// Draw the player's score on screen                   //Added by Copilot
		g.setColor(Color.WHITE); // Set text color
		g.setFont(new Font("Arial", Font.BOLD, 20)); // Set font size and style
		g.drawString("Score: " + playerScore, 500, 30); // Draw at position (20,30)

	}


	private void update() {	//updates every frame

		if (!gameStarted) { // Ensures nothing happens until the first keypress
			return; 
		}

		if (playerHealth <= 0) { //When the player dies
			if (!gameOverLogged) {//Ensures we only write current score once
				LinkedList<Integer> scores = new LinkedList<>(); 	//Empty linked list for scores to be entered

				//Read existing scores into LinkedList                                        		    //Written by CoPilot
				try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {            //
					String line;																		//
					while ((line = reader.readLine()) != null) {										//
						if (line.startsWith("New Score: ")) {											//
							scores.add(Integer.parseInt(line.replace("New Score: ", "").trim()));		//
						}																				//
					}																					//
				} catch (IOException e) {																//
					System.out.println("Error reading leaderboard file.");								//
				}																						//

				//Insert new score in sorted order
				int index = 0;
				while (index < scores.size() && scores.get(index) > playerScore) {
					index++;
				}
				scores.add(index, playerScore); // Inserts score in correct position (even if last which is 7th)

				//Keep only top 6 scores
				while (scores.size() > 6) {
					scores.removeLast(); // Remove lowest score
				}

				//Writes back updated leaderboard														 //Written by CoPilot
				try (FileWriter fileWriter = new FileWriter(filename, false)) { // false overwrites file //
					for (int score : scores) {															 //
						fileWriter.write("New Score: " + score + "\n");                                  //
					} 																					 //
					fileWriter.flush();																	 //
					System.out.println("Leaderboard updated!");											 //
				} catch (IOException e) {																 //
					System.out.println("Error writing leaderboard.");									 //
				}																						 //

				gameOverLogged = true;
			}
			return;  // Stop all movement updates because player is dead
		}

		//Updates player position according to key-presses from CPSC-06 template
		if (up)    boxY -= SPEED; 
		if (down)  boxY += SPEED;
		if (left)  boxX -= SPEED;
		if (right) boxX += SPEED;

		//shooting functionality															//Added by me
		if (!space) { //Updates attack to player position when not active
			attackX = boxX + 12;
			attackY = boxY - 25;
		}

		if (space) { //fires bullet
			if(colorStack.isEmpty()) { 				//prepares stack with rainbow colors in reverse order
				colorStack.add(Color.magenta);
			    colorStack.add(Color.blue);
			    colorStack.add(Color.green);
			    colorStack.add(Color.yellow);
			    colorStack.add(Color.orange);
			    colorStack.add(Color.red);
			}
			attackColor = colorStack.pop(); //takes top color from stack
			if (attackY >= 0) {
				attackY -= 10; // moves the attacks up until it reaches the top of the screen
			} else {
				space = false; // disables the attacks movement once it reaches the top of the screen
				attackColor = Color.black; //hides bullet by making it black
				attackY = boxY - 25; //keeps bullet in position just above the player
				attackX = boxX + 12;
			}
		}

		//keeps obstacles moving
		if(obstacleYDirection <= 600) {  //Prevents obstacle from moving infinitely off-screen
			obstacleYDirection += obstacleSpeed;
		}else {
			obstacleYDirection = 0;								//Refreshes X & Y of obstacle in new locations
			obstacleXDirection = (int)(Math.random()*(100)*4);	//^
			if(obstacleSpeed <= 20) {
				obstacleSpeed++; 									//Increases speed of obstacle
			}else {
				obstacleSpeed = 10; 								//Resets speed to 10 once it gets too fast
			}
		}

		// Reverses direction of enemy when reaching screen edges
		if (enemyX >= 750) { // Right edge
			enemySpeed = -10;
		} else if (enemyX <= 0) { // Left edge
			enemySpeed = 10;
		}

		enemyX += enemySpeed; // Moves enemy constantly

		//Danger Zone implementation
		squareAttack.setAttackTimer(squareAttack.getAttackTimer() + 1); //counts two seconds for the attack to be grey
		
		if(squareAttack.getAttackTimer() >= 120 && playerScore >= 80) { // once the attack has been present for 2 seconds and the 
			squareAttack.setObstacleColor(Color.red);                   // score threshold is met the attack turns red and does damage
			if((boxY - squareAttack.getObstacleY() <= 100 && boxY - squareAttack.getObstacleY() >= -50) && //Checks if players Y coordinate is within the area of the obstacle
					(boxX - (squareAttack.getObstacleX()) <= 100 && boxX - (squareAttack.getObstacleX()) >= -50) && //Checks if players X coordinate is within the area of the obstacle
					playerHealth > 0){
						playerHealth -= 1;
						if (!hitRecently) { //Changes color to yellow to reflect recently taken damage.
							playerColor = Color.YELLOW;
							hitRecently = true;
							hitFrames = 30; // Set duration for hit effect (1/2 second at 60 FPS)
						}
					}
			if(squareAttack.getAttackTimer() >= 200) { //Goes back to inactive
				squareAttack.randomizeLocation();
				squareAttack.setAttackTimer(0);
				squareAttack.setObstacleColor(Color.darkGray);
			}
		}

		//checks constantly for obstacle and player collisions
		if ((boxY - obstacleYDirection <= 50 && boxY - obstacleYDirection >= -50) && //Checks if players Y coordinate is within the area of the obstacle
				(boxX - (obstacleXDirection + 200) <= 200 && boxX - (obstacleXDirection + 200) >= -250) && //Checks if players X coordinate is within the area of the obstacle
				playerHealth > 0) { //insures players health doesn't keep decreasing after reaching 0

			playerHealth -= 1; //Deals damage
			if (!hitRecently) { //Changes color to yellow to reflect recently taken damage.
				playerColor = Color.YELLOW;
				hitRecently = true;
				hitFrames = 30; // Set duration for hit effect (1/2 second at 60 FPS)
			}
		}

		// If the player was recently hit, count down frames
		if (hitFrames > 0) {
			hitFrames--; // Reduce the frame counter
			if (hitFrames == 0) {
				playerColor = Color.GREEN; // Reset color after 30 frames
				hitRecently = false; // Allow future color changes 
			}
		}

		if(playerHealth <= 0) {//hides player when they die
			playerColor = Color.BLACK;
		}
		
		//Collision checker for bullets and enemy
		if ((attackX + BOX_SIZE/2 >= enemyX && attackX <= enemyX + BOX_SIZE) &&
				(attackY + BOX_SIZE/2 >= enemyY && attackY <= enemyY + BOX_SIZE)) { 
			
			//On collision if the colorQueue is empty it gets filled again
			if(colorQueue.isEmpty()) {
		        colorQueue.add(Color.RED);
		        colorQueue.add(Color.ORANGE);
		        colorQueue.add(Color.yellow);
		        colorQueue.add(Color.green);
		        colorQueue.add(Color.blue);
		        colorQueue.add(Color.magenta);
			}
	        enemyColor = colorQueue.poll(); // Removes and returns the first color
			playerScore += 10; //adds points
			attackY = -50;  // Remove attack after collision
		}


		// Keeps box inside the window                                          //From CPSC 06 template
		boxX = Math.max(0, Math.min(getWidth()  - BOX_SIZE, boxX));
		boxY = Math.max(250, Math.min(getHeight() - BOX_SIZE, boxY));
	}

	@Override																	//From CPSC 06 template
	public void actionPerformed(ActionEvent e) { //every time the timer ticks (16ms) these actions are called
		update();
		repaint(); 					//repaint isn't in this code but exists in a java library
	}

	@Override
	public void keyPressed(KeyEvent e) {                                       //From CPSC 06 template

		if (!gameStarted) {                                                    //Added by me
			gameStarted = true; // Start game on first keypress				   //
			System.out.println("Game Started!");							   //
		}

		switch (e.getKeyCode()) {								//interprets keys into boolean states of true when pressed
		case KeyEvent.VK_UP:    up    = true; break;
		case KeyEvent.VK_DOWN:  down  = true; break;
		case KeyEvent.VK_LEFT:  left  = true; break;
		case KeyEvent.VK_RIGHT: right = true; break;
		case KeyEvent.VK_SPACE: space = true; break;                           //
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {                                      //The rest if from CPSC 06 template
		switch (e.getKeyCode()) {								//interprets keys into boolean states
		case KeyEvent.VK_UP:    up    = false; break;
		case KeyEvent.VK_DOWN:  down  = false; break;
		case KeyEvent.VK_LEFT:  left  = false; break;
		case KeyEvent.VK_RIGHT: right = false; break;
		}
	}

	@Override public void keyTyped(KeyEvent e) {
		// not used explicitly
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {   //ensure swing actions happen correctly
			public void run() {
				JFrame frame = new JFrame("Game Window");             //titles Jframe window
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exits program when the window is closed
				frame.add(new FinalProject());						  //creates instance of JFrame in program
				frame.pack();                                         //sizes the window properly
				frame.setLocationRelativeTo(null);                    //centers the window on my screen
				frame.setVisible(true);                               //makes the window visible
			}
		});
	}

}