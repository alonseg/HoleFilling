package HoleFillingPkg.WeightStrategyPkg;

import HoleFillingPkg.Pixel;

/**
 * Created by Alon on 1/11/2016.
 */
public class WeightStrategyDefault extends WeightStrategy {

    public WeightStrategyDefault(float z, float epsilon) {
        super(z, epsilon);
    }

    @Override
    public double weight(Pixel pa, Pixel pb) {
        double denominator = Math.pow(Math.abs(pa.val - pb.val), z) + epsilon;
        return 1 / denominator;
    }
}
