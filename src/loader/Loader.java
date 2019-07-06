package loader;

import view.SiteWalkerFrame;

import javax.swing.*;

public class Loader {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SiteWalkerFrame frame = new SiteWalkerFrame();
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    frame.pack();
                    frame.setMinimumSize(frame.getPreferredSize());
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
