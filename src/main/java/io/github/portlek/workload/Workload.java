/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
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

/**
 * an interface to determine works.
 */
public interface Workload {

  /**
   * computes a piece of the work.
   */
  void compute();

  /**
   * checks if the work is reschedulable.
   *
   * @return {@code true} if the work should be re schedule.
   */
  default boolean reschedule() {
    return false;
  }

  /**
   * checks if the work should execute. if it's {@code true} than runs {@link Workload#compute()}.
   *
   * @return {@code true} if the work should execute.
   */
  default boolean shouldExecute() {
    return true;
  }
}
