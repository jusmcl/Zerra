/**
 * 
 */
/**
 * @author Arpaesis
 *
 */
module com.zerra
{
	exports com.zerra.common.world.tile;
	exports com.zerra.common.world.player;
	exports com.zerra.common.event.entity;
	exports com.zerra.common.recipe;
	exports com.zerra.api.mod;
	exports com.zerra.common.event;
	exports com.zerra.common.network;
	exports com.zerra.client.state;
	exports com.zerra.common.world.item.impl;
	exports com.zerra.client.gfx.texture.cubemap;
	exports com.zerra.client.gfx.renderer.tile;
	exports com.zerra.client.gfx.texture;
	exports com.zerra.client.util;
	exports com.zerra.common.util;
	exports com.zerra.common.world.storage.plate;
	exports com.zerra.common;
	exports com.zerra.common.world;
	exports com.zerra.client.presence;
	exports com.zerra.api.mod.info;
	exports com.zerra.common.registry;
	exports com.zerra.server;
	exports com.zerra.client.gfx.renderer;
	exports com.zerra.common.world.entity;
	exports com.zerra.common.world.storage.sdf;
	exports com.zerra.common.world.item;
	exports com.zerra.client;
	exports com.zerra.client.gfx;
	exports com.zerra.api.mod.example;
	exports com.zerra.client.input.gamepad;
	exports com.zerra;
	exports com.zerra.common.world.item.tool;
	exports com.zerra.client.view;
	exports com.zerra.common.world.storage;
	exports com.zerra.client.input;
	exports com.zerra.common.world.entity.attrib;
	exports com.zerra.client.gfx.model;
	exports com.zerra.client.gfx.shader;
	exports com.zerra.client.gfx.texture.map;
	exports com.zerra.common.world.entity.facing;
	exports com.zerra.common.world.tile.impl;

	requires com.google.common;
	requires combo.sdk;
	requires commons.io;
	requires gson;
	requires io.github.classgraph;
	requires java.desktop;
	requires java.discord.rpc;
	requires java.management;
	requires jna;
	requires jsr305;
	requires org.apache.commons.lang3;
	requires org.apache.logging.log4j;
	requires org.joml;
	requires org.lwjgl;
	requires org.lwjgl.glfw;
	requires org.lwjgl.openal;
	requires org.lwjgl.opengl;
}