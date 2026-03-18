package BTTH1;

import java.util.Arrays;
import java.util.Scanner;

public class BTTH1_3 {
    static void main() {
        System.out.println("Nhap so luong tram canh cao : ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int [][] mang = nhapDiem(n,sc);
        sapXep(mang);
        mang = timBaoLoi(mang);
        for (int i=0;i< mang.length;i++)
        {
            System.out.println(mang[i][0]+" "+mang[i][1]);
        }
       /* for(int i =0;i<n;i++)
        {
            System.out.println(mang[i][0] + " "+ mang[i][1]);
        }
        */
    }

    public static int[][] nhapDiem (int n, Scanner sc)
    {
        int[][] mang = new int[n][2];
        for(int i =0;i<n;i++)
        {
            mang[i][0] = sc.nextInt();
            mang[i][1] = sc.nextInt();
        }
        return mang;
    }

    public static void sapXep (int[][] mang)
    {
        Arrays.sort(mang,(x,y) -> {
            if (x[0] != y[0])
            {
                return Integer.compare(x[0],y[0]);
            }
            return Integer.compare(x[1],y[1]);
        });
    }
    public static int[][] timBaoLoi (int[][] mang) {
        int n = mang.length;
        if (n <=3) return mang;
        // Upper

        int[][] upper = new int[n][2];
        int count=0;
        for (int i =0;i<n;i++)
        {
            while (count >=2 && xetHuong(upper[count-2],upper[count-1],mang[i]) >=0 )
            {
                count--;
            }
            upper[count] = mang[i];
            count++;
        }

        // Lower

        int[][] lower = new int[n][2];
        int count2 = 0;
        for (int i =n-1; i>=0; i--)
        {
            while (count2 >=2 && xetHuong(lower[count2-2],lower[count2-1],mang[i]) >=0)
            {
                count2--;
            }
            lower[count2]=mang[i];
            count2++;
        }

        //merge

        int tong = (count-1) + (count2-1);
        int[][] baoLoi = new int[tong][2];
        int chiSo = 0;
        for (int i=0;i < count -1; i++)
        {
            baoLoi[chiSo++] = upper[i];
        }
        for (int i=0;i < count2 - 1; i++)
        {
            baoLoi[chiSo++] = lower[i];
        }
        return baoLoi;
    }

    public static int xetHuong (int[] a, int[] b, int[] c)
    {
        return ((b[0] - a[0])* (c[1]-a[1]) - (b[1]-a[1]) * (c[0]-a[0]));
    }
}
