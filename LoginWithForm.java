import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginWithForm extends JFrame {

    private JTextField usernameField, emailField, phoneField, ageField, visitDateField;

    public LoginWithForm() {
        setTitle("Login Form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color bgColor = new Color(240, 230, 220); 
        Color btnColor = new Color(181, 136, 99); 
        Color textColor = Color.BLACK; 

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(bgColor);

        usernameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        ageField = new JTextField();
        visitDateField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(btnColor);
        submitButton.setForeground(textColor);

        formPanel.add(createLabel("Name:", textColor));
        formPanel.add(usernameField);

        formPanel.add(createLabel("Email:", textColor));
        formPanel.add(emailField);

        formPanel.add(createLabel("Phone (10 digits):", textColor));
        formPanel.add(phoneField);

        formPanel.add(createLabel("Age:", textColor));
        formPanel.add(ageField);

        formPanel.add(createLabel("Visit Date (YYYY-MM-DD):", textColor));
        formPanel.add(visitDateField);

        formPanel.add(new JLabel()); 
        formPanel.add(submitButton);

        add(formPanel, BorderLayout.CENTER);
        getContentPane().setBackground(bgColor);

        submitButton.addActionListener(e -> handleSubmit());

        setVisible(true);
    }

    private JLabel createLabel(String text, Color textColor) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        return label;
    }

    private void handleSubmit() {
        String name = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String ageText = ageField.getText();
        String visitDate = visitDateField.getText();

        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number. Please enter exactly 10 digits.");
            return;
        }

      
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid age. Please enter a valid number.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/museum_db";
        String user = "root";
        String password = "Njood_2030";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO visitors (name, email, phone, age, visit_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setInt(4, age);
            statement.setString(5, visitDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Visit registered successfully!");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.");
        }
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWithForm::new);
    }
}
