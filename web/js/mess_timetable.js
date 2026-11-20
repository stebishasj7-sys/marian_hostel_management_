// ==========================================================================
// Marian Hostel Management System - Mess Menu & Timetable Module
// ==========================================================================

const messTimetable = {
  data: null,
  activeSubTab: 'menu', // 'menu' or 'timetable'

  async render() {
    const container = document.getElementById('viewMess');
    container.style.display = 'block';

    const res = await app.api('/mess-timetable');
    this.data = res;

    const isWarden = app.currentUser.role === 'WARDEN';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-utensils" style="color:var(--primary);"></i>
              Hostel Mess Menu & Daily Routine Timetable
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Nutritious catering schedule & daily hostel discipline routine.
            </p>
          </div>

          <!-- Sub-tabs switch -->
          <div style="display:flex; gap:8px;">
            <button class="btn ${this.activeSubTab === 'menu' ? 'btn-primary' : 'btn-outline'}" onclick="messTimetable.switchSubTab('menu')">
              <i class="fa-solid fa-bowl-food"></i> Weekly Mess Menu
            </button>
            <button class="btn ${this.activeSubTab === 'timetable' ? 'btn-primary' : 'btn-outline'}" onclick="messTimetable.switchSubTab('timetable')">
              <i class="fa-solid fa-clock"></i> Daily Hostel Schedule
            </button>
          </div>
        </div>

        <div id="subTabContent">
          ${this.activeSubTab === 'menu' ? this.renderMessMenu(isWarden) : this.renderTimetable()}
        </div>
      </div>
    `;
  },

  switchSubTab(tab) {
    this.activeSubTab = tab;
    this.render();
  },

  renderMessMenu(isWarden) {
    const menus = this.data.messMenus || [];
    const todayDayName = new Date().toLocaleDateString('en-US', { weekday: 'long' });

    return `
      <div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 18px;">
        ${menus.map(m => {
          const isToday = m.day.equalsIgnoreCase ? m.day.equalsIgnoreCase(todayDayName) : m.day.toLowerCase() === todayDayName.toLowerCase();
          
          return `
            <div class="card" style="margin:0; border-top: 4px solid ${isToday ? 'var(--primary)' : 'var(--border)'}; background: ${isToday ? '#f0f9ff' : 'white'};">
              <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
                <div style="font-size:1.15rem; font-weight:800; color:var(--secondary);">${m.day}</div>
                ${isToday ? '<span class="badge badge-primary">TODAY</span>' : ''}
                ${isWarden ? `
                  <button class="btn btn-outline btn-sm" onclick="messTimetable.openEditMenuModal('${m.day}')">
                    <i class="fa-solid fa-pen"></i> Edit
                  </button>
                ` : ''}
              </div>

              <div style="font-size:0.85rem; line-height:1.6; display:flex; flex-direction:column; gap:8px;">
                <div style="background:white; padding:8px 12px; border-radius:6px; border:1px solid #e2e8f0;">
                  <strong style="color:#d97706;"><i class="fa-solid fa-mug-saucer"></i> Breakfast:</strong>
                  <div style="color:#334155; margin-top:2px;">${m.breakfast}</div>
                </div>

                <div style="background:white; padding:8px 12px; border-radius:6px; border:1px solid #e2e8f0;">
                  <strong style="color:#2563eb;"><i class="fa-solid fa-bowl-rice"></i> Lunch:</strong>
                  <div style="color:#334155; margin-top:2px;">${m.lunch}</div>
                </div>

                <div style="background:white; padding:8px 12px; border-radius:6px; border:1px solid #e2e8f0;">
                  <strong style="color:#7c3aed;"><i class="fa-solid fa-cookie"></i> Snacks & Tea:</strong>
                  <div style="color:#334155; margin-top:2px;">${m.snacks}</div>
                </div>

                <div style="background:white; padding:8px 12px; border-radius:6px; border:1px solid #e2e8f0;">
                  <strong style="color:#059669;"><i class="fa-solid fa-plate-wheat"></i> Dinner:</strong>
                  <div style="color:#334155; margin-top:2px;">${m.dinner}</div>
                </div>

                ${m.special ? `
                  <div style="background:#ecfdf5; border:1px dashed #10b981; padding:8px 12px; border-radius:6px; font-weight:600; color:#065f46;">
                    <i class="fa-solid fa-star" style="color:#f59e0b;"></i> Special: ${m.special}
                  </div>
                ` : ''}
              </div>
            </div>
          `;
        }).join('')}
      </div>
    `;
  },

  renderTimetable() {
    const tt = this.data.timetables || [];

    return `
      <div style="display:flex; flex-direction:column; gap:12px;">
        ${tt.map(item => `
          <div style="display:flex; align-items:center; gap:20px; background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-family:monospace; font-size:1.05rem; font-weight:800; color:var(--primary); min-width:180px;">
              <i class="fa-regular fa-clock"></i> ${item.timeSlot}
            </div>
            <div style="flex:1;">
              <div style="font-size:1rem; font-weight:700; color:var(--secondary);">${item.title}</div>
              <div style="font-size:0.85rem; color:var(--text-muted); margin-top:2px;">${item.description}</div>
            </div>
            <div>
              <span class="badge badge-neutral">${item.days || 'All Days'}</span>
            </div>
          </div>
        `).join('')}
      </div>
    `;
  },

  openEditMenuModal(day) {
    const menus = this.data.messMenus || [];
    const m = menus.find(x => x.day.toLowerCase() === day.toLowerCase()) || {};

    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:14px;">
        <i class="fa-solid fa-pen" style="color:var(--primary);"></i> Edit Mess Menu • ${day}
      </h3>
      <form onsubmit="messTimetable.saveMenuUpdates(event, '${day}')">
        <div class="form-group">
          <label class="form-label">Breakfast</label>
          <input type="text" id="editMenuBreakfast" class="form-input" value="${m.breakfast || ''}" required>
        </div>
        <div class="form-group">
          <label class="form-label">Lunch</label>
          <input type="text" id="editMenuLunch" class="form-input" value="${m.lunch || ''}" required>
        </div>
        <div class="form-group">
          <label class="form-label">Evening Snacks & Tea</label>
          <input type="text" id="editMenuSnacks" class="form-input" value="${m.snacks || ''}" required>
        </div>
        <div class="form-group">
          <label class="form-label">Dinner</label>
          <input type="text" id="editMenuDinner" class="form-input" value="${m.dinner || ''}" required>
        </div>
        <div class="form-group">
          <label class="form-label">Special Dish / Sweet</label>
          <input type="text" id="editMenuSpecial" class="form-input" value="${m.special || ''}">
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk"></i> Save Menu</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async saveMenuUpdates(e, day) {
    e.preventDefault();
    const payload = {
      day,
      breakfast: document.getElementById('editMenuBreakfast').value.trim(),
      lunch: document.getElementById('editMenuLunch').value.trim(),
      snacks: document.getElementById('editMenuSnacks').value.trim(),
      dinner: document.getElementById('editMenuDinner').value.trim(),
      special: document.getElementById('editMenuSpecial').value.trim()
    };

    const res = await app.api('/mess-timetable/menu-update', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      messTimetable.render();
    } else {
      app.showToast(res.message, 'error');
    }
  }
};
