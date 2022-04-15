import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private final static String SRC = "http://www.playback.ru/";
    public static void main(String[] args) throws SQLException {
//        Set<String> visitedPage = new HashSet<>();
//        Set<SitePage> sitePageSet = new HashSet<>();
//        Parse parse = new Parse(SRC, visitedPage, sitePageSet);
//        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        forkJoinPool.invoke(parse);


//          DBConnection db = new DBConnection(sitePageSet);
           DBConnection db = new DBConnection();
 //       db.insert();
        System.out.println(1);

    }
}
