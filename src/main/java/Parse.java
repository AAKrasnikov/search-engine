import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse extends RecursiveTask<Set<SitePage>> {
    private String url;
    private Set<String> visitedPage;
    private Set<SitePage> setSitePage;
    private final static Pattern PATTERN = Pattern.compile("^\\/.+\\.html$");

    public Parse(String url, Set<String> visitedPage, Set<SitePage> setSitePage) {
        this.url = url;
        this.visitedPage = visitedPage;
        this.setSitePage = setSitePage;
    }

    @Override
    protected Set<SitePage> compute() {
        Set<SitePage> result = new HashSet<>();
        List<Parse> tasks = new ArrayList<>();
        List<String> list;
        Connection.Response response;

        try {
            response = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).execute();
            Document document = response.parse();
            list = getUrl(document);
            SitePage sp = new SitePage(url, response.statusCode(), document.toString());
            visitedPage.add(url);
            setSitePage.add(sp);

            if (list.isEmpty()) {
                result.add(sp);
            } else {
                for (String s : list) {
                    if (visitedPage.contains(s)) {
                        continue;
                    }
                    Parse task = new Parse(s, visitedPage, setSitePage);
                    task.fork();
                    tasks.add(task);
                }
            }

            for (int i = 0; i < tasks.size(); i++) {
                result.addAll(tasks.get(i).join());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> getUrl(Document document) throws SQLException, IOException {
        List<String> list = new ArrayList<>();
        Elements elements = document.select("a");
        for (Element element : elements) {
            String link = element.attr("href");
            Matcher matcher = PATTERN.matcher(link);
            if (matcher.find()) {
                list.add("http://www.playback.ru" + link);
            }
        }
        return list;
    }
}
