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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.NotNull;

/**
 * a class that stores and computes {@link Workload} instances.
 */
public final class WorkloadThread implements Runnable {

  /**
   * the work deque.
   */
  private final Queue<Workload> deque = new ConcurrentLinkedQueue<>();

  /**
   * the work thread id.
   */
  private final long workThreadId;

  /**
   * the maximum nano per tick.
   */
  private final long maxNanosPerTick;

  /**
   * ctor.
   *
   * @param workThreadId the work thread id.
   * @param maxNanosPerTick the maximum nano per tick.
   */
  WorkloadThread(final long workThreadId, final long maxNanosPerTick) {
    this.workThreadId = workThreadId;
    this.maxNanosPerTick = maxNanosPerTick;
  }

  @Override
  public void run() {
    final long stopTime = System.nanoTime() + this.maxNanosPerTick;
    final Workload first = this.deque.poll();
    if (first == null) {
      return;
    }
    this.computeWorkload(first);
    Workload workload;
    while ((workload = this.deque.poll()) != null && System.nanoTime() <= stopTime) {
      this.computeWorkload(workload);
      if (!first.reschedule() && first.equals(workload)) {
        break;
      }
    }
  }

  /**
   * runs {@link Workload#compute()} if {@link Workload#shouldExecute()} is {@code true}.
   * also reschedules if {@link Workload#reschedule()} is {@code true}.
   *
   * @param workload the workload to compute.
   */
  private void computeWorkload(@NotNull final Workload workload) {
    if (workload.shouldExecute()) {
      workload.compute();
    }
    if (workload.reschedule()) {
      this.deque.add(workload);
    }
  }
}
