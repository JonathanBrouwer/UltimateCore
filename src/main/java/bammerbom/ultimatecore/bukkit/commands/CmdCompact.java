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
package bammerbom.ultimatecore.bukkit.commands;

import bammerbom.ultimatecore.bukkit.r;
import bammerbom.ultimatecore.bukkit.resources.utils.InventoryUtil;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class CmdCompact implements UltimateCommand {

    private static final Map<ItemStack, SimpleRecipe> condenseList = new HashMap<>();

    private static boolean condenseStack(Player p, ItemStack stack, boolean validateReverse) {
        SimpleRecipe condenseType = getCondenseType(stack);
        if (condenseType != null) {
            ItemStack input = condenseType.getInput();
            ItemStack result = condenseType.getResult();

            if (validateReverse) {
                boolean pass = false;
                for (Recipe revRecipe : Bukkit.getServer().getRecipesFor(input)) {
                    if (getStackOnRecipeMatch(revRecipe, result) != null) {
                        pass = true;
                        break;
                    }
                }
                if (!pass) {
                    return false;
                }
            }

            int amount = 0;

            for (ItemStack contents : p.getInventory().getContents()) {
                if ((contents != null) && (contents.isSimilar(stack))) {
                    amount += contents.getAmount();
                }
            }

            int output = amount / input.getAmount() * result.getAmount();
            amount -= amount % input.getAmount();

            if (amount > 0) {
                input.setAmount(amount);
                result.setAmount(output);
                if (p.getInventory().containsAtLeast(input, input.getAmount())) {
                    p.getInventory().removeItem(new ItemStack[]{input});
                }
                InventoryUtil.addItem(p.getInventory(), result);
                return true;
            }
        }
        return false;
    }

    private static SimpleRecipe getCondenseType(ItemStack stack) {
        if (condenseList.containsKey(stack)) {
            return condenseList.get(stack);
        }

        Iterator<Recipe> intr = Bukkit.getServer().recipeIterator();
        while (intr.hasNext()) {
            Recipe recipe = intr.next();
            Collection<ItemStack> recipeItems = getStackOnRecipeMatch(recipe, stack);

            if ((recipeItems != null) && ((recipeItems.size() == 4) || (recipeItems.size() == 9)) && (recipeItems.size() > recipe.getResult().getAmount())) {
                ItemStack input = stack.clone();
                input.setAmount(recipeItems.size());
                SimpleRecipe newRecipe = new SimpleRecipe(recipe.getResult(), input);
                condenseList.put(stack, newRecipe);
                return newRecipe;
            }
        }
        condenseList.put(stack, null);
        return null;
    }

    private static Collection<ItemStack> getStackOnRecipeMatch(Recipe recipe, ItemStack stack) {
        Collection<ItemStack> inputList;
        if ((recipe instanceof ShapedRecipe)) {
            ShapedRecipe sRecipe = (ShapedRecipe) recipe;
            inputList = sRecipe.getIngredientMap().values();
        } else {
            if ((recipe instanceof ShapelessRecipe)) {
                ShapelessRecipe slRecipe = (ShapelessRecipe) recipe;
                inputList = slRecipe.getIngredientList();
            } else {
                return null;
            }
        }
        boolean match = true;
        Iterator<ItemStack> iter = inputList.iterator();
        while (iter.hasNext()) {
            ItemStack inputSlot = iter.next();
            if (inputSlot == null) {
                iter.remove();
            } else {
                if (inputSlot.getDurability() == 32767) {
                    inputSlot.setDurability((short) 0);
                }
                if (!inputSlot.isSimilar(stack)) {
                    match = false;
                }
            }
        }
        if (match) {
            return inputList;
        }
        return null;
    }

    @Override
    public String getName() {
        return "compact";
    }

    @Override
    public String getPermission() {
        return "uc.compact";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stack", "condense", "blocks");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.compact", false, true)) {
            return;
        }
        if (!r.isPlayer(cs)) {
            return;
        }
        Player p = (Player) cs;
        Integer a = 0;
        if (r.checkArgs(args, 0) && args[0].equalsIgnoreCase("hand")) {
            condenseStack(p, p.getItemInHand(), false);
            a++;
        } else {
            for (ItemStack s : p.getInventory().getContents()) {
                if (condenseStack(p, s, true)) {
                    a++;
                }
            }
        }
        p.updateInventory();
        if (a == 0) {
            r.sendMes(cs, "compactNone");
        } else {
            r.sendMes(cs, "compactInventory");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return new ArrayList<>();
    }

    static class SimpleRecipe implements Recipe {

        private final ItemStack result;
        private final ItemStack input;

        protected SimpleRecipe(ItemStack result, ItemStack input) {
            this.result = result;
            this.input = input;
        }

        @Override
        public ItemStack getResult() {
            return result.clone();
        }

        public ItemStack getInput() {
            return input.clone();
        }
    }

    //
}
