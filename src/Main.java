
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// call JEncoder to decompress and compress an image file
			// params: image file name, quality of compression, JPEG block size (8 recommended by JPEG)
            new JEncoder("test.jpg", 1, 8);
        } catch (Exception e) {
        	e.printStackTrace();
        }

	}

}
