// ==========================================================================
// Marian Hostel Management System - Railway App Style Room Allocation
// ==========================================================================

const rooms = {
  currentFloor: 1,
  data: null,

  async render() {
    const container = document.getElementById('viewRooms');
    container.style.display = 'block';

    const res = await app.api('/rooms/grid');
    this.data = res;

    container.innerHTML = `
      <!-- Railway Station & Coach Banner -->
      <div class="railway-coach">
        <div class="railway-header">
          <div class="coach-title">
            <i class="fa-solid fa-train-subway" style="color:#60a5fa; font-size:1.6rem;"></i>
            <div>
              <div>Marian Residence Express • Room & Berth Allocation</div>
              <span style="font-size:0.8rem; color:#94a3b8; font-weight:normal;">
                IRCTC Style Real-Time Visual Coach & Berth Map
              </span>
            </div>
          </div>

          <!-- Berth Color Legend -->
          <div class="berth-legend">
            <span style="display:flex; align-items:center; gap:6px;">
              <span class="legend-dot legend-vacant"></span> Available (Vacant)
            </span>
            <span style="display:flex; align-items:center; gap:6px;">
              <span class="legend-dot legend-occupied"></span> Booked (Occupied)
            </span>
            <span style="display:flex; align-items:center; gap:6px;">
              <span class="legend-dot legend-maintenance"></span> Reserved / Maintenance
            </span>
          </div>
        </div>

        <div class="coach-track-bar"></div>

        <!-- Occupancy Statistics Bar -->
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap:12px; background:#1e293b; padding:16px; border-radius:var(--radius-md); margin-bottom:20px;">
          <div>
            <div style="font-size:0.75rem; color:#94a3b8; text-transform:uppercase;">Total Berths</div>
            <div style="font-size:1.3rem; font-weight:700; color:white;">${res.totalBeds || 0} Beds</div>
          </div>
          <div>
            <div style="font-size:0.75rem; color:#94a3b8; text-transform:uppercase;">Occupied Berths</div>
            <div style="font-size:1.3rem; font-weight:700; color:#f87171;">${res.occupiedBeds || 0} Booked</div>
          </div>
          <div>
            <div style="font-size:0.75rem; color:#94a3b8; text-transform:uppercase;">Vacant Berths</div>
            <div style="font-size:1.3rem; font-weight:700; color:#4ade80;">${res.vacantBeds || 0} Available</div>
          </div>
          <div>
            <div style="font-size:0.75rem; color:#94a3b8; text-transform:uppercase;">Occupancy Ratio</div>
            <div style="font-size:1.3rem; font-weight:700; color:#60a5fa;">${res.occupancyRate || 0}% Full</div>
          </div>
        </div>

        <!-- Floor / Coach Selector Buttons -->
        <div style="display:flex; gap:10px; margin-bottom:20px; flex-wrap:wrap;">
          <button class="btn ${this.currentFloor === 1 ? 'btn-primary' : 'btn-secondary'}" onclick="rooms.selectFloor(1)">
            <i class="fa-solid fa-layer-group"></i> Coach Floor 1 (Delux Rooms 101 - 105)
          </button>
          <button class="btn ${this.currentFloor === 2 ? 'btn-primary' : 'btn-secondary'}" onclick="rooms.selectFloor(2)">
            <i class="fa-solid fa-layer-group"></i> Coach Floor 2 (Standard Rooms 201 - 205)
          </button>
          <button class="btn ${this.currentFloor === 3 ? 'btn-primary' : 'btn-secondary'}" onclick="rooms.selectFloor(3)">
            <i class="fa-solid fa-layer-group"></i> Coach Floor 3 (Premium 2-Bed 301 - 305)
          </button>
        </div>

        <!-- Room Compartments Grid -->
        <div id="floorRoomsContainer" class="rooms-grid">
          ${this.renderFloorRooms(this.currentFloor)}
        </div>
      </div>
    `;
  },

  selectFloor(floorNum) {
    this.currentFloor = floorNum;
    this.render();
  },

  renderFloorRooms(floorNum) {
    if (!this.data || !this.data.floors || !this.data.floors[floorNum]) {
      return '<p style="color:#94a3b8;">No room data for this floor.</p>';
    }

    const roomList = this.data.floors[floorNum];
    return roomList.map(r => `
      <div class="room-compartment">
        <div class="room-compartment-header">
          <div>
            <div class="room-number-badge"><i class="fa-solid fa-door-closed"></i> Room ${r.roomNo}</div>
            <span style="font-size:0.72rem; color:#94a3b8;">${r.roomType} • ₹${r.monthlyRent}/mo</span>
          </div>
          <div>
            <span class="badge ${r.vacantCount > 0 ? 'badge-success' : 'badge-danger'}" style="font-size:0.7rem;">
              ${r.vacantCount > 0 ? r.vacantCount + ' Vacant' : 'Full House'}
            </span>
          </div>
        </div>

        <!-- Berths in this compartment -->
        <div class="berths-container">
          ${r.beds.map(b => {
            const isVacant = b.status === 'VACANT';
            const isOccupied = b.status === 'OCCUPIED';
            const cls = isVacant ? 'vacant' : (isOccupied ? 'occupied' : 'maintenance');
            const icon = isVacant ? 'fa-circle-check' : (isOccupied ? 'fa-user-check' : 'fa-wrench');
            
            return `
              <div class="berth-slot ${cls}" onclick="rooms.handleBerthClick('${r.roomNo}', '${b.bedId}', '${b.status}', '${b.studentName}', '${b.studentDept}')" title="${isOccupied ? 'Occupied by ' + b.studentName : 'Click to allocate/book berth'}">
                <span style="display:flex; align-items:center; gap:8px;">
                  <i class="fa-solid ${icon}"></i>
                  <span>${b.bedLabel}</span>
                </span>
                <span style="font-size:0.75rem; font-weight:700;">
                  ${isOccupied ? b.studentName.split(' ')[0] : (isVacant ? 'AVAILABLE' : 'REPAIR')}
                </span>
              </div>
            `;
          }).join('')}
        </div>
      </div>
    `).join('');
  },

  handleBerthClick(roomNo, bedId, status, studentName, studentDept) {
    const isWarden = app.currentUser.role === 'WARDEN';

    if (status === 'OCCUPIED') {
      const modalHtml = `
        <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:12px;">
          <i class="fa-solid fa-bed" style="color:var(--primary);"></i> Berth Details • Room ${roomNo}
        </h3>
        <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px; margin-bottom:16px;">
          <div style="font-size:0.8rem; color:var(--text-muted); text-transform:uppercase; font-weight:700;">Resident Occupant</div>
          <div style="font-size:1.15rem; font-weight:700; color:var(--secondary); margin:4px 0;">${studentName}</div>
          <div style="font-size:0.88rem; color:#475569;"><i class="fa-solid fa-graduation-cap"></i> ${studentDept || 'General Branch'}</div>
          <div style="font-size:0.85rem; color:#10b981; font-weight:600; margin-top:6px;">
            <i class="fa-solid fa-circle-check"></i> Registered Berth Slot: ${bedId}
          </div>
        </div>

        ${isWarden ? `
          <div style="border-top:1px solid #f1f5f9; padding-top:14px; display:flex; justify-content:space-between; align-items:center;">
            <span style="font-size:0.82rem; color:var(--text-muted);">Warden Administrative Action:</span>
            <button class="btn btn-danger btn-sm" onclick="rooms.vacateBed('${roomNo}', '${bedId}')">
              <i class="fa-solid fa-user-xmark"></i> Vacate / Clear Berth
            </button>
          </div>
        ` : ''}

        <div style="display:flex; justify-content:flex-end; margin-top:16px;">
          <button class="btn btn-secondary" onclick="app.closeModal()">Close</button>
        </div>
      `;
      app.showModal(modalHtml);
    } else if (status === 'VACANT') {
      // Allow booking or allocating
      const modalHtml = `
        <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:12px;">
          <i class="fa-solid fa-train" style="color:var(--success);"></i> Reserve Berth • Room ${roomNo}
        </h3>
        <p style="font-size:0.9rem; color:var(--text-muted); margin-bottom:16px;">
          Berth <strong>${bedId}</strong> in Room ${roomNo} is currently VACANT and available for allocation.
        </p>

        <div style="background:#ecfdf5; border:1px solid #a7f3d0; border-radius:var(--radius-md); padding:14px; margin-bottom:16px; font-size:0.88rem; color:#065f46;">
          <strong>Target Berth:</strong> Room ${roomNo} (${bedId})<br>
          <strong>Allocation To:</strong> ${app.currentUser.name} (${app.currentUser.department || 'Student'})
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px;">
          <button class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button class="btn btn-success" onclick="rooms.allocateToMe('${roomNo}', '${bedId}')">
            <i class="fa-solid fa-check"></i> Confirm Berth Reservation
          </button>
        </div>
      `;
      app.showModal(modalHtml);
    } else {
      app.showToast('This berth is currently under maintenance or cleaning.', 'info');
    }
  },

  async allocateToMe(roomNo, bedId) {
    const res = await app.api('/rooms/allocate', {
      method: 'POST',
      body: JSON.stringify({
        roomNo,
        bedId,
        studentId: app.currentUser.id
      })
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      app.currentUser.roomNo = roomNo;
      localStorage.setItem('marian_user', JSON.stringify(app.currentUser));
      app.renderAuthenticatedUI();
      rooms.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  async vacateBed(roomNo, bedId) {
    if (!confirm('Are you sure you want to vacate this berth? The occupant will be unassigned.')) return;

    const res = await app.api('/rooms/vacate', {
      method: 'POST',
      body: JSON.stringify({ roomNo, bedId })
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      rooms.render();
    } else {
      app.showToast(res.message, 'error');
    }
  }
};
