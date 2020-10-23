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

import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/**
 * an abstract implementation for {@link Workload} and,
 * reschedules if the {@link this#test(Object)} returns {@code true}.
 *
 * @param <T> the type of the element.
 */
public abstract class ConditionalScheduleWorkload<T> implements Workload, Predicate<T> {

  /**
   * the element to test {@link Workload#shouldExecute()}.
   */
  @NotNull
  private final T element;

  /**
   * ctor.
   *
   * @param element the element.
   */
  protected ConditionalScheduleWorkload(@NotNull final T element) {
    this.element = element;
  }

  @Override
  public final boolean reschedule() {
    return this.test(this.element);
  }

  /**
   * obtains the given element.
   *
   * @return the given element.
   */
  @NotNull
  protected final T getElement() {
    return this.element;
  }
}
