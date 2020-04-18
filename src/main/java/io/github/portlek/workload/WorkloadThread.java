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

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Objects;

public final class WorkloadThread implements Runnable {

    private final Deque<Workload> deque = new ArrayDeque<>();

    private final int workThreadId;

    private final long maxNanosPerTick;

    public WorkloadThread(final int workThreadId, final long maxNanosPerTick) {
        this.workThreadId = workThreadId;
        this.maxNanosPerTick = maxNanosPerTick;
    }

    public void addLoad(final Workload... workloads) {
        Arrays.stream(workloads).forEach(workload ->
            this.deque.add(Objects.requireNonNull(workload)));
    }

    @Override
    public void run() {
        final long stoptime = System.nanoTime() + this.maxNanosPerTick;
        final Workload first = this.deque.poll();
        if (first == null) {
            return;
        }
        this.computeWorkload(first);
        while (!this.deque.isEmpty() && System.nanoTime() <= stoptime) {
            final Workload workload = this.deque.poll();
            this.computeWorkload(workload);
            if (first.equals(workload)) {
                break;
            }
        }
    }

    private void computeWorkload(final Workload workload) {
        if (Objects.requireNonNull(workload).shouldExecute()) {
            workload.compute();
        }
        if (workload.reschedule()) {
            this.addLoad(workload);
        }
    }

}
