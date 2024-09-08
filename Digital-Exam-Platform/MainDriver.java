
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class MainDriver {

    public static void main(String[] args) {

        String studentId = " "; //User Student ID
        String date = ""; //Exam Date
        final String TITLE = "Java Programming Lab Test";  // Title of the exam

        int option = 0; // User's menu option
        int level = 0; // Difficulty level of the exam

        boolean returnChecker = false;  // Flag to check if returning to menu
        boolean newAttempt = false;  // Flag to check if a new attempt is made
        boolean resultChecker = false;  // Flag to check if results are available
        boolean continueExam = false;  // Flag to check if the exam is to be continued
        boolean exit = false;  // Flag to check if the user wants to exit

        Controller controller = new Controller(TITLE); // Controller object for managing exam

        Question[] question = null; // Array to hold questions

        loadFile(controller); // Load questions from file

        do {

            if (returnChecker) { // If the user returns this message will be displayed
                space();
                System.out.println("[System Information]");
                System.out.println("Return to Menu Page.");
                space();
            }

            option = getUserOption(); // Get user menu option

            switch (option) {
                case 1 -> {
                    returnChecker = false; // reset flag to false

                    int index = 0;

                    if (newAttempt) { // If the user has already completed the exam and is asked if he wants to take the exam again
                        returnChecker = confirmMessage("Do You Want To Take The Exam Again?");

                        if (!returnChecker) { //If user don't want
                            returnChecker = true; //break to menu and display the message
                            break;
                        } else {
                            newAttempt = false; //If user want to take exam again, newAttempt will be reset
                            resultChecker = false; // reset resultChecker
                        }

                    }
                    //If the user quits the exam, the user can choose whether to continue the exam
                    if (continueExam) {
                        continueExam = confirmMessage("Student " + studentId + " Do You Want To Resume The Exame?"); //Get user answer, yes or no

                        if (continueExam) {
                            index = controller.continueQuestion(); //if user want to continue, find the last question the user answered
                        } else {
                            controller.resetController(); //if user want to take new exam, reset controller
                        }
                    }

                    if (!continueExam) { // new exam

                        continueExam = false; // reset the flag

                        System.out.println("Note: Enter 0 to back to Examination Menu."); // print out information 

                        studentId = getStudentID(); // Prompt the user to enter a student ID

                        if (checkReturnCode(studentId)) { // If the user enters "0", it will return to the main menu
                            returnChecker = true; //Changed the flag
                            break; // break switch case
                        }

                        loadingScreen(); // print loading screen 

                        date = date(); // Prompt the user to enter a exam date

                        if (checkReturnCode(date)) { // If the user enters "0", it will return to the main menu
                            returnChecker = true; //Changed the flag
                            break; // break switch case
                        }

                        loadingScreen();  // print loading screen 

                        level = levelSelection() * 10; // Prompt the user to enter a difficulty level

                        if (checkReturnCode(level)) { // If the user enters 0, it will return to the main menu
                            returnChecker = true; //Changed the flag
                            break; // break switch case
                        }

                        loadingScreen(); // print loading screen 

                        question = controller.getQuestions(level); // get a set of random Question

                        controller.setQuestionSize(level); // reset the array size (userAnswer)
                    }

                    resultChecker = controller.displayQuestion(level, question, index); // Display the question

                    if (resultChecker) { //If the result check is true, it means the user has completed the exam
                        continueExam = false; // changed the flag
                        newAttempt = true;
                    } else {
                        continueExam = true; // changed the flag
                        returnChecker = true;
                    }

                }//end case 1

                case 2 -> {

                    if (resultChecker) { // Only users who have completed the exam can export the results
                        try {
                            System.out.println("[System Information]");   //Show export message
                            System.out.println("Export your results...");
                            space();
                            controller.exportResult(studentId, level, date, question); // export the result
                            System.out.println("Your Exam Result Have Been Exported Successfully.");
                        } catch (IOException e) { // If exporting exam results fails
                            space();
                            System.out.println("[System Error]");
                            System.out.println("Something Wrong When Exporting The Result...."); // show error message
                        }

                    } else {
                        space();
                        System.out.println("[System Warning]");
                        System.out.println("Please Start The Exam First and Then Export The Results."); // if user haven't completed exam
                        space();
                        returnChecker = true; // changed flag
                    }

                }//end case 2

                case 3 -> {
                    exit = confirmMessage("Are You Sure You Want to Exit?"); // Prompt the user enter confirmMessage [Y/N]

                    if (!exit) { // if user enter 'N"
                        returnChecker = true; // changed the flag
                        option = 0; // reset option
                        break; // break the swtich case
                    }
                }//end case 3

            }//end switch

        } while (option != 3); // if user enter 3, will exit the loop

        if (exit) { // show exit information
            System.out.println("[System Information]");
            System.out.println("Exit successfully.");
            controller.resetController();
            controller.resetQuestionbank();
        }

        if (resultChecker) { // inform user check exam result 
            System.out.println("Don't Forget to check Your Exam Result.");
        }
    }// end main

    public static int levelSelection() {
        int level = 0;
        boolean isCorrect = false;
        Scanner sc = new Scanner(System.in);

        // Define the level descriptions
        final String firstLevel = "Level 1 [10 Question]";
        final String secondLevel = "Level 2 [20 Question]";
        final String LastLevel = "Level 3 [30 Question]";

        // Loop until a correct level is selected
        while (!isCorrect) {
            // Print the difficulty levels
            System.out.printf("%15s\n", "[Difficulty level]");
            System.out.printf("1. %s\n", firstLevel);
            System.out.printf("2. %s\n", secondLevel);
            System.out.printf("3. %s\n", LastLevel);

            System.out.print("Please Select Level: ");
            String temp = sc.nextLine(); // Read the input from the user

            try {
                level = Integer.parseInt(temp); // Try to convert the input to an integer
            } catch (NumberFormatException e) {
                // If the input is not an integer, show a warning and continue the loop
                System.out.println();
                System.out.println("[System Warning]");
                System.out.println("Please Enter Integer Value!");
                space();
                continue; // Skip the rest of the loop and prompt the user again
            }

            // Check if the level is 0, which seems to be a special case for exiting
            if (level == 0) {
                return level;
            }

            // Check if the level is within the valid range (1 to 3)
            if (level <= 0 || level > 3) {
                // If not, show a warning and prompt the user again
                System.out.println("[System Warning]");
                System.out.println("Invalid Value, Please Enter the value between 1 to 3.");
                space();
            } else {
                isCorrect = true;  // Valid input, exit the loop
            }
        }

        return level;  // Return the selected level
    }// end levelSelection

    public static boolean dateFormatCheck(String date) {
        // Check if the length of the date string is exactly 10 characters
        if (date.length() != 10) {
            return false;
        }

        // Check if the 3rd and 6th characters are '/'
        if (date.charAt(2) != '/' || date.charAt(5) != '/') {
            return false;
        }

        // Split the date string into parts using '/' as the delimiter
        String[] parts = date.split("/");
        if (parts.length != 3) {
            return false;
        }

        // Assign the parts to day, month, and year variables
        String firstPart = parts[0];
        String secondPart = parts[1];
        String thirdPart = parts[2];

        int dayInt;
        int monthInt;
        int yearInt;

        // Try to parse the day, month, and year parts as integers
        try {

            dayInt = Integer.parseInt(firstPart);
            monthInt = Integer.parseInt(secondPart);
            yearInt = Integer.parseInt(thirdPart);

        } catch (NumberFormatException e) {
            return false;
        }

        // Check if the month is valid (1-12)
        if (monthInt < 1 || monthInt > 12) {
            return false;
        }

        // Check if the day is valid (1-31)
        if (dayInt < 1 || dayInt > 31) {
            return false;
        }

        // Special checks for February
        if (monthInt == 2) {
            if (isLeapYear(yearInt)) {
                if (dayInt > 29) {
                    System.out.println("[System Warning]");
                    System.out.println("Invalid date for February: " + date + ".");
                    return false;
                }
            } else {
                if (dayInt > 28) {
                    return false;
                }
            }
            // Special checks for months with 30 days
        } else if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11) {
            if (dayInt > 30) {
                return false;
            }
        }
        // Check if the year is non-negative
        if (yearInt < 0) {
            return false;
        }
        // If all checks pass, the date format is valid
        return true;
    }// end dateFormatCheck

    public static boolean isLeapYear(int year) {
        // Check if the year is divisible by 4
        if (year % 4 == 0) {
            // Check if the year is divisible by 100
            if (year % 100 == 0) {
                // Check if the year is divisible by 400
                if (year % 400 == 0) {
                    // If the year is divisible by 400, it is a leap year
                    return true;
                } else {
                    // If the year is divisible by 100 but not by 400, it is not a leap year
                    return false;
                }
            } else {
                // If the year is divisible by 4 but not by 100, it is a leap year
                return true;
            }
        } else {
            // If the year is not divisible by 4, it is not a leap year
            return false;
        }
    }// end isLeapyear

    public static String date() {
        boolean isCorrect = false; // Flag to control the loop until a correct date is provided
        String date = ""; // Initialize the date string
        Scanner sc = new Scanner(System.in);

        // Loop until a correct date is entered
        while (!isCorrect) {
            // Prompt the user to enter the exam date
            System.out.print("Please Enter Exam Date [dd/mm/yyyy]: ");
            date = sc.nextLine().trim();

            // Check if the user entered "0" to exit
            if (date.equals("0")) {
                return date; // Exit the method and return the date "0"
            }

            // Check if the entered date is in the correct format
            if (dateFormatCheck(date)) {
                isCorrect = true;
            } else {
                // Invalid date, prompt the user again with a warning
                space();
                System.out.println("[System Warning]");
                System.out.println("Invalid date format. Please enter date in [dd/mm/yyyy] format.");
                space();
            }
        }

        return date; // Return the valid date
    }// end date

    // Method to check if the return code (String) is "0"
    public static boolean checkReturnCode(String returnCode) {
        final String code = "0";
        return returnCode.equals(code);
    }// end checkReturnCode

    // Method to check if the return code (int) is "0"
    public static boolean checkReturnCode(int returnCode) {
        final int code = 0;
        return returnCode == code;
    }// end checkReturnCode

    public static String getStudentID() {

        Scanner sc = new Scanner(System.in);
        String studentID = ""; // Initialize the student ID string
        boolean isCorrect = false;  // Flag to control the loop until a correct ID is provided

        while (!isCorrect) {
            // Prompt the user to enter the student ID
            System.out.print("Enter your student ID: ");
            studentID = sc.nextLine().trim();

            // Check if the user entered "0" to exit
            if (studentID.equals("0")) {
                return studentID;
            }

            // Check if the student ID starts with 'P' or 'p'
            if (!studentID.startsWith("p") && !studentID.startsWith("P")) {
                space();
                System.out.println("[System Warning]");
                System.out.println("Invalid Student Id format, Must Start with P.");
                space();
                continue; // Prompt the user again
            }

            // Check if the student ID is 9 characters long and the remaining 8 characters are digits
            if (studentID.length() == 9 && studentID.substring(1).matches("\\d+")) {
                studentID = studentID.substring(0, 1).toUpperCase() + studentID.substring(1);
                isCorrect = true;  // Valid ID, exit the loop

            } else {
                // Invalid ID, prompt the user again with an error message
                space();
                System.out.println("[System Error]");
                System.out.println("Invalid Student ID, Please Try Again.");
                space();
            }
        }
        return studentID; // Return the valid student ID
    } // end getStudentID

    public static boolean confirmMessage(String message) {
        Scanner sc = new Scanner(System.in);
        char answer = ' ';
        do {
            // Display the confirmation message
            System.out.println("[System Information]");
            System.out.print(message + " [Y/N]: ");
            String userInput = sc.nextLine(); // Read and trim the input

            // Check if the input is 'Y' or 'N'
            if (userInput.equals("Y") || userInput.equals("N")) {
                answer = userInput.charAt(0);
            } else {
                // Invalid input, prompt the user again with a warning
                space();
                System.out.println("[System Warning]");
                System.out.println("Invalid Input, Please Enter 'Y' or 'N'.");
            }
            space();
        } while (answer != 'Y' && answer != 'N'); // Repeat until a valid input is received

        return answer == 'Y';  // Return true if the answer is 'Y', otherwise false
    }// end  confirmMessage

    public static int getUserOption() {
        Scanner sc = new Scanner(System.in);
        boolean validOption = false;
        int option = 0;

        // Loop until a valid option is entered
        while (!validOption) {
            space(); //spcae
            printLine(30); // Print a line of 30 dashes for formatting
            space();
            menu();  // Display the menu to the user
            printLine(30); // Print a line of 30 dashes for formatting
            space();
            System.out.print("Please Enter Option: ");

            String temp = sc.nextLine(); // Read the user's input

            try {
                option = Integer.parseInt(temp);  // Try to convert the input to an integer
            } catch (NumberFormatException e) {
                // If input is not a valid integer, catch the exception
                space();
                System.out.println("[System Warning]"); // Display a system warning message
                System.out.println("Please Enter Integer Value!");
                space();
                continue; // Skip the rest of the loop and prompt the user again
            }

            // Check if the input is within the valid range
            if ((option <= 0) || (option > 3)) {
                space();
                System.out.println("[System Warning]");
                System.out.println("Invalid Value, Please Enter the value between 1 to 3.");
                space();
            } else {
                space();
                validOption = true; // Set the flag to true to exit the loop
            }
        }

        return option; // Return the valid option entered by the user
    }// end getUserOption

    public static void loadFile(Controller controller) {
        final String file = "questionbanks.txt"; // Name of the file to load
        boolean exit = true; // Flag to determine if the system should exit

        try {
            space();
            System.out.println("[Loading the system...]");
            space();

            controller.loadQuestion(file); // Attempt to load the file using the controller
            exit = false;  // If loading is successful, set the flag to false
        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found
            System.out.println("[System Error]");  // Display a system error message
            System.out.println("Unable to Open Text File.");
            System.out.println("Please Make Sure You Have Imported The \"questionbanks.txt\" Text File Correctly, Please Try Again.");
            space();

        } catch (IOException e) {
            // Handle general I/O exceptions
            System.out.println("[System Error]"); // Display a system error message
            System.out.println("An Error Occurred While Reading The File. Please Try Again Later...");
        } finally {
            if (exit) {
                // If the exit flag is true, exit the system
                System.exit(0); // Terminate the program
            } else {
                // If the file was loaded successfully
                space();
                System.out.println("[Successfully Entered The System]"); // Inform the user that the system was successfully loaded
                space();
            }
        }
    }// end loadFile

    public static void menu() { //print menu
        System.out.println("Examination");
        System.out.println("1) Start Examination");
        System.out.println("2) Export Result");
        System.out.println("3) Exit");
    }// end menu

    public static void printLine(int size) { //print line
        for (int i = 0; i < size; i++) {
            System.out.print("-");
        }
    }// end printLine

    public static void space() { //spcae
        System.out.println();
    }//end space

    public static void loadingScreen() { // loading screen
        space();
        System.out.println("[System Loading..]");
        space();
    }// end loadingScreen

}// end MainDriver
