package com.github.matthewdawsey.collisionres;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * 
 * Camera.class
 * 
 * @author Matthew Dawsey
 *
 */
public class Camera {
	
	// collider dimensions
	public final float width = 0.25f, height = 1f, length = 0.25f;
	// movement scaling
	public final float lookSensitivity = 0.1f, moveSpeed = 0.03f;
	
	public AABB aabb;
	public CollisionResolution cr;
	public Vec3 position, rotation, velocity;
	
	public boolean onGround;
	
	public Camera(CollisionResolution cr) {
		this.cr = cr;
		this.position = new Vec3(0, 1, 0);
		this.rotation = new Vec3(0, 180, 0);
		this.velocity = new Vec3();
		this.aabb = new AABB(
				new Vec3(this.width / -2, this.height / -2, this.length / -2),
				new Vec3(this.width / 2, this.height / 2, this.length / 2));
	}
	
	public void update() {
		this.input();
		this.physics();
	}
	
	// collision checking and resolution are handled here
	private void physics() {
		// horizontal velocity damping
		this.velocity.x /= 2;
		this.velocity.z /= 2;
		
		// apply gravity if not on top of a collider
		if (!this.onGround)
			this.velocity.y -= CollisionResolution.gravity;
		
		// apply Y axis velocity
		this.position.y += this.velocity.y;
		this.aabb.updatePosition(this.position.clone());
		
		// separate colliders with a height value of 0.01
		// on the top and bottom to detect Y axis collisions
		AABB floorCheckAABB = new AABB(
				new Vec3(this.width / -2, -0.01f, this.length / -2),
				new Vec3(this.width / 2, 0, this.length / 2));
		
		AABB ceilingCheckAABB = new AABB(
				new Vec3(this.width / -2, 0, this.length / -2),
				new Vec3(this.width / 2, 0.01f, this.length / 2));
		
		floorCheckAABB.updatePosition(this.position.clone());
		floorCheckAABB.offset(0, this.height / -2, 0);
		ceilingCheckAABB.updatePosition(this.position.clone());
		ceilingCheckAABB.offset(0, this.height / 2, 0);
		//
		
		// assume falling if not standing on a collider
		this.onGround = false;
		
		// get the list of colliders
		ArrayList<AABB> aabbList = this.cr.aabbList;
		
		// check for Y axis collisions
		for (AABB aabb : aabbList) {
			if (floorCheckAABB.intersects(aabb)) {
				float dy = this.aabb.center.y - aabb.center.y;
				if (dy > 0) { // colliding with +Y face (standing on floor)
					this.onGround = true;
					this.position.y = aabb.end.y + this.height / 2;
					this.velocity.y = 0;
				}
			}
			
			if (ceilingCheckAABB.intersects(aabb)) {
				float dy = this.aabb.center.y - aabb.center.y;
				if (dy < 0) { // colliding with -Y face (bump head on ceiling)
					this.onGround = false;
					this.position.y = aabb.start.y - this.height / 2;
					this.velocity.y = 0;
				}
			}
		}
		
		// apply X axis velocity
		this.position.x += this.velocity.x;
		this.aabb.updatePosition(this.position);
		
		// check for X axis collisions
		for (AABB aabb : aabbList) {
			if (this.aabb.intersects(aabb)) {
				float dx = this.aabb.center.x - aabb.center.x;
				if (dx > 0) { // colliding with +X face
					this.position.x = aabb.end.x + this.width / 2;
				} else if (dx < 0) { // colliding with -X face
					this.position.x = aabb.start.x - this.width / 2;
				}
				
				this.velocity.x = 0;
			}
		}
		
		// apply Z axis velocity
		this.position.z += this.velocity.z;
		this.aabb.updatePosition(this.position);
		
		// check for Z axis collisions
		for (AABB aabb : aabbList) {
			if (this.aabb.intersects(aabb)) {
				float dz = this.aabb.center.z - aabb.center.z;
				if (dz > 0) { // colliding with +Z face
					this.position.z = aabb.end.z + this.length / 2;
				} else if (dz < 0) { // colliding with -Z face
					this.position.z = aabb.start.z - this.length / 2;
				}
				
				this.velocity.z = 0;
			}
		}
		
		// one final realignment so collider doesnt lag behind when being drawn
		this.aabb.updatePosition(this.position);
	}
	
	// handle keyboard/mouse input
	private void input() {
		// mouse
		if (Mouse.isButtonDown(0))
			Mouse.setGrabbed(true);
		if (Mouse.isButtonDown(1))
			Mouse.setGrabbed(false);
		
		if (Mouse.isGrabbed()) {
			float mouseDx = Mouse.getDX(), mouseDy = -Mouse.getDY();
			this.rotation.x += this.lookSensitivity * mouseDy;
			this.rotation.y += this.lookSensitivity * mouseDx;
			if (this.rotation.x < -90)
				this.rotation.x = 90;
			else if (this.rotation.x > 90)
				this.rotation.x = 90;
			
			if (this.rotation.y < 0)
				this.rotation.y += 360;
			else if (this.rotation.y > 359)
				this.rotation.y -= 360;
		}
		//
		
		// keyboard
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.velocity.x -= this.moveSpeed * (float) Math.cos(Math.toRadians(this.rotation.y + 90));
			this.velocity.z -= this.moveSpeed * (float) Math.sin(Math.toRadians(this.rotation.y + 90));
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.velocity.x += this.moveSpeed * (float) Math.cos(Math.toRadians(this.rotation.y + 90));
			this.velocity.z += this.moveSpeed * (float) Math.sin(Math.toRadians(this.rotation.y + 90));
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.velocity.x -= this.moveSpeed * (float) Math.cos(Math.toRadians(this.rotation.y));
			this.velocity.z -= this.moveSpeed * (float) Math.sin(Math.toRadians(this.rotation.y));
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.velocity.x += this.moveSpeed * (float) Math.cos(Math.toRadians(this.rotation.y));
			this.velocity.z += this.moveSpeed * (float) Math.sin(Math.toRadians(this.rotation.y));
		}
		
		// only allow jumping if standing on floor
		if (this.onGround && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			this.velocity.y = 0.1f;
		}
		//
	}
}
