package idv.joe.backend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SeatClearRequest {

  @Size(min = 5, max = 5)
  @NotBlank
  private String empId;
}

