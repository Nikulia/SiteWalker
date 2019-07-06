package site;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class SiteWalker extends Thread {
    private File outerFile;
    private String path;
    private URI baseURI;
    private HashSet<String> links = new HashSet<>();
    private final int MAXIMAL_MILLIS_TO_SLEEP = 100;
    private int countLinksWrote = 0;
    private Boolean isPause = false;

    public SiteWalker(String path, File outerFile) {
        outerFile.delete();
        try {
            outerFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.outerFile = outerFile;
        this.path = path;
        try {
            baseURI = new URI(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        writeLink(path, 0);
        links.add(path);
        siteWalk(path, 1);
    }

    private void siteWalk(String linkToPage, int depth) {
        try {
            Thread.sleep(Math.round(MAXIMAL_MILLIS_TO_SLEEP * Math.random()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!Thread.currentThread().isInterrupted()) {
            while (isPause) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                Document document = Jsoup.connect(linkToPage).get();
                Elements elements = document.getAllElements();
                for (Element element : elements) {
                    writeElementWithLink(element, depth, "href", "src");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void writeElementWithLink(Element element, int depth, String... attributes) {
        for (String attribute : attributes) {
            if (Thread.currentThread().isInterrupted())
                break;
            while (isPause) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            String link = element.attr("abs:" + attribute);
            if (link.contains(baseURI.getHost())) {
                if (writeLink(link, depth))
                    siteWalk(link, depth + 1);
            }
        }
    }

    protected boolean writeLink(String link, int depth) {
        try {
            if (links.add(link)) {
                FileUtils.writeStringToFile(outerFile,
                        getTab(depth) + link + System.lineSeparator(),
                        StandardCharsets.UTF_8, true);
                countLinksWrote++;
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getTab(int countTab) {
        String tab = "";
        for (int i = 0; i < countTab; i++) {
            tab = tab.concat("\t");
        }
        return tab;
    }

    public int getCountLinksWrote() {
        return countLinksWrote;
    }

    public void setPause(Boolean pause) {
        isPause = pause;
    }
}
 