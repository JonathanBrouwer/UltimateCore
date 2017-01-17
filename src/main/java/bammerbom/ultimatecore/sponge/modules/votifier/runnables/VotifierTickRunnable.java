/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
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
package bammerbom.ultimatecore.sponge.modules.votifier.runnables;

import bammerbom.ultimatecore.sponge.api.data.GlobalData;
import bammerbom.ultimatecore.sponge.modules.votifier.api.VotifierKeys;
import bammerbom.ultimatecore.sponge.modules.votifier.handlers.VotifierHandler;
import com.vexsoftware.votifier.model.Vote;

import java.util.ArrayList;
import java.util.List;

public class VotifierTickRunnable implements Runnable {
    @Override
    public void run() {
        List<Vote> votes = GlobalData.get(VotifierKeys.VOTES_CACHED).get();
        boolean edit = false;
        for (Vote vote : new ArrayList<>(votes)) {
            if (VotifierHandler.handle(vote, false)) {
                votes.remove(vote);
                edit = true;
            }
        }

        if (edit) {
            GlobalData.offer(VotifierKeys.VOTES_CACHED, votes);
        }
    }
}
