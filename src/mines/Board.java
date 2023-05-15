package mines;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * La classe Board représente le plateau de jeu pour le jeu de démineur.
 */
public class Board extends JPanel {
	private static final long serialVersionUID = 6195235521361212179L;
	
	private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private int[] field;
    private boolean inGame;
    private int mines_left;
    private Image[] img;
    private int mines = 40;
    private int rows = 16;
    private int cols = 16;
    private int all_cells;
    private JLabel statusbar;


    /**
 * Constructeur de la classe Board.
 * Initialise le plateau de jeu et charge les images nécessaires.
 * @param statusbar une étiquette pour afficher le statut du jeu
 * @return none
 */
public Board(JLabel statusbar) {

    // Initialise l'étiquette de statut du jeu
    this.statusbar = statusbar;

    // Charge les images nécessaires pour le jeu
    img = new Image[NUM_IMAGES];
    for (int i = 0; i < NUM_IMAGES; i++) {
        img[i] = (new ImageIcon(getClass().getClassLoader().getResource((i) + ".gif"))).getImage();
    }

    // Active le double buffering pour améliorer les performances graphiques
    setDoubleBuffered(true);

    // Ajoute un écouteur de souris pour gérer les clics de l'utilisateur
    addMouseListener(new MinesAdapter());

    // Démarre une nouvelle partie
    newGame();
}



/**
 * Initialise une nouvelle partie de démineur.
 * Génère un nouveau plateau de jeu avec des mines aléatoires.
 * @param none
 * @return none
 */
public void newGame() {

    // Initialise les variables nécessaires pour la nouvelle partie
    Random random;
    int current_col;
    int i = 0;
    int position = 0;
    int cell = 0;
    random = new Random();
    inGame = true;
    mines_left = mines;
    all_cells = rows * cols;
    field = new int[all_cells];

    // Initialise chaque cellule du plateau de jeu avec une case cachée
    for (i = 0; i < all_cells; i++)
        field[i] = COVER_FOR_CELL;

    // Affiche le nombre de mines restantes dans l'étiquette de statut
    statusbar.setText(Integer.toString(mines_left));

    // Place les mines aléatoirement sur le plateau de jeu
    i = 0;
    while (i < mines) {

        // Génère une position aléatoire pour la mine
        position = (int) (all_cells * random.nextDouble());

        // Vérifie que la position est valide et qu'il n'y a pas déjà une mine à cet endroit
        if ((position < all_cells) && (field[position] != COVERED_MINE_CELL)) {

            // Calcule la colonne actuelle de la position
            current_col = position % cols;

            // Place une mine à la position choisie
            field[position] = COVERED_MINE_CELL;
            i++;

            // Incrémente le nombre de mines adjacentes pour chaque cellule voisine
            if (current_col > 0) { 
                cell = position - 1 - cols;
                if (cell >= 0)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                cell = position - 1;
                if (cell >= 0)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                cell = position + cols - 1;
                if (cell < all_cells)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
            }

            cell = position - cols;
            if (cell >= 0)
                if (field[cell] != COVERED_MINE_CELL)
                    field[cell] += 1;
            cell = position + cols;
            if (cell < all_cells)
                if (field[cell] != COVERED_MINE_CELL)
                    field[cell] += 1;

            if (current_col < (cols - 1)) {
                cell = position - cols + 1;
                if (cell >= 0)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                cell = position + cols + 1;
                if (cell < all_cells)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                cell = position + 1;
                if (cell < all_cells)
                    if (field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
            }
        }
    }
}


/**
 * Recherche toutes les cellules vides adjacentes à la cellule donnée et les révèle.
 * Utilise la récursivité pour parcourir toutes les cellules vides adjacentes.
 * @param j l'indice de la cellule à partir de laquelle la recherche doit commencer
 * @return none
 */
public void find_empty_cells(int j) {

    // Calcule la colonne actuelle de la cellule
    int current_col = j % cols;
    int cell;

    // Parcourt toutes les cellules adjacentes à la cellule donnée
    if (current_col > 0) { 
        cell = j - cols - 1;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }

        cell = j - 1;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }

        cell = j + cols - 1;
        if (cell < all_cells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }
    }

    cell = j - cols;
    if (cell >= 0)
        if (field[cell] > MINE_CELL) {
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL)
                find_empty_cells(cell);
        }

    cell = j + cols;
    if (cell < all_cells)
        if (field[cell] > MINE_CELL) {
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL)
                find_empty_cells(cell);
        }

    if (current_col < (cols - 1)) {
        cell = j - cols + 1;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }

        cell = j + cols + 1;
        if (cell < all_cells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }

        cell = j + 1;
        if (cell < all_cells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }
    }

}


   /**
 * Dessine le champ de jeu en utilisant les images appropriées pour chaque cellule.
 * @param g l'objet Graphics utilisé pour dessiner le champ de jeu
 * @return none
 */
public void paint(Graphics g) {

    // Initialisation des variables
    int cell = 0;
    int uncover = 0;

    // Parcourt toutes les cellules du champ de jeu
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {

            // Récupère la valeur de la cellule
            cell = field[(i * cols) + j];

            // Vérifie si le jeu est terminé et s'il y a une mine dans la cellule
            if (inGame && cell == MINE_CELL)
                inGame = false;

            // Détermine quelle image doit être utilisée pour la cellule
            if (!inGame) {
                if (cell == COVERED_MINE_CELL) {
                    cell = DRAW_MINE;
                } else if (cell == MARKED_MINE_CELL) {
                    cell = DRAW_MARK;
                } else if (cell > COVERED_MINE_CELL) {
                    cell = DRAW_WRONG_MARK;
                } else if (cell > MINE_CELL) {
                    cell = DRAW_COVER;
                }
            } else {
                if (cell > COVERED_MINE_CELL)
                    cell = DRAW_MARK;
                else if (cell > MINE_CELL) {
                    cell = DRAW_COVER;
                    uncover++;
                }
            }

            // Dessine l'image appropriée pour la cellule
            g.drawImage(img[cell], (j * CELL_SIZE),
                (i * CELL_SIZE), this);
        }
    }

    // Vérifie si le jeu est terminé et met à jour la barre de statut en conséquence
    if (uncover == 0 && inGame) {
        inGame = false;
        statusbar.setText("Game won");
    } else if (!inGame)
        statusbar.setText("Game lost");
}



 /**
 * Classe qui gère les événements de souris pour le jeu de démineur.
 */
class MinesAdapter extends MouseAdapter {

    /**
     * Gère l'événement de clic de souris.
     * @param e l'objet MouseEvent qui contient les informations sur l'événement de clic de souris
     * @return none
     */
    public void mousePressed(MouseEvent e) {

        // Récupère les coordonnées de la souris
        int x = e.getX();
        int y = e.getY();

        // Calcule la colonne et la rangée de la cellule cliquée
        int cCol = x / CELL_SIZE;
        int cRow = y / CELL_SIZE;

        // Variable pour indiquer si la méthode doit redessiner le champ de jeu
        boolean rep = false;

        // Si le jeu est terminé, commence une nouvelle partie et redessine le champ de jeu
        if (!inGame) {
            newGame();
            repaint();
        }

        // Vérifie si le clic de souris est dans les limites du champ de jeu
        if ((x < cols * CELL_SIZE) && (y < rows * CELL_SIZE)) {

            // Si le clic est avec le bouton droit de la souris
            if (e.getButton() == MouseEvent.BUTTON3) {

                // Vérifie si la cellule cliquée est marquée
                if (field[(cRow * cols) + cCol] > MINE_CELL) {
                    rep = true;

                    // Si la cellule est couverte, ajoute un marqueur de mine
                    if (field[(cRow * cols) + cCol] <= COVERED_MINE_CELL) {
                        if (mines_left > 0) {
                            field[(cRow * cols) + cCol] += MARK_FOR_CELL;
                            mines_left--;
                            statusbar.setText(Integer.toString(mines_left));
                        } else
                            statusbar.setText("No marks left");
                    } else {

                        // Si la cellule est déjà marquée, enlève le marqueur de mine
                        field[(cRow * cols) + cCol] -= MARK_FOR_CELL;
                        mines_left++;
                        statusbar.setText(Integer.toString(mines_left));
                    }
                }

            // Si le clic est avec le bouton gauche de la souris
            } else {

                // Vérifie si la cellule cliquée est couverte ou marquée
                if (field[(cRow * cols) + cCol] > COVERED_MINE_CELL) {
                    return;
                }

                // Si la cellule cliquée contient une mine, le jeu est terminé
                if ((field[(cRow * cols) + cCol] > MINE_CELL) &&
                    (field[(cRow * cols) + cCol] < MARKED_MINE_CELL)) {

                    field[(cRow * cols) + cCol] -= COVER_FOR_CELL;
                    rep = true;

                    if (field[(cRow * cols) + cCol] == MINE_CELL)
                        inGame = false;
                    if (field[(cRow * cols) + cCol] == EMPTY_CELL)
                        find_empty_cells((cRow * cols) + cCol);
                }
            }

            // Si la méthode doit redessiner le champ de jeu, appelle la méthode repaint()
            if (rep)
                repaint();

        }
    }
}

}
