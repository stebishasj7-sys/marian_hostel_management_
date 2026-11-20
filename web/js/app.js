// ==========================================================================
// Marian Hostel Management System - Core App Controller
// ==========================================================================

const app = {
  currentUser: null,
  activeTab: 'dashboard',

  init() {
    // Check saved session
    const saved = localStorage.getItem('marian_user');
    if (saved) {
      try {
        this.currentUser = JSON.parse(saved);
      } catch (e) {
        localStorage.removeItem('marian_user');
      }
    }

    // Initialize auth status
    auth.checkWardenStatus();

    if (this.currentUser) {
      this.renderAuthenticatedUI();
      this.navigate(this.activeTab);
    } else {
      this.renderUnauthenticatedUI();
    }

    // Check for active emergency broadcast
    this.checkEmergencyAlert();
    setInterval(() => {
      if (this.currentUser) {
        this.checkEmergencyAlert();
      }
    }, 15000);
  },

  async api(endpoint, options = {}) {
    const headers = options.headers || {};
    headers['Content-Type'] = 'application/json';
    if (this.currentUser) {
      headers['X-User-Id'] = this.currentUser.id;
    }

    const config = {
      ...options,
      headers
    };

    try {
      const res = await fetch('/api' + endpoint, config);
      const data = await res.json();
      return data;
    } catch (err) {
      console.error('[API Error]', endpoint, err);
      return { success: false, message: 'Network or server error.' };
    }
  },

  renderAuthenticatedUI() {
    document.getElementById('viewAuth').style.display = 'none';
    document.getElementById('mainNav').style.display = 'flex';
    document.getElementById('userPill').style.display = 'flex';
    document.getElementById('btnLogout').style.display = 'inline-flex';
    document.getElementById('btnQuickChat').style.display = 'inline-flex';

    const u = this.currentUser;
    document.getElementById('userNameHeader').textContent = u.name || 'User';
    document.getElementById('userRoleHeader').textContent = u.role === 'WARDEN' ? 'Hostel Warden' : 'Student (Rm ' + (u.roomNo || 'N/A') + ')';

    const avatar = document.getElementById('userAvatarSmall');
    if (u.photoUrl && u.photoUrl.startsWith('http') || (u.photoUrl && u.photoUrl.startsWith('data:image'))) {
      avatar.innerHTML = `<img src="${u.photoUrl}" style="width:100%;height:100%;border-radius:50%;object-fit:cover;">`;
    } else {
      avatar.textContent = (u.name || 'U').charAt(0).toUpperCase();
    }
  },

  renderUnauthenticatedUI() {
    document.getElementById('viewAuth').style.display = 'block';
    document.getElementById('mainNav').style.display = 'none';
    document.getElementById('userPill').style.display = 'none';
    document.getElementById('btnLogout').style.display = 'none';
    document.getElementById('btnQuickChat').style.display = 'none';

    // Hide all view panels
    const views = ['viewDashboard', 'viewProfile', 'viewRooms', 'viewComplaints', 'viewOutpass', 'viewFees', 'viewAttendance', 'viewMess', 'viewNotices', 'viewChat', 'viewEmergency'];
    views.forEach(id => {
      const el = document.getElementById(id);
      if (el) el.style.display = 'none';
    });
  },

  navigate(tabName) {
    if (!this.currentUser) {
      this.renderUnauthenticatedUI();
      return;
    }

    this.activeTab = tabName;

    // Update nav item active state
    document.querySelectorAll('.nav-item').forEach(el => {
      if (el.getAttribute('data-tab') === tabName) {
        el.classList.add('active');
      } else {
        el.classList.remove('active');
      }
    });

    // Hide all view divs
    const views = ['viewDashboard', 'viewProfile', 'viewRooms', 'viewComplaints', 'viewOutpass', 'viewFees', 'viewAttendance', 'viewMess', 'viewNotices', 'viewChat', 'viewEmergency'];
    views.forEach(id => {
      const el = document.getElementById(id);
      if (el) el.style.display = 'none';
    });

    window.scrollTo({ top: 0, behavior: 'smooth' });

    // Render corresponding view
    switch (tabName) {
      case 'dashboard':
        this.renderDashboard();
        break;
      case 'profile':
        profile.render();
        break;
      case 'rooms':
        rooms.render();
        break;
      case 'complaints':
        complaints.render();
        break;
      case 'outpass':
        outpass.render();
        break;
      case 'fees':
        fees.render();
        break;
      case 'attendance':
        attendance.render();
        break;
      case 'mess':
        messTimetable.render();
        break;
      case 'notices':
        notices.render();
        break;
      case 'chat':
        chat.render();
        break;
      case 'emergency':
        emergency.render();
        break;
      default:
        this.renderDashboard();
    }
  },

  async renderDashboard() {
    const container = document.getElementById('viewDashboard');
    container.style.display = 'block';

    const u = this.currentUser;
    const isWarden = u.role === 'WARDEN';

    // Fetch snapshot data
    const noticesData = await this.api('/notices');
    const outpassData = await this.api('/outpass');
    const feeData = await this.api('/fees');

    const latestNotice = noticesData.notices && noticesData.notices.length > 0 ? noticesData.notices[0] : null;
    const myPasses = outpassData.outpasses || [];
    const activePass = myPasses.length > 0 ? myPasses[0] : null;

    container.innerHTML = `
      <!-- Welcome Banner -->
      <div class="card" style="background: linear-gradient(135deg, #1e3a8a, #2563eb); color: white; border: none; padding: 28px;">
        <div style="display:flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 16px;">
          <div>
            <span class="badge" style="background: rgba(255,255,255,0.2); color:white; margin-bottom: 8px;">
              ${isWarden ? '🛡️ Hostel Administration' : '🎓 Student Resident Portal'}
            </span>
            <h2 style="font-size: 1.6rem; font-weight: 700; margin-bottom: 4px;">Welcome back, ${u.name}!</h2>
            <p style="opacity: 0.9; font-size: 0.92rem;">
              Marian College Hostel • ${isWarden ? 'Administrative Supervision Active' : 'Room ' + (u.roomNo || 'Unassigned') + ' • Berth ' + (u.bedNo || 'L-1')}
            </p>
          </div>
          <div style="display:flex; gap: 10px;">
            <button class="btn btn-outline" style="color:white; border-color:rgba(255,255,255,0.4);" onclick="app.navigate('rooms')">
              <i class="fa-solid fa-train"></i> Berth Map
            </button>
            <button class="btn" style="background:white; color:#1e3a8a;" onclick="app.navigate('${isWarden ? 'outpass' : 'outpass'}')">
              <i class="fa-solid fa-ticket"></i> ${isWarden ? 'Verify Outpasses' : 'Book Outpass'}
            </button>
          </div>
        </div>
      </div>

      <!-- Quick Metrics Grid -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; margin-bottom: 24px;">
        
        <div class="card" style="margin:0; cursor:pointer;" onclick="app.navigate('rooms')">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
            <span style="font-size:0.85rem; color:var(--text-muted); font-weight:600;">ROOM OCCUPANCY</span>
            <span style="color:#2563eb; font-size:1.2rem;"><i class="fa-solid fa-bed"></i></span>
          </div>
          <div style="font-size: 1.5rem; font-weight:700;">Floor 1, 2, 3</div>
          <span class="badge badge-success" style="margin-top:6px;">Railway Layout Active</span>
        </div>

        <div class="card" style="margin:0; cursor:pointer;" onclick="app.navigate('outpass')">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
            <span style="font-size:0.85rem; color:var(--text-muted); font-weight:600;">DIGITAL OUTPASS</span>
            <span style="color:#8b5cf6; font-size:1.2rem;"><i class="fa-solid fa-ticket"></i></span>
          </div>
          <div style="font-size: 1.5rem; font-weight:700;">
            ${activePass ? activePass.status : 'No Active Pass'}
          </div>
          <span class="badge ${activePass && activePass.status === 'APPROVED' ? 'badge-success' : 'badge-warning'}" style="margin-top:6px;">
            ${activePass ? activePass.tokenNo : 'Tap to Book'}
          </span>
        </div>

        <div class="card" style="margin:0; cursor:pointer;" onclick="app.navigate('fees')">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
            <span style="font-size:0.85rem; color:var(--text-muted); font-weight:600;">FEE STATUS</span>
            <span style="color:#10b981; font-size:1.2rem;"><i class="fa-solid fa-wallet"></i></span>
          </div>
          <div style="font-size: 1.5rem; font-weight:700;">
            ${isWarden ? '₹' + (feeData.totalPending || 0) + ' Dues' : (feeData.pendingCount > 0 ? 'Dues Pending' : 'All Clear')}
          </div>
          <span class="badge ${feeData.pendingCount > 0 ? 'badge-warning' : 'badge-success'}" style="margin-top:6px;">
            ${isWarden ? feeData.paidCount + ' Students Paid' : 'Monthly Tracking'}
          </span>
        </div>

        <div class="card" style="margin:0; cursor:pointer;" onclick="app.navigate('complaints')">
          <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 8px;">
            <span style="font-size:0.85rem; color:var(--text-muted); font-weight:600;">COMPLAINTS FEED</span>
            <span style="color:#f59e0b; font-size:1.2rem;"><i class="fa-solid fa-comments"></i></span>
          </div>
          <div style="font-size: 1.5rem; font-weight:700;">Community Feed</div>
          <span class="badge badge-primary" style="margin-top:6px;">Likes & Comments Live</span>
        </div>

      </div>

      <!-- Two Column Layout: Notices & Today's Menu -->
      <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 24px;">
        
        <!-- Latest Notice Card -->
        <div class="card" style="margin:0;">
          <div class="card-header">
            <div class="card-title"><i class="fa-solid fa-bullhorn" style="color:var(--primary);"></i> Latest Notice</div>
            <button class="btn btn-outline btn-sm" onclick="app.navigate('notices')">View All</button>
          </div>
          ${latestNotice ? `
            <div style="border-left: 3px solid ${latestNotice.isEmergency ? 'var(--danger)' : 'var(--primary)'}; padding-left: 12px;">
              <span class="badge ${latestNotice.isEmergency ? 'badge-danger' : 'badge-primary'}" style="margin-bottom:6px;">
                ${latestNotice.category}
              </span>
              <h4 style="font-size: 1.05rem; font-weight: 700; color: var(--secondary); margin-bottom: 6px;">${latestNotice.title}</h4>
              <p style="font-size: 0.88rem; color: var(--text-muted); line-height: 1.4; margin-bottom: 8px;">${latestNotice.content}</p>
              <div style="font-size: 0.75rem; color: #94a3b8;">
                <i class="fa-regular fa-clock"></i> ${latestNotice.dateFormatted} • Posted by ${latestNotice.postedByName}
              </div>
            </div>
          ` : '<p style="color:var(--text-muted);">No notices posted.</p>'}
        </div>

        <!-- Quick Emergency Contacts Preview -->
        <div class="card" style="margin:0;">
          <div class="card-header">
            <div class="card-title"><i class="fa-solid fa-shield-halved" style="color:var(--danger);"></i> Quick Emergency Dial</div>
            <button class="btn btn-outline btn-sm" onclick="app.navigate('emergency')">Full Directory</button>
          </div>
          <div style="display:flex; flex-direction: column; gap: 10px;">
            <div style="display:flex; justify-content:space-between; align-items:center; background:#f8fafc; padding: 10px 14px; border-radius: var(--radius-md);">
              <div>
                <strong style="font-size:0.9rem; display:block;">Chief Warden Quarter</strong>
                <span style="font-size:0.75rem; color:var(--text-muted);">Dr. Sister Rose Mary (24x7)</span>
              </div>
              <a href="tel:+919447123456" class="btn btn-danger btn-sm"><i class="fa-solid fa-phone"></i> Call</a>
            </div>

            <div style="display:flex; justify-content:space-between; align-items:center; background:#f8fafc; padding: 10px 14px; border-radius: var(--radius-md);">
              <div>
                <strong style="font-size:0.9rem; display:block;">Campus Health Clinic & Ambulance</strong>
                <span style="font-size:0.75rem; color:var(--text-muted);">Emergency Medical Duty</span>
              </div>
              <a href="tel:+919446011999" class="btn btn-danger btn-sm"><i class="fa-solid fa-phone"></i> Call</a>
            </div>

            <div style="display:flex; justify-content:space-between; align-items:center; background:#f8fafc; padding: 10px 14px; border-radius: var(--radius-md);">
              <div>
                <strong style="font-size:0.9rem; display:block;">Main Security Gate</strong>
                <span style="font-size:0.75rem; color:var(--text-muted);">Security Checkpoint</span>
              </div>
              <a href="tel:04869232111" class="btn btn-secondary btn-sm"><i class="fa-solid fa-phone"></i> Call</a>
            </div>
          </div>
        </div>

      </div>
    `;
  },

  async checkEmergencyAlert() {
    try {
      const data = await this.api('/notices');
      const banner = document.getElementById('emergencyBanner');
      const text = document.getElementById('emergencyBannerText');

      if (data.activeEmergency && data.activeEmergency.isEmergency) {
        text.textContent = data.activeEmergency.title + ': ' + data.activeEmergency.content;
        banner.style.display = 'flex';
      } else {
        banner.style.display = 'none';
      }
    } catch (e) {}
  },

  dismissEmergencyBanner() {
    document.getElementById('emergencyBanner').style.display = 'none';
  },

  showModal(contentHtml) {
    document.getElementById('modalBody').innerHTML = contentHtml;
    document.getElementById('globalModal').style.display = 'flex';
  },

  closeModal() {
    document.getElementById('globalModal').style.display = 'none';
    document.getElementById('modalBody').innerHTML = '';
  },

  closeModalOnBackdrop(e) {
    if (e.target.id === 'globalModal') {
      this.closeModal();
    }
  },

  showToast(message, type = 'info') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    
    let icon = '<i class="fa-solid fa-circle-info"></i>';
    if (type === 'success') icon = '<i class="fa-solid fa-circle-check"></i>';
    if (type === 'error') icon = '<i class="fa-solid fa-triangle-exclamation"></i>';

    toast.innerHTML = `${icon} <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
      toast.style.opacity = '0';
      toast.style.transform = 'translateY(10px)';
      toast.style.transition = 'all 0.3s ease';
      setTimeout(() => toast.remove(), 300);
    }, 4000);
  }
};

window.addEventListener('DOMContentLoaded', () => {
  app.init();
});
