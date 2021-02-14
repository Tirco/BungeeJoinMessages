package tv.tirco.bungeejoin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class HexChat {

	private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
	private static final char COLOR_CHAR = '\u00A7';

	public static String translateHexCodes(String message)
	{
		//return translate(HEX_PATTERN, message);
    	message = ChatColor.translateAlternateColorCodes('&', message);
    	return translateHexColorCodes("&#","",message);
	}

    public static String translateHexColorCodes(String startTag, String endTag, String message)
    {

        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                    );
        }
        return matcher.appendTail(buffer).toString();
    }

	private static String translate(Pattern hex, String message)
	{
			Matcher matcher = hex.matcher(message);
			StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
			while (matcher.find()) 
			{
				String group = matcher.group(1);
				matcher.appendReplacement(buffer, COLOR_CHAR + "x" 
						+ COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) 
						+ COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
						+ COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
						);
			}
			return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}
}
