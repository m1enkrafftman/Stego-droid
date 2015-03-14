package devry.networkswitch.com.stegonosaurus.imageutils;

/**
 * Created by Sebastian Florez on 3/14/2015.
 */


/**
 * class to give structure to the header information which needs to always be in the same format so that it is readable.
 * The structure of this class is based on how I want to access these constants (headerName.infoType.getInfoType()). The information
 * that needs to be stored is converted from its' original type to a numerical representation in base 4 and back to its' original type.
 * @author Christian Casadio
 *
 */
public class Header
{
    private int myHeaderBase;
    private int myHeaderLength;
    private int[] myHeaderData;
    public EncodeType encodeType;
    public EncodeBase encodeBase;
    public EncodeEncryption encodeEncryption;
    public FileExtension fileExtension;
    public EncodeSize encodeSize;

    public Header(int headerBase, int encTypeLength, int encBaseLength, int encEncryptionLength, int fileExtensionCharLength, int encSizeLength)
    {
        int fileExtensionLength = fileExtensionCharLength * Transformations.baseNByteLength(headerBase);
        myHeaderBase = headerBase;
        encodeType = new EncodeType(0, encTypeLength);
        encodeBase = new EncodeBase(encodeType.position() + encodeType.length(), encBaseLength);
        encodeEncryption = new EncodeEncryption(encodeBase.position() + encodeBase.length(), encEncryptionLength);
        fileExtension = new FileExtension(encodeEncryption.position() + encodeEncryption.length(), fileExtensionLength);
        encodeSize = new EncodeSize(fileExtension.position() + fileExtension.length(), encSizeLength);
        myHeaderLength = encTypeLength + encBaseLength + encEncryptionLength + fileExtensionLength + encSizeLength;
        myHeaderData = new int[myHeaderLength];
    }

    public int length()
    {
        return myHeaderLength;
    }

    public int getHeaderBase()
    {
        return myHeaderBase;
    }

    public void setData(int[] data)
    {
        if(data.length != myHeaderLength)
        {
            System.err.println("Header.EncodeType.setData() - incorrect data length");
        }
        else
        {
            for(int i = 0; i < myHeaderLength; i++)
            {
                myHeaderData[i] = data[i];
            }
        }
    }

    public void setData(int pos, int data)
    {
        if(data >= myHeaderBase)
        {
            System.err.println("incorrect data");
        }
        myHeaderData[pos] = data;
    }

    public int[] getData()
    {
        return myHeaderData;
    }

    public void reset()
    {
        for(int i : myHeaderData)
        {
            myHeaderData[i] = 0;
        }
    }

    public class HeaderInfo
    {
        private int myPosition;
        private int myLength;

        public HeaderInfo(int position, int length)
        {
            myPosition = position;
            myLength = length;
        }

        public int length()
        {
            return myLength;
        }

        public int position()
        {
            return myPosition;
        }

        public void setData(int[] data)
        {
            if(data.length != this.myLength)
            {
                System.err.println(this.getClass() + "- incorrect data length");
            }
            else
            {
                for(int i = 0; i < this.myLength; i++)
                {
                    myHeaderData[this.myPosition + i] = data[i];
                }
            }
        }

        public int[] getData()
        {
            int[] data = new int[this.myLength];
            for(int i = 0; i < this.myLength; i++)
            {
                data[i] = myHeaderData[this.myPosition + i];
            }
            return data;
        }
    }

    public class EncodeType extends HeaderInfo
    {
        public EncodeType(int position, int length)
        {
            super(position, length);
        }

        public void setEncodeType(int encodeType)
        {
            int[] data = new int[super.myLength];

            data[0] = encodeType;

            super.setData(data);
        }

        public int getEncodeType()
        {
            int[] data = super.getData();
            return data[0];
        }
    }

    public class EncodeBase extends HeaderInfo
    {
        public EncodeBase(int position, int length)
        {
            super(position, length);
        }

        public void setEncodeBase(int base)
        {
            int[] data = Transformations.intBase10ToBaseN(myHeaderBase, super.myLength, base);

            super.setData(data);
        }

        public int getEncodeBase()
        {
            int base;
            base = Transformations.intBaseNToBase10(myHeaderBase, super.getData());

            if(base < 2 || (base > 16 && base != 256))
            {
                System.err.println("Incorrect encode base - " + base);
            }

            return base;
        }
    }

    public class EncodeEncryption extends HeaderInfo
    {
        public EncodeEncryption(int position, int length)
        {
            super(position, length);
        }

        public void setEncodeEncryption(boolean isEncrypted)
        {
            int[] data = new int[super.myLength];
            if(isEncrypted)
            {
                data[0] = 1;
            }
            else
            {
                data[0] = 0;
            }
            super.setData(data);
        }

        public boolean getEncodeEncryption()
        {
            int[] data = super.getData();
            if(data[0] == 1)
            {
                return true;
            }
            else if(data[0] == 0)
            {
                return false;
            }
            else
            {
                System.err.println("Encryption boolean invalid");
                return false;
            }
        }
    }

    public class FileExtension extends HeaderInfo
    {

        public FileExtension(int position, int length)
        {
            super(position, length);
        }

        public void setFileExtension(String fileName)
        {
            int[] data = new int[super.myLength];
            int numChars = super.myLength/Transformations.baseNByteLength(myHeaderBase);
            int[] tempData;
            char c;
            for(int i = 0; i < numChars; i++)
            {
                c = fileName.charAt(fileName.length() - numChars + i);
                if(c == '/')
                {
                    tempData = Transformations.byteToBaseN(myHeaderBase, (int)'a');
                }
                else
                {
                    tempData = Transformations.byteToBaseN(myHeaderBase, (int)c);
                }

                for(int j = 0; j < tempData.length; j++)
                {
                    data[i * tempData.length + j] = tempData[j];

                }
            }

            super.setData(data);
        }

        public String getFileExtension()
        {
            String fileExt = new String("");
            int charLength = Transformations.baseNByteLength(myHeaderBase);
            int[] data = super.getData();
            int[] tempData = new int[charLength];

            for(int i = 0; i < super.myLength/charLength; i++)
            {
                for(int j = 0; j < charLength; j++)
                {
                    tempData[j] = data[i * charLength + j];
                }
                fileExt += (char)Transformations.intBaseNToBase10(myHeaderBase, tempData);
            }


            return fileExt;
        }
    }

    public class EncodeSize extends HeaderInfo
    {
        public EncodeSize(int position, int length)
        {
            super(position, length);
        }

        public void setEncodeSize(int encSize)
        {
            super.setData(Transformations.intBase10ToBaseN(myHeaderBase, super.myLength, encSize));
        }

        public int getEncodeSize()
        {
            return Transformations.intBaseNToBase10(myHeaderBase, getData());
        }
    }
}