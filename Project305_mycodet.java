
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class Project305_mycodet {

    public static void main(String[] args) {
        try {
            // Initialize the model (database operations)
            DatabaseModelt dbModel = new DatabaseModelt("root", "Nada-1234");

            // Initialize the view (frame creation)
            DatabaseViewt dbView = new DatabaseViewt();

            // Initialize database (create tables and insert data)
            dbModel.initializeDatabase();

            // Create the main frame
            JFrame mainFrame = new JFrame("Museum Detector System");
            mainFrame.setSize(600, 400);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(null);

            // Create a content panel with a background image and transparency
            JPanel contentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon backgroundImage = new ImageIcon("Museum.jpg");
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 70% opacity
                    g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                }
            };
            contentPanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());
            mainFrame.setContentPane(contentPanel);
            contentPanel.setLayout(null);

            // Define a button color
            Color buttonColor = new Color(210, 181, 138);  // Sand color

            // Create buttons for navigation and set their properties
            JButton exhibitionsButton = new JButton("Exhibitions");
            exhibitionsButton.setBounds(100, 200, 150, 50);
            exhibitionsButton.setBackground(buttonColor);
            exhibitionsButton.setForeground(Color.black);
            exhibitionsButton.setBorderPainted(false);
            exhibitionsButton.setFocusPainted(false);

            JButton eventsButton = new JButton("Events");
            eventsButton.setBounds(300, 200, 150, 50);
            eventsButton.setBackground(buttonColor);
            eventsButton.setForeground(Color.black);
            eventsButton.setBorderPainted(false);
            eventsButton.setFocusPainted(false);

            JButton loginButton = new JButton("Log In");
            loginButton.setBounds(200, 300, 150, 50);
            loginButton.setBackground(buttonColor);
            loginButton.setForeground(Color.black);
            loginButton.setBorderPainted(false);
            loginButton.setFocusPainted(false);

            // Add action listeners to the buttons
            exhibitionsButton.addActionListener(e -> {
                try {
                    List<String[]> exhibitions = dbModel.getExhibitions();
                    dbView.showExhibitionsFrame(exhibitions, dbModel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            eventsButton.addActionListener(e -> {
                try {
                    List<String[]> events = dbModel.getEvents();
                    dbView.showEventsFrame(events, dbModel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            loginButton.addActionListener(e -> {

                SwingUtilities.invokeLater(() -> {
                    LoginWithForm loginForm = new LoginWithForm();
                    CountdownWindow countdown = new CountdownWindow(90);
                    countdown.setLocation(loginForm.getX() + loginForm.getWidth() + 10, loginForm.getY());
                });
            });

            // Add buttons to the content panel
            contentPanel.add(exhibitionsButton);
            contentPanel.add(eventsButton);
            contentPanel.add(loginButton);

            // Set the frame visible
            mainFrame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error initializing database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
