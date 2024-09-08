public class Question {
    private String questionText; // Holds the text of the question
    private String[] options; // Holds the four options for the question
    private char actualAnswer; // Holds the correct answer (a, b, c, or d)
    
    // Default constructor initializing default values
    public Question() {
        this.questionText = "";
        this.options = new String[4];
        this.actualAnswer = ' ';
    }
    
    // Parameterized constructor to initialize with specific values
    public Question(String questionText, String[] options, char actualAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.actualAnswer = actualAnswer;
    }
    
     // Getter for question text
    public String getQuestionText() {
        return questionText;
    }
    
    // Getter for options array
    public String[] getOptions() {
        return options;
    }
    
    // Getter for the correct answer
    public char getActualAnswer() {
        return actualAnswer;
    }
    
    // Setter for options array
    public void setOption(String[] option){
        this.options = option;
    }
    
     // Setter for question text
    public void setQuestionText(String questionText){
        this.questionText = questionText;
    }
    
    // Setter for the correct answer 
    public void setActuaAnsqer(char actualAnswer) {
        this.actualAnswer = actualAnswer;
    }
    
    // Override the toString method to print the question in a readable format
    @Override
    public String toString() {
        return questionText + "\nA. " + options[0] + "\nB. " + options[1] + "\nC. " + options[2] + "\nD. " + options[3];
    }
    
}// end question
