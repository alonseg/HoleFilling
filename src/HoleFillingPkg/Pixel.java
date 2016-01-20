package HoleFillingPkg;

/**
 * Created by Alon on 1/11/2016.
 */
public class Pixel {
    public int row;
    public int col;

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public float val;

    public Pixel(int row, int col, float val){
        this.col = col;
        this.row = row;
        this.val = val;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pixel pixel = (Pixel) o;

        if (row != pixel.row) return false;
        if (col != pixel.col) return false;
        return Float.compare(pixel.val, val) == 0;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        result = 31 * result + (val != +0.0f ? Float.floatToIntBits(val) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HoleFillingPkg.Pixel(" +
                 row +
                ", " + col +
                ") val=" + val;
    }
}
