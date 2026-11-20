// ==========================================================================
// Marian Hostel Management System - Attendance Module
// ==========================================================================

const attendance = {
  currentDate: new Date().toISOString().split('T')[0],
  currentSession: 'NIGHT_CURFEW',

  async render() {
    const container = document.getElementById('viewAttendance');
    container.style.display = 'block';

    const isWarden = app.currentUser.role === 'WARDEN';

    if (isWarden) {
      await this.renderWardenRollCall(container);
    } else {
      await this.renderStudentSummary(container);
    }
  },

  async renderWardenRollCall(container) {
    const res = await app.api(`/attendance/daily?date=${this.currentDate}&session=${this.currentSession}`);
    const students = res.students || [];

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-clipboard-user" style="color:var(--primary);"></i>
              Hostel Night Roll-Call & Attendance Register (Warden Portal)
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Mark daily physical roll-call and biometric presence for all hostel wings.
            </p>
          </div>

          <!-- Quick Mark All Present button -->
          <button class="btn btn-success" onclick="attendance.markAllPresent()">
            <i class="fa-solid fa-check-double"></i> Mark All Present
          </button>
        </div>

        <!-- Filter Controls -->
        <div style="display:flex; gap:16px; margin-bottom:20px; flex-wrap:wrap; align-items:center; background:#f8fafc; padding:14px; border-radius:var(--radius-md);">
          <div style="display:flex; align-items:center; gap:8px;">
            <label style="font-size:0.85rem; font-weight:700;">Roll-Call Date:</label>
            <input type="date" id="attDate" class="form-input" style="width:auto; padding:6px 12px;" value="${this.currentDate}" onchange="attendance.changeFilter()">
          </div>

          <div style="display:flex; align-items:center; gap:8px;">
            <label style="font-size:0.85rem; font-weight:700;">Session:</label>
            <select id="attSession" class="form-select" style="width:auto; padding:6px 12px;" onchange="attendance.changeFilter()">
              <option value="NIGHT_CURFEW" ${this.currentSession === 'NIGHT_CURFEW' ? 'selected' : ''}>Night Curfew (09:30 PM)</option>
              <option value="MORNING" ${this.currentSession === 'MORNING' ? 'selected' : ''}>Morning Assembly (07:00 AM)</option>
            </select>
          </div>

          <div style="margin-left:auto; display:flex; gap:12px;">
            <span class="badge badge-success">Present: ${res.presentCount || 0}</span>
            <span class="badge badge-danger">Absent: ${res.absentCount || 0}</span>
            <span class="badge badge-warning">On Leave: ${res.onLeaveCount || 0}</span>
          </div>
        </div>

        <!-- Students Attendance Table -->
        <div style="overflow-x:auto;">
          <table style="width:100%; border-collapse:collapse; font-size:0.9rem;">
            <thead>
              <tr style="background:#f1f5f9; text-align:left; border-bottom:2px solid var(--border);">
                <th style="padding:12px;">Room & Berth</th>
                <th style="padding:12px;">Student Name</th>
                <th style="padding:12px;">Department</th>
                <th style="padding:12px;">Status</th>
                <th style="padding:12px; text-align:right;">Actions</th>
              </tr>
            </thead>
            <tbody>
              ${students.map(st => `
                <tr style="border-bottom:1px solid #f1f5f9;">
                  <td style="padding:12px; font-weight:700; color:var(--primary);">
                    Room ${st.roomNo || 'N/A'}
                  </td>
                  <td style="padding:12px; font-weight:600;">
                    ${st.name}
                  </td>
                  <td style="padding:12px; color:var(--text-muted);">
                    ${st.department || ''}
                  </td>
                  <td style="padding:12px;">
                    <span class="badge ${st.status === 'PRESENT' ? 'badge-success' : (st.status === 'ABSENT' ? 'badge-danger' : (st.status === 'ON_LEAVE' ? 'badge-warning' : 'badge-neutral'))}">
                      ${st.status}
                    </span>
                  </td>
                  <td style="padding:12px; text-align:right;">
                    <div style="display:inline-flex; gap:6px;">
                      <button class="btn btn-sm ${st.status === 'PRESENT' ? 'btn-success' : 'btn-outline'}" onclick="attendance.markStudent('${st.studentId}', 'PRESENT')">Present</button>
                      <button class="btn btn-sm ${st.status === 'ABSENT' ? 'btn-danger' : 'btn-outline'}" onclick="attendance.markStudent('${st.studentId}', 'ABSENT')">Absent</button>
                      <button class="btn btn-sm ${st.status === 'ON_LEAVE' ? 'btn-warning' : 'btn-outline'}" onclick="attendance.markStudent('${st.studentId}', 'ON_LEAVE')">Leave</button>
                    </div>
                  </td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </div>
      </div>
    `;
  },

  async renderStudentSummary(container) {
    const res = await app.api('/attendance/summary');
    const history = res.history || [];

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-calendar-check" style="color:var(--primary);"></i>
              My Hostel Attendance Record
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Roll-call presence recorded by the hostel warden.
            </p>
          </div>
        </div>

        <!-- Attendance Scorecard -->
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:16px; margin-bottom:24px;">
          <div style="background:#ecfdf5; border:1px solid #a7f3d0; padding:18px; border-radius:var(--radius-md);">
            <div style="font-size:0.75rem; text-transform:uppercase; color:#065f46; font-weight:700;">Attendance Rate</div>
            <div style="font-size:1.8rem; font-weight:800; color:#059669; margin-top:4px;">${res.attendancePercentage || 100}%</div>
            <span class="badge badge-success" style="margin-top:4px;">Good Standing</span>
          </div>

          <div style="background:#f8fafc; border:1px solid #e2e8f0; padding:18px; border-radius:var(--radius-md);">
            <div style="font-size:0.75rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Days Present</div>
            <div style="font-size:1.8rem; font-weight:800; color:#1e293b; margin-top:4px;">${res.presentDays || 0} / ${res.totalDays || 0}</div>
            <span style="font-size:0.78rem; color:var(--text-muted);">Verified Roll-Calls</span>
          </div>

          <div style="background:#fffbeb; border:1px solid #fde68a; padding:18px; border-radius:var(--radius-md);">
            <div style="font-size:0.75rem; text-transform:uppercase; color:#92400e; font-weight:700;">Approved Leave / Outpass</div>
            <div style="font-size:1.8rem; font-weight:800; color:#b45309; margin-top:4px;">${res.leaveDays || 0} Days</div>
            <span style="font-size:0.78rem; color:#92400e;">Exempted</span>
          </div>
        </div>

        <!-- Attendance Logs -->
        <h3 style="font-size:1.05rem; font-weight:700; color:var(--secondary); margin-bottom: 14px;">
          <i class="fa-solid fa-list-check" style="color:var(--primary);"></i> Attendance Log History
        </h3>

        <div style="overflow-x:auto;">
          <table style="width:100%; border-collapse:collapse; font-size:0.9rem;">
            <thead>
              <tr style="background:#f1f5f9; text-align:left; border-bottom:2px solid var(--border);">
                <th style="padding:10px;">Date</th>
                <th style="padding:10px;">Roll-Call Session</th>
                <th style="padding:10px;">Status</th>
                <th style="padding:10px;">Marked By</th>
              </tr>
            </thead>
            <tbody>
              ${history.length === 0 ? '<tr><td colspan="4" style="text-align:center; padding:20px; color:#94a3b8;">No attendance history logged.</td></tr>' : ''}
              ${history.map(a => `
                <tr style="border-bottom:1px solid #f1f5f9;">
                  <td style="padding:10px; font-weight:600;">${a.date}</td>
                  <td style="padding:10px;">${a.session}</td>
                  <td style="padding:10px;">
                    <span class="badge ${a.status === 'PRESENT' ? 'badge-success' : (a.status === 'ABSENT' ? 'badge-danger' : 'badge-warning')}">
                      ${a.status}
                    </span>
                  </td>
                  <td style="padding:10px; color:var(--text-muted);">${a.markedBy || 'Warden'}</td>
                </tr>
              `).join('')}
            </tbody>
          </table>
        </div>
      </div>
    `;
  },

  changeFilter() {
    this.currentDate = document.getElementById('attDate').value;
    this.currentSession = document.getElementById('attSession').value;
    this.render();
  },

  async markStudent(studentId, status) {
    const res = await app.api('/attendance/mark', {
      method: 'POST',
      body: JSON.stringify({
        studentId,
        status,
        date: this.currentDate,
        session: this.currentSession
      })
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      this.render();
    }
  },

  async markAllPresent() {
    const res = await app.api(`/attendance/daily?date=${this.currentDate}&session=${this.currentSession}`);
    const students = res.students || [];

    const entries = students.map(s => ({
      studentId: s.studentId,
      status: 'PRESENT'
    }));

    const bulkRes = await app.api('/attendance/bulk', {
      method: 'POST',
      body: JSON.stringify({
        date: this.currentDate,
        session: this.currentSession,
        entries
      })
    });

    if (bulkRes.success) {
      app.showToast('All students marked PRESENT for ' + this.currentDate, 'success');
      this.render();
    }
  }
};
