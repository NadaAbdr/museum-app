import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CountdownWindow extends JFrame {
    private JLabel timerLabel;
    private int countdownTime; 
    private Timer timer; 

    public CountdownWindow(int countdownTime) {
        this.countdownTime = countdownTime;

        
        setTitle("Countdown Timer");
        setSize(200, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        
        timerLabel = new JLabel("Time Left: " + countdownTime + "s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        timerLabel.setForeground(new Color(80, 50, 30));
        add(timerLabel, BorderLayout.CENTER);

       
        startCountdown();

        setVisible(true);
    }

    private void startCountdown() {
        timer = new Timer(1000, e -> {
            countdownTime--;
            timerLabel.setText("Time Left: " + countdownTime + "s");

            if (countdownTime <= 0) {
                timer.stop(); 
                JOptionPane.showMessageDialog(this, "Time's up!");
                closeAllWindows(); 
            }
        });

        timer.start();
    }

    private void closeAllWindows() {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            window.dispose(); 
        }

        dispose();
    }

    public static void main(String[] args) {
        new CountdownWindow(90); 
    }
}

