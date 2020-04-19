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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public final class TestAll {

    private static final long TICK = TimeUnit.MILLISECONDS.toNanos(1L);

    static long count = 0L;

    private final WorkloadDistributor distributor = new WorkloadDistributor();

    private final Supplier<List<Workload>> works = () -> {
        final List<Workload> workloads = new ArrayList<>();
        for (int index = 0; index < 1000; index++) {
            workloads.add(new ConditionalScheduleWorkloadTest(11));
        }
        return workloads;
    };

    @AfterAll
    static void after() {
        System.out.println(TestAll.count);
    }

    @Test
    void run() {
        final WorkloadThread thread = this.distributor.createThread(TestAll.TICK);
        thread.addLoad(this.works.get());
        thread.run();
    }

}
