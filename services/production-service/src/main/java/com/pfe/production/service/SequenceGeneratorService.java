package com.pfe.production.service;

import com.pfe.production.domain.DatabaseSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    @Autowired
    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public String generateFormNumber() {
        int year = LocalDate.now().getYear();
        String sequenceKey = "form_sequence_" + year;
        long sequence = generateSequence(sequenceKey);
        return String.format("F%d-%04d", year, sequence);
    }

    public String generateJobNumber() {
        int year = LocalDate.now().getYear();
        String sequenceKey = "job_sequence_" + year;
        long sequence = generateSequence(sequenceKey);
        return String.format("J%d-%04d", year, sequence);
    }

    public String generatePaletteNumber() {
        int year = LocalDate.now().getYear();
        String sequenceKey = "palette_sequence_" + year;
        long sequence = generateSequence(sequenceKey);
        return String.format("P%d-%04d", year, sequence);
    }
}
