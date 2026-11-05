package com.epiis.apirfds220261.business;



import org.springframework.stereotype.Service;

import com.epiis.apirfds220261.dto.request.RequestComplaintCommentInsert;
import com.epiis.apirfds220261.dto.request.RequestSuggestionCommentInsert;
import com.epiis.apirfds220261.dto.response.ResponseComplaintCommentGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseComplaintCommentInsert;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionCommentGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseSuggestionCommentInsert;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntityComplaintComment;
import com.epiis.apirfds220261.entity.EntitySuggestion;
import com.epiis.apirfds220261.entity.EntitySuggestionComment;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositoryComplaintComment;
import com.epiis.apirfds220261.repository.RepositorySuggestion;
import com.epiis.apirfds220261.repository.RepositorySuggestionComment;
import com.epiis.apirfds220261.repository.RepositoryUser;

@Service
public class BusinessComments {

    private final AbstractBusinessComment<EntityComplaint, EntityComplaintComment>
        complaintComments;
    private final AbstractBusinessComment<EntitySuggestion, EntitySuggestionComment>
        suggestionComments;

    public BusinessComments(
        RepositoryComplaint complaintRepository,
        RepositoryComplaintComment complaintCommentRepository,
        RepositorySuggestion suggestionRepository,
        RepositorySuggestionComment suggestionCommentRepository,
        RepositoryUser userRepository
    ) {
        complaintComments = new AbstractBusinessComment<>(
            userRepository,
            new CommentOperations<>(
                complaintRepository::findByCode,
                EntityComplaint::getIdComplaint,
                EntityComplaint::getCode,
                complaint -> complaintCommentRepository
                    .findListByIdComplaint(complaint.getIdComplaint()),
                new CommentEntityFactory<>(
                    EntityComplaintComment::new,
                    EntityComplaintComment::setIdComplaintcomment,
                    EntityComplaintComment::setIdComplaint,
                    EntityComplaintComment::setIdUser,
                    EntityComplaintComment::setDescription,
                    EntityComplaintComment::setCreatedAt,
                    EntityComplaintComment::setUpdatedAt
                ),
                complaintCommentRepository::save,
                EntityComplaintComment::getIdComplaintcomment,
                EntityComplaintComment::getIdUser,
                EntityComplaintComment::getParentUser,
                EntityComplaintComment::getDescription,
                EntityComplaintComment::getCreatedAt,
                "idComplaintcomment",
                code -> "El código de seguimiento \"" + code
                    + "\" no está registrado.",
                "La queja no tiene comentarios registrados."
            )
        );
        suggestionComments = new AbstractBusinessComment<>(
            userRepository,
            new CommentOperations<>(
                suggestionRepository::findByCode,
                EntitySuggestion::getIdSuggestion,
                EntitySuggestion::getCode,
                suggestion -> suggestionCommentRepository
                    .findListByIdSuggestion(suggestion.getIdSuggestion()),
                new CommentEntityFactory<>(
                    EntitySuggestionComment::new,
                    EntitySuggestionComment::setIdSuggestioncomment,
                    EntitySuggestionComment::setIdSuggestion,
                    EntitySuggestionComment::setIdUser,
                    EntitySuggestionComment::setDescription,
                    EntitySuggestionComment::setCreatedAt,
                    EntitySuggestionComment::setUpdatedAt
                ),
                suggestionCommentRepository::save,
                EntitySuggestionComment::getIdSuggestioncomment,
                EntitySuggestionComment::getIdUser,
                EntitySuggestionComment::getParentUser,
                EntitySuggestionComment::getDescription,
                EntitySuggestionComment::getCreatedAt,
                "idSuggestioncomment",
                code -> "El código de seguimiento \"" + code
                    + "\" no está registrado.",
                "La queja no tiene comentarios registrados."
            )
        );
    }

    public ResponseComplaintCommentInsert insertComplaintComment(
        RequestComplaintCommentInsert request
    ) {
        return complaintComments.insertComment(
            request.getCode(),
            request.getIdUser(),
            request.getDescription(),
            ResponseComplaintCommentInsert::new,
            ResponseComplaintCommentInsert::setIdComplaintcomment
        );
    }

    public ResponseComplaintCommentGetByCode getComplaintComments(String code) {
        return complaintComments.getComments(
            code,
            ResponseComplaintCommentGetByCode::new,
            (response, comment) -> response.getListComplaintComment().add(comment)
        );
    }

    public ResponseSuggestionCommentInsert insertSuggestionComment(
        RequestSuggestionCommentInsert request
    ) {
        return suggestionComments.insertComment(
            request.getCode(),
            request.getIdUser(),
            request.getDescription(),
            ResponseSuggestionCommentInsert::new,
            ResponseSuggestionCommentInsert::setIdSuggestioncomment
        );
    }

    public ResponseSuggestionCommentGetByCode getSuggestionComments(String code) {
        return suggestionComments.getComments(
            code,
            ResponseSuggestionCommentGetByCode::new,
            (response, comment) -> response.getListSuggestionComment().add(comment)
        );
    }

}
