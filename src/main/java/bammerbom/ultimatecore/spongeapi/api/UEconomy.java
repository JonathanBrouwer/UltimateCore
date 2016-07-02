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

