package DiscordBot;

public class User {

    private String idDiscord;
    private String username;
    private int nDeaths;

    public User(String idDiscord, String username, int nDeaths) {
        this.idDiscord = idDiscord;
        this.username = username;
        this.nDeaths = nDeaths;

    }

    public String getIdDiscord() {
        return idDiscord;
    }

    public void setIdDiscord(String idDiscord) {
        this.idDiscord = idDiscord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getnDeaths() {
        return nDeaths;
    }

    public void setnDeaths(int nDeaths) {
        this.nDeaths = nDeaths;
    }

    public void incrementDeaths(int increment) {
        this.nDeaths += increment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("idDiscord='").append(idDiscord).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", nDeaths=").append(nDeaths);
        sb.append('}');
        return sb.toString();
    }
}
