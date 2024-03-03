package com.github.matthewdawsey.collisionres;

import java.util.ArrayList;

/**
 * 
 * CollisionResolution.class
 * 
 * Axis-Aligned Bounding Box(AABB) collision detection and resolution
 * Using LWJGL 2.9.3 + slick-util libraries
 * 
 * @author Matthew Dawsey
 *
 */
public class CollisionResolution {
	
	public static final String displayTitle = "Collision Resolution Example";
	public static final int displayWidth = 1280, displayHeight = 720;
	public static final float fov = 100, aspectRatio = displayWidth / displayHeight, nearZPlane = 0.001f, farZPlane = 1000f;
	public static final float gravity = 0.01f;
	
	// list of all colliders in world
	public ArrayList<AABB> aabbList = new ArrayList<AABB>();
	
	// camera object
	public Camera camera = new Camera(this);
	
	public CollisionResolution() {
		// floor
		this.aabbList.add(new AABB(new Vec3(-5, -0.5f, -5),new Vec3(5, 0, 5)));
		//
		
		// stairs and bridge
		this.aabbList.add(new AABB(new Vec3(1, 0, 0),new Vec3(1.5f, 0.3f, 0.5f)));
		this.aabbList.add(new AABB(new Vec3(1.5f, 0, 0),new Vec3(2, 0.6f, 0.5f)));
		this.aabbList.add(new AABB(new Vec3(1.5f, 0, 0.5f),new Vec3(2, 0.9f, 1)));
		this.aabbList.add(new AABB(new Vec3(1.5f, 0, 1),new Vec3(2, 1.2f, 1.5f)));
		this.aabbList.add(new AABB(new Vec3(1, 0, 1),new Vec3(1.5f, 1.5f, 1.5f)));
		this.aabbList.add(new AABB(new Vec3(-1, 1.25f, 1.1f),new Vec3(1, 1.5f, 1.4f)));
		this.aabbList.add(new AABB(new Vec3(-1.5f, 0, 1),new Vec3(-1, 1.5f, 1.5f)));
		this.aabbList.add(new AABB(new Vec3(-2, 0, 1),new Vec3(-1.5f, 1.2f, 1.5f)));
		this.aabbList.add(new AABB(new Vec3(-2, 0, 0.5f),new Vec3(-1.5f, 0.9f, 1)));
		this.aabbList.add(new AABB(new Vec3(-2, 0, 0),new Vec3(-1.5f, 0.6f, 0.5f)));
		this.aabbList.add(new AABB(new Vec3(-1.5f, 0, 0),new Vec3(-1, 0.3f, 0.5f)));
		//
	}
	
	public void update() {
		this.camera.update();
	}
	
	public void render() {
		// allow GL to draw relative to camera perspective
		DisplayHandler.glPerspective3(this.camera.position, this.camera.rotation);
		
		// render camera collider
		DisplayHandler.renderAABB(this.camera.aabb);
		
		// render all colliders in world
		for (AABB aabb : this.aabbList) {
			DisplayHandler.renderAABB(aabb);
		}
		
		// render a quad on top of the floor for a better sense of direction lol
		DisplayHandler.renderPlane(new Vec3(0, 0, 0), 10, 10);
	}
	
	public static void main(String[] args) {
		DisplayHandler.createDisplay();
		CollisionResolution cr = new CollisionResolution();
		
		while (!DisplayHandler.isCloseRequested()) {
			cr.update();
			DisplayHandler.initGL3();
			DisplayHandler.clearGL();
			cr.render();
			DisplayHandler.updateDisplay();
		}
		
		DisplayHandler.destroyDisplay();
	}
	
}
