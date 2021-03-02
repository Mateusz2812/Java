package Weather;

import org.apache.log4j.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Main {

    static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Interface gui = new Interface();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.createAndShowGUI();
            }
        });


    }


}
