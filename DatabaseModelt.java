

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class DatabaseModelt {
    private Connection con;

    public DatabaseModelt(String username, String password) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/new_sc";
        String user = "root";
         password = "Nada-1234";
        this.con = DriverManager.getConnection(url, username, password);
    }

    // Method to create the database and tables
    public void initializeDatabase() throws SQLException {
        Statement st = null;
        try {
            st = con.createStatement();

            // Create database if it doesn't exist
            String database = "new_sc";
            
// Select the database
        st.executeUpdate("USE " + database);  // This will set the current database context

            // Create Exhibitions table if not exists
            String createExhibitionsTable = "CREATE TABLE IF NOT EXISTS Exhibitions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "date DATE, " +
                    "time TIME, " +
                    "location VARCHAR(255), " +
                    "description TEXT, " +
                    "capacity INT, " +
                    "available_tickets INT)";
            st.executeUpdate(createExhibitionsTable);

            // Create Events table if not exists
            String createEventsTable = "CREATE TABLE IF NOT EXISTS Events (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "date DATE, " +
                    "time TIME, " +
                    "location VARCHAR(255), " +
                    "description TEXT, " +
                    "capacity INT, " +
                    "available_tickets INT)";
            st.executeUpdate(createEventsTable);

            // Insert initial data if necessary
            String insertExhibitionData = "INSERT INTO Exhibitions (name, date, time, location, description, capacity, available_tickets) " +
                    "SELECT 'Saudi Art Exhibition', '2024-10-01', '10:00:00', 'Riyadh', 'A showcase of contemporary Saudi art.', 100, 50 " +
                    "FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM Exhibitions WHERE name = 'Saudi Art Exhibition');";
            st.executeUpdate(insertExhibitionData);
             // Insert initial data if necessary
           String insertExhibitionData2 = "INSERT INTO Exhibitions (name, date, time, location, description, capacity, available_tickets) " +
                    "SELECT 'History of Saudi Arabia', '2024-10-15', '11:00:00', 'Jeddah', 'Explore the rich history of Saudi Arabia.', 150, 100 " +
                    "FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM Exhibitions WHERE name = 'History of Saudi Arabia');";
st.executeUpdate(insertExhibitionData2);
//----------------------
String insertExhibitionData3 = "INSERT INTO Exhibitions (name, date, time, location, description, capacity, available_tickets) " +
                    "SELECT 'Science and Technology Expo', '2024-10-20', '12:00:00', 'Dhahran', 'Discover innovative technologies and their impacts.', 200, 150 " +
                    "FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM Exhibitions WHERE name = 'Science and Technology Expo');";
st.executeUpdate(insertExhibitionData3);


//----------------------------------------------------------------
            String insertEventData = "INSERT INTO Events (name, date, time, location, description, capacity, available_tickets) " +
                    "SELECT 'Saudi National Day Celebration', '2024-09-23', '17:00:00', 'King Abdulaziz Park, Riyadh', 'A grand celebration of Saudi National Day.', 500, 300 " +
                    "FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM Events WHERE name = 'Saudi National Day Celebration');";
            st.executeUpdate(insertEventData);
            //---
            // Insert data into Events (second event)
String insertEventData2 = "INSERT INTO Events (name, date, time, location, description, capacity, available_tickets) " +
                "SELECT 'International Book Fair', '2024-11-01', '09:00:00', 'Jeddah', 'A fair showcasing local and international books.', 1000, 700 " +
                "FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM Events WHERE name = 'International Book Fair');";
st.executeUpdate(insertEventData2);

// Insert data into Events (third event)
String insertEventData3 = "INSERT INTO Events (name, date, time, location, description, capacity, available_tickets) " +
                "SELECT 'Cultural Festival', '2024-10-10', '15:00:00', 'Dhahran', 'Explore Saudi culture through art and food.', 300, 150 " +
                "FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM Events WHERE name = 'Cultural Festival');";
st.executeUpdate(insertEventData3);

            //----

        } finally {
            // Ensure that the statement is closed after use
            if (st != null) {
                st.close();
            }
        }
    }

    // Fetch all Exhibitions
    public List<String[]> getExhibitions() throws SQLException {
        List<String[]> exhibitions = new ArrayList<>();
        String fetchExhibitionsData = "SELECT * FROM Exhibitions";

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(fetchExhibitionsData)) {
            while (rs.next()) {
                exhibitions.add(new String[]{
                        rs.getString("name"),
                        rs.getDate("date").toString(),
                        rs.getTime("time").toString(),
                        rs.getString("location"),
                        rs.getString("description"),
                        String.valueOf(rs.getInt("capacity")),
                        String.valueOf(rs.getInt("available_tickets"))
                });
            }
        }
        return exhibitions;
    }

    // Fetch all Events
    public List<String[]> getEvents() throws SQLException {
        List<String[]> events = new ArrayList<>();
        String fetchEventsData = "SELECT * FROM Events";

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(fetchEventsData)) {
            while (rs.next()) {
                events.add(new String[]{
                        rs.getString("name"),
                        rs.getDate("date").toString(),
                        rs.getTime("time").toString(),
                        rs.getString("location"),
                        rs.getString("description"),
                        String.valueOf(rs.getInt("capacity")),
                        String.valueOf(rs.getInt("available_tickets"))
                });
            }
        }
        return events;
    }
   public void updateAvailableTickets(String type, String itemName, int newAvailableTickets) throws SQLException {
    String tableName = type.equals("Events") ? "events" : "exhibitions";  // Set the table based on type
    String query = "UPDATE " + tableName + " SET available_tickets = ? WHERE name = ?";

    try (PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setInt(1, newAvailableTickets);  // Set the new available tickets
        stmt.setString(2, itemName);          // Set the event/exhibition name

        // Debugging: Print the values being passed to the query
        System.out.println("Updating tickets for " + type + ": " + itemName);
        System.out.println("New available tickets: " + newAvailableTickets);

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println(type + " tickets updated successfully!");
        } else {
            System.out.println("No " + type + " found with that name.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new SQLException("Error updating available tickets for " + type + ".");
    }
}

    // Close the connection when done
    public void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
    // Method to update available tickets for a specific exhibition
/*public void updateAvailableTicketsForExhibition(String exhibitionName, int newAvailableTickets) throws SQLException {
    String query = "UPDATE Exhibitions SET available_tickets = ? WHERE name = ?";

    try (PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setInt(1, newAvailableTickets);  // Set the new available tickets
        stmt.setString(2, exhibitionName);    // Set the exhibition name

        // Debugging: Print the values being passed to the query
        System.out.println("Updating tickets for exhibition: " + exhibitionName);
        System.out.println("New available tickets: " + newAvailableTickets);
        System.out.println("Executing query: " + query);  // Print the query to check

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Tickets updated successfully for exhibition!");
        } else {
            System.out.println("No exhibition found with that name.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        throw new SQLException("Error updating available tickets for exhibition.");
    }
}*/
    
   


}
