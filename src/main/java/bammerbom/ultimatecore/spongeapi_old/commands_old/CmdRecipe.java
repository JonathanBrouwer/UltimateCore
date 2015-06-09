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
package bammerbom.ultimatecore.spongeapi_old.commands_old;

import bammerbom.ultimatecore.spongeapi_old.api.UC;
import bammerbom.ultimatecore.spongeapi_old.r;
import bammerbom.ultimatecore.spongeapi_old.resources.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

public class CmdRecipe implements UltimateCommand {

    //Methods
    private static void shapedRecipe(CommandSender sender, ShapedRecipe recipe, boolean showWindow) {
        Map<Character, ItemStack> recipeMap = recipe.getIngredientMap();
        if (showWindow) {
            Player p = (Player) sender;
            p.closeInventory();
            InventoryView view = p.openWorkbench(null, true);
            UC.getPlayer(p).setInRecipeView(true);
            String[] recipeShape = recipe.getShape();
            Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
            for (int j = 0;
                 j < recipeShape.length;
                 j++) {
                for (int k = 0;
                     k < recipeShape[j].length();
                     k++) {
                    ItemStack item = ingredientMap.get(Character.valueOf(recipeShape[j].toCharArray()[k]));
                    if (item != null) {
                        item.setAmount(1);
                        if (item.getDurability() == (short) 32767) {
                            item.setDurability((short) 0);
                        }
                        view.getTopInventory().setItem(j * 3 + k + 1, item);
                    }
                }
            }
        } else {
            HashMap<Material, String> colorMap = new HashMap<>();
            char[] arr = "abcdefghi".toCharArray();
            int len = arr.length;
            for (int i = 0;
                 i < len;
                 i++) {
                Character c = arr[i];

                ItemStack item = recipeMap.get(c);
                if (!colorMap.containsKey(item == null ? null : item.getType())) {
                    colorMap.put(item == null ? null : item.getType(), String.valueOf(i++));
                }
            }
            Material[][] materials = new Material[3][3];
            for (int j = 0;
                 j < recipe.getShape().length;
                 j++) {
                for (int k = 0;
                     k < recipe.getShape()[j].length();
                     k++) {
                    ItemStack item = recipe.getIngredientMap().get(Character.valueOf(recipe.getShape()[j].toCharArray()[k]));
                    materials[j][k] = (item == null ? null : item.getType());
                }
            }
            sender.sendMessage(r.neutral + "" + colorMap.get(materials[0][0]) + r.positive + " | " + r.neutral +
                    colorMap.get(materials[0][1]) + r.positive + " | " + r.neutral + colorMap.get(materials[0][2]));
            sender.sendMessage(r.neutral + "" + colorMap.get(materials[1][0]) + r.positive + " | " + r.neutral +
                    colorMap.get(materials[1][1]) + r.positive + " | " + r.neutral + colorMap.get(materials[1][2]));
            sender.sendMessage(r.neutral + "" + colorMap.get(materials[2][0]) + r.positive + " | " + r.neutral +
                    colorMap.get(materials[2][1]) + r.positive + " | " + r.neutral + colorMap.get(materials[2][2]));

            StringBuilder s = new StringBuilder();
            Boolean a = false;
            List<Material> mats = new ArrayList<>(colorMap.keySet());
            Collections.reverse(mats);
            for (Material mat : mats) {
                String name = (mat != null && mat != Material.AIR) ? mat.name().toLowerCase().replaceAll("_", "") : r.mes("recipeNothing");
                String num = colorMap.get(mat);
                s.append((a ? ", " : "") + num + " " + r.mes("recipeIs") + " " + name);
                a = true;
            }
            sender.sendMessage(r.positive + r.mes("recipeWhere") + ": " + r.neutral + s.toString());
        }
    }

    private static void shapelessRecipe(CommandSender sender, ShapelessRecipe recipe, boolean showWindow) {
        List<ItemStack> ingredients = recipe.getIngredientList();
        if (showWindow) {
            Player p = (Player) sender;
            InventoryView view = p.openWorkbench(null, true);
            UC.getPlayer(p).setInRecipeView(true);
            for (int i = 0;
                 i < ingredients.size();
                 i++) {
                view.setItem(i + 1, ingredients.get(i));
            }

        } else {
            StringBuilder s = new StringBuilder();
            for (int i = 0;
                 i < ingredients.size();
                 i++) {
                s.append(ingredients.get(i).getType().name().toLowerCase().replaceAll("_", ""));
                if (i != ingredients.size() - 1) {
                    s.append(",");
                }
                s.append(" ");
            }
            sender.sendMessage(r.mes("recipeCombine") + " " + r.neutral + s.toString());
        }
    }

    @Override
    public String getName() {
        return "recipe";
    }

    @Override
    public String getPermission() {
        return "uc.recipe";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("formula", "method", "recipes");
    }

    @Override
    public void run(final CommandSender cs, String label, String[] args) {
        if (!r.perm(cs, "uc.recipe", true, true)) {
            return;
        }
        if (!r.checkArgs(args, 0)) {
            r.sendMes(cs, "recipeUsage");
            return;
        }
        ItemStack item = ItemUtil.searchItem(args[0]);
        if (item == null) {
            r.sendMes(cs, "recipeItemNotFound", "%Item", args[0]);
            return;
        }
        int recipeNo = 0;
        if (args.length > 1) {
            if (r.isInt(args[1])) {
                recipeNo = Integer.parseInt(args[1]) - 1;
            } else {
                r.sendMes(cs, "numberFormat", "%Number", args[1]);
                return;
            }
        }
        List<Recipe> recipes = Bukkit.getServer().getRecipesFor(item);
        if (recipes.isEmpty()) {
            r.sendMes(cs, "recipeNoRecipesFound");
            return;
        }
        if (recipes.size() <= (recipeNo)) {
            r.sendMes(cs, "recipeNoMoreRecipes", "%Amount", recipeNo + 1);
            return;
        }
        Recipe selected = recipes.get(recipeNo);
        if (selected instanceof FurnaceRecipe) {
            r.sendMes(cs, "recipeSmelt", "%Input", ((FurnaceRecipe) selected).getInput().getType().name().toLowerCase().replaceAll("_", ""));
        } else if (selected instanceof ShapedRecipe) {
            shapedRecipe(cs, (ShapedRecipe) selected, cs instanceof Player);
        } else if (selected instanceof ShapelessRecipe) {
            if (item.getType().equals(Material.FIREWORK)) {
                ShapelessRecipe shapelessRecipe = new ShapelessRecipe(item);
                shapelessRecipe.addIngredient(Material.SULPHUR);
                shapelessRecipe.addIngredient(Material.PAPER);
                shapelessRecipe.addIngredient(Material.FIREWORK_CHARGE);
                shapelessRecipe(cs, shapelessRecipe, cs instanceof Player);
            }
            shapelessRecipe(cs, (ShapelessRecipe) selected, cs instanceof Player);
        }
        if (recipes.size() > 1) {
            r.sendMes(cs, "recipeTip", "%Item", item.getType().name().toLowerCase().replaceAll("_", ""));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
