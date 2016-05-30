package me.redcardop.redditstatscalc;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ali on 2016-05-20.
 */
public class Main {

    private JFrame frame;


    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    if(args.length == 0) {
                        GUI window = new GUI();
                    }else{
                        GUI window = new GUI(args);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
