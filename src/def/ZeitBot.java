package def;

import org.jibble.pircbot.PircBot;

import elements.Command;
import elements.CommandList;
import elements.Gif;
import elements.GifList;
import elements.Rank;
import elements.Ranklist;
import elements.Viewer;
import elements.ViewerList;
import exceptions.InvalidConfigsException;

//TODO Ranks

public class ZeitBot extends PircBot implements Runnable {

	private String admin;
	private String currency_name;
	private String channel;
	private boolean has_currency = true;
	private int currency_timer = 60;
	private GUI gui;

	private String oauth = "oauth:sdomlaix7kqblbwyicj664t7bqw5ry";

	private int uptime;

	private boolean alive = false;
	private ViewerList viewerList;
	private ViewerList afkList;
	private CommandList commandList;
	private GifList<Gif> gifList;
	public Ranklist<Rank> rankList = new Ranklist<Rank>();
	private boolean playingGif = false;

	/**
	 * Creates a new instance of a TwitchBot
	 * 
	 * @param channelName
	 *            The name of the channel to join
	 * @param currencyName
	 *            The name of the currency
	 */
	public ZeitBot(String channelName, String currency_name, GUI gui) throws InvalidConfigsException {
		if (channelName == null) {
			throw new InvalidConfigsException();
		}
		this.admin = channelName;
		this.currency_name = currency_name;
		if (currency_name == null) {
			has_currency = false;
		}
		this.channel = "#" + channelName;
		this.gui = gui;
		this.setVerbose(true);

		viewerList = new ViewerList();
		afkList = new ViewerList();
		commandList = new CommandList();
		commandList.init();
		gifList = new GifList<Gif>();
		gifList.init();
		rankList.init();
	}

	public void setAccount(String name, String oauth) {
		this.setName(name);
		this.oauth = oauth;
	}

	public void onDisconnect() {
		gui.onDisconnect();
	}

	public ViewerList getViewerList() {
		return viewerList;
	}

	public void setChannel(String channel) {
		this.channel = "#" + channel;
		this.admin = channel;
	}

	public void save() {
		for (Viewer v : afkList.toArray()) {
			viewerList.add(v);
		}
		viewerList.save();
		for (Viewer v : afkList.toArray()) {
			viewerList.remove(v);
		}
		commandList.save();
		gifList.save();
		rankList.save();
	}

	private void connect() {
		try {
			System.out.println(oauth);
			connect("irc.twitch.tv", 6667, oauth);
			setName("zeit_bot");
			joinChannel(channel);
			sendMessage(channel, "Connected succesfully SeemsGood");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCurrency(String currency) {
		currency_name = currency;
		if (!currency_name.equals("null")) {
			has_currency = true;
		} else {
			has_currency = false;
		}
	}

	public void run() {
		connect();
		alive = true;
		long timeNow = System.currentTimeMillis();
		long timeDifference = 0;
		int i = 0;
		uptime = 0;
		while (alive) {
			long nextTime = System.currentTimeMillis();
			timeDifference += nextTime - timeNow;
			timeNow = nextTime;
			if (timeDifference >= 1000) {
				i++;
				timeDifference -= 1000;
				for (int l = 0; l < viewerList.toArray().size(); l++) {
					Viewer v = viewerList.toArray().get(l);
					if (v.getAliveFor() >= 15) {
						v.setAliveFor(0);
						viewerList.remove(v);
						afkList.add(v);
					}
				}
				if (i % currency_timer == 0) {
					viewerList.incrementCurrency();
				}
				if (i % 60 == 0) {
					viewerList.incrementWatchtime();
					viewerList.incrementXP();
					uptime++;
				}
			}
		}
	}

	public void stop() {
		disconnect();
		alive = false;
	}

	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message) {

		if (!viewerList.contains(sender)) {
			if (afkList.contains(sender)) {
				viewerList.add(afkList.get(sender));
			} else {
				viewerList.add(sender);
			}
		}
		Viewer viewer = viewerList.get(sender);
		if (viewer.getAliveFor() >= 1) {
			viewer.incrementCurrency();
		}
		viewer.setAliveFor(0);

		if (message.startsWith("!")) {
			for (Gif g : gifList) {
				if (message.split("!")[1].toLowerCase().startsWith(g.getName().toLowerCase())) {
					if (gui.getGifsAlive()) {
						if (viewer.getCurrency() >= g.getCost()) {
							g.play(this, gui.getGifFrame());
							viewer.setCurrency(viewer.getCurrency() - g.getCost());
							sendMessage(channel,
									sender + " redeemed " + g.getName() + " for " + g.getCost() + currency_name);
						} else {
							sendMessage(channel,
									sender + " you do not have enough " + currency_name + " to play the gif.");
						}
						return;
					} else {
						sendMessage(channel, sender + " gifs are currently turned off");
					}
				}
			}
			if (message.equalsIgnoreCase("!gifs")) {
				String gifsString = "";
				for (int i = 0; i < gifList.size(); i++) {
					Gif g = gifList.get(i);
					gifsString += "!" + g.getName();
					if (i == gifList.size() - 2) {
						gifsString += " and ";
					} else if (i < gifList.size() - 1) {
						gifsString += ", ";
					}
				}
				sendMessage(channel, "The gifs available in this channel are " + gifsString);
				return;
			} else if (message.equalsIgnoreCase("!commands")) {
				String commandString = "";
				for (Command c : commandList.getCommands()) {
					commandString += c.getName() + ", ";
				}
				commandString += " and !gifs";
				sendMessage(channel, "The commands available in this channel are " + commandString);
				return;
			} else if (sender.equalsIgnoreCase(admin) && message.toLowerCase().startsWith("!addcom")) {
				boolean found = false;
				for (Command c : commandList.getCommands()) {
					if (c.getName().equalsIgnoreCase(message.split(" ")[1])) {
						found = true;
						sendMessage(channel, " This command exists already. Try editing it with !editcom");
					}
				}
				if (!found) {
					commandList.add(message.replaceFirst(message.split(" ")[0] + " ", ""));
					sendMessage(channel, sender + " Succesfully added the command.");
				}
				return;
			} else if (sender.equalsIgnoreCase(admin) && message.toLowerCase().startsWith("!editcom")) {
				boolean found = false;
				for (Command c : commandList.getCommands()) {
					if (c.getName().equalsIgnoreCase(message.split(" ")[1])) {
						found = true;
						message = message.replaceFirst(message.split(" ")[0] + " ", "");
						message = message.replaceFirst(message.split(" ")[0] + " ", "");
						c.setAnswer(message);
						sendMessage(channel, sender + " Succesfully replaced the answer for " + c.getName() + " to "
								+ c.getAnswer());
					}
				}
				if (!found) {
					sendMessage(channel, sender + " This command does not exist");
				}
				return;
			} else if (sender.equalsIgnoreCase(admin) && message.toLowerCase().startsWith("!delcom")) {
				boolean found = false;
				for (Command c : commandList.getCommands()) {
					if (c.getName().equals(message.split(" ")[1])) {
						commandList.remove(c);
						sendMessage(channel, sender + "The command " + c.getName() + " has been removed successfully.");
						found = true;
						break;
					}
				}
				if (!found) {
					sendMessage(channel, sender + " This command does not exist.");
				}
				return;
			}
			for (Command command : commandList.getCommands()) {
				if (command.getName().equalsIgnoreCase(message.split(" ")[0])) {
					sendMessage(channel,
							command.getAnswerFor(viewer, message.replaceFirst(command.getName() + " ", ""), this));
					return;
				}
			}
		}

	}

	public boolean isPlayingGif() {
		return playingGif;
	}

	public void setPlayingGif(boolean playingGif) {
		this.playingGif = playingGif;
	}

	public String getCurrencyName() {
		return currency_name;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getCurrency_name() {
		return currency_name;
	}

	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}

	public boolean isHas_currency() {
		return has_currency;
	}

	public void setHas_currency(boolean has_currency) {
		this.has_currency = has_currency;
	}

	public int getCurrency_timer() {
		return currency_timer;
	}

	public void setCurrency_timer(int currency_timer) {
		this.currency_timer = currency_timer;
	}

	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public int getUptime() {
		return uptime;
	}

	public void setUptime(int uptime) {
		this.uptime = uptime;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public CommandList getCommandList() {
		return commandList;
	}

	public void setCommandList(CommandList commandList) {
		this.commandList = commandList;
	}

	public GifList<Gif> getGifList() {
		return gifList;
	}

	public void setGifList(GifList<Gif> gifList) {
		this.gifList = gifList;
	}

	public String getChannel() {
		return channel;
	}

	public void setOauth(String oauth) {
		this.oauth = oauth;
	}

	public void setViewerList(ViewerList viewerList) {
		this.viewerList = viewerList;
	}

	public Ranklist<Rank> getRankList() {
		return rankList;
	}

	public void setRankList(Ranklist<Rank> rankList) {
		this.rankList = rankList;
	}

}
