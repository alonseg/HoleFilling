package HoleFillingPkg.WeightStrategyPkg;

import HoleFillingPkg.Pixel;

/**
 * Created by Alon on 1/11/2016.
 */
public abstract class WeightStrategy {

    public static float z = 2;
    public static float epsilon = 0.0001f;
    public WeightStrategy(float z, float epsilon){
        this.z = z;
        this.epsilon = epsilon;

    }

    public abstract double weight(Pixel pa, Pixel pb);
}
