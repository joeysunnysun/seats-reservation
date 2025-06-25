package idv.joe.backend.controller;

import idv.joe.backend.controller.request.SeatAssignRequest;
import idv.joe.backend.controller.request.SeatClearRequest;
import idv.joe.backend.controller.response.EmployeeResponse;
import idv.joe.backend.controller.response.EmployeeSeatsInfoResponse;
import idv.joe.backend.controller.response.SeatsResponse;
import idv.joe.backend.dao.EmployeeEntity;
import idv.joe.backend.dao.SeatingChartEntity;
import idv.joe.backend.service.SeatService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeatController {

  private final SeatService seatService;

  /**
   * Get all employees. 下拉選單用
   *
   * @return List of employees
   */
  @GetMapping("/employees")
  public List<EmployeeResponse> getEmployees() {
    // get all employees from db
    List<EmployeeEntity> employeeEntities = seatService.getFilteredEmployees();

    // map EmployeeEntity rs and return
    return employeeEntities.stream().map(e -> new EmployeeResponse(e.getEmpId(), e.getName())).toList();

  }

  /**
   * 更新座位資訊
   *
   * @param request Seat sequence to retrieve seating chart
   * @return Seating chart for the given seat sequence
   */
  @PostMapping("/seats/assign")
  public ResponseEntity<HttpStatusCode> assignSeat(@Valid @RequestBody SeatAssignRequest request) {
    // update db
    seatService.assignSeat(request.getEmpId(), request.getFloorNo(), request.getSeatNo());

    // return
    return ResponseEntity.ok().build();
  }

  /**
   * Clear seat assignment for an employee.
   *
   * @param request Employee ID to clear seat assignment
   * @return Response entity indicating success
   */
  @PostMapping("/seats/clear")
  public ResponseEntity<HttpStatusCode> clearSeat(@Valid @RequestBody SeatClearRequest request) {
    seatService.clearSeat(request.getEmpId());
    return ResponseEntity.ok().build();
  }

  /**
   * 取得座位基本資訊
   *
   * @return List of seats in the seating chart
   */
  @GetMapping("/seats")
  public List<SeatsResponse> getSeats() {
    // get seats from db
    List<SeatingChartEntity> seats = seatService.getSeats();

    // Map SeatingChartEntity to SeatsResponse
    return seats.stream().map(seat -> new SeatsResponse(seat.getFloorNo(), seat.getSeatNo(), seat.getFloorSeatSeq()))
        .toList();
  }

  /**
   * 取得員工座位資訊
   *
   * @return List of employee seating information
   */
  @GetMapping("/seats/info")
  public List<EmployeeSeatsInfoResponse> getSeatsInfo() {
    // get seats from db
    List<EmployeeEntity> employees = seatService.getEmployees();

    // 過濾除有座位資訊的員工後回傳
    return employees.stream()
        .filter(e -> e.getFloorSeatSeq() != null)
        .map(e -> new EmployeeSeatsInfoResponse(e.getEmpId(), e.getFloorSeatSeq()))
        .toList();
  }
}
