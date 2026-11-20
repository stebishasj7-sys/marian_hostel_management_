// ==========================================================================
// Marian Hostel Management System - Emergency Contacts Directory
// ==========================================================================

const emergency = {
  data: [],

  async render() {
    const container = document.getElementById('viewEmergency');
    container.style.display = 'block';

    const res = await app.api('/emergency');
    this.data = res.contacts || [];

    const isWarden = app.currentUser.role === 'WARDEN';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-phone-volume" style="color:var(--danger);"></i>
              Emergency Helpline & Medical Assistance Directory
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Immediate phone access for medical casualty, hostel wardens, ambulance, and security.
            </p>
          </div>

          ${isWarden ? `
            <button class="btn btn-primary" onclick="emergency.openAddModal()">
              <i class="fa-solid fa-plus"></i> Add Emergency Contact
            </button>
          ` : ''}
        </div>

        <!-- Emergency Directory Cards Grid -->
        <div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 18px;">
          ${this.data.map(c => `
            <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:18px; display:flex; flex-direction:column; justify-content:space-between; box-shadow:var(--shadow-sm);">
              <div>
                <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:8px;">
                  <span class="badge badge-danger" style="font-size:0.75rem;">
                    ${c.availability || '24x7 Available'}
                  </span>
                  ${isWarden ? `
                    <div style="display:flex; gap:6px;">
                      <button class="btn btn-outline btn-sm" onclick="emergency.openEditModal('${c.id}')"><i class="fa-solid fa-pen"></i></button>
                      <button class="btn btn-danger btn-sm" onclick="emergency.deleteContact('${c.id}')"><i class="fa-solid fa-trash"></i></button>
                    </div>
                  ` : ''}
                </div>

                <div style="font-size:1.1rem; font-weight:700; color:var(--secondary); margin-bottom:2px;">
                  ${c.title}
                </div>
                <div style="font-size:0.9rem; font-weight:600; color:#334155; margin-bottom:6px;">
                  ${c.name}
                </div>
                <div style="font-size:0.8rem; color:var(--text-muted); margin-bottom:12px;">
                  <i class="fa-solid fa-location-dot"></i> ${c.location || 'Campus'}
                </div>
              </div>

              <div>
                <div style="font-family:monospace; font-size:1.1rem; font-weight:800; color:var(--primary); margin-bottom:10px;">
                  ${c.phone}
                </div>
                <div style="display:flex; gap:8px;">
                  <a href="tel:${c.phone}" class="btn btn-danger btn-sm" style="flex:1;">
                    <i class="fa-solid fa-phone"></i> Call Primary
                  </a>
                  ${c.altPhone ? `
                    <a href="tel:${c.altPhone}" class="btn btn-outline btn-sm" style="flex:1;">
                      <i class="fa-solid fa-phone"></i> Call Alt
                    </a>
                  ` : ''}
                </div>
              </div>
            </div>
          `).join('')}
        </div>
      </div>
    `;
  },

  openAddModal() {
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:14px;">
        <i class="fa-solid fa-plus" style="color:var(--danger);"></i> Add Emergency Contact
      </h3>
      <form onsubmit="emergency.saveContactSubmit(event, '')">
        <div class="form-group">
          <label class="form-label">Service / Designation *</label>
          <input type="text" id="contactTitle" class="form-input" placeholder="e.g. Ambulance / Doctor on Call" required>
        </div>
        <div class="form-group">
          <label class="form-label">Contact Person or Hospital Name *</label>
          <input type="text" id="contactName" class="form-input" placeholder="e.g. Dr. Mathew / Mary Queens Casualty" required>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Primary Phone *</label>
            <input type="tel" id="contactPhone" class="form-input" placeholder="+91 94471 00000" required>
          </div>
          <div class="form-group">
            <label class="form-label">Alternative Phone</label>
            <input type="tel" id="contactAltPhone" class="form-input" placeholder="Landline or mobile">
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Location / Room</label>
            <input type="text" id="contactLocation" class="form-input" placeholder="e.g. Campus Gate 2 / Peermade">
          </div>
          <div class="form-group">
            <label class="form-label">Availability</label>
            <input type="text" id="contactAvail" class="form-input" value="24x7 Available">
          </div>
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk"></i> Save Contact</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  openEditModal(id) {
    const c = this.data.find(x => x.id === id);
    if (!c) return;

    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:14px;">
        <i class="fa-solid fa-pen" style="color:var(--primary);"></i> Edit Emergency Contact
      </h3>
      <form onsubmit="emergency.saveContactSubmit(event, '${c.id}')">
        <div class="form-group">
          <label class="form-label">Service / Designation *</label>
          <input type="text" id="contactTitle" class="form-input" value="${c.title || ''}" required>
        </div>
        <div class="form-group">
          <label class="form-label">Contact Person or Service *</label>
          <input type="text" id="contactName" class="form-input" value="${c.name || ''}" required>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Primary Phone *</label>
            <input type="tel" id="contactPhone" class="form-input" value="${c.phone || ''}" required>
          </div>
          <div class="form-group">
            <label class="form-label">Alternative Phone</label>
            <input type="tel" id="contactAltPhone" class="form-input" value="${c.altPhone || ''}">
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Location</label>
            <input type="text" id="contactLocation" class="form-input" value="${c.location || ''}">
          </div>
          <div class="form-group">
            <label class="form-label">Availability</label>
            <input type="text" id="contactAvail" class="form-input" value="${c.availability || '24x7 Available'}">
          </div>
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk"></i> Update</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async saveContactSubmit(e, id) {
    e.preventDefault();
    const payload = {
      id: id,
      title: document.getElementById('contactTitle').value.trim(),
      name: document.getElementById('contactName').value.trim(),
      phone: document.getElementById('contactPhone').value.trim(),
      altPhone: document.getElementById('contactAltPhone').value.trim(),
      location: document.getElementById('contactLocation').value.trim(),
      availability: document.getElementById('contactAvail').value.trim(),
      priority: 5
    };

    const res = await app.api('/emergency/save', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      emergency.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  async deleteContact(contactId) {
    if (!confirm('Are you sure you want to remove this emergency contact?')) return;

    const res = await app.api('/emergency/delete', {
      method: 'POST',
      body: JSON.stringify({ contactId })
    });

    if (res.success) {
      app.showToast(res.message, 'info');
      emergency.render();
    } else {
      app.showToast(res.message, 'error');
    }
  }
};
