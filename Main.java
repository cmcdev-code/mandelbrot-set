import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import javax.swing.*;
import java.util.Scanner;

public class Main {
 
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

    //Function that will create a map of colors using the number of iterations the array of colors must divide number of iterations
    static Map<Integer,Color> generate_color_map(int number_of_iterations, int[] array_of_colors){
        Map<Integer, Color> colorMap = new HashMap<>();

        int delta_colors= number_of_iterations/array_of_colors.length;
        int index_for_switching_colors =0;

        for(int i =1 ;i<=number_of_iterations ;i++){
            colorMap.put(i-1,new Color(array_of_colors[index_for_switching_colors]));
            index_for_switching_colors += ( i%delta_colors - 1>> 31) & 0x1;
        }
        colorMap.put(number_of_iterations,new Color(array_of_colors[index_for_switching_colors-1]));

        return colorMap;
    } 




  public static void main(String[] args) {
        int canvasWidth = 0;
        int canvasHeight = 0;

        int number_of_threads=0;
        System.out.println("What do you want the width to be?");
        Scanner dim_input= new Scanner(System.in);
        
        
        canvasWidth=Integer.parseInt(dim_input.nextLine());

        System.out.println("What do you want the height to be?");

        canvasHeight=Integer.parseInt(dim_input.nextLine());

        System.out.println("How many threads?");
        number_of_threads=Integer.parseInt(dim_input.nextLine());


        int number_of_iterations=500;
        dim_input.close();

        //https://davidjohnstone.net/cubehelix-gradient-picker
        int[] colors={0x000000,0x87cdae,0x6cb5c0,0x6e8fd0,0x8563c4,0x9b3f95,0xe962c4f,
                      0x702911,0x372900,0xfad4c0};

        Map<Integer, Color> colorMap = generate_color_map(number_of_iterations,colors);


        JFrame frame = new JFrame("Pixel Canvas Example");
        PixelCanvas pixelCanvas = new PixelCanvas(canvasWidth, canvasHeight);
        frame.getContentPane().add(pixelCanvas);
        frame.setSize(canvasWidth, canvasHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        double[] real_numbers_x= integer_to_real_range(0,canvasWidth,-2, 0.47);
        double[] real_numbers_y= integer_to_real_range(0, canvasHeight,-1.2,1.2);
      
        int[][] values=new int[canvasWidth][canvasHeight];
        
        int ind_thread_range_width= canvasWidth/number_of_threads;
        int ind_thread_range_height = canvasHeight/number_of_threads;

        



        for(int i=0; i<canvasWidth;i++){
            for(int j=0;j<canvasHeight;j++){
                pixelCanvas.drawPixel(i,j,colorMap.get(complex_function(real_numbers_x[i],real_numbers_y[j],number_of_iterations )));
            }
        }
        pixelCanvas.repaint();

    pixelCanvas.saveCanvasAsImage("canvas321322.png", "PNG");

  }
}