package part2.week2;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private final double[][] energy;
    private final int[][] parent;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture.width(), picture.height());
        this.energy = new double[picture.width()][picture.height()];
        this.parent = new int[picture.width()][picture.height()];
        for (int i = 0; i < picture.width(); i++)
            for (int j = 0; j < picture.height(); j++)
                this.picture.set(i, j, picture.get(i, j));
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return  picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= picture.width() || y >= picture.height() || x < 0 || y < 0)
            throw new IllegalArgumentException("x = " + x + "; y = " + y);
        if (x == 0 || x == picture.width() -1 || y == 0 || y == picture.height() -1)
            return 1000;
        var xLeft = picture.get(x - 1, y);
        var xRight = picture.get(x + 1, y);
        var yBottom = picture.get(x,y - 1);
        var yTop = picture.get(x, y + 1);
        double xGradient = Math.pow(Math.abs(xLeft.getRed() - xRight.getRed()), 2) +
                Math.pow(Math.abs(xLeft.getGreen() - xRight.getGreen()), 2) +
                Math.pow(Math.abs(xLeft.getBlue() - xRight.getBlue()), 2);
        double yGradient = Math.pow(Math.abs(yBottom.getRed() - yTop.getRed()), 2) +
                Math.pow(Math.abs(yBottom.getGreen() - yTop.getGreen()), 2) +
                Math.pow(Math.abs(yBottom.getBlue() - yTop.getBlue()), 2);
        return Math.sqrt(xGradient + yGradient);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] result = new int[picture.width()];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++) {
                if (i == 0)
                    energy[i][j] = energy(i, j);
                else {
                    energy[i][j] = Double.POSITIVE_INFINITY;
                    for (int k = -1; k < 2; k++) {
                        if ((k + j) >= 0 && (k + j < height())) {
                            double curEnergy = energy[i - 1][k + j];
                            if (curEnergy < energy[i][j]) {
                                energy[i][j] = curEnergy;
                                parent[i][j] = k + j;
                            }
                        }
                    }
                    energy[i][j] += energy(i, j);
                }
            }
        int lastIndex = -1;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height(); i++) {
            if (minEnergy > energy[width() - 1][i]) {
                minEnergy = energy[width() - 1][i];
                lastIndex = i;
            }
        }
        for (int i = width() - 1; i >= 0; i--) {
            result[i] = lastIndex;
            lastIndex = parent[i][lastIndex];
        }
        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] result = new int[picture.height()];
        for (int j = 0; j < height(); j++)
            for (int i = 0; i < width(); i++) {
                if (j == 0)
                    energy[i][j] = energy(i, j);
                else {
                    energy[i][j] = Double.POSITIVE_INFINITY;
                    for (int k = -1; k < 2; k++) {
                        if ((k + i) >= 0 && (k + i < width())) {
                            double curEnergy = energy[k + i][j - 1];
                            if (curEnergy < energy[i][j]) {
                                energy[i][j] = curEnergy;
                                parent[i][j] = k + i;
                            }
                        }
                    }
                    energy[i][j] += energy(i, j);
                }
            }
        int lastIndex = -1;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width(); i++) {
            if (minEnergy > energy[i][height() - 1]) {
                minEnergy = energy[i][height() - 1];
                lastIndex = i;
            }
        }
        for (int i = height() - 1; i >= 0; i--) {
            result[i] = lastIndex;
            lastIndex = parent[lastIndex][i];
        }
        return result;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != picture.width() || picture.height() <= 1)
            throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= height())
            throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++)
            if ((Math.abs(seam[i-1] - seam[i]) > 1) || seam[i] < 0 || seam[i] >= height())
                throw new IllegalArgumentException();
        var newPicture = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            int curPoint = seam[i];
            for (int j = 0; j < height(); j++) {
                if (j == curPoint)
                    continue;
                newPicture.set(i, j < curPoint ? j : j - 1, picture.get(i, j));
            }
        }
        picture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != picture.height() || picture.width() <= 1)
            throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= width())
            throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++)
            if ((Math.abs(seam[i-1] - seam[i]) > 1) || seam[i] < 0 || seam[i] >= width())
                throw new IllegalArgumentException();
        var newPicture = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            int curPoint = seam[j];
            for (int i = 0; i < width(); i++) {
                if (i == curPoint)
                    continue;
                newPicture.set(i < curPoint ? i : i - 1, j, picture.get(i, j));
            }
        }
        picture = newPicture;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture p = new Picture("C:\\Users\\gogiyan.artur\\IdeaProjects\\alg1week1\\src\\6x5.png");
        SeamCarver s = new SeamCarver(p);
        var seam = s.findHorizontalSeam();

    }

}
