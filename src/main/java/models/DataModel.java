package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {
    @JsonProperty("id")
    private long id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("time")
    private LocalDateTime time;
}
