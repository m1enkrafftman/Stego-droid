package devry.networkswitch.com.stegonosaurus.imageutils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import helper.AppConstant;

import static android.graphics.BitmapFactory.decodeFile;


public class Model
{
    private static Header myHeader = new Header(4, 1, 4, 1, 8, 12);

    //options for encode type
    private final static int DataMap = 0;
    private final static int BaseN = 1;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void modifyImage(String imgLoc, String fileLoc)
    {
        Bitmap img1 = loadImage(imgLoc);
        Bitmap img = img1.copy(img1.getConfig(), true);
        img.setConfig(Bitmap.Config.ARGB_8888);
        ArrayList<Integer> data = loadFile(fileLoc);


        int base = encodeBase(data.size(), img.getHeight() * img.getWidth() * 3);
        data = transformDataToBaseN(data, base);

        setHeader(BaseN, base, false, fileLoc, data.size());

        //logic to modify each pixel such that val%base == convertedFileData
        Random rand = new Random();
        int[] header = myHeader.getData();
        int count = 0;
        for(int i = 0; i < img.getWidth(); i ++)
        {
            for(int j = 0; j < img.getHeight(); j++)
            {
                int[] pixel = pixelRGB(img.getPixel(i, j));
                for(int k = 0; k < 3; k++)
                {
                    if(pixel[k] + base > 255)
                    {
                        pixel[k] = pixel[k] - base;
                    }
                    if(count < myHeader.length())
                    {
                        pixel[k] = pixel[k] - pixel[k]%myHeader.getHeaderBase() + header[count];
                    }
                    else if(count >= data.size() + myHeader.length())
                    {
                        pixel[k] = pixel[k] - pixel[k]%base + rand.nextInt(base);
                    }
                    else
                    {
                        pixel[k] = pixel[k] - pixel[k]%base + data.get(count - myHeader.length());
                    }
                    count++;
                }
                //Log.e("Pixel before setting" , "rbg: " + pixelRGB(pixel));
                img.setPixel(i, j, pixelRGB(pixel));
            }
        }
        saveImage(img,imgLoc);
    }

    public static void createDataImage(String imgLoc, String fileLoc)
    {
        Bitmap img = null;
        ArrayList<Integer> data = loadFile(fileLoc);

        int base = 256;

        setHeader(DataMap, base, false, fileLoc, data.size());

        //logic to modify each pixel such that val%base == convertedFileData
        Random rand = new Random();
        int x = (int) Math.ceil(Math.sqrt((data.size() + myHeader.length())/3));
        img = Bitmap.createBitmap(x, x, Bitmap.Config.ARGB_8888 );
        int[] header = myHeader.getData();
        int count = 0;
        for(int i = 0; i < x; i ++)
        {
            for(int j = 0; j < x; j++)
            {
                int[] pixel = new int[3];
                for(int k = 0; k < 3; k++)
                {
                    if(count < myHeader.length())
                    {
                        pixel[k] = header[count];
                    }
                    else if(count >= data.size() + myHeader.length())
                    {
                        pixel[k] = rand.nextInt(256);
                    }
                    else
                    {
                        pixel[k] = data.get(count - myHeader.length());
                    }
                    count++;
                }

                img.setPixel(i, j, pixelRGB(pixel));
            }
        }
        saveImage(img,imgLoc);
    }

    public static void pullDataFromImage(String imgLoc, String fileSaveLoc)
    {
        Bitmap img = loadImage(imgLoc);
        ArrayList<Integer> data = new ArrayList<Integer>();

        boolean done = false;
        int headerBase = myHeader.getHeaderBase();
        int count = 0;
        for(int i = 0; i < img.getWidth() && !done; i ++)
        {
            for(int j = 0; j < img.getHeight() && !done; j++)
            {
                int[] pixel = pixelRGB(img.getPixel(i, j));
                for(int k = 0; k < 3 && !done; k++)
                {
                    if(count < myHeader.length())
                    {
                        myHeader.setData(count, pixel[k] % headerBase);
                    }
                    else if(count <= myHeader.encodeSize.getEncodeSize() + myHeader.length())
                    {
                        data.add(pixel[k]);
                    }
                    else
                    {
                        done = true;
                    }
                    count++;
                }
            }
        }

        if(myHeader.encodeType.getEncodeType() == BaseN)
        {
            int base = myHeader.encodeBase.getEncodeBase();
            for(int i = 0; i < data.size(); i++)
            {
                data.set(i, data.get(i)%base);
            }
            data = transformDataFromBaseN(data, base);
        }
        saveFile(fileSaveLoc += myHeader.fileExtension.getFileExtension(), data);
    }

    @SuppressWarnings("resource")
    private static ArrayList<Integer> loadFile(String fileLocation)
    {
        BufferedInputStream is = null;
        try
        {
            is = new BufferedInputStream(new FileInputStream(fileLocation), 10000000);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ArrayList<Integer> al = new ArrayList<Integer>();
        try
        {
            for(int i = is.read(); i != -1; i = is.read())
            {
                al.add(i);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return al;
    }

    private static void saveFile(String fileSaveLocation, ArrayList<Integer> fileData)
    {
        BufferedOutputStream os = null;
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(fileSaveLocation));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        for(int i = 0; i < fileData.size(); i++)
        {
            try
            {
                os.write(fileData.get(i));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            os.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            os.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static int encodeBase(int sizeData, int sizeStorage)
    {
        int base = 0;
        int b = 2;
        while(base == 0)
        {
            if(sizeData * Transformations.baseNByteLength(b) < sizeStorage)
            {
                base = b;
            }

            if(b > 16)
            {
                System.err.println("file too large");
            }

            b++;
        }
        return base;
    }

    private static ArrayList<Integer> transformDataToBaseN(ArrayList<Integer> data, int base)
    {
        ArrayList<Integer> convertedFileData = new ArrayList<Integer>();
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        int byteLength = Transformations.baseNByteLength(base);
        for(int i = 0; i < data.size(); i++)
        {
            int numberToBeChanged = data.get(i);
            for(int j = 0; j < byteLength; j++)
            {
                tempList.add(numberToBeChanged%base);
                numberToBeChanged = (int) Math.floor(numberToBeChanged/base);
            }

            for(int k = tempList.size() - 1; k >= 0; k--)
            {
                convertedFileData.add(tempList.get(k));
            }
            tempList = new ArrayList<Integer>();
        }

        return convertedFileData;
    }

    private static ArrayList<Integer> transformDataFromBaseN(ArrayList<Integer> data, int base)
    {
        ArrayList<Integer> tempData = new ArrayList<Integer>();
        int byteLength = Transformations.baseNByteLength(base);
        Integer tempInt = 0;
        for(int i = 0; i < data.size(); i++)
        {
            tempInt = tempInt * base;
            tempInt = tempInt + data.get(i);
            if(i % byteLength == byteLength - 1)
            {
                tempData.add(tempInt);
                tempInt = 0;
            }
        }
        return tempData;
    }

    private static void setHeader(int encodeType, int base, boolean isEncrypted, String fileLocation, int dataLength)
    {
        //set encode type
        myHeader.encodeType.setEncodeType(encodeType);

        //set encode base
        myHeader.encodeBase.setEncodeBase(base);

        //set boolean isEncrypted
        myHeader.encodeEncryption.setEncodeEncryption(isEncrypted);

        //set file extension
        myHeader.fileExtension.setFileExtension(fileLocation);

        //set data length
        myHeader.encodeSize.setEncodeSize(dataLength);
    }

    private static int[] pixelRGB(int pixel)
    {
//		int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        int[] id = {red, green, blue};
        return id;
    }

    private static int pixelRGB(int[] pixel)
    {
        //Log.e("Pixel RBG: " , "After Mode " + pixel[0] + " " + pixel[1] + " " + pixel[2]);

        //return ((pixel[0]&0x0ff)>>16)|((pixel[1]&0x0ff)>>8)|(pixel[2]&0x0ff);
        return Color.argb(255, pixel[0], pixel[1], pixel[2]);
    }

    private static Bitmap loadImage(String imgLocation)
    {
        Bitmap b = decodeFile(imgLocation);

        return decodeFile(imgLocation);
    }

    public static byte[] getByteArrayFromBitmap(Bitmap img)
    {
        final int lnth = img.getByteCount();
        ByteBuffer dst = ByteBuffer.allocate(lnth);
        img.copyPixelsToBuffer(dst);
        return dst.array();
    }

    public static Bitmap getBitmapFromByteArray(byte[] byte_array)
    {
        return new BitmapFactory().decodeByteArray(byte_array, 0, byte_array.length);
    }

    private static final char[] letters = "asdfghjklqwertyuiopzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();

    public static String getRandomPNGName()
    {
        Random randy = new Random();
        int len = randy.nextInt(15) + 10;
        StringBuilder out = new StringBuilder();
        for(int x = 0; x < len; x++)
        {
            out.append(letters[randy.nextInt(letters.length - 1)]);
        }
        out.append(".png");
        return out.toString();
    }

    public static String getRandomTXTName()
    {
        Random randy = new Random();
        int len = randy.nextInt(15) + 10;
        StringBuilder out = new StringBuilder();
        for(int x = 0; x < len; x++)
        {
            out.append(letters[randy.nextInt(letters.length - 1)]);
        }
        out.append(".txt");
        return out.toString();
    }


    private static void saveImage(Bitmap img, String imgLocation)
    {
        //+ getRandomPNGName()
        byte[] bytes = getByteArrayFromBitmap(img);
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory()) + "/Download/"+ getRandomPNGName());
        AppConstant.currentlyModified = String.valueOf(Environment.getExternalStorageDirectory() + "/Download/"+ getRandomPNGName());

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try
//        {
//            if(file.exists()) file.delete();
//            file.createNewFile();
//            FileOutputStream out = new FileOutputStream(file);
//            out.write(bytes);
//            out.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
    }
}
