package idv.joe.backend.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "seating_chart")
@Data
public class SeatingChartEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "floor_seat_seq")
  private Integer floorSeatSeq;

  @Column(name = "floor_no")
  private Integer floorNo;

  @Column(name = "seat_no")
  private String seatNo;
}

