
class Complex{
    public double a;
    public double b;
    public int scale;

    public Complex(double a,double b,int scale){
        this.a=a;
        this.b=b;
        this.scale=scale;
    }

    public Complex complex_multiplication(Complex other_number){
        return new Complex(this.a*other_number.a - this.b-other_number.b,this.b*other_number.a+this.a*other_number.b,this.scale+other_number.scale);
    }
   
    public <T extends Number> Complex real_multiplication(T real_number){
        return new Complex(this.a *real_number.doubleValue(),this.b*real_number.doubleValue(),this.scale);
    }
    public <T extends Number> Complex real_addition(T real_number){
        return new Complex(real_number.doubleValue()*Math.pow(10,-this.scale)+this.a,this.b,this.scale);
    }

    public Complex complex_addition(Complex otherComplex){
        if(this.scale==otherComplex.scale){
            return new Complex(this.a+otherComplex.a,this.b+otherComplex.b, scale);
        }
        if(this.scale>otherComplex.scale){
            return new Complex(this.a*Math.pow(10,this.scale-otherComplex.scale)+otherComplex.a,this.b*Math.pow(10,this.scale-otherComplex.scale)+otherComplex.b,otherComplex.scale);
        }
        if(this.scale<otherComplex.scale){
            return new Complex(otherComplex.a*Math.pow(10,otherComplex.scale-this.scale)+this.a,otherComplex.b*Math.pow(10,otherComplex.scale-this.scale)+this.b,this.scale);
        }
        System.out.println("That shouldn't be possible");
        return new Complex(0.0,0.0,10);
    }

} 
