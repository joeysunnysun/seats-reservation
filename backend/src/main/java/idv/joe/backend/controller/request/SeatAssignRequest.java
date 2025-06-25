package idv.joe.backend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SeatAssignRequest {

  @Size(min = 5, max = 5)
  @NotBlank
  private String empId;

  @NotNull
  private Integer floorNo;

  @NotNull
  private Integer seatNo;
}
