package com.epiis.apirfds220261.business;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.epiis.apirfds220261.entity.EntityUser;
import com.epiis.apirfds220261.generic.ResponseGeneric;
import com.epiis.apirfds220261.repository.RepositoryUser;

final class AbstractBusinessComment<P, C> {

    private final RepositoryUser repositoryUser;
    private final CommentOperations<P, C> operations;

    AbstractBusinessComment(
        RepositoryUser repositoryUser,
        CommentOperations<P, C> operations
    ) {
        this.repositoryUser = repositoryUser;
        this.operations = operations;
    }

    protected final <R extends ResponseGeneric> R insertComment(
        String code,
        String userId,
        String description,
        Supplier<R> responseFactory,
        BiConsumer<R, String> responseIdSetter
    ) {
        R response = responseFactory.get();
        P parent = operations.findParent().apply(code.trim());
        if (parent == null) {
            response.listMessage.add(
                operations.missingParentMessage().apply(code.trim())
            );
            return response;
        }

        String normalizedUserId = userId == null ? null : userId.trim();
        if (normalizedUserId != null && !normalizedUserId.isEmpty()) {
            Optional<EntityUser> user = repositoryUser.findById(normalizedUserId);
            if (user.isEmpty()) {
                response.listMessage.add("El usuario indicado no está registrado.");
                return response;
            }
        } else {
            normalizedUserId = null;
        }

        C comment = operations.entityFactory().create(
            operations.parentId().apply(parent),
            normalizedUserId,
            description.trim(),
            Instant.now()
        );
        operations.saveComment().accept(comment);
        responseIdSetter.accept(response, operations.commentId().apply(comment));
        response.success();
        response.listMessage.add("Registro realizado correctamente.");
        return response;
    }

    protected final <R extends ResponseGeneric> R getComments(
        String code,
        Supplier<R> responseFactory,
        BiConsumer<R, Map<String, String>> responseCommentAdder
    ) {
        R response = responseFactory.get();
        P parent = operations.findParent().apply(code);
        if (parent == null) {
            response.listMessage.add(operations.missingParentMessage().apply(code));
            return response;
        }

        List<C> comments = operations.findComments().apply(parent);
        for (C comment : comments) {
            responseCommentAdder.accept(
                response,
                CommentResponseMapper.toMap(
                    operations.responseIdKey(),
                    operations.commentId().apply(comment),
                    operations.parentCode().apply(parent),
                    operations.commentUserId().apply(comment),
                    operations.commentUser().apply(comment),
                    operations.commentDescription().apply(comment),
                    operations.commentCreatedAt().apply(comment)
                )
            );
        }

        response.success();
        if (comments.isEmpty()) {
            response.listMessage.add(operations.emptyCommentsMessage());
        }
        return response;
    }
}
