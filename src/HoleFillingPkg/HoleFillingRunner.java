package HoleFillingPkg;

import HoleFillingPkg.WeightStrategyPkg.WeightStrategy;
import HoleFillingPkg.WeightStrategyPkg.WeightStrategyFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Scanner;


/**
 * Created by Alon on 1/11/2016.
 */
public class HoleFillingRunner {

    private static final String Z_NAME = "\'z\'";
    private static final String EPSILON_NAME = "\'epsilon\'";
    private static final int BLACK = 1;
    private static final int WHITE = 0;

    public static float z = 2;
    public static float epsilon = 0.0001f;
    public static WeightStrategy weight = null;

    public static Scanner scanner;
    public static float[][] img;

    public static void main(String[] args){


        //Asks the user for the needed input
        scanner = new Scanner(System.in);
        getParam(Z_NAME, z);
        getParam(EPSILON_NAME, epsilon);

        getWeight();

        getImage();


        //Creates the HoleFilling object
        HoleFilling hf = new HoleFilling(img, weight);

        Collection<Pixel> bound = hf.findHole();

        float[][] markedBoundsImage = hf.markBounds(bound, BLACK);

        float[][] filledImage = hf.fillHole(bound);

        hf.showImage();
        hf.showImage(markedBoundsImage);
        hf.showImage(filledImage);
    }

    /**
     * A stub that is supposed to read the wanted image and return the float[][]
     */
    private static void getImage() {
        throw new NotImplementedException();
    }

    private static void getParam(String paramName, float param) {
        print("please choose a " + paramName + " value or press ENTER for default:");
        String zString = scanner.nextLine();
        if (zString == null || (zString = zString.trim()).equals("") || zString.length() == 0){
            print("illegal " + paramName + " entered, default is: " + param);
        }else {
            try {
                float tmpParam = Float.valueOf(zString);
                if (paramName == EPSILON_NAME && tmpParam == 0) {
                    print("epsilon cannot be 0, returning to default " + param);
                    return;
                }
                param = tmpParam;
                print(paramName + " is: " + param);
            } catch (NumberFormatException e) {
                print("given " + paramName + " is illegal, default is: " + param);
            }
        }
    }

    public static void getWeight() {
        print("Please choose a weight function:");
        print("1 - default weight function");

        String weightString = scanner.nextLine();
        int weightStrat = WeightStrategyFactory.WEIGHT_STRATEGY_DEFAULT;

        if (weightString == null || weightString.equals("")){
            print("illegal number entered, creating default weight function");
        }else {
            try {
                weightStrat = Integer.valueOf(weightStrat);
            } catch (Exception e) {
                print("illegal number entered, creating default weight function");
            }
        }

        weight = WeightStrategyFactory.getWeight(weightStrat, z, epsilon);
    }

    public static void print(String str){
        System.out.println(str);
    }
}
