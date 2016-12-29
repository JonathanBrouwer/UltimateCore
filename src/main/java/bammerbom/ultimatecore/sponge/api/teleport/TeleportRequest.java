package bammerbom.ultimatecore.sponge.api.teleport;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;

import java.util.List;
import java.util.function.Consumer;

public interface TeleportRequest {

    void start();

    void cancel();

    List<Entity> getEntities();

    Transform getTarget();

    Consumer<TeleportRequest> getCompleteConsumer();
}
