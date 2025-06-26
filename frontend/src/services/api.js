const API_BASE = "http://localhost:8080";

// 取得所有員工資訊
export async function fetchEmployees() {
  const res = await fetch(`${API_BASE}/employees`);
  return res.json();
}

// 取得所有座位資訊
export async function fetchSeats() {
  const res = await fetch(`${API_BASE}/seats`);
  return res.json();
}

// 更新座位
export async function assignSeat(request) {
  await fetch(`${API_BASE}/seats/assign`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request),
  });
}

// 清除指定員工座位資訊
export async function clearSeat(empId) {
  await fetch(`${API_BASE}/seats/clear`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ empId }),
  });
}

// 取得員工座位資訊
export async function getEmployeeSeatsInfo() {
  const res = await fetch(`${API_BASE}/seats/info`);
  return res.json();
}

// 清除所有座位資訊
export async function clearAllSeats() {
  await fetch(`${API_BASE}/seats/clear/all`);
}
