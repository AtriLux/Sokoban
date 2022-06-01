package game.sokoban.gameplay.services;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class DataBase {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/Sokoban";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    private boolean isActive;

    public DataBase() {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();
            statement.close();
            connection.close();
            isActive = true;
        } catch (Exception e) {
            System.out.println("База данных не подключена!");
            isActive = false;
            //e.printStackTrace();
        }
    }

    public void add(String nickname, int time, int numLvl) {
        try {
            Class.forName(DRIVER);
            Connection c = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = c.createStatement();

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

    public void writeToTable(VBox table, int numLvl) {
        try {
            Class.forName(DRIVER);
            Connection c = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = c.createStatement();
            int i = 0;
            ResultSet rs = stmt.executeQuery("SELECT NICKNAME, TIME FROM LEVEL" + numLvl + " ORDER BY TIME;");
            while (rs.next()) {
                HBox line = (HBox) table.getChildren().get(i + 1);
                Label nickname = (Label) line.getChildren().get(1);
                Label time = (Label) line.getChildren().get(2);
                nickname.setText(rs.getString("nickname"));
                time.setText(String.valueOf(rs.getInt("time")));
                i++;
            }
            while (i < 10) { // fill empty lines
                HBox line = (HBox) table.getChildren().get(i + 1);
                Label nickname = (Label) line.getChildren().get(1);
                Label time = (Label) line.getChildren().get(2);
                nickname.setText("-");
                time.setText("-");
                i++;
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public boolean isActive() { return isActive; }
}
