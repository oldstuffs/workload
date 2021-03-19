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

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * a simple workload test.
 */
final class WorkloadTest {

  /**
   * a player task
   */
  private final TypedDistributedTask<Player> playerTask = TypedDistributedTask.<Player>builder()
    .distributionSize(20)
    .action(player -> player.sendMessage("Your name is " + player.getName()))
    .escapeCondition(Objects::isNull)
    .build();

  /**
   * application's main method.
   *
   * @param args the args to run.
   *
   * @throws InterruptedException if something goes wrong when thread sleeping.
   */
  public static void main(final String[] args) throws InterruptedException {
    final WorkloadTest test = new WorkloadTest();
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(test::exec, 0L, 5L, TimeUnit.MICROSECONDS);
    Executors.newScheduledThreadPool(1).scheduleAtFixedRate(test.playerTask, 50L, 50L, TimeUnit.MICROSECONDS);
    while (true) {
      Thread.sleep(5L);
    }
  }

  /**
   * creates a joining request.
   */
  public void exec() {
    this.join(new Player() {
      @NotNull
      @Override
      public String getName() {
        return "test";
      }

      @Override
      public void sendMessage(@NotNull final String message) {
        System.out.println(message);
      }
    });
  }

  /**
   * runs when a player joins the server.
   *
   * @param player the player to join.
   */
  public void join(@NotNull final Player player) {
    this.playerTask.add(() -> player);
  }
}
