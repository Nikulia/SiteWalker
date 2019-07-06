package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images {
    private static BufferedImage stopButton;
    private static BufferedImage pauseButton;
    private static BufferedImage playButton;

    private Images() {

    }

    public static BufferedImage getStopButton() {
        if (stopButton == null)
            stopButton = getImage("/images/stop.png");
        return stopButton;
    }

    public static BufferedImage getPauseButton() {
        if (pauseButton == null)
            pauseButton = getImage("/images/pause.png");
        return pauseButton;
    }

    public static BufferedImage getPlayButton() {
        if (playButton == null)
            playButton = getImage("/images/play.png");
        return playButton;
    }

    private static BufferedImage getImage(String path) {
        try {
            return ImageIO.read(Images.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }
}
