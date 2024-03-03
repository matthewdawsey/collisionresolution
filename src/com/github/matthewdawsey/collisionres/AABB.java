package com.github.matthewdawsey.collisionres;

/**
 * 
 * AABB.class
 * 
 * Defines a rectangular prism aligned with XYZ axes for checking collision
 * 
 * @author Matthew Dawsey
 *
 */
public class AABB {
	
	// start is bottom/left/back
	// end is top/right/front
	public Vec3 start, end, center;
	public float width, height, length;
	
	public AABB(Vec3 start, Vec3 end) {
		this.start = start;
		this.end = end;
		this.center = new Vec3();
		this.width = this.end.x - this.start.x;
		this.height = this.end.y - this.start.y;
		this.length = this.end.z - this.start.z;
		this.center.x = this.start.x + this.width / 2;
		this.center.y = this.start.y + this.height / 2;
		this.center.z = this.start.z + this.length / 2;
	}
	
	// checks for intersection
	public boolean intersects(AABB aabb) {
		if (this.start.x < aabb.end.x && this.start.y < aabb.end.y && this.start.z < aabb.end.z &&
				this.end.x > aabb.start.x && this.end.y > aabb.start.y && this.end.z > aabb.start.z)
			return true;
		
		return false;
	}
	
	// relocates aabb based on center position
	public void updatePosition(Vec3 position) {
		this.center.x = position.x;
		this.center.y = position.y;
		this.center.z = position.z;
		this.start.x = this.center.x - this.width / 2;
		this.start.y = this.center.y - this.height / 2;
		this.start.z = this.center.z - this.length / 2;
		this.end.x = this.center.x + this.width / 2;
		this.end.y = this.center.y + this.height / 2;
		this.end.z = this.center.z + this.length / 2;
	}
	
	// moves aabb by some amount
	public void offset(float dx, float dy, float dz) {
		this.start.x += dx;
		this.start.y += dy;
		this.start.z += dz;
		this.end.x += dx;
		this.end.y += dy;
		this.end.z += dz;
		this.center.x = this.start.x + this.width / 2;
		this.center.y = this.start.y + this.height / 2;
		this.center.z = this.start.z + this.length / 2;
		this.width = this.end.x - this.start.x;
		this.height = this.end.y - this.start.y;
		this.length = this.end.z - this.start.z;
	}
	
}
