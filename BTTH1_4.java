package BTTH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BTTH1_4 {
    static void main() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();

        int[] mang = new int[n];
        for (int i=0;i<n;i++)
        {
            mang[i] = sc.nextInt();
        }
        timDayCon(mang,k);

    }

    public static void timDayCon(int[] mang,int k)
    {
        int n = mang.length;
        int[][] dp = new int[n+1][k+1];

        for (int i =0;i<=n;i++)
        {
            for(int j=0;j<=k;j++)
            {
                dp[i][j] = -1;
            }
            dp[i][0] = 0;
        }

        for (int i=1;i<=n;i++)
        {
            for(int j=1;j<=k;j++)
            {
                dp[i][j] = dp[i-1][j];
                if (j >= mang[i-1] && dp[i-1][j-mang[i-1]] != -1)
                {
                    dp[i][j] = Math.max(dp[i][j], dp[i-1][j-mang[i-1]] +1);

                }
            }
        }

        if (dp[n][k] == -1) {
            System.out.println("Khong co day con nao co tong bang " + k);
            return;
        }


        ArrayList<Integer> ketQua = new ArrayList<>();
        int i = n;
        int j = k;

        while (i > 0 && j > 0) {
            if (dp[i][j] == dp[i - 1][j]) {
                i--;
            } else {
                ketQua.add(mang[i - 1]);
                j = j - mang[i - 1];
                i--;
            }
        }

        Collections.reverse(ketQua);

        for (int x = 0; x < ketQua.size(); x++) {
            System.out.print(ketQua.get(x));
            if (x < ketQua.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }


}
