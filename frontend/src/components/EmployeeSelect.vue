<template>
  <div class="seat-grid">
    <div
      v-for="(floorSeats, floorIdx) in seatsByFloor"
      :key="floorIdx"
      class="seat-row"
    >
      <select
        v-for="(seat, seatIdx) in floorSeats"
        :key="seatIdx"
        v-model="selectedEmpIds[floorIdx][seatIdx]"
        :class="seatSelectClass(seat, floorIdx, seatIdx)"
      >
        <option value="">
          <template v-if="seat.empId">
            {{ seat.floorNo }}樓: 座位{{ seat.seatNo }} [員編:{{ seat.empId }}]
          </template>
          <template v-else>
            {{ seat.floorNo }}樓: 座位{{ seat.seatNo }}
          </template>
        </option>
        <option v-for="emp in employees" :key="emp.empId" :value="emp.empId">
          {{ emp.empId }} - {{ emp.name }}
        </option>
      </select>
    </div>
  </div>
  <div class="legend">
    <div class="legend-item">
      <span class="legend-color empty"></span>
      空位
    </div>
    <div class="legend-item">
      <span class="legend-color occupied"></span>
      已佔用
    </div>
    <div class="legend-item">
      <span class="legend-color select"></span>
      請選擇
    </div>
  </div>
  <button class="submit-btn" @click="submitSeats">送出</button>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import {
  fetchEmployees,
  fetchSeats,
  updateSeats,
  getEmployeeSeatsInfo,
} from "../services/api";

const employees = ref([]);
const seats = ref([]);
const seatInfo = ref({});
const selectedEmpIds = ref(Array.from({ length: 4 }, () => Array(4).fill("")));

// 依樓層分組座位
const seatsByFloor = computed(() => {
  const floors = [[], [], [], []];
  seats.value.forEach((seat) => {
    // floorNo 1~4 對應 index 0~3
    floors[seat.floorNo - 1].push(seat);
  });
  return floors;
});

onMounted(async () => {
  // 初始化員工和座位資料
  employees.value = await fetchEmployees();
  console.log("employees:", employees.value);

  // 初始化座位資料
  seats.value = await fetchSeats();
  console.log("seats:", seats.value);

  // 獲取員工座位資訊
  seatInfo.value = await getEmployeeSeatsInfo();
  console.log("employee seats info:", seatInfo);

  // 將seatInfo中的empId合併到seats
  // seatInfo.value為陣列，元素有empId與floorSeatSeq
  const seatInfoMap = {};
  (Array.isArray(seatInfo.value) ? seatInfo.value : []).forEach((info) => {
    seatInfoMap[info.floorSeatSeq] = info.empId;
  });
  seats.value = seats.value.map((seat) => {
    if (seatInfoMap[seat.floorSeatSeq]) {
      return { ...seat, empId: seatInfoMap[seat.floorSeatSeq] };
    }
    return { ...seat, empId: undefined };
  });
});

// 動態產生class
function seatSelectClass(seat, floorIdx, seatIdx) {
  const selected = selectedEmpIds.value[floorIdx][seatIdx];
  if (selected) {
    return ["seat-select", "button-style", "select"];
  }
  if (seat.empId) {
    return ["seat-select", "button-style", "occupied"];
  }
  return ["seat-select", "button-style", "empty"];
}

const submitSeats = async () => {
  const assignments = [];
  seatsByFloor.value.forEach((floorSeats, floorIdx) => {
    floorSeats.forEach((seat, seatIdx) => {
      const empId = selectedEmpIds.value[floorIdx][seatIdx];
      if (empId) {
        assignments.push({
          floorNo: seat.floorNo,
          seatNo: seat.seatNo,
          empId,
        });
      }
    });
  });
  try {
    await updateSeats(assignments);
    alert("座位指派已送出！");
  } catch {
    alert("送出失敗，請稍後再試");
  }
};
</script>

<style scoped>
.seat-grid {
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
}
.seat-row {
  display: flex;
  gap: 8px;
}
.seat-select {
  min-width: 180px;
}

/* 按鈕風格的下拉選單 */
.button-style {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #ebebedc6;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 8px 16px;
  font-size: 1rem;
  cursor: pointer;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
  transition: background 0.2s;
  color: #000000;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  text-align: center;
  line-height: 1.5;
  font-size: 0.875rem;
  padding: 0.5rem 1rem;
}

.legend {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 32px;
  position: static;
}
.legend-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.9rem;
  color: rgba(0, 0, 0);
  font-weight: bold;
}
.legend-color {
  display: inline-block;
  width: 18px;
  height: 18px;
  border-radius: 4px;
  margin-right: 4px;
  border: 1px solid #bbb;
}
.legend-color.empty {
  background-color: #ebebedc6;
}
.legend-color.occupied {
  background-color: #ff4d4f;
}
.legend-color.select {
  background-color: #63f17f;
}

.submit-btn {
  margin-top: 24px;
  background-color: #1e293b;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 12px 32px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
  align-self: flex-start;
}
.submit-btn:hover {
  background-color: #334155;
}
.seat-select.occupied {
  background-color: #ff4d4f !important;
  color: #fff !important;
}
.seat-select.select {
  background-color: #63f17f !important;
  color: #000 !important;
}
.seat-select.empty {
  background-color: #ebebedc6 !important;
  color: #000 !important;
}
</style>
