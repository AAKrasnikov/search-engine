import java.sql.*;
import java.util.List;
import java.util.Set;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/search_engine";
    private static String dbUser = "root";
    private static String dbPass = "CgZAp1KD";
    private Connection connection;
    private Set<SitePage> sitePageSet;

    public DBConnection() {
        this.connection = getConnection();
    }

    public DBConnection(Set<SitePage> sitePageSet) throws SQLException {
        this.sitePageSet = sitePageSet;
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, dbUser, dbPass);
            connection.createStatement().execute("DROP TABLE IF EXISTS field");
            connection.createStatement().execute("CREATE TABLE field(" +
                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "selector VARCHAR(255) NOT NULL, " +
                    "weight FLOAT NOT NULL)");
            connection.createStatement().execute("INSERT INTO field(name, selector, weight) " +
                    "VALUES('title', 'title', 1.0), ('body', 'body', 0.8)");

            connection.createStatement().execute("DROP TABLE IF EXISTS page");
            connection.createStatement().execute("CREATE TABLE page(" +
                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
                    "path TEXT NOT NULL, " +
                    "code INT NOT NULL, " +
                    "content MEDIUMTEXT NOT NULL)");

            connection.createStatement().execute("DROP TABLE IF EXISTS lemma");
            connection.createStatement().execute("CREATE TABLE lemma(" +
                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
                    "lemma VARCHAR(255) NOT NULL, " +
                    "frequency INT NOT NULL)");

            connection.createStatement().execute("DROP TABLE IF EXISTS indexi");
            connection.createStatement().execute("CREATE TABLE indexi(" +
                    "id INT NOT NULL AUTO_INCREMENT primary key, " +
                    "page_id INT NOT NULL, " +
                    "lemma_id INT NOT NULL, " +
                    "rankr FLOAT NOT NULL)");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insert () {
        String sql = "INSERT INTO page(path, code, content) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (SitePage sp : sitePageSet) {
                preparedStatement.setString(1, sp.getPatch());
                preparedStatement.setInt(2, sp.getCode());
                preparedStatement.setString(3, sp.getContent());
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getContent () throws SQLException {
        connection.createStatement().executeQuery("SELECT content FROM page");
    }
}