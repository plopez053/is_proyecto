package viewController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PantallaJuego extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	

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
	public PantallaJuego() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		panel = new JPanel(new GridLayout(60,100,0,0));
		contentPane.add(panel, BorderLayout.CENTER);
		int filas = 60;
		int columnas = 100;
		for (int i = 0; i < filas; i++) {
			for (int j=0; j<columnas;j++) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(Color.black);
				panel.add(label);
				
			}
		}

	}
}
