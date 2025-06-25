package idv.joe.backend.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatsResponse {

  @JsonProperty("floorNo")
  private Integer floorNo;

  @JsonProperty("seatNo")
  private String seatNo;

  @JsonProperty("floorSeatSeq")
  private Integer floorSeatSeq;
  
}
