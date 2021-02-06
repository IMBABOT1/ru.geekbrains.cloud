package ru.geekbrains.cloud.server;

import java.sql.*;

public class SqlAuthManager implements AuthManager {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement ps;

    @Override
    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:ClientStorage.db");
        statement = connection.createStatement();
    }

    @Override
    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) throws SQLException {
        String s = "";
        try {
            ResultSet rs = statement.executeQuery("SELECT USERNAME FROM users WHERE login like " + "'" + login + "'" + "AND pass like " + "'" + password + "'");
            while (rs.next()) {
                s = (rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

}
