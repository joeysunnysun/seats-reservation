<template>
  <div class="seat-grid">
    <div
      v-for="(floorSeats, floorIdx) in seatsByFloor"
      :key="floorIdx"
      class="seat-row"
    >
      <div
        v-for="(seat, seatIdx) in floorSeats"
        :key="seatIdx"
        class="seat-cell"
      >
        <div class="select-clear-container">
          <select
            v-model="selectedEmpIds[floorIdx][seatIdx]"
            :class="seatSelectClass(seat, floorIdx, seatIdx)"
          >
            <option value="">
              <template v-if="seat.empId">
                {{ seat.floorNo }}樓: 座位{{ seat.seatNo }} [員編:{{
                  seat.empId
                }}]
              </template>
              <template v-else>
                {{ seat.floorNo }}樓: 座位{{ seat.seatNo }}
              </template>
            </option>
            <option
              v-for="emp in employees"
              :key="emp.empId"
              :value="emp.empId"
            >
              {{ emp.empId }} - {{ emp.name }}
            </option>
          </select>
          <button
            v-if="seat.empId"
            class="clear-btn"
            @click="() => clearSeatHandler(seat.empId, floorIdx, seatIdx)"
            title="清除此座位"
          >
            ✕
          </button>
        </div>
      </div>
    </div>
  </div>
  <div class="legend-group">
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
    <button class="clear-all-btn" @click="clearAllSeatsHandler">
      清除所有座位
    </button>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import {
  fetchEmployees,
  fetchSeats,
  assignSeat,
  getEmployeeSeatsInfo,
  clearSeat,
  clearAllSeats,
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

  // 初始化座位資料
  seats.value = await fetchSeats();

  // 取得員工座位資訊
  seatInfo.value = await getEmployeeSeatsInfo();

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
          seatNo: Number(seat.seatNo),
          empId: empId,
        });
      }
    });
  });
  try {
    // 依後端格式包裝 seats 屬性
    await assignSeat({ seats: assignments });
    window.location.reload();
  } catch {
    console.error("Failed to update seats");
  }
};

const clearSeatHandler = async (empId, floorIdx, seatIdx) => {
  try {
    await clearSeat(empId);
    // 清除後同步更新畫面
    seats.value = await fetchSeats();
    seatInfo.value = await getEmployeeSeatsInfo();
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
    selectedEmpIds.value[floorIdx][seatIdx] = "";
  } catch {
    console.error("Failed to clear seat");
  }
};

const clearAllSeatsHandler = async () => {
  await clearAllSeats();
  window.location.reload();
};
</script>

<style scoped>
.seat-grid {
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
  align-items: center;
}
.seat-row {
  display: flex;
  gap: 8px;
  justify-content: center;
}
.seat-select {
  min-width: 0;
  flex: 1 1 0;
}

/* 下拉選單 */
.button-style {
  appearance: none;
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #ebebedc6;
  color: #fff;
  border: none;
  border-radius: 6px;
  /* 字體大小 */
  font-size: 1.1rem;
  padding: 0.75rem 2rem;
  min-height: 48px;
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
  box-sizing: border-box;
}

.legend-group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-top: 32px;
  margin-left: 0;
  width: 220px;
}

.legend {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: flex-start;
  margin: 0;
  position: static;
}
.legend-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.9rem;
  color: rgba(0, 0, 0);
  font-weight: bold;
  justify-content: flex-start;
  width: auto;
}
.legend-color {
  display: inline-block;
  width: 28px;
  height: 28px;
  border-radius: 6px;
  margin-right: 8px;
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
  margin-top: 16px;
  background-color: #1e293b;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 16px 40px;
  font-size: 1.2rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s;
  letter-spacing: 2px;
  min-width: 180px;
  align-self: flex-start;
}
.submit-btn:hover {
  background-color: #334155;
}
.clear-all-btn {
  margin-top: 16px;
  background-color: #fff;
  color: #ef4444;
  border: 2px solid #ef4444;
  border-radius: 6px;
  padding: 12px 32px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition:
    background 0.2s,
    color 0.2s,
    border 0.2s;
  letter-spacing: 2px;
  min-width: 180px;
  align-self: flex-start;
}
.clear-all-btn:hover {
  background-color: #ef4444;
  color: #fff;
  border-color: #ef4444;
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
.seat-cell {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 2px;
  width: auto;
  min-width: 0;
}
.select-clear-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  width: 100%;
  min-width: 320px;
  max-width: 100%;
}
.seat-select {
  flex: 1 1 auto;
  min-width: 0;
  max-width: 100%;
  font-size: 1.1rem;
  padding: 0.75rem 2rem;
  height: 48px;
  box-sizing: border-box;
}
.clear-btn {
  margin-left: 4px;
  background: #fff;
  color: #ff4d4f;
  border: 1px solid #ff4d4f;
  border-radius: 50%;
  padding: 0 7px;
  font-size: 1rem;
  height: 28px;
  min-width: 28px;
  max-width: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition:
    background 0.15s,
    color 0.15s,
    border 0.15s;
  line-height: 1;
}
.clear-btn:hover {
  background: #ff4d4f;
  color: #fff;
  border-color: #ff4d4f;
}
</style>
