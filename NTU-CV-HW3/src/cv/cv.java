package cv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class cv {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		 String fileName = "./assets/lena.im";
		 int headerLength = 172;
		 int imageWidth = 512;
		 int imageHeight = 512;
		 
		 ArrayList<Integer> bytes = GetByteData(fileName);
		
		 HistogramEqualizationHelper helper = 
				 new HistogramEqualizationHelper(bytes,headerLength,imageWidth,imageHeight);
		 
		 //calculating new image
		 for(int i = 0;i < imageWidth*imageHeight ;i++)
		 {
			 int origin = bytes.get(headerLength+i);
			 int after = helper.PixelValueMapping(origin);
			 bytes.set(headerLength+i, after);
		 }
		 
		 WriteOut(bytes,"./assets/result.im");
		 
		 //origin histogram
		 Hashtable<Integer,Integer> originHistogram = helper.getOriginHistogram();
		 WriteOutHistogram(originHistogram,"./assets/hw3-histogram-origin.csv");
		 
		 Hashtable<Integer,Integer> newHistogram = helper.CalculateHistogram(bytes, headerLength, imageWidth, imageHeight);
		 WriteOutHistogram(originHistogram,"./assets/hw3-histogram-after.csv");
		 
		 
	}
	
	public static void WriteOutHistogram(Hashtable<Integer,Integer> histogram,String path) throws IOException
	{
		 File f = new File(path);
		 if(f.exists())f.delete();
		 FileWriter writer = new FileWriter(f);
		 
		 writer.write("value, count\n");
		 for(int i = 0 ;i<256;i++)
		 {
			 int value = i;
			 int data = histogram.get(i);
			 
			 writer.write(value+","+data+"\n");
		 }
		 writer.close();
	}
	
	public static ArrayList<Integer> GetByteData(String fileName) throws IOException
	{
		 File f = new File(fileName);
		 ArrayList<Integer> bytes = new ArrayList<Integer>();
		
		 System.out.println("file exist:"+f.exists());
		
		 FileInputStream in = null;	
		 in = new FileInputStream(fileName);
		 
		 int c;
		 while ((c = in.read()) != -1) {
			 bytes.add(c);
        }
		 
		 return bytes;
	}
	
	public static void WriteOut(ArrayList<Integer> data,String name) throws IOException
	{
		File f = new File(name);
		if(f.exists())f.delete();
		FileOutputStream out = null;
		out = new FileOutputStream(name);
		
		for(int i : data)
		{
			out.write((byte)i);
		}
		
		out.flush();
		out.close();
		
	}

}
