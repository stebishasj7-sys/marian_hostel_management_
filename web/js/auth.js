// ==========================================================================
// Marian Hostel Management System - Authentication & Registration
// ==========================================================================

const auth = {
  currentTab: 'student', // 'student', 'warden', 'register'
  registeredPhotoBase64: '',

  async checkWardenStatus() {
    try {
      const data = await app.api('/auth/warden-status', { method: 'GET' });
      const statusText = document.getElementById('wardenStatusText');
      const wardenTab = document.getElementById('tabWardenLogin');
      const quickWardenBtn = document.getElementById('btnQuickWarden');

      const active = data.activeWardenCount || 0;
      const available = data.isWardenLoginAvailable;

      if (available) {
        statusText.innerHTML = `Warden Login Open (${active}/2 active sessions)`;
        wardenTab.style.display = 'block';
        if (quickWardenBtn) quickWardenBtn.style.display = 'inline-block';
      } else {
        // AS REQUESTED: Once 2 wardens logged in, login as warden option is closed and no one can see that option!
        statusText.innerHTML = `<span style="color:#dc2626;"><i class="fa-solid fa-lock"></i> Warden limit full (2/2 Active) - Warden Login Closed</span>`;
        wardenTab.style.display = 'none'; // CLOSED & HIDDEN!
        if (quickWardenBtn) quickWardenBtn.style.display = 'none';

        // If currently on warden tab, redirect to student
        if (this.currentTab === 'warden') {
          this.switchAuthTab('student');
        }
      }
    } catch (e) {
      console.error(e);
    }
  },

  switchAuthTab(tab) {
    this.currentTab = tab;

    const tabStudent = document.getElementById('tabStudentLogin');
    const tabWarden = document.getElementById('tabWardenLogin');
    const tabRegister = document.getElementById('tabRegister');
    const formLogin = document.getElementById('formLogin');
    const formRegister = document.getElementById('formRegister');
    const loginRoleInput = document.getElementById('loginRole');
    const btnSubmit = document.getElementById('btnLoginSubmit');

    tabStudent.classList.remove('active');
    tabWarden.classList.remove('active');
    tabRegister.classList.remove('active');

    if (tab === 'student') {
      tabStudent.classList.add('active');
      formLogin.style.display = 'block';
      formRegister.style.display = 'none';
      loginRoleInput.value = 'STUDENT';
      btnSubmit.innerHTML = '<i class="fa-solid fa-right-to-bracket"></i> Login as Student';
      btnSubmit.className = 'btn btn-primary';
    } else if (tab === 'warden') {
      tabWarden.classList.add('active');
      formLogin.style.display = 'block';
      formRegister.style.display = 'none';
      loginRoleInput.value = 'WARDEN';
      btnSubmit.innerHTML = '<i class="fa-solid fa-shield-halved"></i> Login as Warden (Admin)';
      btnSubmit.className = 'btn btn-success';
    } else if (tab === 'register') {
      tabRegister.classList.add('active');
      formLogin.style.display = 'none';
      formRegister.style.display = 'block';
    }
  },

  fillLogin(email, password, role) {
    document.getElementById('loginEmail').value = email;
    document.getElementById('loginPassword').value = password;
    this.switchAuthTab(role.toLowerCase() === 'warden' ? 'warden' : 'student');
  },

  handlePhotoFileUpload(e) {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        this.registeredPhotoBase64 = event.target.result;
        document.getElementById('regPhotoUrl').value = 'Image Uploaded (' + Math.round(file.size / 1024) + ' KB)';
        app.showToast('Profile image loaded ready for registration!', 'success');
      };
      reader.readAsDataURL(file);
    }
  },

  handleRegRoleChange() {
    const role = document.getElementById('regRole').value;
    const roomSelect = document.getElementById('regRoom');
    if (role === 'WARDEN') {
      roomSelect.disabled = true;
    } else {
      roomSelect.disabled = false;
    }
  },

  async handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPassword').value;
    const role = document.getElementById('loginRole').value;

    const res = await app.api('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password, role })
    });

    if (res.success) {
      app.currentUser = res.user;
      localStorage.setItem('marian_user', JSON.stringify(res.user));
      app.showToast(res.message, 'success');
      app.renderAuthenticatedUI();
      app.navigate('dashboard');
      this.checkWardenStatus();
    } else {
      app.showToast(res.message || 'Login failed', 'error');
      this.checkWardenStatus();
    }
  },

  async handleRegister(e) {
    e.preventDefault();

    let photoUrl = document.getElementById('regPhotoUrl').value.trim();
    if (this.registeredPhotoBase64) {
      photoUrl = this.registeredPhotoBase64;
    } else if (!photoUrl || photoUrl.startsWith('Image Uploaded')) {
      // Default avatar if none provided
      photoUrl = 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=300&q=80';
    }

    const payload = {
      name: document.getElementById('regName').value.trim(),
      role: document.getElementById('regRole').value,
      email: document.getElementById('regEmail').value.trim(),
      password: document.getElementById('regPassword').value,
      department: document.getElementById('regDept').value.trim(),
      roomNo: document.getElementById('regRoom').value,
      phone: document.getElementById('regPhone').value.trim(),
      gender: document.getElementById('regGender').value,
      dob: document.getElementById('regDob').value,
      nationality: document.getElementById('regNationality').value.trim(),
      admissionNo: document.getElementById('regAdmissionNo').value.trim(),
      aadhaar: document.getElementById('regAadhaar').value.trim(),
      address: document.getElementById('regAddress').value.trim(),
      fatherName: document.getElementById('regFather').value.trim(),
      motherName: document.getElementById('regMother').value.trim(),
      guardianPhone: document.getElementById('regGuardianPhone').value.trim(),
      photoUrl: photoUrl
    };

    const res = await app.api('/auth/register', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      app.currentUser = res.user;
      localStorage.setItem('marian_user', JSON.stringify(res.user));
      app.showToast(res.message, 'success');
      app.renderAuthenticatedUI();
      app.navigate('profile');
      this.checkWardenStatus();
    } else {
      app.showToast(res.message || 'Registration failed', 'error');
    }
  },

  async logout() {
    await app.api('/auth/logout', { method: 'POST' });
    app.currentUser = null;
    localStorage.removeItem('marian_user');
    app.showToast('Logged out successfully.', 'info');
    app.renderUnauthenticatedUI();
    this.checkWardenStatus();
  },

  async resetWardenSessions() {
    const res = await app.api('/auth/reset-warden-logins', { method: 'POST' });
    app.showToast(res.message, 'success');
    this.checkWardenStatus();
  }
};
