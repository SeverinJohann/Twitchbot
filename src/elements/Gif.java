package elements;

import java.awt.BorderLayout;
import java.io.FileInputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import def.Main;
import def.ZeitBot;
import javazoom.jl.player.Player;

public class Gif {

	private String name;
	private int cost, duration = 0;
	private ImageIcon image;
	private Player sound;
	private boolean hasSound;
	private String folderPath = Main.FILE_GIFS.getParent();
	
	public Gif(String name, int cost) {
		this.name = name;
		this.image = new ImageIcon(folderPath + "/" + name + ".gif");
		this.cost = cost;
		hasSound = true;
	}
	
	public Gif(String name, int duration, int cost) {
		this.name = name;
		this.image = new ImageIcon(folderPath + "/" + name + ".gif");
		this.duration = duration;
		this.cost = cost;
		hasSound = false;
	}
	
	public void play(ZeitBot bot, JFrame frame) {
		if(!bot.isPlayingGif()) {
			new Thread() {
				@Override
				public void run() {
					bot.setPlayingGif(true);
					JLabel animationLabel = new JLabel(image);
					if(hasSound) {
						frame.getContentPane().add(animationLabel, BorderLayout.SOUTH);
						frame.setVisible(true);
						try {
							try {
								sound = new Player(new FileInputStream(folderPath + "/" + name + ".mp3"));
								sound.play();
								sound.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						frame.getContentPane().add(animationLabel, BorderLayout.SOUTH);
						frame.setVisible(true);
						try {
							Thread.sleep(duration * 100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					frame.setVisible(false);
					frame.getContentPane().remove(animationLabel);
					frame.remove(animationLabel);
					frame.setVisible(true);
					bot.setPlayingGif(false);
				}
			}.start();
		}
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public Player getSound() {
		return sound;
	}

	public void setSound(Player sound) {
		this.sound = sound;
	}

	public boolean isHasSound() {
		return hasSound;
	}

	public void setHasSound(boolean hasSound) {
		this.hasSound = hasSound;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
}
