/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.entity;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.TargetedLocationData;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.text.translation.Translatable;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.util.RelativePositions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nullable;

/**
 * An entity is a Minecraft entity.
 *
 * <p>Examples of entities include:</p>
 *
 * <ul>
 *     <li>Zombies</li>
 *     <li>Sheep</li>
 *     <li>Players</li>
 *     <li>Dropped items</li>
 *     <li>Dropped experience points</li>
 *     <li>etc.</li>
 * </ul>
 *
 * <p>Blocks and items (when they are in inventories) are not entities.</p>
 */
public interface Entity extends Identifiable, DataHolder, DataSerializable, Translatable {

    /**
     * Get the type of entity.
     *
     * @return The type of entity
     */
    EntityType getType();

    /**
     * Gets the current world this entity resides in.
     *
     * @return The current world this entity resides in
     */
    World getWorld();

    /**
     * Creates a {@link EntitySnapshot} containing the {@link EntityType} and data of this entity.
     * @return The snapshot
     */
    EntitySnapshot createSnapshot();

    /**
     * Gets the RNG for this entity.
     * @return The RNG
     */
    Random getRandom();

    /**
     * Get the location of this entity.
     *
     * @return The location
     */
    Location<World> getLocation();

    /**
     * Sets the location of this entity. This is equivalent to a teleport,
     * and also moves this entity's passengers.
     *
     * @param location The location to set
     */
    void setLocation(Location<World> location);

    /**
     * Sets the location of this entity using a safe one from {@link TeleportHelper#getSafeLocation(Location)}. This is equivalent to a
     * teleport and also moves this entity's passengers.
     *
     * @param location The location to set
     * @return True if location was set successfully, false if location couldn't be set as no safe location was found
     */
    boolean setLocationSafely(Location<World> location);

    /**
     * Gets the rotation.
     *
     * <p>The format of the rotation is represented by:</p>
     *
     * <ul><code>x -> pitch</code>, <code>y -> yaw</code>, <code>z -> roll
     * </code></ul>
     *
     * @return The rotation
     */
    Vector3d getRotation();

    /**
     * Sets the rotation of this entity.
     *
     * <p>The format of the rotation is represented by:</p>
     *
     * <ul><code>x -> pitch</code>, <code>y -> yaw</code>, <code>z -> roll
     * </code></ul>
     *
     * @param rotation The rotation to set the entity to
     */
    void setRotation(Vector3d rotation);

    /**
     * Moves the entity to the specified location, and sets the rotation.
     *
     * <p>The format of the rotation is represented by:</p>
     *
     * <ul><code>x -> pitch</code>, <code>y -> yaw</code>, <code>z -> roll
     * </code></ul>
     *
     * @param location The location to set
     * @param rotation The rotation to set
     */
    void setLocationAndRotation(Location<World> location, Vector3d rotation);

    /**
     * Sets the location using a safe one from {@link TeleportHelper#getSafeLocation(Location)} and the rotation of this entity.
     *
     * <p>The format of the rotation is represented by:</p>
     *
     * <ul><code>x -> pitch</code>, <code>y -> yaw</code>, <code>z -> roll
     * </code></ul>
     *
     * @param location The location to set
     * @param rotation The rotation to set
     * @return True if location was set successfully, false if location couldn't be set as no safe location was found
     */
    boolean setLocationAndRotationSafely(Location<World> location, Vector3d rotation);

    /**
     * Moves the entity to the specified location, and sets the rotation. {@link RelativePositions}
     * listed inside the EnumSet are considered relative.
     *
     * <p>The format of the rotation is represented by:</p>
     *
     * <ul><code>x -> pitch</code>, <code>y -> yaw</code>, <code>z -> roll
     * </code></ul>
     *
     * @param location The location to set
     * @param rotation The rotation to set
     * @param relativePositions The coordinates to set relatively
     */
    void setLocationAndRotation(Location<World> location, Vector3d rotation, EnumSet<RelativePositions> relativePositions);

    /**
     * Sets the location using a safe one from
     * {@link TeleportHelper#getSafeLocation(Location)} and the rotation of this
     * entity. {@link RelativePositions} listed inside the EnumSet are
     * considered relative.
     *
     * <p>The format of the rotation is represented by:</p>
     *
     * <ul><code>x -> pitch</code>, <code>y -> yaw</code>, <code>z -> roll
     * </code></ul>
     *
     * @param location The location to set
     * @param rotation The rotation to set
     * @param relativePositions The coordinates to set relatively
     * @return True if location was set successfully, false if location
     *     couldn't be set as no safe location was found
     */
    boolean setLocationAndRotationSafely(Location<World> location, Vector3d rotation, EnumSet<RelativePositions> relativePositions);

    /**
     * Gets the entity scale. Not currently used.
     * Returns {@link Vector3d#ONE}.
     *
     * @return The entity scale
     */
    Vector3d getScale();

    /**
     * Sets the entity scale. Not currently used.
     * Does nothing.
     *
     * @param scale The scale
     */
    void setScale(Vector3d scale);

    /**
     * Returns the entity transform as a new copy.
     * Combines the position, rotation and scale.
     *
     * @return The transform as a new copy
     */
    Transform<World> getTransform();

    /**
     * Sets the entity transform. Sets the
     * position, rotation and scale at once.
     *
     * @param transform The transform to set
     */
    void setTransform(Transform<World> transform);

    /**
     * Sets the location of this entity to a new position in a world which does
     * not have to be loaded (but must at least be enabled).
     *
     * <p>If the target world is loaded then this is equivalent to
     * setting the location via {@link TargetedLocationData}.</p>
     *
     * <p>If the target world is unloaded but is enabled according to its
     * {@link WorldProperties#isEnabled()} then this will first load the world
     * before transferring the entity to that world.</p>
     *
     * <p>If the target world is unloaded and not enabled then the transfer
     * will fail.</p>
     *
     * @param worldName The name of the world to transfer to
     * @param position The position in the target world
     * @return True if the teleport was successful
     */
    boolean transferToWorld(String worldName, Vector3d position);

    /**
     * Sets the location of this entity to a new position in a world which does
     * not have to be loaded (but must at least be enabled).
     *
     * <p>If the target world is loaded then this is equivalent to
     * setting the location via {@link TargetedLocationData}.</p>
     *
     * <p>If the target world is unloaded but is enabled according to its
     * {@link WorldProperties#isEnabled()} then this will first load the world
     * before transferring the entity to that world.</p>
     *
     * <p>If the target world is unloaded and not enabled then the transfer
     * will fail.</p>
     *
     * @param uuid The UUID of the target world to transfer to
     * @param position The position in the target world
     * @return True if the teleport was successful
     */
    boolean transferToWorld(UUID uuid, Vector3d position);

    /**
     * Gets the entity passenger that rides this entity, if available.
     *
     * @return The passenger entity, if it exists
     */
    Optional<Entity> getPassenger();

    /**
     * Sets the passenger entity(the entity that rides this one).
     *
     * @param entity The entity passenger, or null to eject
     * @return True if the set was successful
     */
    DataTransactionResult setPassenger(@Nullable Entity entity);

    /**
     * Gets the entity vehicle that this entity is riding, if available.
     *
     * @return The vehicle entity, if it exists
     */
    Optional<Entity> getVehicle();

    /**
     * Sets the vehicle entity(the entity that is ridden by this one).
     *
     * @param entity The entity vehicle, or null to dismount
     * @return True if the set was successful
     */
    DataTransactionResult setVehicle(@Nullable Entity entity);

    /**
     * Gets the entity vehicle that is the base of what ever stack the
     * current entity is a part of. This can be the current entity, if it is
     * not riding any vehicle.
     *
     * <p>The returned entity can never ride another entity, that would make
     * the ridden entity the base of the stack.</p>
     *
     * @return The vehicle entity, if available
     */
    Entity getBaseVehicle();

    /**
     * Gets the {@link Vector3d} representation of this entity's current
     * velocity.
     *
     * @return The current velocity
     */
    default Vector3d getVelocity() {
        return get(Keys.VELOCITY).get();
    }

    /**
     * Sets the velocity for this entity.
     *
     * @param vector3d The vector 3d velocity
     * @return The resulting data transaction result
     */
    default DataTransactionResult setVelocity(Vector3d vector3d) {
        return offer(Keys.VELOCITY, vector3d);
    }

    /**
     * Returns whether this entity is on the ground (not in the air) or not.
     *
     * @return Whether this entity is on the ground or not
     */
    boolean isOnGround();

    /**
     * Returns whether this entity has been removed.
     *
     * @return True if this entity has been removed
     */
    boolean isRemoved();

    /**
     * Returns whether this entity is still loaded in a world/chunk.
     *
     * @return True if this entity is still loaded
     */
    boolean isLoaded();

    /**
     * Mark this entity for removal in the very near future, preferably
     * within one game tick.
     */
    void remove();

    /**
     * Damages this {@link Entity} with the given {@link DamageSource}.
     *
     * @param damage The damage to deal
     * @param damageSource The cause of the damage
     * @return True if damaging the entity was successful
     */
    default boolean damage(double damage, DamageSource damageSource) {
        return damage(damage, damageSource, Cause.source(damageSource).build());
    }

    /**
     * Damages this {@link Entity} with the given {@link Cause}. It is
     * imperative that a {@link DamageSource} is included
     * with the cause for maximum compatibility with plugins and the game
     * itself.
     *
     * @param damage The damage to deal
     * @param damageSource The source of damage
     * @param cause The cause containing auxiliary objects
     * @return True if damaging the entity was successful
     */
    boolean damage(double damage, DamageSource damageSource, Cause cause);

    /**
     * Gets the nearby entities within the desired distance.
     *
     * @see World#getEntities(Predicate)
     * @param distance The distance
     * @return The collection of nearby entities
     */
    default Collection<Entity> getNearbyEntities(double distance) {
        checkArgument(distance > 0, "Distance must be above zero!");
        return getNearbyEntities(entity -> entity.getTransform().getPosition().distance(this.getTransform().getPosition()) <= distance);
    }

    /**
     * Gets the nearby entities that satisfy the desired predicate.
     *
     * @see World#getEntities(Predicate)
     * @param predicate The predicate to use
     * @return The collection of entities
     */
    default Collection<Entity> getNearbyEntities(Predicate<Entity> predicate) {
        checkNotNull(predicate, "Null predicate!");
        return getWorld().getEntities(predicate::test);
    }

    /**
     * Gets the {@link UUID}, if available, that created this {@link Entity}.
     *
     * @return The {@link UUID} if one exists
     */
    Optional<UUID> getCreator();

    /**
     * Gets the {@link UUID}, if available, that last notified this
     * {@link Entity}.
     *
     * @return The {@link UUID} if one exists
     */
    Optional<UUID> getNotifier();

    /**
     * Sets the {@link UUID} that created this {@link Entity}.
     *
     * @param uuid The {@link UUID} to set as creator.
     */
    void setCreator(@Nullable UUID uuid);

    /**
     * Sets the {@link UUID} that last notified this {@link Entity}.
     *
     * @param uuid The {@link UUID} to set as notifier.
     */
    void setNotifier(@Nullable UUID uuid);

    /**
     * Returns whether this entity can see the provided {@link Entity}.
     *
     * @param entity The entity to check visibility for
     * @return {@code true} if this entity can see the provided entity
     */
    default boolean canSee(Entity entity) {
        Optional<Boolean> optional = entity.get(Keys.INVISIBLE);
        return !optional.isPresent() || !optional.get();
    }
}
