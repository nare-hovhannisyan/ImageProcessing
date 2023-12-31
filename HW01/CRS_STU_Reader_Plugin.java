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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CRS_STU_Reader_Plugin implements PlugIn {

    public void run(String arg) {

        String crsFilePath = "/Users/narehovhannisyan/Desktop/classes/Image Processing/Github/ImageProcessing/HW01/lse-f-91.crs";
        String stuFilePath = "/Users/narehovhannisyan/Desktop/classes/Image Processing/Github/ImageProcessing/HW01/lse-f-91.stu";

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

        List<int[]> coordinatePairs = new ArrayList<>();
        try {

            BufferedReader stuReader = new BufferedReader(new FileReader(stuFilePath));

            String line;
            while ((line = stuReader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length >= 2) {
                    for (int i = 0; i < tokens.length - 1; i += 2) {
                        int x = Integer.parseInt(tokens[i]);
                        int y = Integer.parseInt(tokens[i + 1]);
                        coordinatePairs.add(new int[] { x, y });
                    }
                }
            }

            stuReader.close();
        } catch (IOException e) {
            IJ.error("Error reading the .stu file: " + e.getMessage());
            return;
        }

        for (int[] pair : coordinatePairs) {
            int x = pair[0];
            int y = pair[1];
            if (x >= 0 && x < N && y >= 0 && y < N) {
                byteProcessor.set(x, y, 0);
            }

        }

        ImagePlus binaryImage = new ImagePlus("Binary Image", byteProcessor);
        binaryImage.show();
    }
}
