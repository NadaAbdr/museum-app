
// nada sulami

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Custom JPanel class to render a background image
class BackgroundPanel extends JPanel {

    private Image backgroundImage;
    private float transparency;

    public BackgroundPanel(String imagePath, float transparency) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
        this.transparency = transparency;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency)); // Set transparency
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g2d.dispose();
    }
}

//----------------------------------------------------------------------
public class MuseumApp extends JFrame {

    // Relative path or absolute path to the background image
    private static final String IMAGE_PATH = "C:\\Users\\Nada2\\OneDrive - King Abdullaziz University\\Desktop\\University\\8 term\\CPIT-305\\project_305\\project_305\\Project-CPIT-305\\project_305\\Museum.jpg";

    // Panels for different sections
    private JPanel visitorInfoPanel;
    private JPanel ticketPanel;
    private JPanel contactUsPanel;

    public MuseumApp() {
        initComponents();

    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Create a shared background panel for the main frame
        JPanel contentPanel = new BackgroundPanel(IMAGE_PATH, 0.1f); // Using 70% opacity for the background image
        contentPanel.setLayout(new BorderLayout());
        setContentPane(contentPanel); // Set this as the main content pane

        // Create components for navigation
        JButton visitorInfoButton = new JButton("Visitor Info");
        JButton ticketButton = new JButton("Your Ticket");
        JButton contactUsButton = new JButton("Contact Us");

        // Create a color for the buttons
        Color buttonColor = new Color(210, 181, 138); // Sand color

        // Configure button styles
        visitorInfoButton.setBackground(buttonColor);
        visitorInfoButton.setForeground(Color.black);
        visitorInfoButton.setBorderPainted(false);
        visitorInfoButton.setFocusPainted(false);

        ticketButton.setBackground(buttonColor);
        ticketButton.setForeground(Color.black);
        ticketButton.setBorderPainted(false);
        ticketButton.setFocusPainted(false);

        contactUsButton.setBackground(buttonColor);
        contactUsButton.setForeground(Color.black);
        contactUsButton.setBorderPainted(false);
        contactUsButton.setFocusPainted(false);

        // Create navigation panel to hold the buttons
        JPanel navigationPanel = new JPanel();
        navigationPanel.setOpaque(false); // Set panel to be transparent
        navigationPanel.setLayout(new FlowLayout());
        navigationPanel.add(visitorInfoButton);
        navigationPanel.add(ticketButton);
        navigationPanel.add(contactUsButton);

        // Create Visitor Info Panel
        visitorInfoPanel = new BackgroundPanel(IMAGE_PATH, 0.1f); // Reuse BackgroundPanel with the same image and transparency
        visitorInfoPanel.setLayout(new BoxLayout(visitorInfoPanel, BoxLayout.Y_AXIS));
        visitorInfoPanel.setOpaque(false);
        visitorInfoPanel.add(new JLabel("Full Name:"));
        visitorInfoPanel.add(new JTextField("Nada Alsulami"));
        visitorInfoPanel.add(new JLabel("Email:"));
        visitorInfoPanel.add(new JTextField("Nada2ss2@outlook.com"));
        visitorInfoPanel.add(new JLabel("Phone:"));
        visitorInfoPanel.add(new JTextField("+966555294493"));
        visitorInfoPanel.add(new JButton("Update Information"));
        visitorInfoPanel.add(new JButton("Back"));

        // Create Ticket Panel
        ticketPanel = new BackgroundPanel(IMAGE_PATH, 0.1f); // Reuse BackgroundPanel with the same image and transparency
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setOpaque(false);

        // Create labels and text fields for ticket information
        ticketPanel.add(new JLabel("Ticket Name:"));
        JTextField ticketNameField = new JTextField();
        ticketPanel.add(ticketNameField);
       // ticketNameField.setEditable(false); // Make it non-editable

        ticketPanel.add(new JLabel("Date:"));
        JTextField dateField = new JTextField();
        ticketPanel.add(dateField);

        ticketPanel.add(new JLabel("Time:"));
        JTextField timeField = new JTextField();
        ticketPanel.add(timeField);

        ticketPanel.add(new JLabel("Location:"));
        JTextField locationField = new JTextField();
        ticketPanel.add(locationField);

        ticketPanel.add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        ticketPanel.add(descriptionField);

        // Add the "Save Ticket" button
        JButton saveButton = new JButton("Save Ticket");
        saveButton.addActionListener(e -> {
            String ticketInfo = "Ticket Name: " + ticketNameField.getText()
                    + ", Date: " + dateField.getText()
                    + ", Time: " + timeField.getText()
                    + ", Location: " + locationField.getText()
                    + ", Description: " + descriptionField.getText();
            saveTicketInfo(ticketInfo);  // Call the method to save ticket information
        });
        ticketPanel.add(saveButton);

        // Add the "Display Tickets" button
        JButton displayButton = new JButton("Display Tickets");
        displayButton.addActionListener(e -> displayTicketInfo());
        ticketPanel.add(displayButton);

        // Add the "Cancel Ticket" button to delete ticket information
        JButton cancelButton = new JButton("Cancel Ticket");
        cancelButton.addActionListener(e -> clearTicketInfo());  // Attach clear method to the button
        ticketPanel.add(cancelButton);

        // Create Contact Us Panel
        contactUsPanel = new BackgroundPanel(IMAGE_PATH, 0.1f); // Reuse BackgroundPanel with the same image and transparency
        contactUsPanel.setLayout(new BoxLayout(contactUsPanel, BoxLayout.Y_AXIS));
        contactUsPanel.setOpaque(false);
        contactUsPanel.add(new JLabel("First Name:"));
        contactUsPanel.add(new JTextField());
        contactUsPanel.add(new JLabel("Last Name:"));
        contactUsPanel.add(new JTextField());
        contactUsPanel.add(new JLabel("Email:"));
        contactUsPanel.add(new JTextField());
        contactUsPanel.add(new JLabel("How can we help:"));
        contactUsPanel.add(new JTextField());
        JButton submitButton = new JButton("Submit");
        contactUsPanel.add(submitButton);

        // Add action listeners for navigation buttons
        visitorInfoButton.addActionListener(e -> showPanel(visitorInfoPanel));
        ticketButton.addActionListener(e -> showPanel(ticketPanel));
        contactUsButton.addActionListener(e -> showPanel(contactUsPanel));

        // Add action listener for submit button
        submitButton.addActionListener(e -> {
            String message = "Your message has been submitted!";
            JOptionPane.showMessageDialog(contentPanel, message);
        });

        // Add navigation panel to the main content panel
        contentPanel.add(navigationPanel, BorderLayout.NORTH);

        // Create a container panel with CardLayout to hold the different sections
        JPanel cardsPanel = new JPanel(new CardLayout());
        cardsPanel.setOpaque(false); // Set transparent background
        cardsPanel.add(visitorInfoPanel, "VisitorInfo");
        cardsPanel.add(ticketPanel, "TicketPanel");
        cardsPanel.add(contactUsPanel, "ContactUsPanel");

        // Add cardsPanel to the content panel
        contentPanel.add(cardsPanel, BorderLayout.CENTER);

        // Set initial visibility of panels using CardLayout
        showPanel(visitorInfoPanel); // Show Visitor Info panel by default

        // Set frame properties
        setTitle("Museum Application");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    public void clearTicketInfo() {
        try (FileWriter writer = new FileWriter("tickets.txt")) {  // Open in write mode to clear contents
            writer.write("");  // Write an empty string to clear the file
            JOptionPane.showMessageDialog(this, "Ticket information deleted successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting ticket information.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Helper method to show a specific panel
    private void showPanel(JPanel panelToShow) {
        visitorInfoPanel.setVisible(false);
        ticketPanel.setVisible(false);
        contactUsPanel.setVisible(false);
        panelToShow.setVisible(true);
    }

    // method to implemit ticket file
    public void saveTicketInfo(String ticketInfo) {
        try (FileWriter writer = new FileWriter("tickets.txt", true)) {  // Append mode
            writer.write(ticketInfo + "\n");
            JOptionPane.showMessageDialog(this, "Ticket information saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving ticket information.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void displayTicketInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tickets.txt"))) {
            StringBuilder allTickets = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                allTickets.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(this, allTickets.toString(), "Ticket Information", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error displaying ticket information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*public void loadTicketInfo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tickets.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                // Split and parse line to populate fields
                String[] ticketParts = line.split(", ");
                ticketNameField.setText(ticketParts[0].split(": ")[1]);
                dateField.setText(ticketParts[1].split(": ")[1]);
                timeField.setText(ticketParts[2].split(": ")[1]);
                locationField.setText(ticketParts[3].split(": ")[1]);
                descriptionField.setText(ticketParts[4].split(": ")[1]);
            } else {
                JOptionPane.showMessageDialog(this, "No ticket information available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading ticket information.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MuseumApp::new);
    }
}
