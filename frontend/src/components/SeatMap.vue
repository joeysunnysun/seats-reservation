<template>
  <div>
    <div v-for="seat in seats" :key="seat.floorSeatSeq">
      <button
        :style="{ backgroundColor: getSeatColor(seat) }"
        @click="onSeatClick(seat)"
      >
        {{ seat.seatNo }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { assignSeat } from "../services/api";

const props = defineProps({ floorNo: Number, empId: String });
const seats = ref([]);

onMounted(async () => {
  // seats.value = await fetchSeats(props.floorNo)
});

function getSeatColor(seat) {
  return seat.occupied ? "gray" : "green"; // 未來可根據 API 資料調整
}

async function onSeatClick(seat) {
  if (!props.empId) return alert("請先選擇員工");
  await assignSeat(props.empId, seat.floorSeatSeq);
  alert("指派完成！");
}
</script>
