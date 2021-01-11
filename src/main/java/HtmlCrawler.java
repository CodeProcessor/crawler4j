import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;


public class HtmlCrawler extends WebCrawler {
    CrawlerStatistics stats;
    FileWriter myWriter;

    public HtmlCrawler(CrawlerStatistics stats, FileWriter myWriter) {
        this.stats = stats;
        this.myWriter = myWriter;
    }

    private final static Pattern EXCLUSIONS
            = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return !EXCLUSIONS.matcher(urlString).matches()
                && urlString.startsWith("https://www.crawler-test.com/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();


        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            stats.incrementTotalLinksCount(links.size());
            stats.incrementProcessedPageCount();

            String write_this_text = "<DOC> <DOCNO> " + stats.getPageCount() + " </DOCNO> " + text + " </DOC>\n";
            try {
                myWriter.write(write_this_text);

            } catch (IOException e) {
                e.printStackTrace();
            }


            // do something with the collected data
        }
    }


}
