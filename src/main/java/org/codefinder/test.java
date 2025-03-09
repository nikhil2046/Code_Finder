package org.codefinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test {

    public static void main(String[] args) {
        // Create the JFrame
        JFrame frame = new JFrame("Code Finder Utility");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        // Set a custom font and color for the title
        JLabel titleLabel = new JLabel("Code Finder Utility", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Set text color to white
        titleLabel.setOpaque(true); // Make the background color visible
        titleLabel.setBackground(new Color(0x1E90FF)); // Set background color to a shade of blue

        // Create a panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Ensure panel bounds cover entire JFrame
        panel.add(titleLabel, BorderLayout.NORTH);

        // Add some padding to the main content area
        JPanel paddingPanel = new JPanel(new GridBagLayout());
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a custom button with rounded corners
        JButton customButton = new JButton("Search Code") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // No-op
            }
        };
        customButton.setFont(new Font("Arial", Font.BOLD, 16));
        customButton.setForeground(Color.WHITE); // Set button text color to white
        customButton.setBackground(new Color(0x1E90FF)); // Set button background color to a shade of blue
        customButton.setFocusPainted(false); // Remove focus border around the button
        customButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding to the button

        // Create an indeterminate progress bar (spinner)
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Make the progress bar indeterminate (spinning)
        progressBar.setVisible(false); // Initially hide the progress bar

        // Create an animated label
        JLabel animatedLabel = new JLabel("In progress", JLabel.CENTER);
        animatedLabel.setFont(new Font("Arial", Font.BOLD, 16));
        animatedLabel.setForeground(Color.DARK_GRAY); // Set text color

        // Add an action listener to the button to show the progress bar and animated label when clicked
        customButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progressBar.setVisible(true);
                animatedLabel.setVisible(true);
                // Simulate a task by using a SwingWorker
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Simulate a long-running task (e.g., generating a report)
                        Thread.sleep(3000); // Sleep for 3 seconds
                        return null;
                    }

                    @Override
                    protected void done() {
                        progressBar.setVisible(false); // Hide the progress bar when done
                        animatedLabel.setVisible(false); // Hide the animated label when done
                        JOptionPane.showMessageDialog(frame, "Report generated successfully!");
                    }
                };
                worker.execute();

                // Create a Timer to animate the label text
                Timer timer = new Timer(500, new ActionListener() {
                    private int dotCount = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dotCount = (dotCount + 1) % 4;
                        String dots = new String(new char[dotCount]).replace("\0", ".");
                        animatedLabel.setText("In progress" + dots);
                    }
                });
                timer.start();

                // Stop the Timer when the task is done
                worker.addPropertyChangeListener(evt -> {
                    if (worker.isDone()) {
                        timer.stop();
                    }
                });
            }
        });

        // Add the custom button, progress bar, and animated label to the padding panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding around the components
        gbc.gridx = 0;
        gbc.gridy = 0;
        paddingPanel.add(customButton, gbc);

        gbc.gridy = 1;
        paddingPanel.add(progressBar, gbc);

        gbc.gridy = 2;
        paddingPanel.add(animatedLabel, gbc);
        animatedLabel.setVisible(false); // Initially hide the animated label

        panel.add(paddingPanel, BorderLayout.CENTER);

        // Add the panel to the JFrame
        frame.add(panel);

        // Set the JFrame visibility to true
        frame.setVisible(true);
    }
}
