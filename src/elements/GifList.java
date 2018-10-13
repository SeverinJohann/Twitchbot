package elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import def.Main;

public class GifList<E extends Gif> extends ArrayList<E> {

	private static final long serialVersionUID = 1798972832101401698L;
	
	public boolean contains(String name) {
		for(E e : this) {
			if(e.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public E get(String name) {
		for(E e : this) {
			if(e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void init() {
		File file = Main.FILE_GIFS;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] split = line.split(" ");
				if(split.length == 2) {
					Gif g = new Gif(split[0], Integer.parseInt(split[1]));
					add((E) g);
				} else if(split.length == 3) {
					Gif g = new Gif(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
					add((E) g);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void save() {
		for(int i = 0; i < this.size(); i++) {
			Gif g = get(i);
			if(g.getDuration() == 0 && !g.isHasSound()) {
				remove(g);
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(Main.FILE_GIFS, false));
			bw.append("#Name (duration) cost");
			for(Gif g : this) {
				bw.newLine();
				if(g.getDuration() == 0) {
					bw.append(g.getName() + " " + g.getCost());
				} else if(g.getDuration() >= 0) {
					bw.append(g.getName() + " " + g.getDuration() + " " + g.getCost());
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
