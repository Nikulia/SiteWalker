package view;

import site.SiteWalker;

import javax.swing.*;
import java.util.Calendar;

public class ScreenThread extends Thread {
    public static final int MS_IN_SEC = 1000;
    private JLabel screenLinksWrote;
    private JLabel screenTimeElapsed;
    private SiteWalker siteWalker;
    private Boolean isPause = false;

    public ScreenThread(JLabel screenLinksWrote, JLabel screenTimeElapsed, SiteWalker siteWalker) {
        this.screenLinksWrote = screenLinksWrote;
        this.screenTimeElapsed = screenTimeElapsed;
        this.siteWalker = siteWalker;
    }

    @Override
    public void run() {
        int ms = 0;
        int sec = 0;
        while (!Thread.currentThread().isInterrupted()) {
            while (isPause) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int linksWrote = siteWalker.getCountLinksWrote();
            long start = Calendar.getInstance().getTimeInMillis();
            someTimePass(start);
            ms += Calendar.getInstance().getTimeInMillis() - start;
            sec += ms / MS_IN_SEC;
            ms = ms % MS_IN_SEC;
            long finalMs = ms;
            int finalSec = sec;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    screenLinksWrote.setText(linksWrote + " links wrote to the file");
                    screenTimeElapsed.setText(String.format("%d sec %d ms elapsed", finalSec, finalMs));
                }
            });
        }

    }

    private void someTimePass(long start) {
        for (; ; ) {
            if (Calendar.getInstance().getTimeInMillis() - start >= 1)
                break;
        }
    }

    public void setPause(Boolean pause) {
        isPause = pause;
    }
}
