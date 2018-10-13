package elements;

import java.util.Random;

import def.ZeitBot;

public class Command {
	
	private String name, answer;
	
	public Command(String name, String answer) {
		this.name = name;
		this.answer = answer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * Viewer vars:
	 * $VIEWER$ or $USER$
	 * $CURRENCYAMOUNT$
	 * $WATCHTIME$
	 * $EXP$
	 * 
	 * Target vars:
	 * $TARGET$
	 * $TARGETWATCHTIME$
	 * $TARGETCURRENCYAMOUNT$
	 * 
	 * Streamspecific vars:
	 * $CURRENCY$
	 * 
	 * Static vars:
	 * $RANDOM$
	 * $MESSAGE$
	 * $UPTIME$
	 */
	public String getAnswerFor(Viewer viewer, String message, ZeitBot bot) {
		String customAnswer = new String(answer);
		Viewer target = bot.getViewerList().get(message.split(" ")[0]);
		// The variables which can be used
		// Viewerspecific
		if(customAnswer.contains("$VIEWER$")) {
			customAnswer = customAnswer.replace("$VIEWER$", viewer.getName());
		}
		if(customAnswer.contains("$USER$")) {
			customAnswer = customAnswer.replace("$USER$", viewer.getName());
		}
		if(customAnswer.contains("$CURRENCYAMOUNT$")) {
			customAnswer = customAnswer.replace("$CURRENCYAMOUNT$", viewer.getCurrency() + "");
		}
		if(customAnswer.contains("$WATCHTIME$")) {
			int time = viewer.getWatchtime();
			int hours = time / 60;
			customAnswer = customAnswer.replace("$WATCHTIME$", (hours >= 10 ? hours : "0" + hours )+ ":"+ (time % 60 >= 10 ? time % 60 : "0" + time % 60) + "");
		}
		if(customAnswer.contains("$EXP$")) {
			customAnswer = customAnswer.replace("$EXP$", viewer.getXp() + "");
		}
		// Targetspecific
		if(target != null) {
			if(customAnswer.contains("$TARGET$")) {
				customAnswer = customAnswer.replace("$TARGET$", target.getName());
			}
			if(customAnswer.contains("$TARGETWATCHTIME$")) {
				int time = target.getWatchtime();
				int hours = time / 60;
				customAnswer = customAnswer.replace("$TARGETWATCHTIME$", (hours >= 10 ? hours : "0" + hours )+ ":"+ (time % 60 >= 10 ? time % 60 : "0" + time % 60) + "");
			}
			if(customAnswer.contains("$TARGETCURRENCYAMOUNT$")) {
				customAnswer = customAnswer.replace("$TARGETCURRENCYAMOUNT$", target.getCurrency() + "");
			}
		}
		// Streamspecific
		if(customAnswer.contains("$CURRENCY$")) {
			customAnswer = customAnswer.replace("$CURRENCY$", bot.getCurrencyName());
		}
		// Static
		if(customAnswer.contains("$RANDOM$")) {
			customAnswer = customAnswer.replace("$RANDOM$", new Random().nextInt(101) + "");
		}
		if(customAnswer.contains("$MESSAGE$")) {
			customAnswer = customAnswer.replace("$MESSAGE$", message);
		}
		if(customAnswer.contains("$UPTIME$")) {
			int uptime = bot.getUptime();
			int hours = uptime / 60;
			int minutes = uptime % 60;
			String sHours = (hours >= 10 ? hours : "0" + hours) + "";
			String sMinutes = (minutes >= 10 ? minutes : "0" + minutes) + "";
			customAnswer = customAnswer.replace("$UPTIME$", sHours + ":" + sMinutes);
		}
		return customAnswer;
	}
}
