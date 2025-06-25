package idv.joe.backend.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponse {

  @JsonProperty("empId")
  private String empId;

  @JsonProperty("name")
  private String name;
}
