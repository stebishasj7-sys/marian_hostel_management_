// ==========================================================================
// Marian Hostel Management System - Direct Private Warden Chat
// ==========================================================================

const chat = {
  activeStudentId: '',
  pollTimer: null,

  async render() {
    const container = document.getElementById('viewChat');
    container.style.display = 'block';

    const isWarden = app.currentUser.role === 'WARDEN';

    // Fetch conversations
    const res = await app.api(`/chat?studentId=${this.activeStudentId}`);

    if (isWarden && !this.activeStudentId && res.conversations && res.conversations.length > 0) {
      this.activeStudentId = res.conversations[0].studentId;
      return this.render();
    }

    const messages = res.messages || [];
    const conversations = res.conversations || [];

    container.innerHTML = `
      <div class="card" style="padding:0; overflow:hidden;">
        <div class="chat-container">
          
          <!-- Warden Student List Sidebar (Only for Warden) -->
          ${isWarden ? `
            <div class="chat-sidebar">
              <div style="font-weight:700; font-size:0.95rem; color:var(--secondary); margin-bottom:12px; display:flex; align-items:center; gap:8px;">
                <i class="fa-solid fa-user-lock" style="color:var(--primary);"></i> Private Student Threads
              </div>

              <div class="chat-thread-list">
                ${conversations.map(c => `
                  <div class="chat-thread-item ${c.studentId === this.activeStudentId ? 'active' : ''}" onclick="chat.selectStudent('${c.studentId}')">
                    <div style="font-weight:700; font-size:0.88rem; color:var(--secondary);">${c.studentName}</div>
                    <div style="font-size:0.75rem; color:var(--text-muted);">Room ${c.roomNo || 'N/A'} • ${c.department || ''}</div>
                    <div style="font-size:0.75rem; color:#64748b; margin-top:4px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
                      ${c.lastMessage}
                    </div>
                  </div>
                `).join('')}
              </div>
            </div>
          ` : ''}

          <!-- Main Chat Panel -->
          <div class="chat-main" style="${!isWarden ? 'grid-column: 1 / -1;' : ''}">
            
            <!-- Chat Header -->
            <div style="padding:16px 20px; border-bottom:1px solid var(--border); background:#f8fafc; display:flex; justify-content:space-between; align-items:center;">
              <div>
                <div style="font-size:1.05rem; font-weight:700; color:var(--secondary);">
                  ${isWarden ? `Chatting with: ${this.getActiveStudentName(conversations)}` : '<i class="fa-solid fa-shield-halved" style="color:var(--primary);"></i> Direct Warden Private Channel'}
                </div>
                <div style="font-size:0.78rem; color:var(--success); font-weight:600;">
                  <i class="fa-solid fa-lock"></i> 1-on-1 End-to-End Private Channel
                </div>
              </div>
              <button class="btn btn-outline btn-sm" onclick="chat.render()" title="Refresh Chat">
                <i class="fa-solid fa-arrows-rotate"></i>
              </button>
            </div>

            <!-- Messages Stream -->
            <div id="chatMessagesBox" class="chat-messages">
              ${messages.length === 0 ? `
                <div style="text-align:center; margin:auto; color:var(--text-muted); font-size:0.9rem;">
                  <i class="fa-regular fa-comments" style="font-size:2rem; margin-bottom:8px; display:block;"></i>
                  Start a private conversation with the Hostel Warden. Messages are confidential.
                </div>
              ` : ''}

              ${messages.map(m => {
                const isSentByMe = m.senderId === app.currentUser.id;
                return `
                  <div class="chat-bubble ${isSentByMe ? 'sent' : 'received'}">
                    <div style="font-size:0.75rem; opacity:0.8; font-weight:700; margin-bottom:2px;">
                      ${isSentByMe ? 'You' : m.senderName} (${m.senderRole})
                    </div>
                    <div>${m.message}</div>
                    <div style="font-size:0.65rem; opacity:0.7; text-align:right; margin-top:4px;">
                      ${m.timeFormatted || ''}
                    </div>
                  </div>
                `;
              }).join('')}
            </div>

            <!-- Message Input Form -->
            <form class="chat-input-bar" onsubmit="chat.handleSendMessage(event)">
              <input type="text" id="chatInputText" class="form-input" placeholder="Type confidential message to warden..." required autocomplete="off">
              <button type="submit" class="btn btn-primary" style="padding:0 20px;">
                <i class="fa-solid fa-paper-plane"></i> Send
              </button>
            </form>

          </div>

        </div>
      </div>
    `;

    // Scroll to bottom
    const box = document.getElementById('chatMessagesBox');
    if (box) {
      box.scrollTop = box.scrollHeight;
    }
  },

  getActiveStudentName(conversations) {
    const found = conversations.find(c => c.studentId === this.activeStudentId);
    return found ? `${found.studentName} (Room ${found.roomNo || 'N/A'})` : 'Student';
  },

  selectStudent(studentId) {
    this.activeStudentId = studentId;
    this.render();
  },

  async handleSendMessage(e) {
    e.preventDefault();
    const input = document.getElementById('chatInputText');
    const msg = input.value.trim();
    if (!msg) return;

    input.value = '';

    const payload = {
      studentId: this.activeStudentId,
      message: msg
    };

    const res = await app.api('/chat/send', {
      method: 'POST',
      body: JSON.stringify(payload)
    });

    if (res.success) {
      chat.render();
    } else {
      app.showToast(res.message, 'error');
    }
  }
};
