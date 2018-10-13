package elements;

public class Rank {

	private String name;
	private int level;
	private int xpForUp;
	
	public Rank(String name, int level, int xpForUp) {
		this.name = name;
		this.level = level;
		this.xpForUp = xpForUp;
	}
	
	public boolean equals(Rank r) {
		if(r.getName().equalsIgnoreCase(name) && r.getLevel() == level && xpForUp == r.getXpForUp()) {
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXpForUp() {
		return xpForUp;
	}
	
}
