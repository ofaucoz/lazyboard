package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import controler.Controler;
import observer.Observer;

public class Interface extends JFrame implements Observer {
	private ArrayList<String> list_command= new ArrayList<String>();
	private JLayeredPane container= new JLayeredPane();
	private Controler controler;
	private JLabel information = new JLabel();
    private JPanel panelTop = new JPanel();
    private JPanel panelBottom = new JPanel();

	public Interface(Controler controler) {
		this.setSize(600, 400);
        this.setLayout(new BorderLayout());
		container.setBounds(0, 0, 600, 400);
		this.add(container, BorderLayout.CENTER);
		this.setTitle("Lazyboard");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//this.pack();
		this.setVisible(true);
		this.setContentPane(container);
		this.controler = controler;
		
		initialize();
	}

	public void initialize() {
		try {
			BufferedImage buttonIcon = ImageIO.read(new File("./resources/power_on.png"));
			Font police = new Font("Arial", Font.BOLD, 20);
			information = new JLabel("Waiting to start...");
			information.setFont(police);
			//information.setHorizontalAlignment(JLabel.RIGHT);
			//information.setPreferredSize(new Dimension(220, 20));
			JButton startButton = new JButton(new ImageIcon(buttonIcon));
			startButton.addActionListener(new StartListener());
			startButton.setBorder(BorderFactory.createEmptyBorder());
			startButton.setContentAreaFilled(false);
			panelTop.setBounds(0, 0, 600, 400);
			panelBottom.setBounds(0, 300, 600, 400);
			//panelBottom.setBounds(200, 100, 300, 300);
			panelTop.add(startButton);
			panelBottom.add(information);
			container.add(panelTop, new Integer(0), 0);
			container.add(panelBottom, new Integer(1), 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	class StartListener implements ActionListener {
		private int time_clicked = 0;
		public void actionPerformed(ActionEvent e) {
			time_clicked ++;
			if(time_clicked == 1) {
				controler.start();
			}
			else {
				System.exit(0);
			}
		}
	}
	
	public void update(String str) {
	    information.setText(str);
	  }  

}
