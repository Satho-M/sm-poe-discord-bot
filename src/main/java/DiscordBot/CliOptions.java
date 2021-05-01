package DiscordBot;

import picocli.CommandLine;

public class CliOptions {
    @CommandLine.Option(names = { "-t", "--token" }, description = "discord token")
    public String token;

    @CommandLine.Option(names = { "-db", "--database" }, description = "path to sqlite database path")
    public String dbPath;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CliOptions{");
        sb.append("token='").append(token).append('\'');
        sb.append(", dbPath='").append(dbPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
