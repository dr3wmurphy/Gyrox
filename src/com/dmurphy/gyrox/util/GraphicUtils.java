

package com.dmurphy.gyrox.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GraphicUtils {

	public class vec2 {
		public float v[] = new float[2];
	}
	
	public class vec3 {
		public float v[] = new float[3];
	}
	
	public class vec4 {
		public float v[] = new float[4];
	}

	
	public static vec2 vec2Add(vec2 Result, vec2 v1, vec2 v2) {
		Result.v[0] = v1.v[0] + v2.v[0];
		Result.v[1] = v1.v[1] + v2.v[1];
		
		return Result;
	}
	
	public static FloatBuffer convToFloatBuffer(float buf[]) {
		FloatBuffer ReturnBuffer;
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(buf.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		ReturnBuffer = vbb.asFloatBuffer();
		ReturnBuffer.put(buf);
		ReturnBuffer.position(0);
		
		return ReturnBuffer;
	}
	
	public static ByteBuffer convToByteBuffer(byte buf[]) {
		ByteBuffer ReturnBuffer = ByteBuffer.allocateDirect(buf.length);
		
		ReturnBuffer.order(ByteOrder.nativeOrder());
		ReturnBuffer.put(buf);
		ReturnBuffer.position(0);
		
		return ReturnBuffer;
	}
	
	public static ShortBuffer convToShortBuffer(short buf[]) {
		ShortBuffer ReturnBuffer = ShortBuffer.allocate(buf.length);
		ReturnBuffer.put(buf);
		ReturnBuffer.position(0);
		return ReturnBuffer;
	}
	
}
