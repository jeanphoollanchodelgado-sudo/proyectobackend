package com.epiis.apirfds220261.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epiis.apirfds220261.dto.request.RequestComplaintInsert;
import com.epiis.apirfds220261.dto.response.ResponseComplaintGetByCode;
import com.epiis.apirfds220261.dto.response.ResponseComplaintInsert;
import com.epiis.apirfds220261.entity.EntityComplaint;
import com.epiis.apirfds220261.entity.EntityComplaintFile;
import com.epiis.apirfds220261.helper.GenericHelper;
import com.epiis.apirfds220261.repository.RepositoryComplaint;
import com.epiis.apirfds220261.repository.RepositoryComplaintFile;
import com.epiis.apirfds220261.staticdata.EnumProcess;

@Service
public class BusinessComplaint {

    private final RepositoryComplaint repositoryComplaint;
    private final RepositoryComplaintFile repositoryComplaintFile;

    public BusinessComplaint(
        RepositoryComplaint repositoryComplaint,
        RepositoryComplaintFile repositoryComplaintFile
    ) {
        this.repositoryComplaint = repositoryComplaint;
        this.repositoryComplaintFile = repositoryComplaintFile;
    }

    public ResponseComplaintInsert insert(RequestComplaintInsert request) throws IOException {
        ResponseComplaintInsert response = new ResponseComplaintInsert();

        EntityComplaint entityComplaint = new EntityComplaint();
        String codeTemp = GenericHelper.followCodeGeneration();

        entityComplaint.setIdComplaint(UUID.randomUUID().toString());
        entityComplaint.setIdOffice(request.getIdOffice());
        entityComplaint.setIdProfessor(request.getIdProfessor());
        entityComplaint.setCode(codeTemp);
        entityComplaint.setComplaintFullName(request.getComplaintFullName().trim());
        entityComplaint.setPersonFullName(request.getPersonFullName().trim());
        entityComplaint.setDescription(request.getDescription().trim());
        entityComplaint.setIssueDate(request.getIssueDate());
        entityComplaint.setStatus(EnumProcess.PENDING.toString());
        Instant now = Instant.now();
        entityComplaint.setCreatedAt(now);
        entityComplaint.setUpdatedAt(now);
        repositoryComplaint.save(entityComplaint);

        if (request.getFiles() != null) {
            Path basePath = Paths.get("storage/complaintfile");

            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }

            List<EntityComplaintFile> listEntityComplaintFile = new ArrayList<>();

            for (var file : request.getFiles()) {
                EntityComplaintFile entityComplaintFile = new EntityComplaintFile();

                entityComplaintFile.setIdComplaintfile(UUID.randomUUID().toString());
                entityComplaintFile.setIdComplaint(entityComplaint.getIdComplaint());
                entityComplaintFile.setName(file.getOriginalFilename());
                entityComplaintFile.setExtension(
                    entityComplaintFile.getName()
                        .substring(entityComplaintFile.getName().lastIndexOf(".") + 1)
                        .toLowerCase()
                );
                entityComplaintFile.setCreatedAt(entityComplaint.getCreatedAt());
                entityComplaintFile.setUpdatedAt(entityComplaint.getCreatedAt());

                listEntityComplaintFile.add(entityComplaintFile);

                Files.copy(
                    file.getInputStream(),
                    basePath.resolve(
                        entityComplaintFile.getIdComplaintfile()
                            + "."
                            + entityComplaintFile.getExtension()
                    )
                );
            }

            repositoryComplaintFile.saveAll(listEntityComplaintFile);
        }

        response.setCode(codeTemp);
        response.success();
        response.listMessage.add("Registro realizado correctamente.");

        return response;
    }

    @Transactional(readOnly = true)
    public ResponseComplaintGetByCode getByCode(String code) {
        ResponseComplaintGetByCode response = new ResponseComplaintGetByCode();
        EntityComplaint complaint = repositoryComplaint.findByCode(code);

        if (complaint == null) {
            response.error();
            response.listMessage.add("Que no existe el registro buscado");
            return response;
        }

        response.setStatus(complaint.getStatus());
        response.setIdComplaint(complaint.getIdComplaint());
        response.setDescription(complaint.getDescription());
        response.setCreatedAt(complaint.getCreatedAt());

        if (complaint.getParentOffice() != null) {
            response.setOfficeName(complaint.getParentOffice().getName());
        }

        if (complaint.getParentProfessor() != null) {
            response.setProfessorName(
                complaint.getParentProfessor().getFirstName()
                    + " "
                    + complaint.getParentProfessor().getSurName()
            );
        }

        response.success();
        return response;
    }
}