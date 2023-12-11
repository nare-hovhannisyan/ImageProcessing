import ij.*;
import ij.process.*;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.filter.RankFilters;
import java.awt.*;

public class AdjustBackground implements PlugInFilter {
    public int setup(String arg, ImagePlus imp) {
        return DOES_RGB;
    }

    public void run(ImageProcessor ip) {
         int width = ip.getWidth();
    int height = ip.getHeight();

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int[] rgb = new int[3];
            ip.getPixel(x, y, rgb);

            // Determine if the pixel is part of the background
            if (isBackground(rgb)) {
                // Set to brighter white
                ip.putPixel(x, y, new int[]{132, 157, 188});
            }
        }
    }
    }

private boolean isBackground(int[] rgb) {
 	int threshold = 100;
	int average = (rgb[0] + rgb[1] + rgb[2]) / 3;
	return rgb[0] > threshold ;
}

}
