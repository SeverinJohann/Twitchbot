package elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import def.Main;

public class Ranklist<E extends Rank> extends ArrayList<E>{
	
	private static final long serialVersionUID = 5141746068276034383L;

	public void save() {
		sort();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(Main.FILE_RANKS));
			bw.append("#Name Level XP-Needed-For-Up");
			for(E e : this) {
				bw.newLine();
				bw.append(e.getName() + " " + e.getLevel() + " " + e.getXpForUp());
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sort() {
		this.sort(new Comparator<Rank>() {
			public int compare(Rank r1, Rank r2) {
				if(r1.getLevel() > r2.getLevel()) {
					return 1;
				} else if(r2.getLevel() > r1.getLevel()) {
					return -1;
				}
				return 0;
			}
		});
	}
	
	public Rank get(String name) {
		for(Rank r : this) {
			if(r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(Main.FILE_RANKS));
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				add((E) new Rank(line.split(" ")[0], Integer.parseInt(line.split(" ")[1]), Integer.parseInt(line.split(" ")[2])));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
