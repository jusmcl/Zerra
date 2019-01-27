package com.zerra.common.world.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.zerra.common.event.entity.EntityEvent;
import com.zerra.common.event.entity.EntityUpdateEvent;
import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.attrib.Attribute;
import com.zerra.common.world.entity.attrib.AttributeFactory;
import com.zerra.common.world.entity.attrib.AttributeType;
import com.zerra.common.world.entity.attrib.RangeAttribute;
import com.zerra.common.world.entity.facing.Direction;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.Storable;
import com.zerra.common.world.storage.plate.WorldLayer;

public abstract class Entity implements Storable
{
	private Map<String, Attribute<?>> attributes;

	private World world;
	private String registryName;
	private UUID uuid;

	private int ticksExisted = 0;

	private int layer;
	private Vector3f entityPosition = new Vector3f();
	private Vector2i tilePosition = new Vector2i();
	private Vector3f velocity = new Vector3f();

	private Vector2f entitySize = new Vector2f();

	private boolean isInvisible;

	// Used in determining starting point of collision box sizing.
	private float anchorPoint = 0f;

	private Direction direction;

	public Entity(World world)
	{
		this.attributes = new HashMap<String, Attribute<?>>();

		// Set world.
		this.world = world;

		// Set layer
		this.layer = 0;

		// Set to origin.
		this.entityPosition.set(0f, 0f, 0f);
		this.tilePosition.set(0, 0);

		// Set direction.
		this.direction = Direction.SOUTH;

		// Set velocity.
		this.velocity.set(0F, 0F, 0F);

		this.init();
	}

	/**
	 * Gets the World
	 *
	 * @return World
	 */
	public World getWorld()
	{
		return world;
	}

	/**
	 * Gets the layer this entity is in from the world
	 *
	 * @return Layer for this entity
	 */
	@Nullable
	public Layer getLayer()
	{
		return getWorld().getLayer(getLayerId());
	}

	/**
	 * Sets the registry name for this entity
	 *
	 * @param name
	 *            The registry name
	 */
	public void setRegistryName(String name)
	{
		registryName = name;
	}

	/**
	 * Gets the registry name for this entity
	 *
	 * @return The registry name
	 */
	public String getRegistryName()
	{
		return registryName;
	}

	/**
	 * Gets the unique ID for this entity Will be null if the entity has not been spawned yet
	 *
	 * @return Unique ID
	 */
	public UUID getUUID()
	{
		return uuid;
	}

	/**
	 * Updates the entity per tick.
	 */
	public void update()
	{
		EntityUpdateEvent updateEvent = EntityEvent.onEntityUpdate(this);
		updateEvent.call();
		if (updateEvent.isCancelled())
			return;
		this.ticksExisted++;
	}

	/**
	 * Called during entity construction to add entity attributes to the map. Use {@link AttributeFactory} to create new attributes.
	 * 
	 * @param attributes
	 *            The attributes map
	 */
	protected void setEntityAttributes(Map<String, Attribute<?>> attributes)
	{
	}

	/**
	 * Called during entity construction.
	 */
	protected void init()
	{
	}

	/**
	 * Gets the facing of this entity
	 *
	 * @return Entity facing
	 */
	public Direction getFacingDirection()
	{
		return this.direction;
	}

	/**
	 * Sets the facing of this entity
	 *
	 * @param direction
	 *            Entity facing
	 */
	public void setFacingDirection(Direction direction)
	{
		this.direction = direction;
	}

	public void setPosition(Vector3fc position)
	{
		this.entityPosition.set(position);
		this.tilePosition.set((int) Math.floor(position.x()), (int) Math.floor(position.z()));
	}

	/**
	 * Gets this entity's tile position X component
	 *
	 * @return Tile position X component
	 */
	public int getXTilePos()
	{
		return this.tilePosition.x();
	}

	/**
	 * Gets this entity's tile position Y component
	 *
	 * @return Tile position Y component
	 */
	public int getYTilePos()
	{
		return this.tilePosition.y();
	}

	/**
	 * Gets this entity's actual position X component
	 *
	 * @return Actual position X component
	 */
	public float getXEntityPos()
	{
		return this.entityPosition.x();
	}

	/**
	 * Gets this entity's actual position Y component
	 *
	 * @return Actual position Y component
	 */
	public float getYEntityPos()
	{
		return this.entityPosition.y();
	}

	/**
	 * Gets this entity's actual position Z component
	 *
	 * @return Actual position Z component
	 */
	public float getZEntityPos()
	{
		return this.entityPosition.z();
	}

	/**
	 * Gets the tile position that this entity is currently at as a read-only object
	 *
	 * @return Tile position
	 */
	public Vector2ic getTilePosition()
	{
		return this.tilePosition;
	}

	/**
	 * Gets the actual position of this entity as a read-only object
	 *
	 * @return Actual position
	 */
	public Vector3fc getActualPosition()
	{
		return this.entityPosition;
	}

	/**
	 * Gets the layer that this entity is in
	 *
	 * @return World layer
	 */
	public int getLayerId()
	{
		return layer;
	}

	/**
	 * Sets the size of the entity.
	 *
	 * @param width
	 *            Entity width
	 * @param height
	 *            Entity height
	 * @param anchorPoint
	 *            Entity collision box start point offset
	 */
	public void setSize(float width, float height, float anchorPoint)
	{
		this.entitySize.x = width;
		this.entitySize.y = height;
		this.anchorPoint = anchorPoint;
	}

	/**
	 * Gets the entity's width
	 *
	 * @return Entity width
	 */
	public float getWidth()
	{
		return this.entitySize.x();
	}

	/**
	 * Gets the entity's height
	 *
	 * @return Entity height
	 */
	public float getHeight()
	{
		return this.entitySize.y();
	}

	/**
	 * Sets the velocity of this entity
	 *
	 * @param velocity
	 *            Velocity
	 */
	public void setVelocity(Vector3fc velocity)
	{
		this.velocity.set(velocity);
	}

	/**
	 * Adds to the velocity of this entity
	 *
	 * @param velocity
	 *            Velocity to add
	 */
	public void addVelocity(Vector3fc velocity)
	{
		this.velocity.add(velocity);
	}

	/**
	 * Gets the velocity of this entity as a read-only object
	 *
	 * @return Entity velocity
	 */
	public Vector3fc getVelocity()
	{
		return this.velocity;
	}

	/**
	 * Calculates the distance of this entity to another entity
	 *
	 * @param entity
	 *            Other entity
	 * @return Distance to the other entity
	 */
	public float getDistanceTo(Entity entity)
	{
		return getDistanceTo(entity.getActualPosition());
	}

	/**
	 * Calculates the distance of this entity to a position in the current world layer
	 *
	 * @param pos
	 *            Position in the world layer
	 * @return Distance to the position
	 */
	public float getDistanceTo(Vector3fc pos)
	{
		return (float) Math.sqrt(Math.pow(pos.x() - this.getXEntityPos(), 2) + Math.pow(pos.y() - this.getYEntityPos(), 2) + Math.pow(pos.z() - this.getZEntityPos(), 2));
	}

	public void spawn()
	{
		this.uuid = UUID.randomUUID();
	}

	public void despawn()
	{
		// TODO: Save entity to data on spawn IF it should be saved.
	}

	/**
	 * Gets if this entity is invisible
	 *
	 * @return If this entity is invisible
	 */
	public boolean isInvisible()
	{
		return isInvisible;
	}

	/**
	 * Sets if this entity should be invisible
	 *
	 * @param invisible
	 *            Whether this entity should be invisible
	 */
	public void setInvisible(boolean invisible)
	{
		isInvisible = invisible;
	}

	/**
	 * Gest if this entity can be pushed
	 *
	 * @return If this entity can be pushed
	 */
	public boolean canBePushed()
	{
		return true;
	}

	/**
	 * Gest if this entity can spawn at the current position
	 *
	 * @return If this entity can spawn at the current position
	 */
	public boolean canSpawnHere()
	{
		return true;
	}

	/**
	 * Gets how many ticks this entity has existed for
	 *
	 * @return How many ticks this entity has existed for
	 */
	public int getTicksExisted()
	{
		return ticksExisted;
	}

	@Nonnull
	@Override
	public UBObjectWrapper writeToUBO(@Nonnull UBObjectWrapper ubo)
	{
		// Registry Name
		ubo.setString("name", this.registryName);

		// UUID
		ubo.setUUID("uuid", this.uuid);

		// Position
		UBObjectWrapper uboPos = new UBObjectWrapper();
		uboPos.setFloat("x", this.entityPosition.x());
		uboPos.setFloat("y", this.entityPosition.y());
		uboPos.setFloat("z", this.entityPosition.z());
		ubo.setUBObject("position", uboPos);

		// Layer
		ubo.setInt("layer", this.layer);

		// Direction
		ubo.setByte("direction", (byte) this.direction.ordinal());

		// Attributes
		UBObjectWrapper attributesUbo = new UBObjectWrapper();
		for (String attributeName : this.attributes.keySet())
		{
			Attribute<?> attribute = this.attributes.get(attributeName);
			RangeAttribute<?> range = attribute instanceof RangeAttribute<?> ? (RangeAttribute<?>) attribute : null;
			Object value = attribute.getValue();

			UBObjectWrapper attributeUbo = new UBObjectWrapper();
			if (value instanceof Byte)
			{
				attributeUbo.setByte("type", range != null ? AttributeType.BYTE_RANGE.getId() : AttributeType.BYTE.getId());
				attributeUbo.setByte("value", (Byte) value);
				if (range != null)
				{
					attributeUbo.setByte("minValue", (Byte) range.getMinimumValue());
					attributeUbo.setByte("maxValue", (Byte) range.getMaximumValue());
				}
			}
			else if (value instanceof Short)
			{
				attributeUbo.setByte("type", range != null ? AttributeType.SHORT_RANGE.getId() : AttributeType.SHORT.getId());
				attributeUbo.setShort("value", (Short) value);
				if (range != null)
				{
					attributeUbo.setShort("minValue", (Short) range.getMinimumValue());
					attributeUbo.setShort("maxValue", (Short) range.getMaximumValue());
				}
			}
			else if (value instanceof Integer)
			{
				attributeUbo.setByte("type", range != null ? AttributeType.INTEGER_RANGE.getId() : AttributeType.INTEGER.getId());
				attributeUbo.setInt("value", (Integer) value);
				if (range != null)
				{
					attributeUbo.setInt("minValue", (Integer) range.getMinimumValue());
					attributeUbo.setInt("maxValue", (Integer) range.getMaximumValue());
				}
			}
			else if (value instanceof Float)
			{
				attributeUbo.setByte("type", range != null ? AttributeType.FLOAT_RANGE.getId() : AttributeType.FLOAT.getId());
				attributeUbo.setFloat("value", (Float) value);
				if (range != null)
				{
					attributeUbo.setFloat("minValue", (Float) range.getMinimumValue());
					attributeUbo.setFloat("maxValue", (Float) range.getMaximumValue());
				}
			}
			else if (value instanceof Double)
			{
				attributeUbo.setByte("type", range != null ? AttributeType.DOUBLE_RANGE.getId() : AttributeType.DOUBLE.getId());
				attributeUbo.setDouble("value", (Double) value);
				if (range != null)
				{
					attributeUbo.setDouble("minValue", (Double) range.getMinimumValue());
					attributeUbo.setDouble("maxValue", (Double) range.getMaximumValue());
				}
			}
			else if (value instanceof Long)
			{
				attributeUbo.setByte("type", range != null ? AttributeType.LONG_RANGE.getId() : AttributeType.LONG.getId());
				attributeUbo.setLong("value", (Long) value);
				if (range != null)
				{
					attributeUbo.setLong("minValue", (Long) range.getMinimumValue());
					attributeUbo.setLong("maxValue", (Long) range.getMaximumValue());
				}
			}
			else if (value instanceof Boolean)
			{
				attributeUbo.setByte("type", AttributeType.BOOLEAN.getId());
				attributeUbo.setBoolean("value", (Boolean) value);
			}
			else if (value instanceof String)
			{
				attributeUbo.setByte("type", AttributeType.STRING.getId());
				attributeUbo.setString("value", (String) value);
			}
			else
			{
				throw new RuntimeException("Invalid attribute type \'" + value.getClass().getName() + "\'");
			}

			attributesUbo.setUBObject(attributeName, attributeUbo);
		}
		ubo.setUBObject("attributes", attributesUbo);

		// Misc
		ubo.setBoolean("invisible", this.isInvisible);

		return ubo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readFromUBO(@Nonnull UBObjectWrapper ubo)
	{
		// Registry Name
		this.registryName = ubo.getString("name");
		if (this.registryName == null && this.world != null)
		{
			this.world.logger().warn("Entity {} get a null registry name from deserialisation!", this);
		}

		this.uuid = ubo.getUUID("uuid");

		// Position
		UBObjectWrapper uboPos = ubo.getUBObjectWrapped("position");
		if (uboPos == null)
		{
			setPosition(new Vector3f(0F, 0F, 0F));
		}
		else
		{
			setPosition(new Vector3f(uboPos.getFloatSafe("x"), uboPos.getFloatSafe("y"), uboPos.getFloatSafe("z")));
		}

		// Direction
		this.direction = Direction.getFromIndex(ubo.getByteSafe("direction"));

		// Layer
		this.layer = ubo.getIntSafe("layer");

		// Attributes
		UBObjectWrapper attributesUbo = ubo.getUBObjectWrapped("attributes");
		for (String attributeName : attributesUbo.getWrappedUBObject().keySet())
		{
			Attribute<?> attribute = this.attributes.get(attributeName);
			if (attribute == null)
				throw new RuntimeException("Entity \'" + this.getClass().getName() + "\' has additional attributes in UBO that aren't registered!");
			UBObjectWrapper attributeUbo = attributesUbo.getUBObjectWrapped(attributeName);
			AttributeType type = AttributeType.byId(attributeUbo.getByte("type"));

			switch (type)
			{
			case BYTE:
				((Attribute<Byte>) attribute).setValue(attributeUbo.getByteSafe("value"));
				break;
			case SHORT:
				((Attribute<Short>) attribute).setValue(attributeUbo.getShortSafe("value"));
				break;
			case INTEGER:
				((Attribute<Integer>) attribute).setValue(attributeUbo.getIntSafe("value"));
				break;
			case FLOAT:
				((Attribute<Float>) attribute).setValue(attributeUbo.getFloatSafe("value"));
				break;
			case DOUBLE:
				((Attribute<Double>) attribute).setValue(attributeUbo.getDoubleSafe("value"));
				break;
			case LONG:
				((Attribute<Long>) attribute).setValue(attributeUbo.getLongSafe("value"));
				break;
			case BYTE_RANGE:
				((RangeAttribute<Byte>) attribute).setValue(attributeUbo.getByteSafe("value"));
				((RangeAttribute<Byte>) attribute).setMinimumValue(attributeUbo.getByteSafe("minValue"));
				((RangeAttribute<Byte>) attribute).setMaximumValue(attributeUbo.getByteSafe("maxValue"));
				break;
			case SHORT_RANGE:
				((RangeAttribute<Short>) attribute).setValue(attributeUbo.getShortSafe("value"));
				((RangeAttribute<Short>) attribute).setMinimumValue(attributeUbo.getShortSafe("minValue"));
				((RangeAttribute<Short>) attribute).setMaximumValue(attributeUbo.getShortSafe("maxValue"));
				break;
			case INTEGER_RANGE:
				((RangeAttribute<Integer>) attribute).setValue(attributeUbo.getIntSafe("value"));
				((RangeAttribute<Integer>) attribute).setMinimumValue(attributeUbo.getIntSafe("minValue"));
				((RangeAttribute<Integer>) attribute).setMaximumValue(attributeUbo.getIntSafe("maxValue"));
				break;
			case FLOAT_RANGE:
				((RangeAttribute<Float>) attribute).setValue(attributeUbo.getFloatSafe("value"));
				((RangeAttribute<Float>) attribute).setMinimumValue(attributeUbo.getFloatSafe("minValue"));
				((RangeAttribute<Float>) attribute).setMaximumValue(attributeUbo.getFloatSafe("maxValue"));
				break;
			case DOUBLE_RANGE:
				((RangeAttribute<Double>) attribute).setValue(attributeUbo.getDoubleSafe("value"));
				((RangeAttribute<Double>) attribute).setMinimumValue(attributeUbo.getDoubleSafe("minValue"));
				((RangeAttribute<Double>) attribute).setMaximumValue(attributeUbo.getDoubleSafe("maxValue"));
				break;
			case LONG_RANGE:
				((RangeAttribute<Long>) attribute).setValue(attributeUbo.getLongSafe("value"));
				((RangeAttribute<Long>) attribute).setMinimumValue(attributeUbo.getLongSafe("minValue"));
				((RangeAttribute<Long>) attribute).setMaximumValue(attributeUbo.getLongSafe("maxValue"));
				break;
			case BOOLEAN:
				((Attribute<Boolean>) attribute).setValue(attributeUbo.getBooleanSafe("value"));
				break;
			case STRING:
				((Attribute<String>) attribute).setValue(attributeUbo.getStringSafe("value"));
				break;
			default:
				throw new RuntimeException("Invalid attribute type \'" + type.name() + "\'");
			}
		}
		ubo.setUBObject("attributes", attributesUbo);

		// Misc
		this.isInvisible = ubo.getBooleanSafe("invisible");
	}
}
