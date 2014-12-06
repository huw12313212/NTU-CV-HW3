package cv;

import java.util.ArrayList;
import java.util.Hashtable;

public class HistogramEqualizationHelper {
	
	private final ArrayList<Integer> originData;
	private final Hashtable<Integer,Integer> originHistogram;
	private final ArrayList<Integer> originCDF;
	
	public final int MinOfCDF;
	public final int MaxOfCDF;
	
	public final int headerLength;
	public final int imageWidth;
	public final int imageHeight;
	
	public Hashtable<Integer,Integer>getOriginHistogram()
	{
		return originHistogram;
	}
	
	public HistogramEqualizationHelper(ArrayList<Integer> bytes, int header, int width, int height)
	{
		originData = (ArrayList<Integer>)bytes.clone();
		originHistogram = this.CalculateHistogram(bytes,header,width,height);
		headerLength = header;
		imageWidth = width;
		imageHeight = height;
		originCDF = this.CalculateCDF(originHistogram);
		MinOfCDF = this.GetMinCDF(originHistogram);
		MaxOfCDF = this.GetMaxCDF(originHistogram);
		
		//debug
		for(int i = 0; i < 256 ; i ++)
		{
			System.out.println("CDF ["+i+"] =" + originCDF.get(i));
		}
		System.out.println("Min:"+MinOfCDF+ " Max:"+MaxOfCDF);
	}
	
	public int PixelValueMapping(int originValue)
	{
		int up = originCDF.get(originValue) - originCDF.get(MinOfCDF);
		int bottom = (imageWidth*imageHeight) -originCDF.get(MinOfCDF);
		float ratio = (float)up/(float)bottom;
		int result = Math.round(ratio*255);
		
		return result; 
	}
	
	public ArrayList<Integer> CalculateCDF(Hashtable<Integer,Integer> histogram)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		int sum = 0;
		for(int i=0 ; i<256; i++)
		{	
			sum += histogram.get(i);
			result.add(sum);
		}
		
		return result; 
	}
	
	public int GetMinCDF(Hashtable<Integer,Integer> histogram)
	{
		for(int i = 0; i<256;i++)
		{
			if(histogram.get(i)>0)return i;
		}
		
		return 0;
	}
	
	public int GetMaxCDF(Hashtable<Integer,Integer> histogram)
	{
		for(int i = 255; i>= 0;i--)
		{
			if(histogram.get(i)>0)return i;
		}
		
		return 0;
	}
	
	public Hashtable<Integer,Integer> CalculateHistogram(ArrayList<Integer> pixelData,int header,int width,int height)
	{
		Hashtable<Integer,Integer> result = new Hashtable<Integer,Integer>();
		
		 for(int i =0;i<256;i++)
		 {
			 result.put(i, 0);
		 }
		 
		 for(int y = 0 ; y<height; y++)
		 {
			 for(int x = 0; x <width;x++)
			 {
				 int data = pixelData.get(header+(y*width+x)*1);
				 int oldValue = result.get(data);
				 int newValue = oldValue + 1;
				 result.put(data, newValue);
			 }
		 }
		 return result;
	}

}
