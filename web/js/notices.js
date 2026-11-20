// ==========================================================================
// Marian Hostel Management System - Notice Board & Sudden Emergency Alerts
// ==========================================================================

const notices = {
  data: [],

  async render() {
    const container = document.getElementById('viewNotices');
    container.style.display = 'block';

    const res = await app.api('/notices');
    this.data = res.notices || [];

    const isWarden = app.currentUser.role === 'WARDEN';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-bell" style="color:var(--primary);"></i>
              Official Marian Hostel Notice Board
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Announcements, disciplinary circulars, and instant emergency broadcasts.
            </p>
          </div>

          ${isWarden ? `
            <div style="display:flex; gap:10px;">
              <button class="btn btn-danger" onclick="notices.openCreateModal(true)">
                <i class="fa-solid fa-triangle-exclamation"></i> Broadcast Sudden Emergency
              </button>
              <button class="btn btn-primary" onclick="notices.openCreateModal(false)">
                <i class="fa-solid fa-plus"></i> Post Circular / Notice
              </button>
            </div>
          ` : ''}
        </div>

        <!-- Notices Stream -->
        ${this.data.length === 0 ? '<p style="color:var(--text-muted); text-align:center; padding:30px;">No active notices.</p>' : ''}

        <div style="display:flex; flex-direction:column; gap:16px;">
          ${this.data.map(n => `
            <div style="background:${n.isEmergency ? '#fef2f2' : 'white'}; border:1px solid ${n.isEmergency ? '#fecaca' : 'var(--border)'}; border-left: 5px solid ${n.isEmergency ? '#dc2626' : '#2563eb'}; border-radius:var(--radius-md); padding:20px; box-shadow:var(--shadow-sm);">
              <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:8px;">
                <div>
                  <div style="display:flex; align-items:center; gap:8px; margin-bottom:6px;">
                    <span class="badge ${n.isEmergency ? 'badge-danger' : 'badge-primary'}">
                      ${n.isEmergency ? '🚨 URGENT EMERGENCY' : n.category}
                    </span>
                    <span style="font-size:0.75rem; color:#94a3b8;"><i class="fa-regular fa-clock"></i> ${n.dateFormatted}</span>
                  </div>
                  <h3 style="font-size:1.15rem; font-weight:700; color:var(--secondary);">${n.title}</h3>
                </div>

                ${isWarden ? `
                  <button class="btn btn-danger btn-sm" onclick="notices.deleteNotice('${n.id}')" title="Delete Notice">
                    <i class="fa-solid fa-trash"></i>
                  </button>
                ` : ''}
              </div>

              <p style="font-size:0.92rem; color:#334155; line-height:1.5; margin-bottom:12px;">
                ${n.content}
              </p>

              <div style="font-size:0.8rem; color:#64748b; border-top:1px solid ${n.isEmergency ? '#fee2e2' : '#f1f5f9'}; padding-top:8px;">
                <i class="fa-solid fa-user-shield"></i> Authorized By: <strong>${n.postedByName || 'Hostel Warden'}</strong>
              </div>
            </div>
          `).join('')}
        </div>
      </div>
    `;
  },

  openCreateModal(isEmergency = false) {
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:${isEmergency ? '#dc2626' : 'var(--secondary)'}; margin-bottom:14px;">
        <i class="fa-solid ${isEmergency ? 'fa-triangle-exclamation' : 'fa-bullhorn'}"></i>
        ${isEmergency ? 'Broadcast Urgent Emergency Notice' : 'Publish Hostel Notice'}
      </h3>

      ${isEmergency ? `
        <div style="background:#fef2f2; border:1px solid #fecaca; color:#991b1b; padding:10px 14px; border-radius:6px; font-size:0.85rem; margin-bottom:14px;">
          <strong>Note:</strong> Emergency notices trigger a high-priority pulsating top banner on all student portals.
        </div>
      ` : ''}

      <form onsubmit="notices.handleCreateSubmit(event, ${isEmergency})">
        <div class="form-group">
          <label class="form-label">Category</label>
          <select id="noticeCategory" class="form-select">
            <option value="General" ${!isEmergency ? 'selected' : ''}>General Information</option>
            <option value="Emergency" ${isEmergency ? 'selected' : ''}>Emergency / Water / Power Alert</option>
            <option value="Academic">Academic / Study Hours</option>
            <option value="Mess">Mess / Dining Schedule</option>
            <option value="Maintenance">Maintenance & Cleaning</option>
            <option value="Cultural">Hostel Events & Cultural</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Notice Headline *</label>
          <input type="text" id="noticeTitle" class="form-input" placeholder="e.g. Water Supply Interruption / Annual Hostel Day" required>
        </div>

        <div class="form-group">
          <label class="form-label">Full Circular Content *</label>
          <textarea id="noticeContent" class="form-textarea" rows="4" placeholder="Enter full announcement details for residents..." required></textarea>
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn ${isEmergency ? 'btn-danger' : 'btn-primary'}">
            <i class="fa-solid fa-bullhorn"></i> ${isEmergency ? 'Broadcast Alert Now' : 'Publish Notice'}
          </button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async handleCreateSubmit(e, isEmergency) {
    e.preventDefault();
    const payload = {
      title: document.getElementById('noticeTitle').value.trim(),
      category: document.getElementById('noticeCategory').value,
      content: document.getElementById('noticeContent').value.trim(),
      isEmergency: isEmergency
    };

    const res = await app.api('/notices/create', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      notices.render();
      app.checkEmergencyAlert();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  async deleteNotice(noticeId) {
    if (!confirm('Are you sure you want to delete this notice?')) return;

    const res = await app.api('/notices/delete', {
      method: 'POST',
      body: JSON.stringify({ noticeId })
    });

    if (res.success) {
      app.showToast('Notice deleted.', 'info');
      notices.render();
      app.checkEmergencyAlert();
    }
  }
};
