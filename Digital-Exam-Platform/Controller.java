
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Controller extends QuestionBank {

    private char[] userAnswer;
    
    // Parameterized constructor to initialize with specific values
    public Controller(String title) {
        super(title);
        userAnswer = null;
    }
    
    // Parameterized constructor to initialize with specific values
    public Controller(String title, int size) {
        super(title);
        this.userAnswer = new char[size];
    }
    
    //set the userAnswer Array size
    public void setQuestionSize(int size) {
        this.userAnswer = new char[size];
    }
    
    // Method to set the user's answer at a specific location (index)
    public void setUserAnswer(int location, char userAnswer) {
        
        if(location < 0 || location >= this.userAnswer.length){
            System.out.println("[System Warning]");
            System.out.println("Invalid Value, Please Try Again");
            return;
        }
        
        this.userAnswer[location] = userAnswer;
    }
    
     // Method to get the array of user answers
    public char[] getUserAnswer() {
        return userAnswer;
    }
    
    // Method to reset the controller by clearing the user answers
    public void resetController() {
        userAnswer = null;
    }

    public boolean displayQuestion(int size, Question[] question, int continueQuestion) {

        System.out.println("[System Information]");
        System.out.println("Enter 'p' To return To The Previous Question, And '0' To Exit.");

        // Loop through each question starting from continueQuestion
        for (int i = continueQuestion; i < size; i++) {

            // Get the user's answer for the current question
            userAnswer[i] = getUserAnswer(i, question, size);

            // If the user enters '0', exit the method and return false
            if (userAnswer[i] == '0') {
                return false;
            }

            // If the user enters 'p', go to the previous question
            if (userAnswer[i] == 'p') {
                int returnCount = previousQuestion(i, 1, question, size);

                if (returnCount == 0) {
                    return false;
                } else {
                    i -= returnCount;

                    continue;
                }
            }

            // If the user is on the last question
            if (i == (size - 1)) {
                char confirmMessage = getConfirm();

                if (confirmMessage == 'N') {
                    System.out.println("[System]");
                    System.out.println("Back to Previous Question.");
                    System.out.println();
                    i--;
                } else {
                    System.out.println();
                    System.out.println("[System]");
                    System.out.println("Submitting....");
                    System.out.println();
                }
            }

        }

        // Display the marks after all questions have been answered
        displayMark(size, question);

        return true;
    }// end displayQuestion

    private void displayMark(int level, Question[] question) {

        int totalCorrect = 0;

        // Calculate the number of correct answers
        for (int i = 0; i < level; i++) {
            char userAnswerStr = userAnswer[i];
            char correctAnswer = question[i].getActualAnswer();

            if (userAnswerStr == correctAnswer) {
                totalCorrect++;
            }
        }

        // Calculate and display the score
        int score = (int) getScore(totalCorrect, level);
        System.out.println("Total Correct: " + totalCorrect);
        System.out.println("Total Incorrect: " + (level - totalCorrect));
        System.out.println("Overall Mark: " + score + "%");

    }// end displayMark

    private char getConfirm() {
        boolean isValid = false;
        Scanner sc = new Scanner(System.in);
        char confirmMessage = ' ';

        // Loop until a valid input is received
        while (!isValid) {
            System.out.println("[System]");
            System.out.print("Are you sure you want to submit? [Y/N]: ");
            String option = sc.nextLine();

            if (option.equals("Y") || option.equals("N")) { // Check if input is 'Y' or 'N'
                isValid = true; // Set flag to true indicating valid input
                confirmMessage = option.charAt(0); // Store the first character of the input

            } else {
                System.out.println("[System Warning]");
                System.out.println("Error Message, Only Accept 'Y' and 'N");  // Display error message for invalid input
                System.out.println();
            }
        }

        return confirmMessage; // Return the confirmation message
    }// end getConfirm

    private int previousQuestion(int counter, int returnCount, Question[] question, int size) {
        // Calculate the index of the previous question
        int previousIndex = counter - 1;

        // If the current question is the first question
        if (counter == 0) {
            System.out.println("[System]");
            System.out.println("You are Already at Qestion 1.");
            return returnCount;  //Return the count as is since we can't go back further
        }

        System.out.println("[Back to Previous Question...]");

        // Get the user's answer for the previous question
        userAnswer[previousIndex] = getUserAnswer(previousIndex, question, size);

        // If the user answered '0' for the previous question
        if (userAnswer[previousIndex] == '0') {
            return 0;
        }

        // If the user wants to go back further ('p' for previous)
        if (userAnswer[previousIndex] == 'p') {
            return previousQuestion(previousIndex, (returnCount + 1), question, size);
        }

        return returnCount; // return returncount

    }// end previousQuestion

    private char getUserAnswer(int counter, Question[] question, int size) {

        Scanner sc = new Scanner(System.in);
        boolean isValid = false;
        char answer = ' ';

        // Loop until a valid input is received
        while (!isValid) {
            System.out.println();
            System.out.printf("Question %d/%d\n\n", counter + 1, size);
            System.out.println(question[counter]);
            System.out.print("Enter Your Answer:");
            String input = sc.nextLine();
            System.out.println();

            // Check if the input is one of the valid options: 'a', 'b', 'c', 'd', 'p', or '0'
            if (input.equalsIgnoreCase("a")
                    || input.equalsIgnoreCase("b")
                    || input.equalsIgnoreCase("c")
                    || input.equalsIgnoreCase("d")
                    || input.equals("p")
                    || input.equals("0")) {

                if (input.equals("0")) { // Special condition if the input is '0'
                    return '0'; // Return '0' immediately
                }

                answer = Character.toLowerCase(input.charAt(0)); // Convert input to lowercase and store it
                isValid = true; // Set flag to true 
            } else {
                System.out.println("[System Warning]");
                System.out.println("Erro Message! Only Accept Chracter 'A','B','C','D', and 'p'.\n");
            }

        }

        return answer;  // Return the validated answer
    }// end getUserAnswer

    public int continueQuestion() {
        int index = 0; // Initialize index to 0

        // Loop through the userAnswer array
        for (index = 0; index < userAnswer.length; index++) {
            // Check if the current element is '0'
            if (userAnswer[index] == '0') {
                break;  // Exit the loop if '0' is found
            }
        }

        return index;
    }// end continueQuestion

    public void exportResult(String studentId, int level, String date, Question[] question) throws IOException {
        double score = 0;
        int totalCorrect = 0;
        char grade = ' ';
        String fileName = studentId + ".txt";
        String title = super.getTitle();

        // Create a PrintWriter object for writing to the file
        PrintWriter fw = new PrintWriter(fileName);

        // Write the examination title and date
        fw.printf("Examination: %s  %s%n", title, date);
        fw.println("Student Answers:");
        fw.println(studentId);

        // Loop through the questions up to the given level
        for (int i = 0; i < level; i++) {
            char userAnswerStr = userAnswer[i]; // Get the user's answer for the current question
            char correctAnswer = question[i].getActualAnswer(); // Get the correct answer for the current question

            // Adjust formatting for single-digit question numbers
            if (i < 9) {
                fw.print(" ");
            }

            // Check if the user's answer is correct
            if (userAnswerStr != correctAnswer) {
                fw.printf("%d) %s (%s)%n", i + 1, userAnswerStr, correctAnswer); // Print the question number, user's answer, and correct answer
            } else {
                fw.printf("%d) %s%n", i + 1, userAnswerStr); // Print the question number and user's answer
                totalCorrect++; // Increment the counter for correct answers
            }
        }
        // Calculate the score and grade
        score = getScore(totalCorrect, level);
        grade = getGrade(score);

        // Write the final results to the file
        fw.println();
        fw.printf("(%s): ", studentId);
        fw.printf("%d%% ", (int) score);
        fw.println(grade);
        fw.close();  // Close the PrintWriter

    }// end exportResult

    private double getScore(int totalCorrect, int level) {
        return (totalCorrect * 1.0 / level) * 100; // Calculate the score as a percentage
    }// end getScore

    private char getGrade(double score) {

        int scoreRange = (int) score / 10; // Determine the score range by dividing by 10

        // Return a grade based on the score range using a switch statement
        return switch (scoreRange) {
            case 10, 9 ->
                'A'; // For scores 90-100%
            case 8 ->
                'B';     // For scores 80-89%
            case 7 ->
                'C';     // For scores 70-79%
            case 6 ->
                'D';     // For scores 60-69%
            default ->
                'E';    // For scores below 60%
        };
    }// end getGrade

}// end controller
