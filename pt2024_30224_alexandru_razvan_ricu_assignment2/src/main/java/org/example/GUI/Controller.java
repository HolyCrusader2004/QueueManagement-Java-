package org.example.GUI;

import org.example.BusinessLogic.SelectionPolicy;
import org.example.BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
private final Gui gui;
private SelectionPolicy policy;
    public Controller(Gui gui){
    this.gui=gui;
}

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Start")) {
            //System.out.println("Start button clicked");
            if(policy == null){
                policy=SelectionPolicy.SHORTEST_TIME;
            }
            SimulationManager sim = new SimulationManager(gui.getQueueTextFields(), policy);
            Thread t =new Thread(sim);
            t.start();
        }
        if (command.equals("Switch strategies")) {
            JComboBox<String> comboBox = gui.getStrategyComboBox();
            String selectedStrategy = (String) comboBox.getSelectedItem();
            if (selectedStrategy.equals("Minimum time strategy")) {
                policy=SelectionPolicy.SHORTEST_TIME;
            } else if (selectedStrategy.equals("Minimum length strategy")) {
                policy=SelectionPolicy.SHORTEST_QUEUE;
            }
        }
    }
}
