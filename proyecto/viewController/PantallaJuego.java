package viewController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Model.GameBoard;

public class PantallaJuego extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private Image backgroundImage;
	private JLabel[][] labels;
	private int height = GameBoard.getGameBoard().getHeight();
	private int width = GameBoard.getGameBoard().getWidth();
	private Controlador2 controlador2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PantallaJuego(int x, int y) {
		addKeyListener(getControlador2());
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		requestFocusInWindow();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700); // Much larger for 100x60 grid
		GameBoard.getGameBoard().addObserver(this); // Se añaden los observers

		URL imgUrl = PantallaJuego.class.getResource("img/fondo.jpg");
		if (imgUrl != null) {
			backgroundImage = new ImageIcon(imgUrl).getImage();
		}

		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		panel = new JPanel(new GridLayout(60, 100, 0, 0));
		panel.setOpaque(false);
		contentPane.add(panel, BorderLayout.CENTER);
		labels = new JLabel[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				JLabel label = new JLabel();
				label.setOpaque(false);
				panel.add(label);
				labels[i][j] = label;
			}
		}
		JLabel labelN = labels[y][x];
		labelN.setOpaque(true);
		labelN.setBackground(Color.red);
		labelN.setBorder(BorderFactory.createLineBorder(Color.gray));

	}

	private Controlador2 getControlador2() {
		if (controlador2 == null) {
			controlador2 = new Controlador2();
		}
		return controlador2;
	}

	private class Controlador2 implements KeyListener {
		// Tras pulsar el botón, abrir la otra pantalla
		@Override
		public void keyPressed(KeyEvent e) {

			GameBoard board = GameBoard.getGameBoard();

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				board.moverNave(-1);
			}

			if (key == KeyEvent.VK_RIGHT) {
				board.moverNave(1);
			}

			if (key == KeyEvent.VK_UP) {
				board.moverNaveV(-1);
			}
			if (key == KeyEvent.VK_DOWN) {
				board.moverNaveV(1);
			}

			if (key == KeyEvent.VK_SPACE) {
				board.disparar();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public void keyReleased(KeyEvent e) {
		// metodo que no hace absolutamente nada y que lo meto pq sino no furrula
	}

	public void keyTyped(KeyEvent e) {
		// mas de lo mismo

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof GameBoard && labels != null) {
			int[] posN = (int[]) arg;
			GameBoard board = (GameBoard) o;
			SwingUtilities.invokeLater(() -> {
				for (int i = 0; i < board.getHeight(); i++) {
					for (int j = 0; j < board.getWidth(); j++) {
						Model.Casilla c = board.getCasilla(j, i);
						boolean esEnemigo = (c instanceof Model.Enemigo);
						boolean esNave = (i == posN[0] && j == posN[1]);
						if (esNave) {
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.red);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (esEnemigo) {
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.GREEN);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (c instanceof Model.Disparo) {
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.YELLOW);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else {
							labels[i][j].setOpaque(false);
							labels[i][j].setBorder(null);

						}
					}
				}
				panel.repaint();
			});
		}
	}
}
