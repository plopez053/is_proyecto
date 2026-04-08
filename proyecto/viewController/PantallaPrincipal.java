package viewController;

import java.awt.BorderLayout;
import java.awt.Color;
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
	private JPanel panelLogo;
	private JButton btnIniciar;
	private JButton btnRed;
	private JButton btnGreen;
	private JButton btnBlue;
	private Image backgroundImage;
	private Image logoSI;
	private Controlador controlador;

	public PantallaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		GameBoard.getGameBoard().addObserver(this); // Se añaden los observers

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
					// Escalar proporcionalmente el logo a la mitad del ancho del panel
					int logoWidth = getWidth() / 2;
					int logoHeight = logoSI.getHeight(null) * logoWidth / logoSI.getWidth(null); // mantener proporción
					int x = (getWidth() - logoWidth) / 2; // centrar horizontal
					int y = getHeight() / 4; // un poco hacia arriba
					g.drawImage(logoSI, x, y, logoWidth, logoHeight, this);
				}

			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getBtnIniciar(), BorderLayout.SOUTH);
		JPanel panelSeleccion = new JPanel();
        panelSeleccion.setOpaque(false);
        btnRed = new JButton("Red");
        btnGreen = new JButton("Green");
        btnBlue = new JButton("Blue");
        panelSeleccion.add(btnRed);
        panelSeleccion.add(btnGreen);
        panelSeleccion.add(btnBlue);
        contentPane.add(panelSeleccion, BorderLayout.CENTER);
        btnRed.setActionCommand("RED");
        btnRed.setBackground(Color.red);
        btnGreen.setActionCommand("GREEN");
        btnGreen.setBackground(Color.green);
        btnBlue.setActionCommand("BLUE");
        btnBlue.setBackground(Color.BLUE);
        btnRed.addActionListener(getControlador());
        btnGreen.addActionListener(getControlador());
        btnBlue.addActionListener(getControlador());
	}

	private Controlador getControlador() {
		if (controlador == null) {
			controlador = new Controlador();
		}
		return controlador;
	}

	private class Controlador implements ActionListener {
		// Tras pulsar el botón, abrir la otra pantalla
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String comando = e.getActionCommand();
			switch (comando) {

            case "RED":
            	GameBoard.getGameBoard().setTipoNave("BUENO_RED");
                break;

            case "GREEN":
            	GameBoard.getGameBoard().setTipoNave("BUENO_GREEN");
                break;

            case "BLUE":
            	GameBoard.getGameBoard().setTipoNave("BUENO_BLUE");
                break;

            case "INICIAR":
            	if (GameBoard.getGameBoard().getTipoNave() == null) {
					GameBoard.getGameBoard().setTipoNave("BUENO_RED");
	            }
				GameBoard.getGameBoard().crearTablero();
                
                break;
			}
		}
	}

	private JButton getBtnIniciar() {
		if (btnIniciar == null) {
			btnIniciar = new JButton("INICIAR");
			btnIniciar.addActionListener(getControlador());

		}
		return btnIniciar;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Object[]) {
			Object[] data = (Object[]) arg;
			if (data.length > 0 && data[0] instanceof Integer && (int) data[0] == 1) {
				PantallaJuego juego = new PantallaJuego();
				juego.setVisible(true);
				setVisible(false);
			}

		}
	}
}
