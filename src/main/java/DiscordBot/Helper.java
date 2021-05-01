package DiscordBot;

public class Helper {
    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    public boolean isShort(String input) {
        try {
            Short.parseShort( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
