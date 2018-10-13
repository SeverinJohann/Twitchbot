package def;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import exceptions.InvalidConfigsException;

public class Main {

	private ZeitBot bot;
	public static final File FILE_CONFIG = new File("C:/Users/" + System.getProperty("user.name") + "/Documents/Zeitbot/config.txt");
	public static final File FILE_VIEWERS = new File("C:/Users/" + System.getProperty("user.name") + "/Documents/Zeitbot/viewerlist.txt");
	public static final File FILE_COMMANDS = new File("C:/Users/" + System.getProperty("user.name") + "/Documents/Zeitbot/commandlist.txt");
	public static final File FILE_HELP = new File("C:/Users/" + System.getProperty("user.name") + "/Documents/Zeitbot/README.txt");
	public static final File FILE_GIFS = new File("C:/Users/" + System.getProperty("user.name") + "/Documents/Zeitbot/gifs/gifs.txt");
	public static final File FILE_RANKS = new File("C:/Users/" + System.getProperty("user.name") + "/Documents/Zeitbot/ranks.txt");
	public static String CURRENCY_NAME = null;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		GUI gui = new GUI(this);
		initBot(gui);
		gui.showStartupFrame();
	}
	
	public ZeitBot getBot() {
		return bot;
	}
	
	public void connect() {
		new Thread(bot).start();
	}

	public void disconnect() {
		bot.stop();
	}
	
	private void initBot(GUI gui) {
		try {
			if(!FILE_GIFS.exists()) {
				FILE_GIFS.getParentFile().mkdirs();
				FILE_GIFS.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_GIFS));
				bw.append("#Name (duration) cost\r\n" + 
						"yay 50\r\n" + 
						"cool 50\r\n" + 
						"yourock 50\r\n" + 
						"pirate 50");
				bw.newLine();
				bw.close();
			}
			if(!FILE_CONFIG.exists()) {
				FILE_CONFIG.getParentFile().mkdirs();
				FILE_CONFIG.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_CONFIG));
				bw.append("#CHANNEL:null");
				bw.newLine();
				bw.append("#CURRENCY:points");
				bw.newLine();
				bw.append("#BOTNAME:zeit_bot");
				bw.newLine();
				bw.append("#BOTOAUTH:oauth:sdomlaix7kqblbwyicj664t7bqw5ry");
				bw.newLine();
				bw.append("#COLORSET:colorful");
				bw.close();
			}
			if(!FILE_VIEWERS.exists()) {
				if(!FILE_VIEWERS.getParentFile().exists()) {
					FILE_VIEWERS.getParentFile().mkdirs();
				}
				FILE_VIEWERS.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_VIEWERS));
				bw.append("#Name Currency Watchtime XP Rank\r\n"
						+ "zeitless 90000 0 0 starter");
				bw.close();
			}
			if(!FILE_RANKS.exists()) {
				if(!FILE_RANKS.getParentFile().exists()) {
					FILE_RANKS.getParentFile().mkdirs();
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_RANKS));
				bw.append("#Work in Progress (System not added yet)\r\n" + 
						"#Name Level XP-Needed-For-Up\r\n" + 
						"Starter 1 15\r\n" + 
						"Intermediate 2 60\r\n" + 
						"Advanced 3 180\r\n" + 
						"Challenger 4 360\r\n" + 
						"Master 5 720\r\n" + 
						"Grandmaster 6 1440\r\n" + 
						"Legend 7 -1");
				bw.close();
			}
			if(!FILE_HELP.exists()) {
				if(!FILE_HELP.getParentFile().exists()) {
					FILE_HELP.getParentFile().mkdirs();
				}
				FILE_HELP.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_HELP));
				bw.append("This is the official ZeitBot!\r\n" + 
						"For a list of the commands simply open the commandlist.txt\r\n" + 
						"For a list of the viewers simply open the viewerlist.txt\r\n" + 
						"For the settings simply open the settings.txt; only if neccessary though because usually you can change the Settings within in the Application\r\n" + 
						"The currently available variables can be found here: https://drive.google.com/open?id=1nhr9Y2uQjmHBiUitGM81XBVhMXHYKZtB\r\n" + 
						"For further help feel free to write me on https://www.twitch.tv/zeitless\r\n" + 
						"\r\n" + 
						"------------ USING GIFS ---------------\r\n" + 
						"To add the Gif-Window to OBS use a window-capture.\r\n" + 
						"The background of the gifs' window is Colorcode (0,0,0).\r\n" + 
						"Make sure to use a filter for this color.\r\n" + 
						"\r\n" + 
						"To add a gif follow these steps:\r\n" + 
						"	1. Put the .gif file into the \"gifs\" folder\r\n" + 
						"	2. If the gif has a sound, put the corresponding .mp3 file into the \"gifs\" folder. Make sure it has the EXACTLY same name as the .gif file.\r\n" + 
						"	3. Start the bot and click the \"Add Gif\" button. \r\n" + 
						"		-If you got a soundfile use the syntax Gifname;Cost\r\n" + 
						"		-Else use the syntax Gifname;Duration;Cost\r\n" + 
						"	4. Enjoy the gif by calling it with !Gifname");
				bw.close();
			}
			
			String channel = null;
			String currency = null;
			String botname = null;
			String botoauth = null;
			String colorset = null;
			BufferedReader br = new BufferedReader(new FileReader(FILE_CONFIG));
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#CHANNEL")) {
					channel = line.split(":")[1];
				} else if(line.startsWith("#CURRENCY")) {
					currency = line.split(":")[1];
					CURRENCY_NAME = currency;
				} else if(line.startsWith("#BOTNAME")) {
					botname = line.split(":")[1];
				} else if(line.startsWith("#BOTOAUTH")) {
					botoauth = line.replace("#BOTOAUTH:", "");
				} else if(line.startsWith("#COLORSET")) {
					colorset = line.split(":")[1];
				}
			}
			br.close();
			gui.setColorset(colorset);
			bot = new ZeitBot(channel, currency, gui);
			bot.setAccount(botname, botoauth);
		} catch (IOException | InvalidConfigsException e) {
			e.printStackTrace();
		}
	}
}
