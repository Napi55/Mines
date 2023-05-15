package mines;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;



/**
 * Constructeur de la classe Mines.
 * Initialise la fenêtre du jeu et crée un nouveau plateau de jeu.
 * @param none
 * @return none
 */
public Mines() {

    // Ferme la fenêtre lorsque l'utilisateur clique sur le bouton de fermeture
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Définit la taille de la fenêtre
    setSize(WIDTH, HEIGHT);

    // Centre la fenêtre sur l'écran
    setLocationRelativeTo(null);

    // Définit le titre de la fenêtre
    setTitle("Minesweeper");

    // Crée une étiquette pour afficher le statut du jeu
    statusbar = new JLabel("");
    add(statusbar, BorderLayout.SOUTH);

    // Crée un nouveau plateau de jeu et l'ajoute à la fenêtre
    add(new Board(statusbar));

    // Empêche l'utilisateur de redimensionner la fenêtre
    setResizable(false);

    // Rend la fenêtre visible
    setVisible(true);
}

    
    public static void main(String[] args) {
        new Mines();
    }
}
