package org.example.GUI;

import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationFrame extends JFrame {
    public JPanel contentPane;
    public JPanel information;
    public JPanel deskPanel;
    public JPanel leftPanel;
    public JTextField timePassedField;
    public JTextField peakHourField;
    public JTextField avgServiceField;
    public JTextField avgWaitingField;
    private int maxclients;
    public int peakHour;
    public SimulationFrame(String title, List<Task> generatedTasks, int numberOfServers,List<Server> servers) {
        super(title);
        maxclients=0;
        peakHour=0;
        contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(1200, 700));
        contentPane.setLayout(new BorderLayout());

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(leftPanel);

        for (Task task : generatedTasks) {
            JPanel taskPanel = new JPanel();
            taskPanel.setPreferredSize(new Dimension(150, 60));
            taskPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            taskPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskPanel.setBackground(Color.red);

            JLabel arrivalLabel = new JLabel("Arrival Time: " + task.getArrivalTime());
            JLabel serviceLabel = new JLabel("Service Time: " + task.getServiceTime());

            taskPanel.add(arrivalLabel);
            taskPanel.add(serviceLabel);

            leftPanel.add(taskPanel);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contentPane.add(scrollPane, BorderLayout.WEST);

        information = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel avgWaitingLabel = new JLabel("Average Waiting Time:");
        avgWaitingField = new JTextField();
        avgWaitingField.setEditable(false);
        avgWaitingField.setPreferredSize(new Dimension(150, avgWaitingField.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 0;
        information.add(avgWaitingLabel, gbc);

        gbc.gridx = 1;
        information.add(avgWaitingField, gbc);

        JLabel avgServiceLabel = new JLabel("Average Service Time:");
        avgServiceField = new JTextField();
        avgServiceField.setEditable(false);
        avgServiceField.setPreferredSize(new Dimension(150, avgServiceField.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 1;
        information.add(avgServiceLabel, gbc);

        gbc.gridx = 1;
        information.add(avgServiceField, gbc);

        JLabel peakHourLabel = new JLabel("Peak Hour:");
        peakHourField = new JTextField();
        peakHourField.setEditable(false);
        peakHourField.setPreferredSize(new Dimension(150, peakHourField.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 2;
        information.add(peakHourLabel, gbc);

        gbc.gridx = 1;
        information.add(peakHourField, gbc);

        JLabel timePassedLabel = new JLabel("Current time:");
        timePassedField = new JTextField();
        timePassedField.setEditable(false);
        timePassedField.setPreferredSize(new Dimension(150, timePassedField.getPreferredSize().height));
        gbc.gridx = 0;
        gbc.gridy = 3;
        information.add(timePassedLabel, gbc);

        gbc.gridx = 1;
        information.add(timePassedField, gbc);

        contentPane.add(information, BorderLayout.SOUTH);

        deskPanel = new DeskPanel(numberOfServers, servers);
        JScrollPane deskScrollPane = new JScrollPane(deskPanel);
        deskScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        contentPane.add(deskScrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        pack();
        setVisible(true);
    }

    public void update(List<Task> generatedTasks) {
        leftPanel.removeAll();
        deskPanel.removeAll();
        for (Task task : generatedTasks) {
            JPanel taskPanel = new JPanel();
            taskPanel.setPreferredSize(new Dimension(150, 60));
            taskPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            taskPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskPanel.setBackground(Color.red);

            JLabel arrivalLabel = new JLabel("Arrival Time: " + task.getArrivalTime());
            JLabel serviceLabel = new JLabel("Service Time: " + task.getServiceTime());

            taskPanel.add(arrivalLabel);
            taskPanel.add(serviceLabel);

            leftPanel.add(taskPanel);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        leftPanel.revalidate();
        leftPanel.repaint();
        deskPanel.revalidate();
        deskPanel.repaint();
    }

    public class DeskPanel extends JPanel {
        private final List<JLabel> taskLabels;
        private final List<Server> servers;

        public DeskPanel(int numberOfServers, List<Server> servers) {
            this.taskLabels = new ArrayList<>();
            int panelWidth = numberOfServers * 300 - 100;
            setPreferredSize(new Dimension(panelWidth, 600));
            this.servers = servers;
            updateTaskLabels();
        }
        public void updateTaskLabels() {
            taskLabels.clear();
            for (Server server : servers) {
                Task currentTask = server.getCurrentTask();
                if (currentTask != null) {
                    JLabel taskLabel = new JLabel("Service Time: " + currentTask.getServiceTime());
                    taskLabels.add(taskLabel);
                    JLabel taskLabel2 = new JLabel("Arrival Time: " + currentTask.getArrivalTime());
                    taskLabels.add(taskLabel2);
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = 50;
            int y = 50;
            int width = 100;
            int height = 60;
            int taskLabelX;
            int taskLabelY;
            int index = 0;

            if (servers != null) {
                updateTaskLabels();
            }

            for (Server server : servers) {
                Task currentTask = server.getCurrentTask();
                g.setColor(new Color(139, 69, 19));
                g.fillRect(x, y, width, height);
                if (currentTask != null) {

                    g.setColor(Color.GREEN);
                    g.drawRect(x, y + height + 10, width, height);
                    g.fillRect(x, y + height + 10, width, height);

                    taskLabelX = x;
                    taskLabelY = y + height+15;

                    if (index < taskLabels.size()) {
                        JLabel taskLabel = taskLabels.get(index);
                        taskLabel.setBounds(taskLabelX, taskLabelY, 100, 20);
                        add(taskLabel);
                        JLabel taskLabel2 = taskLabels.get(index+1);
                        taskLabel2.setBounds(taskLabelX, taskLabelY+30, 100, 20);
                        add(taskLabel2);
                    }
                    List<Task> serverTasks = server.getTasks();
                    if (!serverTasks.isEmpty()) {
                        int taskY = y + height * 3 + 50;
                        for (Task task : serverTasks) {
                            g.setColor(Color.YELLOW);
                            g.drawRect(x, taskY  - height, width, height);
                            g.fillRect(x, taskY  - height, width, height);
                            JLabel serverTaskLabel = new JLabel("Service Time: " + task.getServiceTime());
                            serverTaskLabel.setBounds(taskLabelX, taskY-height-10, 150, 50);
                            add(serverTaskLabel);
                            JLabel serverTaskLabel2 = new JLabel("Arrival Time: " + task.getArrivalTime());
                            serverTaskLabel2.setBounds(taskLabelX, taskY-height+20, 150, 50);
                            add(serverTaskLabel2);

                            taskY += 50+height;
                        }
                    }
                }
                x += 300;
                index+=2;
            }
        }
    }
    public void updatePeakHour(List<Server>servers, int time){
        int totalClients=0;
        for(Server server:servers){
            totalClients+=server.getTasks().size();
            if(server.getCurrentTask()!=null){
                totalClients++;
            }
        }
        if(totalClients>maxclients){
            maxclients = totalClients;
            peakHourField.setText(""+time);
            peakHour=time;
        }
    }
}
