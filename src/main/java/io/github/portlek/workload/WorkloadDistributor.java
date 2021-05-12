/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.workload;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * a class that stores and distributes {@link WorkloadThread} instances.
 */
public final class WorkloadDistributor implements Runnable {

  /**
   * the work load thread map.
   */
  private final Map<Long, WorkloadThread> map = new HashMap<>();

  /**
   * the next work load id.
   */
  private long nextId = 0L;

  /**
   * creates a new {@link WorkloadThread} instance.
   *
   * @param nanoPerTick the nano per tick.
   *
   * @return a new {@link WorkloadThread} instance.
   */
  @NotNull
  public WorkloadThread createThread(final long nanoPerTick) {
    final WorkloadThread thread = new WorkloadThread(++this.nextId, nanoPerTick);
    this.map.put(this.nextId, thread);
    return thread;
  }

  @Override
  public void run() {
    this.map.values().forEach(WorkloadThread::run);
  }
}
