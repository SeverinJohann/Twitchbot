package elements;

public class Viewer {

	private String name;
	private int currency;
	private int watchtime;
	private int aliveFor;
	private int xp;
	private Rank currentRank;
	private Ranklist<Rank> rankList = new Ranklist<Rank>();
	private boolean afk;
	
	public Viewer(String name) {
		this.name = name;
		currency = 0;
		watchtime = 0;
		aliveFor = 0;
		xp = 0;
		rankList.init();
		currentRank = rankList.get(0);
	}
	
	public Viewer(String toString, boolean b) {
		name = toString.split(" ")[0];
		currency = Integer.parseInt(toString.split(" ")[1]);
		watchtime = Integer.parseInt(toString.split(" ")[2]);
		aliveFor = 0;
		xp = Integer.parseInt(toString.split(" ")[3]);
		rankList.init();
		currentRank = rankList.get(toString.split(" ")[4]);
	}

	public String toString() {
		return name + " " + currency + " " + watchtime + " " + xp + " " + currentRank.getName().trim().toLowerCase();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}
	
	public void incrementCurrency() {
		if(!afk) {
			currency++;
		}
	}

	public int getWatchtime() {
		return watchtime;
	}

	public void setWatchtime(int watchtime) {
		this.watchtime = watchtime;
	}

	public void incrementWatchtime() {
		if(!afk) {
			watchtime++;
			aliveFor++;
		}
	}
	
	public void incrementXP() {
		if(currentRank.getXpForUp() > 0) {
			xp++;
			if(currentRank.getXpForUp() <= xp) {
				currentRank = rankList.get(rankList.indexOf(currentRank) + 1);
				xp = 0;
			}
		}
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public int getAliveFor() {
		return aliveFor;
	}

	public void setAliveFor(int aliveFor) {
		this.aliveFor = aliveFor;
		if(aliveFor < 10) {
			afk = false;
		} else {
			afk = true;
		}
	}

	public void setAfk(boolean b) {
		afk = true;
	}
}
