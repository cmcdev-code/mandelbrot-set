import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import javax.swing.*;
import java.util.Scanner;

public class Main {
        // Interpolate colors using smoothstep function
        private static Color interpolateColors(Color startColor, Color endColor, double t) {
            t = smoothstep(0, 1, t);
            int r = (int) (startColor.getRed() * (1 - t) + endColor.getRed() * t);
            int g = (int) (startColor.getGreen() * (1 - t) + endColor.getGreen() * t);
            int b = (int) (startColor.getBlue() * (1 - t) + endColor.getBlue() * t);
            return new Color(r, g, b);
        }
    
        // Smoothstep function for smooth interpolation
        private static double smoothstep(double edge0, double edge1, double x) {
            x = clamp((x - edge0) / (edge1 - edge0), 0, 1);
            return x * x * (3 - 2 * x);
        }
    
        // Clamp function to restrict values within a range
        private static double clamp(double value, double min, double max) {
            return Math.max(min, Math.min(max, value));
        }
    //function that will take a range of integers inclusive and convert them to a range of real numbers inclusive
    static double[] integer_to_real_range(int int_start,int int_end, double real_start,double real_end){
                                            
            double[] real_range_array= new double[int_end-int_start];
            

            //this is assuming that it won't just be a single point
            double delta = (real_end-real_start)/(int_end-int_start);      

            for(int i=0;i<int_end-int_start;i++){
                real_range_array[i]= delta*i+real_start;
            }

        return real_range_array;
    }
    // //function that will mimic the mandelbrot equation and will return the number of iterations 
    // static int complex_function(double x0, double y0, int max_iterations){

    //     int iterations=0;
    //     double x=0.0,y=0.0;        
    //     //https://en.wikipedia.org/wiki/Mandelbrot_set
    //     for(;x*x +y*y <= 4.0 && iterations<max_iterations;iterations++ ){

    //         double xtemp = x*x - y*y + x0;
    //         y= 2*x*y + y0;
    //         x=xtemp;
    //     }
    //     return iterations;
    // }


 //function that will mimic the mandelbrot equation and will return the number of iterations 
    static int complex_function(double x0, double y0, int max_iterations){

        int iterations=0;
        double x=0.0,y=0.0;        
        //https://en.wikipedia.org/wiki/Mandelbrot_set
        for(;x*x +y*y <= 4.0 && iterations<max_iterations;iterations++ ){

            double xtemp = x*x - y*y + x0;
            y= 2*x* y + y0;
            x=xtemp;
        }
        return iterations;
    }




  public static void main(String[] args) {
        int canvasWidth = 0;
        int canvasHeight = 0;
        System.out.println("What do you want the width to be?");
        Scanner dim_input= new Scanner(System.in);
        
        
        canvasWidth=Integer.parseInt(dim_input.nextLine());

        System.out.println("What do you want the height to be?");

        canvasHeight=Integer.parseInt(dim_input.nextLine());


        dim_input.close();
             // Create a dictionary to map iterations to colors
        Map<Integer, Color> colorMap = new HashMap<>();

        // Define colors for different iteration counts
        colorMap.put(0, Color.BLACK);

        // Define gradient colors
        Color startColor = new Color(0x2a0e26);
        Color endColor= new Color(0x4f97a2);
     
             // Add smooth gradient iteration-color mappings
        for (int i = 1; i <= 20; i++) {
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }
     
        startColor=endColor;
        endColor= new Color(0x6e69b1);
   
        

        for(int i=21;i<=41;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }
        

        startColor=endColor;
          endColor= new Color(0x8f4680);
    


        for(int i=42;i<=63;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }


        startColor=endColor;
        endColor= new Color(0x7f4036);


        for(int i=64;i<=80;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }

        startColor=endColor;
       endColor = new Color(0x454811);

        for(int i=81;i<=100;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }
        
        startColor=endColor;
       endColor = new Color(0x14431d);
              for(int i=101;i<=121;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }
        

        startColor=endColor;
       endColor = new Color(0x092a2e);
              for(int i=122;i<=143;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }
         startColor=endColor;
       endColor = new Color(0x0f0e1f);
              for(int i=144;i<=165;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }

           startColor=endColor;
       endColor = new Color(0x000000);
              for(int i=166;i<=200;i++){
            double t = (double) i / 100; // Normalized value between 0 and 1
            Color interpolatedColor = interpolateColors(startColor, endColor, t);
            colorMap.put(i, interpolatedColor);
        }


        JFrame frame = new JFrame("Pixel Canvas Example");
        PixelCanvas pixelCanvas = new PixelCanvas(canvasWidth, canvasHeight);
        frame.getContentPane().add(pixelCanvas);
        frame.setSize(canvasWidth, canvasHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        

        double[] real_numbers_x= integer_to_real_range(0,canvasWidth,-2, 0.47);
        double[] real_numbers_y= integer_to_real_range(0, canvasHeight,-1.2,1.2);

        for(int i=0; i<canvasWidth;i++){
            for(int j=0;j<canvasHeight;j++){
                pixelCanvas.drawPixel(i,j,colorMap.get(complex_function(real_numbers_x[i],real_numbers_y[j],200 )));
            }
        }
        pixelCanvas.repaint();

    pixelCanvas.saveCanvasAsImage("canvas2.png", "PNG");

  }
}