package com.zerra.common.world.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import com.zerra.common.event.entity.EntityEvent;
import com.zerra.common.event.entity.EntityUpdateEvent;
import com.zerra.common.util.UBObjectWrapper;
import com.zerra.common.world.World;
import com.zerra.common.world.entity.attrib.Attribute;
import com.zerra.common.world.entity.attrib.AttributeFactory;
import com.zerra.common.world.entity.attrib.AttributeType;
import com.zerra.common.world.entity.attrib.AttributeWrapper;
import com.zerra.common.world.entity.facing.Direction;
import com.zerra.common.world.storage.Layer;
import com.zerra.common.world.storage.Storable;

public abstract class Entity implements Storable
{
	private Map<Attribute<?>, AttributeWrapper<?>> attributes;

	private World world;
	private String registryName;
	private UUID uuid;

	private int ticksExisted;

	private int layer;
	private Vector3f position;
	private Vector3i tilePosition;
	private Vector3f velocity;

	private Vector2f entitySize;

	private boolean isInvisible;
	private float anchorPoint;

	private Direction direction;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Entity(World world)
	{
		this.attributes = new HashMap<Attribute<?>, AttributeWrapper<?>>();

		List<Attribute<?>> list = new ArrayList<Attribute<?>>();
		this.registerEntityAttributes(list);
		for (Attribute<?> attribute : list)
		{
			this.attributes.put(attribute, new AttributeWrapper(attribute, attribute.getDefaultValue()));
		}

		this.world = world;

		this.ticksExisted = 0;

		this.layer = 0;
		this.position = new Vector3f();
		this.tilePosition = new Vector3i();
		this.velocity = new Vector3f();

		this.entitySize = new Vector2f();

		this.isInvisible = false;
		this.anchorPoint = 0;

		this.direction = Direction.SOUTH;

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
	protected void registerEntityAttributes(List<Attribute<?>> attributes)
	{
	}

	/**
	 * Called during entity construction.
	 */
	protected void init()
	{
	}

	/**
	 * Checks the attributes for an attribute registered under the specified name.
	 * 
	 * @param name
	 *            The name of the attribute
	 * @return The attribute registered under that name or null if there is none.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public <T> AttributeWrapper<T> getAttribute(Attribute<T> attribute)
	{
		return (AttributeWrapper<T>) attributes.get(attribute);
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
		this.position.set(position);
		this.tilePosition.set((int) Math.floor(position.x()), (int) Math.floor(position.y()), (int) Math.floor(position.z()));
	}

	public void setX(float x)
	{
		this.position.x = x;
		this.tilePosition.x = (int) Math.floor(x);
	}

	public void setY(float y)
	{
		this.position.y = y;
		this.tilePosition.y = (int) Math.floor(y);
	}

	public void setZ(float z)
	{
		this.position.z = z;
		this.tilePosition.z = (int) Math.floor(z);
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
		return this.position.x();
	}

	/**
	 * Gets this entity's actual position Y component
	 *
	 * @return Actual position Y component
	 */
	public float getYEntityPos()
	{
		return this.position.y();
	}

	/**
	 * Gets this entity's actual position Z component
	 *
	 * @return Actual position Z component
	 */
	public float getZEntityPos()
	{
		return this.position.z();
	}

	/**
	 * Gets the tile position that this entity is currently at as a read-only object
	 *
	 * @return Tile position
	 */
	public Vector3ic getTilePosition()
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
		return this.position;
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
		uboPos.setFloat("x", this.position.x());
		uboPos.setFloat("y", this.position.y());
		uboPos.setFloat("z", this.position.z());
		ubo.setUBObject("position", uboPos);

		// Layer
		ubo.setInt("layer", this.layer);

		// Direction
		ubo.setByte("direction", (byte) this.direction.ordinal());

		// Attributes
		UBObjectWrapper attributesUbo = new UBObjectWrapper();
		for (Attribute<?> attribute : this.attributes.keySet())
		{
			AttributeWrapper<?> wrapper = this.attributes.get(attribute);
			Object value = wrapper.getValue();

			UBObjectWrapper attributeUbo = new UBObjectWrapper();
			if (value instanceof Byte)
			{
				attributeUbo.setByte("type", AttributeType.BYTE.getId());
				attributeUbo.setByte("value", (Byte) value);
			}
			else if (value instanceof Short)
			{
				attributeUbo.setByte("type", AttributeType.SHORT.getId());
				attributeUbo.setShort("value", (Short) value);
			}
			else if (value instanceof Integer)
			{
				attributeUbo.setByte("type", AttributeType.INTEGER.getId());
				attributeUbo.setInt("value", (Integer) value);
			}
			else if (value instanceof Float)
			{
				attributeUbo.setByte("type", AttributeType.FLOAT.getId());
				attributeUbo.setFloat("value", (Float) value);
			}
			else if (value instanceof Double)
			{
				attributeUbo.setByte("type", AttributeType.DOUBLE.getId());
				attributeUbo.setDouble("value", (Double) value);
			}
			else if (value instanceof Long)
			{
				attributeUbo.setByte("type", AttributeType.LONG.getId());
				attributeUbo.setLong("value", (Long) value);
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

			attributesUbo.setUBObject(attribute.getName(), attributeUbo);
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
			Attribute<?> attribute = null;
			for (Attribute<?> attributeM : this.attributes.keySet())
			{
				if (attributeM.getName().equals(attributeName))
				{
					attribute = attributeM;
					break;
				}
			}

			if (attribute == null)
				throw new RuntimeException("Entity \'" + this.getClass().getName() + "\' has additional attributes in UBO that aren't registered!");
			AttributeWrapper<?> wrapper = this.attributes.get(attribute);
			UBObjectWrapper attributeUbo = attributesUbo.getUBObjectWrapped(attributeName);
			AttributeType type = AttributeType.byId(attributeUbo.getByte("type"));

			switch (type)
			{
			case BYTE:
				((AttributeWrapper<Byte>) wrapper).setValue(attributeUbo.getByteSafe("value"));
				break;
			case SHORT:
				((AttributeWrapper<Short>) wrapper).setValue(attributeUbo.getShortSafe("value"));
				break;
			case INTEGER:
				((AttributeWrapper<Integer>) wrapper).setValue(attributeUbo.getIntSafe("value"));
				break;
			case FLOAT:
				((AttributeWrapper<Float>) wrapper).setValue(attributeUbo.getFloatSafe("value"));
				break;
			case DOUBLE:
				((AttributeWrapper<Double>) wrapper).setValue(attributeUbo.getDoubleSafe("value"));
				break;
			case LONG:
				((AttributeWrapper<Long>) wrapper).setValue(attributeUbo.getLongSafe("value"));
				break;
			case BOOLEAN:
				((AttributeWrapper<Boolean>) wrapper).setValue(attributeUbo.getBooleanSafe("value"));
				break;
			case STRING:
				((AttributeWrapper<String>) wrapper).setValue(attributeUbo.getStringSafe("value"));
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
