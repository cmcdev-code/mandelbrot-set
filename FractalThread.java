public class FractalThread implements Runnable {
    private double[] realNumbersX;
    private double[] realNumbersY;
    private int startX;
    private int startY;
    private int threadIndex;
    private int maxIterations;
    private int[][] result;

    public FractalThread(double[] real_range_x,double[] real_range_y, int region_x,int region_y,int thread_number,int iterations) {
        this.realNumbersX = real_range_x;
        this.realNumbersY = real_range_y;
        this.startX = region_x;
        this.startY = region_y;
        this.threadIndex = thread_number;
        this.maxIterations = iterations;
        this.result = new int[region_x][region_y];
    }
   //function that will mimic the mandelbrot equation and will return the number of iterations 
   static int complex_function(double x0, double y0, int max_iterations){

    int iterations=0;
    double x=0.0,y=0.0;        
    //https://en.wikipedia.org/wiki/Mandelbrot_set
    for(;x*x +y*y <= 4.0 && iterations<max_iterations;iterations++ ){

        double xtemp = x*x - y*y + x0;
        y= 2*x*y + y0;
        x=xtemp;
    }
    return iterations;
    }

    public int[][] getResult() {
        return result;
    }

    @Override
    public void run() {
        for(int i =0;i<this.startX;i++){
            for(int j=0;j<this.startY;j++){
                result[i][j] = complex_function(this.realNumbersX[this.threadIndex*startX+i],this.realNumbersY[j], this.maxIterations);
            }
        }
    }
}
