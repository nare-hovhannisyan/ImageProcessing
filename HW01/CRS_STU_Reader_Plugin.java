import ij.IJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;
import ij.process.BinaryProcessor;
import ij.process.ImageProcessor;
import ij.process.ByteProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CRS_STU_Reader_Plugin implements PlugIn {

    public void run(String arg) {

        String crsFilePath = "/Users/narehovhannisyan/Desktop/classes/Image Processing/Github/ImageProcessing/HW01/tre-s-92.crs";
        String stuFilePath = "/Users/narehovhannisyan/Desktop/classes/Image Processing/Github/ImageProcessing/HW01/tre-s-92.stu";

        int N = 0;

        try {
            BufferedReader crsReader = new BufferedReader(new FileReader(crsFilePath));

            String crsLine;
            Pattern crsPattern = Pattern.compile("(\\d+) (\\d+)");
            while ((crsLine = crsReader.readLine()) != null) {
                Matcher crsMatcher = crsPattern.matcher(crsLine);
                while (crsMatcher.find()) {
                    N = Integer.parseInt(crsMatcher.group(2));
                }
            }

            crsReader.close();
        } catch (IOException e) {
            IJ.error("Error reading the .crs file: " + e.getMessage());
            return;
        }

        if (N == 0) {
            IJ.error("Could not find the value of N in the .crs file.");
            return;
        }

        ByteProcessor byteProcessor = new ByteProcessor(N, N);
        byteProcessor.setValue(255);
        byteProcessor.fill();

        try {
            BufferedReader stuReader = new BufferedReader(new FileReader(stuFilePath));

            String stuLine;
            Pattern stuPattern = Pattern.compile("(\\d+) (\\d+)");
            while ((stuLine = stuReader.readLine()) != null) {
                Matcher stuMatcher = stuPattern.matcher(stuLine);
                while (stuMatcher.find()) {
                    int x = Integer.parseInt(stuMatcher.group(1));
                    int y = Integer.parseInt(stuMatcher.group(2));
                    if (x >= 0 && x < N && y >= 0 && y < N) {
                        byteProcessor.putPixel(x, y, 0);
                    }
                }
            }

            stuReader.close();
        } catch (IOException e) {
            IJ.error("Error reading the .stu file: " + e.getMessage());
            return;
        }

        ImagePlus binaryImage = new ImagePlus("Binary Image", byteProcessor);
        binaryImage.show();
    }
}
