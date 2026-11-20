// ==========================================================================
// Marian Hostel Management System - Profile Management Module
// ==========================================================================

const profile = {
  async render() {
    const container = document.getElementById('viewProfile');
    container.style.display = 'block';

    const res = await app.api('/profile');
    if (!res.success) {
      container.innerHTML = `<div class="card"><p style="color:red;">Failed to load profile.</p></div>`;
      return;
    }

    const u = res.user;
    app.currentUser = u;
    localStorage.setItem('marian_user', JSON.stringify(u));

    const photoSrc = u.photoUrl && (u.photoUrl.startsWith('http') || u.photoUrl.startsWith('data:image'))
      ? u.photoUrl
      : 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80';

    container.innerHTML = `
      <div class="card">
        <div class="card-header">
          <div class="card-title">
            <i class="fa-solid fa-id-card-clip" style="color:var(--primary);"></i>
            Resident Student Profile
          </div>
          <button class="btn btn-primary btn-sm" onclick="profile.openEditModal()">
            <i class="fa-solid fa-user-pen"></i> Edit Profile Details
          </button>
        </div>

        <!-- Top Profile Banner & Avatar -->
        <div style="display:flex; gap:24px; align-items:center; flex-wrap:wrap; border-bottom:1px solid #f1f5f9; padding-bottom: 24px; margin-bottom: 24px;">
          <div style="position:relative;">
            <img src="${photoSrc}" alt="${u.name}" style="width: 110px; height: 110px; border-radius: 50%; object-fit: cover; border: 4px solid #eff6ff; box-shadow: var(--shadow-md);">
            <button onclick="profile.openPhotoUploadModal()" class="btn btn-sm" style="position:absolute; bottom:0; right:0; background:var(--primary); color:white; border-radius:50%; width:32px; height:32px; padding:0;" title="Change Profile Photo">
              <i class="fa-solid fa-camera"></i>
            </button>
          </div>

          <div style="flex:1;">
            <div style="display:flex; align-items:center; gap:12px; margin-bottom: 6px;">
              <h2 style="font-size: 1.5rem; font-weight: 700; color:var(--secondary);">${u.name}</h2>
              <span class="badge ${u.role === 'WARDEN' ? 'badge-primary' : 'badge-success'}">${u.role}</span>
            </div>
            <p style="color:var(--text-muted); font-size:0.95rem; margin-bottom:8px;">
              <i class="fa-solid fa-graduation-cap"></i> ${u.department || 'General Branch'} • 
              <i class="fa-solid fa-door-open"></i> Room ${u.roomNo || 'N/A'} (Berth: ${u.bedNo || 'L-1'})
            </p>
            <div style="display:flex; gap:16px; flex-wrap:wrap; font-size:0.85rem; color:#475569;">
              <span><i class="fa-regular fa-envelope"></i> ${u.email}</span>
              <span><i class="fa-solid fa-phone"></i> ${u.phone}</span>
              <span><i class="fa-solid fa-location-dot"></i> ${u.nationality || 'Indian'}</span>
            </div>
          </div>
        </div>

        <!-- Full Detailed Profile Grid -->
        <h3 style="font-size:1.05rem; font-weight:700; color:var(--secondary); margin-bottom: 16px;">
          <i class="fa-solid fa-address-book" style="color:var(--primary);"></i> Comprehensive Profile Information
        </h3>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 16px;">
          
          <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Admission / Roll Number</div>
            <div style="font-size:1rem; font-weight:600; margin-top:4px;">${u.admissionNo || 'Not Provided'}</div>
          </div>

          <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Aadhaar Verification Number</div>
            <div style="font-size:1rem; font-weight:600; margin-top:4px;">${u.aadhaar || 'XXXX-XXXX-XXXX'}</div>
          </div>

          <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Date of Birth & Gender</div>
            <div style="font-size:1rem; font-weight:600; margin-top:4px;">${u.dob || 'N/A'} • ${u.gender || 'N/A'}</div>
          </div>

          <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Father's Name</div>
            <div style="font-size:1rem; font-weight:600; margin-top:4px;">${u.fatherName || 'Not Provided'}</div>
          </div>

          <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Mother's Name</div>
            <div style="font-size:1rem; font-weight:600; margin-top:4px;">${u.motherName || 'Not Provided'}</div>
          </div>

          <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px;">
            <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Guardian Emergency Phone</div>
            <div style="font-size:1rem; font-weight:600; margin-top:4px; color:var(--primary); font-family:monospace;">
              ${u.guardianPhone || 'Not Provided'}
            </div>
          </div>

        </div>

        <!-- Address Card -->
        <div style="background:#f8fafc; border:1px solid #e2e8f0; border-radius:var(--radius-md); padding:16px; margin-top:16px;">
          <div style="font-size:0.78rem; text-transform:uppercase; color:var(--text-muted); font-weight:700;">Permanent Residential Address</div>
          <div style="font-size:0.95rem; font-weight:500; margin-top:4px; line-height:1.5;">${u.address || 'No permanent address recorded.'}</div>
        </div>

      </div>
    `;
  },

  openEditModal() {
    const u = app.currentUser;
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; margin-bottom:16px; color:var(--secondary);">
        <i class="fa-solid fa-user-pen" style="color:var(--primary);"></i> Edit Resident Profile
      </h3>
      <form onsubmit="profile.saveProfileUpdates(event)">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Full Name</label>
            <input type="text" id="editName" class="form-input" value="${u.name || ''}" required>
          </div>
          <div class="form-group">
            <label class="form-label">Department / Branch</label>
            <input type="text" id="editDept" class="form-input" value="${u.department || ''}">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Phone Number</label>
            <input type="tel" id="editPhone" class="form-input" value="${u.phone || ''}" required>
          </div>
          <div class="form-group">
            <label class="form-label">Gender</label>
            <select id="editGender" class="form-select">
              <option value="Male" ${u.gender === 'Male' ? 'selected' : ''}>Male</option>
              <option value="Female" ${u.gender === 'Female' ? 'selected' : ''}>Female</option>
              <option value="Other" ${u.gender === 'Other' ? 'selected' : ''}>Other</option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Date of Birth</label>
            <input type="date" id="editDob" class="form-input" value="${u.dob || ''}">
          </div>
          <div class="form-group">
            <label class="form-label">Nationality</label>
            <input type="text" id="editNationality" class="form-input" value="${u.nationality || 'Indian'}">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Admission / Roll No</label>
            <input type="text" id="editAdmissionNo" class="form-input" value="${u.admissionNo || ''}">
          </div>
          <div class="form-group">
            <label class="form-label">Aadhaar Card No</label>
            <input type="text" id="editAadhaar" class="form-input" value="${u.aadhaar || ''}">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">Permanent Address</label>
          <textarea id="editAddress" class="form-textarea" rows="2">${u.address || ''}</textarea>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Father's Name</label>
            <input type="text" id="editFather" class="form-input" value="${u.fatherName || ''}">
          </div>
          <div class="form-group">
            <label class="form-label">Mother's Name</label>
            <input type="text" id="editMother" class="form-input" value="${u.motherName || ''}">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">Guardian Emergency Phone</label>
          <input type="tel" id="editGuardianPhone" class="form-input" value="${u.guardianPhone || ''}">
        </div>

        <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:20px;">
          <button type="button" class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
          <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk"></i> Save Profile</button>
        </div>
      </form>
    `;
    app.showModal(modalHtml);
  },

  async saveProfileUpdates(e) {
    e.preventDefault();
    const payload = {
      name: document.getElementById('editName').value.trim(),
      department: document.getElementById('editDept').value.trim(),
      phone: document.getElementById('editPhone').value.trim(),
      gender: document.getElementById('editGender').value,
      dob: document.getElementById('editDob').value,
      nationality: document.getElementById('editNationality').value.trim(),
      admissionNo: document.getElementById('editAdmissionNo').value.trim(),
      aadhaar: document.getElementById('editAadhaar').value.trim(),
      address: document.getElementById('editAddress').value.trim(),
      fatherName: document.getElementById('editFather').value.trim(),
      motherName: document.getElementById('editMother').value.trim(),
      guardianPhone: document.getElementById('editGuardianPhone').value.trim()
    };

    const res = await app.api('/profile/update', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.currentUser = res.user;
      localStorage.setItem('marian_user', JSON.stringify(res.user));
      app.showToast('Profile updated successfully!', 'success');
      app.closeModal();
      app.renderAuthenticatedUI();
      profile.render();
    } else {
      app.showToast(res.message || 'Update failed', 'error');
    }
  },

  openPhotoUploadModal() {
    const modalHtml = `
      <h3 style="font-size:1.2rem; font-weight:700; margin-bottom:14px; color:var(--secondary);">
        <i class="fa-solid fa-camera" style="color:var(--primary);"></i> Update Profile Photo
      </h3>
      <p style="font-size:0.88rem; color:var(--text-muted); margin-bottom:16px;">
        Upload a file from your computer or paste an image URL.
      </p>

      <div class="form-group">
        <label class="form-label">Upload Image File</label>
        <input type="file" id="modalPhotoFile" accept="image/*" onchange="profile.handleModalPhotoFile(event)" class="form-input">
      </div>

      <div class="form-group">
        <label class="form-label">Or Image URL</label>
        <input type="text" id="modalPhotoUrl" class="form-input" placeholder="https://..." value="${app.currentUser.photoUrl || ''}">
      </div>

      <!-- Preview container -->
      <div id="modalPhotoPreviewBox" style="text-align:center; margin:16px 0;">
        <img id="modalPhotoPreview" src="${app.currentUser.photoUrl || ''}" style="width:100px; height:100px; border-radius:50%; object-fit:cover; border:2px solid var(--border);">
      </div>

      <div style="display:flex; justify-content:flex-end; gap:10px; margin-top:16px;">
        <button class="btn btn-secondary" onclick="app.closeModal()">Cancel</button>
        <button class="btn btn-primary" onclick="profile.savePhoto()"><i class="fa-solid fa-check"></i> Set as Photo</button>
      </div>
    `;
    app.showModal(modalHtml);
  },

  handleModalPhotoFile(e) {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        document.getElementById('modalPhotoUrl').value = event.target.result;
        document.getElementById('modalPhotoPreview').src = event.target.result;
      };
      reader.readAsDataURL(file);
    }
  },

  async savePhoto() {
    const photoUrl = document.getElementById('modalPhotoUrl').value.trim();
    if (!photoUrl) {
      app.showToast('Please select or paste an image URL.', 'error');
      return;
    }

    const res = await app.api('/profile/photo', {
      method: 'POST',
      body: JSON.stringify({ photoUrl })
    });

    if (res.success) {
      app.currentUser.photoUrl = photoUrl;
      localStorage.setItem('marian_user', JSON.stringify(app.currentUser));
      app.showToast('Profile photo updated!', 'success');
      app.closeModal();
      app.renderAuthenticatedUI();
      profile.render();
    }
  }
};
