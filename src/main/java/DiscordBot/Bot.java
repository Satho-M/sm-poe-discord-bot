package DiscordBot;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.commons.lang3.math.NumberUtils;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

import java.sql.SQLException;
import java.util.Optional;

public class Bot {
    public static UserDAO userDAO;
    public static Helper helper;


    public static void main(String[] args) throws SQLException {

        CliOptions options = new CliOptions();
        ParseResult parseResult = new CommandLine(options).parseArgs(args);

        Bot.userDAO = new UserDAO(options.dbPath);

        GatewayDiscordClient client = DiscordClientBuilder.create(options.token)
                .build()
                .login()
                .block();
        readChat(client);
        identifyCommand(client);

        client.onDisconnect().block();
    }

    private static void readChat(GatewayDiscordClient client) {
        //Reads chat and prints into Console

        client.on(MessageCreateEvent.class).subscribe(event ->
        {
            String userName = "";
            Snowflake discordId = null;
            final Message message = event.getMessage();
            for (Embed e : message.getEmbeds()) {
                System.out.println(e);
            }
            String content = message.getContent();
            Optional<User> author = message.getAuthor();
            if (author.isPresent()) {
                User user = author.get();
                discordId = user.getId();
                userName = user.getUsername();

            }
            System.out.println("Conteudo: " + content + "\nUser: " + userName + "\nSnowflake:" + discordId.asString());

        });
    }

    private static void identifyCommand(GatewayDiscordClient client) {
        client.on(MessageCreateEvent.class).subscribe(event -> {
            //Reading message content and eliminating spaces into a StringArray
            final Message command = event.getMessage();

            String strCommand = command.getContent();
            if (strCommand.charAt(0) == '&') {
                String[] splitCommand = strCommand.split("\\s+");

                //Testing
                System.out.println(splitCommand[0]);

                switch (splitCommand[0]) {
                    case "&help":
                        commandHelp(command);
                    case "&ping":
                        commandPong(command);
                        break;
                    case "&death":
                        commandDeath(command, splitCommand);
                        break;
                    case "&deaths":
                        commandDeaths(command, splitCommand);
                        break;
                    default:

                        break;
                }
            }
        });
    }

    private static void commandHelp(Message command) {
        MessageChannel channel = command.getChannel().block();

    }

    private static void commandPong(Message command) {
        MessageChannel channel = command.getChannel().block();
        channel.createMessage("Pong Motherfucker!").block();
    }

    private static void commandDeath(Message command, String[] stringCommand) {

        MessageChannel channel = command.getChannel().block();
        //Verifies if command &death brings an int attached
        String idDiscord = command.getAuthor().get().getId().asString();
        Optional<DiscordBot.User> optUser = userDAO.get(idDiscord);
        if (optUser.isPresent()) {
            if (stringCommand.length > 1) {

                //Check if number has less than 3 digits
                if (NumberUtils.isNumber(stringCommand[1]) && (stringCommand[1].length() < 3)) {
                    int numberDeaths = Integer.parseInt(stringCommand[1]);
                    DiscordBot.User user = optUser.get();
                    user.incrementDeaths(numberDeaths);
                    userDAO.save(user);

                    channel.createMessage( numberDeaths + " more deaths Exile?").block();
                } else {
                    channel.createMessage("Value is too high, maybe try dying a bit less Exile?").block();
                }
            } else {
                DiscordBot.User user = optUser.get();
                user.incrementDeaths(1);
                userDAO.save(user);

                channel.createMessage("Another death Exile?").block();
        }
    }

}

    private static void commandDeaths(Message command, String[] stringCommand) {
        if (stringCommand.length > 1) {
            if ((stringCommand[1].charAt(0) == '<') && (stringCommand[1].charAt(1) == '@') && (stringCommand[1].charAt(stringCommand[1].length() - 1) == '>')) {

                String idDiscord = stringCommand[1].replaceAll("[<@!>]", "");
                if (NumberUtils.isNumber(idDiscord)) {
                    Optional<DiscordBot.User> optUser = userDAO.get(idDiscord);
                    if (optUser.isPresent()) {
                        MessageChannel channel = command.getChannel().block();
                        channel.createMessage("Nickname: " + optUser.get().getUsername() + "\nMortes: " + optUser.get().getnDeaths()).block();


                    }
                }
            }

            if (stringCommand[1].equals("all")) {

                MessageChannel channel = command.getChannel().block();
                for (DiscordBot.User u : userDAO.getAll())
                    channel.createMessage("Nickname: " + u.getUsername() + "\nMortes: " + u.getnDeaths() + "\n___________________________").block();


            }
        }
    }
}
