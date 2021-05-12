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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents typed distributed tasks.
 *
 * @param <T> type of the work.
 */
public final class TypedDistributedTask<T> implements Runnable {

  /**
   * the action.
   */
  @Nullable
  private final Consumer<T> action;

  /**
   * the distribution size.
   */
  private final int distributionSize;

  /**
   * the escape condition.
   */
  @Nullable
  private final Predicate<T> escapeCondition;

  /**
   * the world-load matrix.
   */
  @NotNull
  private final List<LinkedList<Supplier<T>>> suppliedValueMatrix;

  /**
   * the current position.
   */
  private int currentPosition = 0;

  /**
   * ctor.
   *
   * @param action the action.
   * @param escapeCondition the escape condition.
   * @param distributionSize the distribution size.
   */
  @Builder
  public TypedDistributedTask(@Nullable final Consumer<T> action, @Nullable final Predicate<T> escapeCondition,
                              final int distributionSize) {
    this.distributionSize = distributionSize;
    this.action = action;
    this.escapeCondition = escapeCondition;
    this.suppliedValueMatrix = new ArrayList<>(this.distributionSize);
    IntStream.range(0, this.distributionSize)
      .<LinkedList<Supplier<T>>>mapToObj(index -> new LinkedList<>())
      .forEach(this.suppliedValueMatrix::add);
  }

  /**
   * adds the given workload into the {@link #suppliedValueMatrix}.
   *
   * @param workload the workload to add.
   */
  public void add(@NotNull final Supplier<T> workload) {
    var smallestList = this.suppliedValueMatrix.get(0);
    for (var index = 0; index < this.distributionSize; index++) {
      if (smallestList.size() == 0) {
        break;
      }
      final var next = this.suppliedValueMatrix.get(index);
      final var size = next.size();
      if (size < smallestList.size()) {
        smallestList = next;
      }
    }
    smallestList.add(workload);
  }

  @Override
  public void run() {
    this.suppliedValueMatrix.get(this.currentPosition).removeIf(this::executeThenCheck);
    this.proceedPosition();
  }

  /**
   * executes the given value supplier then checks {@link #escapeCondition}.
   *
   * @param valueSupplier the value supplier to execute and check.
   *
   * @return {@code true} if {@link #escapeCondition} is null or the test succeeded.
   */
  private boolean executeThenCheck(@NotNull final Supplier<T> valueSupplier) {
    final var value = valueSupplier.get();
    if (this.action != null) {
      this.action.accept(value);
    }
    return this.escapeCondition == null || this.escapeCondition.test(value);
  }

  /**
   * processes the {@link #currentPosition}.
   */
  private void proceedPosition() {
    if (++this.currentPosition == this.distributionSize) {
      this.currentPosition = 0;
    }
  }
}
