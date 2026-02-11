package com.pfe.production.domain.form;

import com.pfe.production.domain.form.Form.CapacityTime;
import com.pfe.production.domain.form.Form.ConfigurationProperty;
import com.pfe.production.domain.form.Form.FileMeta;
import com.pfe.production.domain.form.Form.Size;
import com.pfe.production.domain.form.Form.Step;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateFormRequest(

                @NotNull(message = "Thumbnail is required") @Valid FileMeta thumbnail,

                @NotNull(message = "Repetition is required") @Min(value = 1, message = "Repetition must be greater than 0") Integer repetition,

                @NotNull(message = "Size is required") @Valid Size size,

                @NotNull(message = "Capacity time is required") @Valid CapacityTime capacityTime,

                @NotEmpty(message = "At least one step is required") @Valid List<Step> steps,

                List<String> orderItemIds,

                @NotEmpty(message = "At least one configuration property is required") @Valid List<ConfigurationProperty> configurationProperties,

                Boolean reprint) {
}
