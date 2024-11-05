package org.example.GUI;

import org.example.BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    private final JTextField[] queueTextFields;
    private final JComboBox<String> strategyComboBox;
    public JTextField[] getQueueTextFields() {
        return queueTextFields;
    }
    public JComboBox<String> getStrategyComboBox() {
        return strategyComboBox;
    }
    public Gui(String title) {
        super(title);
        Controller controller = new Controller(this);
        JButton button = new JButton("Start");
        this.strategyComboBox = new JComboBox<>(new String[]{"Minimum time strategy", "Minimum length strategy"});

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button.addActionListener(controller);
        this.strategyComboBox.addActionListener(controller);
        button.setActionCommand("Start");
        this.strategyComboBox.setActionCommand("Switch strategies");

        JPanel contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel[] queueLabels = new JLabel[7];
        queueTextFields = new JTextField[7];


        String[] labels = {"Number of clients:", "Number of queues:",
                "Maximum arrival time:", "Minimum arrival time:", "Maximum service time:",
                "Minimum service time:", "Simulation Interval:"};


        for (int i = 0; i < 7; i++) {
            JLabel label = new JLabel(labels[i]);
            JTextField textField = new JTextField(20);
            queueLabels[i] = label;
            queueTextFields[i] = textField;

            gbc.gridx = 2 * (i % 2);
            gbc.gridy = i / 2;
            gbc.anchor = GridBagConstraints.EAST;
            contentPane.add(label, gbc);

            gbc.gridx = 2 * (i % 2) + 1;
            gbc.anchor = GridBagConstraints.WEST;
            contentPane.add(textField, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(button, gbc);

        gbc.gridy = 5;
        contentPane.add(strategyComboBox, gbc);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPane, BorderLayout.CENTER);

        mainPanel.setPreferredSize(new Dimension(750, 500));

        setContentPane(mainPanel);

        pack();
        setVisible(true);
    }
}
