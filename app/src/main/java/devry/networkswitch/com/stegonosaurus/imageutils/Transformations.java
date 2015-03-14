package devry.networkswitch.com.stegonosaurus.imageutils;

/**
 * Created by Sebastian Florez on 3/14/2015.
 */

public class Transformations
{

    public static int[] byteToBaseN(int base, int data)
    {
        if(base < 2)
        {
            System.err.println("byteToBaseN base too small error");
        }

        int length = baseNByteLength(base);
        int[] transformedData = new int[length];

        int numberToBeChanged = data;
        for(int i = length - 1; i >= 0 ; i--)
        {
            transformedData[i] = numberToBeChanged%base;
            numberToBeChanged = (int) Math.floor(numberToBeChanged/base);
        }

        return transformedData;
    }

    public static int[] intBase10ToBaseN(int base, int length, int data)
    {
        if(base < 2)
        {
            System.err.println("intBase10ToBaseN base too small error");
        }

        int[] transformedData = new int[length];

        int numberToBeChanged = data;
        for(int i = length - 1; i >= 0 ; i--)
        {
            transformedData[i] = numberToBeChanged%base;
            numberToBeChanged = (int) Math.floor(numberToBeChanged/base);
        }

        if(transformedData[length - 1] >= base)
        {
            System.err.println("intBase10ToBaseN size mismatch error");
        }

        return transformedData;
    }

    public static int intBaseNToBase10(int base, int[] data)
    {
        if(base < 2)
        {
            System.err.println("intBaseNToBase10 base too small error");
        }

        int temp = 0;

        for(int i = 0; i < data.length; i++)
        {
            temp = temp * base;
            temp = temp + data[i];
        }

        return temp;
    }

    public static int baseNByteLength(int base)
    {
        if(base == 2)
        {
            return 8;
        }
        else if(base == 3)
        {
            return 6;
        }
        else if(base == 4 || base == 5 || base == 6)
        {
            return 4;
        }
        else if(base >= 7 && base <= 15)
        {
            return 3;
        }
        else if(base == 16)
        {
            return 2;
        }
        else if(base > 16)
        {
            return 1;
        }

        return 0;
    }
}

