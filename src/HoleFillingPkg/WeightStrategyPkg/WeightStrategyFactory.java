package HoleFillingPkg.WeightStrategyPkg;

/**
 * Created by Alon on 1/12/2016.
 */
public class WeightStrategyFactory {
    public static int WEIGHT_STRATEGY_DEFAULT = 1;

    public static WeightStrategy getWeight(int strategy, float z, float epsilon){
        assert(epsilon != 0);
        switch (strategy){
            default:
                return new WeightStrategyDefault(z, epsilon);
        }
    }
}
