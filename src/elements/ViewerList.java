package elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import def.Main;

public class ViewerList {

	private ArrayList<Viewer> viewerList = new ArrayList<Viewer>();
	
	public ViewerList() {}
	
	/**
	 * 
	 * @param name Name of viewer
	 * @return The viewer; null if not found
	 */
	public Viewer get(String name) {
		for(Viewer v : viewerList) {
			if(v.getName().equals(name)) {
				return v;
			}
		}
		return null;
	}
	
	public void initialise() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("files/viewerlist.txt"));
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String name) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(Main.FILE_VIEWERS));
			String line;
			boolean found = false;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				} else if(line.startsWith(name)) {
					add(new Viewer(line, true));
					found = true;
					break;
				}
			}
			if(!found) {
				add(new Viewer(name));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean contains(String name) {
		for(Viewer v : viewerList) {
			if(v.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	private void sort() {
		String[] names = new String[viewerList.size()];
		for(int i = 0; i < viewerList.size(); i++) {
			Viewer v = viewerList.get(i);
			names[i] = v.getName();
		}
		boolean sorted = false;
		while(!sorted) {
			sorted = true;
			for(int i = 1; i < names.length; i++) {
				if(names[i-1].compareTo(names[i]) > 0) {
					String temp = names[i];
					names[i] = names[i-1];
					names[i-1] = temp;
					sorted = false;
				}
			}
		}
		ArrayList<Viewer> newList = new ArrayList<Viewer>();
		for(int i = 0; i < viewerList.size(); i++) {
			newList.add(get(names[i]));
		}
		viewerList = newList;
	}
	
	public void save() {
		try {
			ViewerList list = new ViewerList();
			BufferedReader br = new BufferedReader(new FileReader(Main.FILE_VIEWERS));
			String line;
			for(Viewer v : viewerList) {
				list.add(v);
			}
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				if(!list.contains(line.split(" ")[0])) {
					list.add(new Viewer(line, true));
				}
			}
			br.close();
			list.sort();
			BufferedWriter bw = new BufferedWriter(new FileWriter(Main.FILE_VIEWERS));
			bw.append("#Name Currency Watchtime Rank");
			for(Viewer v : list.toArray()) {
				bw.newLine();
				bw.append(v.toString());
			}
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Saved");
	}
	
	public ArrayList<Viewer> toArray() {
		return viewerList;
	}
	
	public ArrayList<Viewer> removeAll(ArrayList<Viewer> toRemove) {
		for(Viewer v : toRemove) {
			viewerList.remove(v);
		}
		return viewerList;
	}

	public boolean add(Viewer v) {
		return viewerList.add(v);
	}
	
	public void incrementCurrency() {
		for(Viewer v : viewerList) {
			v.incrementCurrency();
		}
	}
	
	public void disable(Viewer v) {
		v.setAfk(true);
	}
	
	public void enable(Viewer v) {
		v.setAfk(false);
	}
	
	public void incrementWatchtime() {
		for(Viewer v : viewerList) {
			v.incrementWatchtime();
		}
	}

	public void remove(Viewer v) {
		viewerList.remove(v);
	}

	public void incrementXP() {
		for(Viewer v : viewerList) {
			v.incrementXP();
		}
	}
	
}
