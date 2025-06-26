package idv.joe.backend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatAssign {

  @Size(min = 5, max = 5)
  @NotBlank
  private String empId;

  @NotNull
  private Integer floorNo;

  @NotNull
  private Integer seatNo;
}
