package DiscordBot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {

    private Connection conn;

    public UserDAO(String dbpath) throws SQLException {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbpath;
            // create a connection to the database
            this.conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            try {
                if (this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException ex) {
                throw ex;
            }
            throw e;
        }

    }

    @Override
    public Optional<User> get(String id) {

        User user = null;

        String sql = "SELECT * FROM tblDeaths WHERE idDiscord = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                user = new User(
                        res.getString("idDiscord"),
                        res.getString("nickname"),
                        res.getInt("nDeaths"));
            }

        } catch (SQLException throwables) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM tblDeaths";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet res = pstmt.executeQuery();

            while (res.next()) {
                User user = new User(
                        res.getString("idDiscord"),
                        res.getString("nickname"),
                        res.getInt("nDeaths"));

                users.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    @Override
    public void save(User u) {

        String sql = "INSERT INTO " +
                "tblDeaths(idDiscord, nickname, nDeaths) " +
                "VALUES(?,?,?) " +
                "ON CONFLICT(idDiscord) " +
                "DO UPDATE SET nDeaths=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u.getIdDiscord());
            pstmt.setString(2, u.getUsername());
            pstmt.setInt(3, u.getnDeaths());
            pstmt.setInt(4, u.getnDeaths());
            pstmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void update(User u, String[] params) {

    }

    @Override
    public void delete(User u) {

    }

}
