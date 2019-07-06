package view;

import image.Helper;
import image.Images;
import site.SiteWalker;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SiteWalkerFrame extends JFrame {
    private JButton startStopButton;
    private JButton saveLinksAsTXTButton;
    private JButton pauseResumeButton;
    private JTextField textFieldForSitePath;
    private JPanel rootPanel;
    private JLabel screenLinksWrote;
    private JLabel screenTimeElapsed;
    public static final String START = "Start!";
    public static final String STOP = "Stop!";
    public static final String PAUSE = "Pause";
    public static final String RESUME = "Resume";
    private File outerFile;
    private SiteWalker siteWalker;
    private ScreenThread screenThread;

    {
        setImageAndTextOnButton(startStopButton, Images.getPlayButton(), START);
        setImageAndTextOnButton(pauseResumeButton, Images.getPauseButton(), PAUSE);
        pauseResumeButton.setEnabled(false);
        setContentPane(rootPanel);
        screenLinksWrote.setText("0 links wrote to the file");
        screenTimeElapsed.setText("00 sec 000 ms elapsed");
    }

    public SiteWalkerFrame() throws Exception {
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startStopButton.getText().equals(START)) {
                    if (textFieldForSitePath.getText().trim().equals("")) {
                        showWarningDialog("The field of enter path is empty");
                    } else if (outerFile == null)
                        showWarningDialog("File isn't chosen");
                    else {
                        siteWalker = new SiteWalker(textFieldForSitePath.getText(), outerFile);
                        siteWalker.start();
                        screenThread = new ScreenThread(screenLinksWrote, screenTimeElapsed, siteWalker);
                        screenThread.start();
                        setImageAndTextOnButton(startStopButton, Images.getStopButton(), STOP);
                        pauseResumeButton.setEnabled(true);
                    }
                } else if (startStopButton.getText().equals(STOP)) {
                    if (pauseResumeButton.getText().equals(RESUME))
                        setImageAndTextOnButton(pauseResumeButton, Images.getPauseButton(), PAUSE);
                    setImageAndTextOnButton(startStopButton, Images.getPlayButton(), START);
                    pauseResumeButton.setEnabled(false);
                    siteWalker.interrupt();
                    screenThread.interrupt();
                } else
                    incorrectTextOnButtonException();

            }

            private void showWarningDialog(String message) {
                JOptionPane.showOptionDialog(SiteWalkerFrame.this,
                        message, "Not yet...", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, null, JOptionPane.OK_OPTION);
            }
        });
        pauseResumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pauseResumeButton.getText().equals(PAUSE)) {
                    setImageAndTextOnButton(pauseResumeButton, null, RESUME);
                    siteWalker.setPause(true);
                    screenThread.setPause(true);
                } else if (pauseResumeButton.getText().equals(RESUME)) {
                    setImageAndTextOnButton(pauseResumeButton, Images.getPauseButton(), PAUSE);
                    siteWalker.setPause(false);
                    screenThread.setPause(false);
                } else
                    incorrectTextOnButtonException();
            }
        });
        saveLinksAsTXTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT file", "txt");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showSaveDialog(SiteWalkerFrame.this) ==
                        JFileChooser.APPROVE_OPTION) {
                    if (fileChooser.getSelectedFile().getName().contains(".txt"))
                        outerFile = fileChooser.getSelectedFile();
                    else
                        outerFile = new File(fileChooser.getSelectedFile().getParent(),
                                fileChooser.getSelectedFile().getName() + ".txt");
                }
                if (startStopButton.getText().equals(STOP))
                    setImageAndTextOnButton(startStopButton, Images.getPlayButton(), START);
                if (pauseResumeButton.getText().equals(RESUME))
                    setImageAndTextOnButton(pauseResumeButton, Images.getPauseButton(), PAUSE);
            }
        });
    }

    public void setImageAndTextOnButton(JButton button, Image image, String text) {
        Helper.decorateButton(button, image);
        button.setText(text);
    }

    private void incorrectTextOnButtonException() {
        throw new IllegalStateException("Text on the button incorrect!");
    }
}
