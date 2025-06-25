const API_BASE = "http://localhost:8080";

export async function fetchEmployees() {
  const res = await fetch(`${API_BASE}/employees`);
  return res.json();
}

export async function fetchSeats() {
  const res = await fetch(`${API_BASE}/seats`);
  return res.json();
}

export async function assignSeat(empId, seatSeq) {
  await fetch(`${API_BASE}/seats/assign`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ empId, seatSeq }),
  });
}

export async function clearSeat(empId) {
  await fetch(`${API_BASE}/seats/clear`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ empId }),
  });
}

export async function updateSeats(assignments) {
  await fetch(`${API_BASE}/seats/update`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(assignments),
  });
}

export async function getEmployeeSeatsInfo() {
  const res = await fetch(`${API_BASE}/seats/info`);
  return res.json();
}
