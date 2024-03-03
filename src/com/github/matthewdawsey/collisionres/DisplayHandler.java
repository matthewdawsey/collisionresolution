package com.github.matthewdawsey.collisionres;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * 
 * DisplayHandler.class
 * 
 * Handles everything to do with lwjgl/opengl
 * 
 * @author Matthew Dawsey
 *
 */
public class DisplayHandler {
	// renders a wireframe with aabb dimensions
	public static void renderAABB(AABB aabb) {
		float wb2 = aabb.width / 2, hb2 = aabb.height / 2, lb2 = aabb.length / 2;
		GL11.glColor3f(1, 1, 1);
		GL11.glTranslatef(aabb.center.x, aabb.center.y, aabb.center.z);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3f(-wb2, -hb2, -lb2);
		GL11.glVertex3f(-wb2, -hb2, lb2);
		GL11.glVertex3f(wb2, -hb2, lb2);
		GL11.glVertex3f(wb2, -hb2, -lb2);
		GL11.glVertex3f(-wb2, -hb2, -lb2);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(-wb2, -hb2, -lb2);
		GL11.glVertex3f(-wb2, hb2, -lb2);
		GL11.glVertex3f(wb2, -hb2, -lb2);
		GL11.glVertex3f(wb2, hb2, -lb2);
		GL11.glVertex3f(-wb2, -hb2, lb2);
		GL11.glVertex3f(-wb2, hb2, lb2);
		GL11.glVertex3f(wb2, -hb2, lb2);
		GL11.glVertex3f(wb2, hb2, lb2);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3f(-wb2, hb2, -lb2);
		GL11.glVertex3f(-wb2, hb2, lb2);
		GL11.glVertex3f(wb2, hb2, lb2);
		GL11.glVertex3f(wb2, hb2, -lb2);
		GL11.glVertex3f(-wb2, hb2, -lb2);
		GL11.glEnd();
		GL11.glTranslatef(-aabb.center.x, -aabb.center.y, -aabb.center.z);
	}
	
	// for the floor
	public static void renderPlane(Vec3 position, float width, float length) {
		GL11.glColor3f(0.1f, 0.1f, 0.1f);
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(width / -2, 0, length / -2);
		GL11.glVertex3f(width / -2, 0, length / 2);
		GL11.glVertex3f(width / 2, 0, length / 2);
		GL11.glVertex3f(width / 2, 0, length / -2);
		GL11.glEnd();
		GL11.glTranslatef(-position.x, -position.y, -position.z);
		GL11.glColor3f(1, 1, 1);
	}
	
	public static void glPerspective3(Vec3 position, Vec3 rotation) {
		GL11.glRotatef(rotation.x, 1, 0, 0);
		GL11.glRotatef(rotation.y, 0, 1, 0);
		GL11.glRotatef(rotation.z, 0, 0, 1);
		GL11.glTranslatef(-position.x, -position.y, -position.z);
	}
	
	// draw 3d
	public static void initGL3() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(CollisionResolution.fov, CollisionResolution.aspectRatio, CollisionResolution.nearZPlane, CollisionResolution.farZPlane);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	// draw 2d
	public static void initGL2() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, CollisionResolution.displayWidth, CollisionResolution.displayHeight, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}
	
	// clear buffers
	public static void clearGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
	}
	
	// init LWJGL display
	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(CollisionResolution.displayWidth, CollisionResolution.displayHeight));
			Display.setTitle(CollisionResolution.displayTitle);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	// update LWJGL display
	public static void updateDisplay() {
		Display.update();
		Display.sync(30);
	}
	
	// destroy LWJGL display
	public static void destroyDisplay() {
		Display.destroy();
	}
	
	// returns true when display exit button pressed
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
}
