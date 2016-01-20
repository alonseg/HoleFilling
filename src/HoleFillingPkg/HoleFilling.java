package HoleFillingPkg;

import HoleFillingPkg.WeightStrategyPkg.WeightStrategy;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Alon on 1/11/2016.
 */
public class HoleFilling {

    public static final int[] NEIGHBOUR_OPS = {-1, 0, 1};
    public static final int INV_PXL = -1;

    private WeightStrategy weight;
    private float[][] image;
    private Collection<Pixel> holePixels;

    /**
     * Constructor - creates the HoleFilling object that can perform the several operations ove an image
     *
     * @param weight the wanted weighting function
     * @param image the image that we want to work on
     */
    public HoleFilling(float[][] image, WeightStrategy weight){
        this.weight = weight;
        this.image = image;
        this.holePixels = new HashSet<>();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * Given a 2D image, this method will find the 8-boundary pixels.
     *
     * @return Collection of the 8-boundary pixels.
     */
    public Collection<Pixel> findHole(){
        if (image == null || image.length < 1 || holePixels == null){
            HoleFillingRunner.print("illegal input exiting...");
            return null;
        }

        int numOfRows = image.length;
        int numOfCols = image[0].length;

        //a HashSet that will hold the boundary pixels
        Collection<Pixel> boundary = new HashSet<>();

        boolean firstFound = false;
        int firstRow = -1;
        int firstCol = -1;
        /*
         * iterates over the entire image to find first missing pixel,
         * when it finds it, it start a DFS to find all other missing pixels and boundary pixels
         */
        for (int row = 0; row < image.length && !firstFound; row++) {
            for (int col = 0; col < image[row].length && !firstFound; col++) {
                if (image[row][col] != INV_PXL) {
                    continue;
                }
                firstFound = true;
                firstCol = col;
                firstRow = row;
            }
        }
        if (firstRow != -1) {
            setBoundaries(boundary, firstRow, firstCol, image.length, image[0].length);
        }else {
            System.out.println("Now holes found!");
        }
        return boundary;
    }

    /**
     * Returns a copy of the given image with it's hole's bounds marked
     *
     * @param bound the Collection of Pixels that are the boundary of the hole
     * @param color the color ( 0 or 1 only) that the boundary will be marked in
     * @return an image where the boundary is marked with the given color
     */
    public float[][] markBounds(Collection<Pixel> bound, int color){
        if (image == null || image.length < 1 || image[0] == null || image[0].length < 1){
            System.out.println("illegal image size");
            return null;
        }
        if (color != 0 && color != 1){
            System.out.println("illegal color");
            return null;
        }
        float[][] ret = copyImage(image);
        for (Pixel pix: bound) {
            ret[pix.row][pix.col] = color;
        }
        return ret;
    }

    /**
     *
     * @param boundary
     * @return
     */
    public float[][] fillHole(Collection<Pixel> boundary){
        if (image == null || image.length < 1 || image[0] == null || image[0].length < 1 || holePixels == null || holePixels.size() < 1){
            System.out.println("Some params were illegal :(. exiting...");
            System.exit(0);
        }

        float[][] ret = copyImage(image);

        for (Pixel pix: holePixels) {
            ret[pix.row][pix.col] = calcPixFilling(boundary, pix, this.weight);
        }

        return ret;
    }

    /**
     * Calculates the value of the missing pixel of the given image according to the formula given in the
     * requirements paper.
     *
     * @param boundary the set of boundary pixels
     * @param missing the missing pixel to fill
     * @param weight the WeightStrategy that calculate the weight
     */
    public float calcPixFilling(Collection<Pixel> boundary, Pixel missing, WeightStrategy weight){
        if (boundary == null || missing == null){
            HoleFillingRunner.print("one of the parameters were null :(. exiting");
            System.exit(0);
        }

        float weightSum = 0;
        float weightTimesValSum = 0;
        for (Pixel boundPix: boundary) {
            double currWeight = weight.weight(boundPix, missing);
            weightSum += currWeight;
            weightTimesValSum += (currWeight * boundPix.val);
        }
        return (weightTimesValSum / weightSum);
    }

    //###################################################################################################
    //##################################### PRIVATE HELPERS #############################################
    //###################################################################################################

    private float[][] copyImage(float[][] image) {
        float[][] ret = new float[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            System.arraycopy(image[i], 0, ret[i], 0, image[0].length);
        }
        return ret;
    }

    /**
     * The recursive helper to find the hole and the boundary.
     *
     *
     * @param boundary the collection to hold the boundary pixels
     * @param row the current row index
     * @param col the current col index
     * @param numOfRows the number of rows in the image
     * @param numOfCols the number of cols in the image
     */
    private void setBoundaries(Collection<Pixel> boundary, int row, int col,
                                int numOfRows, int numOfCols) {
        if (row < 0 || col < 0 || row >= numOfRows || col >= numOfCols ||
                holePixels.contains(new Pixel(row, col, INV_PXL)))
            return;

        if (image[row][col] == INV_PXL){
            holePixels.add(new Pixel(row, col, INV_PXL));
            for (int i: NEIGHBOUR_OPS) {
                for (int j: NEIGHBOUR_OPS) {
                    setBoundaries(boundary, row + i, col + j, numOfRows, numOfCols);
                }
            }
        }else {
            boundary.add(new Pixel(row, col, image[row][col]));
        }


    }

    /**
     * Shows the image
     * @param image
     */
    private void showImage(BufferedImage image) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates a Buffered image of the given float[][] and shoes it
     * @param image
     */
    public void showImage(float[][] image) {
        BufferedImage bi = twoDArrToBufferedImage(image);
        showImage(bi);
    }

    /**
     *
     */
    public void showImage() {
        showImage(image);
    }

    /**
     * Converts a 2D array of floats into a BufferedImage.
     *
     *  ***  it expects the array values to be [0-1] ***
     *
     * @param image the 2D float array
     * @return BufferedImage representation of the input
     */
    private BufferedImage twoDArrToBufferedImage(float[][] image) {
        int yLength = image.length;
        int xLength = image[0].length;
        BufferedImage b = new BufferedImage(xLength, yLength, BufferedImage.TYPE_USHORT_GRAY);

        for(int x = 0; x < xLength; x++) {
            for(int y = 0; y < yLength; y++) {
                int tmp = (int)image[y][x] * 255;
                int rgb = tmp<<16 | tmp << 8 | tmp;
                b.setRGB(x, y, rgb);
            }
        }
        return b;
    }
}
