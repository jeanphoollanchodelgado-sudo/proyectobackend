package com.epiis.apirfds220261.business;

import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

record CommentEntityFactory<C>(
    Supplier<C> factory,
    BiConsumer<C, String> idSetter,
    BiConsumer<C, String> parentSetter,
    BiConsumer<C, String> userSetter,
    BiConsumer<C, String> descriptionSetter,
    BiConsumer<C, Instant> createdAtSetter,
    BiConsumer<C, Instant> updatedAtSetter
) {
    C create(String parentId, String userId, String description, Instant now) {
        C comment = factory.get();
        idSetter.accept(comment, UUID.randomUUID().toString());
        parentSetter.accept(comment, parentId);
        userSetter.accept(comment, userId);
        descriptionSetter.accept(comment, description);
        createdAtSetter.accept(comment, now);
        updatedAtSetter.accept(comment, now);
        return comment;
    }
}
