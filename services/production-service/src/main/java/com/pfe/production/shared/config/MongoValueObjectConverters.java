package com.pfe.production.shared.config;
import com.pfe.production.domain.form.FormNumber;

import com.pfe.production.shared.valueobject.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

/**
 * MongoDB custom converters for Value Objects.
 * Ensures Value Objects serialize/deserialize as their underlying primitive
 * types,
 * maintaining backward compatibility with the existing MongoDB schema.
 */
@Configuration
public class MongoValueObjectConverters {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
                // FormNumber
                new FormNumberToStringConverter(),
                new StringToFormNumberConverter(),
                // Quantity
                new QuantityToIntegerConverter(),
                new IntegerToQuantityConverter(),
                // Dimension
                new DimensionToDoubleConverter(),
                new DoubleToDimensionConverter(),
                // StepName
                new StepNameToStringConverter(),
                new StringToStepNameConverter(),
                // DurationMinutes
                new DurationMinutesToIntegerConverter(),
                new IntegerToDurationMinutesConverter()));
    }

    // --- FormNumber converters ---
    static class FormNumberToStringConverter implements Converter<FormNumber, String> {
        @Override
        public String convert(FormNumber source) {
            return source.getValue();
        }
    }

    static class StringToFormNumberConverter implements Converter<String, FormNumber> {
        @Override
        public FormNumber convert(String source) {
            return new FormNumber(source);
        }
    }

    // --- Quantity converters ---
    static class QuantityToIntegerConverter implements Converter<Quantity, Integer> {
        @Override
        public Integer convert(Quantity source) {
            return source.getValue();
        }
    }

    static class IntegerToQuantityConverter implements Converter<Integer, Quantity> {
        @Override
        public Quantity convert(Integer source) {
            return new Quantity(source);
        }
    }

    // --- Dimension converters ---
    static class DimensionToDoubleConverter implements Converter<Dimension, Double> {
        @Override
        public Double convert(Dimension source) {
            return source.getValue();
        }
    }

    static class DoubleToDimensionConverter implements Converter<Double, Dimension> {
        @Override
        public Dimension convert(Double source) {
            return new Dimension(source);
        }
    }

    // --- StepName converters ---
    static class StepNameToStringConverter implements Converter<StepName, String> {
        @Override
        public String convert(StepName source) {
            return source.getValue();
        }
    }

    static class StringToStepNameConverter implements Converter<String, StepName> {
        @Override
        public StepName convert(String source) {
            return new StepName(source);
        }
    }

    // --- DurationMinutes converters ---
    static class DurationMinutesToIntegerConverter implements Converter<DurationMinutes, Integer> {
        @Override
        public Integer convert(DurationMinutes source) {
            return source.getValue();
        }
    }

    static class IntegerToDurationMinutesConverter implements Converter<Integer, DurationMinutes> {
        @Override
        public DurationMinutes convert(Integer source) {
            return new DurationMinutes(source);
        }
    }
}
