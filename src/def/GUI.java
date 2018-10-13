package def;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import elements.Gif;
import elements.Viewer;
import elements.ViewerList;
import listeners.MyWindowListener;

public class GUI implements ActionListener {

	private Main main;

	private String current_page = "startup";

	private String colorset;

	private final Color COLOR_DARK_BACKGROUND = new Color(80, 80, 80);
	private final Color COLOR_DARK_FOREGROUND = new Color(110, 110, 110);
	private final Color COLOR_DARK_FONT = new Color(150, 150, 150);
	private final Color COLOR_DARK_TEXTFIELD_BACKGROUND = new Color(60, 60, 60);
	private final Color COLOR_DARK_BUTTON_BACKGROUND = new Color(50, 50, 50);
	private final Color COLOR_DARK_BUTTON_FOREGROUND = new Color(150, 150, 150);
	private final Color COLOR_DARK_HEADLINE = new Color(90, 90, 90);

	private final Color COLOR_BRIGHT_BACKGROUND = new Color(255, 255, 255);
	private final Color COLOR_BRIGHT_FOREGROUND = new Color(110, 110, 110);
	private final Color COLOR_BRIGHT_FONT = new Color(30, 30, 30);
	private final Color COLOR_BRIGHT_TEXTFIELD_BACKGROUND = new Color(180, 180, 180);
	private final Color COLOR_BRIGHT_BUTTON_BACKGROUND = new Color(200, 200, 200);
	private final Color COLOR_BRIGHT_BUTTON_FOREGROUND = new Color(30, 30, 30);
	private final Color COLOR_BRIGHT_HEADLINE = new Color(190, 190, 190);

	private final Color COLOR_COLORFUL_BACKGROUND = new Color(85, 85, 255);
	private final Color COLOR_COLORFUL_FOREGROUND = new Color(30, 30, 30);
	private final Color COLOR_COLORFUL_FONT = new Color(220, 180, 30);
	private final Color COLOR_COLORFUL_TEXTFIELD_BACKGROUND = new Color(220, 70, 70);
	private final Color COLOR_COLORFUL_BUTTON_BACKGROUND = new Color(80, 200, 80);
	private final Color COLOR_COLORFUL_BUTTON_FOREGROUND = new Color(220, 70, 70);
	private final Color COLOR_COLORFUL_HEADLINE = new Color(70, 180, 70);

	private Color color_background;
	private Color color_foreground;
	private Color color_font;
	private Color color_textfield_background;
	private Color color_button_background;
	private Color color_button_foreground;
	private Color color_headline;
	private Color color_table_background;

	private File file_configs = Main.FILE_CONFIG;

	private JFrame frame;
	private JFrame gif_frame;
	private boolean gif_frame_open = false;
	private JPanel main_panel;
	private ArrayList<JPanel> main_panel_list = new ArrayList<JPanel>();
	private int main_panel_columns;
	private int main_panel_rows;

	private boolean connected = false;
	private ViewerList viewerList;

	public GUI(Main main) {
		this.main = main;
		frame = new JFrame("Zeitbot - Who is this Miss Streamlabs? Keepo");
		resetMainPanel(10, 10, 1);
		frame.setSize(1000, 700);
		frame.setMinimumSize(new Dimension(750, 480));
		frame.setLocationRelativeTo(null);
		frame.setFocusable(false);
		frame.setAutoRequestFocus(false);
		frame.add(main_panel);
		frame.addWindowListener(new MyWindowListener(main));
		new Thread() {
			public void run() {
				long timeNow = System.currentTimeMillis();
				long timeDifference = 0;
				int i = 0;
				while (true) {
					long nextTime = System.currentTimeMillis();
					timeDifference += nextTime - timeNow;
					timeNow = nextTime;
					if (timeDifference >= 1000) {
						i++;
						timeDifference -= 1000;
						if (current_page.equals("startup") && i % 5 == 0) {
							updateTable();
						}
					}
				}
			}
		}.start();
	}

	public void showStartupFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(color_background);
		initStartupPanels();
	}

	private void initStartupPanels() {
		resetMainPanel(8, 20, 0);
		addToMainPanel(createButton("Settings", 20), 7, 0);
		addToMainPanel(createButton("Connect", 20), 0, 0);
		addToMainPanel(createButton("Open Gifs", 20), 1, 0);
		addToMainPanel(createButton("Add Gif", 20), 2, 0);
		addToMainPanel(createButton("Ranks", 20), 3, 0);
		for (int i = 1 + 16; i <= main_panel_list.size(); i++) {
			if (i == 20 || i == 21 || i == 22 || i == 23) {
				main_panel_list.get(i - 8).setBackground(color_textfield_background);
			}
			if (i % 8 == 0) {
				JPanel p1 = main_panel_list.get(i - 1);
				JPanel p2 = main_panel_list.get(i - 2);
				JPanel p3 = main_panel_list.get(i - 3);
				JPanel p4 = main_panel_list.get(i - 4);
				p1.setBackground(color_table_background);
				p2.setBackground(color_table_background);
				p3.setBackground(color_table_background);
				p4.setBackground(color_table_background);
				p1.setBorder(BorderFactory.createLineBorder(color_background, 1));
				p2.setBorder(BorderFactory.createLineBorder(color_background, 1));
				p3.setBorder(BorderFactory.createLineBorder(color_background, 1));
				p4.setBorder(BorderFactory.createLineBorder(color_background, 1));
				p1.add(createTableText("", 18, color_background, color_foreground));
				p2.add(createTableText("", 18, color_background, color_foreground));
				p3.add(createTableText("", 18, color_background, color_foreground));
				p4.add(createTableText("", 18, color_background, color_foreground));
			}
		}
		addToMainPanel(createTextArea("        Viewer ", 24, color_textfield_background, color_font), 5, 1);
		addToMainPanel(createTextArea("list              ", 24, color_textfield_background, color_font), 6, 1);
		addToMainPanel(createTableText("Name", 18, color_background, color_foreground), 4, 2);
		addToMainPanel(createTableText(main.getBot().getCurrencyName(), 18, color_background, color_foreground), 5, 2);
		addToMainPanel(createTableText("Name", 18, color_background, color_foreground), 6, 2);
		addToMainPanel(createTableText(main.getBot().getCurrencyName(), 18, color_background, color_foreground), 7, 2);
		update();
		updateTable();
	}

	private void updateTable() {
		if (viewerList != null && viewerList.toArray().size() >= 0) {
			int column = 4;
			int row = 2;
			for (int i = 0; i < 36; i++) {
				Viewer v;
				if (viewerList.toArray().size() > i) {
					v = viewerList.toArray().get(i);
					synchronized (v) {
						Component c1 = getFromMainPanel(column, row);
						if (c1 instanceof JTextArea) {
							JTextArea area = (JTextArea) c1;
							area.setText(v != null ? v.getName() : "");
						}
						Component c2 = getFromMainPanel(column + 1, row);
						if (c2 instanceof JTextArea) {
							JTextArea area = (JTextArea) c2;
							area.setText(v != null ? v.getCurrency() + "" : "");
						}
					}
				} else {
					Component c1 = getFromMainPanel(column, row);
					if (c1 instanceof JTextArea) {
						JTextArea area = (JTextArea) c1;
						area.setText("");
					}
					Component c2 = getFromMainPanel(column + 1, row);
					if (c2 instanceof JTextArea) {
						JTextArea area = (JTextArea) c2;
						area.setText("");
					}
				}
				column += 2;
				if ((column) % 8 == 0) {
					column -= 4;
					row++;
				}
			}
		}
		update();
	}

	private void initConfigPanels() {
		resetMainPanel(8, 20, 0);
		String channel = "";
		String currency = "";
		String botname = "";
		String botoauth = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file_configs));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#CHANNEL")) {
					channel = line.split(":")[1];
				} else if (line.startsWith("#CURRENCY")) {
					currency = line.split(":")[1];
				} else if (line.startsWith("#BOTNAME")) {
					botname = line.split(":")[1];
				} else if (line.startsWith("#BOTOAUTH")) {
					botoauth = line.replace("#BOTOAUTH:", "");
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 10; i < 161; i++) {
			if (i % 8 == 0) {
				JPanel p1 = main_panel_list.get(i - 4);
				JPanel p2 = main_panel_list.get(i - 5);
				p1.setBackground(color_background.darker());
				p2.setBackground(color_background.darker());
			}
		}
		addToMainPanel(createButton("Ranks", 20), 3, 0);
		addToMainPanel(createButton("Add Gif", 20), 2, 0);
		addToMainPanel(createButton("Open Gifs", 20), 1, 0);
		addToMainPanel(createButton("Connect", 20), 0, 0);
		addToMainPanel(createTextArea("Channel:", 20, color_background.darker(), color_font), 3, 1);
		addToMainPanel(createTextField(channel, 20, color_textfield_background, color_font), 4, 1);
		addToMainPanel(createTextArea("Currency:", 20, color_background.darker(), color_font), 3, 2);
		addToMainPanel(createTextField(currency, 20, color_textfield_background, color_font), 4, 2);
		// addToMainPanel(createButton("Remove", 24), 5, 2);
		addToMainPanel(createTextArea("Botname:", 20, color_background.darker(), color_font), 3, 3);
		addToMainPanel(createTextField(botname, 20, color_textfield_background, color_font), 4, 3);
		addToMainPanel(createTextArea("Bot-OAuth:", 20, color_background.darker(), color_font), 3, 4);
		addToMainPanel(createTextField(botoauth, 20, color_textfield_background, color_font), 4, 4);
		addToMainPanel(createTextArea("Window:", 20, color_background.darker(), color_font), 3, 5);
		if (colorset.equals("dark")) {
			addToMainPanel(createComboBox(new String[] { "dark", "bright", "colorful" }, color_background.darker(),
					color_font), 4, 5);
		} else if (colorset.equals("bright")) {
			addToMainPanel(createComboBox(new String[] { "bright", "dark", "colorful" }, color_background.darker(),
					color_font), 4, 5);
		} else if (colorset.equals("colorful")) {
			addToMainPanel(createComboBox(new String[] { "colorful", "bright", "dark" }, color_background.darker(),
					color_font), 4, 5);
		}
		addToMainPanel(createButton("Apply", 20), 4, 18);
		addToMainPanel(createButton("Return", 20), 7, 0);
		update();
	}

	public void update() {
		if (!connected) {
			((JButton) (main_panel_list.get(0).getComponent(0))).setText("Connect");
		} else {
			((JButton) (main_panel_list.get(0).getComponent(0))).setText("Disconnect");
		}
		viewerList = main.getBot().getViewerList();
		main_panel.setVisible(true);
		frame.setVisible(true);
	}

	private void resetMainPanel(int columns, int rows, int bordersize) {
		main_panel_columns = columns;
		main_panel_rows = rows;
		if (main_panel != null) {
			frame.remove(main_panel);
		}
		main_panel_list = new ArrayList<JPanel>();
		GridLayout layout = new GridLayout();
		layout.setColumns(columns);
		layout.setRows(rows);
		main_panel = new JPanel(layout);
		for (int i = 0; i < columns * rows; i++) {
			JPanel panel = createSparePanel((double) frame.getWidth() / (double) columns,
					(double) frame.getHeight() / (double) rows, bordersize);
			if (i < columns) {
				panel.setBackground(color_headline);
			}
			main_panel.add(panel);
			main_panel_list.add(panel);
		}
		main_panel.setBackground(color_foreground);
		frame.add(main_panel);
	}

	private void addToMainPanel(Component c, int column, int row) {
		main_panel_list.get(row * main_panel_columns + column).add(c, Component.TOP_ALIGNMENT);
	}

	private Component getFromMainPanel(int column, int row) {
		return main_panel_list.get(row * main_panel_columns + column).getComponent(0);
	}

	private JPanel createSparePanel(double width, double height, int bordersize) {
		JPanel panel = new JPanel();
		Dimension dimension = new Dimension((int) Math.round(width), (int) Math.round(height));
		panel.setSize(dimension);
		panel.setMaximumSize(dimension);
		panel.setMinimumSize(dimension);
		panel.setPreferredSize(dimension);
		panel.setBackground(color_background);
		panel.setBorder(BorderFactory.createLineBorder(color_foreground, bordersize));
		return panel;
	}

	private JButton createButton(String text, int fontsize) {
		JButton btn = new JButton(text);
		Dimension dimension = new Dimension((int) (frame.getWidth() / (main_panel_columns * 1.2)),
				(int) (frame.getHeight() / (main_panel_rows * 1.2)));
		btn.setPreferredSize(dimension);
		btn.setBackground(color_button_background);
		btn.setForeground(color_button_foreground);
		btn.setBorder(BorderFactory.createEmptyBorder());
		btn.setFont(btn.getFont().deriveFont((float) fontsize));
		btn.addActionListener(this);
		return btn;
	}

	private JComboBox<String> createComboBox(String[] options, Color background, Color foreground) {
		JComboBox<String> box = new JComboBox<String>();
		box.setBackground(background);
		box.setForeground(foreground);
		for (String s : options) {
			box.addItem(s);
		}
		return box;
	}

	private JTextArea createTextArea(String text, float size, Color background, Color foreground) {
		JTextArea txt = new JTextArea(text);
		txt.setFont(txt.getFont().deriveFont(size));
		txt.setForeground(foreground);
		txt.setBackground(background);
		txt.setEditable(false);
		return txt;
	}

	private JTextField createTextField(String text, float size, Color background, Color foreground) {
		JTextField txt = new JTextField(text);
		txt.setFont(txt.getFont().deriveFont(size));
		txt.setForeground(foreground);
		txt.setBackground(background);
		txt.setBorder(BorderFactory.createLineBorder(color_background, 1));
		Dimension dimension = new Dimension((int) (frame.getWidth() / (main_panel_columns * 1.1)), (int) (size * 1.4));
		txt.setPreferredSize(dimension);
		return txt;
	}

	private JTextArea createTableText(String text, float size, Color background, Color foreground) {
		JTextArea txt = new JTextArea(text);
		txt.setFont(txt.getFont().deriveFont(size));
		txt.setForeground(background);
		txt.setBackground(foreground);
		txt.setBorder(BorderFactory.createEmptyBorder());
		txt.setEditable(false);
		Dimension dimension = new Dimension((int) (frame.getWidth() / (main_panel_columns * 1.1)), (int) (size * 1.4));
		txt.setPreferredSize(dimension);
		return txt;
	}

	public void actionPerformed(ActionEvent evt) {
		System.out.println("Action Command: " + evt.getActionCommand());
		if (current_page.equals("startup")) { // On startup page

		} else if (current_page.equals("settings")) { // On settings page
			if (evt.getActionCommand().equals("Apply")) {
				saveSettings();
				initConfigPanels();
			} else if (evt.getActionCommand().equals("Return")) {
				initStartupPanels();
				current_page = "startup";
			} else if (evt.getActionCommand().equals("Remove")) {
				((JTextField) getFromMainPanel(4, 2)).setText("null");
			}
		}
		if (evt.getActionCommand().equals("Connect")) { // header
			connected = true;
			main.connect();
		} else if (evt.getActionCommand().equals("Disconnect")) {
			connected = false;
			main.disconnect();
		} else if (evt.getActionCommand().equals("Settings")) {
			initConfigPanels();
			current_page = "settings";
		} else if (evt.getActionCommand().equals("Open Gifs")) {
			((JButton) getFromMainPanel(1, 0)).setText("Close Gifs");
			initGifs();
		} else if (evt.getActionCommand().equals("Close Gifs")) {
			if (!main.getBot().isPlayingGif()) {
				gif_frame.dispose();
				((JButton) getFromMainPanel(1, 0)).setText("Open Gifs");
			}
		} else if(evt.getActionCommand().equals("Add Gif")) {
			String s = JOptionPane.showInputDialog(
					"Please write the following Syntax if the gif has no soundfile: Gifname;Duration;Cost \r\nElse use this Syntax: Gifname;Cost");
			if(s != null) {
				String[] split = s.split(";");
				if (split.length == 2) {
					main.getBot().getGifList().add(new Gif(split[0], Integer.parseInt(split[1])));
				} else if (split.length == 3) {
					main.getBot().getGifList()
							.add(new Gif(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
				}
			}
		}
		update();
	}

	private void initGifs() {
		if (!gif_frame_open) {
			gif_frame = new JFrame("Dank and funny gifs");
			gif_frame.setSize(720, 480);
			gif_frame.getContentPane().setBackground(new Color(0, 0, 0));
			gif_frame.setAutoRequestFocus(false);
			gif_frame.setFocusable(false);
			gif_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			gif_frame_open = true;
			gif_frame.addWindowListener(new WindowListener() {
				public void windowActivated(WindowEvent e) {
				}

				public void windowClosed(WindowEvent e) {
					gif_frame_open = false;
				}

				public void windowClosing(WindowEvent e) {
					gif_frame.dispose();
				}

				public void windowDeactivated(WindowEvent e) {
				}

				public void windowDeiconified(WindowEvent e) {
				}

				public void windowIconified(WindowEvent e) {
				}

				public void windowOpened(WindowEvent e) {
				}
			});
			gif_frame.setVisible(true);
		}
	}

	public JFrame getGifFrame() {
		return gif_frame;
	}

	public boolean getGifsAlive() {
		return gif_frame_open;
	}

	public void saveSettings() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file_configs, false));
			String channel = ((JTextField) getFromMainPanel(4, 1)).getText().toLowerCase().trim();
			String currency = ((JTextField) getFromMainPanel(4, 2)).getText().replace(" ", "_");
			String botname = ((JTextField) getFromMainPanel(4, 3)).getText().toLowerCase().trim();
			String botoauth = ((JTextField) getFromMainPanel(4, 4)).getText().toLowerCase().trim();
			@SuppressWarnings("unchecked")
			String colorset = ((String) (((JComboBox<String>) getFromMainPanel(4, 5)).getSelectedItem())).toLowerCase()
					.trim();
			bw.append("#CHANNEL:");
			bw.append(channel);
			bw.newLine();
			bw.append("#CURRENCY:");
			bw.append(currency);
			bw.newLine();
			bw.append("#BOTNAME:");
			bw.append(botname);
			bw.newLine();
			bw.append("#BOTOAUTH:");
			bw.append(botoauth);
			bw.newLine();
			bw.append("#COLORSET:");
			bw.append(colorset);
			bw.close();
			main.getBot().setChannel(channel);
			main.getBot().setCurrency(currency);
			main.getBot().setAccount(botname, botoauth);
			setColorset(colorset);
			update();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onDisconnect() {
		connected = false;
	}

	public void setColorset(String colorset) {
		this.colorset = colorset;
		if (colorset.equals("dark")) {
			color_background = COLOR_DARK_BACKGROUND;
			color_foreground = COLOR_DARK_FOREGROUND;
			color_font = COLOR_DARK_FONT;
			color_textfield_background = COLOR_DARK_TEXTFIELD_BACKGROUND;
			color_button_background = COLOR_DARK_BUTTON_BACKGROUND;
			color_button_foreground = COLOR_DARK_BUTTON_FOREGROUND;
			color_headline = COLOR_DARK_HEADLINE;
		} else if (colorset.equals("bright")) {
			color_background = COLOR_BRIGHT_BACKGROUND;
			color_foreground = COLOR_BRIGHT_FOREGROUND;
			color_font = COLOR_BRIGHT_FONT;
			color_textfield_background = COLOR_BRIGHT_TEXTFIELD_BACKGROUND;
			color_button_background = COLOR_BRIGHT_BUTTON_BACKGROUND;
			color_button_foreground = COLOR_BRIGHT_BUTTON_FOREGROUND;
			color_headline = COLOR_BRIGHT_HEADLINE;
		} else if (colorset.equals("colorful")) {
			color_background = COLOR_COLORFUL_BACKGROUND;
			color_foreground = COLOR_COLORFUL_FOREGROUND;
			color_font = COLOR_COLORFUL_FONT;
			color_textfield_background = COLOR_COLORFUL_TEXTFIELD_BACKGROUND;
			color_button_background = COLOR_COLORFUL_BUTTON_BACKGROUND;
			color_button_foreground = COLOR_COLORFUL_BUTTON_FOREGROUND;
			color_headline = COLOR_COLORFUL_HEADLINE;
		}
	}

}
