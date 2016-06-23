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
package bammerbom.ultimatecore.spongeapi.api;

import bammerbom.ultimatecore.spongeapi.UltimateFileLoader;
import bammerbom.ultimatecore.spongeapi.jsonconfiguration.JsonConfig;
import bammerbom.ultimatecore.spongeapi.r;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.context.Contextual;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.*;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.*;

public class UEconomy implements EconomyService {

    public static String test;
    static JsonConfig conf;
    static String format;
    static Text currencyName;
    static Text currencyNamePlural;
    static int round;
    static Currency currency;

    /**
     * Internal method, ignore please.
     */
    public static void start() {
        UEconomy uEconomy = new UEconomy();
        conf = new JsonConfig(UltimateFileLoader.Deconomy);
        format = r.getCnfg().getString("Economy.format");
        currencyName = Text.of(r.getCnfg().getString("Economy.currencyName"));
        currencyNamePlural = Text.of(r.getCnfg().getString("Economy.currencyNamePlural"));
        round = r.getCnfg().getInt("Economy.roundBalance", 3);
        currency = uEconomy.new UltimateCurrency();
        Sponge.getServiceManager().setProvider(r.getUC(), EconomyService.class, uEconomy);
    }

    public static TransactionResult getTransactionResult(Account ac, BigDecimal am, Set<Context> co, ResultType resultType, TransactionType transactionType) {
        return new TransactionResult() {
            @Override
            public Account getAccount() {
                return ac;
            }

            @Override
            public Currency getCurrency() {
                return currency;
            }

            @Override
            public BigDecimal getAmount() {
                return am;
            }

            @Override
            public Set<Context> getContexts() {
                return co;
            }

            @Override
            public ResultType getResult() {
                return resultType;
            }

            @Override
            public TransactionType getType() {
                return transactionType;
            }
        };
    }

    public static TransferResult getTransferResult(Account from, Account to, BigDecimal am, Set<Context> co, ResultType resultType, TransactionType transactionType) {
        return new TransferResult() {
            @Override
            public Account getAccountTo() {
                return to;
            }

            @Override
            public Account getAccount() {
                return from;
            }

            @Override
            public Currency getCurrency() {
                return currency;
            }

            @Override
            public BigDecimal getAmount() {
                return am;
            }

            @Override
            public Set<Context> getContexts() {
                return co;
            }

            @Override
            public ResultType getResult() {
                return resultType;
            }

            @Override
            public TransactionType getType() {
                return transactionType;
            }
        };
    }

    public JsonConfig getData() {
        return conf;
    }

    /**
     * Retrieves the default {@link Currency} used by the {@link EconomyService}.
     *
     * @return {@link Currency} default for the EconomyService
     * @see Currency
     */
    @Override
    public Currency getDefaultCurrency() {
        return currency;
    }

    /**
     * Returns the {@link Set} of supported {@link Currency} objects that are
     * implemented by this EconomyService.
     * <p>
     * <p>The economy service provider may only support one currency, in which
     * case {@link #getDefaultCurrency()} will be the only member of the set.</p>
     * <p>
     * <p>The set returned is a read-only a view of all currencies available in
     * the EconomyService.</p>
     *
     * @return The {@link Set} of all {@link Currency}s
     */
    @Override
    public Set<Currency> getCurrencies() {
        HashSet<Currency> set = new HashSet<>();
        set.add(currency);
        return set;
    }

    /**
     * Returns whether a {@link UniqueAccount} exists with the specified {@link UUID}.
     *
     * @param uuid The {@link UUID} of the account to check for
     * @return Whether a {@link UniqueAccount} exists with the specified {@link UUID}
     */
    @Override
    public boolean hasAccount(UUID uuid) {
        return getData().contains(uuid.toString());
    }

    /**
     * Returns whether an {@link Account} with the specified identifier exists.
     * <p>
     * <p>Depending on the implementation, the {@link Account} may be a {@link UniqueAccount} or
     * a {@link VirtualAccount}.
     *
     * @param identifier The identifier of the account to check for
     * @return Whether an {@link Account} with the specified identifier exists
     */
    @Override
    public boolean hasAccount(String identifier) {
        return getData().contains(identifier);
    }

    /**
     * Gets the {@link UniqueAccount} for the user with the specified {@link UUID}.
     * <p>
     * <p>If an account does not already exists with the specified {@link UUID}, it will be
     * created.</p>
     * <p>
     * <p>Creation might fail if the provided {@link UUID} does not correspond to an actual
     * player, or for an implementation-defined reason.</p>
     *
     * @param uuid The {@link UUID} of the account to get.
     * @return The {@link UniqueAccount}, if available.
     */
    @Override
    public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) {
        return Optional.of(new UltimateAccount(uuid));
    }

    /**
     * Gets the {@link VirtualAccount} with the specified identifier
     * <p>
     * <p>Depending on the implementation, the {@link Account} may be a {@link UniqueAccount} or
     * a {@link VirtualAccount}.
     * <p>
     * <p>If an account does not already exists with the specified identifier, it will be
     * created.</p>
     * <p>
     * <p>Creation may fail for an implementation-defined reason.</p>
     *
     * @param identifier The identifier of the account to get.
     * @return The {@link Account}, if available.
     */
    @Override
    public Optional<Account> getOrCreateAccount(String identifier) {
        if (!r.searchGameProfile(identifier).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new UltimateAccount(r.searchGameProfile(identifier).get()));
    }

    /**
     * Register a function that calculates {@link Context}s relevant to a
     * {@link Contextual} given at the time the function is called.
     *
     * @param calculator The context calculator to register
     */
    @Override
    public void registerContextCalculator(ContextCalculator<Account> calculator) {

    }

    /**
     * The minimum amount of money a player can have.
     *
     * @return the minimum amount
     */
    public BigDecimal getMinimumMoney() {
        return r.getCnfg().getBigDecimal("Economy.minimumMoney");
    }

    /**
     * The maximum amount of money a player can have.
     *
     * @return the maximum amount, or null when not available.
     */
    public BigDecimal getMaximumMoney() {
        BigDecimal q = r.getCnfg().getBigDecimal("Economy.maximumMoney");
        return q == new BigDecimal(-1.0) ? null : q;
    }

    /**
     * The amount of money a player starts with
     *
     * @return the starting amount
     */
    public BigDecimal getStartingMoney() {
        return r.getCnfg().getBigDecimal("Economy.startingBalance");
    }

    public class UltimateCurrency implements Currency {

        /**
         * The currency's display name, in singular form. Ex: Dollar.
         * <p>
         * <p>This should be preferred over {@link CatalogType#getName()}
         * for display purposes.</p>
         *
         * @return displayName of the currency singular
         */
        @Override
        public Text getDisplayName() {
            return UEconomy.currencyName;
        }

        /**
         * The currency's display name in plural form. Ex: Dollars.
         * <p>
         * <p>Not all currencies will have a plural name that differs from the
         * display name.</p>
         *
         * @return displayName of the currency plural
         */
        @Override
        public Text getPluralDisplayName() {
            return UEconomy.currencyNamePlural;
        }

        /**
         * The currency's symbol. Ex. $
         *
         * @return symbol of the currency
         */
        @Override
        public Text getSymbol() {
            return null;
        }

        /**
         * Formats the given amount using the default number of fractional digits.
         * <p>
         * <p>Should include the symbol if it is present</p>
         *
         * @param amount The amount to format
         * @return String formatted amount
         */
        @Override
        public Text format(BigDecimal amount) {
            return Text.of(format.replace("%Amount", r.round(amount, round) + ""));
        }

        /**
         * Formats the given amount using the specified number of fractional digits.
         * <p>
         * <p>Should include the symbol if it is present</p>
         *
         * @param amount            The amount to format
         * @param numFractionDigits The numer of fractional digits to use
         * @return String formatted amount.
         */
        @Override
        public Text format(BigDecimal amount, int numFractionDigits) {
            return Text.of(format.replace("%Amount", r.round(amount, numFractionDigits) + ""));
        }

        /**
         * This is the default number of fractional digits that is utilized for
         * formatting purposes.
         *
         * @return defaultFractionDigits utilized.
         */
        @Override
        public int getDefaultFractionDigits() {
            return round;
        }

        /**
         * Returns true if this currency is the default currency for the economy,
         * otherwise false.
         *
         * @return true if this is the default currency
         */
        @Override
        public boolean isDefault() {
            return true;
        }

        /**
         * Gets the unique identifier of this {@link CatalogType}. The identifier is
         * case insensitive, thus there cannot be another instance with a different
         * character case. The id of this instance must remain the same for the
         * entire duration of its existence. The identifier can be formatted however
         * needed.
         * <p>
         * <p>A typical id format follows the pattern of <code>`modId:name`</code>
         * or <code>`minecraft:name`</code>. However the prefix may be omitted for
         * default/vanilla minecraft types.</p>
         *
         * @return The unique identifier of this catalog type
         */
        @Override
        public String getId() {
            return "ultimatecore:currency";
        }

        /**
         * Gets the human-readable name of this individual {@link CatalogType}. This
         * name is not guaranteed to be unique. This value should not be used for
         * serialization.
         *
         * @return The human-readable name of this catalog type
         */
        @Override
        public String getName() {
            return "UltimateCore Currency";
        }
    }

    public class UltimateAccount implements UniqueAccount {

        private final UUID uuid;

        public UltimateAccount(UUID uuid) {
            this.uuid = uuid;
        }

        public UltimateAccount(GameProfile user) {
            this.uuid = user.getUniqueId();
        }

        /**
         * Gets the display name for this account.
         * <p>
         * <p>This should be used by plugins to get a human-readable name for an
         * account, regardless of the specific type ({@link UniqueAccount} or
         * {@link VirtualAccount}).</p>
         * <p>
         * <p>Its contents are dependent on the provider of {@link EconomyService}.
         * For example, an economy plugin could allow players to configure the
         * display name of their account</p>.
         *
         * @return the display name for this account.
         */
        @Override
        public Text getDisplayName() {
            return Text.of(r.searchGameProfile(uuid).get().getName());
        }

        /**
         * Gets the default balance of this account for the specified
         * {@link Currency}.
         * <p>
         * <p>The default balance is used when the balance is retrieved for the
         * first time for a given {@link Currency} on this account, or if no
         * balance is available for the {@link Context}s used when retrieving
         * a balance.</p>
         *
         * @param currency the currency to get the default balance for.
         * @return The default balance for the specified {@link Currency}.
         */
        @Override
        public BigDecimal getDefaultBalance(Currency currency) {
            if (!(currency instanceof UltimateCurrency)) {
                return null;
            }
            return getStartingMoney();
        }

        /**
         * Returns whether this account has a set balance for the specified
         * {@link Currency}, with the specified {@link Context}s.
         * <p>
         * <p>If this method returns <code>false</code>, then {@link #getDefaultBalance(Currency)}
         * will be used when retrieving a balance for the specified {@link Currency} with
         * the specified {@link Context}s.</p>
         *
         * @param currency The {@link Currency} to determine if a balance is set for.
         * @param contexts The {@link Context}s to use with the {@link Currency}.
         * @return Whether this account has a set balance for the specified {@link Currency} and {@link Context}s
         */
        @Override
        public boolean hasBalance(Currency currency, Set<Context> contexts) {
            return hasBalance(currency);
        }

        /**
         * Returns whether this account has a set balance for the specified
         * {@link Currency}, with the current active contexts.
         * <p>
         * <p>If this method returns <code>false</code>, then {@link #getDefaultBalance(Currency)}
         * will be used when retrieving a balance for the specifid {@link Currency} with the
         * current active contexts</p>.
         *
         * @param currency The {@link Currency} to determine if a balance is set for.
         * @return Whether this account has a set balance for the speicified {@link Currency} and current active contexts.
         */
        @Override
        public boolean hasBalance(Currency currency) {
            return currency instanceof UltimateCurrency;
        }

        /**
         * Returns a {@link BigDecimal} representative of the balance stored within this
         * {@link Account} for the {@link Currency} given and the set of {@link Context}s.
         * <p>
         * <p>The default result when the account does not have a balance of the
         * given {@link Currency} should be {@link BigDecimal#ZERO}.</p>
         * <p>
         * <p>The balance may be unavailable depending on the set of {@link Context}s used.</p>
         *
         * @param currency a {@link Currency} to check the balance of
         * @param contexts a set of contexts to check the balance against
         * @return he value for the specified {@link Currency} with the specified {@link Context}s.
         */
        @Override
        public BigDecimal getBalance(Currency currency, Set<Context> contexts) {
            return getBalance(currency);
        }

        /**
         * Returns a {@link BigDecimal} representative of the balance stored within this
         * {@link Account} for the {@link Currency} given, with the current active contexts.
         * <p>
         * <p>The default result when the account does not have a balance of the
         * given {@link Currency} will be {@link #getDefaultBalance(Currency)}.</p>
         *
         * @param currency a {@link Currency} to check the balance of
         * @return the value for the specified {@link Currency}.
         */
        @Override
        public BigDecimal getBalance(Currency currency) {
            if (!(currency instanceof UltimateCurrency)) {
                return null;
            }
            return getData().getBigDecimal(uuid.toString(), getStartingMoney());
        }

        /**
         * Returns a {@link Map} of all currently set balances the account holds within
         * the set of {@link Context}s.
         * <p>
         * <p>Amounts may differ depending on the {@link Context}s specified and
         * the implementation. The set of {@link Context}s may be empty.</p>
         * <p>
         * <p>{@link Currency} amounts which are 0 may or may not be included in the
         * returned mapping.</p>
         * <p>
         * <p>Changes to the returned {@link Map} will not be reflected in the underlying
         * {@link Account}.
         * See {@link #setBalance(Currency, BigDecimal, Cause, Set)}  to set values.</p>
         *
         * @param contexts the set of {@link Context}s to use with the speciied amounts.
         * @return {@link Map} of {@link Currency} to {@link BigDecimal} amounts that this
         * account holds.
         */
        @Override
        public Map<Currency, BigDecimal> getBalances(Set<Context> contexts) {
            return getBalances();
        }

        /**
         * Returns a {@link Map} of all currently set balances the account holds within
         * the current active {@link Context}s.2
         * <p>
         * <p>Amounts may differ depending on the {@link Context}s specified and
         * the implementation. The set of {@link Context}s may be empty.</p>
         * <p>
         * <p>{@link Currency} amounts which are 0 may or may not be included in the
         * returned mapping.</p>
         * <p>
         * <p>Changes to the returned {@link Map} will not be reflected in the underlying
         * {@link Account} and may result in runtime exceptions depending on implementation.
         * See {@link #setBalance(Currency, BigDecimal, Cause, Set)}  to set values.</p>
         *
         * @return {@link Map} of {@link Currency} to {@link BigDecimal} amounts that this
         * account holds.
         */
        @Override
        public Map<Currency, BigDecimal> getBalances() {
            Map<Currency, BigDecimal> map = new HashMap<>();
            map.put(currency, getData().getBigDecimal(uuid.toString(), getStartingMoney()));
            return map;
        }

        /**
         * Sets the balance for this account to the specified amount for
         * the specified {@link Currency}, with the specified set of {@link Context}s.
         * <p>
         * <p>Negative balances may or may not be supported depending on
         * the {@link Currency} specified and the implementation.</p>
         *
         * @param currency The {@link Currency} to set the balance for
         * @param amount   The amount to set for the specified {@link Currency}
         * @param cause    The {@link Cause} for the transaction
         * @param contexts The set of {@link Context}s to use with the specified {@link Currency}
         * @return The result of the transaction
         */
        @Override
        public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
            return setBalance(currency, amount, cause);
        }

        /**
         * Sets the balance for this account to the specified amount for the
         * specified {@link Currency}, with the current active {@link Context}s.
         * <p>
         * <p>Negative balances may or may not be supported depending on
         * the {@link Currency} specified and the implementation.</p>
         *
         * @param currency The {@link Currency} to set the balance for
         * @param amount   The amount to set for the specified {@link Currency}
         * @param cause    The {@link Cause} for the transaction
         * @return The result of the transaction
         */
        @Override
        public TransactionResult setBalance(Currency currency, BigDecimal amount, Cause cause) {
            if (!(currency instanceof UltimateCurrency)) {
                return UEconomy.getTransactionResult(this, amount, new HashSet<>(), ResultType.CONTEXT_MISMATCH, TransactionTypes.DEPOSIT);
            }

            if (amount.compareTo(new BigDecimal(0.0D)) == -1) {
                return UEconomy.getTransactionResult(this, amount, new HashSet<>(), ResultType.FAILED, TransactionTypes.DEPOSIT);
            }
            if (getMaximumMoney() != null && getBalance(currency).add(amount).compareTo(getMaximumMoney()) == 1) {
                return UEconomy.getTransactionResult(this, amount, new HashSet<>(), ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.DEPOSIT);
            }
            getData().set(uuid.toString(), amount);
            getData().save();
            return UEconomy.getTransactionResult(this, amount, new HashSet<>(), ResultType.SUCCESS, TransactionTypes.DEPOSIT);
        }

        /**
         * Resets the balances for all {@link Currency}s used on this account to their
         * default values ({@link #getDefaultBalance(Currency)}), using the specified {@link Context}s.
         *
         * @param cause    The {@link Cause} for the transaction
         * @param contexts the {@link Context}s to use when resetting the balances.
         * @return A map of {@link Currency} to {@link TransactionResult}. Each
         * entry represents the result of resetting a particular currency.
         */
        @Override
        public Map<Currency, TransactionResult> resetBalances(Cause cause, Set<Context> contexts) {
            return resetBalances(cause);
        }

        /**
         * Resets the balances for all {@link Currency}s used on this account to
         * their default values ({@link #getDefaultBalance(Currency)}), using the current active {@link Context}.
         *
         * @param cause The {@link Cause} for the transaction
         * @return A map of {@link Currency} to {@link TransactionResult}. Each
         * entry represents the result of resetting a particular currency.
         */
        @Override
        public Map<Currency, TransactionResult> resetBalances(Cause cause) {
            Map<Currency, TransactionResult> map = new HashMap<>();
            map.put(currency, resetBalance(currency, cause));
            return map;
        }

        /**
         * Resets the balance for the specified {@link Currency} to its default value
         * ({@link #getDefaultBalance(Currency)}), using the specified {@link Context}s.
         *
         * @param currency The {@link Currency} to reset the balance for
         * @param cause    The {@link Cause} for the transaction
         * @param contexts The {@link Context}s to use when resetting the balance
         * @return The result of the transaction
         */
        @Override
        public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> contexts) {
            return setBalance(currency, getStartingMoney(), cause, contexts);
        }

        /**
         * Resets the balance for the specified {@link Currency} to its default value
         * ({@link #getDefaultBalance(Currency)}), using the current active {@link Context}s.
         *
         * @param currency The {@link Currency} to reset the balance for
         * @param cause    The {@link Cause} for the transaction
         * @return The result of the transaction
         */
        @Override
        public TransactionResult resetBalance(Currency currency, Cause cause) {
            return setBalance(currency, getStartingMoney(), cause);
        }

        /**
         * Deposits the specified amount of the specified {@link Currency} to this account,
         * using the specified {@link Context}s.
         *
         * @param currency The {@link Currency} to deposit the specified amount for
         * @param amount   The amount to deposit for the specified {@link Currency}.
         * @param cause    The {@link Cause} for the transaction
         * @param contexts the {@link Context}s to use with the specified {@link Currency}
         * @return The result of the transaction
         */
        @Override
        public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
            return setBalance(currency, getBalance(currency).add(amount), cause, contexts);
        }

        /**
         * Deposits the given amount of the specified {@link Currency} to this account,
         * using the current active {@link Context}s.
         *
         * @param currency The {@link Currency} to deposit the specified amount for
         * @param amount   The amount to deposit for the specified {@link Currency}.
         * @param cause    The {@link Cause} for the transaction
         * @return The result of the transaction
         */
        @Override
        public TransactionResult deposit(Currency currency, BigDecimal amount, Cause cause) {
            return setBalance(currency, getBalance(currency).add(amount), cause);
        }

        /**
         * Withdraws the specified amount of the specified {@link Currency} from this account,
         * using the specified {@link Context}s.
         *
         * @param currency The {@link Currency} to deposit the specifie amount for
         * @param amount   The amount to deposit for the specified {@link Currency}
         * @param cause    The {@link Cause} for the transaction
         * @param contexts The {@link Context}s to use with the specified {@link Currency}
         * @return The result of the transaction
         */
        @Override
        public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
            return setBalance(currency, getBalance(currency).subtract(amount), cause, contexts);
        }

        /**
         * Withdraws the specified amount of the specified {@link Currency} from this account,
         * using the current active {@link Context}s.
         *
         * @param currency The {@link Currency} to deposit the specified amount for
         * @param amount   The amount to deposit for the specified {@link Currency}
         * @param cause    The {@link Cause} for the transaction
         * @return The result of the transaction
         */
        @Override
        public TransactionResult withdraw(Currency currency, BigDecimal amount, Cause cause) {
            return setBalance(currency, getBalance(currency).subtract(amount), cause);
        }

        /**
         * Transfers the specified amount of the specified {@link Currency} from this account
         * the destination account, using the specified {@link Context}s.
         * <p>
         * <p>This operation is a merged {@link #withdraw(Currency, BigDecimal, Cause, Set)}  from this account
         * with a {@link #deposit(Currency, BigDecimal, Cause, Set)}  into the specified account.</p>
         *
         * @param to       the Account to transfer the amounts to.
         * @param currency The {@link Currency} to transfer the specified amount for
         * @param amount   The amount to transfer for the specified {@link Currency}
         * @param cause    The {@link Cause} for the transaction
         * @param contexts The {@link Context}s to use with the specified {@link Currency} and account
         * @return a {@link TransferResult} representative of the effects of the
         * operation
         */
        @Override
        public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause, Set<Context> contexts) {
            TransactionResult r1 = withdraw(currency, amount, cause, contexts);
            if (!r1.getResult().equals(ResultType.SUCCESS)) {
                return UEconomy.getTransferResult(this, to, amount, contexts, ResultType.FAILED, TransactionTypes.TRANSFER);
            }
            TransactionResult r2 = to.deposit(currency, amount, cause, contexts);
            return UEconomy.getTransferResult(this, to, amount, contexts, r2.getResult(), TransactionTypes.TRANSFER);
        }

        /**
         * Transfers the specified amount of the specified {@link Currency} from this account
         * the destination account, using the current active {@link Context}s.
         * <p>
         * <p>This operation is a merged {@link #withdraw(Currency, BigDecimal, Cause, Set)} from this account
         * with a {@link #deposit(Currency, BigDecimal, Cause, Set)} into the specified account.</p>
         *
         * @param to       the Account to transfer the amounts to.
         * @param currency The {@link Currency} to transfer the specified amount for
         * @param amount   The amount to transfer for the specified {@link Currency}
         * @param cause    The {@link Cause} for the transaction
         * @return a {@link TransferResult} representative of the effects of the
         * operation
         */
        @Override
        public TransferResult transfer(Account to, Currency currency, BigDecimal amount, Cause cause) {
            TransactionResult r1 = withdraw(currency, amount, cause, new HashSet<>());
            if (!r1.getResult().equals(ResultType.SUCCESS)) {
                return UEconomy.getTransferResult(this, to, amount, new HashSet<>(), ResultType.FAILED, TransactionTypes.TRANSFER);
            }
            TransactionResult r2 = to.deposit(currency, amount, cause, new HashSet<>());
            return UEconomy.getTransferResult(this, to, amount, new HashSet<>(), r2.getResult(), TransactionTypes.TRANSFER);
        }

        /**
         * Returns the identifier associated with this Contextual. Not guaranteed to
         * be human-readable.
         *
         * @return The unique identifier for this subject
         */
        @Override
        public String getIdentifier() {
            return uuid.toString();
        }

        /**
         * Calculate active contexts, using the {@link ContextCalculator}s for the
         * service.
         * <p>
         * <p>The result of these calculations may be cached.</p>
         *
         * @return An immutable set of active contexts
         */
        @Override
        public Set<Context> getActiveContexts() {
            return null;
        }

        /**
         * Gets the unique ID for this object.
         *
         * @return The {@link UUID}
         */
        @Override
        public UUID getUniqueId() {
            return uuid;
        }
    }
}


//    /**
//     * Checks if economy method is enabled.
//     *
//     * @return Success or Failure
//     */
//    @Override
//    public boolean isEnabled() {
//        r.debug("isEnabled - " + r.getUC().isEnabled());
//        return r.getUC().isEnabled();
//    }
//
//    /**
//     * Gets name of economy method
//     *
//     * @return Name of Economy Method
//     */
//    @Override
//    public String getName() {
//        return "UltimateCore";
//    }
//
//    /**
//     * Returns true if the given implementation supports banks.
//     *
//     * @return true if the implementation supports banks
//     */
//    @Override
//    public boolean hasBankSupport() {
//        return false;
//    }
//
//    /**
//     * Some economy plugins round off after a certain number of digits. This function returns the
//     * number of digits the plugin keeps or -1 if no rounding occurs.
//     *
//     * @return number of digits after the decimal point kept
//     */
//    @Override
//    public int fractionalDigits() {
//        return -1;
//    }
//
//    /**
//     * Format amount into a human readable String This provides translation into economy specific
//     * formatting to improve consistency between plugins.
//     *
//     * @param amount to format
//     * @return Human readable string describing amount
//     */
//    @Override
//    public String format(BigDecimal amount) {
//        r.debug("format - " + amount);
//
//        return format.replace("%Amount", r.round(amount, round) + "");
//    }
//
//    /**
//     * Returns the name of the currency in plural form. If the economy being used does not support
//     * currency names then an empty string will be returned.
//     *
//     * @return name of the currency (plural)
//     */
//    @Override
//    public String currencyNamePlural() {
//        return currencyName;
//    }
//
//    /**
//     * Returns the name of the currency in singular form. If the economy being used does not
//     * support currency names then an empty string will be returned.
//     *
//     * @return name of the currency (singular)
//     */
//    @Override
//    public String currencyNameSingular() {
//        return currencyNamePlural;
//    }
//
//    /**
//     * The minimum amount of money a player can have.
//     *
//     * @return the minimum amount
//     */
//    public BigDecimal getMinimumMoney() {
//        return r.getCnfg().getBigDecimal("Economy.minimumMoney");
//    }
//
//    /**
//     * The maximum amount of money a player can have.
//     *
//     * @return the maximum amount, or null when not available.
//     */
//    public BigDecimal getMaximumMoney() {
//        BigDecimal q = r.getCnfg().getBigDecimal("Economy.maximumMoney");
//        return q == -1.0 ? null : q;
//    }
//
//    /**
//     * The amount of money a player starts with
//     *
//     * @return the starting amount
//     */
//    public BigDecimal getStartingMoney() {
//        return r.getCnfg().getBigDecimal("Economy.startingBalance");
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer)} instead.
//     */
//    @Override
//    public boolean hasAccount(String playerName) {
//        Long time = System.currentTimeMillis();
//        r.debug("hasAccount - " + playerName + " - " + getData().contains(playerName));
//        if (!r.isUUID(playerName)) {
//            OfflinePlayer player = r.searchGameProfile(playerName);
//            if (player.getUniqueId() != null && (player.hasPlayedBefore() || player.isOnline())) {
//                playerName = player.getUniqueId().toString();
//            }
//        }
//        boolean data = getData().contains(playerName);
//        r.debug("Took: " + (System.currentTimeMillis() - time));
//        return data;
//    }
//
//    /**
//     * Checks if this player has an account on the server yet This will always return true if the
//     * player has joined the server at least once as all major economy plugins auto-generate a
//     * player account when the player joins the server
//     *
//     * @param player to check
//     * @return if the player has an account
//     */
//    @Override
//    public boolean hasAccount(OfflinePlayer player) {
//        return hasAccount(player.getUniqueId().toString());
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer, String)} instead.
//     */
//    @Override
//    public boolean hasAccount(String playerName, String worldName) {
//        return hasAccount(playerName);
//    }
//
//    /**
//     * Checks if this player has an account on the server yet on the given world This will always
//     * return true if the player has joined the server at least once as all major economy plugins
//     * auto-generate a player account when the player joins the server
//     *
//     * @param player    to check in the world
//     * @param worldName world-specific account
//     * @return if the player has an account
//     */
//    @Override
//    public boolean hasAccount(OfflinePlayer player, String worldName) {
//        return hasAccount(player);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer)} instead.
//     */
//    @Override
//    public BigDecimal getBalance(String playerName) {
//        Long time = System.currentTimeMillis();
//        if (!r.isUUID(playerName)) {
//            OfflinePlayer player = r.searchGameProfile(playerName);
//            if (player.getUniqueId() != null && (player.hasPlayedBefore() || player.isOnline())) {
//                playerName = player.getUniqueId().toString();
//            }
//        }
//        if (!hasAccount(playerName)) {
//            createPlayerAccount(playerName);
//        }
//        r.debug("getBalance - " + playerName + " - " + getData().getBigDecimal(playerName));
//        BigDecimal data = getData().getBigDecimal(playerName);
//        r.debug("Took: " + (System.currentTimeMillis() - time));
//        return data;
//    }
//
//    /**
//     * Gets balance of a player
//     *
//     * @param player of the player
//     * @return Amount currently held in players account
//     */
//    @Override
//    public BigDecimal getBalance(OfflinePlayer player) {
//        if (!hasAccount(player)) {
//            createPlayerAccount(player);
//        }
//        return getBalance(player.getUniqueId().toString());
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer, String)} instead.
//     */
//    @Override
//    public BigDecimal getBalance(String playerName, String world) {
//        return getBalance(playerName);
//    }
//
//    /**
//     * Gets balance of a player on the specified world. IMPLEMENTATION SPECIFIC - if an economy
//     * plugin does not support this the global balance will be returned.
//     *
//     * @param player to check
//     * @param world  name of the world
//     * @return Amount currently held in players account
//     */
//    @Override
//    public BigDecimal getBalance(OfflinePlayer player, String world) {
//        return getBalance(player);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #has(OfflinePlayer, BigDecimal)} instead.
//     */
//    @Override
//    public boolean has(String playerName, BigDecimal amount) {
//        Long time = System.currentTimeMillis();
//        if (!r.isUUID(playerName)) {
//            OfflinePlayer player = r.searchGameProfile(playerName);
//            if (player.getUniqueId() != null && (player.hasPlayedBefore() || player.isOnline())) {
//                playerName = player.getUniqueId().toString();
//            }
//        }
//        boolean a = getBalance(playerName) >= amount;
//        r.debug("Took: " + (System.currentTimeMillis() - time));
//        return a;
//    }
//
//    /**
//     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
//     *
//     * @param player to check
//     * @param amount to check for
//     * @return True if <b>player</b> has <b>amount</b>, False else wise
//     */
//    @Override
//    public boolean has(OfflinePlayer player, BigDecimal amount) {
//        return getBalance(player) >= amount;
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use @{link {@link #has(OfflinePlayer, String, BigDecimal)}
//     * instead.
//     */
//    @Override
//    public boolean has(String playerName, String worldName, BigDecimal amount) {
//        return getBalance(playerName) >= amount;
//    }
//
//    /**
//     * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
//     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will
//     * be returned.
//     *
//     * @param player    to check
//     * @param worldName to check with
//     * @param amount    to check for
//     * @return True if <b>player</b> has <b>amount</b>, False else wise
//     */
//    @Override
//    public boolean has(OfflinePlayer player, String worldName, BigDecimal amount) {
//        return getBalance(player) >= amount;
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, BigDecimal)} instead.
//     */
//    @Override
//    public EconomyResponse withdrawPlayer(String playerName, BigDecimal amount) {
//        return withdrawPlayer(playerName, amount, false);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, BigDecimal)} instead.
//     */
//    public EconomyResponse withdrawPlayer(String playerName, BigDecimal amount, boolean force) {
//        Long time = System.currentTimeMillis();
//
//        if (!r.isUUID(playerName)) {
//            OfflinePlayer player = r.searchGameProfile(playerName);
//            if (player.getUniqueId() != null && (player.hasPlayedBefore() || player.isOnline())) {
//                playerName = player.getUniqueId().toString();
//            }
//        }
//        if (!hasAccount(playerName)) {
//            createPlayerAccount(playerName);
//        }
//        r.debug("withdrawPlayer - " + playerName + " - " + amount);
//        if (amount < 0.0D) {
//            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative " + "funds");
//        }
//        if (!getData().contains(playerName)) {
//            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "This player has no account");
//        }
//        if (getBalance(playerName) - amount < (force ? getMinimumMoney() : 0)) {
//            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "This player has too less money");
//        }
//        getData().set(playerName, getBalance(playerName) - amount);
//        getData().save();
//        r.debug("Took: " + (System.currentTimeMillis() - time));
//        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
//    }
//
//    /**
//     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
//     *
//     * @param player to withdraw from
//     * @param amount Amount to withdraw
//     * @return Detailed response of transaction
//     */
//    @Override
//    public EconomyResponse withdrawPlayer(OfflinePlayer player, BigDecimal amount) {
//        return withdrawPlayer(player.getUniqueId().toString(), amount);
//    }
//
//    /**
//     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
//     *
//     * @param player to withdraw from
//     * @param amount Amount to withdraw
//     * @return Detailed response of transaction
//     */
//    public EconomyResponse withdrawPlayer(OfflinePlayer player, BigDecimal amount, boolean force) {
//        return withdrawPlayer(player.getUniqueId().toString(), amount, force);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #withdrawPlayer(OfflinePlayer, String, BigDecimal)}
//     * instead.
//     */
//    @Override
//    public EconomyResponse withdrawPlayer(String playerName, String worldName, BigDecimal amount) {
//        return withdrawPlayer(playerName, amount);
//    }
//
//    /**
//     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
//     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will
//     * be returned.
//     *
//     * @param player    to withdraw from
//     * @param worldName - name of the world
//     * @param amount    Amount to withdraw
//     * @return Detailed response of transaction
//     */
//    @Override
//    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, BigDecimal amount) {
//        return withdrawPlayer(player.getUniqueId().toString(), amount);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, BigDecimal)} instead.
//     */
//    @Override
//    public EconomyResponse depositPlayer(String playerName, BigDecimal amount) {
//        Long time = System.currentTimeMillis();
//
//        if (!r.isUUID(playerName)) {
//            OfflinePlayer player = r.searchGameProfile(playerName);
//            if (player.getUniqueId() != null && (player.hasPlayedBefore() || player.isOnline())) {
//                playerName = player.getUniqueId().toString();
//            }
//        }
//        if (!hasAccount(playerName)) {
//            createPlayerAccount(playerName);
//        }
//        r.debug("depositPlayer - " + playerName + " - " + amount);
//        if (amount < 0.0D) {
//            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative " + "funds");
//        }
//        if (!getData().contains(playerName)) {
//            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "This player has no account");
//        }
//        if (getMaximumMoney() != null && getBalance(playerName) + amount > getMaximumMoney()) {
//            return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE, "This player has too much money");
//        }
//        getData().set(playerName, getBalance(playerName) + amount);
//        getData().save();
//        r.debug("Took: " + (System.currentTimeMillis() - time));
//        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
//    }
//
//    /**
//     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
//     *
//     * @param player to deposit to
//     * @param amount Amount to deposit
//     * @return Detailed response of transaction
//     */
//    @Override
//    public EconomyResponse depositPlayer(OfflinePlayer player, BigDecimal amount) {
//        return depositPlayer(player.getUniqueId().toString(), amount);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {@link #depositPlayer(OfflinePlayer, String, BigDecimal)}
//     * instead.
//     */
//    @Override
//    public EconomyResponse depositPlayer(String playerName, String worldName, BigDecimal amount) {
//        return depositPlayer(playerName, amount);
//    }
//
//    /**
//     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an
//     * economy plugin does not support this the global balance will be returned.
//     *
//     * @param player    to deposit to
//     * @param worldName name of the world
//     * @param amount    Amount to deposit
//     * @return Detailed response of transaction
//     */
//    @Override
//    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, BigDecimal amount) {
//        return depositPlayer(player.getUniqueId().toString(), amount);
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {{@link #createBank(String, OfflinePlayer)} instead.
//     */
//    @Override
//    public EconomyResponse createBank(String name, String player) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Creates a bank account with the specified name and the player as the owner
//     *
//     * @param name   of account
//     * @param player the account should be linked to
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse createBank(String name, OfflinePlayer player) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Deletes a bank account with the specified name.
//     *
//     * @param name of the back to delete
//     * @return if the operation completed successfully
//     */
//    @Override
//    public EconomyResponse deleteBank(String name) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Returns the amount the bank has
//     *
//     * @param name of the account
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse bankBalance(String name) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE
//     * AMOUNTS
//     *
//     * @param name   of the account
//     * @param amount to check for
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse bankHas(String name, BigDecimal amount) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
//     *
//     * @param name   of the account
//     * @param amount to withdraw
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse bankWithdraw(String name, BigDecimal amount) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
//     *
//     * @param name   of the account
//     * @param amount to deposit
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse bankDeposit(String name, BigDecimal amount) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {{@link #isBankOwner(String, OfflinePlayer)} instead.
//     */
//    @Override
//    public EconomyResponse isBankOwner(String name, String playerName) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Check if a player is the owner of a bank account
//     *
//     * @param name   of the account
//     * @param player to check for ownership
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {{@link #isBankMember(String, OfflinePlayer)} instead.
//     */
//    @Override
//    public EconomyResponse isBankMember(String name, String playerName) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Check if the player is a member of the bank account
//     *
//     * @param name   of the account
//     * @param player to check membership
//     * @return EconomyResponse Object
//     */
//    @Override
//    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
//        return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "UltimateCore does not " + "support bank accounts!");
//    }
//
//    /**
//     * Gets the list of banks
//     *
//     * @return the List of Banks
//     */
//    @Override
//    public List<String> getBanks() {
//        return new ArrayList<>();
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer)} instead.
//     */
//    @Override
//    public boolean createPlayerAccount(String playerName) {
//        Long time = System.currentTimeMillis();
//        r.debug("createPlayerAccount - " + playerName);
//
//        if (!r.isUUID(playerName)) {
//            OfflinePlayer player = r.searchGameProfile(playerName);
//            if (player.getUniqueId() != null && (player.hasPlayedBefore() || player.isOnline())) {
//                playerName = player.getUniqueId().toString();
//            }
//        }
//        if (getData().contains(playerName)) {
//            return false;
//        }
//        getData().set(playerName, r.getCnfg().getBigDecimal("Economy.startingBalance"));
//        getData().save();
//        r.debug("Took: " + (System.currentTimeMillis() - time));
//        return true;
//    }
//
//    /**
//     * Attempts to create a player account for the given player
//     *
//     * @param player OfflinePlayer
//     * @return if the account creation was successful
//     */
//    @Override
//    public boolean createPlayerAccount(OfflinePlayer player) {
//        return createPlayerAccount(player.getUniqueId().toString());
//    }
//
//    /**
//     * @deprecated As of VaultAPI 1.4 use {{@link #createPlayerAccount(OfflinePlayer, String)}
//     * instead.
//     */
//    @Override
//    public boolean createPlayerAccount(String playerName, String worldName) {
//        return createPlayerAccount(playerName);
//    }
//
//    /**
//     * Attempts to create a player account for the given player on the specified world
//     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will
//     * be returned.
//     *
//     * @param player    OfflinePlayer
//     * @param worldName String name of the world
//     * @return if the account creation was successful
//     */
//    @Override
//    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
//        return createPlayerAccount(player.getUniqueId().toString());
//    }
//

