package viewController;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Model.GameBoard;

public class PantallaPrincipal extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnIniciar;
	private Image backgroundImage;
	private Image logoSI;
	private Controlador controlador;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrincipal frame = new PantallaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PantallaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		GameBoard.getGameBoard().addObserver(this);

		URL imgUrl = PantallaPrincipal.class.getResource("img/fondo.jpg");
		URL imgFondo = PantallaPrincipal.class.getResource("img/Space_invaders_logo.svg.png");
		if (imgUrl != null) {
			backgroundImage = new ImageIcon(imgUrl).getImage();
		}
		if (imgFondo != null) {
			logoSI = new ImageIcon(imgFondo).getImage();
		}
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
				if (logoSI != null) {
					int logoWidth = getWidth() / 2;
					int logoHeight = logoSI.getHeight(null) * logoWidth / logoSI.getWidth(null);
					int x = (getWidth() - logoWidth) / 2;
					int y = getHeight() / 4;
					g.drawImage(logoSI, x, y, logoWidth, logoHeight, this);
				}
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getBtnIniciar(), BorderLayout.SOUTH);
	}

	private Controlador getControlador() {
		if (controlador == null) {
			controlador = new Controlador();
		}
		return controlador;
	}

	private class Controlador implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			GameBoard.getGameBoard().crearTablero();
		}
	}

	private JButton getBtnIniciar() {
		if (btnIniciar == null) {
			btnIniciar = new JButton("iniciar");
			btnIniciar.addActionListener(getControlador());
		}
		return btnIniciar;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof GameBoard && arg instanceof int[]) {
			int[] listo = (int[]) arg;
			if (listo[0] == 1) {
				int x = listo[2];
				int y = listo[1];
				PantallaJuego juego = new PantallaJuego(x, y);
				juego.setVisible(true);
				GameBoard.getGameBoard().deleteObserver(this);
				dispose();
			}
		}
	}
}
