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
package bammerbom.ultimatecore.sponge.impl.sign;


import bammerbom.ultimatecore.sponge.api.sign.SignService;
import bammerbom.ultimatecore.sponge.api.sign.UCSign;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//TODO move sign service to sign module
public class UCSignService implements SignService {
    List<UCSign> signs = new ArrayList<>();

    /**
     * Retrieves list of all registered signs
     *
     * @return the list
     */
    @Override
    public List<UCSign> getRegisteredSigns() {
        return signs;
    }

    /**
     * Retrieves a specific sign by id
     *
     * @param id the id to search for
     * @return the sign, or Optional.empty() if not found
     */
    @Override
    public Optional<UCSign> getSign(String id) {
        for (UCSign sign : signs) {
            if (sign.getIdentifier().equalsIgnoreCase(id)) {
                return Optional.of(sign);
            }
        }
        return Optional.empty();
    }

    /**
     * Registers a new sign
     *
     * @param sign The sign to register
     * @return Whether the sign was successfully registered
     */
    @Override
    public boolean registerSign(UCSign sign) {
        signs.add(sign);
        return true;
    }

    /**
     * Unregisters a sign
     *
     * @param id The identifier of the sign
     * @return Whether the sign was found
     */
    @Override
    public boolean unregisterSign(String id) {
        return getSign(id).isPresent() ? unregisterSign(getSign(id).get()) : false;
    }

    /**
     * Unregisters a sign
     *
     * @param sign The instance of the sign
     * @return Whether the sign was found
     */
    @Override
    public boolean unregisterSign(UCSign sign) {
        if (signs.contains(sign)) {
            signs.remove(sign);
            return true;
        }
        return false;
    }
}
