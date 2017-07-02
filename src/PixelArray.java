import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * PixelArray: A class developed by Fahmi Ncibi, it contains methods to display Graphical array of data
 * Date: August 28, 2014
 * Published: July 1, 2017
 */
public class PixelArray extends JFrame{

	private static final long serialVersionUID = 1L;

	private String windowTitle=null; // Titre de la fenetre
	private JScrollPane scrollTable=null; // Barre de défilement
	private JTable table=null; // tableau graphique qui s'affiche dans la fenetre, qui va contenir les valeurs des matrices de pixel
	private JPanel panelTable=null; // panneau qui va contenir la table de pixels
	int[][] pixelArray=null; // matrice de pixel de l'image en format entier
	private String[][] pixelArrayStr=null; // matrice de chaine de caractère qui contient les valeurs héxadécimale de couleur de pixel
	private String[] titles=null; // les titre dans le tableau graphique
	private int width; // la largeur de l'image
	private int height; // l'hauteur de l'image
	private Object[][] pixelObject; // un objet qui va contenir la matrice de pixel (entier ou hexadecimale) utile pour construire le tableau graphique, puisque l'objet JTable prend en parametre un Object

	/* Constructeur par défaut, vide */
	public PixelArray(){
		super();
	}

	/* 2eme Constructeur qui prend en paramètre le titre de la fenetre, une matrice qui contient les valeurs hexadécimales
	 * des couleurs de l'image, la largeur et l'hauteur de l'image.
	 */
	public PixelArray(String windowTitle,String[][] pixelArrayStr,int width,int height){
		// appel aux membres de la classe mere (JFrame)
		super();
		// initialisation des attributs de la classe:
		this.windowTitle=windowTitle;
		this.width=width;
		this.height=height;
		this.pixelArrayStr=pixelArrayStr;
		// appel a la méthode qui va construire la fenetre et remplir le tableau graphique:
		init();
		setVisible(true); // affiche la fenetre a l'ecran
	}

	/* 3eme Constructeur qui prend en paramètre le titre de la fenetre, une matrice qui contient les valeurs entieres
	 * des couleurs de l'image, la largeur et l'hauteur de l'image.
	 */
	public PixelArray(String windowTitle,int[][] pixelArray,int width,int height){
		//appel aux membres de la classe mere (JFrame)
		super();
		// initialisation des attributs de la classe:
		this.windowTitle=windowTitle;
		this.width=width;
		this.height=height;
		this.pixelArray=pixelArray;
		// appel a la méthode qui va construire la fenetre et remplir le tableau graphique:
		init();
		setVisible(true);
	}

	/*
	 * méthode qui initialise la fenetre principale, remplir le tableau graphique
	 */
	private void init() {
		// Test sur la matrice passée en parametre
		if(pixelArray!=null) // si la matrice passée est une matrice d'entier on fait une conversion entier => Object, on appel la méthode de conversion avec une matrice d'entiers
			pixelObject=convertArray2Object(pixelArray,null,width,height);
		if(pixelArrayStr!=null)// si la matrice passée est une matrice de chaine de caracteres on fait une conversion string => object, on appel la methode avec une matrice de string
			pixelObject=convertArray2Object(null,pixelArrayStr,width,height);
		titles=new String[width]; // tableau qui contient les titres de la tableau graphique JTable
		for(int i=0;i<width;i++)
			titles[i]=""+i;

		table=new JTable(pixelObject,titles); // creation de l'objet JTable
		panelTable=new JPanel(); // creation du panneau qui va contenir le tableau graphique
		panelTable.add(table); // ajout du tableau graphique au panneau
		scrollTable=new JScrollPane(panelTable); // Ajout d'un barre de défilement au panneau
		scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // la barre de défilement horizentale est toujours visible
		scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // la barre de défilement verticale est toujours visible
		getContentPane().add(scrollTable); // ajout du barre de défilement a la fenetre
		this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH); // la fenetre s'ouvre en pein ecran par défaut
		setTitle(windowTitle); // config le titre de la fenetre
		setSize(new Dimension(300,300)); // taile de la fenetre est 300x300
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);	// l'orsque en ferme la fenetre la fenetre sera masquée

	}

	/* méthode qui convertit une matrice d'entier ou de string en un Object */
	private Object[][] convertArray2Object(int[][] array,String[][] arrayStr,int width,int height) {
		Object[][] object=new Object[width+100][height+100]; // l'objet de retour
		/* parcours du tableau et remplissage de la matrice d'objet */
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				// Test s'il s'agit d'une matrice d'entiers ou de chaine de caracteres:
				if(array!=null)
					object[i][j]=new Integer(array[i][j]);
				else if(arrayStr!=null)
					object[i][j]=new String(arrayStr[i][j]);
			}
		}
		return object;
	}
}
