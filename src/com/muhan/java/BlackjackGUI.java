package com.muhan.joba;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.*;

public class BlackjackGUI extends Canvas implements ActionListener {
	private BufferedImage back;
	// private Board board;
	private Hand dealer;
	private Hand player;
	private int totalWins;
	private int gamesPlayed;
	private Deck deck;
	private static final String[] ranks = { "ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen",
			"king" };
	private static final String[] suits = { "spades", "hearts", "diamonds", "clubs" };
	private static final int[] pointValues = { 11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10 };
	private String message;
	private File file;

	public BlackjackGUI() {
		deck = new Deck(ranks, suits, pointValues);
		setBackground(Color.decode("#007F00"));
		dealer = new BlackjackHand(deck);
		player = new BlackjackHand(deck);
		gamesPlayed = 0;
		message = "";
		setVisible(true);
		
		
//		File file = new File("src/blackjack/music/bjstart.wav");
//		System.out.println(file.exists()); 
//
//		try {
//
//			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
//			Clip clip = AudioSystem.getClip();
//			clip.open(stream);
//			clip.start();
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}

	}

	public void update(Graphics window) {
		paint(window);
	}

	public void paint(Graphics window) {

		Graphics2D twoDGraph = (Graphics2D) window;

		if (back == null)
			back = (BufferedImage) (createImage(getWidth(), getHeight()));

		Graphics graphToBack = back.createGraphics();

		

		graphToBack.setColor(Color.decode("#007F00"));
		graphToBack.fillRect(0, 0, 1500, 1000);
		graphToBack.setFont(new Font("SansSerif", Font.BOLD, 45));
		graphToBack.setColor(Color.WHITE);
		graphToBack.drawString("������ ���ӿ� �°��� ȯ���Ѵ� " , 400, 100);
		graphToBack.setColor(Color.BLACK);
		graphToBack.drawString("    �� �� "  , 25, 270);
		graphToBack.drawString("�÷��̾� " , 25, 570);
		
		if (deck.size() < 4)
			deck = new Deck(ranks, suits, pointValues);
		if (player.size() > 0)
			graphToBack.drawString("�÷��̾� ����: " + player.score(), 1000, 550);
		if (player.score() == 21 || dealer.score() == 21)
			endCheck();
		if (message != "") {
			graphToBack.drawString("���� ����: " + dealer.score(), 1000, 250);
			dealer.draw(graphToBack, 100, 100, false);
		} else
			dealer.draw(graphToBack, 100, 100, true);
		player.draw(graphToBack, 100, 400, false);
		graphToBack.setFont(new Font("SansSerif", Font.PLAIN, 50));
		graphToBack.setColor(Color.PINK);
		graphToBack.drawString(message, 300, 400);
		twoDGraph.drawImage(back, null, 0, 0);
		
	}

	public void run() {
		try {
			while (true) {
				Thread.currentThread().sleep(5);
				repaint();
			}
		} catch (Exception e) {

		}
	}

	public void dealPlayer() {
		player.deal(deck);
		if (player.bust())
			endCheck();

	}

	public void bot() {
		while (dealer.score() <= 17)
			dealer.deal(deck);
	}

	public void endCheck() {
		
			
		
		if (player.won(dealer)) {
		
			message = "";
			totalWins++;
			if (player.score() == 21 && player.size() == 2)
				
				message = "Blackjack! ";
			
			message += "�÷��̾ �¸��Ͽ����ϴ�";
		} else if (player.score() == dealer.score())
			message = " �����ϴ�";
		else {
			message = "������ �¸� �Դϴ�";
		}
		
		gamesPlayed++;

	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(BlackjackGUIRunner.start)) {
			System.out.println("��ŸƮ ��ư�� �����ÿ�");
			player.newGame(deck);
			dealer.newGame(deck);
			message = "";
			repaint();
		}

		else if (e.getSource().equals(BlackjackGUIRunner.hit)) {
			if (message == "")
				dealPlayer();
			else
				
				message = "���۹�ư�� �����ÿ�";
			repaint();
		} else if (e.getSource().equals(BlackjackGUIRunner.stand)) {
			System.out.println("You pressed stand");
			if (message == "") {
				bot();
				endCheck();
			} else
				message = "���۹�ư�� �����ÿ�";
			repaint();

		} else {
			System.out.print("ERROR.could not identify actionevent!");
			return;
		}

	}

}