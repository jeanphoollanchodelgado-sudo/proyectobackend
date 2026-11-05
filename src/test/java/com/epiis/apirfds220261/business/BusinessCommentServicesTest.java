package com.epiis.apirfds220261.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epiis.apirfds220261.dto.request.RequestComplaintCommentInsert;
import com.epiis.apirfds220261.dto.request.RequestSuggestionCommentInsert;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntityComplaintComment;
import com.epiis.apirfds220261.entity.EntitySuggestion;
import com.epiis.apirfds220261.entity.EntitySuggestionComment;
import com.epiis.apirfds220261.entity.EntityUser;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositoryComplaintComment;
import com.epiis.apirfds220261.repository.RepositorySuggestion;
import com.epiis.apirfds220261.repository.RepositorySuggestionComment;
import com.epiis.apirfds220261.repository.RepositoryUser;

@ExtendWith(MockitoExtension.class)
class BusinessCommentServicesTest {

    @Mock
    private RepositoryComplaint complaintRepository;
    @Mock
    private RepositoryComplaintComment complaintCommentRepository;
    @Mock
    private RepositorySuggestion suggestionRepository;
    @Mock
    private RepositorySuggestionComment suggestionCommentRepository;
    @Mock
    private RepositoryUser userRepository;

    @InjectMocks
    private BusinessComments businessComments;

    @Test
    void insertsAndListsComplaintCommentsForGuestAndUser() {
        RequestComplaintCommentInsert request = complaintRequest(" missing ", null);
        when(complaintRepository.findByCode("missing")).thenReturn(null);
        assertEquals("error", businessComments.insertComplaintComment(request).getType());
        verify(complaintCommentRepository, never()).save(any());

        EntityComplaint complaint = new EntityComplaint();
        complaint.setIdComplaint("complaint");
        complaint.setCode("C123");
        when(complaintRepository.findByCode("missing")).thenReturn(complaint);
        request.setIdUser("   ");
        assertEquals("success", businessComments.insertComplaintComment(request).getType());

        request.setIdUser("unknown");
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());
        assertEquals("error", businessComments.insertComplaintComment(request).getType());

        EntityUser user = new EntityUser();
        when(userRepository.findById("known")).thenReturn(Optional.of(user));
        request.setIdUser(" known ");
        assertEquals("success", businessComments.insertComplaintComment(request).getType());

        when(complaintRepository.findByCode("not-found")).thenReturn(null);
        assertEquals(
            "error",
            businessComments.getComplaintComments("not-found").getType()
        );

        when(complaintRepository.findByCode("C123")).thenReturn(complaint);
        when(complaintCommentRepository.findListByIdComplaint("complaint"))
            .thenReturn(List.of());
        var empty = businessComments.getComplaintComments("C123");
        assertEquals("success", empty.getType());
        assertEquals(1, empty.getListMessage().size());

        EntityComplaintComment anonymousComment = new EntityComplaintComment();
        anonymousComment.setIdComplaintcomment("comment-1");
        anonymousComment.setDescription("anonymous");
        anonymousComment.setCreatedAt(Instant.parse("2026-01-02T03:04:05Z"));
        EntityComplaintComment namedComment = new EntityComplaintComment();
        namedComment.setIdComplaintcomment("comment-2");
        namedComment.setIdUser("known");
        namedComment.setDescription("named");
        namedComment.setCreatedAt(Instant.parse("2026-01-02T03:04:05Z"));
        EntityUser commenter = new EntityUser();
        commenter.setFirstName("Ada");
        commenter.setSurName("Lovelace");
        namedComment.setParentUser(commenter);
        when(complaintCommentRepository.findListByIdComplaint("complaint"))
            .thenReturn(List.of(anonymousComment, namedComment));
        var listed = businessComments.getComplaintComments("C123");
        assertEquals(2, listed.getListComplaintComment().size());
        assertEquals(
            "",
            listed.getListComplaintComment().getFirst().get("personFullName")
        );
        assertEquals(
            "Ada Lovelace",
            listed.getListComplaintComment().get(1).get("personFullName")
        );
    }

    @Test
    void insertsAndListsSuggestionCommentsForGuestAndUser() {
        RequestSuggestionCommentInsert request = suggestionRequest(" missing ", null);
        when(suggestionRepository.findByCode("missing")).thenReturn(null);
        assertEquals("error", businessComments.insertSuggestionComment(request).getType());
        verify(suggestionCommentRepository, never()).save(any());

        EntitySuggestion suggestion = new EntitySuggestion();
        suggestion.setIdSuggestion("suggestion");
        suggestion.setCode("S123");
        when(suggestionRepository.findByCode("missing")).thenReturn(suggestion);
        request.setIdUser(" ");
        assertEquals("success", businessComments.insertSuggestionComment(request).getType());

        request.setIdUser("unknown");
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());
        assertEquals("error", businessComments.insertSuggestionComment(request).getType());

        when(userRepository.findById("known")).thenReturn(Optional.of(new EntityUser()));
        request.setIdUser(" known ");
        assertEquals("success", businessComments.insertSuggestionComment(request).getType());

        when(suggestionRepository.findByCode("not-found")).thenReturn(null);
        assertEquals(
            "error",
            businessComments.getSuggestionComments("not-found").getType()
        );

        when(suggestionRepository.findByCode("S123")).thenReturn(suggestion);
        when(suggestionCommentRepository.findListByIdSuggestion("suggestion"))
            .thenReturn(List.of());
        var empty = businessComments.getSuggestionComments("S123");
        assertEquals("success", empty.getType());
        assertEquals(1, empty.getListMessage().size());

        EntitySuggestionComment comment = new EntitySuggestionComment();
        comment.setIdSuggestioncomment("comment");
        comment.setDescription("idea");
        comment.setCreatedAt(Instant.parse("2026-01-02T03:04:05Z"));
        when(suggestionCommentRepository.findListByIdSuggestion("suggestion"))
            .thenReturn(List.of(comment));
        var listed = businessComments.getSuggestionComments("S123");
        assertEquals(1, listed.getListSuggestionComment().size());
        assertEquals("", listed.getListSuggestionComment().getFirst().get("idUser"));
    }

    private static RequestComplaintCommentInsert complaintRequest(
        String code,
        String idUser
    ) {
        RequestComplaintCommentInsert request = new RequestComplaintCommentInsert();
        request.setCode(code);
        request.setIdUser(idUser);
        request.setDescription("  details  ");
        return request;
    }

    private static RequestSuggestionCommentInsert suggestionRequest(
        String code,
        String idUser
    ) {
        RequestSuggestionCommentInsert request = new RequestSuggestionCommentInsert();
        request.setCode(code);
        request.setIdUser(idUser);
        request.setDescription("  details  ");
        return request;
    }
}
