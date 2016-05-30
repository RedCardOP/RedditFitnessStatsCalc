package me.redcardop.redditstatscalc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ali on 2016-05-20.
 */
public class ResultsGUI extends  JFrame{
    private JTextArea categoryMetricTextArea;
    private JPanel rootPanel;
    private JButton closeButton;

    public ResultsGUI(String resultsMessage){
        super("Results");
        pack();
        setContentPane(rootPanel);
        setBounds(100, 100, 475, 675);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });

        categoryMetricTextArea.setText(resultsMessage);
    }

}
