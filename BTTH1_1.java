package BTTH;

import java.util.Random;
import java.util.Scanner;

public class BTTH1_1 {
    static void main() {
        System.out.println("Nhap vao ban kinh r : ");
        Scanner sc = new Scanner(System.in);
        double r = sc.nextFloat();
        int N = 10000;
        double[][] mang = random(N,r);
        int n = dem(mang,r);
        double S = tinhDienTich(n,N,r);
        System.out.println("Dien tich la : "+S);
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
