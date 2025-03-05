Card Game - 24

This project is a JavaFX implementation of the classic “24” card game. The goal of the game is to use four randomly selected playing cards and apply arithmetic operations to create an expression that evaluates to 24.

Features
	•	Random Card Selection:
Four playing cards are randomly chosen from a standard deck. Card values are based on:
	•	Ace = 1
	•	Numbers 2–10 as written
	•	Jack = 11
	•	Queen = 12
	•	King = 13
	•	User Expression Input:
Players must use each of the four card values exactly once, combining them with addition (+), subtraction (-), multiplication (*), and division (/), along with parentheses for grouping.
	•	Solution Finder:
A “Find a Solution” button automatically generates a valid arithmetic expression (if one exists) that evaluates to 24 using the current set of cards.
	•	Verification:
The “Verify” button checks if the user-entered expression is correct and evaluates to 24.
	•	UI Styling:
A Swing-like UI with gradient backgrounds, 1-pixel black borders around card images, and a consistent layout across top, center, and bottom sections.


Requirements
	•	Java JDK 11 or higher (tested with JDK 23)
	•	JavaFX 23.0.1 (or compatible version)
	•	Maven or Gradle for build management (if desired)

How to Run
	1.	Clone the Repository:
 git clone https://github.com/yourusername/Module3Assignment.git
	2.	Run the Application

 Usage
	•	Starting the Game:
When the application launches, four random cards are displayed.
	•	Finding a Solution:
Click the “Find a Solution” button to auto-generate a valid expression that evaluates to 24. The solution will appear in the top non-editable text field.
	•	Verifying Your Answer:
Enter your expression in the bottom text field (using each card value exactly once) and click the “Verify” button. An alert will indicate whether your solution is correct.
	•	Refreshing Cards:
Click the “Refresh” button (located on the top row) to generate a new set of cards.
  
