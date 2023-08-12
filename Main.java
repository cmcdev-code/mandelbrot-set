import java.awt.*;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import javax.swing.*;
import java.util.Scanner;

public class Main extends Thread{
 
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

    //function that will return a list that is the size of the region needed
    public int[][] run(double[] real_range_x,double[] real_range_y, int region_x,int region_y,int thread_number,int iterations){
        int[][] iterations_value=new int[region_x][region_y];

        for(int i =0;i<region_x;i++){
            for(int j=0;j<region_y;j++){
                iterations_value[i][j] = complex_function(real_range_x[thread_number*region_x+i], real_range_y[thread_number*region_y+j], iterations);
            }
        }
        return iterations_value;

    }


    //This function is wrong i need to figure out how to map it back
    static int[][] array_3d_to_2d(int[][][] array_3d){

        int[][] array_2d= new int[array_3d.length*array_3d[0][0].length][array_3d[0].length*array_3d[0][0].length];

        //This logic is so complicated bellow it is making my head hurt
        for(int i=0;i<array_2d.length;i++){
            int index_dim_3=0;
            for(int j=0;j<array_2d[0].length;j++){
                array_2d[i][j]=array_3d[ i % array_3d.length][j% array_3d[0].length][index_dim_3];
                if (j % array_3d[0].length==0 && j!=0){
                    index_dim_3+=1;
                }
            }
        }
        return array_2d;
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

        
        Main[] threads_array= new Main[number_of_threads];

        for(int i=0;i<number_of_threads;i++){
            threads_array[i]= new Main();
        }
        
        int[][][] iterations_threads= new int[canvasWidth][canvasHeight][number_of_threads];

        for (int i = 0; i < number_of_threads; i++) {
            int[][] threadResult = threads_array[i].run(real_numbers_x, real_numbers_y, ind_thread_range_width, ind_thread_range_height, i, number_of_iterations);
            
            for (int x = 0; x < ind_thread_range_width; x++) {
                for (int y = 0; y < ind_thread_range_height; y++) {
                    iterations_threads[x][y][i] = threadResult[x][y];
                }
            }
        }

        for(int k=0;k<number_of_threads;k++){        
            for(int i =0 ;i<canvasWidth;i++){
                for(int j=0;j<canvasHeight;j++){
            
                    System.out.println(iterations_threads[i][j][k]);
                }
            }
        }
        values=array_3d_to_2d(iterations_threads);

        System.out.println("HERE___________________________");

        for(int i=0;i<canvasWidth;i++){
            for(int j=0 ; j<canvasHeight;j++){
                System.out.println(values[i][j]);
            }
        }

        //turn the 3d array into a 2d array


        for(int i=0; i<canvasWidth;i++){
            for(int j=0;j<canvasHeight;j++){
                pixelCanvas.drawPixel(i,j,colorMap.get(values[i][j]));
            }
        }
        pixelCanvas.repaint();

    pixelCanvas.saveCanvasAsImage("canvas321322.png", "PNG");

  }
}