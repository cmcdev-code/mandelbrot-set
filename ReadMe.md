# The Mandelbrot Fractal 

![](https://github.com/cmcdev-code/mandlebrot/blob/main/media/fractal100.gif)

This code is written in java and will just produce an image of the mandelbrot fractal.


The function bellow can be modified to produce different fractals on the complex plane with ease. 


```java

  static int complex_function(double x0, double y0, int max_iterations){

        int iterations=0;
        double x=0.0,y=0.0;        
        for(;x*x +y*y <= 4.0 && iterations<max_iterations;iterations++ ){

            double xtemp = x*x - y*y + x0;
            y= 2*x*y + y0;
            x=xtemp;
        }
        return iterations;
    }

```

This other function bellow uses branchless programming to know when to increment to the next color. In other languages boolean values either are a integer 0 or 1 while in java they are only denoted "true/false". So the line instead gets the index mod delta color and then subtracts 1 from it. Because it is a signed integer the 31st bit will only be a 1 if it is negative and otherwise will be just a 0, so thats what I do instead.

```java
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
```
## Time to create images

On my system Ryzen 7 3700x ~4.2GHz 120gb DDR4 ram 3600mhz.

32768 $\times$ 32768 image 
* 1 Thread 553.909s
* 4 Threads 518.253s
* 8 Threads 516.122s
* 
