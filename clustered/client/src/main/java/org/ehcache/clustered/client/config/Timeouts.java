/*
 * Copyright Terracotta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehcache.clustered.client.config;

import org.ehcache.clustered.client.internal.ClusterTierManagerClientEntity;

import java.time.Duration;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.function.LongSupplier;

/**
 * Describes the timeouts for {@link ClusterTierManagerClientEntity} operations.  Use
 * {@link #builder()} to construct an instance.
 */
public final class Timeouts {

  public static final Duration DEFAULT_OPERATION_TIMEOUT = Duration.of(5, SECONDS);

  private final Duration readOperationTimeout;
  private final Duration mutativeOperationTimeout;
  private final Duration lifecycleOperationTimeout;

  private Timeouts(Duration readOperationTimeout, Duration mutativeOperationTimeout, Duration lifecycleOperationTimeout) {
    this.readOperationTimeout = readOperationTimeout;
    this.mutativeOperationTimeout = mutativeOperationTimeout;
    this.lifecycleOperationTimeout = lifecycleOperationTimeout;
  }

  public Duration getReadOperationTimeout() {
    return readOperationTimeout;
  }

  public Duration getMutativeOperationTimeout() {
    return mutativeOperationTimeout;
  }

  public Duration getLifecycleOperationTimeout() {
    return lifecycleOperationTimeout;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Timeouts timeouts = (Timeouts) o;

    if (!readOperationTimeout.equals(timeouts.readOperationTimeout)) {
      return false;
    }
    if (!mutativeOperationTimeout.equals(timeouts.mutativeOperationTimeout)) {
      return false;
    }
    return lifecycleOperationTimeout.equals(timeouts.lifecycleOperationTimeout);
  }

  @Override
  public int hashCode() {
    int result = readOperationTimeout.hashCode();
    result = 31 * result + mutativeOperationTimeout.hashCode();
    result = 31 * result + lifecycleOperationTimeout.hashCode();
    return result;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static LongSupplier nanosStartingFromNow(Duration timeout) {
    long end = System.nanoTime() + timeout.toNanos();
    return () -> end - System.nanoTime();
  }

  @Override
  public String toString() {
    return "Timeouts{" +
        "readOperationTimeout=" + readOperationTimeout +
        ", mutativeOperationTimeout=" + mutativeOperationTimeout +
        ", lifecycleOperationTimeout=" + lifecycleOperationTimeout +
        '}';
  }

  /**
   * Constructs instances of {@link Timeouts}.  When obtained from
   * {@link Timeouts#builder()}, the default values are pre-set.
   */
  public static final class Builder implements org.ehcache.config.Builder<Timeouts> {
    private Duration readOperationTimeout = DEFAULT_OPERATION_TIMEOUT;
    private Duration mutativeOperationTimeout = DEFAULT_OPERATION_TIMEOUT;
    private Duration lifecycleOperationTimeout = DEFAULT_OPERATION_TIMEOUT;

    /**
     * Sets the timeout for read operations.  The default value for this timeout is
     * 5 seconds.
     *
     * @param readOperationTimeout the {@code Duration} to use for the read operation timeout
     *
     * @return this {@code Builder}
     */
    public Builder setReadOperationTimeout(Duration readOperationTimeout) {
      this.readOperationTimeout = Objects.requireNonNull(readOperationTimeout, "Read operation timeout can't be null");
      return this;
    }

    /**
     * Sets the timeout for mutative operations like {@code put} and {@code remove}. The default value for this timeout
     * is 5 seconds.
     *
     * @param mutativeOperationTimeout the {@code Duration} to use for a mutative operation timeout
     *
     * @return this {@code Builder}
     */
    public Builder setMutativeOperationTimeout(Duration mutativeOperationTimeout) {
      this.mutativeOperationTimeout = Objects.requireNonNull(mutativeOperationTimeout, "Mutative operation timeout can't be null");
      return this;
    }

    /**
     * Sets the timeout for server store manager lifecycle operations like {@code validate} and {@code validateCache}.
     * The default value for this timeout is 5 seconds.
     *
     * @param lifecycleOperationTimeout the {@code Duration} to use for a store manager lifecycle operation timeout
     *
     * @return this {@code Builder}
     */
    public Builder setLifecycleOperationTimeout(Duration lifecycleOperationTimeout) {
      this.lifecycleOperationTimeout = Objects.requireNonNull(lifecycleOperationTimeout, "Lifecycle operation timeout can't be null");
      return this;
    }

    /**
     * Gets a new {@link Timeouts} instance using the current timeout duration settings.
     *
     * @return a new {@code Timeouts} instance
     */
    public Timeouts build() {
      return new Timeouts(readOperationTimeout, mutativeOperationTimeout, lifecycleOperationTimeout);
    }
  }
}
