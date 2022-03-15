import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/search_engine";
    private static String dbUser = "root";
    private static String dbPass = "CgZAp1KD";

    private Set<SitePage> sitePageSet;

    public DBConnection(Set<SitePage> sitePageSet) throws SQLException {
        this.sitePageSet = sitePageSet;
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, dbUser, dbPass);
            connection.createStatement().execute("DROP TABLE IF EXISTS page");
            connection.createStatement().execute("CREATE TABLE page(" +
                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
                    "path TEXT NOT NULL, " +
                    "code INT NOT NULL, " +
                    "content MEDIUMTEXT NOT NULL)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insert () throws SQLException {
        String sql = "INSERT INTO page(path, code, content) " +
                "VALUES" + "(" + generateStringBuilder() + ")";
        DBConnection.getConnection().createStatement().execute(sql);
    }

    public String generateStringBuilder () {
        StringBuilder stringBuilder = new StringBuilder();
        for (SitePage sp : sitePageSet) {
            String patch = sp.getPatch();
            int code = sp.getCode();
            String content = sp.getContent();
            stringBuilder.append((stringBuilder.length() == 0 ? "" : ",") + "('" + patch + "', '" + code + "', '" + content + "')");
        }
        return stringBuilder.toString();
    }
}