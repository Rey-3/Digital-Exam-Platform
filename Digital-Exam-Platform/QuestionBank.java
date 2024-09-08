    import java.io.BufferedReader;
    import java.io.FileNotFoundException;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.Random;

    public class QuestionBank {

        private Question[] question;
        private String title;

        public QuestionBank(String title) { //constructor
            this.question = new Question[41];
            this.title = title;
        }

        public void setTitle(String title) { //set the title
            this.title = title;
        } // end setTitle

        public String getTitle() { // get the title
            return title;
        }// end getTitle

        public void loadQuestion(String file) throws FileNotFoundException, IOException {
            BufferedReader br = null; // Initialize BufferedReader

            br = new BufferedReader(new FileReader(file));  // Create BufferedReader to read the file
            String line; // Variable to hold each line read from the file
            StringBuilder questionText = new StringBuilder();  // StringBuilder to accumulate question text
            String[] options = new String[4]; // Array to hold the four options, a,b,c,d
            char actualAnswer = ' ';  // Variable to hold the actual answer
            int optionIndex = 0;  // Index for the options array
            int questionCount = 0;  // Counter for the number of questions

            while ((line = br.readLine()) != null) { // Read each line from the file
                line = line.trim(); // Trim leading and trailing whitespace

                 // Check if the line matches the pattern for options
                if (line.matches("[a-d]\\.\\s*.*") || line.matches("\\*[a-d]\\.\\s*.*")) {

                    if (line.charAt(0) == '*') {  // Check if the option is marked as the actual answer
                        actualAnswer = line.charAt(1); // Set the actual answer
                        options[optionIndex++] = line.substring(3).trim(); // Add the option to the array
                    } else {
                        options[optionIndex++] = line.substring(2).trim(); // Add the option to the array
                    }
                    if (optionIndex == 4) { // Check if all four options are read
                        question[questionCount++] = new Question(questionText.toString().trim(), options, actualAnswer);
                        questionText.setLength(0); // Reset question text
                        options = new String[4]; // Reset options
                        optionIndex = 0; // Reset option index

                    }
                } else {
                    questionText.append(line).append("\n"); // Append the line to the question text
                }

            }
            br.close();  // Close the BufferedReader

        }// end loadQuestion

        public Question[] getQuestions(int size) {

            Question[] randomQuestion = new Question[size];
            int[] randomNumber = randomNumber(size);  // Generate an array of random numbers

            // Check if the requested size is greater than the available questions in the ban
            if (size > getQuestionBankSize()) {
                System.out.println();
                System.out.println("[System Error]");
                System.out.println("Please Try Again Later...");
                System.out.println();
                return randomQuestion;
            }

            // Populate the randomQuestion array with questions selected based on the random numbers
            for (int i = 0; i < size; i++) {
                randomQuestion[i] = question[randomNumber[i]];
            }

            return randomQuestion;
        }// end getQuestions

        public int[] randomNumber(int size) {
            Random rd = new Random();  // Create a Random object to generate random numbers
            int[] randomNo = new int[size];  // Initialize an array to hold the random numbers
            boolean isUnique = false;

             // Generate random numbers ensuring they are unique
            for (int i = 0; i < size; i++) {
                randomNo[i] = rd.nextInt(getQuestionBankSize()); // Generate a random number within the range of question bank size
                isUnique = false;

                 // Check if the generated number is unique
                for (int checker = 0; checker < i; checker++) {
                    if (randomNo[i] == randomNo[checker]) {
                        isUnique = true;
                        break;
                    }
                }

                 // If the number is not unique, decrement the counter to retry
                if (isUnique) {
                    i--;
                }
            }

            return randomNo;
        }// end randomNumber

        public void showQuestionDetails() {

            if (question.length < 0) {
                System.out.println("The Question Bank is Empty.");
            }

            System.out.println("Title: " + title);
            System.out.println("Total Questions: " + getQuestionBankSize()); // show the total question and title

        }// end showQuestionDetails

        public void resetQuestionbank() {
            this.question = null; // reset the question bank
            this.title = null;
        }// end resetQuestionbank

        public void viewQuestion() {

            if (question.length < 3) { // check if question bank more than 3 question
                System.out.println();
                System.out.println("[System Warning]");
                System.out.println("The QuestionBank is Less Than 3 Question.");
                return;
            }

            int[] randomNumber = randomNumber(question.length);  // Generate a random number 

            for (int i = 0; i < question.length; i++) { //print out 
                
                for(int j=0 ; j <3; j++){
                    System.out.print((i + 1) + ". ");
                    System.out.println(question[randomNumber[i]]);
                    System.out.println();
                }
                
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
            }

        }// end viewQuestion

        protected int getQuestionBankSize() { //return question size
            return question.length;
        }// end getQuestionBankSize

    }//end QuestionBank
