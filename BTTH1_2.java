package BTTH1;

import java.util.Random;


public class BTTH1_2 {
    static void main() {
        double r = 1;
        int N = 10000;
        double[][] mang = random(N,r);
        int n = dem(mang,r);
        double S = tinhDienTich(n,N,r);
        double p = S / (r*r);
        System.out.println("So pi la : "+p);
    }


    public static double[][] random(int n,double r)
    {
        Random rd = new Random();
        double[][] mang = new double[n][2];
        for (int i = 0; i <n;i++)
        {
            mang[i][0] = rd.nextDouble(-r,r);
            mang[i][1] = rd.nextDouble(-r,r);
        }
        return mang;
    }
    public static int dem (double[][] mang,double r)
    {
        int n=0;
        for (int i = 0; i <mang.length;i++)
        {
            double x = mang[i][0];
            double y = mang[i][1];
            if (x*x + y*y <= r*r)
            {
                n++;
            }
        }
        return n;
    }
    public static double tinhDienTich (int n, int N,double r)
    {
        double S;
        double n1 = n;
        double N1 = N;
        S = (n1/N1)*4*r*r;
        return S;
    }
}
