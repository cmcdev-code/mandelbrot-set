import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import javax.swing.*;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            index_for_switching_colors += ( i % delta_colors - 1>> 31) & 0x1;
        }
        colorMap.put(number_of_iterations,new Color(array_of_colors[index_for_switching_colors-1]));

        return colorMap;
    } 


    //function that will take in a 3d array and convert it to 2d
    static int[][] array_3d_to_2d(int[][][] array_3d){

        int[][] array_2d= new int[array_3d.length*array_3d[0][0].length][array_3d[0].length];

        
        for(int k=0;k< array_3d[0][0].length;k++){
            for(int i=0;i<array_3d.length;i++){

                for(int j=0;j<array_3d[0].length;j++){
                array_2d[k*array_3d.length+i][j]=array_3d[i][j][k];
                }
            }
        }    
        return array_2d;
    }

    static int[] user_inputs(){
        
        int[] all_user_inputs = new int[4];

        System.out.println("What do you want the width to be? Must be a power of 2");
        Scanner dim_input= new Scanner(System.in);
        
        
        all_user_inputs[0]=Integer.parseInt(dim_input.nextLine());

        System.out.println("What do you want the height to be? Must be a power of 2");

        all_user_inputs[1]=Integer.parseInt(dim_input.nextLine());

        System.out.println("How many threads? must be a power of 2 less then or equal to number of threads");
        all_user_inputs[2]=Integer.parseInt(dim_input.nextLine());
 
        System.out.println("How many iterations ?");

        all_user_inputs[3]=Integer.parseInt(dim_input.nextLine());
        dim_input.close();

        return all_user_inputs;
    }

    static PixelCanvas create_new_pixel_Canvas(int canvasWidth,int canvasHeight,Map<Integer,Color> colorMap,int number_of_threads,double[] real_numbers_x,double[] real_numbers_y,int number_of_iterations){
        PixelCanvas pixelCanvas=new PixelCanvas(canvasWidth,canvasHeight);

        int[][] values=new int[canvasWidth][canvasHeight];
        
        int ind_thread_range_width= (int)canvasWidth/number_of_threads;
        int ind_thread_range_height = canvasHeight;

        int[][][] iterations_threads= new int[ind_thread_range_width][ind_thread_range_height][number_of_threads]; 
        long time_thread_start= System.currentTimeMillis();
        FractalThread[] threads = new FractalThread[number_of_threads];
        Thread[] threadObjects = new Thread[number_of_threads];
        
        for (int i = 0; i < number_of_threads; i++) {
            threads[i] = new FractalThread(real_numbers_x, real_numbers_y, ind_thread_range_width, ind_thread_range_height, i, number_of_iterations);
            threadObjects[i] = new Thread(threads[i]);
            threadObjects[i].start();
        }
        
        for (int i = 0; i < number_of_threads; i++) {
            try {
                threadObjects[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            
            for(int i=0 ;i<number_of_threads;i++){
            for (int x = 0; x < ind_thread_range_width; x++) {
                int[][] thread_result=threads[i].getResult();
                for (int y = 0; y < ind_thread_range_height; y++) {
                    iterations_threads[x][y][i] = thread_result[x][y] ;
                }
            }
        }
        long time_thread_end= System.currentTimeMillis();

        System.out.println("Thread time "+(double) (time_thread_end-time_thread_start)/1000);
        values=array_3d_to_2d(iterations_threads);
              
        for(int i=0; i<canvasWidth;i++){
            for(int j=0;j<canvasHeight;j++){
                    pixelCanvas.drawPixel(i,j,colorMap.get(values[i][j]));
            }
        }
        pixelCanvas.repaint();
        
        return pixelCanvas;
    }


  public static void main(String[] args) {
        int[] user_inputs_array=user_inputs();
        int canvasWidth = user_inputs_array[0];
        int canvasHeight = user_inputs_array[1];
        int number_of_threads=user_inputs_array[2];
        int number_of_iterations=user_inputs_array[3];
        
        //https://davidjohnstone.net/cubehelix-gradient-picker
        int[] colors={0x000000,0x87cdae,0x6cb5c0,0x6e8fd0,0x8563c4,0x9b3f95,0xe962c4f,
                          0x702911,0x372900,0xfad4c0};
        //int[] colors={0x2f2d5b,0x1b545b,0x2b6f38,0x6b6e29,0xaa6258,0xb66aa8,0x9390d5,371430,0x1d0b09,0x000000};


        Map<Integer, Color> colorMap = generate_color_map(number_of_iterations,colors);

        double[] real_numbers_x= integer_to_real_range(0,canvasWidth,-2, 0.47);
        double[] real_numbers_y= integer_to_real_range(0, canvasHeight,-1.2,1.2);
        
        PixelCanvas pixelCanvas = create_new_pixel_Canvas(canvasWidth, canvasHeight, colorMap, number_of_threads,real_numbers_x,real_numbers_y,number_of_iterations);
      
        JFrame frame1 = new JFrame("MandelBrot");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(canvasWidth+300, canvasHeight);
        JButton button = new JButton("Take picture");
        JButton zoomButton= new JButton("Zoom in box");
         zoomButton.setBounds(canvasWidth,41,100,40);
            frame1.add(zoomButton);
            button.setBounds(canvasWidth,0, 100,40);
            frame1.add(button);
            // Add the panel to the frame
            frame1.getContentPane().add(pixelCanvas);
    
                // Display the frame
            frame1.setVisible(true);
            
            JLabel high_D_width=new JLabel("High d photo width");
            JTextField high_D_Width_number = new JTextField(10);
           

            high_D_width.setBounds(canvasWidth+20,81,100,20);
            high_D_Width_number.setBounds(canvasWidth+200,101,100,30);
        

            JLabel high_D_length=new JLabel("High d photo length");
            
            JTextField high_D_length_number = new JTextField();

            high_D_length.setBounds(canvasWidth,131,100,20);
            high_D_length_number.setBounds(canvasWidth,151,100,30);

            JLabel img_name_label= new JLabel("img name");

            JTextField img_name_enter= new JTextField();

            JLabel iterations_text_field= new JLabel("Depth");


            JTextField iterations = new JTextField();
            
            
            img_name_label.setBounds(canvasWidth,181,100,20);
            img_name_enter.setBounds(canvasWidth,201,100,30);

            iterations_text_field.setBounds(canvasWidth,231,100,20);
            iterations.setBounds(canvasWidth,251,100,30);


            frame1.add(high_D_width);
            frame1.add(high_D_Width_number);
            frame1.add(high_D_length);
            frame1.add(high_D_length_number);
            frame1.add(img_name_label);
            frame1.add(img_name_enter);
            frame1.add(iterations_text_field);
            frame1.add(iterations);

        SwingUtilities.invokeLater(() -> {
            
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    
                    int highDefinitionPic_width=0;
                    int highDefinitionPic_height=0;
                    String img_name="";

                    String width=high_D_Width_number.getText();
                    String length= high_D_length_number.getText();

                    double[] real_range_width= integer_to_real_range(0,30000, real_numbers_x[0], real_numbers_x[real_numbers_x.length-1]);
                    double[] real_range_length= integer_to_real_range(0,30000, real_numbers_y[0], real_numbers_y[real_numbers_y.length-1]);

                    PixelCanvas new_pixel=create_new_pixel_Canvas(30000,30000, colorMap, number_of_threads, real_range_width,real_range_length, 1000);

                    new_pixel.saveCanvasAsImage("testinaddgHighD.png","PNG");
                    
                }
            });
            
            zoomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedTopLeftX = pixelCanvas.getSquareTopLeftX();
                    int selectedTopLeftY = pixelCanvas.getSquareTopLeftY();
                    int selectedSize = pixelCanvas.getSquareSize();

                    System.out.println("Pixel top left "+selectedTopLeftX+" "+selectedTopLeftY);
                    
                    // Calculate updated real number ranges based on the selected square's coordinates
                    double[] real_numbers_x_copy = integer_to_real_range(0, canvasWidth, real_numbers_x[selectedTopLeftX], real_numbers_x[selectedTopLeftX + selectedSize]);
                    double[] real_numbers_y_copy = integer_to_real_range(0, canvasHeight, real_numbers_y[selectedTopLeftY], real_numbers_y[selectedTopLeftY + selectedSize]);
            
                    // Update the real number arrays with the new values
                    System.arraycopy(real_numbers_x_copy, 0, real_numbers_x, 0, canvasWidth);
                    System.arraycopy(real_numbers_y_copy, 0, real_numbers_y, 0, canvasHeight);
            
                    // Update the selected square's properties
                    pixelCanvas.setSquareTopLeftX(selectedTopLeftX);
                    pixelCanvas.setSquareTopLeftY(selectedTopLeftY);
                    pixelCanvas.setSquareSize(selectedSize);
                    
                    
                    pixelCanvas.copyCanvasContents(create_new_pixel_Canvas(canvasWidth, canvasHeight, colorMap, number_of_threads, real_numbers_x, real_numbers_y, number_of_iterations));

                    // Refresh the display
                    pixelCanvas.repaint();
                }
            });
            
           
            });

  }
}