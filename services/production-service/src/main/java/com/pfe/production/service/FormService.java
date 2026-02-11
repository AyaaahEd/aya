package com.pfe.production.service;

import com.pfe.production.domain.Form;
import com.pfe.production.repository.FormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public FormService(FormRepository formRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.formRepository = formRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Transactional
    public Form createForm(com.pfe.production.dto.CreateFormRequest request) {
        // Validation is handled by Jakarta Validation annotations in DTO

        Form form = new Form();
        form.setThumbnail(request.thumbnail());
        form.setRepetition(request.repetition());
        form.setSize(request.size());
        form.setCapacityTime(request.capacityTime());
        form.setSteps(request.steps());
        form.setOrderItemIds(request.orderItemIds());
        form.setConfigurationProperties(request.configurationProperties());
        form.setReprint(request.reprint());
        form.setState(Form.FormState.DRAFT); // Default state

        form.setFormNumber(sequenceGeneratorService.generateFormNumber());
        form.setCreatedAt(LocalDateTime.now());
        form.setUpdatedAt(LocalDateTime.now());

        if (form.getDeleted() == null) {
            form.setDeleted(false);
        }

        return formRepository.save(form);
    }

    public List<Form> getAllForms() {
        return formRepository.findAll().stream()
                .filter(f -> !Boolean.TRUE.equals(f.getDeleted()))
                .toList();
    }

    public Optional<Form> getFormById(String id) {
        return formRepository.findById(id)
                .filter(f -> !Boolean.TRUE.equals(f.getDeleted()));
    }

    public Optional<Form> getFormByNumber(String formNumber) {
        return formRepository.findByFormNumber(formNumber)
                .filter(f -> !Boolean.TRUE.equals(f.getDeleted()));
    }

    @Transactional
    public Form updateForm(String id, Form formDetails) {
        return formRepository.findById(id)
                .filter(f -> !Boolean.TRUE.equals(f.getDeleted()))
                .map(existingForm -> {
                    // Update allowed fields
                    existingForm.setThumbnail(formDetails.getThumbnail());
                    existingForm.setRepetition(formDetails.getRepetition());
                    existingForm.setSize(formDetails.getSize());
                    existingForm.setState(formDetails.getState());
                    existingForm.setReprint(formDetails.getReprint());
                    existingForm.setCapacityTime(formDetails.getCapacityTime());
                    existingForm.setSteps(formDetails.getSteps());
                    existingForm.setOrderItemIds(formDetails.getOrderItemIds());
                    existingForm.setConfigurationProperties(formDetails.getConfigurationProperties());

                    existingForm.setUpdatedAt(LocalDateTime.now());

                    return formRepository.save(existingForm);
                })
                .orElseThrow(() -> new RuntimeException("Form not found or deleted with id: " + id));
    }

    @Transactional
    public void deleteForm(String id) {
        formRepository.findById(id).ifPresent(form -> {
            form.setDeleted(true);
            form.setUpdatedAt(LocalDateTime.now());
            formRepository.save(form);
        });
    }

}
