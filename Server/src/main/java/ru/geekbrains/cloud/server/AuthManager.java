package ru.geekbrains.cloud.server;

import java.sql.SQLException;

public interface AuthManager {
    void connect() throws ClassNotFoundException, SQLException;
    void disconnect();
    String getNickNameByLoginAndPassword(String login, String password) throws SQLException;

}