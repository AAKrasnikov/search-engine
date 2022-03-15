import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private final static String SRC = "http://www.playback.ru/";
    public static void main(String[] args){
        Set<String> visitedPage = new HashSet<>();
        Parse parse = new Parse(SRC, visitedPage);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Set<SitePage> sitePageSet = forkJoinPool.invoke(parse);
        try {
            DBConnection db = new DBConnection(sitePageSet);
            db.insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
