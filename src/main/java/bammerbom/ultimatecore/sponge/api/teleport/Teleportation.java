package bammerbom.ultimatecore.sponge.api.teleport;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Teleportation {

    /**
     * This will init handling the teleportation.
     * This means all registered handlers will be runned and then the targets will be teleported.
     * <p>
     * An {@link IllegalStateException} is thrown when the teleportation has already started.
     */
    void start();

    /**
     * Pause the teleportation
     * This will wait with executing the remaining handlers until the {@link #resume()} method is called.
     * <p>
     * An {@link IllegalStateException} is thrown when the teleportation has not been started yet, is already paused, or has already been finished.
     */
    void pause();

    /**
     * Resumes the teleportation after it has been cancelled.
     * This will execute all handlers which have not been executed yet.
     * <p>
     * An {@link IllegalStateException} is thrown when the teleportation has not been paused.
     */
    void resume();

    /**
     * This will cancel the teleportation.
     * <p>
     * An {@link IllegalStateException} is thrown when the teleportation has already been cancelled.
     */
    void cancel(String reason);

    /**
     * This will skip all remaining handlers and force complete the teleportation.
     * This means the user will be teleported no matter what other handlers think!
     */
    void complete();

    /**
     * Returns the current state.
     * 0 = Not started, 1 = Executing, 2 = Paused or waiting, 3 = Cancelled, 4 = Completed
     *
     * @return The current state.
     */
    int getState();

    /**
     * Returns the command source to send error messages to, when something goes wrong.
     * This can be absent.
     */
    Optional<CommandSource> getCommandSource();

    /**
     * Returns a list of entities which will be teleported to the {@link #getTarget() target} when the teleportation has been completed.
     *
     * @return A list of entities.
     */
    List<Entity> getEntities();

    /**
     * The location which the {@link #getEntities() entities} will be teleported to when the teleportation has been completed.
     *
     * @return The location
     */
    Supplier<Transform<World>> getTarget();

    /**
     * Returns the consumer which will be run after the teleportation has been completed.
     *
     * @return The consumer
     */
    Consumer<Teleportation> getCompleteConsumer();

    /**
     * Returns the consumer which will be run after the teleportation has been cancelled.
     * The arguments for the consumer are the teleportation and the reason of failure.
     *
     * @return The consumer
     */
    BiConsumer<Teleportation, String> getCancelConsumer();

    /**
     * Whether the teleportation should be safe
     */
    boolean isSafe();

    /**
     * Get the utc timestamp of when this teleportation was created.
     *
     * @return The utc timestamp of when this teleportation was created
     */
    Long getCreationTime();
}
