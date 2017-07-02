import java.lang.*;
import java.awt.*;
import java.net.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

/**
 * <b>GraphixTools - A collection of handy graphics functions</b>
 * <br><br>
 * This is a collection of graphics mundanes that I had to cut and
 * paste one too many times. While laziness on my part I think that
 * I was doing this stuff enough to warrant packaging up these goodies
 * into their own proper class. Hopefully this will evolve into something
 * that's worth releasing.
 * <br><br>
 * <b>Feature list:</b><br>
 * Array returns (R,G,B).<br>
 * E-Z Image Producing.<br>
 * RGB to YUV colorspace conversion.<br>
 * YUV to RGB colorspave conversion.<br>
 * Filesize and other information<br>
 * Free & Used VM Machine Memory Info<br>
 * <br><br>
 * You can get the <a href="GrafixTools.java">source code</a> here.
 * @author <a href="http://eagle.uccb.ns.ca/steve/home.html">Stephen Manley</a> (smanley@eagle.uccb.ns.ca)
 * @version 0.9.7 August 20th 1996
 */
public class GrafixTools
{
    /**
     * Here lieth an image.
     */
    public Image imageobj;
    /**
     * Image height.
     */
    public int imageHeight;

    /**
     * Image width.
     */
    public int imageWidth;

    /**
     * Gives GraphixTools a preloaded image object. Image must be
     * pre-loaded with the MediaTracker or bad things will happen.
     * The constructor calculates all the image parameters from this
     * passed image. (Dimensions, size, etc.)
     * @param image The 100% loaded image
     */
    public GrafixTools(Image image)
    {
        imageobj = image;
        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);
    }

    /**
     * This method returns an array[][] of 8-bit red values. The dimensions are
     * the array bounds ex. (320 x 200).
     * @returns An array[][] of red pixel values
     */
    public int[][] getRedArray()
    {
        int values[] = new int[imageWidth * imageHeight];
    	PixelGrabber grabber = new PixelGrabber(imageobj.getSource(), 0, 0, imageWidth, imageHeight, values, 0, imageWidth);

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

    	int r[][] = new int[imageWidth][imageHeight];

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
    	PixelGrabber grabber = new PixelGrabber(imageobj.getSource(), 0, 0, imageWidth, imageHeight, values, 0, imageWidth);

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
    	PixelGrabber grabber = new PixelGrabber(imageobj.getSource(), 0, 0, imageWidth, imageHeight, values, 0, imageWidth);

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

    /**
     * This method converts an RGB colorspace to a YUV colorspace. (YUV is more
     * technically correct when referred to a YCbCr colorspace.) YCbCr was developed
     * according to recommondation ITU-R BT.601 (Formerly: CCIR 601) to develop
     * a world-wide digital video standard. It is commonly used in many streaming
     * video and compression routines, such as <a href="http://www.mpeg.org">MPEG</a>
     * and <a href="http://www.telenor.nl">H.263.</a>
     * <br><br>
     * This routine handles dealing with 0..255 values per pixel, the more common
     * PC format. The passed arrays should all have the same bounds, corresponding
     * to the image dimensions.
     * @param redArray An array of 0..255 red pixel values.
     * @param greenArray An array of 0..255 green pixel values.
     * @param blueArray An array of 0..255 blue pixel values.
     * @returns Y The Y component
     */
    public int[][] convertRGBtoY(int[][] redArray, int[][] greenArray, int[][] blueArray)
    {
        int i;
        int j;
        int Y[][] = new int[imageWidth][imageHeight];

        for (i=0; i<imageWidth; i++)
        {
            for (j=0; j<imageHeight; j++)
            {
                Y[i][j] = (int)((0.257 * (float)redArray[i][j]) + (0.504 * (float)greenArray[i][j]) + (0.098 * (float)blueArray[i][j]) + 16.0);
            }
        }

        return Y;
    }

    /**
     * This method converts an RGB colorspace to a YUV colorspace. (YUV is more
     * technically correct when referred to a YCbCr colorspace.) YCbCr was developed
     * according to recommondation ITU-R BT.601 (Formerly: CCIR 601) to develop
     * a world-wide digital video standard. It is commonly used in many streaming
     * video and compression routines, such as <a href="http://www.mpeg.org">MPEG</a>
     * and <a href="http://www.telenor.nl">H.263.</a>
     * <br><br>
     * This routine handles dealing with 0..255 values per pixel, the more common
     * PC format. The passed arrays should all have the same bounds, corresponding
     * to the image dimensions.
     * @param redArray An array of 0..255 red pixel values.
     * @param greenArray An array of 0..255 green pixel values.
     * @param blueArray An array of 0..255 blue pixel values.
     * @returns Cb The Cb component.
     */
    public int[][] convertRGBtoCb(int[][] redArray, int[][] greenArray, int[][] blueArray)
    {
        int i;
        int j;
        int Cb[][] = new int[imageWidth][imageHeight];

        for (i=0; i<imageWidth; i++)
        {
            for (j=0; j<imageHeight; j++)
            {
                Cb[i][j] = (int)((-0.148 * (float)redArray[i][j]) - (0.291 * (float)greenArray[i][j]) + (0.439 * (float)blueArray[i][j]) + 128.0);
            }
        }

        return Cb;
    }

    /**
     * This method converts an RGB colorspace to a YUV colorspace. (YUV is more
     * technically correct when referred to a YCbCr colorspace.) YCbCr was developed
     * according to recommondation ITU-R BT.601 (Formerly: CCIR 601) to develop
     * a world-wide digital video standard. It is commonly used in many streaming
     * video and compression routines, such as <a href="http://www.mpeg.org">MPEG</a>
     * and <a href="http://www.telenor.nl">H.263.</a>
     * <br><br>
     * This routine handles dealing with 0..255 values per pixel, the more common
     * PC format. The passed arrays should all have the same bounds, corresponding
     * to the image dimensions.
     * @param redArray An array of 0..255 red pixel values.
     * @param greenArray An array of 0..255 green pixel values.
     * @param blueArray An array of 0..255 blue pixel values.
     * @returns Cr The Cr component.
     */
    public int[][] convertRGBtoCr(int[][] redArray, int[][] greenArray, int[][] blueArray)
    {
        int i;
        int j;
        int Cr[][] = new int[imageWidth][imageHeight];

        for (i=0; i<imageWidth; i++)
        {
            for (j=0; j<imageHeight; j++)
            {
                Cr[i][j] = (int)((0.439 * (float)redArray[i][j]) - (0.368 * (float)greenArray[i][j]) - (0.071 * (float)blueArray[i][j]) + 128.0);
            }
        }

        return Cr;
    }

    /**
     * This method converts a YUV (aka YCbCr) colorspace to a RGB colorspace.
     * This is handy when trying to reconstruct an image in Java from YCbCr transmitted
     * data. This routine expects the data to fall in the standard PC 0..255 range
     * per pixel, with the array dimensions corresponding to the imageWidth and imageHeight.
     * These variables are either set manually in the case of a null constructor,
     * or they are automatically calculated from the image parameter constructor.
     * @param Y The Y component set.
     * @param Cb The Cb component set.
     * @param Cr The Cr component set.
     * @returns R The R component.
     */
    public int[][] convertYCbCrtoR(int[][] Y, int[][] Cb, int[][] Cr)
    {
        int i;
        int j;
        int R[][] = new int[imageWidth][imageHeight];

        for (i=0; i<imageWidth; i++)
        {
            for (j=0; i<imageHeight; j++)
            {
                R[i][j] = (int)( ((1.164*((float)Y[i][j]-16.0))) + (1.596*((float)Cr[i][j] - 128.0)) );
            }
        }

        return R;
    }

    /**
     * This method converts a YUV (aka YCbCr) colorspace to a RGB colorspace.
     * This is handy when trying to reconstruct an image in Java from YCbCr transmitted
     * data. This routine expects the data to fall in the standard PC 0..255 range
     * per pixel, with the array dimensions corresponding to the imageWidth and imageHeight.
     * These variables are either set manually in the case of a null constructor,
     * or they are automatically calculated from the image parameter constructor.
     * @param Y The Y component set.
     * @param Cb The Cb component set.
     * @param Cr The Cr component set.
     * @returns G The G component.
     */
    public int[][] convertYCbCrtoG(int[][] Y, int[][] Cb, int[][] Cr)
    {
        int i;
        int j;
        int G[][] = new int[imageWidth][imageHeight];

        for (i=0; i<imageWidth; i++)
        {
            for (j=0; i<imageHeight; j++)
            {
                G[i][j] = (int)(((1.164*((float)Y[i][j]-16.0))) - (0.813*((float)Cr[i][j] - 128.0)) - (0.392*((float)Cb[i][j] - 128.0)));
            }
        }

        return G;
    }

    /**
     * This method converts a YUV (aka YCbCr) colorspace to a RGB colorspace.
     * This is handy when trying to reconstruct an image in Java from YCbCr transmitted
     * data. This routine expects the data to fall in the standard PC 0..255 range
     * per pixel, with the array dimensions corresponding to the imageWidth and imageHeight.
     * These variables are either set manually in the case of a null constructor,
     * or they are automatically calculated from the image parameter constructor.
     * @param Y The Y component set.
     * @param Cb The Cb component set.
     * @param Cr The Cr component set.
     * @returns B The B component.
     */
    public int[][] convertYCbCrtoB(int[][] Y, int[][] Cb, int[][] Cr)
    {
        int i;
        int j;
        int B[][] = new int[imageWidth][imageHeight];

        for (i=0; i<imageWidth; i++)
        {
            for (j=0; i<imageHeight; j++)
            {
                B[i][j] = (int)(((1.164*((float)Y[i][j]-16.0))) + (2.017*((float)Cb[i][j] - 128.0)));
            }
        }

        return B;
    }


    /**
     * This method converts a 0..255 range pixel matrix to a java-compatible
     * array of 32 bit integers. This method can take a long time to complete
     * so a reporting feature has been added. The arrays should be the same
     * dimensions as imageWidth and imageHeight. This array can then be converted
     * to an image with: <br>
     * newimage = comp.createImage(new MemoryImageSource(imageWidth,imageHeight,imageArray,0,imageWidth));
     * @param R The Red Values.
     * @param G The Green Values.
     * @param B The Blue Values.
     * @param log Enable/disable progress reporting.
     * @returns array A one dimensional image array.
     */
    public int[] convertRGBtoArray(int[][] R, int[][] G, int[][] B, boolean log)
    {
        int x = 0;
        int y = 0;
        int index = 0;

        int array[] = new int[imageWidth * imageHeight];

    	for (y=0; y<imageHeight; y++)
    	{
    	    for (x=0; x<imageWidth; x++)
    	    {
    	        array[index] = (int)((B[x][y]));
        		array[index] = (int)((G[x][y] << 8));
        		array[index] = (int)((R[x][y] << 16));
        	    array[index] = (int)((0 << 24));
        	    index++;
    	    }

    	    if (log)
    	    {
                System.out.print("Blocks: ");
                System.out.print(index + "\r");
    	    }
    	}

        return array;
    }

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
    public int[] convertGrayToArray(int[][] R, boolean log)
    {
        int array[] = new int[imageWidth * imageHeight];

        int x = 0;
        int y = 0;
        int index = 0;

        int G[][] = new int[imageWidth][imageHeight];
        int B[][] = new int[imageWidth][imageHeight];

        G = R;
        B = R;

    	for (y=0; y<imageHeight; y++)
    	{
    	    for (x=0; x<imageWidth; x++)
    	    {
                array[index] = -16777216 | (R[x][y] << 16) | (G[x][y] << 8) | B[x][y];
        	    index++;
    	    }

    	    if (log)
    	    {
                System.out.print("Blocks: ");
                System.out.print(index + "\r");
    	    }
    	}

        return array;
    }
}


