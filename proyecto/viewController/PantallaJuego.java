package viewController;

import Model.GameBoard;
import Model.JugadorManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PantallaJuego extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private Image backgroundImage;
	private JLabel[][] labels;
	private Controlador2 controlador2;
	private int boardHeight = 60;
	private int boardWidth = 100;
	private JLabel armaLabel;

	/**
	 * Create the frame.
	 */
	public PantallaJuego() {
		addKeyListener(getControlador2());
		addWindowListener(getControlador2());
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		requestFocusInWindow();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700); // Much larger for 100x60 grid
		GameBoard.getGameBoard().addObserver(this); // Se a√±aden los observers

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
		panel = new JPanel(new GridLayout(boardHeight, boardWidth, 0, 0));
		panel.setOpaque(false);
		contentPane.add(panel, BorderLayout.CENTER);

		labels = new JLabel[boardHeight][boardWidth];
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				JLabel label = new JLabel();
				label.setOpaque(false);
				panel.add(label);
				labels[i][j] = label;
			}
		}
		armaLabel = new JLabel("Arma: -    MuniciÛn: -");
        armaLabel.setForeground(Color.WHITE);
        armaLabel.setFont(armaLabel.getFont().deriveFont(14f));
        armaLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        JPanel hudPanel = new JPanel(new BorderLayout());
        hudPanel.setBackground(new Color(0, 0, 0, 180));
        hudPanel.add(armaLabel, BorderLayout.WEST);
        contentPane.add(hudPanel, BorderLayout.SOUTH);
	}

	private Controlador2 getControlador2() {
		if (controlador2 == null) {
			controlador2 = new Controlador2();
		}
		return controlador2;
	}

	private class Controlador2 implements KeyListener, WindowListener {
		// Tras pulsar el bot√≥n, abrir la otra pantalla
		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
				if (key == KeyEvent.VK_LEFT) {
					JugadorManager.getJugadorManager().moverNave(-1, 0);
				}

				if (key == KeyEvent.VK_RIGHT) {
					JugadorManager.getJugadorManager().moverNave(1, 0);
				}

				if (key == KeyEvent.VK_UP) {
					JugadorManager.getJugadorManager().moverNave(0, -1);
				}
				if (key == KeyEvent.VK_DOWN) {
					JugadorManager.getJugadorManager().moverNave(0, 1);
				}

				if (key == KeyEvent.VK_SPACE) {
					JugadorManager.getJugadorManager().disparar();
					actualizarHUD();
				}

				if (  key == KeyEvent.VK_C) {
					JugadorManager.getJugadorManager().cambiarArma();
					actualizarHUD();
				}
				GameBoard.getGameBoard().actualizarTablero();
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowOpened(WindowEvent e) {
			GameBoard.getGameBoard().actualizarTablero();
			actualizarHUD();
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	}
	
	private void actualizarHUD() {
	    String nombre = JugadorManager.getJugadorManager().getNombreArmaActual();
	    String municion = JugadorManager.getJugadorManager().getMunicionArmaActual();
	    armaLabel.setText("Arma: " + nombre + "    MuniciÛn: " + municion + "    [C] cambiar");
	}
	    
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Object[] && labels != null) {
			Object[] total = (Object[]) arg;
			int fin = (int) total[0];
			int[][] snapshot = (int[][]) total[1];
			SwingUtilities.invokeLater(() -> {
				int boardHeight = snapshot.length;
				int boardWidth = snapshot[0].length;
				for (int i = 0; i < boardHeight; i++) {
					for (int j = 0; j < boardWidth; j++) {
						int tipo = snapshot[i][j];
						if (tipo == 1) { // Nave
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.red);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (tipo == 0) { // Enemigo
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.GREEN);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (tipo == 2) { // Disparo
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.YELLOW);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (tipo == 4) { // nave de color verde
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.green);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (tipo == 5) { // nave de color azul
							labels[i][j].setOpaque(true);
							labels[i][j].setBackground(Color.BLUE);
							labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else if (tipo == 6) {
						    labels[i][j].setOpaque(true);
						    labels[i][j].setBackground(Color.MAGENTA);
						    labels[i][j].setBorder(BorderFactory.createLineBorder(Color.gray));
						} else { // Vacia
							labels[i][j].setOpaque(false);
							labels[i][j].setBorder(null);

						}
					}
				}
				if (fin == 2) {
					JOptionPane.showMessageDialog(contentPane, "GAME OVER", "TRY IT AGAIN",
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0); // Se cierra la pantalla. Pero igual es mejor forma hacer que al pulsar OK,
									// vuelva a la pantalla ppal para reiniciar juego.
				}
			});
		}
	}
}
