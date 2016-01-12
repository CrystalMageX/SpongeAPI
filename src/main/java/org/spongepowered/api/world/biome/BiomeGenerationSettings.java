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
package org.spongepowered.api.world.biome;

import org.spongepowered.api.world.gen.GenerationPopulator;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.structure.Structure;

import java.util.List;

/**
 * A representation of the biome-specific generation settings.
 */
public interface BiomeGenerationSettings {

    /**
     * Gets the minimum terrain height of this biome.
     * 
     * @return The min height
     */
    float getMinHeight();

    /**
     * Sets the minimum terrain height of this biome.
     * 
     * @param height The new min height
     */
    void setMinHeight(float height);

    /**
     * Gets the maximum terrain height of this biome.
     * 
     * @return The max height
     */
    float getMaxHeight();

    /**
     * Sets the maximum terrain height of this biome.
     * 
     * @param height The new max height
     */
    void setMaxHeight(float height);

    /**
     * Gets a mutable ordered list of {@link GroundCoverLayer}s. These layers
     * will be applied to the base terrain during the generation phase starting
     * at the topmost stone block in each column.
     * 
     * @return The ground cover layers
     */
    List<GroundCoverLayer> getGroundCoverLayers();

    /**
     * Gets a mutable list of {@link GenerationPopulator}s. These populators
     * work strictly on a single chunk. They will be executed directly after the
     * {@link #getGroundCoverLayers() ground cover layers} are applied. These
     * generation populators are typically used to generate large terrain
     * features, like caves and ravines.
     * 
     * @return The generation populators
     */
    List<GenerationPopulator> getGenerationPopulators();

    /**
     * Gets an immutable list of {@link GenerationPopulator}s matching the given
     * class type.
     * 
     * @param type the generation populator type to return
     * @return The generation populators
     */
    <G extends GenerationPopulator> List<G> getGenerationPopulators(Class<G> type);
    
    /**
     * Gets a mutable list of {@link Structure}s. These structures are a special
     * case of both populators and generation populators and are called for each
     * of the two phases. They will be called after the
     * {@link GenerationPopulator}s for the generation phase and before the
     * {@link Populator}s for the population phase.
     * 
     * @return The structures
     */
    List<Structure> getStructures();
    
    /**
     * Gets an immutable list of {@link Structure}s which match the given
     * structure type.
     * 
     * @param type The type to match
     * @return The structures which match the type
     */
    <S extends Structure> List<S> getStructures(Class<S> type);

    /**
     * Returns a mutable list of {@link Populator}s specific to this biome.
     * Changing this list will affect population of all new chunks.
     * 
     * @return The populators
     */
    List<Populator> getPopulators();

    /**
     * Returns an immutable list of {@link Populator}s specific to this biome
     * which match the given class type.
     * 
     * @param type the populator type to return
     * @param <P> The populator type
     * @return The populators
     */
    <P extends Populator> List<P> getPopulators(Class<P> type);

}
