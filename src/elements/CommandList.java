package elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import def.Main;

public class CommandList {
	
	private String seperator = "<<<";
	private ArrayList<Command> commandList = new ArrayList<Command>();
	
	public ArrayList<Command> getCommands() {
		return commandList;
	}

	public void init() {
		if(!Main.FILE_COMMANDS.exists()) {
			if(!Main.FILE_COMMANDS.getParentFile().exists()) {
				Main.FILE_COMMANDS.getParentFile().mkdirs();
			}
			try {
				Main.FILE_COMMANDS.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(Main.FILE_COMMANDS));
				bw.append("#Name <<< Answer\r\n" + 
						"!me<<<$VIEWER$: you have $CURRENCYAMOUNT$ $CURRENCY$ and watched $WATCHTIME$ hours.\r\n" + 
						"!love<<<$VIEWER$ loves $MESSAGE$ to $RANDOM$% <3\r\n" + 
						"!hug<<<$VIEWER$ gives $MESSAGE$ a magical and warm hug <3\r\n" + 
						"!points<<<$USER$ has $CURRENCYAMOUNT$ points.");
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(Main.FILE_COMMANDS));
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				commandList.add(new Command(line.split(seperator)[0], line.split(seperator)[1]));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add(String line) {
		String name = line.split(" ")[0];
		String answer = line.replaceFirst(name + " ", "");
		commandList.add(new Command(name, answer));
	}
	
	public void save() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(Main.FILE_COMMANDS));
			bw.append("#Name <<< Answer");
			for(Command command : commandList) {
				bw.newLine();
				bw.append(command.getName());
				bw.append(seperator);
				bw.append(command.getAnswer());
			}
			bw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public void remove(Command c) {
		commandList.remove(c);
	}
	
}
