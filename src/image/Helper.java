package image;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Helper {
    private Helper() {
    }

    public static void decorateButton(JButton button, Image image) {
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        Dimension size = button.getPreferredSize();
        button.setIcon(new ImageIcon(scaleImage(image, size.width/3, size.height)));
        button.setFocusable(false);
    }

    public static BufferedImage scaleImage(Image image, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        try {
            graphics2D.drawImage(image, 0,0, width, height, null);
        } finally {
            graphics2D.dispose();
        }
        return  bufferedImage;
    }
}
