// ==========================================================================
// Marian Hostel Management System - Digital Outpass (Gate Pass Appointment)
// ==========================================================================

const outpass = {
  data: [],
  settings: null,

  async render() {
    const container = document.getElementById('viewOutpass');
    container.style.display = 'block';

    const res = await app.api('/outpass');
    this.data = res.outpasses || [];
    this.settings = res.settings || {};

    const isWarden = app.currentUser.role === 'WARDEN';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-hospital-user" style="color:var(--primary);"></i>
              Digital Outpass & Gate Pass Appointment Portal
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Digital appointment pass system with Warden Time-Slot control and cryptographic verification seal.
            </p>
          </div>

          <div style="display:flex; gap:10px;">
            ${isWarden ? `
              <button class="btn btn-secondary" onclick="outpass.openSettingsModal()">
                <i class="fa-solid fa-sliders"></i> Edit Permitted Pass Windows
              </button>
            ` : ''}
            <button class="btn btn-primary" onclick="outpass.openRequestModal()">
              <i class="fa-solid fa-calendar-plus"></i> Book Gate Pass Appointment
            </button>
          </div>
        </div>

        <!-- Warden Time-Limit Policy Bar -->
        <div style="background: linear-gradient(135deg, #1e293b, #0f172a); color: white; padding: 18px 24px; border-radius: var(--radius-md); margin-bottom: 24px;">
          <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:12px;">
            <div>
              <div style="font-size:0.78rem; text-transform:uppercase; color:#94a3b8; font-weight:700;">
                <i class="fa-solid fa-clock"></i> Warden Authorized Exit Windows
              </div>
              <div style="font-size:1.1rem; font-weight:700; margin-top:4px;">
                Morning: <span style="color:#60a5fa;">${this.settings.morningStart || '08:00'} - ${this.settings.morningEnd || '09:00'}</span> &nbsp;•&nbsp; 
                Evening: <span style="color:#f59e0b;">${this.settings.eveningStart || '16:00'} - ${this.settings.eveningEnd || '17:00'}</span>
              </div>
            </div>
            <div style="display:flex; gap:12px; align-items:center;">
              <span class="badge badge-success"><i class="fa-solid fa-circle-check"></i> Pre-booking Open</span>
              <span class="badge badge-danger"><i class="fa-solid fa-ban"></i> Night Curfew: ${this.settings.curfewTime || '20:30'}</span>
            </div>
          </div>
        </div>

        <!-- Outpasses Stream / Cards -->
        <h3 style="font-size:1.1rem; font-weight:700; color:var(--secondary); margin-bottom: 16px;">
          <i class="fa-solid fa-ticket" style="color:var(--primary);"></i> ${isWarden ? 'Pending & Verified Outpass Requests' : 'My Digital Gate Passes'}
        </h3>

        ${this.data.length === 0 ? '<p style="color:var(--text-muted); text-align:center; padding:30px;">No outpass records found.</p>' : ''}
        
        <div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px;">
          ${this.data.map(op => this.renderOutpassCard(op, isWarden)).join('')}
        </div>
      </div>
    `;
  },

  renderOutpassCard(op, isWarden) {
    const isApproved = op.status === 'APPROVED';
    const isPending = op.status === 'PENDING';
    const isRejected = op.status === 'REJECTED';

    return `
      <div class="outpass-card" style="border-left: 5px solid ${isApproved ? 'var(--success)' : (isPending ? 'var(--warning)' : 'var(--danger)')};">
        ${isApproved ? `
          <div class="outpass-seal">
            <i class="fa-solid fa-stamp"></i> WARDEN VERIFIED
          </div>
        ` : ''}

        <div class="outpass-token">
          <i class="fa-solid fa-hashtag"></i> ${op.tokenNo}
        </div>

        <div style="font-size:0.95rem; font-weight:700; color:var(--secondary); margin-bottom: 4px;">
          ${op.studentName}
        </div>
        <div style="font-size:0.8rem; color:var(--text-muted); margin-bottom: 10px;">
          Room ${op.roomNo || 'N/A'} • ${op.department || ''} • Ph: ${op.phone || 'N/A'}
        </div>

        <div style="background:#f8fafc; padding:12px; border-radius:var(--radius-sm); font-size:0.85rem; margin-bottom:12px;">
          <div><strong>Purpose:</strong> ${op.purpose}</div>
          <div><strong>Destination:</strong> ${op.destination}</div>
          <div style="margin-top:6px; color:#475569;">
            <i class="fa-solid fa-arrow-right-from-bracket" style="color:#ef4444;"></i> <strong>Out:</strong> ${op.departureDate} at ${op.departureTime}<br>
            <i class="fa-solid fa-arrow-right-to-bracket" style="color:#10b981;"></i> <strong>Return:</strong> ${op.returnDate} at ${op.returnTime}
          </div>
        </div>

        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">
          <span class="badge ${isApproved ? 'badge-success' : (isPending ? 'badge-warning' : 'badge-danger')}">
            ${op.status}
          </span>
          <span style="font-size:0.75rem; color:#64748b;">
            ${op.isPrebooked ? '⚡ Pre-booked' : 'Standard'}
          </span>
        </div>

        ${op.wardenRemarks ? `
          <div style="font-size:0.8rem; color:#065f46; background:#ecfdf5; padding:6px 10px; border-radius:4px; margin-top:8px;">
            <strong>Warden:</strong> ${op.wardenRemarks}
          </div>
        ` : ''}

        ${isWarden && isPending ? `
          <div style="display:flex; gap:8px; margin-top:14px; border-top:1px solid #f1f5f9; padding-top:10px;">
            <button class="btn btn-success btn-sm" style="flex:1;" onclick="outpass.verifyPass('${op.id}', 'APPROVE')">
              <i class="fa-solid fa-check"></i> Verify & Approve
            </button>
            <button class="btn btn-danger btn-sm" onclick="outpass.verifyPass('${op.id}', 'REJECT')">
              <i class="fa-solid fa-xmark"></i> Reject
            </button>
          </div>
        ` : ''}

        ${isApproved ? `
          <div style="margin-top:10px;">
            <button class="btn btn-outline btn-sm" style="width:100%;" onclick="outpass.viewPassModal('${op.tokenNo}')">
              <i class="fa-solid fa-qrcode"></i> Show Digital Gate Pass (Security)
            </button>
          </div>
        ` : ''}
      </div>
    `;
  },

  openRequestModal() {
    const today = new Date().toISOString().split('T')[0];
    const s = this.settings;

    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:14px;">
        <i class="fa-solid fa-calendar-check" style="color:var(--primary);"></i> Book Digital Gate Pass / Outpass
      </h3>
      <p style="font-size:0.85rem; color:var(--text-muted); margin-bottom:14px;">
        Regular permitted departure windows: <strong>Morning ${s.morningStart || '08:00'} - ${s.morningEnd || '09:00'}</strong> or <strong>Evening ${s.eveningStart || '16:00'} - ${s.eveningEnd || '17:00'}</strong>.
      </p>

      <form onsubmit="outpass.handleRequestSubmit(event)">
        <div class="form-group">
          <label class="form-label">Purpose of Outpass *</label>
          <select id="passPurpose" class="form-select" required>
            <option value="Medical Consultation / Hospital Appointment">Medical Consultation / Hospital Appointment</option>
            <option value="Weekend Home Visit">Weekend Home Visit</option>
            <option value="Academic Project / Library Research">Academic Project / Library Research</option>
            <option value="Urgent Personal / Market Shopping">Urgent Personal / Market Shopping</option>
            <option value="Family Function / Emergency">Family Function / Emergency</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Destination Address / Town *</label>
          <input type="text" id="passDestination" class="form-input" placeholder="e.g. Mary Queens Hospital or Home, Kottayam" required>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Departure Date *</label>
            <input type="date" id="passDeptDate" class="form-input" value="${today}" min="${today}" required>
          </div>
          <div class="form-group">
            <label class="form-label">Departure Time *</label>
            <input type="time" id="passDeptTime" class="form-input" value="${s.eveningStart || '16:00'}" required>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Expected Return Date *</label>
            <input type="date" id="passRetDate" class="form-input" value="${today}" min="${today}" required>
          </div>
          <div class="form-group">
            <label class="form-label">Expected Return Time *</label>
            <input type="time" id="passRetTime" class="form-input" value="19:30" required>
          </div>
        </div>

        <div style="background:#f1f5f9; padding:12px; border-radius:var(--radius-md); margin-bottom:14px;">
          <label style="display:flex; align-items:center; gap:8px; font-size:0.85rem; font-weight:600; cursor:pointer;">
            <input type="checkbox" id="passParentConsent" required>
            I confirm that my parents/guardian have been informed and granted consent.
          </label>
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-paper-plane"></i> Submit for Warden Verification</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async handleRequestSubmit(e) {
    e.preventDefault();
    const payload = {
      purpose: document.getElementById('passPurpose').value,
      destination: document.getElementById('passDestination').value.trim(),
      departureDate: document.getElementById('passDeptDate').value,
      departureTime: document.getElementById('passDeptTime').value,
      returnDate: document.getElementById('passRetDate').value,
      returnTime: document.getElementById('passRetTime').value,
      parentConsentConfirmed: document.getElementById('passParentConsent').checked
    };

    const res = await app.api('/outpass/request', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      outpass.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  openSettingsModal() {
    const s = this.settings;
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:14px;">
        <i class="fa-solid fa-sliders" style="color:var(--primary);"></i> Configure Warden Permitted Pass Hours
      </h3>
      <p style="font-size:0.85rem; color:var(--text-muted); margin-bottom:14px;">
        Set the morning and evening pass departure time windows. Students can only book within these hours.
      </p>

      <form onsubmit="outpass.saveSettings(event)">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Morning Pass Start</label>
            <input type="time" id="setMorningStart" class="form-input" value="${s.morningStart || '08:00'}">
          </div>
          <div class="form-group">
            <label class="form-label">Morning Pass End</label>
            <input type="time" id="setMorningEnd" class="form-input" value="${s.morningEnd || '09:00'}">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Evening Pass Start</label>
            <input type="time" id="setEveningStart" class="form-input" value="${s.eveningStart || '16:00'}">
          </div>
          <div class="form-group">
            <label class="form-label">Evening Pass End</label>
            <input type="time" id="setEveningEnd" class="form-input" value="${s.eveningEnd || '17:00'}">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">Night Hostel Curfew Deadline</label>
          <input type="time" id="setCurfew" class="form-input" value="${s.curfewTime || '20:30'}">
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-check"></i> Save Time Windows</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async saveSettings(e) {
    e.preventDefault();
    const payload = {
      morningStart: document.getElementById('setMorningStart').value,
      morningEnd: document.getElementById('setMorningEnd').value,
      eveningStart: document.getElementById('setEveningStart').value,
      eveningEnd: document.getElementById('setEveningEnd').value,
      curfewTime: document.getElementById('setCurfew').value,
      enforceTimeLimits: true,
      allowPrebooking: true
    };

    const res = await app.api('/outpass/settings', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      outpass.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  async verifyPass(outpassId, action) {
    let remarks = '';
    if (action === 'APPROVE') {
      remarks = prompt('Enter approval remarks (or leave blank for default):', 'Digitally Verified & Approved');
      if (remarks === null) return;
    } else {
      remarks = prompt('Enter reason for rejection:');
      if (remarks === null) return;
    }

    const res = await app.api('/outpass/verify', {
      method: 'POST',
      body: JSON.stringify({ outpassId, action, remarks })
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      outpass.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  viewPassModal(tokenNo) {
    const op = this.data.find(o => o.tokenNo === tokenNo);
    if (!op) return;

    const modalHtml = `
      <div style="text-align:center; padding:10px;">
        <div style="font-size:2rem; margin-bottom:4px;">🏛️</div>
        <h3 style="font-size:1.3rem; font-weight:800; color:#1e3a8a;">MARIAN HOSTEL DIGITAL GATE PASS</h3>
        <p style="font-size:0.75rem; text-transform:uppercase; color:#64748b; letter-spacing:1px; margin-bottom:16px;">
          Security Verification Voucher
        </p>

        <div style="background:#eff6ff; border:2px dashed #3b82f6; border-radius:12px; padding:20px; text-align:left; margin-bottom:16px;">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
            <div style="font-family:monospace; font-size:1.3rem; font-weight:800; color:#1d4ed8;">${op.tokenNo}</div>
            <span class="badge badge-success"><i class="fa-solid fa-stamp"></i> VERIFIED</span>
          </div>

          <div style="font-size:1.05rem; font-weight:700; color:#0f172a;">${op.studentName}</div>
          <div style="font-size:0.85rem; color:#475569; margin-bottom:10px;">
            Room ${op.roomNo} • ${op.department} • Phone: ${op.phone}
          </div>

          <div style="font-size:0.85rem; line-height:1.6; border-top:1px solid #bfdbfe; padding-top:8px;">
            <div><strong>Exit Date & Time:</strong> ${op.departureDate} at ${op.departureTime}</div>
            <div><strong>Expected Return:</strong> ${op.returnDate} at ${op.returnTime}</div>
            <div><strong>Destination:</strong> ${op.destination}</div>
            <div><strong>Purpose:</strong> ${op.purpose}</div>
            <div style="margin-top:6px; color:#1e40af;"><strong>Warden Verification:</strong> ${op.verifiedByWardenName || 'Hostel Warden'}</div>
            <div style="font-family:monospace; font-size:0.75rem; color:#64748b;">Seal: ${op.digitalVerificationHash || 'MARIAN-SEAL'}</div>
          </div>
        </div>

        <p style="font-size:0.8rem; color:#64748b; margin-bottom:16px;">
          Present this verified token screen to Security Gate Guards upon departure and re-entry.
        </p>

        <button class="btn btn-primary" onclick="window.print()">
          <i class="fa-solid fa-print"></i> Print Gate Pass
        </button>
        <button class="btn btn-secondary" onclick="app.closeModal()">Close</button>
      </div>
    `;
    app.showModal(modalHtml);
  }
};
