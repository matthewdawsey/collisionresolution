package com.github.matthewdawsey.collisionres;

/**
 * 
 * Vec3.class
 * 
 * @author Matthew Dawsey
 *
 */
public class Vec3 {
	public float x, y, z;
	public Vec3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 clone() {
		return new Vec3(this.x, this.y, this.z);
	}
}
