

package com.PlaylistPanic.Video;

public class Segment {
	public Vec vStart = new Vec();
	public Vec vDirection = new Vec();
	
	public float t1;
	public float t2;
	
	private boolean findT_Result = false;
	
	private float findT(Segment S, Vec V) {
		float t = 0.0f;
		float epsilon = 0.001f;
		
		findT_Result = false;
		
		if(Math.abs(S.vDirection.v[0]) > Math.abs(S.vDirection.v[1])) {
			
			t = (V.v[0] - S.vStart.v[0]) / S.vDirection.v[0];
			
			if(Math.abs(V.v[1] - (S.vStart.v[1] + t * S.vDirection.v[1])) > epsilon) {
				findT_Result = true;
			}
		
		} else {
			
			t = (V.v[1] - S.vStart.v[1]) / S.vDirection.v[1];
			
			if(Math.abs(V.v[0] - (S.vStart.v[0] + t * S.vDirection.v[0])) > epsilon) {
				findT_Result = true;
			}
			
		}
		
		return t;
	}
	
	private Vec intersectParallel(Segment S2) {
		Vec ReturnResult = new Vec();
		Vec V = new Vec();
		float t;
		
		// If the lines dont overlap return NULL
		// else find t2 for t1 = 0
		// if t2 in [0,1] return t2, t1 = 0
		// else find t1 for t2 ==0 and t2 == 1
		// if t1 < 0 return NULL (no intersection)
		// else return the smaller t1 and the corresponding t2
		
		// if the lines dont overlap return NULL
		// else find t2 for t1 == 0
		
		V.copy(vStart);
		t2 = findT(S2, V);
		if(findT_Result) {
			// vector is not collinear
			return null;
		}
		
		// if t2 in [0,1] return t2, t1 = 0
		if(t2 >= 0.0f && t2 <= 1.0f) {
			ReturnResult.copy(vStart);
			t1 = 0;
			return ReturnResult;
		}
		
		// else find t1 for t2 == 0 and t2 == 1
		V.copy(S2.vStart);
		t1 = findT(this, V);
		if(findT_Result) {
			return null;
		}
		
		// if t1 < 0 return NULL (no intersection)
		if(t1 < 0.0f) {
			return null;
		}
		
		V = S2.vStart.add(S2.vDirection);
		
		t = findT(this,V);
		if(findT_Result) {
			return null;
		}
		
		if(t1 > 1.0f && t > 1.0f) {
			return null;
		}
		
		if(t < t1) {
			t1 = t;
			t2 = 1.0f;
			ReturnResult.copy(V);
		} else {
			t2 = 0.0f;
			ReturnResult.copy(S2.vStart);
		}
			
		return ReturnResult;
	}
	
	private Vec intersectNonParallel(Segment S2) {
		Vec ReturnResult = new Vec();
		Vec v1 = new Vec();
		Vec v2 = new Vec();
		Vec tmp1 = new Vec();
		Vec tmp2 = new Vec();
		Vec vIntersection = new Vec();
		
		// compute the homogenous line coordinates
		tmp1.v[0] = vStart.v[0];
		tmp1.v[1] = vStart.v[1];
		tmp1.v[2] = 1.0f;
		
		tmp2.v[0] = vStart.v[0] + vDirection.v[0];
		tmp2.v[1] = vStart.v[1] + vDirection.v[1];
		tmp2.v[2] = 1.0f;
		
		v1 = tmp1.cross(tmp2);
		
		tmp1.v[0] = S2.vStart.v[0];
		tmp1.v[1] = S2.vStart.v[1];
		tmp1.v[2] = 1.0f;
		
		tmp2.v[0] = S2.vStart.v[0] + S2.vDirection.v[0];
		tmp2.v[1] = S2.vStart.v[1] + S2.vDirection.v[1];
		tmp2.v[2] = 1.0f;
		
		v2 = tmp1.cross(tmp2);
		
		// compute the intersection in homogenous coordinates and project back to 2d
		vIntersection = v1.cross(v2);
		ReturnResult.v[0] = vIntersection.v[0] / vIntersection.v[2];
		ReturnResult.v[1] = vIntersection.v[1] / vIntersection.v[2];
		
		// compute t1, t2
		if(Math.abs(vDirection.v[0]) > Math.abs(vDirection.v[1])) {
			t1 = (ReturnResult.v[0] - vStart.v[0]) / vDirection.v[0];
		} else {
			t1 = (ReturnResult.v[1] - vStart.v[1]) / vDirection.v[1];
		}
		
		if(Math.abs(S2.vDirection.v[0]) > Math.abs(S2.vDirection.v[1])) {
			t2 = (ReturnResult.v[0] - S2.vStart.v[0]) / S2.vDirection.v[0];
		} else {
			t2 = (ReturnResult.v[1] - S2.vStart.v[1]) / S2.vDirection.v[1];
		}
		
		return ReturnResult;
	}
	
	public Vec intersect(Segment S2) {
		Vec ReturnResult; // = new Vec();
		
		if(Math.abs(vDirection.dot(S2.vDirection.ortho())) < 0.1) {
			// Vector lines are parallel
			ReturnResult = intersectParallel(S2);
			if(ReturnResult == null) {
				t1 = 0.0f;
				t2 = 0.0f;
			}
		} else {
			// Non parallel
			ReturnResult = intersectNonParallel(S2);
		}
		
		return ReturnResult;
	}
	
	public float length() {
		return vDirection.length();
	}
	
}
