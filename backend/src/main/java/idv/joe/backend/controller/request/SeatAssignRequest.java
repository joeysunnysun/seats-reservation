package idv.joe.backend.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class SeatAssignRequest {

  @JsonProperty("seats")
  List<SeatAssign> seats;
}
