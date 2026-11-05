package com.epiis.apirfds220261.business;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

record CommentOperations<P, C>(
    Function<String, P> findParent,
    Function<P, String> parentId,
    Function<P, String> parentCode,
    Function<P, List<C>> findComments,
    CommentEntityFactory<C> entityFactory,
    Consumer<C> saveComment,
    Function<C, String> commentId,
    Function<C, String> commentUserId,
    Function<C, com.epiis.apirfds220261.entity.EntityUser> commentUser,
    Function<C, String> commentDescription,
    Function<C, Instant> commentCreatedAt,
    String responseIdKey,
    Function<String, String> missingParentMessage,
    String emptyCommentsMessage
) {
}
