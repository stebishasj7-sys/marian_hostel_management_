// ==========================================================================
// Marian Hostel Management System - Social Complaint Management
// ==========================================================================

const complaints = {
  data: [],

  async render() {
    const container = document.getElementById('viewComplaints');
    container.style.display = 'block';

    const res = await app.api('/complaints');
    this.data = res.complaints || [];

    const isWarden = app.currentUser.role === 'WARDEN';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div>
            <div class="card-title">
              <i class="fa-solid fa-bullhorn" style="color:var(--primary);"></i>
              Hostel Complaint Management & Community Feed
            </div>
            <p style="font-size:0.85rem; color:var(--text-muted); margin-top:4px;">
              Post issues, like existing reports, add comments, and track Warden resolution verification.
            </p>
          </div>
          <button class="btn btn-primary" onclick="complaints.openCreateModal()">
            <i class="fa-solid fa-plus"></i> Register New Complaint
          </button>
        </div>

        <!-- Metrics Overview -->
        <div style="display:flex; gap:16px; margin-bottom:20px; flex-wrap:wrap;">
          <div style="background:#f8fafc; border:1px solid #e2e8f0; padding:10px 18px; border-radius:var(--radius-md);">
            <span style="font-size:0.75rem; color:var(--text-muted); text-transform:uppercase; font-weight:700;">Total Complaints</span>
            <div style="font-size:1.2rem; font-weight:700;">${res.totalComplaints || 0}</div>
          </div>
          <div style="background:#fffbeb; border:1px solid #fde68a; padding:10px 18px; border-radius:var(--radius-md);">
            <span style="font-size:0.75rem; color:#92400e; text-transform:uppercase; font-weight:700;">Pending Review</span>
            <div style="font-size:1.2rem; font-weight:700; color:#b45309;">${res.pendingCount || 0}</div>
          </div>
          <div style="background:#ecfdf5; border:1px solid #a7f3d0; padding:10px 18px; border-radius:var(--radius-md);">
            <span style="font-size:0.75rem; color:#065f46; text-transform:uppercase; font-weight:700;">Resolved & Cleared</span>
            <div style="font-size:1.2rem; font-weight:700; color:#059669;">${res.resolvedCount || 0}</div>
          </div>
        </div>

        <!-- Complaints Stream -->
        <div id="complaintsList">
          ${this.data.length === 0 ? '<p style="color:var(--text-muted); text-align:center; padding:30px;">No complaints registered yet.</p>' : ''}
          ${this.data.map(c => this.renderComplaintItem(c, isWarden)).join('')}
        </div>
      </div>
    `;
  },

  renderComplaintItem(c, isWarden) {
    const isLiked = c.likes && c.likes.includes(app.currentUser.id);
    const isResolved = c.status === 'RESOLVED';

    return `
      <div class="complaint-item ${isResolved ? 'resolved' : 'pending'}">
        <div class="complaint-header">
          <div>
            <span class="badge ${isResolved ? 'badge-success' : 'badge-warning'}" style="margin-bottom:6px;">
              ${c.category}
            </span>
            <div class="complaint-title">${c.title}</div>
            <div class="complaint-meta">
              Reported by <strong>${c.studentName}</strong> (Room ${c.studentRoom || 'N/A'} • ${c.studentDept || ''}) • <i class="fa-regular fa-clock"></i> ${c.dateFormatted}
            </div>
          </div>

          <!-- PROMINENT WARDEN TICK MARK AS REQUESTED -->
          <div>
            ${c.wardenTick ? `
              <div class="warden-tick-badge" title="Cleared & verified by Warden">
                <i class="fa-solid fa-circle-check" style="font-size:1.1rem; color:#10b981;"></i>
                <span>VERIFIED & RESOLVED</span>
              </div>
            ` : `
              <span class="badge badge-warning"><i class="fa-solid fa-hourglass-half"></i> Pending Action</span>
            `}
          </div>
        </div>

        <p style="font-size:0.92rem; color:#334155; line-height:1.5; margin-bottom:12px;">
          ${c.description}
        </p>

        <!-- If resolved, show Warden remarks box -->
        ${c.wardenRemarks ? `
          <div style="background:#f0fdf4; border-left:3px solid #10b981; padding:8px 14px; border-radius:4px; font-size:0.85rem; color:#166534; margin-bottom:12px;">
            <strong><i class="fa-solid fa-user-shield"></i> Warden Note (${c.resolvedBy || 'Warden'}):</strong> ${c.wardenRemarks}
          </div>
        ` : ''}

        <!-- Social Action Buttons: Likes & Comment toggles & Warden Tick button -->
        <div class="complaint-actions">
          <button class="social-btn ${isLiked ? 'liked' : ''}" onclick="complaints.toggleLike('${c.id}')">
            <i class="fa-${isLiked ? 'solid' : 'regular'} fa-heart"></i>
            <span>${c.likesCount || 0} Likes</span>
          </button>

          <button class="social-btn" onclick="complaints.toggleCommentBox('${c.id}')">
            <i class="fa-regular fa-comment-dots"></i>
            <span>${c.comments ? c.comments.length : 0} Comments</span>
          </button>

          ${isWarden && !isResolved ? `
            <button class="btn btn-success btn-sm" style="margin-left:auto;" onclick="complaints.openResolveModal('${c.id}')">
              <i class="fa-solid fa-check"></i> Put Warden Tick Mark (Clear)
            </button>
          ` : ''}
        </div>

        <!-- Comments Container -->
        <div id="commentsBox-${c.id}" class="comments-container" style="display:block;">
          <div style="font-size:0.82rem; font-weight:700; color:#64748b; margin-bottom:8px;">
            Discussion & Responses:
          </div>
          
          <div id="commentList-${c.id}">
            ${(!c.comments || c.comments.length === 0) ? '<p style="font-size:0.82rem; color:#94a3b8;">No comments yet. Be the first to comment!</p>' : ''}
            ${(c.comments || []).map(cm => `
              <div class="comment-row">
                <span class="comment-author">${cm.userName}</span>
                <span class="badge ${cm.userRole === 'WARDEN' ? 'badge-primary' : 'badge-neutral'}" style="font-size:0.65rem; padding:1px 6px;">
                  ${cm.userRole}
                </span>
                <span style="color:#64748b; font-size:0.75rem; margin-left:6px;">${cm.dateFormatted || ''}</span>
                <div style="color:#334155; margin-top:2px;">${cm.text}</div>
              </div>
            `).join('')}
          </div>

          <!-- Add Comment Bar -->
          <div style="display:flex; gap:8px; margin-top:12px;">
            <input type="text" id="commentInput-${c.id}" class="form-input" style="padding:6px 12px; font-size:0.85rem;" placeholder="Add a comment or support this complaint...">
            <button class="btn btn-primary btn-sm" onclick="complaints.postComment('${c.id}')">Post</button>
          </div>
        </div>
      </div>
    `;
  },

  toggleCommentBox(id) {
    const box = document.getElementById(`commentsBox-${id}`);
    if (box) {
      box.style.display = box.style.display === 'none' ? 'block' : 'none';
    }
  },

  async toggleLike(complaintId) {
    const res = await app.api('/complaints/like', {
      method: 'POST',
      body: JSON.stringify({ complaintId })
    });

    if (res.success) {
      complaints.render();
    }
  },

  async postComment(complaintId) {
    const input = document.getElementById(`commentInput-${complaintId}`);
    const text = input ? input.value.trim() : '';

    if (!text) {
      app.showToast('Comment text cannot be empty.', 'error');
      return;
    }

    const res = await app.api('/complaints/comment', {
      method: 'POST',
      body: JSON.stringify({ complaintId, text })
    });

    if (res.success) {
      app.showToast('Comment posted.', 'success');
      complaints.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  openCreateModal() {
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; margin-bottom:14px; color:var(--secondary);">
        <i class="fa-solid fa-bullhorn" style="color:var(--primary);"></i> Submit Hostel Complaint
      </h3>
      <form onsubmit="complaints.handleCreateSubmit(event)">
        <div class="form-group">
          <label class="form-label">Category *</label>
          <select id="compCategory" class="form-select" required>
            <option value="Electrical">Electrical (Lights, Fan, Switch)</option>
            <option value="Plumbing">Plumbing (Tap, Shower, Geyser, Drain)</option>
            <option value="Wi-Fi/Network">Wi-Fi & Internet Connectivity</option>
            <option value="Cleanliness">Cleanliness & Waste Management</option>
            <option value="Mess/Food">Mess Food Quality & Timings</option>
            <option value="Furniture/Carpentry">Furniture & Bed Repair</option>
            <option value="Noise/Discipline">Noise & Disturbance</option>
            <option value="Other">Other Specific Issue</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">Complaint Title *</label>
          <input type="text" id="compTitle" class="form-input" placeholder="Brief summary of the issue" required>
        </div>

        <div class="form-group">
          <label class="form-label">Room / Block Location</label>
          <input type="text" id="compRoom" class="form-input" value="${app.currentUser.roomNo || ''}" placeholder="e.g. Room 102 or 2nd Floor Corridor">
        </div>

        <div class="form-group">
          <label class="form-label">Detailed Description *</label>
          <textarea id="compDesc" class="form-textarea" rows="3" placeholder="Provide full details so maintenance staff can resolve it quickly..." required></textarea>
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-paper-plane"></i> Submit Complaint</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async handleCreateSubmit(e) {
    e.preventDefault();
    const payload = {
      category: document.getElementById('compCategory').value,
      title: document.getElementById('compTitle').value.trim(),
      roomNo: document.getElementById('compRoom').value.trim(),
      description: document.getElementById('compDesc').value.trim()
    };

    const res = await app.api('/complaints/create', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      complaints.render();
    } else {
      app.showToast(res.message, 'error');
    }
  },

  openResolveModal(complaintId) {
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; color:var(--secondary); margin-bottom:12px;">
        <i class="fa-solid fa-circle-check" style="color:var(--success);"></i> Warden Verification & Green Tick Seal
      </h3>
      <p style="font-size:0.9rem; color:var(--text-muted); margin-bottom:14px;">
        Confirming this complaint will mark it as <strong>RESOLVED</strong> with a prominent green checkmark visible to all hostel students.
      </p>

      <div class="form-group">
        <label class="form-label">Warden Resolution Remarks</label>
        <textarea id="resolveRemarks" class="form-textarea" rows="2" placeholder="e.g. Electrician Babu repaired the socket and tested voltage. All clear."></textarea>
      </div>

      <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
        <button class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
        <button class="btn btn-success" onclick="complaints.submitResolve('${complaintId}')">
          <i class="fa-solid fa-check-double"></i> Confirm & Put Tick Mark
        </button>
      </div>
    `;
    app.showModal(modalHtml);
  },

  async submitResolve(complaintId) {
    const remarks = document.getElementById('resolveRemarks').value.trim();
    const res = await app.api('/complaints/resolve', {
      method: 'POST',
      body: JSON.stringify({ complaintId, remarks })
    });

    if (res.success) {
      app.showToast(res.message, 'success');
      app.closeModal();
      complaints.render();
    } else {
      app.showToast(res.message, 'error');
    }
  }
};
