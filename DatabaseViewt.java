
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseViewt {

    // Method to display exhibitions frame
    public void showExhibitionsFrame(List<String[]> exhibitions, DatabaseModelt dbModel) {
        showGenericFrame(exhibitions, dbModel, "Exhibitions");
    }

    // Method to display events frame
    public void showEventsFrame(List<String[]> events, DatabaseModelt dbModel) {
        showGenericFrame(events, dbModel, "Events");
    }
   private void showGenericFrame(List<String[]> data, DatabaseModelt dbModel, String type) {
    String[] columns = {"Name", "Date", "Time", "Location", "Description", "Capacity", "Available Tickets"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);
    for (String[] row : data) {
        model.addRow(row);
    }
    JTable table = new JTable(model);
    JFrame frame = new JFrame(type);
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    // Buttons for various actions
    JButton viewDetailsButton = createButton("View Details");
    JButton reserveTicketButton = createButton("Reserve Ticket");
    JButton trackLocationButton = createButton("Track Location");
    trackLocationButton.addActionListener(e -> handleTrackLocation(table, frame));

    // Action listeners for buttons
    viewDetailsButton.addActionListener(e -> handleViewDetails(table, frame, columns));
    reserveTicketButton.addActionListener(e -> {
        try {
            reserveTicket(table, frame, dbModel, type);  // Reserve ticket dynamically based on type
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseViewt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseViewt.class.getName()).log(Level.SEVERE, null, ex);
        }
    });
    trackLocationButton.addActionListener(e -> handleTrackLocation(table, frame));

    // Add components to the frame
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(viewDetailsButton);
    buttonPanel.add(reserveTicketButton);
    buttonPanel.add(trackLocationButton);
    frame.add(new JScrollPane(table), BorderLayout.CENTER);
    frame.add(buttonPanel, BorderLayout.SOUTH);
    frame.setVisible(true);
}

    // Helper to create buttons with styling
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(210, 181, 138));  // Sand color
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

 

    // Method to handle View Details
    private void handleViewDetails(JTable table, JFrame parentFrame, String[] columns) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            StringBuilder details = new StringBuilder();
            for (int i = 0; i < columns.length; i++) {
                details.append(columns[i]).append(": ")
                        .append(table.getValueAt(selectedRow, i)).append("\n");
            }
            showDetailsFrame("Details", details.toString());
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Please select an entry.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to handle Track Location
  // Updated: tracking location 
    private void handleTrackLocation(JTable table, JFrame parentFrame) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String location = (String) table.getValueAt(selectedRow, 3); // Get location from table
            if (location == null || location.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid location data.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Selected location: " + location); // Debugging log

            SwingUtilities.invokeLater(() -> {
                try {
                    OpenMapWindow mapWindow = new OpenMapWindow();
                    mapWindow.showMapWithLocation(location); // Pass location to map
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame, "Failed to open map: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Please select an entry.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Method to display details frame
    private void showDetailsFrame(String title, String details) {
        JFrame detailsFrame = new JFrame(title);
        detailsFrame.setSize(400, 300);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea detailsTextArea = new JTextArea(details);
        detailsTextArea.setWrapStyleWord(true);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setEditable(false);

        detailsFrame.add(new JScrollPane(detailsTextArea));
        detailsFrame.setVisible(true);
    }

    // Method to display location frame
    private void showLocationFrame(String location) {
        JFrame locationFrame = new JFrame("Location Tracker");
        locationFrame.setSize(400, 300);
        locationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel locationLabel = new JLabel("Tracking Location: " + location);
        locationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        locationFrame.add(locationLabel);
        locationFrame.setVisible(true);
    }
    // Updated: Generalized method to handle ticket reservation
    private void reserveTicket(JTable table, JFrame parentFrame, DatabaseModelt dbModel, String type) throws SQLException, IOException {
        int selectedRow = table.getSelectedRow();  // For both exhibitions and events

        if (selectedRow != -1) {
            // Retrieve the available tickets as String from the table
            Object availableTicketsValue = table.getValueAt(selectedRow, 6);

            // Check if the value is valid and parse it
            if (availableTicketsValue == null || availableTicketsValue.toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "No available tickets data found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int availableTickets = Integer.parseInt(availableTicketsValue.toString());

                if (availableTickets > 0) {
                    // Reduce available tickets in the table
                    table.setValueAt(String.valueOf(availableTickets - 1), selectedRow, 6);
                    JOptionPane.showMessageDialog(parentFrame, "Ticket reserved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Update the available tickets in the database
                    dbModel.updateAvailableTickets(type, (String) table.getValueAt(selectedRow, 0), availableTickets - 1);

                    // Save the reserved ticket details to a file
                    // NEW FEATURE: Save to tickets.txt
                    String ticketDetails = type + ": " + table.getValueAt(selectedRow, 0)
                            + ", Date: " + table.getValueAt(selectedRow, 1)
                            + ", Time: " + table.getValueAt(selectedRow, 2)
                            + ", Location: " + table.getValueAt(selectedRow, 3)
                            + ", Description: " + table.getValueAt(selectedRow, 4);

                    try (FileWriter writer = new FileWriter("tickets.txt", true)) { // Append mode
                        writer.write(ticketDetails + "\n"); // Save ticket details
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(parentFrame, "Error saving ticket details.", "Error", JOptionPane.ERROR_MESSAGE);
                        ioException.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(parentFrame, "No tickets available!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid number of available tickets.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Please select an " + type + ".", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    

}
