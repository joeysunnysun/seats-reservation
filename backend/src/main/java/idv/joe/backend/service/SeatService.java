package idv.joe.backend.service;

import idv.joe.backend.dao.EmployeeEntity;
import idv.joe.backend.dao.SeatingChartEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

  // Parameter names for stored procedures
  public static final String P_EMP_ID = "p_emp_id";
  public static final String P_FLOOR_NO = "p_floor_no";
  public static final String P_SEAT_NO = "p_seat_no";
  // stored procedure names
  public static final String ASSIGN_SEAT = "assign_seat";
  public static final String CLEAR_SEAT = "clear_seat";
  public static final String GET_ALL_EMPLOYEES = "get_all_employees";
  public static final String CLEAR_ALL_SEATS = "clear_all_seats";

  @PersistenceContext
  private EntityManager em;

  public List<EmployeeEntity> getFilteredEmployees() {
    // 使用 stored procedure 從資料庫取得所有員工
    @SuppressWarnings("unchecked") List<EmployeeEntity> employeeList = em.createStoredProcedureQuery(GET_ALL_EMPLOYEES,
        EmployeeEntity.class).getResultList();

    // 篩選掉有座位的員工
    return employeeList.stream().filter(employee -> employee.getFloorSeatSeq() == null).toList();
  }

  public void assignSeat(String empId, Integer floorNo, Integer seatNo) {
    // 使用 StoredProcedure 更新 db
    StoredProcedureQuery query = em.createStoredProcedureQuery(ASSIGN_SEAT);
    query.registerStoredProcedureParameter(P_EMP_ID, String.class, ParameterMode.IN);
    query.registerStoredProcedureParameter(P_FLOOR_NO, Integer.class, ParameterMode.IN);
    query.registerStoredProcedureParameter(P_SEAT_NO, Integer.class, ParameterMode.IN);
    query.setParameter(P_EMP_ID, empId);
    query.setParameter(P_FLOOR_NO, floorNo);
    query.setParameter(P_SEAT_NO, seatNo);
    query.execute();
  }

  @Transactional
  public void clearSeat(String empId) {
    // 使用 StoredProcedure 更新 db
    StoredProcedureQuery query = em.createStoredProcedureQuery(CLEAR_SEAT);
    query.registerStoredProcedureParameter(P_EMP_ID, String.class, ParameterMode.IN);
    query.setParameter(P_EMP_ID, empId);
    query.execute();
  }

  public List<EmployeeEntity> getEmployees() {
    // 使用 stored procedure 從資料庫取得所有員工
    @SuppressWarnings("unchecked") List<EmployeeEntity> employeeList = em.createStoredProcedureQuery(GET_ALL_EMPLOYEES,
        EmployeeEntity.class).getResultList();

    return employeeList;
  }

  public List<SeatingChartEntity> getSeats() {
    // 使用 stored procedure 從資料庫取得所有座位資訊
    @SuppressWarnings("unchecked") List<SeatingChartEntity> seatingList = em.createStoredProcedureQuery(
        "get_seating_chart", SeatingChartEntity.class).getResultList();

    // 回傳座位資訊
    return seatingList;
  }


  @Transactional
  public void clearAllSeats() {
    // 使用 StoredProcedure 清除所有座位資訊
    StoredProcedureQuery query = em.createStoredProcedureQuery(CLEAR_ALL_SEATS);
    query.execute();
  }
}
