import java.awt.AWTException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/*
* ImageTools: a class library created by Fahmi Ncibi, it contains communs functions/methods used for image processing/clustering/features extraction
* It allows you to
* - Get color matrices data (red, blue, green, alpha)
* - Construct image from color matrices data
* - Read image file
* - Create image file
* - Show image in a custom window
* - Change image properties
* - Display arrays in graphical window (datatable)
* - ...etc.

* Date: August 28, 2014
* Published: July 1, 2017
*/
public class ImageTools {
	private static BufferedImage myImage; // La sous-classe BufferedImage décrit d'une image avec un buffer de données d'image accessible.
	static final int RED_COLOR_MAP=1; // drapeau pour dire qu'on va choisir le plan de couleur rouge
	static final int GREEN_COLOR_MAP=2; // drapeau pour dire qu'on va choisir le plan de couleur vert
	final static int BLUE_COLOR_MAP=3; // drapeau pour dire qu'on va choisir le plan de couleur bleu
	private static int[][] pixelArray=null; // Matrice qui va contenir les échantillons
	private static int imageWidth=320; // largeur de l'image
	private static int imageHeight=240; // hauteur de l'image
	private static int[][] redPixels = null; // Matrice de pixel du plan rouge
	private static int[][] greenPixels = null; // Matrice de pixel du plan rouge
	private static int[][] bluePixels = null; // Matrice de pixel du plan rouge
	private static Image myImageObject=null;

	public ImageTools(){
		super();
	}

	public ImageTools(String fileName){
		super();
		File imageFile;
		try {
			imageFile=new File(fileName);
			myImage=ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("Erreur de lecture de l'image");
			new JOptionPane();
			JOptionPane.showMessageDialog(null, "Erreur de lecture du fichier", "Erreur",JOptionPane.ERROR_MESSAGE); // afficher un message d'erreur
			e.printStackTrace();
		}
		// Load in an image
		myImageObject = Toolkit.getDefaultToolkit().getImage("test.jpg");

        // Make sure it gets loaded
        MediaTracker tracker = new MediaTracker(null);
        tracker.addImage(myImageObject, 0);
        System.out.println("Initialized tracker.");
        try {
            System.out.println("Waiting for tracker...");
            tracker.waitForID(0);
        } catch (Exception e) {
            return;
        }
        System.out.println("Loaded image.");
	}


	// 2eme constructeur qui prend comme parametre un objer BufferedIamge
	public ImageTools(BufferedImage image){
		this.myImage=image;
		this.imageWidth=image.getWidth();
		this.imageHeight=image.getHeight();
	}

	public static int getImageWidth(){
		return myImage.getWidth();
	}
	public static int getImageHeight(){
		return myImage.getHeight();
	}

	/*
	 *  Méthode qui retourne la matrice correspondante au fichier image JPEG "imagFile":
	 *  Elle utilise un objet 'BufferedImage' pour récuperer la matrice RGB 'pixelArray' de l'image
	 */

	public static int[][] getArrayFromImage(File imageFile){
		BufferedImage myImage=null; // l'objet qui nous permet de manipuler l'image
		int[][] array=null;
		try{
	        myImage=ImageIO.read(imageFile); //La sous-classe BufferedImage décrit d'une image avec un buffer de données d'image accessible.
	        imageHeight=myImage.getHeight(); // l'hauteur de l'image a partir de l'objet BufferedImage
	        imageWidth=myImage.getWidth(); // la largeur de l'image a partir de l'objet BufferedImage
	        array=new int[imageWidth][imageHeight]; // matrice qui va contenir la matrice RGB de l'image
	        for(int i=0;i<imageWidth;i++){
	            for(int j=0;j<imageHeight;j++){
	            	array[i][j]=myImage.getRGB(i, j); // Récupère la composante RGB de l'image a partir de l'objet BufferedImage
	            }
	        }
		}
		catch(Exception ee){
			ee.printStackTrace();
			System.err.println("Erreur de lecture du fichier image");
		}
		return array;
	}

	/*
	 * Méthode qui construit une image a partir d'une matrice de pixel RGB:
	 * On a utilisé la méthode "setRGB()" de l'objet BufferedImage, elle a comme paramètre:la matrice de pixel RGB et les positions (en x et y)
	 */
	public static Image writeImageFromArray(int[][] pixelArray,String fileName){
		BufferedImage myReconstImage=new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_BYTE_GRAY); // l'image reconstruite , image en niveau de gris grace au flag "BufferedImage.TYPE_BYTE_GRAY"
			try{
	        for(int y=0;y<imageHeight;y++){
	            for(int x=0;x<imageWidth;x++){
	            	myReconstImage.setRGB(x, y, pixelArray[x][y]);   //  Remplissage de l'image en utilisant la méthode setRGB a partir de la matrice RGB
	            }
	        }
	         File outputfile = new File(fileName); // Création d'un nouveau fichier qui va contenir l'image reconstruite
	            ImageIO.write(myReconstImage, "jpg", outputfile); // écriture de l'image dans le fichier
        }
        catch(Exception ee){
            ee.printStackTrace();
        }
		return myReconstImage; // retourne l'image reconstruite
	}

	/*
	 * Méthode qui retourne la matrice de plan de couleur rouge, vert ou bleu, ca depend de la variable 'colorMap':
	 * 		si colorMap=RED_COLOR_MAP (1) => retarne la matrice du plan rouge
     *  	si colorMap=BLUE_COLOR_MAP (2) => retourne la matrice du plan bleu
     *  	si colorMap=GREEN_COLOR_MAP (3) => retourne la matrice du plan vert
	 */
	public static int[][] getSingleColorMapArray(File imageFile,int colorMap){
		int[][] array=null;
		try{
	        BufferedImage myImage=ImageIO.read(imageFile); //La sous-classe BufferedImage décrit d'une image avec un buffer de données d'image accessible.
	        imageHeight=myImage.getHeight(); // l'hauteur de l'image a partir de l'objet BufferedImage
	        imageWidth=myImage.getWidth(); // la largeur de l'image a partir de l'objet BufferedImage
	        array=new int[imageWidth][imageHeight]; // matrice qui va contenir la matrice RGB de l'image
	        redPixels=new int[imageWidth][imageHeight]; // matrice du plan rouge de l'image
	        greenPixels=new int[imageWidth][imageHeight]; // matrice du plan rouge de l'image
	        bluePixels=new int[imageWidth][imageHeight]; //  matrice du plan rouge de l'image
	        for(int i=0;i<imageWidth;i++){
	            for(int j=0;j<imageHeight;j++){
	            	array[i][j]=myImage.getRGB(i, j);                   // Récupère la composante RGB de l'image a partir de l'objet BufferedImage
	                redPixels[i][j]=  getRed(array[i][j]);                    // Récupère le plan rouge de l'image
	                greenPixels[i][j] = getGreen(array[i][j]);             // Récupère le plan vert de l'image
	                bluePixels[i][j] = getBlue(array[i][j]);               // Récupère le plan bleu de l'image
	            }
	        }
		}
        catch(Exception ee){
            ee.printStackTrace();
        }
        /*
         *  la matrice retournée dépend de la variable d'entrée 'colorMap',
         *  	si colorMap=RED_COLOR_MAP (1) => retarne la matrice du plan rouge
         *  	si colorMap=BLUE_COLOR_MAP (2) => retourne la matrice du plan bleu
         *  	si colorMap=GREEN_COLOR_MAP (3) => retourne la matrice du plan vert
         */
        if(colorMap==1)
			return redPixels; // retourne la matrice du plan rouge
		else if(colorMap==2)
			return greenPixels; // retourne la matrice du plan vert
		else if(colorMap==3)
			return bluePixels; // retourne la matrice du plan vert
		else
			return null;
	}

	/*
	 * Méthode qui retourne une matrice composée par les échantillons des trois couleur (Rouge,Vert et Bleu),
	 * les pixels sont organisés dans l'ordre suivant: Rouge Vert Bleu dans trois cases consécutifs
	 */

	public static int[][] getFullColorMatrix(File imageFile){
		BufferedImage image=null; // l'objet qui nous permet de manipuler l'image
		int[][] fullPixelArray=null;
		try{
			image=ImageIO.read(imageFile); //La sous-classe BufferedImage décrit d'une image avec un buffer de données d'image accessible.
			int w=image.getWidth();
			int h=image.getHeight();
	        redPixels=  getSingleColorMapArray(imageFile,RED_COLOR_MAP);                    // Récupère le plan rouge de l'image
	        greenPixels = getSingleColorMapArray(imageFile,GREEN_COLOR_MAP);             // Récupère le plan vert de l'image
	        bluePixels = getSingleColorMapArray(imageFile,BLUE_COLOR_MAP);               // Récupère le plan bleu de l'image
	        fullPixelArray=new int[w][h];
	        for(int i=0;i<w;i++){
	        	for(int j=0;j<h;j++){
	        		 /* On rassemble les trois composantes (Rouge, Verte, Bleue) dans une meme matrice respectivement :
	                 * par exemple : fullPixelArray[0][0]=redPixels[0][0]
	                 * 				 fullPixelArray[0][1]=greenPixels[0][0]
	                 * 				 fullPixelArray[0][2]=bluePixels[0][0]
	                 * 				 ...
	                 */
	        		fullPixelArray[i][3*j]=redPixels[i][j]; // Récupère la composante Rouge et la mettre dans leur position dans la matrice globale
	                fullPixelArray[i][3*j+1]=bluePixels[i][j]; // Récupère la composante Bleu et la mettre dans leur position dans la matrice globale
	                fullPixelArray[i][3*j+2]=greenPixels[i][j]; // Récupère la composante Verte et la mettre dans leur position dans la matrice globale
	        	}
	        }

		}
		catch(Exception ee){
			ee.printStackTrace();
			new JOptionPane();
			JOptionPane.showMessageDialog(null, "Erreur de lecture du fichier", "Erreur",JOptionPane.ERROR_MESSAGE); // afficher un message d'erreur
			System.err.println("Erreur de lecture du fichier image");
		}
		return fullPixelArray;
	}

	/* Méthode qui lit un fichier image et retourne une matrice de pixel RGB en format hexadécimale */
	public static String[][] getHexArrayFromImage(File imageFile){
		BufferedImage myImage=null; // l'objet qui nous permet de manipuler l'image
		String[][] pixelHexArray=null;
		try{
	        myImage=ImageIO.read(imageFile); //La sous-classe BufferedImage décrit d'une image avec un buffer de données d'image accessible.
	        imageHeight=myImage.getHeight(); // l'hauteur de l'image a partir de l'objet BufferedImage
	        imageWidth=myImage.getWidth(); // la largeur de l'image a partir de l'objet BufferedImage
	        pixelHexArray=new String[imageWidth][imageHeight]; // matrice qui va contenir la matrice RGB de l'image
	        for(int i=0;i<imageWidth;i++){
	            for(int j=0;j<imageHeight;j++){
	            	pixelHexArray[i][j]="#"+Integer.toHexString(myImage.getRGB(i,j)).substring(2); // Récupère la composante RGB de l'image a partir de l'objet BufferedImage et la convertir en hexadécimale (une chaine de caractère)
	            }
	        }
		}
		catch(Exception ee){
			ee.printStackTrace();
			System.err.println("Erreur de lecture du fichier image");
		}
		return pixelHexArray;
	}

	/*
	 * Méthode qui construit une image a partir des trois matrices des plans de couleurs rouge, vert et bleu, en cours d'édition
	 */
	public Image writeImageFrom3ColorMap(int[][] redColorMap,int[][] greenColorMap,int[][] blueColorMap, File fileName){
		/* En cours d'édition */
		return null;

	}

	/*
	 * Méthode qui lit un fichier image et retourne un tableau 1D contient les pixels de l'image
	 */
	public static double[] read(String fileName){
		double[] pixelArrayDouble=null; // tableau de type réel double précision qui va contenir les valeurs des pixels en format double 32 bits
		int w,h; // w:largeur, h:hauteur de l'image
		int index=0; // compteur
		try {
			File imageFile=new File(fileName); // lecture du fichier image
			BufferedImage image=ImageIO.read(imageFile); // affectation du fichier à l'objet BufferedImage

			w=image.getWidth(); // on récupère largeur de l'image
			h=image.getHeight(); // on récupère l'hauteur de l'image
			pixelArrayDouble=new double[w*h];
			for(int i=0;i<w;i++){
	            for(int j=0;j<h;j++){
	                pixelArrayDouble[index]=image.getRGB(i, j); // Récupère la composante RGB de l'image a partir de l'objet BufferedImage
	                index++;
	            }
	        }
			// en cas d'erreur de chargement du fichier image
		} catch (IOException e) {
			new JOptionPane();
			JOptionPane.showMessageDialog(null, "Erreur de lecture du fichier", "Erreur",JOptionPane.ERROR_MESSAGE); // afficher un message d'erreur
			e.printStackTrace();
		}
		return pixelArrayDouble;
	}

	/*
	 * Méthode qui lit un fichier image et retourne un tableau d'entier contient les pixels en format entier 32 bits
	 */
	public static int[] read(String fileName,Object objetNull){ // la surcharge du type de retour d'une méthode exige le changement de parametre d'entrée
		int[] pixelArrayInt = null; // tableau de type entier qui va contenir les valeurs des pixels en format entier 32 bits
		int w,h; // w:largeur, h:hauteur de l'image
		int index=0;
		try {
			File imageFile=new File(fileName);// lecture du fichier image
			BufferedImage image=ImageIO.read(imageFile); //

			w=image.getWidth(); // on récupère largeur de l'image
			h=image.getHeight(); // on récupère l'hauteur de l'image
			pixelArrayInt=new int[w*h];
			for(int i=0;i<w;i++){
	            for(int j=0;j<h;j++){
	                pixelArrayInt[index]=image.getRGB(i, j); // Récupère la composante RGB de l'image a partir de l'objet BufferedImage
	                index++;
	            }
	        }
			// en cas d'erreur de chargement du fichier image
		} catch (IOException e) {
			new JOptionPane();
			JOptionPane.showMessageDialog(null, "Erreur de lecture du fichier", "Erreur",JOptionPane.ERROR_MESSAGE); // afficher un message d'erreur
			System.out.println("Erreur de lecture du fichier");
			e.printStackTrace();
		}
		return pixelArrayInt;
	}

	/*
	 * Méthode qui écrire un fichier image a partir d'un tableau de pixel codés en format entier 32 bits
	 * le parametre imageType est un entier correspond au type de l'image qu'on vise la construire se sont
	 *  des constantes proposées par l'objet BufferedImage:TYPE_4BYTE_ABGR, TYPE_BYTE_GRAY, TYPE_INT_ARGB , ...
	 */
	public static void write(String imageName, int[] pixel1DArray, int width, int height, int imageType,boolean rgbFormat){
		BufferedImage myReconstImage=new BufferedImage(width, height,imageType); // l'image reconstruite , image en niveau de gris grace au flag "BufferedImage.TYPE_BYTE_GRAY"
		int index=0;
		int[][] array=new int[width][height];
		try{
			if(rgbFormat){
		        for(int x=0;x<width;x++){
		            for(int y=0;y<height;y++){
		            	array[x][y]=pixel1DArray[index];
		            	myReconstImage.setRGB(x, y, array[x][y]);   //  Remplissage de l'image en utilisant la méthode setRGB a partir de la matrice RGB
		            	index++;
		            }
		        }
			}
		         File outputfile = new File(imageName); // Création d'un nouveau fichier qui va contenir l'image reconstruite
		            ImageIO.write(myReconstImage, "jpg", outputfile); // écriture de l'image dans le fichier
			}
		catch(Exception ee){
        ee.printStackTrace();
    }
   //new PixelArray("Array",array,320,240);
	}

	/*
	 * Méthode qui convertit un tableau 1D a une matrice 2D
	 */
	private static int[][] convert1DTo2DArray(int[] pixel1DArray, int w, int h) {
		int index=0;
		int[][] array=new int[w][h];
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				array[i][j]=pixel1DArray[index];
				index++;
			}
		}
		return array;
	}

	public static int getRed(int pixel){return((pixel >> 16) & 0xff);}

	public static int getGreen(int pixel) {return((pixel >> 8) & 0xff);}

	public static int getBlue(int pixel) { return(pixel & 0xff);}

	public static int getAlpha(int pixel){ return (pixel >>> 24) & 0xff;}

	/**
     * This method converts a single array 0..255 range pixel matrix to a
     * java-compatible array of 32 bit integers. This method can take a long
     * time to complete so a reporting feature has been added. The arrays
     * should be the same dimensions as imageWidth and imageHeight. This array
     * can then be converted to an image with: <br>
     * newimage = comp.createImage(new MemoryImageSource(imageWidth,imageHeight,imageArray,0,imageWidth));
     * @param Array The pixel values
     * @param log Enable/Disable progress reporting.
     * @returns array A one dimensional image array.
     */
    public static int[] convertGrayToArray(int[][] R)
    {
        int array[] = new int[imageWidth * imageHeight];

        int x = 0;
        int y = 0;
        int index = 0;

        int G[][] = new int[imageWidth][imageHeight];
        int B[][] = new int[imageWidth][imageHeight];
        int alpha[][]=new int[imageWidth][imageHeight];

        G = R;
        B = R;
        alpha=R;


    	for (x=0; x<imageWidth; x++)
    	{
    	    for (y=0; y<imageHeight; y++)
    	    {
                array[index] = -16777216 | (R[x][y] << 16) | (G[x][y] << 8) | B[x][y];
        	    index++;
    	    }
    	}

        return array;
    }

	/*
	 * Méthode qui affiche les éléments d'une matrice (matrix) dans le console java
	 */
	static void display2DArray(int[][] matrix,
			int imageWidth, int imageHeight) {
		for(int i=0;i<imageWidth;i++){
			for(int j=0;j<imageHeight;j++){
				System.out.print(" | "+matrix[i][j]);
			}
			System.out.println();
		}
	}

	/**
     * This method returns an array[][] of 8-bit red values. The dimensions are
     * the array bounds ex. (320 x 200).
     * @returns An array[][] of red pixel values
     */
    public int[][] getRedArray()
    {
        int values[] = new int[imageWidth * imageHeight];
    	PixelGrabber grabber = new PixelGrabber(myImageObject.getSource(), 0, 0, imageWidth, imageHeight, values, 0, imageWidth);

    	try
    	{
    	    if(grabber.grabPixels() != true)
    	    {
    		    try
    		    {
    		        throw new AWTException("Grabber returned false: " + grabber.status());
    		    }
    		    catch (Exception e) {};
    		}
    	}
    	catch (InterruptedException e) {};

    	int r[][] = new int[imageWidth][ imageHeight];

  		int index = 0;
    	for (int y = 0; y < imageHeight; ++y)
    	{
    	    for (int x = 0; x < imageWidth; ++x)
    	    {
    	    	r[x][y] = ((values[index] & 0x00ff0000) >> 16);
        		index++;
    	    }
    	}

    	return r;
    }

    /**
     * This method returns an array[][] of 8-bit green values. The dimensions are
     * the array bounds ex. (320 x 200).
     * @returns An array[][] of green pixel values.
     */
    public int[][] getGreenArray()
    {
        int values[] = new int[imageWidth * imageHeight];
    	PixelGrabber grabber = new PixelGrabber(myImageObject.getSource(), 0, 0, imageWidth, imageHeight, values, 0, imageWidth);

    	try
    	{
    	    if(grabber.grabPixels() != true)
    	    {
    	        try
    	        {
    		        throw new AWTException("Grabber returned false: " + grabber.status());
    		    }
    		    catch (Exception e) {};
    		}
    	}
    	catch (InterruptedException e) {};

    	int g[][] = new int[imageWidth][imageHeight];

  		int index = 0;
    	for (int y = 0; y < imageHeight; ++y)
    	{
    	    for (int x = 0; x < imageWidth; ++x)
    	    {
        		g[x][y] = ((values[index] & 0x0000ff00) >> 8);
        		++index;
    	    }
    	}

    	return g;
    }

    /**
     * This method returns an array[][] of 8-bit blue values. The dimensions are
     * the array bounds, ex. (320 x 200).
     * @returns An array[][] of blue pixel values.
     */
    public int[][] getBlueArray()
    {
        int values[] = new int[imageWidth * imageHeight];
    	PixelGrabber grabber = new PixelGrabber(myImageObject.getSource(), 0, 0, imageWidth, imageHeight, values, 0, imageWidth);

    	try
    	{
    	    if(grabber.grabPixels() != true)
    	    {
    	        try
    	        {
    		        throw new AWTException("Grabber returned false: " + grabber.status());
    		    }
    		    catch (Exception e) {};
    		}
    	}
    	catch (InterruptedException e) {};

    	int b[][] = new int[imageWidth][imageHeight];

  		int index = 0;
    	for (int y = 0; y < imageHeight; ++y)
    	{
    	    for (int x = 0; x < imageWidth; ++x)
    	    {
        		b[x][y] = (values[index] & 0x000000ff);
        		++index;
    	    }
    	}

    	return b;
    }
}