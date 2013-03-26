

package com.PlaylistPanic.Video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class Material {

	// members
	ArrayList<MaterialData> materials = new ArrayList<MaterialData>();
	
	String Debug;
	StringBuffer sb = new StringBuffer(40);
	
	public enum ColourType {
		E_AMBIENT,
		E_DIFFUSE,
		E_SPECULAR
	}
	
	private class MaterialData {
		//members
		public float[] ambient =  { 0.2f, 0.2f, 0.2f, 1.0f };
		public float[] diffuse =  { 1.0f, 1.0f, 0.0f, 1.0f };
		public float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f };
		public float shininess = 2.0f;
		public float[] emission = {0.0f, 0.0f, 0.0f, 1.0f};
		public String name;
		//public String map_diffuse;
	}
	
	public Material(Context ctx, int resId) {
		Debug = sb.append("Start Add Material, ").append(resId).toString();
		Log.e("Material",Debug);
		addMaterial(ctx,resId);
	}
	
    public void addMaterial(Context ctx, int resId) {
		// Load the file and save the resources
		String buf;
		String[] temp;
		int iMaterial;
		MaterialData mData;

		InputStream inputStream = ctx.getResources().openRawResource(resId);
		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		
		iMaterial = -1;
		
		try {
			while((buf = buffreader.readLine()) != null) {
				if(buf != null && (buf.length() > 1)) {
					temp = buf.split(" ");
					switch(buf.charAt(0)) {
						case '#':
							break;
						case 'n':
							iMaterial++;
							materials.add(new MaterialData());
							mData = materials.get(iMaterial);
							mData.name = temp[1];
							break;
	     				default:
	     					mData = materials.get(iMaterial);
	     					if(temp[0].contains("Ka")) {
	     						mData.ambient[0] = Float.valueOf(temp[1].trim()).floatValue();
	     						mData.ambient[1] = Float.valueOf(temp[2].trim()).floatValue();
	     						mData.ambient[2] = Float.valueOf(temp[3].trim()).floatValue();
	     					} else if(temp[0].contains("Kd")) {
	     						mData.diffuse[0] = Float.valueOf(temp[1].trim()).floatValue();
	     						mData.diffuse[1] = Float.valueOf(temp[2].trim()).floatValue();
	     						mData.diffuse[2] = Float.valueOf(temp[3].trim()).floatValue();
	     					} else if(temp[0].contains("Ks")) {
	     						mData.specular[0] = Float.valueOf(temp[1].trim()).floatValue();
	     						mData.specular[1] = Float.valueOf(temp[2].trim()).floatValue();
	     						mData.specular[2] = Float.valueOf(temp[3].trim()).floatValue();
	     					} else if(temp[0].contains("Ns")) {
	     						mData.shininess = Float.valueOf(temp[1].trim()).floatValue();
	     					}
							break;
					}
				}
			}
		}
		catch (IOException e) 
		{
			Log.e("Material","Materail Read File Exception");
		}
	}
	
	
	public int getIndex(String sname) {
		int retindex = -1;
		MaterialData[] mData = new MaterialData[materials.size()];
		mData = (MaterialData[])materials.toArray(mData);
		int numOfElements = mData.length;
		
		for(int x=0; x < numOfElements; x++) {
			if(mData[x].name.equals(sname)) {
				retindex = x;
			}
		}
		
		return retindex;
	}
	
	public int getNumber() {
		MaterialData[] mData = new MaterialData[materials.size()]; 
		mData =	(MaterialData[])materials.toArray(mData);
		return mData.length;
	}
	
	public FloatBuffer getAmbient(int mindex) {
		FloatBuffer Ambient;
		MaterialData mData = (MaterialData)(materials.get(mindex));
		ByteBuffer bb = ByteBuffer.allocateDirect(4 * 4);
		bb.order(ByteOrder.nativeOrder());
		Ambient = bb.asFloatBuffer();
		Ambient.put(mData.ambient);
		Ambient.position(0);
		
		return Ambient;
	}
	
	public FloatBuffer getDiffuse(int mindex) {
		FloatBuffer Diffuse;
		MaterialData mData = (MaterialData)(materials.get(mindex));
		ByteBuffer bb = ByteBuffer.allocateDirect(4 * 4);
		bb.order(ByteOrder.nativeOrder());
		Diffuse = bb.asFloatBuffer();
		Diffuse.put(mData.diffuse);
		Diffuse.position(0);
		
		return Diffuse; 
	}
	
	public FloatBuffer getSpecular(int mindex) {
		FloatBuffer Specular;
		MaterialData mData = (MaterialData)(materials.get(mindex));
		ByteBuffer bb = ByteBuffer.allocateDirect(4 * 4);
		bb.order(ByteOrder.nativeOrder());
		Specular = bb.asFloatBuffer();
		Specular.put(mData.specular);
		Specular.position(0);

		return Specular;
	}
	
	public float getShininess(int mindex) {
		MaterialData mData = (materials.get(mindex));
		return mData.shininess;
	}
	
	public void setMaterialColour(String name, ColourType Element, float[] colour) {
		for(int i=0;i<materials.size();i++) {
			MaterialData mData = (MaterialData)(materials.get(i));
			if(name.compareTo(mData.name) == 0) {
				for(int j=0;j<4;j++){
					switch(Element) {
						case E_AMBIENT:
							mData.ambient[j] = colour[j];
							break;
						case E_DIFFUSE:
							mData.diffuse[j] = colour[j];
							break;
						case E_SPECULAR:
							mData.specular[j] = colour[j];
							break;
					}
				}
			}
		}
	}
	
	public FloatBuffer getEmission(int mindex) {
		FloatBuffer emission;
		MaterialData mData = (MaterialData)(materials.get(mindex));
		ByteBuffer bb = ByteBuffer.allocateDirect(4 * 4);
		bb.order(ByteOrder.nativeOrder());
		emission = bb.asFloatBuffer();
		emission.put(mData.emission);
		emission.position(0);

		return emission;
	}
	
	public void setEmission(String name, float[] v) {
		for(int i=0;i<materials.size();i++) {
			MaterialData mData = (MaterialData)(materials.get(i));
			if(name.compareTo(mData.name) == 0) {
				for(int j=0;j<4;j++){
					mData.emission[j] = v[j];
				}
			}
		}
	}
	
}
