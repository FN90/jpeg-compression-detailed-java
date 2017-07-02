
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/*
* JEncoder: Compress and decompress an image file: it takes as params an image file, decompress it, get featrures and compress it again using JPEG standard steps
* Edited by Fahmi
* Date: August 28, 2014
* Published: July 1, 2017
*/

public class JEncoder extends Frame {
	// Qualité de compression:
		private static int Quality;
		// Taille du bloc:
		private static int BlocSize;
		// Image:
		private Image monImage;
		// Matrice de pixel:
		private int[][] inputArray;
		// Bloc de NxN:
		private int[][] bloc;
		// Bloc après DCT:
		private int[][] blocDCT;
		// bloc après quantification:
		private int[][] bloc_DCT_Q;
		// Matrice reconstruit:
		private int[][] outputArray;
    // constants

    int width =320, height = 240;
    // objet image
    private Image image;
    GrafixTools GT;
    DCT dctTrans;
    int locationwidth=100;
    int locationheight=10;
	private String fileName;

    public JEncoder(String imageName, int quality, int blocSize) {
        this.Quality=quality;
        this.BlocSize=blocSize;
        this.fileName=imageName;
    	/*
    	setLocation(locationwidth, locationheight);   //move(100,00);
        setSize(width, height);		//resize(width,height);
        repaint();
		*/
        // Load in an image
        image = Toolkit.getDefaultToolkit().getImage(imageName);

        // Make sure it gets loaded
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);
        System.out.println("Initialized tracker.");
        try {
            System.out.println("Waiting for tracker...");
            tracker.waitForID(0);
        } catch (Exception e) {
            return;
        }
        System.out.println("Loaded image.");

        GT = new GrafixTools(image);
        System.out.println("Dimensions: " + GT.imageHeight + "," + GT.imageWidth);

        System.out.println("Getting array information");
        //outputArray = GT.getRedArray();
        outputArray=new ImageTools("test.jpg").getRedArray();
        int[] outputArray1D=ImageTools.convertGrayToArray(outputArray);
        ImageTools.write("imgOrgnl.jpg", outputArray1D, 320, 240, BufferedImage.TYPE_BYTE_GRAY, true);
        new PixelArray("Matrice de pixel",outputArray,320,240);
        System.out.println("Initializing DCT");

        dctTrans = new DCT();
        System.out.println("Done initializing DCT");

        compressImage();
    }

    public void compressImage() {
        int i = 0;
        int j = 0;
        int a = 0;
        int b = 0;
        int x = 0;
        int y = 0;
        int counter = 0;

        int temp1 = 0, temp2 = 0, count = 0;

        int xpos;
        int ypos;
        //int width = width, height = height;

        float dctArray1[][] = new float[BlocSize][BlocSize];
        float[][] dctArray2 = new float[BlocSize][BlocSize];
        double[][] dctArray3 = new double[BlocSize][BlocSize];
        double[][] dctArray4 = new double[BlocSize][BlocSize];

        int reconstImage[][] = new int[width][height];
		// Initialisation de l'opération de compression,DCT & la quatification:
        System.out.println("Initializing compression - DCT & Qualitization");

        for (i = 0; i < width / BlocSize; i++) {
            for (j = 0; j < height / BlocSize; j++) {
                // Read in a BLOCK_SIZE x BLOCK_SIZE block, starting at (i * BLOCK_SIZE) -> BLOCK_SIZE;
                xpos = i * BlocSize;
                ypos = j * BlocSize;
				// Décomposition de la matrice image "outputArray" en des blocs 8x8
                for (a = 0; a < BlocSize; a++) {
                    for (b = 0; b < BlocSize; b++) {
					// chargement du bloc 8x8 (matrice dctArray) a partir de la matrice image (outputArray)
                        dctArray1[a][b] = (float) outputArray[xpos + a][ypos + b];
                    }
                }

                // Apply the forward DCT to the block (appliquer la DCT sur le bloc 8x8 (dctArray))
                dctArray2 = dctTrans.fast_fdct(dctArray1);

                // Quantitize (quantification)
                dctArray3 = CompressionTools.quantitizeBloc(dctArray2, Quality, BlocSize);

                // Reconstruct the compressed data in an image... (Reconstruction de l'image du données compréssées)
                for (a = 0; a < BlocSize; a++) {
                    for (b = 0; b < BlocSize; b++) {
                        reconstImage[xpos + a][ypos + b] = (int) dctArray2[a][b];
                    }
                }

            }
        }
        new PixelArray("Image apres DCT et quantification",reconstImage,width,height);
       /*
        // Test RLE Encoding (test de l'encodgae RLE)

        int one[] = new int[width * height];
        System.out.println("Array Conversion & RLE Start");

        int r = 0;
        int s = 0;
        int index = 0;

        for (s = 0; s < width; s++) {
            for (r = 0; r < height; r++) {
                one[index] = reconstImage[s][r];
                index++;
            }

        }

        // Run the compressor (Faire fonctionner le compresseur)
        int compressed[] = new int[width * height];
		// la méthode compressImage de l'objet dctTrans prend comme paramètre un tableau 1D, et une variable booléenne et nous donne comme variable de sortie un tableau 1D d'entier:
        compressed = CompressionTools.compressImage(one, width, height);

        // Run the decompressor (Faire fonctionner le décompresseur)
        int decompressed[] = new int[width * height];
		// Appel à la méthode decompressImage de l'objet dctTrans : cette méthode prend comme paramètres le tableau 1D (tableau de données compréssés "compressed" créer par la méthode compressImage) et une variable booléenne:
        decompressed = CompressionTools.decompressImage(compressed, width, height);
		// Test sur l'opération de décompréssion s'il y a des erreurs on affiche message
        if (one != decompressed) {
            System.out.println("Compression error");
        }
		// Conversion de la tableau 1D de données comprésseés à un tableau 2D (image reconstruite):
        System.out.println("Converting decompressed image array to 2D");

        index = 0;

        for (s = 0; s < width; s++) {
            for (r = 0; r < height; r++) {
                reconstImage[s][r] = decompressed[index];
                index++;
            }

        }
        new PixelArray("Image apres decompression",reconstImage,width,height);
        /*
         // Figure how just how good we are
         int nonzero = 0;
         for (i=0; i<width; i++)
         {
         for (j=0; j<height; j++)
         {
         if (reconstImage[i][j] != 0)
         {
         nonzero++;
         System.out.print("Signifigant Pixels: " + nonzero);
         System.out.print("\r");
         }
         }
         }

         System.out.println();
         double cr;
         cr = ((double)nonzero / (double)(width*height)) * 100;
         System.out.println("Tossed percentage: " + (100.0 - cr) + "%");
         */


		// Initialisation de l'opération de décompression, déquatification & la DCT inverse:
        System.out.println("Initializing decompression - DeQualitization & Inverse DCT");
        counter = 0;

        for (i = 0; i < width/BlocSize; i++) {
            for (j = 0; j < height/BlocSize; j++) {
                // Read in a BLOCK_SIZExBLOCK_SIZE block, starting at (i * BLOCK_SIZE) -> BLOCK_SIZE;
				// Lecture d'une bloc 8x8 a partir du matrice image reconstruite reconstImage
                xpos = i * BlocSize;
                ypos = j * BlocSize;

                for (a = 0; a < BlocSize; a++) {
                    for (b = 0; b < BlocSize; b++) {
					// remplissage de la matrice 8x8 dctArray2 a partir de la matrice reconstImage:
                        dctArray2[a][b] = reconstImage[xpos + a][ypos + b];
                    }
                }
                int[][] quantum=CompressionTools.initQuantum(Quality,BlocSize);
                // Run the dequantitizer
				// Faire fonctionner l'opération de déquantification de l'objet dctTrans, qui prend comme paramètre un tableru 2D (dctArray2) et une variable booleènne
                dctArray3 = CompressionTools.dequantitizeImage(dctArray2, quantum, BlocSize);

                // Run the inverse DCT
				// Faire fonctionner l'opération de la DCT inverse:méthode de l'objet dctTrans, prend comme parmètre un tableau 2D (dctArray3), retourne un tab 2D:
				// Création du tab dctArray4:
                dctArray4 = dctTrans.fast_idct(dctArray2);

                // Overwrite with a new reconstructed image
				// Remplacer l'ancienne image recontruite reconstImage par une nouvelle image a partir dctArray4:
                for (a = 0; a < BlocSize; a++) {
                    for (b = 0; b < BlocSize; b++) {
						// remplir la nouvelle image reconstruite a partir du bloc 8x8 (dctArray4)
                        reconstImage[xpos + a][ypos + b] =(int) dctArray4[a][b];
                    }
                }
                /*
                counter++;
                System.out.print("Blocks: ");
                System.out.print(counter + "\r");
                */
            }
        }
        new PixelArray("Image apres dequantification, IDCT",reconstImage,width,height);
        System.out.println();
		// Construction  de l'image:
        System.out.println("Constructing the images..");
		// Appel a la fonction "makeImage" qui prend comme paramètre un tableau 2D (reconstImage):
        makeImage(reconstImage);


        System.out.println("Hit enter");
        try {
            System.in.read();
        } catch (Exception e) {
        }
        System.exit(0);

    }
	// Méthode "makeImage" qui se charge pour traiter un tableau image et l'affiche dans une fenetre(l'objet ImageWindow):
    public void makeImage(int[][] image) {
        int i;
        int j;
        int k = 0;

        int one[] = new int[width * height];

        System.out.println("Calling conversion");
        one = GT.convertGrayToArray(image, true);
        //one = GT.convertGrayToArray(GlobalRed, true);
        System.out.println();
        System.out.println("Completed one dimensional array conversion");
        ImageWindow iw = new ImageWindow(one);
    }
}

