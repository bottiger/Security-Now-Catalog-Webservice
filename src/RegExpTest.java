import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegExpTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String testData = "<table width=\"100%\" bgcolor=\"#F8F8F8\" " +
			"border=0 cellpadding=0 cellspacing=0><tr><td>" +
			"<table width=\"100%\" border=0 cellpadding=0 " +
			"cellspacing=10><tr><td colspan=6><font size=1>" +
			"<font size=2><b>Voting Machine Hacking</b></font><br>" +
			"<img src=\"/image/transpixel.gif\" width=1 height=4 " +
			"border=0><br>This week Leo and I describe the inner " +
			"workings of one of the best designed and apparently most " +
			"secure electronic voting machines &#8212; currently in " +
			"use in the United States &#8212; and how a group of " +
			"university researchers hacked it without any outside " +
			"information <to create a 100% stealth vote stealing " +
			"system.<br></font></td></tr><tr>";
		
		Pattern titleAndDesc = Pattern.compile("<table width=\"100%\" " +
				"bgcolor=\"#F8F8F8\" border=0 cellpadding=0 " +
				"cellspacing=0><tr>" +
				"<td><table width=\"100%\" border=0 cellpadding=0 " +
				"cellspacing=10><tr><td colspan=6><font size=1>" +
				"<font size=2>" +
				"<b>([^<][^/]+)</b></font><br>" +
				"<img src=\"/image/transpixel.gif\" width=1 height=4 " +
				"border=0><br>([^</]+)<(.)+");
		Matcher m3 = titleAndDesc.matcher (testData);

		if (m3.find()) {
			System.out.print("Succes");
		} else
			System.out.print("Failure");

	}

}
