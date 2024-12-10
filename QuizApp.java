import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Main class for the Quiz Application
public class QuizApp extends JFrame {
    // Fields for username and password input
    private JTextField usernameField;
    private JPasswordField passwordField;

    // Array to store correct answers
    private String[] correctAnswers = {
            "Game development",
            "Bytecode and JVM",
            "By using classes and objects",
            "Within classes",
            "Declared with data types"
    };

    // Constructor for the QuizApp class
    public QuizApp() {
        setTitle("Admission Entrance Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Title label for the login page
        JLabel titleLabel = new JLabel("Welcome to the Java Quiz");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Panel for username and password input
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.setBackground(Color.LIGHT_GRAY);

        // Labels and fields for username and password
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.GREEN);
        loginButton.setForeground(Color.WHITE);

        // Adding components to the login panel
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        add(loginPanel, BorderLayout.CENTER);

        // ActionListener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Checking username and password
                if (username.equals("Deeraj") && password.equals("1234")) {
                    openMCQPage(); // Open MCQ page if credentials are correct
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Try again.");
                }
            }
        });

        setVisible(true);
    }

    // Method to open the Multiple Choice Questions (MCQ) page
    private void openMCQPage() {
        JFrame mcqFrame = new JFrame("Java Quiz");
        mcqFrame.setSize(700, 500);
        mcqFrame.setLocationRelativeTo(null);
        mcqFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mcqPanel = new JPanel();
        mcqPanel.setLayout(new BoxLayout(mcqPanel, BoxLayout.Y_AXIS));

        // Title label for MCQ page
        JLabel titleLabel = new JLabel("Simple Java Quiz");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mcqPanel.add(titleLabel);

        // Adding MCQs to the panel
        addMCQ(mcqPanel,
                       "Question 1: What is Java primarily used for?",
                       new String[] {
                               "Web development",
                               "Game development",
                               "Scripting language",
                               "Mobile apps" });
       
               addMCQ(mcqPanel, "Question 2: What makes Java platform-independent?",
                       new String[] {
                               "Dynamic typing",
                               "Open-source nature",
                               "Platform-dependent libraries",
                               "Bytecode and JVM" });
       
               addMCQ(mcqPanel, "Question 3: How does Java handle web development?",
                       new String[] {
                               "By using classes and objects",
                               "By interpreting HTML code",
                               "By using Python scripts",
                               "By compiling web pages" });
       
               addMCQ(mcqPanel, "Question 4: Where are Java methods defined?",
                       new String[] {
                               "Within classes",
                               "Outside classes",
                               "Inheritance hierarchy",
                               "Database tables" });
       
               addMCQ(mcqPanel, "Question 5: What is a characteristic of Java variables?",
                       new String[] {
                               "Declared with data types",
                               "Dynamically typed",
                               "Unlimited size",
                               "Constant values" });


        // JScrollPane for scrolling MCQs
        JScrollPane scrollPane = new JScrollPane(mcqPanel);
        mcqFrame.add(scrollPane);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        mcqPanel.add(submitButton);

        // ActionListener for submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int score = calculateScore(mcqPanel);
                String username = usernameField.getText();
                JOptionPane.showMessageDialog(null, "Your score: " + score);
                storeScoreInDatabase(username, score); // Store score in database
            }
        });

        mcqFrame.setVisible(true);
        setVisible(false);
    }

    // Method to add a single MCQ to the panel
    private void addMCQ(JPanel panel, String question, String[] options) {
        JLabel questionLabel = new JLabel(question);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(questionLabel);
        ButtonGroup group = new ButtonGroup();
        for (String option : options) {
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.setFont(new Font("Arial", Font.PLAIN, 12));
            group.add(radioButton);
            panel.add(radioButton);
        }
    }

    // Method to calculate score based on selected answers
    private int calculateScore(JPanel panel) {
        int score = 0;
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JRadioButton) {
                JRadioButton radioButton = (JRadioButton) component;
                if (radioButton.isSelected() && isCorrectAnswer(radioButton.getText())) {
                    score++;
                }
            }
        }
        return score;
    }

    // Method to check if the selected answer is correct
    private boolean isCorrectAnswer(String selectedAnswer) {
        for (String correctAnswer : correctAnswers) {
            if (selectedAnswer.equals(correctAnswer)) {
                return true;
            }
        }
        return false;
    }

    // Method to store score in the database
    private void storeScoreInDatabase(String username, int score) {
        String url = "jdbc:mysql://localhost:3306/studentMarks";
        String user = "root";
        String password = "Deeraj@8898";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String checkTableExistsSQL = "SHOW TABLES LIKE 'scores'";
            PreparedStatement checkTableExistsStatement = connection.prepareStatement(checkTableExistsSQL);
            ResultSet resultSet = checkTableExistsStatement.executeQuery();
            if (!resultSet.next()) {
                String createTableSQL = "CREATE TABLE scores (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255), score INT)";
                Statement createTableStatement = connection.createStatement();
                createTableStatement.executeUpdate(createTableSQL);
            }
            String insertScoreSQL = "INSERT INTO scores (username, score) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertScoreSQL);
            insertStatement.setString(1, username);
            insertStatement.setInt(2, score);
            insertStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, username + " Your score has successfully submitted", "System Message",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApp();
            }
        });
    }
}
