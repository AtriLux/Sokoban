package game.sokoban;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class DataBase {

    static final String DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/Sokoban";
    static final String USER = "postgres";
    static final String PASS = "1234";

    private String[] data;

    public DataBase() {
        Statement statement = null;
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("База данных не подключена!");
            e.printStackTrace();
        }
    }

    public void add(String nickname, int time, int numLvl) {
        Connection c = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = c.createStatement();

            c.setAutoCommit(false);
            String sql = "INSERT INTO LEVEL" + numLvl + " (NICKNAME, TIME)"
                    + "VALUES ('" + nickname + "', " + time + ");";
            statement.executeUpdate(sql);

            statement.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToString(int numLvl) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            c.setAutoCommit(false);
            data = new String[10];
            int i = 0;
            ResultSet rs = stmt.executeQuery("SELECT NICKNAME, TIME FROM LEVEL" + numLvl + " ORDER BY TIME;");
            while (rs.next()) {
                data[i++] = rs.getString("nickname") + " " + rs.getInt("time");
            }
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void writeToTable(VBox table, int numLvl) {
        writeToString(numLvl);
        for (int i = 0; i < 10; i++) {
            HBox line = (HBox) table.getChildren().get(i + 1);
            Label nickname = (Label) line.getChildren().get(1);
            Label time = (Label) line.getChildren().get(2);
            String d = data[i];
            if (d != null) {
                nickname.setText(d.split(" ")[0]);
                time.setText(d.split(" ")[1]);
            }
            else {
                nickname.setText("-");
                time.setText("-");
            };
        }
    }


}
